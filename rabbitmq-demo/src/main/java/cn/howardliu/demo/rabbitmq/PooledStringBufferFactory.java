package cn.howardliu.demo.rabbitmq;

import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <br>created at 17-6-22
 *
 * @author liuxh
 * @version 1.0.0
 * @since 1.0.0
 */
public class PooledStringBufferFactory extends BasePooledObjectFactory<StringBuffer> {
    private static final Logger logger = LoggerFactory.getLogger(PooledStringBufferFactory.class);

    public static void main(String[] args) throws Exception {
        GenericObjectPoolConfig config = new GenericObjectPoolConfig();
        config.setMinIdle(1);
        config.setMaxIdle(2);
        config.setMaxTotal(5);
        GenericObjectPool<StringBuffer> objectPool = new GenericObjectPool<>(new PooledStringBufferFactory(), config);

        StringBuffer sb1 = objectPool.borrowObject();
        sb1.append(1);
        System.out.println(sb1);
        objectPool.returnObject(sb1);

        StringBuffer sb2 = objectPool.borrowObject();
        sb2.append(2);
        System.out.println(sb2);

        StringBuffer sb3 = objectPool.borrowObject();
        sb3.append(3);
        System.out.println(sb3);

        StringBuffer sb4 = objectPool.borrowObject();
        sb4.append(4);
        System.out.println(sb4);

        StringBuffer sb5 = objectPool.borrowObject();
        sb5.append(5);
        System.out.println(sb5);
        objectPool.returnObject(sb5);

        StringBuffer sb6 = objectPool.borrowObject();
        sb6.append(6);
        System.out.println(sb6);
    }

    @Override
    public StringBuffer create() throws Exception {
        return new StringBuffer(16);
    }

    @Override
    public PooledObject<StringBuffer> wrap(StringBuffer obj) {
        return new DefaultPooledObject<>(obj);
    }
}
