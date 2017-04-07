package cn.howardliu.demo.nio.java_nio;

/**
 * <br>created at 17-4-6
 *
 * @author liuxh
 * @since 1.0.0
 */
public class TimeServer {
    public static void main(String[] args) {
        int port = 8080;
        if (args != null && args.length > 0) {
            try {
                port = Integer.valueOf(args[0]);
            } catch (NumberFormatException ignore) {
            }
        }
        new Thread(new MultiplexerTimeServer(port), "NIO-MultiplexerTimerServer").start();
    }
}
