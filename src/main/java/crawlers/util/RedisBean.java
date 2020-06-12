package crawlers.util;

import org.redisson.Redisson;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

public class RedisBean {

	//Redis's default address
    private final static String REDIS_ADDRESS = "redis://127.0.0.1:6379";
	//Instance of Redis
	private static RedissonClient redisson = Redisson.create(configuration());
	
	public static RMap<String, String> getInstance(String mapname) {	
		return redisson.getMap("test");
	}
	
	private static Config configuration() {
		Config config = new Config();
		config.useSingleServer().setAddress(REDIS_ADDRESS);
		return config;
	}
}
