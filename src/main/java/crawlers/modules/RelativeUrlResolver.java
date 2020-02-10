package crawlers.modules;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RelativeUrlResolver {
	
	/*
	 * Stolen regex from stackoverflow url: https://stackoverflow.com/questions/24924072/website-url-validation-regex-in-java
	 * gropu(0) should return base url if matcher
	*/
	private static final Pattern BASEIT_PATTERN = Pattern.compile("((http:\\/\\/|https:\\/\\/)?(www.)?([a-zA-Z0-9]+)).[a-zA-Z0-9]*.[a-z]{3}.?([a-z]+)?");
	
	/*
	 * Stolen regex from stackoverflow url:https://stackoverflow.com/questions/31430167/regex-check-if-given-string-is-relative-url
	 * well the URI of above's URI says what I'm gonna say next, this regex used to indicate if given URL is relative or absolute 
	*/
	private static final Pattern IS_ABSOLUTE_PATTERN = Pattern.compile("^([a-z0-9]*:|.{0})\\/\\/.*$");
		
	private static Matcher matcher;
	
	/*
	 * Three cases should be handled differently 
	 * Relative URL start with '/', start with "./", start with "../" or start with non of them (that should be handled same as "./" but appends a slash to it)
	 * "../" should be called recursively because it may contain more than one of those in relative URL
	*/
		
	public static String resolve(String base, String relative) {
		String resolved = null, based;
		if(relative.startsWith("/")) {
			based = baseIt(base);
			if(based != null)
				resolved = baseIt(base) + relative;
		}
		else if(relative.startsWith("./")) {
			resolved = currentDirectory(base) + relative;
		}
		else if(relative.startsWith("../")) {
			resolved = previousDirectory(base) + relative;
		}
		else {
			resolved = currentDirectory(base) + '/' +relative;
		}
		return resolved;
	}
	
	//Takes full URL and delete all URI leaving the base URL
	//Example http://www.notMaliciousAtAll.com/page/id it should return http://www.notmalicious.com
	private static String baseIt(String nonBase) {
		matcher = BASEIT_PATTERN.matcher(nonBase);
		return matcher.find() ? matcher.group(0) : null;
	}
	
	//Takes full URL and returns it without file-name in path
	//Example http://www.notMaliciousAtAll.com/page/fooling.html it should return http://www.notMaliciousAtAll.com/page
	private static String currentDirectory(String url) {
		int lengthOfUrl = url.length(), i, lastSlash = 0;
		char[] urlAsChars = url.toCharArray();
		
		for(i = 0; i< lengthOfUrl; i++)
			if(urlAsChars[i] == '/')
				lastSlash = i;
		
		return url.substring(0, lastSlash);
	}
	
	//Takes full URL and returns it by deleting file-name plus current directory
	//Example http://www.notMaliciousAtAll.com/page/anotherpage/fooling.html it should return http://www.notMaliciousAtAll.com/page
	private static String previousDirectory(String url) {
		int lengthOfUrl = url.length(), i, lastSlash = 0, beforeLastSlash = 0;
		char[] urlAsChars = url.toCharArray();
		
		for(i = 0; i< lengthOfUrl; i++)
			if(urlAsChars[i] == '/') {
				lastSlash = i;
				beforeLastSlash = lastSlash;
			}
		
		return url.substring(0, beforeLastSlash);
	}
	
	//Iterate through URLs and turn every relative to absolute URL with 
	public static void normalize(String url, List<String> urls) {	
		int lengthOfUrls = urls.size(), i;
		String tempUrl;
		
		for(i = 0; i < lengthOfUrls; i++) {
			tempUrl = urls.remove(0);
			if(isAbsolute(tempUrl))
				urls.add(lengthOfUrls - 1, tempUrl);
			else
				urls.add(lengthOfUrls - 1, resolve(url, tempUrl));
				
		}
	}

	private static boolean isAbsolute(String url) {
		return IS_ABSOLUTE_PATTERN.matcher(url).matches();
	}
}
