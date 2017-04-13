package cn.howardliu.demo.thread;

import java.util.concurrent.TimeUnit;

/**
 * <br>created at 17-4-13
 *
 * @author liuxh
 * @version 1.0.0
 * @since 1.0.0
 */
public class StopThreadMain {
    public static void main(String[] args) throws InterruptedException {
        Runner runner = new Runner();
        Thread t = new Thread(runner);
        t.setName("T1");
        t.start();

        System.out.println(Thread.currentThread().getName() + " is stopping T1 thread");

        runner.stop();

        TimeUnit.MILLISECONDS.sleep(200);

        System.out.println(Thread.currentThread().getName() + " is finished now");
    }

    static class Runner implements Runnable {
        private volatile boolean exit = false;

        @Override
        public void run() {
            while (!exit) {
                System.out.println("Server is running...");
            }
            System.out.println("Server is stopped...");
        }

        public void stop() {
            this.exit = true;
        }
    }
}
