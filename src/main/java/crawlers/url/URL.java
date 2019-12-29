package crawlers.url;

import java.util.regex.Pattern;

public enum URL{
		Title("<title>(.*?)</title>"),
		Src("src=[\"|'](.*?)[\"|']"),
		Href("href=[\"|'](.*?)[\"|']");
	
		private Pattern pattern;
	
		private URL(String pattern) {this.pattern = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);}
		public Pattern getPattern() {return pattern;}
}
