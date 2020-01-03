package crawlers.crawlers;

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
	
	public Master() {
		queueOfSlaves = new LinkedList<Slave>();
		numberOfAvailableSlaves = 0;
	}
	
	//TODO: contact the frontier for url and coordinate Slaves or separate those two??
	//TODO: create pools (queue maybe?) for available and busy Slaves (maybe only available (fallback:not monitoring all?))
	//TODO: master should get page from slave to featured-modules? or slave should directly send it?	
	
	public Slave getFreeSlave() {
		return null;
	}
	
	public boolean allSlavesAreBusy() {
		return false;
	}
	
	//This return first available slave's address
	public ZFrame getFreeSlaves() {return queueOfSlaves.remove().getAddress();}
	
	public void init() {
		try (ZContext context = new ZContext()) {
			
		      ROUTER = context.createSocket(SocketType.ROUTER);
		      PUB = context.createSocket(SocketType.PUB);
		      poller = context.createPoller(2);

		      poller.register(ROUTER, ZMQ.Poller.POLLIN);
		      poller.register(PUB, ZMQ.Poller.POLLIN);
		      
		      while (!Thread.currentThread().isInterrupted()) {
		    	ZMsg message = null;
		    	poller.poll(5000);
		    	
		    	if(poller.pollin(0)) {
		    		message = ZMsg.recvMsg(ROUTER);
		    	}
		        
		    	//Create message of type ZMSG
		    	//Send it using abovesZMSG.send(pub)
		    	Thread.sleep(1000);
		      }
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	//TODO: make heart beat standard
	public void sendHearbeat() {
		
	}
	
	//TODO: pop available-slave call getUrlFromFrontier and put it in message body 
	public void dispatchWork() {
		
	}
	
	//TODO: contact frontier for URL
	public String getUrlFromFrontier() {
		return "";
	}
	
	//Kill expired slaves
	public void killSlaves() {
		
	}
	
}
