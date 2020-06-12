package crawlers.storage;

import org.redisson.api.RMap;

import crawlers.util.RedisBean;

public class Cache {

	//Redis's based distributed Map
    private RMap<String, String> CACHE;

    public Cache(String mapname){
        CACHE = RedisBean.getInstance(mapname);
    }
    
    public String get(String key) {
        return CACHE.get(key);
    }

    public void set(String key, String value){
        CACHE.fastPutAsync(key, value);
    }

}
