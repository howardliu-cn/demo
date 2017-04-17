package cn.howardliu.demo.nio.netty_nio.protocol.http.json.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpUtil;

import java.net.InetAddress;
import java.util.List;

import static io.netty.handler.codec.http.HttpHeaderNames.*;
import static io.netty.handler.codec.http.HttpMethod.GET;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * <br>created at 17-4-17
 *
 * @author liuxh
 * @version 1.0.0
 * @since 1.0.0
 */
public class HttpJsonRequestEncoder extends AbstractHttpJsonEncoder<HttpJsonRequest> {
    @Override
    protected void encode(ChannelHandlerContext ctx, HttpJsonRequest msg, List<Object> out) throws Exception {
        ByteBuf body = encode0(ctx, msg.getBody());
        FullHttpRequest request = msg.getRequest();
        if (request == null) {
            request = new DefaultFullHttpRequest(HTTP_1_1, GET, "/do", body);
            request.headers()
                    .set(HOST, InetAddress.getLocalHost().getHostAddress())
                    .set(CONNECTION, HttpHeaderValues.CLOSE)
                    .set(ACCEPT_ENCODING, HttpHeaderValues.GZIP.toString() + ',' + HttpHeaderValues.DEFLATE.toString())
                    .set(ACCEPT_CHARSET, "ISO-8859-1,utf-8;q=0.7,*;q=0.7")
                    .set(ACCEPT_LANGUAGE, "zh")
                    .set(USER_AGENT, "Netty json Http Client side")
                    .set(ACCEPT, "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        }
        HttpUtil.setContentLength(request, body.readableBytes());
        out.add(request);
    }
}
