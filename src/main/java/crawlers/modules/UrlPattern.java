package crawlers.modules;

import java.util.regex.Pattern;

public enum UrlPattern{

		Src("src=[\"|'](.*?)[\"|']"),
		Href("href=[\"|'](.*?)[\"|']");
	
		private Pattern pattern;
	
		private UrlPattern(String pattern) {
			this.pattern = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
		}
		
		public Pattern getPattern() {
			return pattern;
		}
}
