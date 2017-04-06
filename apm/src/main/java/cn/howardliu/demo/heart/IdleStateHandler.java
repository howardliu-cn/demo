package cn.howardliu.demo.heart;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 * <br>created at 17-3-29
 *
 * @author liuxh
 * @since 1.0.0
 */
public class IdleStateHandler implements Closeable {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private static final long MIN_TIMEOUT_NANOS = TimeUnit.MILLISECONDS.toNanos(1);

    private final long readerIdleTimeNanos;
    private final long writerIdleTimeNanos;
    private final long allIdleTimeNanos;

    volatile ScheduledFuture<?> readerIdleTimeout;
    volatile long lastReadTime;
    private boolean firstReaderIdleEvent = true;

    volatile ScheduledFuture<?> writerIdleTimeout;
    volatile long lastWriteTime;
    private boolean firstWriterIdleEvent = true;

    volatile ScheduledFuture<?> allIdleTimeout;
    private boolean firstAllIdleEvent = true;

    private volatile int state = 0; // 0 - none, 1 - initialized, 2 - destroyed
    private volatile boolean reading;

    public IdleStateHandler(int readerIdleTimeSeconds, int writerIdleTimeSeconds, int allIdleTimeSeconds) {
        this(readerIdleTimeSeconds, writerIdleTimeSeconds, allIdleTimeSeconds, TimeUnit.SECONDS);
    }

    public IdleStateHandler(long readerIdleTime, long writerIdleTime, long allIdleTime, TimeUnit unit) {
        if (unit == null) {
            throw new NullPointerException("unit");
        }

        if (readerIdleTime <= 0) {
            readerIdleTimeNanos = 0;
        } else {
            readerIdleTimeNanos = Math.max(unit.toNanos(readerIdleTime), MIN_TIMEOUT_NANOS);
        }
        if (writerIdleTime <= 0) {
            writerIdleTimeNanos = 0;
        } else {
            writerIdleTimeNanos = Math.max(unit.toNanos(writerIdleTime), MIN_TIMEOUT_NANOS);
        }
        if (allIdleTime <= 0) {
            allIdleTimeNanos = 0;
        } else {
            allIdleTimeNanos = Math.max(unit.toNanos(allIdleTime), MIN_TIMEOUT_NANOS);
        }
    }

    public void handlerAdded(ChannelContext ctx) {
        if (ctx.channel().isConnected() && ctx.channel().isConnected() && ctx.channel().isRegistered()) {
            initialize(ctx);
        }
    }

    public void handlerRemoved() throws Exception {
        destroy();
    }

    public void channelRead() throws Exception {
        if (readerIdleTimeNanos > 0 || allIdleTimeNanos > 0) {
            reading = true;
            firstReaderIdleEvent = firstAllIdleEvent = true;
        }
    }

    public void channelReadComplete() throws Exception {
        if (readerIdleTimeNanos > 0 || allIdleTimeNanos > 0) {
            lastReadTime = System.nanoTime();
            reading = false;
        }
    }

    @Override
    public void close() throws IOException {
        destroy();
    }

