package crawlers.modules;

import java.util.List;

import org.redisson.Redisson;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;

public class Seen {

	private static RedissonClient CLIENT;
	private static RBloomFilter<String> BLOOM_FILTER;
	
	static {
		//connect to it's default 127.0.0.1:6379
		CLIENT = Redisson.create();
		
		//Retrieve bloom filter by it name
		BLOOM_FILTER = CLIENT.getBloomFilter("bloomFilter");
		
		//The huge number on left is expected amount of insertions, on the right is expected false probability
		BLOOM_FILTER.tryInit(55000000L, 0.03);
	}
	
	public static void shutdown() {CLIENT.shutdown();}
	
	//Adds URL is not seen before
	public static void add(String url) {
		BLOOM_FILTER.add(url);
	}
	
	public static boolean contains(String url) {
		return BLOOM_FILTER.contains(url);
	}
	
	//Iterate through list of URLs and drops and URLs that seen before (been in bloom-filter)
	public static List<String> filter(List<String> urls) {
		
		int i, lengthOfUrls = urls.size();
		String url;
		
		for(i = 0; i < lengthOfUrls; i++) {
			url = urls.remove(0);
			if(!contains(url)) {
				//Add it to bloom-filter
				add(url);
				//Add it back to the list
				urls.add(url);
			}
		}

		return urls;
	}
}
