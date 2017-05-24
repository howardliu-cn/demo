package cn.howardliu.demo.nio.netty_nio.monitor.codec;

import cn.howardliu.demo.nio.netty_nio.monitor.struct.Header;
import cn.howardliu.demo.nio.netty_nio.monitor.struct.Message;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <br>created at 17-5-11
 *
 * @author liuxh
 * @version 1.0.0
 * @since 1.0.0
 */
public class MessageDecoder extends LengthFieldBasedFrameDecoder {
    private static final Logger logger = LoggerFactory.getLogger(MessageDecoder.class);

    public MessageDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength) {
        super(maxFrameLength, lengthFieldOffset, lengthFieldLength);
    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        ByteBuf frame = (ByteBuf) super.decode(ctx, in);
        if (frame == null) {
            return null;
        }
        Message message = new Message()
                .setHeader(new Header()
                        .setCrcCode(frame.readInt())
                        .setLength(frame.readInt())
                        .setType(frame.readByte())
                        .setTag(frame.readCharSequence(frame.readInt(), CharsetUtil.UTF_8).toString()));
        if (frame.readableBytes() > 4) {
            message.setBody(frame.readCharSequence(frame.readInt(), CharsetUtil.UTF_8).toString());
        }
        return message;
    }
}
