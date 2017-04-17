package cn.howardliu.demo.nio.netty_nio.protocol.http.json.codec;

import com.alibaba.fastjson.JSON;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.nio.charset.Charset;

/**
 * <br>created at 17-4-17
 *
 * @author liuxh
 * @version 1.0.0
 * @since 1.0.0
 */
public abstract class AbstractHttpJsonDecoder<T> extends MessageToMessageDecoder<T> {
    final static String CHARSET_NAME = "UTF-8";
    final static Charset UTF_8 = Charset.forName(CHARSET_NAME);
    private Class<?> clazz;
    private boolean isPrint;

    protected AbstractHttpJsonDecoder(Class<?> clazz) {
        this(clazz, false);
    }

    protected AbstractHttpJsonDecoder(Class<?> clazz, boolean isPrint) {
        this.clazz = clazz;
        this.isPrint = isPrint;
    }

    protected Object decode0(ChannelHandlerContext ctx, ByteBuf body) throws Exception {
        String content = body.toString(UTF_8);
        if(isPrint) {
            System.out.println("The body is : " + content);
        }
        return JSON.parseObject(content, clazz);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
    }
}
