package crawlers.crawlers;

import java.util.LinkedList;
import java.util.Queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;

import crawlers.modules.Preprocessor;
import crawlers.modules.frontier.Frontier;
import crawlers.storage.CacheService;

public class Master {

	public static void main(String args[]) {

		// 'standby' is environment properties will be passed as command line argument as -Dstandby="true"
		boolean standby = Boolean.getBoolean("standby");

		if (standby)
			logger.info("MASTER IS RUNNING IN STANDBY MODE WAITING FOR ACTIVE MASTER TO DIE");
		else 
			logger.info("MASTER IS UP AND RUNNING IN ACTIVE MODE");
		
	}

	private static final Logger logger = LoggerFactory.getLogger(Master.class);
	// Holds addresses of slaves whom ready for work
	private Queue<String> queueOfSlaves = new LinkedList<String>();
	// Router socket for Dealer-Router pattern
	private ZMQ.Socket ROUTER;
	// Publisher socket for Subscriber-Publisher pattern used to send heart beat to
	// master
	private ZMQ.Socket PUBLISHER;
	// subscriber socket for Subscriber-Publisher pattern used to receive heartbeat from active master
	private ZMQ.Socket SUBSCRIBER;
	// Address to bind-to for Subscriber-Publisher locally
	private final String SUBSCRIBER_ADDRESS = "tcp://localhost:5556";
	// to read from multiple sockets
	private ZMQ.Poller poller;
	// Time to wait before sending next heart beat
	private final static int HEARTBEAT_INTERVAL = 10000;
	// Time to send heart beat in msec
	private long nextHeartbeat;
	// event heart-beat
	private final static String HEARTBEAT_EVENT = "001";
	// event ready-for-work;
	private final static String READY_FOR_WORK_EVENT = "002";
	// event task is done
	private final static String WORK_FINISHED_EVENT = "003";
	// event task to be done
	private final static String WORK_TO_BE_DONE_EVENT = "004";
	// Address to bind-to for Dealer-Router locally
	private final static String ROUTER_ADDRESS = "tcp://127.0.0.1:5555";
	// Address to bind-to for Subscriber-Publisher locally
	private final static String PUBLISHER_ADDRESS = "tcp://*:5556";
	// Redis instance
	private CacheService cacheService = new CacheService(REDIS_INSTNACE_NAME);
	// Master's instance name
	private final static String REDIS_INSTNACE_NAME = "master";
	// for achieving passive-active availability
	private final boolean standby;


	public Master(boolean standby){

		this.standby = standby;
	}

	// Send work to all ready slaves
	public void dispatchWork() {
		// while there is URLs in frontier give them to slaves
		while(queueOfSlaves.size() > 0 && urlReadyInFrontier())
			sendWorkTo(getReadySlaveAddress());

	}
	
	public String getReadySlaveAddress(){
		logger.info("Slave dequeued from the queue");
		return queueOfSlaves.remove();
	}
	
	public void sendWorkTo(String address){
		// First packet must be the address of the slave
		ROUTER.sendMore(address);
		// Send event work-to-be-done
		ROUTER.sendMore(WORK_TO_BE_DONE_EVENT);
		// Send body of message
		ROUTER.send(Frontier.get());

		logger.info("Work sent to slave {}", address);
	}
	
	// Checks if there is ready URL in frontier
	public static boolean urlReadyInFrontier() {
		return Frontier.ready(); 
	}

	// Master running in standby will just keep receiving heartbeat from active master util it stop then it will be promoted to run in active mode   
	public void passive() {
		try (ZContext context = new ZContext()) {

			// Sub subscribe to all kind of message of master (disable filtering)
			SUBSCRIBER = context.createSocket(SocketType.SUB);

			// Sub subscribe to all kind of message of master (disable filtering)
			SUBSCRIBER.subscribe(ZMQ.SUBSCRIPTION_ALL);

			SUBSCRIBER.connect(SUBSCRIBER_ADDRESS);

			while (true) {

				String messageReceived = SUBSCRIBER.recvStr();
			}
		}
	}
	
	public void active() {
		try (ZContext context = new ZContext()) {
			
			  ROUTER = context.createSocket(SocketType.ROUTER);
		      PUBLISHER = context.createSocket(SocketType.PUB);
		      poller = context.createPoller(2);
		      
		      PUBLISHER.bind(PUBLISHER_ADDRESS);
		      ROUTER.bind(ROUTER_ADDRESS);
		      
		      // Register two sockets in poller so to listen on both sockets
		      poller.register(ROUTER, ZMQ.Poller.POLLIN);
		      poller.register(PUBLISHER, ZMQ.Poller.POLLIN);
		      nextHeartbeat = System.currentTimeMillis() + HEARTBEAT_INTERVAL;
		      
		      while (true) {
				poller.poll(HEARTBEAT_INTERVAL);

				// If it's time to send heart beat send it
				sendHearbeat();

		    	// If there is a URL in frontier dispatch it to available slave
				dispatchWork();
						    	
		    	// Message received from slave
		    	if(poller.pollin(0))
		    		// The message received from slave should have three part first part is address second part is event type and third part is body content
					handleMessage(ROUTER.recvStr(), ROUTER.recvStr(), ROUTER.recvStr());
		     }
		}
	}
	
	// Takes the event frame and take action upon it
	public void handleMessage(String frame1, String frame2, String frame3) {
		logger.info("Message received from slave {}", frame1);
		if(frame2.equals(READY_FOR_WORK_EVENT))
			insertSlave(frame1);
		else if(frame2.equals(WORK_FINISHED_EVENT))
			handleFinishedWork(frame3);
	}
	
	// When slave sends back response that means an crawled 
	public void handleFinishedWork(String key) {
		logger.info("Thread created for preprosscing : {}", key);
		new Preprocessor(cacheService.get(key), key);
	}

	// Creates a new slave object for an address and enqueue it
	public void insertSlave(String address){
		queueOfSlaves.add(address);
		logger.info("Slave registered in queue with address {}", address);
	}
	
	// This needs to be sent to alert slaves that master a live and if any new subscriber haven't pushed in queue and in idle state 
	public void sendHearbeat() {
		// It's time to send heart beat to all subscriber
		if(System.currentTimeMillis() > nextHeartbeat) {
			PUBLISHER.send(HEARTBEAT_EVENT);
			logger.info("Heartbeat sent to slaves");
			nextHeartbeat = System.currentTimeMillis() + HEARTBEAT_INTERVAL;
		}
	}

}