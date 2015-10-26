package cn.howardliu.demo.redis;

import static org.junit.Assert.*;

import com.lambdaworks.redis.RedisFuture;
import com.lambdaworks.redis.cluster.RedisAdvancedClusterAsyncConnection;
import com.lambdaworks.redis.cluster.RedisAdvancedClusterConnection;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.concurrent.ExecutionException;

/**
 * <br/>create at 15-10-26
 *
 * @author liuxh
 * @since 1.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath*:spring-test.xml")
public class TestRedisUseLettuce {
    @Autowired
    private RedisAdvancedClusterConnection<String, String> clusterConnection;
    @Autowired
    private RedisAdvancedClusterAsyncConnection<String, String> asyncClusterConnection;

    @Test
    public void test() throws ExecutionException, InterruptedException {
        String set = clusterConnection.set("key", "value");
        String get = clusterConnection.get("key");
        assertEquals("OK", set);
        assertEquals("value", get);
    }

    @Test
    public void testAsync() throws ExecutionException, InterruptedException {
        RedisFuture<String> set = asyncClusterConnection.set("key", "value");
        RedisFuture<String> get = asyncClusterConnection.get("key");
        assertEquals("OK", set.get());
        assertEquals("value", get.get());
    }
}
