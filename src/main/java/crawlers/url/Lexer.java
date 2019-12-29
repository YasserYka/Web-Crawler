package crawlers.url;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

public class Lexer{

	public static List<String> extractURLs(String html){
		List<String> urls = new ArrayList<String>(); 
		for(URL url : URL.values()){
			Matcher matcher = url.getPattern().matcher(html);
			while(matcher.find())
				urls.add(matcher.group(1));
		}
		return urls;
	}
}