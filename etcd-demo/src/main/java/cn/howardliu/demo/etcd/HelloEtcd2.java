package cn.howardliu.demo.etcd;

import mousio.etcd4j.EtcdClient;
import mousio.etcd4j.requests.EtcdKeyGetRequest;
import mousio.etcd4j.responses.EtcdAuthenticationException;
import mousio.etcd4j.responses.EtcdException;
import mousio.etcd4j.responses.EtcdKeysResponse;
import mousio.etcd4j.responses.EtcdVersionResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.util.concurrent.TimeoutException;

/**
 * <br/>created at 16-4-13
 *
 * @author liuxh
 * @since 1.1.21
 */
public class HelloEtcd2 {
    private static final Logger logger = LoggerFactory.getLogger(HelloEtcd2.class);

    public static void main(String[] args)
            throws IOException, EtcdAuthenticationException, TimeoutException, EtcdException {
        try(EtcdClient client = new EtcdClient(
                URI.create("http://10.6.2.151:4001"),
                URI.create("http://10.6.2.152:4001"),
                URI.create("http://10.6.2.153:4001"))) {
            EtcdVersionResponse version = client.version();
            System.out.println(version.cluster);
            System.out.println(version.server);

            EtcdKeysResponse response = client.put("foo", "bar").send().get();
            System.out.println(response.node.value);
        } catch (Exception e) {
            logger.error("", e);
        }
    }
}
