package cn.howardliu.demo.thread;

import java.util.concurrent.TimeUnit;

/**
 * <br>created at 17-4-13
 *
 * @author liuxh
 * @version 1.0.0
 * @since 1.0.0
 */
public class ThreadMain {
    public static void main(String[] args) throws InterruptedException {
        Thread t = new Thread(() -> {
            System.out.println("running...");
            try {
                TimeUnit.MILLISECONDS.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("stopped...");
        });

        TimeUnit.SECONDS.sleep(1);

        System.out.println(t.isAlive());
    }
}
