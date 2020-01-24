package crawlers.modules.exclusion;

import java.util.List;
import crawlers.util.Lexer;

public class RobotTXT {
	
	private static final String ROBOT_TXT_PATH = "/robots.txt";
	
	public static void haveRobotTxt(String domainName){
		
	}
	
	public static List<String> extractDisallowedPaths(String robotTxtFile){
		List<String> paths = Lexer.extractExcludedPaths(robotTxtFile);
		return paths;
	}
}
