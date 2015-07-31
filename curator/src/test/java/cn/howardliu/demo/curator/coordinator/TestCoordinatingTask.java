package cn.howardliu.demo.curator.coordinator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * <br/>create at 15-7-30
 *
 * @author liuxh
 * @since 1.0.0
 */
public class TestCoordinatingTask {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private static CuratorFramework client;

    @BeforeClass
    public static void setUpBeforeClass() {
        // 127.0.0.1:2181,127.0.0.1:2182,127.0.0.1:2183
        client = CuratorFrameworkFactory.builder().connectString("127.0.0.1:2181")
                .sessionTimeoutMs(15000).retryPolicy(new ExponentialBackoffRetry(1000, 3)).build();
        client.start();
    }

    @Test
    public void testMyTask() {
        int count = 2;
        CountDownLatch countDown = new CountDownLatch(count);
        CountDownLatch switchCount = new CountDownLatch(1);
        for (int i = 0; i < count; i++) {
            Thread thread = new Thread(() -> {
                try {
                    switchCount.await();
                } catch (InterruptedException ignored) {
                }
                try {
                    CoordinatingTestTask task = new CoordinatingTestTask(client, "/com/wfj/coordinator/test/operation",
                            new CoordinatingTestTaskParam(System.currentTimeMillis() + ""), "testInstanceName",
                            Duration.ofSeconds(60));
                    task.run();
                } catch (Exception ignored) {
                    ignored.printStackTrace();
                }
                countDown.countDown();
            }, "test-coordinator-thread-" + i);
            thread.start();
        }
        switchCount.countDown();
        try {
            countDown.await();
        } catch (InterruptedException ignored) {
        }
    }
}

class CoordinatingTestTask extends CoordinatingTask<String, Void> {
    private Logger logger = LoggerFactory.getLogger(getClass());

    protected CoordinatingTestTask(CuratorFramework client, String taskNamespace,
            CoordinatingTaskParam<String> taskParam, String instanceName, Duration taskDurationLimit) throws Exception {
        super(client, taskNamespace, "test-coordinator-task", taskParam, instanceName, taskDurationLimit);
    }

    @Override
    protected void doTask() throws Exception {
        String threadName = Thread.currentThread().getName();
        TimeUnit.SECONDS.sleep(5);
        try {
//            if (System.currentTimeMillis() % 2 == 0) {
//                throw new RuntimeException("就是让你出错");
//            }
            logger.info("[" + threadName + "]任务结束.");
        } catch (Exception e) {
            logger.error("[" + threadName + "]任务异常----------------");
            throw e;
        }
    }

    @Override
    protected void collectResult() {
    }
}

class CoordinatingTestTaskParam implements CoordinatingTaskParam<String> {
    private final String string;

    public CoordinatingTestTaskParam(String string) {
        this.string = string;
    }

    @Override
    public String get() {
        return string;
    }

    @Override
    public String toPath() {
        return "default";
    }
}