package crawlers;
import java.util.regex.Pattern;

public enum Token {

	Title("<title>.*?</title>", 7, 8),
	//TODO: add keywords meta
	Src("src=(\".*?\"|'.*?')", 5, 1),
	Href("href=(\".*?\"|'.*?')", 6, 1),
	Description("meta\\s(name=(\"description\"|'description')|.*?\\sname=(\"description\"|'description'))\\scontent=(\".*?\"|'.*?')", 33, 1);
	
	private Pattern pattern;
	private int lengthOfUndesiredStringFromFirst;
	private int lengthOfUndesiredStringFromLast;
	
	private Token(String pattern, int lengthOfUndesiredStringFromFirst, int lengthOfUndesiredStringFromLast) {
		this.pattern = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
		this.lengthOfUndesiredStringFromFirst = lengthOfUndesiredStringFromFirst;
		this.lengthOfUndesiredStringFromLast = lengthOfUndesiredStringFromLast;
	}
	public Pattern getPattern() {
		return pattern;
	}
	public int getLengthOfUndesiredStringFromFirst() {
		return lengthOfUndesiredStringFromFirst;
	}
	public int getlengthOfUndesiredStringFromLast() {
		return lengthOfUndesiredStringFromLast;
	}
}
