package cn.howardliu.demo.rabbitmq;

import org.apache.commons.pool2.BaseKeyedPooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.commons.pool2.impl.GenericKeyedObjectPool;
import org.apache.commons.pool2.impl.GenericKeyedObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <br>created at 17-6-22
 *
 * @author liuxh
 * @version 1.0.0
 * @since 1.0.0
 */
public class KeyedPooledStringBufferFactory extends BaseKeyedPooledObjectFactory<String, StringBuffer> {
    private static final Logger logger = LoggerFactory.getLogger(KeyedPooledStringBufferFactory.class);

    public static void main(String[] args) throws Exception {
        GenericKeyedObjectPoolConfig config = new GenericKeyedObjectPoolConfig();
        config.setMinIdlePerKey(2);
        config.setMaxIdlePerKey(2);
        config.setMaxTotalPerKey(2);
        config.setMaxTotal(5);

        GenericKeyedObjectPool<String, StringBuffer> objectPool = new GenericKeyedObjectPool<>(
                new KeyedPooledStringBufferFactory(), config);

        StringBuffer sb1 = objectPool.borrowObject("1");
        sb1.append(1);
        System.out.println(sb1);
        objectPool.returnObject("1", sb1);

        StringBuffer sb2= objectPool.borrowObject("2");
        sb2.append(2);
        System.out.println(sb2);
        objectPool.returnObject("2", sb2);

        System.out.println(objectPool.getNumIdle("1"));
        System.out.println(objectPool.getNumIdle());
    }

    @Override
    public StringBuffer create(String key) throws Exception {
        StringBuffer stringBuffer = new StringBuffer(16);
        stringBuffer.append("key:").append(key);
        return stringBuffer;
    }

    @Override
    public PooledObject<StringBuffer> wrap(StringBuffer value) {
        return new DefaultPooledObject<>(value);
    }

    @Override
    public boolean validateObject(String key, PooledObject<StringBuffer> p) {
        return super.validateObject(key, p);
    }
}
