package crawlers.url;

import java.util.regex.Pattern;

public enum UrlsPattern{

		Src("src=[\"|'](.*?)[\"|']"),
		Href("href=[\"|'](.*?)[\"|']");
	
		private Pattern pattern;
	
		private UrlsPattern(String pattern) {this.pattern = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);}
		public Pattern getPattern() {return pattern;}
}
