package cn.howardliu.demo.rabbitmq;

import com.rabbitmq.client.Address;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.junit.Before;
import org.junit.Test;

/**
 * <br>created at 17-6-22
 *
 * @author liuxh
 * @version 1.0.0
 * @since 1.0.0
 */
public class PooledConnectionFactoryTest {
    private GenericObjectPool<Connection> objectPool;

    @Before
    public void setUp() throws Exception {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setUsername("admin");
        connectionFactory.setPassword("12345");
        connectionFactory.setVirtualHost("/");
        connectionFactory.setAutomaticRecoveryEnabled(true);
        Address[] addresses = new Address[1];
        addresses[0] = new Address("10.6.3.255", 5672);
        PooledConnectionFactory pooledConnectionFactory = new PooledConnectionFactory(connectionFactory, addresses);
        GenericObjectPoolConfig config = new GenericObjectPoolConfig();
        config.setMinIdle(1);
        config.setMaxIdle(2);
        config.setMaxTotal(5);
         objectPool = new GenericObjectPool<>(pooledConnectionFactory, config);
    }

    @Test
    public void test() throws Exception {
        Connection connection = objectPool.borrowObject();


    }
}