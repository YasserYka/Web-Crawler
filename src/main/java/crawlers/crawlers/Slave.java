package crawlers.crawlers;

import java.util.Random;

import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;

import crawlers.storage.CacheService;
import crawlers.util.Fake;

import org.apache.http.impl.client.CloseableHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Slave {

	public static void main(String args[]) {
		logger.info("THE CRAWLER IS UP AND RUNNING");
		new Slave().run();
	}

	private static final Logger logger = LoggerFactory.getLogger(Slave.class);
	// Address of slave
	private String address;
	// Dealer socket for Dealer-Router pattern
	private ZMQ.Socket DLR;
	// subscriber socket for Subscriber-Publisher pattern used to receive heartbeat from master
	private ZMQ.Socket SUB;
	// to read from multiple sockets
	private ZMQ.Poller poller;
	// This set to true if work not done yet
	private boolean busy = false;
	// This set to true if ready for work event sent which means waiting for work
	private boolean ready_for_work_sent = false;
	// Master will send heart beats every 5mscs
	private final static int EXPECTED_HEARTBEAT_INTERVAL = 12500;
	// Liveness of the master (when we don't receive heart beat form master 10 times
	// (10 heart beat intervals) means the master is down)
	private final static int LIVNESS_OF_MASTER = 10;
	// event heart-beat
	private final static String HEARTBEAT = "001";
	// event ready-for-work;
	private final static String READY_FOR_WORK = "002";
	// event task is done
	private final static String WORK_FINISHED = "003";
	// event task to be done
	private final static String WORK_TO_BE_DONE = "004";
	// Counter for liveness of master
	private int liveness = LIVNESS_OF_MASTER;
	// Address to bind-to for Dealer-Router locally
	private final String DEALER_ADDRESS = "tcp://127.0.0.1:5555";
	// Address to bind-to for Subscriber-Publisher locally
	private final String SUBSCRIBER_ADDRESS = "tcp://localhost:5556";
	// Used to generate unique identity
	private Random random = new Random(System.nanoTime());
	// Apache's http client instance
	private CloseableHttpClient httpClient;
	// Master's instance name
	private final static String REDIS_INSTNACE_NAME = "slave";
	// Redis instance
	private CacheService cacheService = new CacheService(REDIS_INSTNACE_NAME);


	public void run() {
		try (ZContext context = new ZContext()) {

			DLR = context.createSocket(SocketType.DEALER);
			SUB = context.createSocket(SocketType.SUB);
			poller = context.createPoller(2);

			boolean pollin_SUB = false;
			boolean pollin_DLR = false;

			// Set identity for master
			address = String.format("%04X-%04X", random.nextInt(), random.nextInt());
			DLR.setIdentity(address.getBytes(ZMQ.CHARSET));
			logger.info("Address created {}", address);

			// Sub subscribe to all kind of message of master (disable filtering)
			SUB.subscribe(ZMQ.SUBSCRIPTION_ALL);

			DLR.connect(DEALER_ADDRESS);
			SUB.connect(SUBSCRIBER_ADDRESS);

			poller.register(DLR, ZMQ.Poller.POLLIN);
			poller.register(SUB, ZMQ.Poller.POLLIN);

			while (true) {
				// check for message in this interval
				poller.poll(EXPECTED_HEARTBEAT_INTERVAL);

				// Received message from master via dealer

				pollin_DLR = poller.pollin(0);

				if (pollin_DLR) {
					String event = DLR.recvStr();
					String body = DLR.recvStr();
					if (event.equals(WORK_TO_BE_DONE)) {
						logger.info("Work received {}", body);
						crawl(body);
					}
				}

				// Heart beat from master
				pollin_SUB = poller.pollin(1);

				if (pollin_SUB) {
					String messageReceived = SUB.recvStr();

					// If message from master is a heart beat handle it other wise
					if (messageReceived.equals(HEARTBEAT))
						handleHeartbeat();
					else
						handleWrongMessage(messageReceived);
				}

				if (!pollin_SUB && !pollin_DLR) {
					logger.info("Master haven't sent heartbeat yet, the livness before operating self destruction {}",
							liveness);
					// if liveness equal zero means master is down call selfDestruction
					if (--liveness == 0)
						selfDestruction(context);
				}
			}
		}
	}

	// Takes key of where the document was stored in cache
	public void handleFinishedWork(String key) {
		DLR.sendMore(WORK_FINISHED);
		DLR.send(key);
		logger.info("Finished work event sent");
		busy = ready_for_work_sent = false;
	}

	//when received message not like what we expected
	public void handleWrongMessage(String messageReceived) {
		logger.error("Invalid message received {}", messageReceived);
	}
	
	//check if heart beat is correct message if true reset liveness if no call handleWrongMessage
	public void handleHeartbeat(){
		logger.info("Heartbeat received");
		liveness = LIVNESS_OF_MASTER;
    	if(!busy)
    		sendRequestForWork();
	}

	//When master dosen't send a heart beat for long time kill this whole thread
	public void selfDestruction(ZContext context){
		logger.info("Operating self destruction");
		context.destroySocket(DLR);
		context.destroySocket(SUB);
		System.exit(0);
	}
	
	public void sendRequestForWork() {
		if(!ready_for_work_sent){
			DLR.sendMore(READY_FOR_WORK);
			DLR.send(" ");
			ready_for_work_sent = true;
			logger.info("Request for work sent");
		}
	}

	public void crawl(String domainName){
		logger.info("Request sent to domain name {}", domainName);
		cacheService.set(domainName, Fake.loadHtml());
		handleFinishedWork(domainName);
	}	
}
