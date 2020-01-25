package crawlers.modules;

import org.redisson.Redisson;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;

public class Seen {

	RedissonClient redisson;
	RBloomFilter<String> bloomFilter;
	
	public void init() {
		//connect to it's default 127.0.0.1:6379
		redisson = Redisson.create();
		
		//Retrieve bloom filter by it name
		bloomFilter = redisson.getBloomFilter("bloomFilter");
		
		//The huge number on left is expected amount of insertions, on the right is expected false probability
		bloomFilter.tryInit(55000000L, 0.03);
	}
	
	public void shutdown() {redisson.shutdown();}
	
	//Adds
	public void Add(String contnet) {
		if(!bloomFilter.contains(contnet))
			bloomFilter.add(contnet);
	}
}
