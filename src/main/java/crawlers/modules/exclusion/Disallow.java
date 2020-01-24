package crawlers.modules.exclusion;


import java.util.regex.Pattern;

public enum Disallow{
	
	disallow("Disallow: (.*)");
	
	private Pattern pattern;
	
	private Disallow(String regex) {this.pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);}
	
	public Pattern getPattern() {return pattern;};
}

