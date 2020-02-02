package crawlers.crawlers;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Random;

import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import org.redisson.Redisson;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

public class Slave {

	public static void main(String args[]) {
		System.out.println("Slave is up and running");
		new Slave().init();	
	}
	
	//TODO: set user-agent header to googlebot or any other crawler 
	
	//Address of slave
	private String address;
	//Dealer socket for Dealer-Router pattern
	private ZMQ.Socket DLR; 
	//subscriber socket for Subscriber-Publisher pattern used to receive heart beat from master
	private ZMQ.Socket SUB;
	//to read from multiple sockets
	private ZMQ.Poller poller;
	//This set to true if work not done yet
	private boolean busy;
	//Master will send heart beats every 5mscs
	private final int heartbeatInterval = 5000;
	//Liveness of the master (when we don't receive heart beat form master 10 times (10 heart beat intervals) means the master is down)
	private final int livenessOfMaster = 10;
	//event heart-beat
	private final String heartbeat = "001";
	//event ready-for-work;
	private final String readyforWork = "002";
	//event task is done
	private final String finishedWork = "003";
	//event task to be done
	private final String workToBeDone = "004";
	//Counter for liveness of master
    private int liveness;
	//Address to bind-to for Dealer-Router locally
	private final String dealerAddress = "tcp://127.0.0.1:5555";
	//Address to bind-to for Subscriber-Publisher locally
	private final String subscriberAddress = "tcp://localhost:5556";
	//Used to generate unique identity
	private Random random;
	//Instance of Redis
	private RedissonClient redisson;
	//Redis based distributed Map
	private RMap<String, String> cache;
	//Counter for RMap
	private int counter;
	//Apache's http client instance
    private  CloseableHttpClient httpClient;

	protected Slave() {
        busy = false;
        liveness = livenessOfMaster;
        random = new Random(System.nanoTime());
	}
	
	public void init() {
		try (ZContext context = new ZContext()) {
			
			DLR = context.createSocket(SocketType.DEALER);
			SUB = context.createSocket(SocketType.SUB);
		    poller = context.createPoller(2);
		    
		    //Set identity for master
		    address = String.format("%04X-%04X", random.nextInt(), random.nextInt());
		    DLR.setIdentity(address.getBytes(ZMQ.CHARSET));
		    
		    //Sub subscribe to all kind of message of master (disable filtering)
		    SUB.subscribe(ZMQ.SUBSCRIPTION_ALL);
		    
		    DLR.connect(dealerAddress);
		    SUB.connect(subscriberAddress);
		    
		    poller.register(DLR, ZMQ.Poller.POLLIN);
		    poller.register(SUB, ZMQ.Poller.POLLIN); 
		    
		    establishConnectionToCache();

		    while (true) {
		    	//check for message in this interval
		    	poller.poll(heartbeatInterval);
		    	
		    	//Received message from master via dealer
		    	if(poller.pollin(0)) {
		    		String event = DLR.recvStr();
		    		String body = DLR.recvStr();
		    	}
		        
		    	//Heart beat from master
		    	if(poller.pollin(1)) {
		    		String messageReceived = SUB.recvStr();

		    		//If message from master is a heart beat handle it other wise 
		    		if(messageReceived.equals(heartbeat))
		    			handleHeartbeat();
		    		else
		    			handleWrongMessage();
		    	}else
		    		//if liveness equal zero means master is down call selfDestruction
		    		if(--liveness == 0)
		    			selfDestruction(context);
		      }
		}
	}
	
	//Gets URL and start crawling
	public void crawle(String url) {
		
		//TODO: how to contact the DNS resolver RMI maybe?
		InetAddress address = null; //Represent address from DNS resolver
		//make get request
		String document = makeRequest(address);
		
		String key = generateKey();
		
		busy = true;
		
		addToCache(key, document);
		
		handleFinishedWork(key);
		
	}
	
	public String generateKey() {
		return DLR.getIdentity().toString() + counter++;
	}
	
	public void addToCache(String key, String document) {
		cache.fastPutAsync(key, document);
	}
	
	//Takes key of where the document was stored in cache
	public void handleFinishedWork(String key) {
		DLR.sendMore(finishedWork);
		DLR.sendMore(key);
		DLR.send("");
		System.out.println("S: FINISHED WORK SENT");
	}
	
	//when received message not like what we expected
	public void handleWrongMessage() {System.out.println("Received: invalid message\n");}
	
	//check if heart beat is correct message if true reset liveness if no call handleWrongMessage
	public void handleHeartbeat(){
		System.out.println("R: HEARTBEAT RECIVED");
		liveness = heartbeatInterval;
    	if(!busy)
    		sendRequestForWork();
	}

	//When master dosen't send a heart beat for long time kill this whole thread
	public void selfDestruction(ZContext context){
		System.out.println("Self destructing...");
		context.destroySocket(DLR);
		context.destroySocket(SUB);
		System.exit(0);
	}
	
	public void establishConnectionToCache() {
		redisson = Redisson.create(cacheConfiguration());
		cache = redisson.getMap("test");
	}
	
	public void sendRequestForWork() {
		DLR.sendMore(readyforWork);
		DLR.send("");
		System.out.println("S: REQUEST FOR WORK");
	}
	
	public Config cacheConfiguration() {
		Config config = new Config();
		config.useSingleServer().setAddress("127.0.0.1:6379");
		return config;
	}
	
	//Gets host-name's as IntetAddress format then makes get request and returns it's body as String
	public String makeRequest(InetAddress address) {
		//getHostAddress convert InetAddress to string presentation
		HttpGet request = new HttpGet(address.getHostAddress());
		httpClient = HttpClients.createDefault();
        HttpEntity entity = null;
		String content = "";
		
		request.addHeader(HttpHeaders.USER_AGENT, "Googlebot");
		
        try (CloseableHttpResponse response = httpClient.execute(request)) {
        	response.getAllHeaders().toString();
        	if(response.getStatusLine().getStatusCode() != 200) {
        		entity = response.getEntity();
        		content = response.getAllHeaders().toString();
        	}
        		
        	if(entity != null)
        		content += EntityUtils.toString(entity);
		}
        catch(ClientProtocolException cpe) {/*TODO: LOG IT*/}
        catch (IOException ie) {/*TODO:LOG IT*/}
        
        return content;
	}
	
}
