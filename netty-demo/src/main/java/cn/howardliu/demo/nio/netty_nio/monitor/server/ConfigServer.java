package cn.howardliu.demo.nio.netty_nio.monitor.server;

import cn.howardliu.demo.nio.netty_nio.monitor.codec.MessageDecoder;
import cn.howardliu.demo.nio.netty_nio.monitor.codec.MessageEncoder;
import cn.howardliu.demo.nio.netty_nio.monitor.struct.Header;
import cn.howardliu.demo.nio.netty_nio.monitor.struct.Message;
import cn.howardliu.demo.nio.netty_nio.monitor.struct.MessageType;
import cn.howardliu.demo.nio.netty_nio.monitor.struct.ServerInfo;
import com.alibaba.fastjson.JSON;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.ReadTimeoutHandler;

import java.util.Collections;

import static cn.howardliu.demo.nio.netty_nio.monitor.struct.ServerInfo.ServerType.LAN;

/**
 * <br>created at 17-5-22
 *
 * @author liuxh
 * @version 1.0.0
 * @since 1.0.0
 */
public class ConfigServer {
    public static void main(String[] args) throws InterruptedException {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            new ServerBootstrap()
                    .group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline()
                                    .addLast("MessageDecoder", new MessageDecoder(1024 * 1024 * 100, 4, 4))
                                    .addLast("MessageEncoder", new MessageEncoder())
                                    .addLast("read-timeout-handler", new ReadTimeoutHandler(50))
                                    .addLast(new SimpleChannelInboundHandler<Message>() {
                                        @Override
                                        protected void channelRead0(ChannelHandlerContext ctx, Message msg)
                                                throws Exception {
                                            if (msg != null && msg.getHeader() != null) {
                                                if (MessageType.CONFIG_REQ.value() == msg.getHeader().getType()) {
                                                    String list = JSON.toJSONString(
                                                            Collections.singletonList(
                                                                    new ServerInfo(
                                                                            "127.0.0.1",
                                                                            8082,
                                                                            LAN
                                                                    )
                                                            )
                                                    );
                                                    ctx.writeAndFlush(
                                                            new Message()
                                                                    .setHeader(
                                                                            new Header()
                                                                                    .setType(MessageType.CONFIG_RESP
                                                                                            .value())
                                                                                    .setLength(list.length()))
                                                                    .setBody(list)
                                                    );
                                                } else {
                                                    ctx.fireChannelRead(msg);
                                                }
                                            }
                                        }
                                    })
                            ;
                        }
                    })
                    .bind(8081).sync()
                    .channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
