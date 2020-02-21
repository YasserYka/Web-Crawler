package crawlers.modules.frontier;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

public class Frontend {
	
	//Each queue well be assigned a value indicates it priority (0 is the highest priority)
	private static HashMap<Integer, Queue<String>> queues = new HashMap<Integer, Queue<String>>();
	//Max number of Queues
	private static final int maxNumberOfQueues = 10;
	//Counter for number of queues
	private static int numberOfQueues = 0;
	//Variables for Pyramid-based dequeuing
	private static int pyramidLimit = 0, pyramidHolder = 0;

	public static LinkedList<String> initializeNewQueue() {return new LinkedList<String>();}

	public static void putQueue(int index) {
		try {if(numberOfQueues + 1 >= maxNumberOfQueues)throw new ExceedNumberOfQueues();}catch (ExceedNumberOfQueues e) {e.printStackTrace();}
		queues.put(index, initializeNewQueue());
		numberOfQueues++;
	}
	
	//add URL to queue with this priority
	public static void addUrl(int priority, String url) {
		if(!queues.containsKey(priority))
			putQueue(priority);
		queues.get(priority).add(url);
	}
	
	//TODO: Check if queue is not empty if it's skip queue
	//Gets URL based on Pyramid-turn
	public static String getUrl() {
		return queues.get(indexOfNextQueue()).remove();
	}
	
	public static boolean isTheQueueAtIndexIsEmpty(int index) {return queues.get(index).isEmpty();}
	
	//Get next queue based on pyramid pattern (for example 1, 12, 123, ...)
	private static int indexOfNextQueue() {
		if(pyramidLimit == maxNumberOfQueues) pyramidLimit = 0;
		if(pyramidHolder == pyramidLimit) {pyramidHolder = 0;pyramidLimit++;}
		return pyramidHolder++;
	}
}
