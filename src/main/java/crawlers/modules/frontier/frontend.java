package crawlers.modules.frontier;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

public class frontend {
	
	//Each queue well be assigned a value indicates it priority (0 is the highest priority)
	private HashMap<Integer, Queue<String>> queues;
	//Number of Queues
	private final int maxNumberOfQueues = 10;
	//Counter for number of queues
	private int numberOfQueues;
	
	public frontend() {
		queues = new HashMap<Integer, Queue<String>>();
		numberOfQueues = 0;
	}

	public LinkedList<String> initializeNewQueue() {return new LinkedList<String>();}

	public void putQueue() {
		try {if(numberOfQueues + 1 >= maxNumberOfQueues)throw new ExceedNumberOfQueues();}catch (ExceedNumberOfQueues e) {e.printStackTrace();}
		queues.put(numberOfQueues++, initializeNewQueue());
	}
}
