package cn.howardliu.cache.redis.common;

/**
 * <p>create at 15-10-22</p>
 *
 * @author liufl
 * @since 1.0.0
 */
public interface StringValueSerializer {
    <T> String serialize(T value);

    <T> T deserialize(String stringValue, Class<T> clazz);
}
