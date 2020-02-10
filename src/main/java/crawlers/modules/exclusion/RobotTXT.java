package crawlers.modules.exclusion;

import java.util.List;
import crawlers.util.MakeRequest;

public class RobotTXT {
	
	
	private static final String ROBOT_TXT_PATH = "/robots.txt";
	
	//TODO: override apache's DNS resolver
	
	public static void getRobotsTxt(String domainName){
		String content = MakeRequest.getContentOf(domainName, ROBOT_TXT_PATH);
	}
	
	public static List<String> extractDisallowedPaths(String robotTxtFile){
		List<String> paths = ExclusionLexer.extract(robotTxtFile);
		return paths;
	}
}
