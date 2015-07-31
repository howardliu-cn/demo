package cn.howardliu.demo.curator.atomic;

import cn.howardliu.demo.curator.ZooKeeperClientFactory;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.imps.CuratorFrameworkState;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.framework.recipes.shared.SharedCount;

/**
 * 借助ZooKeeper实现的计数器
 * <br/>create at 15-7-28
 *
 * @author liuxh
 * @since 1.0.0
 */
public class Counter {
    private final static String DEFAULT_PATH = "/zk_util/zk_count";// 默认计数器节点在ZK中的路径
    private final static int DEFAULT_SEED_VALUE = 0;// 默认计数器初始值
    private final SharedCount sharedCount;// curator提供的int格式计数器
    private final InterProcessMutex interProcessMutex;// curator提供的分布式锁

    /**
     * 构造器：使用默认路径、默认初始值
     *
     * @param zkAddress zk地址
     * @throws Exception
     */
    public Counter(String zkAddress) throws Exception {
        this(zkAddress, DEFAULT_PATH);
    }

    /**
     * 构造器：使用指定路径，默认初始值
     *
     * @param zkAddress zk地址
     * @param path      指定计数器在zk中挂载的路径
     * @throws Exception
     */
    public Counter(String zkAddress, String path) throws Exception {
        this(zkAddress, path, DEFAULT_SEED_VALUE);
    }

    /**
     * 构造器：使用指定路径、指定初始值
     *
     * @param zkAddress zk地址
     * @param path      指定计数器在zk中挂载的路径
     * @param seedValue 计数器种子值（指定路径无值时使用）
     * @throws Exception
     */
    public Counter(String zkAddress, String path, int seedValue) throws Exception {
        ZooKeeperClientFactory factory = new ZooKeeperClientFactory();
        factory.setZkAddresses(zkAddress);
        CuratorFramework client = factory.createClient();
        if (client.getState() == CuratorFrameworkState.LATENT) {
            client.start();
        }
        sharedCount = new SharedCount(client, path, seedValue);
        sharedCount.start();
        interProcessMutex = new InterProcessMutex(client, path);
    }

    /**
     * 递增计数
     *
     * @return 递增计数结果
     * @throws Exception ZK errors, connection interruptions, current thread does not own the lock, etc.
     * @see #countUp(int) 按指定步长增加计数
     * @see #countDown() 递减计数
     * @see #countDown(int) 按指定步长减少计数
     */
    public int countUp() throws Exception {
        return countUp(1);
    }

    /**
     * 按指定步长增加计数
     *
     * @param step 指定计数步长
     * @return 按指定步长增加计数结果
     * @throws Exception ZK errors, connection interruptions, current thread does not own the lock, etc.
     * @see #countUp() 递增计数
     * @see #countDown() 递减计数
     * @see #countDown(int) 按指定步长减少计数
     */
    public int countUp(int step) throws Exception {
        return getNextCount(step);
    }

    /**
     * 递减计数
     *
     * @return 递减计数结果
     * @throws Exception ZK errors, connection interruptions, current thread does not own the lock, etc.
     * @see #countDown(int) 按指定步长减少计数
     * @see #countUp() 递增计数
     * @see #countUp(int) 按指定步长增加计数
     */
    public int countDown() throws Exception {
        return countDown(1);
    }

    /**
     * 按指定步长减少计数
     *
     * @param step 指定计数步长
     * @return 按指定步长减少计数结果
     * @throws Exception ZK errors, connection interruptions, current thread does not own the lock, etc.
     * @see #countDown() 递减计数
     * @see #countUp() 递增计数
     * @see #countUp(int) 按指定步长增加计数
     */
    public int countDown(int step) throws Exception {
        return getNextCount(-step);
    }

    /**
     * 获取当前数据
     *
     * @return 当前数据
     */
    public int currentCount() {
        return sharedCount.getCount();
    }

    private int getNextCount(int step) throws Exception {
        interProcessMutex.acquire();
        int count = sharedCount.getCount();
        sharedCount.setCount(count + step);
        interProcessMutex.release();
        return count;
    }
}