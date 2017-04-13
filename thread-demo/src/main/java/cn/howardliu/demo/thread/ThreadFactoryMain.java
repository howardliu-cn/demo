package cn.howardliu.demo.thread;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 * <br>created at 17-4-13
 *
 * @author liuxh
 * @version 1.0.0
 * @since 1.0.0
 */
public class ThreadFactoryMain {
    public static void main(String[] args) throws InterruptedException {
        Thread t = new QueueThreadFactory("this-thread-name").newThread(() -> {
            System.out.println("running...");
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("stopped...");
        });
        TimeUnit.MILLISECONDS.sleep(200);
        System.out.println(t.getName());
        System.out.println(Thread.currentThread().getName());
    }

    static class QueueThreadFactory implements ThreadFactory {
        private String prefixName;

        public QueueThreadFactory(String prefixName) {
            this.prefixName = prefixName;
        }

        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(r);
            t.setDaemon(true);
            t.setName(this.prefixName + "-output-thread-" + t.getId());
            return t;
        }
    }
}