    private void initialize(ChannelContext ctx) {
        switch (state) {
            case 1:
            case 2:
                return;
        }
        state = 1;

        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(3, new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread t = new Thread(r);
                t.setName("schedule-heart-bear-thread-" + t.getId());
                t.setDaemon(true);
                return t;
            }
        });

        lastReadTime = lastWriteTime = System.nanoTime();

        if (readerIdleTimeNanos > 0) {
            readerIdleTimeout = executor.schedule(
                    new ReaderIdleTimeoutTask(ctx),
                    readerIdleTimeNanos,
                    TimeUnit.NANOSECONDS
            );
        }

        if (writerIdleTimeNanos > 0) {
            writerIdleTimeout = executor.schedule(
                    new WriterIdleTimeoutTask(ctx),
                    writerIdleTimeNanos,
                    TimeUnit.NANOSECONDS
            );
        }

        if (allIdleTimeNanos > 0) {
            allIdleTimeout = executor.schedule(
                    new AllIdleTimeoutTask(ctx),
                    allIdleTimeNanos,
                    TimeUnit.NANOSECONDS
            );
        }
    }

    private void destroy() {
        state = 2;

        if (readerIdleTimeout != null) {
            readerIdleTimeout.cancel(false);
            readerIdleTimeout = null;
        }

        if (writerIdleTimeout != null) {
            writerIdleTimeout.cancel(false);
            writerIdleTimeout = null;
        }

        if (allIdleTimeout != null) {
            allIdleTimeout.cancel(false);
            allIdleTimeout = null;
        }
    }

    public long getReaderIdleTimeInMillis() {
        return TimeUnit.NANOSECONDS.toMillis(readerIdleTimeNanos);
    }

    public long getWriterIdleTimeInMillis() {
        return TimeUnit.NANOSECONDS.toMillis(writerIdleTimeNanos);
    }

    public long getAllIdleTimeInMillis() {
        return TimeUnit.NANOSECONDS.toMillis(allIdleTimeNanos);
    }

    private class ReaderIdleTimeoutTask extends IdleTimeTask {
        public ReaderIdleTimeoutTask(ChannelContext ctx) {
            super(ctx);
        }

        @Override
        public void run() {
            if (!this.ctx.channel().isOpen()) {
                return;
            }
            long nextDelay = readerIdleTimeNanos;
            if (!reading) {
                nextDelay -= System.nanoTime() - lastReadTime;
            }

            if (nextDelay <= 0) {
                allIdleTimeout = ctx.executor().schedule(this, allIdleTimeNanos, TimeUnit.NANOSECONDS);
                try {
                    IdleStateEvent event;
                    if (firstReaderIdleEvent) {
                        firstReaderIdleEvent = false;
                        event = IdleStateEvent.FIRST_READER_IDLE_STATE_EVENT;
                    } else {
                        event = IdleStateEvent.READER_IDLE_STATE_EVENT;
                    }
                    channelIdle(event);
                } catch (Throwable t) {
                    logger.error("An exception was thrown while handling idle event in reader task", t);
                }
            } else {
                readerIdleTimeout = ctx.executor().schedule(this, nextDelay, TimeUnit.NANOSECONDS);
            }
        }
    }

    private class WriterIdleTimeoutTask extends IdleTimeTask {
        public WriterIdleTimeoutTask(ChannelContext ctx) {
            super(ctx);
        }

        @Override
        public void run() {
            if (!ctx.channel().isOpen()) {
                return;
            }

            long lastWriteTime = IdleStateHandler.this.lastWriteTime;
            long nextDelay = writerIdleTimeNanos - (System.nanoTime() - lastWriteTime);
            if (nextDelay <= 0) {
                writerIdleTimeout = ctx.executor().schedule(this, writerIdleTimeNanos, TimeUnit.NANOSECONDS);
                try {
                    IdleStateEvent event;
                    if (firstWriterIdleEvent) {
                        firstWriterIdleEvent = false;
                        event = IdleStateEvent.FIRST_WRITER_IDLE_STATE_EVENT;
                    } else {
                        event = IdleStateEvent.WRITER_IDLE_STATE_EVENT;
                    }
                    channelIdle(event);
                } catch (Throwable t) {
                    logger.error("An exception was thrown while handling idle event in writer task", t);
                }
            } else {
                writerIdleTimeout = ctx.executor().schedule(this, nextDelay, TimeUnit.NANOSECONDS);
            }
        }
    }

    private class AllIdleTimeoutTask extends IdleTimeTask {
        public AllIdleTimeoutTask(ChannelContext ctx) {
            super(ctx);
        }

        @Override
        public void run() {
            if (!ctx.channel().isOpen()) {
                return;
            }

            long nextDelay = allIdleTimeNanos;
            if (!reading) {
                nextDelay -= System.nanoTime() - Math.max(lastReadTime, lastWriteTime);
            }
            if (nextDelay <= 0) {
                allIdleTimeout = ctx.executor().schedule(this, allIdleTimeNanos, TimeUnit.NANOSECONDS);
                try {
                    IdleStateEvent event;
                    if (firstAllIdleEvent) {
                        firstAllIdleEvent = false;
                        event = IdleStateEvent.FIRST_ALL_IDLE_STATE_EVENT;
                    } else {
                        event = IdleStateEvent.ALL_IDLE_STATE_EVENT;
                    }
                    channelIdle(event);
                } catch (Throwable t) {
                    logger.error("An exception was thrown while handling idle event in task", t);
                }
            } else {
                allIdleTimeout = ctx.executor().schedule(this, nextDelay, TimeUnit.NANOSECONDS);
            }
        }
    }
}
