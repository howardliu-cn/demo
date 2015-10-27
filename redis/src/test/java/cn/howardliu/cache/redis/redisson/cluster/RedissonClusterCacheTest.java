package cn.howardliu.cache.redis.redisson.cluster;

import org.junit.*;
import org.redisson.Config;
import org.redisson.Redisson;
import org.redisson.core.RSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <br/>create at 15-10-27
 *
 * @author liuxh
 * @since 1.0.0
 */
public class RedissonClusterCacheTest {
    private static final Logger logger = LoggerFactory.getLogger(RedissonClusterCacheTest.class);

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void test() {
        Config config = new Config();
        config.useClusterServers()
                .setScanInterval(2000) // sets cluster state scan interval
                .addNodeAddress("10.6.2.55:23400")
                .addNodeAddress("10.6.2.55:23401")
                .addNodeAddress("10.6.2.55:23402")
                .addNodeAddress("10.6.2.55:23403")
                .addNodeAddress("10.6.2.55:23404")
                .addNodeAddress("10.6.2.55:23405");
        Redisson redisson = Redisson.create(config);

        RSet<String> mySet = redisson.getSet("mySet");
        mySet.clear();
        mySet.add("1");
        mySet.add("2");
        mySet.add("3");
        RSet<String> mySetCache = redisson.getSet("mySet");
        mySetCache.forEach(logger::info);

//        RList<String> myList = redisson.getList("myList");
//        myList.add("A");
//        myList.add("B");
//        myList.add("C");
//        logger.info(myList.get(0));
//        RList<SampleBean> myListCache = redisson.getList("myList");
//        myListCache.forEach(b -> logger.info(b.toString()));
    }
}
