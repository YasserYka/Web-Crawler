package crawlers.storage;

import org.redisson.api.RMap;

import crawlers.util.RedisBean;

public class CacheService {

	//Redis's based distributed Map
    private RMap<String, String> CACHE;

    public CacheService(String mapname){
        CACHE = RedisBean.getInstance(mapname);
    }
    
    public String get(String key) {
        return CACHE.get(key);
    }

    public void set(String key, String value){
        CACHE.fastPutAsync(key, value);
    }

}
