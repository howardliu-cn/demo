package cn.howardliu.cache.redis.lettuce.cluster;

import cn.howardliu.cache.redis.lettuce.Jackson2Serializer;
import cn.howardliu.cache.redis.lettuce.StringValueSerializer;
import com.google.common.collect.Lists;
import com.lambdaworks.redis.RedisClusterConnection;
import com.lambdaworks.redis.cluster.RedisAdvancedClusterConnection;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.support.SimpleValueWrapper;

import java.util.List;

/**
 * <p>create at 15-10-23</p>
 *
 * @author liufl
 * @since 1.0.0
 */
public class LettuceClusterCache implements Cache {
    private Logger logger = LoggerFactory.getLogger(getClass());
    private final RedisAdvancedClusterConnection<String, String> connection;
    private final String name;

    private StringValueSerializer valueSerializer = new Jackson2Serializer();
    private long defaultExpireTimeInMs = 30000L;

    public LettuceClusterCache(
            RedisAdvancedClusterConnection<String, String> connection, String name) {
        this.connection = Validate.notNull(connection);
        this.name = name;
    }

    public StringValueSerializer getValueSerializer() {
        return valueSerializer;
    }

    public void setValueSerializer(StringValueSerializer valueSerializer) {
        Validate.notNull(valueSerializer);
        this.valueSerializer = valueSerializer;
    }

    public long getDefaultExpireTimeInMs() {
        return defaultExpireTimeInMs;
    }

    public void setDefaultExpireTimeInMs(long defaultExpireTimeInMs) {
        this.defaultExpireTimeInMs = defaultExpireTimeInMs;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public Object getNativeCache() {
        return this.connection;
    }

    private static String typePrefix(String cacheName) {
        return "db-" + cacheName + "~typeKey-";
    }

    private static String valuePrefix(String cacheName) {
        return "db-" + cacheName + "~valueKey-";
    }

    private static String typeRealKey(String cacheName, String key) {
        return "db-" + cacheName + "~typeKey-" + key;
    }

    private static String valueRealKey(String cacheName, String key) {
        return "db-" + cacheName + "~valueKey-" + key;
    }

    @Override
    public ValueWrapper get(Object key) {
        Validate.notNull(key);
        if (!(key instanceof String)) {
            throw new IllegalArgumentException("Support STRING key ONLY!");
        }
        String strKey = key.toString();
        Validate.notEmpty(strKey);
        String valueRealKey = valueRealKey(this.name, strKey);
        String typeRealKey = typeRealKey(this.name, strKey);
        logger.debug("get with real key: {}/{}", valueRealKey, typeRealKey);
        String stringValue = this.connection.get(valueRealKey);
        String typeName = this.connection.get(typeRealKey);
        if (StringUtils.isBlank(stringValue)) {
            return null;
        }
        if (StringUtils.isNotBlank(typeName)) {
            try {
                typeName = this.valueSerializer.deserialize(typeName, String.class);
                logger.debug("Restore value as type {}", typeName);
                Object value = this.valueSerializer.deserialize(stringValue, Class.forName(typeName));
                return new SimpleValueWrapper(value);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        } else {
            logger.debug("Type unknown, use raw value");
            return new SimpleValueWrapper(stringValue);
        }
    }

    @Override
    public <T> T get(Object key, Class<T> type) {
        Validate.notNull(key);
        if (!(key instanceof String)) {
            throw new IllegalArgumentException("Support STRING key ONLY!");
        }
        String strKey = key.toString();
        Validate.notEmpty(strKey);
        String valueRealKey = valueRealKey(this.name, strKey);
        logger.debug("get with real key: {}", valueRealKey);
        String stringValue = this.connection.get(valueRealKey);
        logger.debug("deserialize {} to given type: {}", stringValue, type.getCanonicalName());
        return this.valueSerializer.deserialize(stringValue, type);
    }

    @Override
    public void put(Object key, Object value) {
        Validate.notNull(key);
        if (!(key instanceof String)) {
            throw new IllegalArgumentException("Support STRING key ONLY!");
        }
        String strKey = key.toString();
        Validate.notEmpty(strKey);
        String valueRealKey = valueRealKey(this.name, strKey);
        String typeRealKey = typeRealKey(this.name, strKey);
        String serializedValue = this.valueSerializer.serialize(value);
        String serializedType = this.valueSerializer.serialize(value.getClass().getCanonicalName());
        logger.debug("put with real key: {}/{}, value: {}/{}, expired in {} ms", valueRealKey, typeRealKey,
                serializedValue, serializedType, this.defaultExpireTimeInMs);
        this.connection.psetex(valueRealKey, this.defaultExpireTimeInMs, serializedValue);
        this.connection.psetex(typeRealKey, this.defaultExpireTimeInMs, serializedType);
    }

    @Override
    public ValueWrapper putIfAbsent(Object key, Object value) {
        ValueWrapper existing = this.get(key);
        if (existing == null) {
            this.put(key, value);
            return null;
        } else {
            return existing;
        }
    }

    @Override
    public void evict(Object key) {
        Validate.notNull(key);
        if (!(key instanceof String)) {
            throw new IllegalArgumentException("Support STRING key ONLY!");
        }
        String strKey = key.toString();
        String valueRealKey = valueRealKey(this.name, strKey);
        String typeRealKey = typeRealKey(this.name, strKey);
        logger.debug("del with real key: {}/{}", valueRealKey, typeRealKey);
        this.connection.del(valueRealKey);
        this.connection.del(typeRealKey);
    }

    @Override
    public void clear() {
        String clusterNodes = this.connection.clusterNodes();
        String[] nodeInfos = clusterNodes.split("\n");
        List<String> masterIds = Lists.newArrayList();
        for (String nodeInfo : nodeInfos) {
            String[] infoParts = nodeInfo.split("\\s+");
            if (infoParts[2].contains("master")) {
                masterIds.add(infoParts[0]);
            }
        }
        for (String masterId : masterIds) {
            RedisClusterConnection<String, String> nodeConnection = this.connection.getConnection(masterId);
            List<String> keys = nodeConnection.keys(valuePrefix(this.name) + "*");
            keys.forEach(nodeConnection::del);
            keys = nodeConnection.keys(typePrefix(this.name) + "*");
            keys.forEach(nodeConnection::del);
        }
    }
}
