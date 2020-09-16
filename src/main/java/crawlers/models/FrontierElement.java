package crawlers.models;

import crawlers.configuration.Config;

public class FrontierElement {

	private Long backoffTime;
	private String url;
	
	public FrontierElement(String url) {
		this.url = url;
		backoffTime = System.currentTimeMillis() + Config.WAITING_TIME;
	}

	public Long getBackoffTime() {
		return backoffTime;
	}

	public String getUrl() {
		return url;
	}

}
