package crawlers.mapReduce;

import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;
import org.zeromq.ZMsg;

public class masterZeroMQ {

	public void init() {
		try (ZContext context = new ZContext()) {
		      ZMQ.Socket ROUTER = context.createSocket(SocketType.ROUTER);
		      ZMQ.Socket PUB = context.createSocket(SocketType.PUB);
		      ZMQ.Poller poller = context.createPoller(2);

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
		      }
		}
	}
}
