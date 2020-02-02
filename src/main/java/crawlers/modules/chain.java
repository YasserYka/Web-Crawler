package crawlers.modules;

import java.util.List;

import crawlers.modules.filter.Filter;
import crawlers.util.Lexer;

//This should take the document from the master and operate on it using modules
public class chain {
	
	public void process(String document) {
		
		//Parsing the 
		List<String> urls = Lexer.extractURLs(document);
		
		//TODO: call relative resolver here
		
		//Drop seen URLs
		Duplicate.drop(urls);
		
		//Drop URLs that are out of scope
		Filter.drop(urls);
		
		
		
	}

}
