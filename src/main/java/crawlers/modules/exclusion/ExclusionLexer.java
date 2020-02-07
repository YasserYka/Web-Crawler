package crawlers.modules.exclusion;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExclusionLexer {
	
	private static final Pattern DISALLOW_PATTERN = Pattern.compile("Disallow: (.*)");
	
	//Takes Robot.txt file and extract URI that crawlers shouldn't crawl and returns them as list
	public static List<String> extract(String file){
		List<String> urls = new ArrayList<String>(); 
		Matcher matcher = DISALLOW_PATTERN.matcher(file);
		while(matcher.find())
			urls.add(matcher.group(1));
		return urls;
	}
}
