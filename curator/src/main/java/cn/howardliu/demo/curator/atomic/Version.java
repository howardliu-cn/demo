package cn.howardliu.demo.curator.atomic;

/**
 * 使用ZooKeeper管理的版本号生成工具
 * <br/>create at 15-7-28
 *
 * @author liuxh
 * @since 1.0.0
 */
public class Version {
    private static final String PRE_VERSION_PATH = "/control/version/int";
    private static final String COMMON_PATH = "common";
    private final Counter counter;

    /**
     * 构造器：使用默认路径
     *
     * @param zkAddress zk地址
     * @throws Exception
     */
    public Version(String zkAddress) throws Exception {
        this(zkAddress, COMMON_PATH);
    }

    /**
     * 构造器
     *
     * @param zkAddress zk地址
     * @param category  指定路径。如果指定路径为空（{@code null}，或空字符处，或全部为空格的字符串）
     * @throws Exception
     */
    public Version(String zkAddress, String category) throws Exception {
        category = (category == null || category.trim().isEmpty()) ? COMMON_PATH : category;
        counter = new Counter(zkAddress, PRE_VERSION_PATH + "/" + category);
    }

    /**
     * 获取当前版本号
     *
     * @return 版本号
     */
    public int getCurrentVersion() {
        return counter.currentCount();
    }

    /**
     * 获取下一个版本号
     *
     * @return 下一个版本号
     * @throws Exception ZK errors, connection interruptions, current thread does not own the lock, etc.
     */
    public int getNextVersion() throws Exception {
        return counter.countUp();
    }
}
