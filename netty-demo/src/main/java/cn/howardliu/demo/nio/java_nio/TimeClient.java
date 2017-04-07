package cn.howardliu.demo.nio.java_nio;

/**
 * <br>created at 17-4-7
 *
 * @author liuxh
 * @since 1.0.0
 */
public class TimeClient {
    public static void main(String[] args) {
        int port = 8080;
        if (args != null && args.length > 0) {
            try {
                port = Integer.valueOf(args[0]);
            } catch (NumberFormatException ignore) {
            }
        }
        new Thread(new TimeClientHandle("127.0.0.1", port), "NIO-TimeClient-001").start();
        new Thread(new TimeClientHandle("127.0.0.1", port), "NIO-TimeClient-002").start();
        new Thread(new TimeClientHandle("127.0.0.1", port), "NIO-TimeClient-003").start();
        new Thread(new TimeClientHandle("127.0.0.1", port), "NIO-TimeClient-004").start();
    }
}
