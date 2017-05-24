package cn.howardliu.demo.nio.netty_nio.monitor.codec;

import cn.howardliu.demo.nio.netty_nio.monitor.struct.Message;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.util.CharsetUtil;
import org.apache.commons.lang3.text.StrBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <br>created at 17-5-11
 *
 * @author liuxh
 * @version 1.0.0
 * @since 1.0.0
 */
public class MessageEncoder extends MessageToByteEncoder<Message> {
    private static final Logger logger = LoggerFactory.getLogger(MessageEncoder.class);

    @Override
    protected void encode(ChannelHandlerContext ctx, Message msg, ByteBuf out) throws Exception {
        if (msg == null || msg.getHeader() == null) {
            throw new IllegalArgumentException("the encode message is null.");
        }
        out.writeInt(msg.getHeader().getCrcCode());
        out.writeInt(msg.getHeader().getLength());
        out.writeByte(msg.getHeader().getType());
        out.writeInt(msg.getHeader().getTag().length());
        out.writeCharSequence(new StrBuilder(msg.getHeader().getTag()), CharsetUtil.UTF_8);
        if (msg.getBody() == null) {
            out.writeInt(0);
        } else {
            out.writeInt(msg.getBody().length());
            out.writeCharSequence(new StrBuilder(msg.getBody()), CharsetUtil.UTF_8);
        }
        out.setInt(4, out.readableBytes() - 8);
    }
}
