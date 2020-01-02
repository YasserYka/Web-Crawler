package crawlers.webcrawlers;

import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;
import org.zeromq.ZMsg;

public class masterZeroMQ {

	public int availableSlaves;
	public int queueOfSlaves;
	
	ZMQ.Socket ROUTER;
	ZMQ.Socket PUB;
	ZMQ.Poller poller;
	
	public void init() {
		try (ZContext context = new ZContext()) {
			
		      ROUTER = context.createSocket(SocketType.ROUTER);
		      PUB = context.createSocket(SocketType.PUB);
		      poller = context.createPoller(2);

		      poller.register(ROUTER, ZMQ.Poller.POLLIN);
		      poller.register(PUB, ZMQ.Poller.POLLIN);
		      
		      while (true) {
		    	ZMsg message = null;
		    	poller.poll(5000);
		    	
		    	if(poller.pollin(0)) {
		    		message = ZMsg.recvMsg(ROUTER);
		    	}
		        
		    	//Create message of type ZMSG
		    	//Send it using abovesZMSG.send(pub)
		      }
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
	
	
}
