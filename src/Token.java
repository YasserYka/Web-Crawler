import java.util.regex.Pattern;

public enum Token {

	Title("<title>.*?</title>"),
	Src("src=(\".*?\"|'.*?')"),
	Href("href=(\".?*\"|'.?*')");
	
	private Pattern pattern;
	
	private Token(String pattern) {
		this.pattern = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
	}
	public Pattern getPattern() {
		return pattern;
	}
}
