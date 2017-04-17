package cn.howardliu.demo.nio.netty_nio.protocol.http.json.client;

import cn.howardliu.demo.nio.netty_nio.protocol.http.json.codec.HttpJsonRequestEncoder;
import cn.howardliu.demo.nio.netty_nio.protocol.http.json.codec.HttpJsonResponseDecoder;
import cn.howardliu.demo.nio.netty_nio.protocol.http.json.pojo.Order;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestEncoder;
import io.netty.handler.codec.http.HttpResponseDecoder;

import java.net.InetSocketAddress;

/**
 * <br>created at 17-4-17
 *
 * @author liuxh
 * @version 1.0.0
 * @since 1.0.0
 */
public class HttpJsonClient {
    public void connect(int port) throws Exception {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline()
                                    .addLast("http-decoder", new HttpResponseDecoder())
                                    .addLast("http-aggregator", new HttpObjectAggregator(65536))
                                    .addLast("json-decoder", new HttpJsonResponseDecoder(Order.class, true))
                                    .addLast("http-encoder", new HttpRequestEncoder())
                                    .addLast("json-encoder", new HttpJsonRequestEncoder())
                                    .addLast("jsonClientHandler", new HttpJsonClientHandler());
                        }
                    });
            b.connect(new InetSocketAddress(port)).sync()
                    .channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception {
        int port = 8080;
        if (args != null && args.length > 0) {
            try {
                port = Integer.valueOf(args[0]);
            } catch (NumberFormatException ignored) {
            }
        }
        new HttpJsonClient().connect(port);
    }
}
