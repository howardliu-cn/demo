package cn.howardliu.demo.curator;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * Zk客户端工厂类
 * <br/>create at 15-5-29
 *
 * @author liuxh
 * @since 1.0.0
 */
public class ZooKeeperClientFactory {
    private String zkAddresses;
    private RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 5);
    private Integer connectionTimeoutMs;
    private Integer sessionTimeoutMs;

    /**
     * Zk连接地址。
     *
     * @return Zk连接地址
     */
    public String getZkAddresses() {
        return zkAddresses;
    }

    /**
     * Zk连接地址
     *
     * @param zkAddresses Zk连接地址
     */
    public void setZkAddresses(String zkAddresses) {
        this.zkAddresses = zkAddresses;
    }

    /**
     * Zk重新连接策略
     *
     * @return Zk重新连接策略
     */
    public RetryPolicy getRetryPolicy() {
        return retryPolicy;
    }

    /**
     * Zk重新连接策略
     *
     * @param retryPolicy Zk重新连接策略
     */
    public void setRetryPolicy(RetryPolicy retryPolicy) {
        this.retryPolicy = retryPolicy;
    }

    /**
     * 连接超时时间，ms
     *
     * @return 连接超时时间，ms
     */
    public Integer getConnectionTimeoutMs() {
        return connectionTimeoutMs;
    }

    /**
     * 连接超时时间，ms
     *
     * @param connectionTimeoutMs 连接超时时间，ms
     */
    public void setConnectionTimeoutMs(Integer connectionTimeoutMs) {
        this.connectionTimeoutMs = connectionTimeoutMs;
    }

    /**
     * 会话超时时间，ms
     * @return 会话超时时间，ms
     */
    public Integer getSessionTimeoutMs() {
        return sessionTimeoutMs;
    }

    /**
     * 会话超时时间，ms
     * @param sessionTimeoutMs 会话超时时间，ms
     */
    public void setSessionTimeoutMs(Integer sessionTimeoutMs) {
        this.sessionTimeoutMs = sessionTimeoutMs;
    }

    /**
     * 新建一个客户端。
     * @return 新的客户端对象。
     */
    public CuratorFramework createClient() {
        CuratorFrameworkFactory.Builder builder = CuratorFrameworkFactory.builder();
        builder.connectString(zkAddresses).retryPolicy(retryPolicy);
        if (connectionTimeoutMs != null) {
            builder.connectionTimeoutMs(connectionTimeoutMs);
        }
        if (sessionTimeoutMs != null) {
            builder.sessionTimeoutMs(sessionTimeoutMs);
        }
        return builder.build();
    }
}
