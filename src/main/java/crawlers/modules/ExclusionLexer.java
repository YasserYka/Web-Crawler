package crawlers.modules;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExclusionLexer {
	
	private static final Pattern DISALLOW_PATTERN = Pattern.compile("Disallow: (.*)");
	
	//Takes Robot.txt file and extract URI that crawlers shouldn't crawl and returns them as one line string with "OR" operations between them
	public static String extract(String file){
		
		StringBuilder urisInLine = new StringBuilder();
		Matcher matcher = DISALLOW_PATTERN.matcher(file);

		while(matcher.find())
			urisInLine.append(matcher.group(1) + "|");

		String output = urisInLine.toString();
		return output.substring(0, output.length() - 1).replaceAll("\\*", ".*");
	}
}
