package cn.howardliu.demo.nio.netty_nio.protocol.http.json.codec;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;

import java.util.List;

/**
 * <br>created at 17-4-17
 *
 * @author liuxh
 * @version 1.0.0
 * @since 1.0.0
 */
public class HttpJsonResponseDecoder extends AbstractHttpJsonDecoder<DefaultFullHttpResponse> {
    public HttpJsonResponseDecoder(Class<?> clazz) {
        this(clazz, false);
    }

    public HttpJsonResponseDecoder(Class<?> clazz, boolean isPrint) {
        super(clazz, isPrint);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, DefaultFullHttpResponse msg, List<Object> out) throws Exception {
        out.add(new HttpJsonResponse(msg, decode0(ctx, msg.content())));
    }
}
