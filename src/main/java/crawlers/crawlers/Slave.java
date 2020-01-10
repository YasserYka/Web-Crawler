package crawlers.crawlers;

import java.util.Random;

import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZFrame;
import org.zeromq.ZMQ;
import org.zeromq.ZMsg;

public class Slave {

	public static void main(String args[]) {
		System.out.println("SLave running");
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
	//Expires at this time
	private long expiration;
	//This set to true if work not done yet
	private boolean busy;
	//Master will send heart beats every 5mscs
	private final int heartbeatInterval = 5000;
	//Liveness of the master (when we don't receive heart beat form master 3 times (3 heart beat intervals) means the master is down)
	private final int livenessOfMaster = 3;
	//event heart-beat
	private final String heartbeat = "\001";
	//event ready-for-work;
	private final String readyforTask = "\002";
	//event task is done
	private final String finishedTask = "\003";
	//Counter for liveness of master
    private int liveness;
	//Address to bind-to for Dealer-Router locally
	private final String dealerAddress = "tcp://127.0.0.1:5555";
	//Address to bind-to for Subscriber-Publisher locally
	private final String subscriberAddress = "tcp://127.0.0.1:5556";
	//Used to generate unique identity
	private Random random;


	protected Slave() {
        busy = false;
        liveness = livenessOfMaster;
        busy = false;
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
		    
		    DLR.connect(dealerAddress);
		    SUB.connect(subscriberAddress);
		    
		    poller.register(DLR, ZMQ.Poller.POLLIN);
		    poller.register(SUB, ZMQ.Poller.POLLIN); 

		    while (true) {
		    	ZMsg message = null;
		    	poller.poll(heartbeatInterval);
		    	
		    	
		    	//Received message contain work to be done from master
		    	if(poller.pollin(0)) {
		    		message = ZMsg.recvMsg(DLR);
		    		doTask(message.getFirst().toString());
		    	}
		        
		    	//Heart beat from master
		    	if(poller.pollin(1)) {
		    		message = ZMsg.recvMsg(SUB);
		    		
		    		//Heart beat would have size = 1 (only one frame indicate liveness of slave)
		    		if(message.size() != 1)
		    			handleWrongMessage(message);
		    		else {
		    			handleHeartbeat(message);
			    		//Send to Master address, don't response if busy working

		    		}
		    	}else
		    		//if liveness equal zero means master is down call selfDestruction
		    		if(--liveness == 0)
		    			selfDestruction(context);
		      }
		}
	}
	
	//Gets URL and start crawling
	public void doTask(String url) {
		
	}
	
	//when received message not like what we expected
	public void handleWrongMessage(ZMsg message) {
        System.out.println("Received: invalid message\n");
        message.dump(System.out);
	}
	
	//check if heart beat is correct message if true reset liveness if no call handleWrongMessage
	public void handleHeartbeat(ZMsg message){
		if(heartbeat.equals(new String(message.getFirst().getData(), ZMQ.CHARSET))) {
			liveness = heartbeatInterval;
    		if(!busy)
    			createAndSendMessage(readyforTask, "");
		}
		else
			handleWrongMessage(message);
	}
	
	public long getExpiration() {
		return expiration;
	}
	
	public String getAddress() {
		return address;
	}

	//TODO: Repressing the Enum events as byte
	//This sends event as byte asking for Work
	public void requestWork() {
		ZMsg request = new ZMsg();
		request.add(address);
		request.send(DLR);
	}
	
	//When master dosen't send a heart beat for long time kill this whole thread
	public void selfDestruction(ZContext context){
		System.out.println("Self destructing...");
		context.destroySocket(DLR);
		context.destroySocket(SUB);
		System.exit(0);
	}
	
	public void createAndSendMessage(String event, String body) {
		ZMsg message = new ZMsg();
		message.add(address);
		message.add(event);
		message.add(body);
	}
	
}
