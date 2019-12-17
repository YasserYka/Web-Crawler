package crawlers;


import java.util.regex.Pattern;

enum idTODO{
	Disallow("Disallow: (.*)");
	private Pattern pattern;
	private idTODO(String regex) {this.pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);}
	public Pattern getPattern() {return pattern;};
}
public class RobotsTxtParser {
	
}
