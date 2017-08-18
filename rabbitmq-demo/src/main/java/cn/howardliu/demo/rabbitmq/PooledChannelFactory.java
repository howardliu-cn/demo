package cn.howardliu.demo.rabbitmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
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
public class PooledChannelFactory extends BasePooledObjectFactory<Channel> {
    private Connection connection;

    public PooledChannelFactory(Connection connection) {
        this.connection = Validate.notNull(connection, "connection cannot be null");
    }

    @Override
    public Channel create() throws Exception {
        return this.connection.createChannel();
    }

    @Override
    public PooledObject<Channel> wrap(Channel channel) {
        return new DefaultPooledObject<>(channel);
    }
}
