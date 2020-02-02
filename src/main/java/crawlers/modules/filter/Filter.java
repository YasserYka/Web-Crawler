package crawlers.modules.filter;

import java.util.regex.Pattern;

public class Filter {

	//Pattern for detecting HTML files
	private static final Pattern HTML_PATTERN = Pattern.compile(".*.html|.*.htm|.*.shtml|.*.php|.*.cgi|.*.jsp|.*.aspx|.*.asp|.*.pl|.*.cfm|.*\\/|.*\\w$");
	
	//Checks if the URL points to HTML or not
	public boolean isHtml(String url) {
		return HTML_PATTERN.matcher(url).matches();
	}
}
