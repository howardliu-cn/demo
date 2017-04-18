package cn.howardliu.demo.nio.netty_nio.protocol.privateProtocol.server;

import cn.howardliu.demo.nio.netty_nio.protocol.privateProtocol.MessageType;
import cn.howardliu.demo.nio.netty_nio.protocol.privateProtocol.struct.Header;
import cn.howardliu.demo.nio.netty_nio.protocol.privateProtocol.struct.NettyMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * <br>created at 17-4-18
 *
 * @author liuxh
 * @version 1.0.0
 * @since 1.0.0
 */
public class HeartbeatRespHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        NettyMessage message = (NettyMessage) msg;
        if (message.getHeader() != null && message.getHeader().getType() == MessageType.HEARTBEAT_REQ.value()) {
            System.out.println("Receive client heartbeat message <---: " + message);
            NettyMessage heartbeat = buildHeartbeat();
            System.out.println("Send heartbeat response message to client :---> " + heartbeat);
            ctx.writeAndFlush(heartbeat);
        } else {
            ctx.fireChannelRead(msg);
        }
    }

    private NettyMessage buildHeartbeat() {
        NettyMessage message = new NettyMessage();
        Header header = new Header();
        header.setType(MessageType.HEARTBEAT_RESP.value());
        message.setHeader(header);
        return message;
    }
}
