package cn.howardliu.demo.nio.netty_nio.monitor.server;

import cn.howardliu.demo.nio.netty_nio.monitor.client.MonitorInfoClient;
import cn.howardliu.demo.nio.netty_nio.monitor.codec.MessageDecoder;
import cn.howardliu.demo.nio.netty_nio.monitor.codec.MessageEncoder;
import cn.howardliu.demo.nio.netty_nio.monitor.handler.HeartbeatHandler;
import cn.howardliu.demo.nio.netty_nio.monitor.handler.OtherInfoHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.ReadTimeoutHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <br>created at 17-5-22
 *
 * @author liuxh
 * @version 1.0.0
 * @since 1.0.0
 */
public class MonitorInfoServer {
    private static final Logger logger = LoggerFactory.getLogger(MonitorInfoClient.class);

    public static void main(String[] args) throws InterruptedException {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            new ServerBootstrap()
                    .group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 100)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline()
                                    .addLast("MessageDecoder", new MessageDecoder(1024 * 1024 * 100, 4, 4))
                                    .addLast("MessageEncoder", new MessageEncoder())
                                    .addLast("read-timeout-handler", new ReadTimeoutHandler(50))
                                    .addLast("", new HeartbeatHandler("test-server"))
                                    .addLast("OtherInfoHandler", new OtherInfoHandler());
                        }
                    })
                    .bind(8082).sync()
                    .channel().closeFuture().sync()
            ;
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
