package cn.howardliu.demo.heart;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * <br>created at 17-3-30
 *
 * @author liuxh
 * @since 1.0.0
 */
public class CustomHeartBeatHandler implements HeartBeatHandler, Closeable {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    public static final byte PING_MSG = 1;
    public static final byte PONG_MSG = 2;

    public void channelRead(ChannelContext ctx, ByteBuffer buffer) throws Exception {
        if (buffer.get(4) == PING_MSG) {
            pong(ctx);
        } else if (buffer.get(4) == PONG_MSG) {
            logger.debug("get pong msg");
        } else {
            handleData(ctx, buffer);
        }
    }

    protected void handleData(ChannelContext ctx, ByteBuffer buffer) {
        logger.debug("get data: " + buffer);
    }

    protected void ping(ChannelContext ctx) {
        ByteBuffer buffer = ByteBuffer.allocate(5);
        buffer.putInt(5);
        buffer.put(PING_MSG);
        ctx.writeAndFlush(buffer);
    }

    protected void ping(SocketChannel channel) {
        ByteBuffer buffer = ByteBuffer.allocate(5);
        buffer.putInt(5);
        buffer.put(PING_MSG);
        try {
            channel.write(buffer);
        } catch (IOException e) {
            logger.error("An exception was thrown while sending ping single", e);
        }
    }

    protected void pong(ChannelContext ctx) {
        ByteBuffer buffer = ByteBuffer.allocate(5);
        buffer.putInt(5);
        buffer.put(PONG_MSG);
        ctx.writeAndFlush(buffer);
    }

    @Override
    public void userEventTriggered(ChannelContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent e = (IdleStateEvent) evt;
            switch (e.state()) {
                case READER_IDLE:
                    handleReaderIdle(ctx);
                    break;
                case WRITER_IDLE:
                    handleWriterIdle(ctx);
                    break;
                case ALL_IDLE:
                    handleAllIdle(ctx);
                    break;
            }
        }
    }

    public void channelActive(ChannelContext ctx) throws Exception {
        logger.debug("---" + ctx.channel().getRemoteAddress() + " is active---");
    }

    public void channelInactive(ChannelContext ctx) throws Exception {
        logger.debug("---" + ctx.channel().getRemoteAddress() + " is inactive---");
    }

    protected void handleReaderIdle(ChannelContext ctx) {
        logger.debug("---READER_IDLE---");
    }

    protected void handleWriterIdle(ChannelContext ctx) {
        logger.debug("---WRITER_IDLE---");
    }

    protected void handleAllIdle(ChannelContext ctx) {
        logger.debug("---ALL_IDLE---");
        this.ping(ctx);
    }

    @Override
    public void close() throws IOException {
    }
}
