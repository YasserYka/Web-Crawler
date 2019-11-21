package crawlers;

import java.util.HashSet;
import java.util.Optional;

public class Frontier {

	private static HashSet<String> frontier;
	private final int numberOfPagesToCrawl = 5; 
	
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
	
	//TODO: ask the DB if url have been visited?
	private boolean isVisited(String url) {return false;}
	
	//TODO: 
	private void nextUrl() {
		
	}
}
