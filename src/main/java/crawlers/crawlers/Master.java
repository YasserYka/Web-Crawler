package crawlers.crawlers;

import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.Queue;

import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;

import crawlers.modules.Seen;
import crawlers.modules.exclusion.RobotTXT;
import crawlers.modules.frontier.selector.Selector;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Master {

	
	public static void main(String args[]) throws UnknownHostException {
		System.out.println("Running");
		//PropertyConfigurator.configure("resources/log4j.properties");
		logger.info("DISPATCHER IS UP AND RUNNING");
		new Master().init();
	}
	
    private static final Logger logger = LoggerFactory.getLogger(Master.class);
	//Event to handle event in getUrlFromFrontier
	private final static String EMPTY_EVENT = "EMPTY";
	//Event to handle event in getUrlFromFrontier
	private final static String WAIT_EVENT = "WAIT";
	//Holds addresses of slaves whom ready for work
	private Queue<String> queueOfSlaves;
	//Router socket for Dealer-Router pattern
	private ZMQ.Socket ROUTER;
	//Publisher socket for Subscriber-Publisher pattern used to send heart beat to master
	private ZMQ.Socket PUB;
	//to read from multiple sockets
	private ZMQ.Poller poller;
	//Time to wait before sending next heart beat
	private final int heartbeatInterval = 5000;
	//Time to send heart beat in msec
	private long nextHeartbeat;
	//event heart-beat
	private final String heartbeat = "001";
	//event ready-for-work;
	private final String readyforWork = "002";
	//event task is done
	private final String finishedWork = "003";
	//event task to be done
	private final String workToBeDone = "004";
	//Address to bind-to for Dealer-Router locally
	private final String routerAddress = "tcp://127.0.0.1:5555";
	//Address to bind-to for Subscriber-Publisher locally
	private final String publisherAddress = "tcp://*:5556";
	
	public Master() {queueOfSlaves = new LinkedList<String>();}

	//Send work to all ready slaves
	public void dispatchWork() {
		//while there is URLs in frontier give them to slaves
		while(queueOfSlaves.size() > 0 && existUrlInFrontier()) {
			sendWorkToThisAddress(getReadySlaveAddress());
		}
	}
	
	public String getReadySlaveAddress(){
		logger.trace("SLAVE DEQUEUED FROM THE QUEUE");
		return queueOfSlaves.remove();
	}
	
	public void sendWorkToThisAddress(String address){
		ROUTER.sendMore(address);
		//Send event work-to-be-done
		ROUTER.sendMore("EVENT");
		//TODO: but the body to be sent in queue then send it index to slave
		//Send body of message
		ROUTER.send("BODY");
		logger.trace("WORK SENT TO SLAVE {}", address);
	}
	
	//If url exit fetch it 
	public boolean existUrlInFrontier() {
		return true;
	}
	
	public void init() {
		try (ZContext context = new ZContext()) {
			  System.out.println("init");
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
		    	
		    	//If there is a URL in frontier dispatch it to available slave
		    	dispatchWork();
		    	
		    	//message received from slave
		    	if(poller.pollin(0)) {
		    		//The message received from slave should have three part first part is address second part is event type and third part is body content
		    		handleMessage(ROUTER.recvStr(), ROUTER.recvStr(), ROUTER.recvStr());
		    	}
		     }
		}
	}
	
	//Takes the event frame and take action upon it
	public void handleMessage(String address, String event, String body) {
		
		logger.trace("R: MESSAGE RECEIVED FROM SLAVE {}", address);
		
		if(event.equals(readyforWork))
			insertSlave(address);
		else if(event.equals(finishedWork))
			handleFinishedWork(body);
	}
	
	//When slave sends back response that means an crawled 
	public void handleFinishedWork(String body) {
		
	}
	
	//Creates a new slave object for an address and enqueue it
	public void insertSlave(String address){
		logger.trace("SLAVE REGISTERED IN QUEUE WITH ADDRESS", address);
		queueOfSlaves.add(address);
	}
	
	
	//This needs to be sent to alert slaves that master a live and if any new subscriber haven't pushed in queue and in idle state 
	public void sendHearbeat() {
		//It's time to send heart beat to all subscriber
		if(System.currentTimeMillis() > nextHeartbeat) {
			PUB.send(heartbeat);
			logger.trace("S: HEARTBEAT TO SLAVE");
			nextHeartbeat = System.currentTimeMillis() + heartbeatInterval;
		}
	}
	
	//Call Selector class to fetch URL from frontier
	public String getUrlFromFrontier() {
		String response = Selector.Select();
		
		//TODO: Deal with it two first conditions
		if(response == EMPTY_EVENT)
			return null;
		else if(response == WAIT_EVENT)
			return null;
		else
			return response;
	}

}