package cn.howardliu.demo.nio.netty_nio.protocol.http.json.codec;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

import java.util.List;

import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpResponseStatus.BAD_REQUEST;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * <br>created at 17-4-17
 *
 * @author liuxh
 * @version 1.0.0
 * @since 1.0.0
 */
public class HttpJsonRequestDecoder extends AbstractHttpJsonDecoder<FullHttpRequest> {
    public HttpJsonRequestDecoder(Class<?> clazz) {
        this(clazz, false);
    }

    public HttpJsonRequestDecoder(Class<?> clazz, boolean isPrint) {
        super(clazz, isPrint);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, FullHttpRequest msg, List<Object> out) throws Exception {
        if (!msg.decoderResult().isSuccess()) {
            sendError(ctx, BAD_REQUEST);
            return;
        }
        out.add(new HttpJsonRequest(msg, decode0(ctx, msg.content())));
    }

    private static void sendError(ChannelHandlerContext ctx, HttpResponseStatus status) {
        FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, status,
                Unpooled.copiedBuffer("Failure: " + status.toString() + "\r\n",
                        CharsetUtil.UTF_8));
        response.headers().set(CONTENT_TYPE, HttpHeaderValues.TEXT_PLAIN + "; charset=UTF-8");
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }
}
