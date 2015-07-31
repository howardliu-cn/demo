package cn.howardliu.demo.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * <br/>create at 15-7-30
 *
 * @author liuxh
 * @since 1.0.0
 */
public class TestGetData {
    public static void main(String[] args) throws Exception {
        CuratorFramework client = CuratorFrameworkFactory.builder().connectString("127.0.0.1:2181")
                .sessionTimeoutMs(15000).retryPolicy(new ExponentialBackoffRetry(1000, 5)).build();
        client.start();
        String path = "/com/wfj/coordinator/test/operation/test-coordinator-task/default/status/test";
        byte[] bytes = client.getData().forPath(path);
        System.out.println(new String(bytes, "UTF-8"));
        client.setData().forPath(path, "FAILURE".getBytes("UTF-8"));
        bytes = client.getData().forPath(path);
        System.out.println(new String(bytes, "UTF-8"));
    }
}
