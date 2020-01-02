package crawlers.webcrawlers;

import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;
import org.zeromq.ZMsg;

public class Slave {

	//TODO: set user-agent header to googlebot or any other crawler 
	
	ZMQ.Socket DLR; 
	ZMQ.Socket SUB;
	ZMQ.Poller poller;
	
	public void init() {
		try (ZContext context = new ZContext()) {
			
			DLR = context.createSocket(SocketType.DEALER);
			SUB = context.createSocket(SocketType.SUB);
		    poller = context.createPoller(2);
		    
		    poller.register(DLR, ZMQ.Poller.POLLIN);
		    poller.register(SUB, ZMQ.Poller.POLLIN); 
		    
		    while (true) {
		    ZMsg message = null;
		    	poller.poll(5000);
		    	
		    	//Message from Master
		    	if(poller.pollin(0))
		    		message = ZMsg.recvMsg(DLR);
		        
		    	//Heart beat from Master
		    	if(poller.pollin(1)) {
		    		message = ZMsg.recvMsg(SUB);
		    		//TODO: send to Master status
		    	}

		      }
		}
	}
	
	public void requestWork() {
		
	}
}
