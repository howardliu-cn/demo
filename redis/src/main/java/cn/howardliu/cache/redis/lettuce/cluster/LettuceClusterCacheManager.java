package cn.howardliu.cache.redis.lettuce.cluster;

import cn.howardliu.cache.redis.common.Jackson2Serializer;
import cn.howardliu.cache.redis.common.StringValueSerializer;
import com.lambdaworks.redis.cluster.RedisAdvancedClusterConnection;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.support.AbstractCacheManager;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

/**
 * <p>create at 15-10-24</p>
 *
 * @author liufl
 * @since 1.0.0
 */
public class LettuceClusterCacheManager extends AbstractCacheManager {
    Logger logger = LoggerFactory.getLogger(getClass());
    public static final long DEFAULT_EXPIRATION = 300000;
    private final RedisAdvancedClusterConnection<String, String> connection;

    public LettuceClusterCacheManager(
            RedisAdvancedClusterConnection<String, String> connection) {
        this.connection = Validate.notNull(connection);
    }

    // 0 - never expire
    private long defaultExpiration = DEFAULT_EXPIRATION;
    private Map<String, Long> expires = null;
    private StringValueSerializer defaultValueSerializer = new Jackson2Serializer();
    private Map<String, StringValueSerializer> valueSerializers = null;

    public long getDefaultExpiration() {
        return defaultExpiration;
    }

    public void setDefaultExpiration(long defaultExpiration) {
        this.defaultExpiration = defaultExpiration;
        if (defaultExpiration < 0) {
            logger.warn("Negative expiration for value {}, use {}ms instead.", defaultExpiration, DEFAULT_EXPIRATION);
            this.defaultExpiration = 300000;
        } else if (defaultExpiration == 0) {
            logger.warn("It is NOT recommended caching none-expiration values!");
        }
    }

    public Map<String, Long> getExpires() {
        return expires;
    }

    public void setExpires(Map<String, Long> expires) {
        this.expires = expires;
    }

    public StringValueSerializer getDefaultValueSerializer() {
        return defaultValueSerializer;
    }

    public void setDefaultValueSerializer(StringValueSerializer defaultValueSerializer) {
        this.defaultValueSerializer = Validate.notNull(defaultValueSerializer);
    }

    public Map<String, StringValueSerializer> getValueSerializers() {
        return valueSerializers;
    }

    public void setValueSerializers(Map<String, StringValueSerializer> valueSerializers) {
        this.valueSerializers = valueSerializers;
    }

    @Override
    protected Collection<? extends Cache> loadCaches() {
        return Collections.emptyList();
    }

    @Override
    public Cache getCache(String name) {
        Cache cache = super.getCache(name);
        if (cache == null) {
            return createAndAddCache(name);
        }
        return cache;
    }

    protected Cache createAndAddCache(String cacheName) {
        addCache(createCache(cacheName));
        return super.getCache(cacheName);
    }

    private long computeExpiration(String cacheName) {
        Long expiration = null;
        if (expires != null) {
            expiration = expires.get(cacheName);
            if (expiration < 0) {
                logger.warn("Negative expiration for value {}, use {}ms instead.", expiration, defaultExpiration);
                expiration = 300000L;
            } else if (expiration == 0) {
                logger.warn("It is NOT recommended caching none-expiration values!");
            }
        }
        return (expiration != null ? expiration : defaultExpiration);
    }

    private StringValueSerializer computeValueSerializer(String cacheName) {
        StringValueSerializer valueSerializer = null;
        if (this.valueSerializers != null) {
            valueSerializer = valueSerializers.get(cacheName);
        }
        return valueSerializer != null ? valueSerializer : defaultValueSerializer;
    }

    private Cache createCache(String cacheName) {
        LettuceClusterCache clusterCache = new LettuceClusterCache(this.connection, cacheName);
        clusterCache.setDefaultExpireTimeInMs(computeExpiration(cacheName));
        clusterCache.setValueSerializer(computeValueSerializer(cacheName));
        return clusterCache;
    }
}
