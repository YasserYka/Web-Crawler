package crawlers;
import java.util.regex.Matcher;

public class Lexer {

	public static tokenByload lexer(String html){
				
		for(Token token : Token.values()){
			Matcher matcher = token.getPattern().matcher(html);
			if(matcher.find()) {
				return new tokenByload(token.name(), matcher.group().substring(token.getLengthOfUndesiredStringFromFirst(), matcher.group().length() - token.getlengthOfUndesiredStringFromLast()));
			}
		}
		return null;
	}
}
