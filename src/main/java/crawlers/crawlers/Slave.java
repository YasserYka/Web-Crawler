package crawlers.crawlers;

import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZFrame;
import org.zeromq.ZMQ;
import org.zeromq.ZMsg;

public class Slave {

	//TODO: set user-agent header to googlebot or any other crawler 
	
	//Address of slave
	private ZFrame address;
	//Printable identifier
	private String identity;
	//Dealer socket for Dealer-Router pattern
	private ZMQ.Socket DLR; 
	//subscriber socket for Subscriber-Publisher pattern
	private ZMQ.Socket SUB;
	//to read from multiple sockets
	private ZMQ.Poller poller;
	
	protected Slave(ZFrame address) {
		this.address = address;
        identity = new String(address.getData(), ZMQ.CHARSET);
	}
	
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
	
	
	
	public ZFrame getAddress() {
		return address;
	}

	public void requestWork() {
		
	}
}
