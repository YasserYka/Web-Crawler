package crawlers.crawlers;

import crawlers.configuration.all;
import java.util.LinkedList;
import java.util.Queue;

import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZFrame;
import org.zeromq.ZMQ;
import org.zeromq.ZMsg;

public class Master {

	private int numberOfAvailableSlaves;
	private Queue<Slave> queueOfSlaves;
	private ZMQ.Socket ROUTER;
	private ZMQ.Socket PUB;
	private ZMQ.Poller poller;
	private int heartbeatInterval;
	private long nextHeartbeat;
	
	public Master() {
		queueOfSlaves = new LinkedList<Slave>();
		numberOfAvailableSlaves = 0;
		heartbeatInterval = all.HEARTBEAT_INTERVAL;
	}
	
	//This return first available slave's address
	public ZFrame getAvailableSlaves() {return queueOfSlaves.remove().getAddress();}
	
	//This removes slaves from the queue if they are expired
	public void killExpiredSlaves() {queueOfSlaves.removeIf(slave -> slave.getExpiration() > System.currentTimeMillis());}
	
	public void init() {
		try (ZContext context = new ZContext()) {
			
		      ROUTER = context.createSocket(SocketType.ROUTER);
		      PUB = context.createSocket(SocketType.PUB);
		      poller = context.createPoller(2);
		      
		      //Register two sockets in poller so to listen on both sockets
		      poller.register(ROUTER, ZMQ.Poller.POLLIN);
		      poller.register(PUB, ZMQ.Poller.POLLIN);
		      nextHeartbeat = System.currentTimeMillis() + heartbeatInterval;
		      
		      while (true) {
		    	  
		    	ZMsg message = null;
		    	poller.poll(heartbeatInterval);
		    	
		    	//message received from slave
		    	if(poller.pollin(0)) {
		    		message = ZMsg.recvMsg(ROUTER);
		    		//If message received from slave that would means its available for work otherwise it would be busy requesting a web page
		    		insertSlave(message.unwrap());
		    		//TODO: check what kind of message received 
		    		//This if statement means it received an event from slave (request for work, heart beat etc..) 
		    		if(message.size() == 1) {
		    			//TODO: if message == request-for-work.event
		    		}
		    		//This life for debugging the content of message
		    		message.dump(System.out);
		    	}
		     }
		}
	}
	
	//Creates a new slave object for an address and enqueue it
	public void insertSlave(ZFrame address){queueOfSlaves.add(new Slave(address));}
	
	//TODO: override equal
	//This remove slave that is ready for work
	public void removeSlave(Slave slave) {
		//queueOfSlaves.remove(slave);
	}
	
	//TODO: make heart beat standard
	public void sendHearbeat() {
		for(Slave slave : queueOfSlaves) {
			slave.getAddress().send(PUB, 0);
		}
		
		if(System.currentTimeMillis() > nextHeartbeat)
			nextHeartbeat = System.currentTimeMillis() + heartbeatInterval;
	}
	
	//TODO: contact frontier for URL
	public String getUrlFromFrontier() {
		return "";
	}
	
}
