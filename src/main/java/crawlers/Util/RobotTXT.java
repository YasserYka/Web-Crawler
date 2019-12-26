package crawlers.Util;


import java.util.ArrayList;

public class RobotTXT {
	
	public static boolean haveRobotTxt(String url){
		String robotsFilePath = url + "/robots.txt";
		//TODO: make http connection
		//TODO: response type is OK -> true, or not found -> 400 
		return false;
	}
	public static void lexer(String url) {
		//TODO: call Lexer.robotTxt
		//TODO: store returned values from Lexer to disallow/allow paths
	}
}
