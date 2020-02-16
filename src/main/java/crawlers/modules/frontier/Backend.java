package crawlers.modules.frontier;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

import crawlers.models.URL;
import crawlers.modules.frontier.selector.Selector;

public class Backend {
	
	//Queue for each domain name
	private static HashMap<String, Queue<String>> queues = new HashMap<String, Queue<String>>();
	//For testing purposes it will be set to 10
	private static int maxNumberOfQueues = 10;
	//Counter for number of queues
	private static int numberOfQueues = 0;
	
	//if one queue become empty don't remove that queue but keep ask for unique domain name to fill it
	public void refillEmptyQueue() {
		//TODO: while queue from front end is not unique and font end still have URLs keep asking for queue
	}
	
	//If URL's domain-name already exist in heap add it to domain-name's queue
	public void addUrl(URL url) {
		if(queues.containsKey(url.getDomainName()))
			queues.get(url.getDomainName()).add(url.getUrl());
		else if(numberOfQueues <= maxNumberOfQueues) {
			Selector.add(url.getDomainName());
			queues.put(url.getDomainName(), initializeNewQueue());
		}
		else
			try {throw new ExceedNumberOfQueues();}catch(ExceedNumberOfQueues e) {/* TODO:Log it*/e.printStackTrace();}
	}
	
	public static LinkedList<String> initializeNewQueue() {return new LinkedList<String>();}

	public static String get(String key) {
		return queues.get(key).remove();
	}
}
