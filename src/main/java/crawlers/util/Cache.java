package crawlers.util;

import org.redisson.Redisson;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

public class Cache {

	//Redis's default address
    private final static String REDIS_ADDRESS = "redis://127.0.0.1:6379";
	//Instance of Redis
	private static RedissonClient redisson = Redisson.create(configuration());
	//Redis's based distributed Map
	private static RMap<String, String> cache = redisson.getMap("test");
	
	private static Config configuration() {
		Config config = new Config();
		config.useSingleServer().setAddress(REDIS_ADDRESS);
		return config;
	}
	
	public static void add(String key, String document) {
		cache.fastPutAsync(key, document);
	}
	
	public static boolean contain(String key) {
		return cache.containsKey(key);
	}
	
	public static String get(String key) {
		return cache.get(key);
	}
}
