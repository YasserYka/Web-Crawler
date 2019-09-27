import java.util.ArrayList;
import java.util.regex.Matcher;

public class Lexer {

	public static ArrayList<String> lexer(String html){
		ArrayList<String> links = new ArrayList<>();
		
		for(Token token : Token.values()){
			Matcher matcher = token.getPattern().matcher(html);
			if(matcher.find()) {
				//Warning wait until testing the token's Regex finishes first 
				//TODO: drop undesired substrings
			}
		}
		return links;
	}
}
