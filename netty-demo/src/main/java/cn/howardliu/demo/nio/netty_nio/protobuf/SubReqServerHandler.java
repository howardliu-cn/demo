package cn.howardliu.demo.nio.netty_nio.protobuf;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * <br>created at 17-4-10
 *
 * @author liuxh
 * @version 1.0.0
 * @since 1.0.0
 */
@Sharable
public class SubReqServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        SubscribeReqProto.SubscribeReq req = (SubscribeReqProto.SubscribeReq) msg;
        if ("www.howardliu.cn".equals(req.getUserName())) {
            System.out.println("Service accept client subscribe req: [\n" + req.toString() + "]");
            ctx.writeAndFlush(resp(req.getSubReqID()));
        }
    }

    private SubscribeRespProto.SubscribeResp resp(int subReqID) {
        return SubscribeRespProto.SubscribeResp.newBuilder()
                .setSubReqID(subReqID)
                .setRespCode(0)
                .setDesc("Netty book order succeed, 3 days later, sent to the designated address.").build();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
