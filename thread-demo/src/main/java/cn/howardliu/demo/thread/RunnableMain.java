package cn.howardliu.demo.thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * <br>created at 17-4-13
 *
 * @author liuxh
 * @version 1.0.0
 * @since 1.0.0
 */
public class RunnableMain {
    public static void main(String[] args) throws InterruptedException {
        Thread t = new Thread(new QueueRunner("this-thread-name") {
            @Override
            public void run() {
                Thread t = Thread.currentThread();
                t.setName(this.prefixName + "-runner-" + t.getId());
                System.out.println("running...");
                try {
                    TimeUnit.SECONDS.sleep(2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("stopped...");
            }
        });
        t.setDaemon(true);
        t.start();

        TimeUnit.MILLISECONDS.sleep(200);

        System.out.println(t.getName());
        System.out.println(Thread.currentThread().getName());
    }

    static abstract class QueueRunner implements Runnable{
        protected String prefixName;

        public QueueRunner(String prefixName) {
            this.prefixName = prefixName;
        }
    }
}
