package cn.howardliu.demo.nio.java_aio;

/**
 * <br>created at 17-4-7
 *
 * @author liuxh
 * @since 1.0.0
 */
public class AsyncTimeServer {
    public static void main(String[] args) {
        int port = 8080;
        if (args != null && args.length > 0) {
            try {
                port = Integer.valueOf(args[0]);
            } catch (NumberFormatException ignore) {
            }
        }
        new Thread(new AsyncTimeServerHandler(port), "AIO-TimerServer").start();
    }
}
