package crawlers.crawlers;

import crawlers.configuration.all;
import java.util.LinkedList;
import java.util.Queue;

import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZFrame;
import org.zeromq.ZMQ;
import org.zeromq.ZMsg;

import com.google.gson.Gson;


public class Master {

	
	public static void main(String args[]) {
		System.out.println("Master running");
		new Master().init();	
	}
	
	//Holds slaves whom ready for work
	private Queue<Slave> queueOfSlaves;
	//Router socket for Dealer-Router pattern
	private ZMQ.Socket ROUTER;
	//Publisher socket for Subscriber-Publisher pattern used to send heart beat to master
	private ZMQ.Socket PUB;
	//to read from multiple sockets
	private ZMQ.Poller poller;
	//Time to wait before sending next heart beat
	private int heartbeatInterval;
	//Time to send heart beat in msec
	private long nextHeartbeat;
	//event heart-beat
	private final String heartbeat = "001";
	//event ready-for-work;
	private final String readyforWork = "002";
	//event task is done
	private final String finishedWork = "003";
	//event task to be done
	private final String taskToBeDone = "004";
	//Address to bind-to for Dealer-Router locally
	private final String routerAddress = "tcp://127.0.0.1:5555";
	//Address to bind-to for Subscriber-Publisher locally
	private final String publisherAddress = "tcp://*:5556";
	//Subscr
	public Master() {
		queueOfSlaves = new LinkedList<Slave>();
		heartbeatInterval = all.HEARTBEAT_INTERVAL;
	}

	
	//This return first available slave's address
	public String getAvailableSlaves() {return queueOfSlaves.remove().getAddress();}
	
	public void dispatch() {
		//while there is URLs in frontier give them to slaves
		while(queueOfSlaves.size() > 0) {
			if(existUrlInFrontier()) {
				//sendTask(getAvailableSlaves());
			}
		}
	}
	
	public void sendTask(String address){
		ROUTER.sendMore(address);
		ROUTER.send(taskToBeDone, ZMQ.SNDMORE);
		ROUTER.send(getUrlFromFrontier());
		ROUTER.sendMore("");
	}
	
	private String _constructJsonWithEventAndBody(String event, String body) {
		return event + " " + body;
	}
	
	//If url exit fetch it 
	public boolean existUrlInFrontier() {
		return false;
	}
	
	//This removes slaves from the queue if they are expired
	public void killExpiredSlaves() {queueOfSlaves.removeIf(slave -> slave.getExpiration() > System.currentTimeMillis());}
	
	public void init() {
		try (ZContext context = new ZContext()) {
			
		      ROUTER = context.createSocket(SocketType.ROUTER);
		      PUB = context.createSocket(SocketType.PUB);
		      poller = context.createPoller(2);
		      
		      PUB.bind(publisherAddress);
		      ROUTER.bind(routerAddress);
		      
		      //Register two sockets in poller so to listen on both sockets
		      poller.register(ROUTER, ZMQ.Poller.POLLIN);
		      poller.register(PUB, ZMQ.Poller.POLLIN);
		      nextHeartbeat = System.currentTimeMillis() + heartbeatInterval;
		      
		      while (true) {
		    	  
		    	poller.poll(heartbeatInterval);
		    	
		    	//if it's time to send heart beat send it
		    	sendHearbeat();
		    	
		    	//message received from slave requesting for work
		    	if(poller.pollin(0)) {
		    		//The message received from slave should have three part first part is address
		    		insertSlave(ROUTER.recvStr());
		    		//second part is event type and third part should be handled by handleEvent method
		    		handleEvent(ROUTER.recvStr(), ROUTER.recvStr());
		    	}
		    	killExpiredSlaves();
		     }
		}
	}
	
	//Takes the event frame and take action upon it
	public void handleEvent(String event, String body) {
		if(event.equals(readyforWork)) {
			
		}
	}
	
	//When slave sends back response that means an crawled 
	public void handleDoneJob(String string) {
		
	}
	
	//Creates a new slave object for an address and enqueue it
	public void insertSlave(String address){queueOfSlaves.add(new Slave());}
	
	//TODO: override equal
	//This remove slave that is ready for work
	public void removeSlave(Slave slave) {
		//queueOfSlaves.remove(slave);
	}
	
	//This needs to be sent to alert slaves that master a live and if any new subscriber haven't pushed in queue and in idle state 
	public void sendHearbeat() {
		//It's time to send heart beat to all subscriber
		if(System.currentTimeMillis() > nextHeartbeat) {
			PUB.send(heartbeat);
			System.out.println("S: Heartbeat to slaves");
			nextHeartbeat = System.currentTimeMillis() + heartbeatInterval;
		}
	}
	
	//TODO: contact frontier for URL
	public String getUrlFromFrontier() {
		return "";
	}

	
}