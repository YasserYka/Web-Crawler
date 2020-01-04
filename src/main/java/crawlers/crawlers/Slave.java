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
	//Expires at this time
	private long expiration;
	//This set to true if work not done yet
	private boolean busy;
	
	protected Slave(ZFrame address) {
		this.address = address;
        identity = new String(address.getData(), ZMQ.CHARSET);
        busy = false;
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
		    	
		    	//Message from master
		    	if(poller.pollin(0)) {
		    		message = ZMsg.recvMsg(DLR);
		    	}
		        
		    	//Heart beat from master
		    	if(poller.pollin(1)) {
		    		message = ZMsg.recvMsg(SUB);
		    		//Send to Master address, don't response if busy working
		    		if(!busy)
		    			sendStatusToMaster();
		    	}

		      }
		}
	}
	
	public void sendStatusToMaster() {
		ZMsg status = new ZMsg();
		status.add(address);
		status.send(DLR);
	}
	
	public void handleHeartbeat(ZFrame message){
		//if(heartbeat != new String(message.getData(), ZMQ.CHARSET))
	}
	
	public long getExpiration() {
		return expiration;
	}
	
	public ZFrame getAddress() {
		return address;
	}

	//TODO: Repressing the Enum events as byte
	//This sends event as byte asking for Work
	public void requestWork() {
		new ZFrame("\000").send(DLR,  0);
	}
}
