package crawlers.modules.frontier.selector;

import java.util.PriorityQueue;

public class Selector {

	//Act as min-heap 
	PriorityQueue<Element> heap;
	
	public Selector() {
		heap = new PriorityQueue<Element>(new CustomComparator());
	}
}
