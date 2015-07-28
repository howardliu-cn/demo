package cn.howardliu.demo.redis;

import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath*:spring.xml")
public class RedisDemoComponentTest extends TestCase {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private RedisDemoComponent redisDemo;

    @Test
    public void testGetTime() throws Exception {
        String time1 = redisDemo.getTime("time");
        logger.debug("time:{}", time1);
        String time2 = redisDemo.getTime("time");
        logger.debug("time:{}", time2);
        Assert.assertEquals(time1, time2);
    }
}