package cn.howardliu.demo.nio.netty_nio.protocol.http.json.codec;

import com.alibaba.fastjson.JSON;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.nio.charset.Charset;

/**
 * <br>created at 17-4-17
 *
 * @author liuxh
 * @version 1.0.0
 * @since 1.0.0
 */
public abstract class AbstractHttpJsonEncoder<T> extends MessageToMessageEncoder<T> {
    final static String CHARSET_NAME = "UTF-8";
    final static Charset UTF_8 = Charset.forName(CHARSET_NAME);

    protected ByteBuf encode0(ChannelHandlerContext ctx, Object body) throws Exception {
        return Unpooled.copiedBuffer(JSON.toJSONString(body), UTF_8);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
    }
}
