package cn.howardliu.demo.nio.netty_nio.protocol.http.json.client;

import cn.howardliu.demo.nio.netty_nio.protocol.http.json.codec.HttpJsonRequest;
import cn.howardliu.demo.nio.netty_nio.protocol.http.json.codec.HttpJsonResponse;
import cn.howardliu.demo.nio.netty_nio.protocol.http.json.pojo.OrderFactory;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * <br>created at 17-4-17
 *
 * @author liuxh
 * @version 1.0.0
 * @since 1.0.0
 */
public class HttpJsonClientHandler extends SimpleChannelInboundHandler<HttpJsonResponse> {
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(new HttpJsonRequest(null, OrderFactory.create(123)));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpJsonResponse msg) throws Exception {
        System.out
                .println("The client receive response of http header is : " + msg.getHttpResponse().headers().names());
        System.out.println("The client receive response of http body is : " + msg.getResult());
    }
}
