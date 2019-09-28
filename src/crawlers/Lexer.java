package crawlers;
import java.util.regex.Matcher;

public class Lexer {

	public static tokenByload lexer(String html){
		
		tokenByload tokenbyload = null;
		
		for(Token token : Token.values()){
			Matcher matcher = token.getPattern().matcher(html);
			if(matcher.find()) {
				tokenbyload = new tokenByload(token.name(), matcher.group().substring(token.getLengthOfUndesiredStringFromFirst(), matcher.group().length() - token.getlengthOfUndesiredStringFromLast()));
			}
		}
		return tokenbyload;
	}
}
