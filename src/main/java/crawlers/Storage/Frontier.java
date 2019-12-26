package crawlers.Storage;


import java.util.HashSet;
import java.util.LinkedList;
import java.util.Optional;
import java.util.Queue;

public class Frontier {

	private static Queue<String> frontier;
	//Universal proposed waiting time before sending next request
	private static final int WAITING_TIME = 60;
	//used for testing proposed it should be removed/changed in production
	private static final int NUMBER_OF_PAGES_TO_CRAWLER = 10;
	private static HashSet<String> seen;
	
	private Frontier() {}
	
	public static final Queue<String> createFrontier() {
		if(!Optional.ofNullable(frontier).isPresent())
			frontier = new LinkedList<String>();
		return frontier;
	}
	
	public static final HashSet<String> createSeen() {
		if(!Optional.ofNullable(frontier).isPresent())
			seen = new HashSet<String>();
		return seen;
	}
	
	private void parseURL(){
		
		String url;
		while(!frontier.isEmpty()) {
			url = frontier.remove();
			seen.add(url);
			//extract links from url's HTML
			//for link in above's links 
				//if not seen add it to frontier
		}
	}
	
	public void start(String seed) {
		
	}
	private boolean isAbsolute() {
		return false;
	}
	
	//TODO: ask the DB if url have been visited?
	private boolean isVisited(String url) {return false;}
	
	//TODO: 
	private void nextUrl() {
		
	}
	/*private boolean isAllowed(String url) {
		if(!RobotTXT.haveRobotTxt(url))
			return false;
		return true;
	}*/
	
	public void makeRequest() {
		
	}
	//To use Mercator's web crawler (if round trip is t, crawler should wait (t*10) second)
	private int calculateRoundTrip() {
		//Something like: int t = makeRequest();
		return 0;
	}
}
