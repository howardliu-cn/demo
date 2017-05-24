package cn.howardliu.demo.nio.netty_nio.monitor.handler;

import cn.howardliu.demo.nio.netty_nio.monitor.struct.Header;
import cn.howardliu.demo.nio.netty_nio.monitor.struct.Message;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static cn.howardliu.demo.nio.netty_nio.monitor.struct.MessageType.*;

/**
 * <br>created at 17-5-11
 *
 * @author liuxh
 * @version 1.0.0
 * @since 1.0.0
 */
public class OtherInfoHandler extends SimpleChannelInboundHandler<Message> {
    private static final Logger logger = LoggerFactory.getLogger(OtherInfoHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Message msg) throws Exception {
        assert msg != null;
        assert msg.getHeader() != null;
        Header header = msg.getHeader();
        if (header.getType() == APP_INFO_REQ.value()) {
            if (logger.isDebugEnabled()) {
                logger.debug("receive appInfo request: {}", msg);
            }
        } else if (header.getType() == APP_INFO_RESP.value()) {
            if (logger.isDebugEnabled()) {
                logger.debug("receive appInfo response: {}", msg);
            }
        } else if (header.getType() == SQL_INFO_REQ.value()) {
            if (logger.isDebugEnabled()) {
                logger.debug("receive sqlInfo request: {}", msg);
            }
        } else if (header.getType() == SQL_INFO_RESP.value()) {
            if (logger.isDebugEnabled()) {
                logger.debug("receive sqlInfo response: {}", msg);
            }
        } else if (header.getType() == REQUEST_INFO_REQ.value()) {
            if (logger.isDebugEnabled()) {
                logger.debug("receive requestInfo request: {}", msg);
            }
        } else if (header.getType() == REQUEST_INFO_RESP.value()) {
            if (logger.isDebugEnabled()) {
                logger.debug("receive requestInfo response: {}", msg);
            }
        } else {
            ctx.fireChannelRead(msg);
        }
    }
}
