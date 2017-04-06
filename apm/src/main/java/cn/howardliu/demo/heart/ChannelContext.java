package cn.howardliu.demo.heart;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;

/**
 * <br>created at 17-3-29
 *
 * @author liuxh
 * @since 1.0.0
 */
public class ChannelContext {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    public static final int INIT_THREAD_COUNT = Runtime.getRuntime().availableProcessors() * 2;
    public static final ScheduledExecutorService DEFAULT_SCHEDULED_EXECUTOR = new ScheduledThreadPoolExecutor(
            INIT_THREAD_COUNT,
            new ThreadFactory() {
                @Override
                public Thread newThread(Runnable r) {
                    Thread t = new Thread(r);
                    t.setName("channel-context-schedule-executor-thread-" + t.getId());
                    t.setDaemon(true);
                    return t;
                }
            });

    private SocketChannel channel;
    private SocketChannel linkChannel;
    private HeartBeatHandler heartBeatHandler;
    private IdleStateHandler idleStateHandler;
    private ScheduledExecutorService executor;

    public ChannelContext(SocketChannel channel, HeartBeatHandler heartBeatHandler,
            IdleStateHandler idleStateHandler) {
        this(channel, heartBeatHandler, idleStateHandler, DEFAULT_SCHEDULED_EXECUTOR);
    }

    public ChannelContext(SocketChannel channel, HeartBeatHandler heartBeatHandler,
            IdleStateHandler idleStateHandler, ScheduledExecutorService executor) {
        this.channel = channel;
        this.heartBeatHandler = heartBeatHandler;
        this.idleStateHandler = idleStateHandler;
        this.executor = executor;
    }

    public SocketChannel channel() {
        return this.channel;
    }

    public void channel(SocketChannel channel) {
        this.channel = channel;
    }

    public ScheduledExecutorService executor() {
        return this.executor;
    }

    public void executor(ScheduledExecutorService executor) {
        this.executor = executor;
    }

    public HeartBeatHandler heartBeatHandler() {
        return this.heartBeatHandler;
    }

    public void heartBeatHandler(HeartBeatHandler heartBeatHandler) {
        this.heartBeatHandler = heartBeatHandler;
    }

    public IdleStateHandler idleStateHandler() {
        return this.idleStateHandler;
    }

    public void idleStateHandler(IdleStateHandler idleStateHandler) {
        this.idleStateHandler = idleStateHandler;
    }

    public void setLinkChannel(SocketChannel linkChannel) {
        this.linkChannel = linkChannel;
    }

    public ChannelContext fireUserEventTriggered(final Object event) {
        try {
            heartBeatHandler.userEventTriggered(this, event);
        } catch (Throwable t) {
            logger.error("An exception was thrown by user handler while handling an user event", t);
        }
        return this;
    }

    public void writeAndFlush(SocketChannel channel, ByteBuffer buffer) {
        try {
            channel.write(buffer);
        } catch (Throwable t) {
            logger.error("An exception was thrown while writing data", t);
        }
    }

    public void writeAndFlush(ByteBuffer buffer) {
        try {
            this.channel.write(buffer);
        } catch (Throwable t) {
            logger.error("An exception was thrown while writing data", t);
        }
    }
}
