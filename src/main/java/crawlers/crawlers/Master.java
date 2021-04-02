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

		if (standby){
			logger.info("MASTER IS RUNNING IN STANDBY MODE WAITING FOR ACTIVE MASTER TO DIE");
			new Master().passive();		
		}
		else{
			logger.info("MASTER IS UP AND RUNNING IN ACTIVE MODE");
			new Master().active();
		} 
		
	}

	private static final Logger logger = LoggerFactory.getLogger(Master.class);
	// Holds addresses of slaves whom ready for work
	private Queue<String> queueOfSlaves = new LinkedList<String>();
	// router socket for Dealer-router pattern
	private ZMQ.Socket router;
	// publisher socket for Subscriber-publisher pattern used to send heart beat to
	// master
	private ZMQ.Socket publisher;	
	// Address to bind-to for Subscriber-publisher locally
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
	// Address to bind-to for Dealer-router locally
	private final static String router_ADDRESS = "tcp://127.0.0.1:5555";
	// Address to bind-to for Subscriber-publisher locally
	private final static String publisher_ADDRESS = "tcp://*:5556";
	// Redis instance
	private CacheService cacheService = new CacheService(REDIS_INSTNACE_NAME);
	// Master's instance name
	private final static String REDIS_INSTNACE_NAME = "master";
	// Liveness of the active master (when we don't receive heart beat form master 10 times
	// (10 heart beat intervals) means the active master is down)
	private final static int LIVNESS_OF_ACTIVE_MASTER = 10;
	// Counter for liveness of master
	private int liveness = LIVNESS_OF_ACTIVE_MASTER;


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
		router.sendMore(address);
		// Send event work-to-be-done
		router.sendMore(WORK_TO_BE_DONE_EVENT);
		// Send body of message
		router.send(Frontier.get());

		logger.info("Work sent to slave {}", address);
	}
	
	// Checks if there is ready URL in frontier
	public static boolean urlReadyInFrontier() {
		return Frontier.ready(); 
	}

	// Master running in standby will just keep receiving heartbeat from active master util it stop then it will be promoted to run in active mode   
	public void passive() {

		// to receive heartbeat from active master
		ZMQ.Socket subscriber;

		try (ZContext context = new ZContext()) {

			subscriber = context.createSocket(SocketType.SUB);

			// Sub subscribe to all kind of message of master (disable filtering)
			subscriber.subscribe(ZMQ.SUBSCRIPTION_ALL);

			subscriber.connect(SUBSCRIBER_ADDRESS);

			// don't block if not received anything
			subscriber.setReceiveTimeOut(HEARTBEAT_INTERVAL + 2500);
			
			while (true) {

				String messageReceived = subscriber.recvStr();

				if(messageReceived == null){
					logger.info("Master haven't sent heartbeat yet, the livness before operating self promoting {}", liveness);

					if (--liveness == 0)
						break;
				}
				else
					logger.info("Heartbeats received from active master");

			}

			// one reason is caused to break from above loop which is the livness reached limit
			selfPromoting(context, subscriber);

		}
	}

	public void selfPromoting(ZContext context, ZMQ.Socket subscriber){

		context.destroySocket(subscriber);
		logger.info("Self promoting after not receiving heartbeats from active master, this master is now active");
		active();
	}
	
	public void active() {

		try (ZContext context = new ZContext()) {
			
			  router = context.createSocket(SocketType.ROUTER);
		      publisher = context.createSocket(SocketType.PUB);
		      poller = context.createPoller(2);
		      
		      publisher.bind(publisher_ADDRESS);
		      router.bind(router_ADDRESS);
		      
		      // Register two sockets in poller so to listen on both sockets
		      poller.register(router, ZMQ.Poller.POLLIN);
		      poller.register(publisher, ZMQ.Poller.POLLIN);
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
					handleMessage(router.recvStr(), router.recvStr(), router.recvStr());
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
			publisher.send(HEARTBEAT_EVENT);
			logger.info("Heartbeat sent to slaves");
			nextHeartbeat = System.currentTimeMillis() + HEARTBEAT_INTERVAL;
		}
	}

}