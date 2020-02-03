package crawlers.modules;

import java.util.List;
import java.util.regex.Pattern;

public class RelativeUrlResolver {
	
	private static final Pattern BASEIT_PATTERN = null;

	/*
	 * Three cases should be handled differently 
	 * Relative URL start with '/', start with "./", start with "../" or start with non of them (that should be handled same as "./" but appends a slash to it)
	 * "../" should be called recursively because it may contain more than one of those in relative URL
	*/
	public String resolve(String base, String relative) {
		String resolved = null;
		if(relative.startsWith("/"))
			resolved = baseIt(base) + relative;
		return resolved;
	}
	
	//Takes full URL and delete all URI leaving the base URL
	//Example http://www.notMaliciousAtAll.com/page/id it should return http://www.notmalicious.com
	private String baseIt(String nonBase) {
		return null;
	}
	
	//Takes full URL and returns it without file-name in path
	//Example http://www.notMaliciousAtAll.com/page/fooling.html it should return http://www.notMaliciousAtAll.com/page/
	private String currentDirectory(String url) {
		int lengthOfUrl = url.length(), i, lastSlash = 0;
		char[] urlAsChars = url.toCharArray();
		
		for(i = 0; i< lengthOfUrl; i++)
			if(urlAsChars[i] == '/')
				lastSlash = i;
		
		return url.substring(0, lastSlash);
	}
	
	//Takes full URL and returns it by deleting file-name plus current directory
	//Example http://www.notMaliciousAtAll.com/page/anotherpage/fooling.html it should return http://www.notMaliciousAtAll.com/page/
	private String previousDirectory(String url) {
		int lengthOfUrl = url.length(), i, lastSlash = 0, beforeLastSlash = 0;
		char[] urlAsChars = url.toCharArray();
		
		for(i = 0; i< lengthOfUrl; i++)
			if(urlAsChars[i] == '/') {
				lastSlash = i;
				beforeLastSlash = lastSlash;
			}
		
		return url.substring(0, beforeLastSlash);
	}
	
	//Iterate through URLs and turn every relative to absolute URL
	public static void drop(List<String> urls) {
		int lengthOfUrls = urls.size(), i;
		String url;
		
		for(i = 0; i < lengthOfUrls; i++) {
			//Resolve if need to be resolved
		}
	}}
