package cn.howardliu.demo.rabbitmq;

import com.rabbitmq.client.Address;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.apache.commons.lang3.Validate;
import org.apache.commons.pool2.BaseKeyedPooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <br>created at 17-6-22
 *
 * @author liuxh
 * @version 1.0.0
 * @since 1.0.0
 */
public class KeyedPooledConnectionFactory extends BaseKeyedPooledObjectFactory<String, Connection> {
    private static final Logger logger = LoggerFactory.getLogger(KeyedPooledConnectionFactory.class);
    private ConnectionFactory connectionFactory;
    private Address[] addresses;

    public KeyedPooledConnectionFactory(ConnectionFactory connectionFactory, Address[] addresses) {
        this.connectionFactory = Validate.notNull(connectionFactory, "connectionFactory cannot be null");
        this.addresses = Validate.notEmpty(addresses, "addresses cannot be null or empty");
    }

    @Override
    public Connection create(String key) throws Exception {
        return connectionFactory.newConnection(addresses);
    }

    @Override
    public PooledObject<Connection> wrap(Connection connection) {
        return new DefaultPooledObject<>(connection);
    }
}
