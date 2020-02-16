package crawlers.modules.filter;

import java.util.List;
import java.util.regex.Pattern;

public class Filter {

	//Pattern for detecting HTML files
	private static final Pattern HTML_PATTERN = Pattern.compile(".*.html|.*.htm|.*.shtml|.*.php|.*.cgi|.*.jsp|.*.aspx|.*.asp|.*.pl|.*.cfm|.*\\/|.*\\w");
	
	//Checks if the URL points to HTML or not
	public static boolean isHtml(String url) {
		return HTML_PATTERN.matcher(url).matches();
	}
	
	//Takes list of URLs and drop URLs that not interested-in
	public static void drop(List<String> urls) {
		int lengthOfUrls = urls.size(), i;
		String url;
		System.out.println("LENGTH: " + lengthOfUrls);
		for(i = 0; i < lengthOfUrls; i++) {
			lengthOfUrls = urls.size();
			System.out.println(i);
			url = urls.remove(0);
			System.out.println(url);
			if(isHtml(url))
				urls.add(url);
		}
	}
}
