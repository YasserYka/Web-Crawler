package crawlers.modules.frontier;

import java.util.PriorityQueue;

public class BackendSelector {

	//Act as min-heap 
	PriorityQueue<Integer> heap;
	
	public BackendSelector() {
		heap = new PriorityQueue<Integer>();
	}
}
