package crawlers;

import java.util.ArrayList;

public class Webpage {

	private StringBuilder description; 
	private StringBuilder keywords;
	private StringBuilder title;
	private ArrayList<String> links;
	
	public Webpage() {
		description = new StringBuilder();
		keywords = new StringBuilder();
		title = new StringBuilder();
		links = new ArrayList<String>();
	}
	public Webpage(String description, String keywords, String title) {
		this.description = new StringBuilder(description);
		this.keywords = new StringBuilder(keywords);
		this.title = new StringBuilder(title);
		links = new ArrayList<String>();
	}

	public void addLink(String link) {
		links.add(link);
	}
	
	public String popLink() {
		if(links.size() == 0)
			throw new IndexOutOfBoundsException("the links arrayList is empty");
		return links.get(0);
	}
	public String getDescription() {
		return description.toString();
	}

	public void appendDescription(String description) {
		this.description.append(description);
	}

	public String getKeywords() {
		return keywords.toString();
	}

	public void appendKeywords(String keywords) {
		this.keywords.append(keywords);
	}

	public String getTitle() {
		return title.toString();
	}

	public void appendTitle(String title) {
		this.title.append(title);
	}
	
	
}
