package cn.howardliu.demo.heart;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * <br>created at 17-3-30
 *
 * @author liuxh
 * @since 1.0.0
 */
public class HeartBeatClient implements Closeable {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private SocketChannel channel;
    private Selector selector;
    private CustomHeartBeatHandler heartBeatHandler;
    private IdleStateHandler idleStateHandler;
    private InetSocketAddress remoteAddress;
    private ChannelContext ctx;
    int connectionDelay = 10;

    public HeartBeatClient(String ip, int port) throws Exception {
        this.remoteAddress = new InetSocketAddress(ip, port);
    }

    public void start() {
        connect();

        this.heartBeatHandler = new CustomHeartBeatHandler();
        this.idleStateHandler = new IdleStateHandler(0, 0, 5);
        this.ctx = new ChannelContext(this.channel, heartBeatHandler, idleStateHandler);
        this.idleStateHandler.handlerAdded(ctx);

        try {
            listen();
        } catch (Exception e) {
            this.ctx.executor().schedule(
                    new Runnable() {
                        @Override
                        public void run() {
                            try {
                                listen();
                            } catch (Exception ignored) {
                            }
                        }
                    },
                    connectionDelay,
                    TimeUnit.SECONDS
            );
        }
    }

    protected void connect() {
        try {
            this.channel = SocketChannel.open();
            this.channel.configureBlocking(false);
            this.selector = Selector.open();

            this.channel.connect(remoteAddress);
            this.channel.register(this.selector, SelectionKey.OP_CONNECT);
            if (channel.isConnectionPending()) {
                channel.finishConnect();
            }
        } catch (Exception e) {
            this.ctx.executor().schedule(
                    new Runnable() {
                        @Override
                        public void run() {
                            connect();
                        }
                    },
                    connectionDelay,
                    TimeUnit.SECONDS
            );
            logger.warn("Failed to connect to server, try connect after " + connectionDelay + "s");
        }
    }

    protected void listen() throws Exception {
        while(true) {
            if (selector.select(TimeUnit.SECONDS.toMillis(1)) == 0) {
                System.out.print(".");
                TimeUnit.SECONDS.sleep(1);
                continue;
            }
            Iterator<SelectionKey> it = this.selector.selectedKeys().iterator();
            while (it.hasNext()) {
                SelectionKey key = it.next();
                it.remove();
                if (key.isConnectable()) {
                    SocketChannel channel = (SocketChannel) key.channel();
                    if (channel.isConnectionPending()) {
                        channel.finishConnect();
                    }
                    channel.configureBlocking(false);
                    this.ctx.setLinkChannel(channel);
                    this.heartBeatHandler.ping(channel);
                    channel.register(this.selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
                } else if (key.isReadable()) {
                    SocketChannel channel = (SocketChannel) key.channel();
                    this.idleStateHandler.channelRead();
                    ByteBuffer buffer = ByteBuffer.allocate(1024);
                    channel.read(buffer);
                    this.heartBeatHandler.channelRead(this.ctx, buffer);
                    this.idleStateHandler.channelReadComplete();
                }
            }
        }
    }

    public void sendData() throws Exception {
        Random random = new Random(System.currentTimeMillis());
        for (int i = 0; i < 10000; i++) {
            if (channel != null && channel.isOpen() && channel.isConnected()) {
                String content = "client msg " + i;
                ByteBuffer buf = ByteBuffer.allocate(5 + content.getBytes().length);
                buf.putInt(5 + content.getBytes().length);
                buf.put((byte) 3);
                buf.put(content.getBytes());
                channel.write(buf);
            }
            Thread.sleep(random.nextInt(20000));
        }
    }

    @Override
    public void close() throws IOException {
        if (this.idleStateHandler != null) {
            this.idleStateHandler.close();
        }
        if (this.heartBeatHandler != null) {
            this.heartBeatHandler.close();
        }
        if (this.selector != null) {
            this.selector.close();
        }
        if (this.channel != null) {
            this.channel.close();
        }
    }
}
