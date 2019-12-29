package crawlers.storage;

import redis.clients.jedis.Jedis;

public class Redis {

	public static Jedis jedis;
	
	public static Jedis init() {
		if(jedis == null)
			jedis = new Jedis();
		return jedis;
	}
}
