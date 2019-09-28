package crawlers;
import java.util.regex.Matcher;

public class Lexer {

	public static String lexer(String html){
		
		String link = null;
		
		for(Token token : Token.values()){
			Matcher matcher = token.getPattern().matcher(html);
			if(matcher.find()) {
				link = matcher.group().substring(token.getLengthOfUndesiredStringFromFirst(), matcher.group().length() - token.getlengthOfUndesiredStringFromLast());
			}
		}
		return link;
	}
}
