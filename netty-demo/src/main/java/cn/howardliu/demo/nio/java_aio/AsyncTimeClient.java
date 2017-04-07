package cn.howardliu.demo.nio.java_aio;

/**
 * <br>created at 17-4-7
 *
 * @author liuxh
 * @version 1.0.0
 * @since 1.0.0
 */
public class AsyncTimeClient {
    public static void main(String[] args) {
        int port = 8080;
        if (args != null && args.length > 0) {
            try {
                port = Integer.valueOf(args[0]);
            } catch (NumberFormatException ignore) {
            }
        }
        new Thread(new AsyncTimeClientHandler("127.0.0.1", port), "AIO-TimeClient-001").start();
        new Thread(new AsyncTimeClientHandler("127.0.0.1", port), "AIO-TimeClient-002").start();
        new Thread(new AsyncTimeClientHandler("127.0.0.1", port), "AIO-TimeClient-003").start();
        new Thread(new AsyncTimeClientHandler("127.0.0.1", port), "AIO-TimeClient-004").start();
    }
}
