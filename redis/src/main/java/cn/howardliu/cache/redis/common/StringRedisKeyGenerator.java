package cn.howardliu.cache.redis.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.interceptor.KeyGenerator;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * 用于Spring Cache Abstraction + Redis String Template 的KeyGenerator
 * <br/>create at 15-11-5
 *
 * @author liuxh
 * @since 1.0.0
 */
public class StringRedisKeyGenerator implements KeyGenerator {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    public StringRedisKeyGenerator() {
        logger.info("init StringRedisKeyGenerator");
    }

    @Override
    public Object generate(Object target, Method method, Object... params) {
        logger.info("input parameters is target={}, method={}, params={}", target.getClass().getName(),
                method.getName(), Arrays.toString(params));
        StringBuilder sb = new StringBuilder();
        // 从 Cacheable 或 CacheEvict 获取value值
        Annotation[] annotations = method.getDeclaredAnnotations();
        for (Annotation annotation : annotations) {
            if (annotation instanceof Cacheable || annotation instanceof CachePut || annotation instanceof CacheEvict) {
                try {
                    Method m = annotation.getClass().getDeclaredMethod("value");
                    String[] values = (String[]) m.invoke(annotation);
                    for (String value : values) {
                        sb.append(value).append("_");
                    }
                } catch (NoSuchMethodException e) {
                    logger.error("you should check the jdk!", e);// never happen if you use Java HotSpot.
                } catch (InvocationTargetException e) {
                    logger.error("the underlying method throws the exception", e);
                } catch (IllegalAccessException e) {
                    logger.error(
                            "this Method object is enforcing Java language access control and the underlying method is inaccessible.",
                            e);
                }
            }
        }
        // sb.append(target.getClass().getName()).append("_"); // 类名作为Repo名
        // sb.append(method.getName()).append("_"); // 方法名会使get/set使用不同的键值(-_-!)
        for (Object obj : params) {
            sb.append(obj.toString()).append("-");
        }
        return sb.toString();
    }
}
