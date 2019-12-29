package crawlers.url;

public class Extract{

	public void extract(String html) {
		Lexer.extractURLs(html);
	}
	
	//TODO: if url is sub domain (without http) append it to it main domain
}
