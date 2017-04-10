package cn.howardliu.demo.nio.netty_nio.protobuf;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * <br>created at 17-4-10
 *
 * @author liuxh
 * @version 1.0.0
 * @since 1.0.0
 */
public class SubReqClientHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        for (int i = 0; i < 10; i++) {
            ctx.write(subReq(i));
        }
        ctx.flush();
    }

    private SubscribeReqProto.SubscribeReq subReq(int i) {
        return SubscribeReqProto.SubscribeReq.newBuilder()
                .setSubReqID(i)
                .setUserName("www.howardliu.cn")
                .setProductName("Netty Boot For Protobuf")
                .addAddress("a")
                .addAddress("b")
                .addAddress("c")
                .build();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("Receive server response: [\n" + msg + "].");
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
