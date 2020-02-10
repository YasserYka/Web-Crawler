package crawlers.modules.exclusion;

import java.util.List;
import crawlers.util.MakeRequest;

public class RobotTXT {
	
	
	private static final String ROBOT_TXT_PATH = "/robots.txt";
	
	//TODO: override apache's DNS resolver
	
	public static String getRobotsTxt(String domainName){
		String content = null;
		//Sends head request to check if it's exist
		if(MakeRequest.isFound(domainName, ROBOT_TXT_PATH)) {
			//Send get request to get the content body
			content = MakeRequest.getContentOf(domainName, ROBOT_TXT_PATH);
		}
		return content;
	}
	
	public static List<String> extractDisallowedPaths(String content){
		List<String> paths = ExclusionLexer.extract(content);
		return paths;
	}
}
