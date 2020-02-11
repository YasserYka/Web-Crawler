package crawlers.modules.frontier.selector;

import java.util.Calendar;
import java.util.PriorityQueue;

import crawlers.configuration.all;

public class Selector {
	
	//Event to tell master that the back-end is empty stop asking for while
	private final static String EMPTY_EVENT = "EMPTY";
	
	//Event to tell master to wait more for politeness
	private final static String WAIT_EVENT = "WAIT";

	//Act as min-heap for selecting URL from back end
	private static PriorityQueue<Element> HEAP = new PriorityQueue<Element>(new CustomComparator());
	
	//Select first element in Heap if it time pass the current time
	public static String Select() {
		Element element = HEAP.peek();
		
		if(HEAP.size() == 0)
			return EMPTY_EVENT;
		else if(element.getStamp().after(Calendar.getInstance()))
			return HEAP.remove().getDomainName();
		else
		return WAIT_EVENT;
	}
	
	public static void add(String url) {
		HEAP.add(new Element(url, all.TIME_GAP));
	}
}
