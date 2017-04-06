package cn.howardliu.demo.heart;

/**
 * <br>created at 17-3-30
 *
 * @author liuxh
 * @since 1.0.0
 */
public abstract class IdleTimeTask implements Runnable {
    protected ChannelContext ctx;

    public IdleTimeTask(ChannelContext ctx) {
        this.ctx = ctx;
    }

    protected void channelIdle(IdleStateEvent event) {
        ctx.fireUserEventTriggered(event);
    }
}
