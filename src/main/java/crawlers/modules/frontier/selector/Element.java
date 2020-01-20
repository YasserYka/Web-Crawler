package crawlers.modules.frontier.selector;

import java.util.Calendar;
import java.util.Date;

public class Element {

	Calendar stamp;
	String domainName;
	
	public Element(String domainName, int seconds) {
		this.domainName = domainName;
		stamp = Calendar.getInstance();
		stamp.setTime(new Date());
		stamp.add(Calendar.SECOND, seconds);
	}

	public Calendar getStamp() {
		return stamp;
	}

	public void setStamp(Calendar stamp) {
		this.stamp = stamp;
	}

	public String getDomainName() {
		return domainName;
	}

	public void setDomainName(String domainName) {
		this.domainName = domainName;
	}
}
