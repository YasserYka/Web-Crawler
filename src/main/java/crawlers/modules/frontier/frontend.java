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
	//Variables for Pyramid-based dequeuing
	private int pyramidLimit, pyramidHolder;
	
	public frontend() {
		queues = new HashMap<Integer, Queue<String>>();
		numberOfQueues = pyramidLimit = pyramidHolder = 0;
	}

	public LinkedList<String> initializeNewQueue() {return new LinkedList<String>();}

	public void putQueue() {
		try {if(numberOfQueues + 1 >= maxNumberOfQueues)throw new ExceedNumberOfQueues();}catch (ExceedNumberOfQueues e) {e.printStackTrace();}
		queues.put(numberOfQueues++, initializeNewQueue());
	}
	
	//add URL to queue with this priority
	public void addUrl(int priority, String url) {queues.get(priority).add(url);}
	
	//TODO: Check if queue is not empty if it's skip queue
	//Gets URL based on Pyramid-turn
	public String getUrl() {return queues.get(indexOfNextQueue()).remove();}
	
	public boolean isTheQueueAtIndexIsEmpty(int index) {return queues.get(index).isEmpty();}
	
	//Get next queue based on pyramid pattern (for example 1, 12, 123, ...)
	private int indexOfNextQueue() {
		if(pyramidLimit == maxNumberOfQueues) pyramidLimit = 0;
		if(pyramidHolder == pyramidLimit) {pyramidHolder = 0;pyramidLimit++;}
		return pyramidHolder++;
	}
}
