package cn.howardliu.demo.redis;

import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

import java.util.HashSet;
import java.util.Set;

/**
 * <br/>create at 15-10-23
 *
 * @author liuxh
 * @since 1.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath*:spring.xml")
public class TestJedisCluster {
    JedisCluster jc = null;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
        Set<HostAndPort> jedisClusterNodes = new HashSet<>();
        jedisClusterNodes.add(new HostAndPort("127.0.0.1", 7379));
        jedisClusterNodes.add(new HostAndPort("127.0.0.1", 7379));
        jc = new JedisCluster(jedisClusterNodes);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void test() throws InterruptedException {
        String key = "test_operate_during_failover";
        jc.del(key);
        long failureTime = 0;
        long recoveryTime = 0;
        while (true) {
            try {
                String result = jc.get(key);
                if (failureTime != 0 && recoveryTime == 0) {
                    recoveryTime = System.currentTimeMillis();
                    System.out.println("Cluster is recovered! Downtime lasted " + (recoveryTime - failureTime) + " ms");
                }
                System.out.println(result);
                jc.set(key, System.currentTimeMillis() + "");
            } catch (Exception e) {
                if (failureTime == 0) {
                    failureTime = System.currentTimeMillis();
                }
                e.printStackTrace();
            }
            Thread.sleep(1000);
        }
    }
}
