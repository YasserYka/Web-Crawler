package crawlers.modules.frontier;

import crawlers.url.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

public class Backend {
	
	//Queue for each domain name
	private HashMap<String, Queue<String>> queues;
	//TODO: Limit number of queue or limit-less?
	private int maxNumberOfQueues = Integer.MAX_VALUE;
	//Counter for number of queues
	private int numberOfQueues;
	
	public Backend() {
		queues = new HashMap<String, Queue<String>>();
		numberOfQueues = 0;
	}
	
	//if one queue become empty don't remove that queue but keep ask for unique domain name to fill it
	public void refillEmptyQueue() {
		//TODO: while queue from front end is not unique and font end still have URLs keep asking for queue
	}
	
	//If URL's domain-name already exist in heap add it to domain-name's queue
	public void addUrl(URL url) {
		if(queues.containsKey(url.getDomainName()))
			queues.get(url.getDomainName()).add(url.getUrl());
		else if(numberOfQueues <= maxNumberOfQueues)
			queues.put(url.getDomainName(), initializeNewQueue());
		else
			try {throw new ExceedNumberOfQueues();}catch(ExceedNumberOfQueues e) {/* TODO:Log it*/e.printStackTrace();}
	}
	
	public LinkedList<String> initializeNewQueue() {return new LinkedList<String>();}

	
}
