package cn.howardliu.demo.curator.coordinator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * <br/>create at 15-7-31
 *
 * @author liuxh
 * @since 1.0.0
 */
public abstract class CoordinatingTask<P, R> {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    protected final CuratorFramework client;// zk客户端
    protected final String taskNamespace;// 任务命名空间
    protected final String taskName;// 任务名
    protected final CoordinatingTaskParam<P> taskParam;// 任务参数
    protected final String instanceName;// 节点实例名
    private final static String STATUS_STANDBY = "STANDBY";
    private final static String STATUS_RUNNING = "RUNNING";
    private final static String STATUS_SUCCESS = "SUCCESS";
    private final static String STATUS_OVER = "OVER";
    private Duration taskDurationLimit = Duration.ofHours(1);// 超时限制
    private final CountDownLatch finishFlag = new CountDownLatch(1);// 结束标志
    private final String statusDomain;// 状态域
    private final InterProcessMutex statusNodeMutex;// 状态节点上的分布式锁
    private final NodeCache statusNodeCache;
    private final CoordinatingTask _$task;

    protected CoordinatingTask(CuratorFramework client, String taskNamespace, String taskName,
            CoordinatingTaskParam<P> taskParam, String instanceName, Duration taskDurationLimit) throws Exception {
        this.client = client;
        this.taskNamespace = taskNamespace;
        this.taskName = taskName;
        this.taskParam = taskParam;
        this.instanceName = instanceName;
        this.taskDurationLimit = taskDurationLimit;
        this.statusDomain = this.taskNamespace + "/" + this.taskName + "/" + this.taskParam.toPath() + "/status";
        this.statusNodeMutex = new InterProcessMutex(this.client, this.statusDomain);
        this._$task = this;

        statusNodeCache = new NodeCache(this.client, this.statusDomain);
        // 状态节点监听
        statusNodeCache.getListenable().addListener(() -> {
            String status = getStatus();
            if (STATUS_STANDBY.equals(status)) {
                logger.info("任务挂起，开始抢夺。");
                synchronized (this._$task) {
                    this._$task.notify();
                }
            } else if (STATUS_RUNNING.equals(status)) {
                logger.info("任务运行中。。");
            } else if (STATUS_SUCCESS.equals(status)) {
                logger.info("任务成功，结束抢夺。");
                synchronized (this._$task) {
                    this._$task.notify();
                }
                this.statusNodeCache.close();
            }
        });
        statusNodeCache.start(true);
    }

    public void run() throws Exception {
        ExecutorService executor = Executors.newCachedThreadPool();
        executor.execute(() -> {
            try {
                String status;
                while (!(status = this.getStatus()).equals(STATUS_SUCCESS)) {
                    synchronized (this._$task) {
                        if (STATUS_RUNNING.equals(status)) {
                            logger.info("状态为" + status + "，等待结果。。。");
                            // 正在运行，等待
                            this._$task.wait();
                        } else {
                            logger.info("状态为" + status + "，开始执行");
                            // 未开始运行，修改节点数据，执行
                            if (this.lockStatus()) {
                                logger.info("该节点开始执行任务。");
                                // 锁定成功，执行任务
                                try {
                                    this.doTask();
                                    this.collectResult();
                                    logger.info("运行结束");
                                    this.releaseStatus(true);
                                    logger.info("释放状态结束");
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    this.releaseStatus(false);
                                }
                            } else {
                                logger.info("有其他节点在运行，该节点挂起等待。");
                                this._$task.wait();
                            }
                        }
                    }
                }
                logger.info("运行成功，跳出循环等待。。。");
                this.finishTask();
                finishFlag.countDown();
                logger.info("countDown结束");
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        logger.info("等待。。。");
        finishFlag.await(this.taskDurationLimit.getSeconds(), TimeUnit.SECONDS);
        logger.info("等待结束，开始shutdown");
        executor.shutdown();
        logger.info("shutdown结束。");
    }

    protected abstract void doTask() throws Exception;

    protected abstract void collectResult();

    private boolean lockStatus() throws Exception {
        long id = System.currentTimeMillis();
        logger.info("编号：{}", id);
//        this.statusNodeMutex.acquire();
        try {
            String status = getStatus();
            logger.info("锁定状态：先获取状态为{}，然后进行操作。", status);
            if (STATUS_RUNNING.equals(status)) {
                return false;
            } else {
                this.client.setData().forPath(this.statusDomain, STATUS_RUNNING.getBytes());
                logger.info("当前状态为：{}", getStatus());
                return true;
            }
        } finally {
            try {
//                this.statusNodeMutex.release();
            } catch (Exception ignored) {
                ignored.printStackTrace();
            }
            logger.info("锁定状态锁结束");
            logger.info("编号：{}", id);
        }
    }

    private void releaseStatus(boolean isSuccess) throws Exception {
        logger.info("释放状态锁开始");
        long id = System.currentTimeMillis();
        logger.info("编号：{}", id);
//        statusNodeMutex.acquire();
        try {
            if (isSuccess) {
                this.client.setData().forPath(this.statusDomain, STATUS_SUCCESS.getBytes());
            } else {
                this.client.setData().forPath(this.statusDomain, STATUS_STANDBY.getBytes());
            }
        } finally {
//            statusNodeMutex.release();
            logger.info("释放状态锁结束");
            logger.info("编号：{}", id);
        }
    }

    private void finishTask() throws Exception {
        this.statusNodeCache.close();
        this.client.setData().forPath(this.statusDomain, STATUS_OVER.getBytes());
    }

    private String getStatus() throws Exception {
        long id = System.currentTimeMillis();
        logger.info("编号：{}", id);
//        this.statusNodeMutex.acquire();
        String status = new String(this.client.getData().forPath(this.statusDomain));
//        this.statusNodeMutex.release();
        logger.info("编号：{}", id);
        return status;
    }
}
