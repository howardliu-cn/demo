package cn.howardliu.demo.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.test.TestingCluster;
import org.apache.curator.test.TestingServer;
import org.apache.curator.test.TestingZooKeeperServer;
import org.apache.zookeeper.CreateMode;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * 通过TestingCluster测试
 * <br/>create at 15-7-29
 *
 * @author liuxh
 * @since 1.0.0
 */
public class TestCuratorSample {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Test
    public void testServer() throws Exception {
        String path = "/zk";
        TestingServer server = new TestingServer(2181, new File("/home/liuxh/temp/zk/data"));
        CuratorFramework client = CuratorFrameworkFactory.builder().connectString(server.getConnectString())
                .sessionTimeoutMs(5000).retryPolicy(new ExponentialBackoffRetry(1000, 3)).build();
        client.start();
        client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(path, "init".getBytes());
        logger.debug(client.getChildren().forPath(path) + "");
        server.close();
    }

    @Test
    @Ignore
    public void testCluster() throws Exception {
        TestingCluster cluster = new TestingCluster(3);
        cluster.start();
        Thread.sleep(2000);
        TestingZooKeeperServer leader = null;
        for (TestingZooKeeperServer zs : cluster.getServers()) {
            logger.debug(zs.getInstanceSpec().getServerId() + "-"
                    + zs.getQuorumPeer().getServerState() + "-"
                    + zs.getInstanceSpec().getDataDirectory().getAbsoluteFile());
            if (zs.getQuorumPeer().getServerState().equals("leading")) {
                leader = zs;
            }
        }
        assert leader != null;
        leader.kill();
        logger.debug("--After leader kill:");
        for (TestingZooKeeperServer zs : cluster.getServers()) {
            logger.debug(zs.getInstanceSpec().getServerId() + "-"
                    + zs.getQuorumPeer().getServerState() + "-"
                    + zs.getInstanceSpec().getDataDirectory().getAbsoluteFile());
        }
        cluster.stop();
    }
}
