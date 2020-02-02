package crawlers.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import crawlers.modules.exclusion.Disallow;
import crawlers.url.Regex;

public class Lexer{

	//Gets a HTML document and extract URLs and returns it as List
	public static List<String> extractURLs(String document){
		List<String> urls = new ArrayList<String>(); 
		for(Regex url : Regex.values()){
			Matcher matcher = url.getPattern().matcher(document);
			while(matcher.find())
				urls.add(matcher.group(1));
		}
		return urls;
	}
	
	//Takes Robot.txt file and parse it and returns disallowed URI
	public static List<String> extractExcludedPaths(String file){
		List<String> urls = new ArrayList<String>(); 
		for(Disallow url : Disallow.values()){
			Matcher matcher = url.getPattern().matcher(file);
			while(matcher.find())
				urls.add(matcher.group(1));
		}
		return urls;
	}
}