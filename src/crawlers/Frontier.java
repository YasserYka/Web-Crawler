package crawlers;

import java.util.HashSet;

public class Frontier {

	HashSet<String> frontier;
	
	public Frontier() {
		frontier = new HashSet<String>();
	}
	
	public void parseURL(){
		//TODO: loop through frontier
		//TODO: if already in frontier means don't crawler it 
		//TODO: use http client to send request to url
	}
}
