package crawlers.url;

import crawlers.util.Lexer;

public class Extract{

	public void extract(String html) {
		Regex.values();
		Lexer.extractURLs(html);
	}
	
	//TODO: if url is sub domain (without http) append it to it main domain
}
