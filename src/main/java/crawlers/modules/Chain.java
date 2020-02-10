package crawlers.modules;

import java.util.List;

import crawlers.modules.exclusion.RobotTXT;
import crawlers.modules.filter.Filter;
import crawlers.url.UrlLexer;

//This should take the document from the master and operate on it using modules
public class Chain {
	
	public void process(String urlOfDocuemnt, String document) {
		
		//Parsing the 
		List<String> urls = UrlLexer.extractURLs(document);
		
		//TODO: call relative resolver here
		RelativeUrlResolver.normalize(urlOfDocuemnt, urls);
		
		//Drop seen URLs
		Duplicate.drop(urls);
		
		//Drop URLs that are out of scope
		Filter.drop(urls);
		
		//TODO: call exclusion filter here
		RobotTXT.filter(urlOfDocuemnt, urls);
	}

}
