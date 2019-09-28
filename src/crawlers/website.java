package crawlers;

public class website {

	private String description; 
	private String keywords;
	private String title;
	
	public website(String description, String keywords, String title) {
		this.description = description;
		this.keywords = keywords;
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	
}
