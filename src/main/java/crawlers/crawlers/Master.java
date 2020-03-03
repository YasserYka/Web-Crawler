package crawlers.crawlers;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.CompletableFuture;

import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;

import crawlers.modules.RelativeUrlResolver;
import crawlers.modules.Seen;
import crawlers.modules.exclusion.RobotTXT;
import crawlers.modules.filter.Filter;
import crawlers.modules.frontier.selector.Selector;
import crawlers.url.UrlLexer;
import crawlers.util.Cache;
import crawlers.util.FakeData;
import io.netty.util.concurrent.CompleteFuture;
import io.reactivex.Completable;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.PropertyConfigurator;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Master {

	
	public static void main(String args[]) throws UnknownHostException {
		logger.info("MASTER IS UP AND RUNNING");
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
	private ZMQ.Socket PUBLISHER;
	//to read from multiple sockets
	private ZMQ.Poller poller;
	//Time to wait before sending next heart beat
	private final static int HEARTBEAT_INTERVAL = 5000;
	//Time to send heart beat in msec
	private long nextHeartbeat;
	//event heart-beat
	private final  static String HEARTBEAT = "001";
	//event ready-for-work;
	private final static String READY_FOR_WORK = "002";
	//event task is done
	private final static String WORK_FINISHED = "003";
	//event task to be done
	private final static String WORK_TO_BE_DONE = "004";
	//Address to bind-to for Dealer-Router locally
	private final static String ROUTER_ADDRESS = "tcp://127.0.0.1:5555";
	//Address to bind-to for Subscriber-Publisher locally
	private final static String PUBLISHER_ADDRESS = "tcp://*:5556";
	//Redis's based distributed Map
	private static RMap<String, String> cache = null;

	
	public Master() {queueOfSlaves = new LinkedList<String>();}

	//Send work to all ready slaves
	public void dispatchWork() {
		//while there is URLs in frontier give them to slaves
		while(queueOfSlaves.size() > 0 && existUrlInFrontier()) {
			sendWorkToThisAddress(getReadySlaveAddress());
		}
	}
	
	public String getReadySlaveAddress(){
		logger.info("SLAVE DEQUEUED FROM THE QUEUE");
		return queueOfSlaves.remove();
	}
	
	public void sendWorkToThisAddress(String address){
		ROUTER.sendMore(address);
		//Send event work-to-be-done
		ROUTER.sendMore(WORK_TO_BE_DONE);
		//TODO: but the body to be sent in queue then send it index to slave
		//Send body of message
		ROUTER.send(FakeData.DOMAINNAME);
		logger.info("WORK SENT TO SLAVE {}", address);
	}
	
	//If url exit fetch it 
	public boolean existUrlInFrontier() {
		return true;
	}
	
	public void init() {
		try (ZContext context = new ZContext()) {

			  cache = Cache.initializeCache();
			
			  ROUTER = context.createSocket(SocketType.ROUTER);
		      PUBLISHER = context.createSocket(SocketType.PUB);
		      poller = context.createPoller(2);
		      
		      PUBLISHER.bind(PUBLISHER_ADDRESS);
		      ROUTER.bind(ROUTER_ADDRESS);
		      
		      //Register two sockets in poller so to listen on both sockets
		      poller.register(ROUTER, ZMQ.Poller.POLLIN);
		      poller.register(PUBLISHER, ZMQ.Poller.POLLIN);
		      nextHeartbeat = System.currentTimeMillis() + HEARTBEAT_INTERVAL;
		      
		      while (true) {
		    	  
		    	poller.poll(HEARTBEAT_INTERVAL);
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
	public void handleMessage(String frame1, String frame2, String frame3) {
		
		logger.info("MESSAGE RECEIVED FROM SLAVE {}", frame1);

		if(frame2.equals(READY_FOR_WORK))
			insertSlave(frame1);
		else if(frame2.equals(WORK_FINISHED))
			handleFinishedWork(frame3);
	}
	
	//When slave sends back response that means an crawled 
	public void handleFinishedWork(String key) {
		logger.info("SLAVE FINISHED CRAWLING THIS DOMAINNAME {}", key);
				
		CompletableFuture.supplyAsync(() -> cache.get(key))
			.thenApplyAsync(doc -> UrlLexer.extractURLs(doc))
				.thenApplyAsync(urls -> RelativeUrlResolver.normalize(key, urls))
					.thenApplyAsync(urls -> Filter.drop(urls))
						.thenApplyAsync(urls -> RobotTXT.filter(key, urls))
							.thenAcceptAsync(urls -> Seen.filter(urls));
		

	}

	//Creates a new slave object for an address and enqueue it
	public void insertSlave(String address){
		logger.info("SLAVE REGISTERED IN QUEUE WITH ADDRESS {}", address);
		queueOfSlaves.add(address);
	}
	
	
	//This needs to be sent to alert slaves that master a live and if any new subscriber haven't pushed in queue and in idle state 
	public void sendHearbeat() {
		//It's time to send heart beat to all subscriber
		if(System.currentTimeMillis() > nextHeartbeat) {
			PUBLISHER.send(HEARTBEAT);
			logger.info("HEARTBEAT SENT TO SLAVES");
			nextHeartbeat = System.currentTimeMillis() + HEARTBEAT_INTERVAL;
		}
	}
	
	//Call Selector class to fetch URL from frontier
	public String getUrlFromFrontier() {
		String response = Selector.select();
		return null;
	}

}