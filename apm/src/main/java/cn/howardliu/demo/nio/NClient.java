package cn.howardliu.demo.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * <br>created at 17-3-29
 *
 * @author liuxh
 * @since 1.0.0
 */
public class NClient {
    private Selector selector;

    public void initClient(String ip, int port) throws IOException {
        SocketChannel channel = SocketChannel.open();
        channel.configureBlocking(false);
        this.selector = Selector.open();
        channel.connect(new InetSocketAddress(ip, port));
        channel.register(this.selector, SelectionKey.OP_CONNECT | SelectionKey.OP_READ | SelectionKey.OP_WRITE);
    }

    public void listen() throws IOException {
        while (this.selector.select() > 0) {
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
                    String content = "Client " + 1 + "," + System.currentTimeMillis();
                    channel.write(ByteBuffer.wrap(content.getBytes()));
                    channel.register(this.selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
                } else if (key.isReadable()) {
                    SocketChannel channel = (SocketChannel) key.channel();
                    ByteBuffer buffer = ByteBuffer.allocate(1024);
                    channel.read(buffer);
                    System.out.println(new String(buffer.array()));
                } else if(key.isAcceptable()) {
                    System.out.println("isAcceptable");
                } else if(key.isValid()) {
                    System.out.println("isValid");
                } else if(key.isWritable()) {
                    System.out.println("isWritable");
                }
            }
        }
    }

    public static void main(String[] args) throws IOException {
        NClient client = new NClient();
        client.initClient("127.0.0.1", 12345);
        client.listen();
    }
}
