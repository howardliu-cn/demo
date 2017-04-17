package cn.howardliu.demo.nio.netty_nio.protocol.http.json.server;

import cn.howardliu.demo.nio.netty_nio.protocol.http.json.codec.HttpJsonRequest;
import cn.howardliu.demo.nio.netty_nio.protocol.http.json.codec.HttpJsonResponse;
import cn.howardliu.demo.nio.netty_nio.protocol.http.json.pojo.Address;
import cn.howardliu.demo.nio.netty_nio.protocol.http.json.pojo.Order;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

import java.util.ArrayList;
import java.util.List;

import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpResponseStatus.INTERNAL_SERVER_ERROR;

/**
 * <br>created at 17-4-17
 *
 * @author liuxh
 * @version 1.0.0
 * @since 1.0.0
 */
public class HttpJsonServerHandler extends SimpleChannelInboundHandler<HttpJsonRequest> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpJsonRequest msg) throws Exception {
        HttpRequest request = msg.getRequest();
        Order order = (Order) msg.getBody();
        System.out.println("Http server receive request: " + order);
        doBusiness(order);
        ChannelFuture future = ctx.writeAndFlush(new HttpJsonResponse(null, order));
        if (!HttpUtil.isKeepAlive(request)) {
            future.addListener(future1 -> ctx.close());
        }
    }

    private void doBusiness(Order order) {
        order.getCustomer().setFirstName("狄");
        order.getCustomer().setLastName("仁杰");
        List<String> midNames = new ArrayList<>();
        midNames.add("李元芳");
        order.getCustomer().setMiddleNames(midNames);
        Address address = order.getBillTo();
        address.setCity("洛阳");
        address.setCountry("大唐");
        address.setState("河南道");
        address.setPostCode("123456");
        order.setBillTo(address);
        order.setShipTo(address);

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        if (ctx.channel().isActive()) {
            sendError(ctx, INTERNAL_SERVER_ERROR);
        }
    }

    private void sendError(ChannelHandlerContext ctx, HttpResponseStatus status) {
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, status,
                Unpooled.copiedBuffer("失败: " + status.toString() + "\r\n", CharsetUtil.UTF_8));
        response.headers().set(CONTENT_TYPE, HttpHeaderValues.TEXT_PLAIN + "; charset=UTF-8");
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }
}
