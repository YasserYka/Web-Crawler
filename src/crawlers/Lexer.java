package crawlers;
import java.util.regex.Matcher;

public class Lexer {

	public static Webpage lexer(String html){
		
		Webpage webpage = new Webpage();
		
		for(Token token : Token.values()){
			Matcher matcher = token.getPattern().matcher(html);
			while(matcher.find()) {
				if(token == Token.Src) 
					webpage.appendDescription(matcher.group().substring(token.getLengthOfUndesiredStringFromFirst(), matcher.group().length() - token.getlengthOfUndesiredStringFromLast()));
				else if(token == Token.Href || token == Token.Src)
					webpage.addLink(matcher.group().substring(token.getLengthOfUndesiredStringFromFirst(), matcher.group().length() - token.getlengthOfUndesiredStringFromLast()));
				else if(token == Token.Title)
					webpage.appendTitle(matcher.group().substring(token.getLengthOfUndesiredStringFromFirst(), matcher.group().length() - token.getlengthOfUndesiredStringFromLast()));
				else if(token == Token.Description)
					webpage.appendDescription(matcher.group().substring(token.getLengthOfUndesiredStringFromFirst(), matcher.group().length() - token.getlengthOfUndesiredStringFromLast()));
			}
		}
		return null;
	}
}
