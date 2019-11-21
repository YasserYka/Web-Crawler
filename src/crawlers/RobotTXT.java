package crawlers;

import java.util.ArrayList;

public class RobotTXT {

	//TODO: think if this 
	private ArrayList<String> Disallow;
	private ArrayList<String> Allow;
	
	public void haveRobotTxt(String url){
		String robotsFilePath = url + "/robots.txt";
		//TODO: make http connection
		//TODO: response type is OK -> true, or not found -> 400 
	}
	public void lexer(String url) {
		//TODO: call Lexer.robotTxt
		//TODO: store returned values from Lexer to disallow/allow paths
	}
}
