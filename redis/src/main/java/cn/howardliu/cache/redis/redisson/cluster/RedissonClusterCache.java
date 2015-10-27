package cn.howardliu.cache.redis.redisson.cluster;

import org.redisson.Redisson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;

/**
 * <br/>create at 15-10-27
 *
 * @author liuxh
 * @since 1.0.0
 */
public class RedissonClusterCache implements Cache {
    private static final Logger logger = LoggerFactory.getLogger(RedissonClusterCache.class);
    private final String name;

    public RedissonClusterCache(String name) {
        this.name = name;
        Redisson redisson = Redisson.create();
        redisson.getConfig();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Object getNativeCache() {
        return null;
    }

    @Override
    public ValueWrapper get(Object key) {
        return null;
    }

    @Override
    public <T> T get(Object key, Class<T> type) {
        return null;
    }

    @Override
    public void put(Object key, Object value) {

    }

    @Override
    public ValueWrapper putIfAbsent(Object key, Object value) {
        return null;
    }

    @Override
    public void evict(Object key) {

    }

    @Override
    public void clear() {

    }
}
