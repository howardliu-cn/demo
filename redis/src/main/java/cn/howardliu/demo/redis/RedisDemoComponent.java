package cn.howardliu.demo.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

/**
 * <br/>create at 15-7-28
 *
 * @author liuxh
 * @since 1.0.0
 */
@Component("redisDemo")
public class RedisDemoComponent {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Cacheable("default")
    public String getTime(String time) {
        logger.debug("time:{}", time);
        long now = System.currentTimeMillis();
        logger.debug("now:{}", now);
        return now + "";
    }
}
