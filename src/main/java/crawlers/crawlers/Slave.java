package crawlers.crawlers;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Random;

import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;

import crawlers.modules.DNSResolution;
import crawlers.util.FakeData;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.redisson.Redisson;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Slave {

	public static void main(String args[]) {
		logger.info("THE CRAWLER IS UP AND RUNNING");
		new Slave().init();
	}
	
	private static final Logger logger = LoggerFactory.getLogger(Slave.class);
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
	private final static int HEARTBEAT_INTERVAL = 5000;
	//Liveness of the master (when we don't receive heart beat form master 10 times (10 heart beat intervals) means the master is down)
	private final static int LIVNESS_OF_MASTER = 10;
	//event heart-beat
	private final static String HEARTBEAT = "001";
	//event ready-for-work;
	private final static String READY_FOR_WORK = "002";
	//event task is done
	private final static String WORK_FINISHED = "003";
	//event task to be done
	private final static String WORK_TO_BE_DONE = "004";
	//Counter for liveness of master
    private int liveness;
	//Address to bind-to for Dealer-Router locally
	private final String DEALER_ADDRESS = "tcp://127.0.0.1:5555";
	//Address to bind-to for Subscriber-Publisher locally
	private final String SUBSCRIBER_ADDRESS = "tcp://localhost:5556";
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
    //default URL of Redis
    private final static String REDIS_ADDRESS = "redis://127.0.0.1:6379";

	protected Slave() {
        busy = false;
        liveness = LIVNESS_OF_MASTER;
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
		    logger.info("ADDRESS CREATED {}", address);
		    
		    //Sub subscribe to all kind of message of master (disable filtering)
		    SUB.subscribe(ZMQ.SUBSCRIPTION_ALL);
		    
		    DLR.connect(DEALER_ADDRESS);
		    SUB.connect(SUBSCRIBER_ADDRESS);
		    
		    poller.register(DLR, ZMQ.Poller.POLLIN);
		    poller.register(SUB, ZMQ.Poller.POLLIN); 
		    
		    establishConnectionToCache();

		    while (true) {
		    	//check for message in this interval
		    	poller.poll(HEARTBEAT_INTERVAL);
		    	
		    	//Received message from master via dealer
		    	if(poller.pollin(0)) {
		    		String event = DLR.recvStr();
		    		String body = DLR.recvStr();
		    		if(event.equals(WORK_TO_BE_DONE)) {
			    		logger.info("WORK RECIVED FORM MASTER WITH BODY {}", body);
		    			crawl(body);
		    		}
		    	}
		        
		    	//Heart beat from master
		    	if(poller.pollin(1)) {
		    		String messageReceived = SUB.recvStr();

		    		//If message from master is a heart beat handle it other wise 
		    		if(messageReceived.equals(HEARTBEAT))
		    			handleHeartbeat();
		    		else
		    			handleWrongMessage(messageReceived);
		    	}else{
					logger.info("MASTER HAVEN'T SENT HEARTBEAT YET, THE LIVNESS BEFORE OPERATING SELF DESTRUCTION {}", liveness);
					//if liveness equal zero means master is down call selfDestruction
		    		if(--liveness == 0)
						selfDestruction(context);
				}
		      }
		}
	}
	
	public void addToCache(String key, String document) {
		cache.fastPutAsync(key, document);
	}
	
	//Takes key of where the document was stored in cache
	public void handleFinishedWork(String key) {
		DLR.sendMore(WORK_FINISHED);
		DLR.send(key);
		logger.info("FINISHED WORK SENT");
		busy = false;
	}
	
	//when received message not like what we expected
	public void handleWrongMessage(String messageReceived) {logger.error("INVALID MESSAGE RECEIVED {}", messageReceived);}
	
	//check if heart beat is correct message if true reset liveness if no call handleWrongMessage
	public void handleHeartbeat(){
		logger.info("HEARTBEAT RECIVED");
		liveness = LIVNESS_OF_MASTER;
    	if(!busy)
    		sendRequestForWork();
	}

	//When master dosen't send a heart beat for long time kill this whole thread
	public void selfDestruction(ZContext context){
		logger.info("OPERATING SELF DESTRUCTION");
		context.destroySocket(DLR);
		context.destroySocket(SUB);
		System.exit(0);
	}
	
	public void establishConnectionToCache() {
		redisson = Redisson.create(cacheConfiguration());
		cache = redisson.getMap("test");
	}
	
	public void sendRequestForWork() {
		DLR.sendMore(READY_FOR_WORK);
		DLR.send(" ");
		logger.info("REQUEST FOR WORK SENT");
	}
	
	public Config cacheConfiguration() {
		Config config = new Config();
		config.useSingleServer().setAddress(REDIS_ADDRESS);
		return config;
	}
	
	//Gets host-name's as IntetAddress format then makes get request and returns it's body as String
	public String makeRequest(URI uri) {
		//getHostAddress convert InetAddress to string presentation
		HttpGet request = new HttpGet(uri);
		httpClient = HttpClients.createDefault();
        HttpEntity entity = null;
		String content = "";
		
		request.addHeader(HttpHeaders.USER_AGENT, "Googlebot");
		
        try (CloseableHttpResponse response = httpClient.execute(request)) {
        	
        	logger.info("HTTP REQUEST HAS BEEN SENT TO {}", uri.toURL());
        	
        	response.getAllHeaders().toString();
        	
        	if(response.getStatusLine().getStatusCode() == 200){
        		entity = response.getEntity();
        		content = response.getAllHeaders().toString();
        	}
        		
        	if(entity != null)
        		content += EntityUtils.toString(entity);
		}
        catch(ClientProtocolException cpe) {logger.error("SOMETHING WENT WORNG {}",cpe);}
        catch (IOException ie) {logger.error("SOMETHING WENT WORNG {}",ie);}
        
        return content;
	}
	
	//TODO: Call resolver in some way
	//TODO: for now ill call it via fs
	public void crawl(String domainName){
		logger.info("REQUEST SENT TO DOMAINNAME {}", domainName);
		busy = true; 
		String address = DNSResolution.resolveHostnameToIP(domainName).getHostAddress();
		URI uri = buildUri(address);
		addToCache(domainName, FakeData.HTML_DOCUMENT);
		handleFinishedWork(domainName);
	}	
	
	public URI buildUri(String address) {
		try {return new URIBuilder().setScheme("http").setHost(address).build();} catch (URISyntaxException e) {logger.debug(e.toString());}
		return null;
	}
}
