package crawlers.modules.frontier.selector;

import java.util.PriorityQueue;

public class Selector {

	//Act as min-heap for selecting URL from back end
	private static PriorityQueue<Element> HEAP = new PriorityQueue<Element>(new CustomComparator());
	
	public String Select() {
		return "url";
	}
}
