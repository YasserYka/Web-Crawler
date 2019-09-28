package crawlers;

public class tokenByload{

	private String tokenName;
	private String data;
	
	public tokenByload(String tokenName, String data) {
		this.tokenName = tokenName;
		this.data = data;
	}
	
	public String getData() {
		return data;
	}
	public String getTokenName() {
		return tokenName;
	}
}
