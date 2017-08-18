package cn.howardliu.demo.rabbitmq;

import com.rabbitmq.client.Address;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.apache.commons.lang3.Validate;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

/**
 * <br>created at 17-6-22
 *
 * @author liuxh
 * @version 1.0.0
 * @since 1.0.0
 */
public class PooledConnectionFactory extends BasePooledObjectFactory<Connection> {
    private ConnectionFactory connectionFactory;
    private Address[] addresses;

    public PooledConnectionFactory(ConnectionFactory connectionFactory, Address[] addresses) {
        this.connectionFactory = Validate.notNull(connectionFactory, "connectionFactory cannot be null");
        this.addresses = Validate.notEmpty(addresses, "addresses cannot be null or empty");
    }

    @Override
    public Connection create() throws Exception {
        return this.connectionFactory.newConnection(this.addresses);
    }

    @Override
    public PooledObject<Connection> wrap(Connection connection) {
        return new DefaultPooledObject<>(connection);
    }
}
