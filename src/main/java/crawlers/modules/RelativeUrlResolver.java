package crawlers.modules;

public class RelativeUrlResolver {

	/*
	 * Three cases should be handled differently 
	 * Relative URL start with '/', start with "./", start with "../"
	*/
	public String resolve(String relative, String base) {
		String resolved = null;
		if(relative.startsWith("/"))
			resolved = baseIt(base) + relative;
		return resolved;
	}
	
	//Takes full URL and delete all URI leaving the base URL
	//Example http://www.notMaliciousAtAll.com/page/id it should return http://www.malicious.com
	private String baseIt(String nonBase) {
		return null;
	}
	
	//Takes full URL and returns it without file-name in path
	//Example http://www.notMaliciousAtAll.com/page/fooling.html it should return http://www.notMaliciousAtAll.com/page/
	private String currentDirectury(String url) {
		return null;
	}
}
