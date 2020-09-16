package crawlers.modules.frontier;

import java.util.Comparator;

import crawlers.models.FrontierElement;

public class CustomComparator implements Comparator<FrontierElement>{
	
	@Override
	public int compare(FrontierElement element1, FrontierElement element2) {
		int value = element1.getBackoffTime().compareTo(element2.getBackoffTime());
		if(value > 0)
			return -1;
		else if(value < 0)
			return 1;
		else
			return 0;
	}
}