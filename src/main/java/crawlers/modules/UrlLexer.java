package crawlers.modules;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

public class UrlLexer{

	//Gets a HTML document and extract URLs and returns it as List
	public static List<String> extractURLs(String document){

		List<String> urls = new ArrayList<String>(); 
		for(UrlPattern url : UrlPattern.values()){
			Matcher matcher = url.getPattern().matcher(document);
			while(matcher.find())
				urls.add(matcher.group(1));
		}
		return urls;
	}

}