package cn.howardliu.demo.curator.coordinator;

/**
 * 协调任务参数
 * <br/>create at 15-7-28
 *
 * @author liuxh
 * @since 1.0.0
 */
public interface CoordinatingTaskParam<P> {
    P get();

    String toPath();
}
