package cn.howardliu.demo.etcd;

import jetcd.EtcdClient;
import jetcd.EtcdClientFactory;
import jetcd.EtcdException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <br/>created at 16-4-13
 *
 * @author liuxh
 * @since 1.0.0
 */
public class HelloEtcd {
    private static final Logger logger = LoggerFactory.getLogger(HelloEtcd.class);

    public static void main(String[] args) throws EtcdException {
        EtcdClient client = EtcdClientFactory.newInstance("http://10.6.2.151:4001");
        System.out.println(client.version());
        client.set("hello", "world");
        System.out.println(client.get("hello"));
        client.delete("hello");
    }
}
