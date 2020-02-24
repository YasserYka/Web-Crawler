package crawlers.modules;

import java.util.List;

import crawlers.modules.exclusion.RobotTXT;
import crawlers.modules.filter.Filter;
import crawlers.url.UrlLexer;

//This should take the document from the master and operate on it using modules
public class Chain {
	
	public static void process(String url, String document) {
				
		//Parsing the 
		List<String> urls = UrlLexer.extractURLs(document);
				
		//Resolve relative URLs to absolute ones
		RelativeUrlResolver.normalize(url, urls);		
		
		//Drop URLs that are out of scope
		Filter.drop(urls);
				
		//Fetch robots.txt file from URL and drop excluded URLs
		RobotTXT.filter(url, urls);
		
		//Drop seen URLs
		Seen.filter(urls);
		
		/*//If some URLs survived put them in frontier
		if(urls.size() > 0)
			addUrlsToFrontier(urls);*/
	}
	
	private static void addUrlsToFrontier(List<String> urls) {
		
	}

}
