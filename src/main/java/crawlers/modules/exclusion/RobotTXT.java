package crawlers.modules.exclusion;

import java.util.List;

import crawlers.modules.DNSResolution;
import crawlers.util.Lexer;
import crawlers.util.MakeRequest;

public class RobotTXT {
	
	private static final String ROBOT_TXT_PATH = "/robots.txt";
	
	public static void getRobotsTxt(String domainName){
		String address = DNSResolution.resolveHostnameToIP(domainName).getHostName();
		String robotsFile = MakeRequest.get(address+ROBOT_TXT_PATH);
	}
	
	public static List<String> extractDisallowedPaths(String robotTxtFile){
		List<String> paths = Lexer.extractExcludedPaths(robotTxtFile);
		return paths;
	}
}
