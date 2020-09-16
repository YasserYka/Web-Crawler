package crawlers.modules;

import java.util.List;
import java.util.regex.Pattern;

import crawlers.util.MakeRequest;

public class RobotTXT {
	
	
	private static final String ROBOTS_TXT_PATH = "/robots.txt";
	
	//TODO: override apache's DNS resolver
	
	private  static String getRobotsTxt(String domainName){
		String robotstxt = null;
		//Sends head request to check if it's exist
		if(MakeRequest.isFound(domainName, ROBOTS_TXT_PATH)) {
			//Send get request to get the content body
			robotstxt = MakeRequest.getContentOf(domainName, ROBOTS_TXT_PATH);
		}
		return robotstxt;
	}
	
	private static String extractDisallowedPaths(String robotstxt){return ExclusionLexer.extract(robotstxt);}
	
	private static Pattern getPatternOfContent(String robotstxt) {
		return Pattern.compile(extractDisallowedPaths(robotstxt));
	}
	
	public static List<String> filter(String domainName, List<String> urls) {
		int lengthOfUrls = urls.size(), i;
		String tempUrl, robotstxt = getRobotsTxt(domainName);
				
		//We don't want to take a risk and send request to web sites that don't have robots.txt file so for know drop all URLs
		if(robotstxt == null)
			urls.clear();
		else {
			Pattern pattern = getPatternOfContent(robotstxt);
				
			for(i = 0; i < lengthOfUrls; i++) {
				tempUrl = urls.remove(0);
				
				//If URL not matched one of the excluded URI return it
				if(!pattern.matcher(tempUrl).matches())
					urls.add(tempUrl);
			}
		}
		return urls;
	}
}
