package cn.howardliu.cache.redis.lettuce;

import org.springframework.cache.interceptor.KeyGenerator;

import java.lang.reflect.Method;

/**
 * 用于Spring Cache Abstraction + Redis String Template的KeyGenerator
 * <br/>create at 15-8-17
 *
 * @author liufl
 * @since 1.0.0
 */
public class StringRedisKeyGenerator implements KeyGenerator {
    @Override
    public Object generate(Object target, Method method, Object... params) {
        StringBuilder sb = new StringBuilder();
        sb.append(target.getClass().getName()).append("_"); // 类名作为Repo名
        // sb.append(method.getName()).append("_"); // 方法名会使get/set使用不同的键值(-_-!)
        for (Object obj : params) {
            sb.append(obj.toString()).append("-");
        }
        return sb.toString();
    }
}
