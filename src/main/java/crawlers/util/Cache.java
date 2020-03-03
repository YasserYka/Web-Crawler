package crawlers.util;

import org.redisson.Redisson;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

public class Cache {

	//Redis's default address
    private final static String REDIS_ADDRESS = "redis://127.0.0.1:6379";
	//Instance of Redis
	private static RedissonClient redisson = null;
	//Redis's based distributed Map
	private static RMap<String, String> cache = null;
	
	public static RMap<String, String> initializeCache() {
		
		if(redisson == null) {
			redisson = Redisson.create(configuration());
		}
		
		if(cache == null) {
			cache = redisson.getMap("test");
		}
		return cache;
	}
	
	private static Config configuration() {
		Config config = new Config();
		config.useSingleServer().setAddress(REDIS_ADDRESS);
		return config;
	}
}
