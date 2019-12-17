package crawlers;


import java.util.HashSet;
import java.util.Optional;

public class Frontier {

	private static HashSet<String> frontier;
	//Universal proposed waiting time before sending next request
	private static final int WAITING_TIME = 60;
	//used for testing proposed it should be removed/changed in production
	private static final int NUMBER_OF_PAGES_TO_CRAWLER = 10;
	
	private Frontier() {}
	
	public static final HashSet<String> createFrontier() {
		if(!Optional.ofNullable(frontier).isPresent())
			frontier = new HashSet<String>();
		return frontier;
	}
	
	private void parseURL(){
		//TODO: loop through frontier
		//TODO: check if already in frontier means don't crawler it 
		//TODO: use http client to send request to url
		//TODO: call the lexer
		//TODO: add url into frontier
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
	private boolean isAllowed(String url) {
		if(!RobotTXT.haveRobotTxt(url))
			return false;
		return true;
	}
	
	public void makeRequest() {
		
	}
	//To use Mercator's web crawler (if round trip is t, crawler should wait (t*10) second)
	private int calculateRoundTrip() {
		//Something like: int t = makeRequest();
		return 0;
	}
}
