package cn.howardliu.demo.heart;

/**
 * <br>created at 17-3-30
 *
 * @author liuxh
 * @since 1.0.0
 */
public interface HeartBeatHandler {
    void userEventTriggered(ChannelContext ctx, Object evt) throws Exception;
}
