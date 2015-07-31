package cn.howardliu.demo.curator.atomic;

import cn.howardliu.demo.curator.ZooKeeperClientFactory;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.imps.CuratorFrameworkState;
import org.apache.curator.framework.recipes.atomic.AtomicValue;
import org.apache.curator.framework.recipes.atomic.CachedAtomicLong;
import org.apache.curator.framework.recipes.atomic.DistributedAtomicLong;

/**
 * <br/>create at 15-7-28
 *
 * @author liuxh
 * @since 1.0.0
 */
public class LongVersion {
    private static final String PRE_VERSION_PATH = "/control/version/long";
    private static final String COMMON_PATH = "common";
    private static final int DEFAULT_CACHE_FACTOR = 1;
    private final CachedAtomicLong cachedLong;

    /**
     * 构造器：返回长整型数据，可以作为版本号
     *
     * @param zkAddress zk地址
     */
    public LongVersion(String zkAddress) {
        this(zkAddress, COMMON_PATH, DEFAULT_CACHE_FACTOR);
    }

    /**
     * 构造器：返回长整型数据，可以作为版本号
     *
     * @param zkAddress zk地址
     * @param category  zk上的路径
     */
    public LongVersion(String zkAddress, String category) {
        this(zkAddress, category, DEFAULT_CACHE_FACTOR);
    }

    /**
     * 构造器：返回长整型数据，可以作为版本号
     *
     * @param zkAddress   zk地址
     * @param cacheFactor 通过getNext方法获取数据的步长
     */
    public LongVersion(String zkAddress, int cacheFactor) {
        this(zkAddress, COMMON_PATH, cacheFactor);
    }

    /**
     * 构造器：返回长整型数据，可以作为版本号
     *
     * @param zkAddress   zk地址
     * @param category    zk上的路径
     * @param cacheFactor 通过getNext方法获取数据的步长
     */
    public LongVersion(String zkAddress, String category, int cacheFactor) {
        ZooKeeperClientFactory factory = new ZooKeeperClientFactory();
        factory.setZkAddresses(zkAddress);
        CuratorFramework client = factory.createClient();
        if (client.getState() == CuratorFrameworkState.LATENT) {
            client.start();
        }
        category = (category == null || category.trim().isEmpty()) ? COMMON_PATH : category;
        DistributedAtomicLong dal = new DistributedAtomicLong(client, PRE_VERSION_PATH + "/" + category,
                factory.getRetryPolicy());
        cachedLong = new CachedAtomicLong(dal, cacheFactor);
    }

    public long getNext() throws Exception {
        AtomicValue<Long> value = cachedLong.next();
        return value.succeeded() ? value.postValue() : 0;
    }
}
