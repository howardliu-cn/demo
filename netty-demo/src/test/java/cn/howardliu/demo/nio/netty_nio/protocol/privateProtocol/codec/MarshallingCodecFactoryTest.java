package cn.howardliu.demo.nio.netty_nio.protocol.privateProtocol.codec;

import org.junit.Test;
import org.springframework.util.Assert;

/**
 * <br>created at 17-4-18
 *
 * @author liuxh
 * @version 1.0.0
 * @since 1.0.0
 */
public class MarshallingCodecFactoryTest {

    @Test
    public void testMarshaller() throws Exception {
        Assert.notNull(MarshallingCodecFactory.marshaller());
    }

    @Test
    public void testUnmarshaller() throws Exception {
        Assert.notNull(MarshallingCodecFactory.unmarshaller());
    }
}