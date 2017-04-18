package cn.howardliu.demo.nio.netty_nio.protocol.privateProtocol.client;

import cn.howardliu.demo.nio.netty_nio.protocol.privateProtocol.MessageType;
import cn.howardliu.demo.nio.netty_nio.protocol.privateProtocol.struct.Header;
import cn.howardliu.demo.nio.netty_nio.protocol.privateProtocol.struct.NettyMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * <br>created at 17-4-18
 *
 * @author liuxh
 * @version 1.0.0
 * @since 1.0.0
 */
public class HeartbeatReqHandler extends ChannelInboundHandlerAdapter {
    private volatile ScheduledFuture<?> heartbeat;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        NettyMessage message = (NettyMessage) msg;
        if (message.getHeader() != null && message.getHeader().getType() == MessageType.LOGIN_RESP.value()) {
            heartbeat = ctx.executor().scheduleAtFixedRate(new HeartbeatTask(ctx), 0, 5000, TimeUnit.MILLISECONDS);
        } else if (message.getHeader() != null && message.getHeader().getType() == MessageType.HEARTBEAT_RESP.value()) {
            System.out.println("Client receive server heartbeat message <---: " + message);
        } else {
            ctx.fireChannelRead(msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        if (heartbeat != null) {
            heartbeat.cancel(true);
            heartbeat = null;
        }
        ctx.fireExceptionCaught(cause);
    }

    private class HeartbeatTask implements Runnable {
        private final ChannelHandlerContext ctx;

        public HeartbeatTask(ChannelHandlerContext ctx) {
            this.ctx = ctx;
        }

        @Override
        public void run() {
            NettyMessage heartbeat = buildHeartBeat();
            System.out.println("Client send heartbeat message to server :---> " + heartbeat);
            ctx.writeAndFlush(heartbeat);
        }

        private NettyMessage buildHeartBeat() {
            NettyMessage message = new NettyMessage();
            Header header = new Header();
            header.setType(MessageType.HEARTBEAT_REQ.value());
            message.setHeader(header);
            return message;
        }
    }
}
