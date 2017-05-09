package cn.howardliu.demo.jdk8Time;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * <br>created at 17-5-2
 *
 * @author liuxh
 * @version 1.0.0
 * @since 1.0.0
 */
public class ShutdownHookMain {
    public static void main(String[] args) {
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            private volatile boolean hasShutdown = false;
            private AtomicInteger shutdownTimes = new AtomicInteger();

            @Override
            public void run() {
                synchronized (this) {
                    System.out.println(shutdownTimes.incrementAndGet());
                    if (!this.hasShutdown) {
                        this.hasShutdown = true;
                    }
                }
            }
        }, "shutdownHook"));
    }
}
