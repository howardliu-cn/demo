package cn.howardliu.demo.redis;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.StringRedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.URL;

/**
 * <br/>create at 15-7-28
 *
 * @author liuxh
 * @since 1.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath*:spring.xml")
public class TestRedisTemplate {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void testStringRedisTemplate() {
        String key = "key";
        String value = "20150728";
        ValueOperations<String, String> stringValueCacheOps = stringRedisTemplate.opsForValue();
        stringValueCacheOps.set(key, value);
        if(stringRedisTemplate.hasKey(key)) {
            String v = stringValueCacheOps.get(key);
            logger.debug(v);
            Assert.assertEquals(value, v);
        }
        stringRedisTemplate.convertAndSend(key, "201507282041");
        redisTemplate.convertAndSend(key, "201507282051");
    }

    @Test
    public void testCallBack() {
        String key = "key";
        String value = "201507282037";
        String v = stringRedisTemplate.execute((RedisConnection connection) -> {
            StringRedisConnection con = ((StringRedisConnection) connection);
            con.set(key, value);
            logger.debug("从redis中取出的值为：{}", con.get(key));
            return con.get(key);
        });
        Assert.assertEquals(value, v);
    }
}
