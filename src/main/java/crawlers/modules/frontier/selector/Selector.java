package crawlers.modules.frontier.selector;

import java.util.Calendar;
import java.util.PriorityQueue;

import crawlers.configuration.all;

public class Selector {

	//Act as min-heap for selecting URL from back end
	private static PriorityQueue<Element> HEAP = new PriorityQueue<Element>(new CustomComparator());
	
	public static String Select() {
		Element element = HEAP.peek();
		
		if(element.getStamp().after(Calendar.getInstance()))
			return HEAP.remove().getDomainName();
		
		return null;
	}
	
	public static void add(String url) {
		HEAP.add(new Element(url, all.TIME_GAP));
	}
}
