package cn.howardliu.demo.thread;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * <br>created at 17-4-13
 *
 * @author liuxh
 * @version 1.0.0
 * @since 1.0.0
 */
public class GeThreadByIdMain {
    private static final Map<Long, Thread> thread_collector = Collections.synchronizedMap(new HashMap<>());

    public static void main(String[] args) throws InterruptedException {
        Thread t = new Thread(() -> {
            while (true) {
                // do nothing
            }
        });
        t.setName("sleep-thread");
        t.setDaemon(true);
        t.start();
        long tid = t.getId();
        thread_collector.put(tid, t);

        TimeUnit.MILLISECONDS.sleep(200);

        Thread _t = getThread(tid);
        System.out.println("thread name: " + _t.getName());

        ThreadMXBean tmx = ManagementFactory.getThreadMXBean();
        ThreadInfo info = tmx.getThreadInfo(tid);
        System.out.println("thread name: " + info.getThreadName());

        Thread __t = thread_collector.get(tid);
        System.out.println("thread name: " + __t.getName());

        TimeUnit.MILLISECONDS.sleep(200);
    }

    public static Thread getThread(long tid) {
        ThreadGroup group = Thread.currentThread().getThreadGroup();
        while (group != null) {
            Thread[] threads = new Thread[(int)(group.activeCount() * 1.2)];
            int count = group.enumerate(threads, true);
            for(int i = 0; i < count; i++) {
                if(tid == threads[i].getId()) {
                    return threads[i];
                }
            }
            group = group.getParent();
        }
        return null;
    }
}
