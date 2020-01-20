package crawlers.modules.frontier.selector;

import java.util.Comparator;

public class CustomComparator implements Comparator<Element>{
	
	@Override
	public int compare(Element element1, Element element2) {
		int value = element1.getStamp().compareTo(element2.getStamp());
		if(value > 0)
			return -1;
		else if(value < 0)
			return 1;
		else
			return 0;
	}
}
