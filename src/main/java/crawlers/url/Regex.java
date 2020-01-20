package crawlers.url;

import java.util.regex.Pattern;

public enum Regex{
		Title("<title>(.*?)</title>"),
		Src("src=[\"|'](.*?)[\"|']"),
		Href("href=[\"|'](.*?)[\"|']");
	
		private Pattern pattern;
	
		private Regex(String pattern) {this.pattern = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);}
		public Pattern getPattern() {return pattern;}
}
