package crawlers.modules;

import java.util.List;

import crawlers.util.Lexer;

//This should take the document from the master and operate on it using modules
public class chain {
	
	public void process(String document) {
		
		//Parsing the 
		List<String> urls = Lexer.extractURLs(document);
		
		//Drop seen URLs
		Duplicate.drop(urls);
	}

}
