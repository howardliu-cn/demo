package cn.howardliu.http.client.simple;

import org.apache.http.impl.client.CloseableHttpClient;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

public class PoolingHttpRequesterTest {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Test
    public void testGetHttpClient() throws Exception {
        HttpRequester poolingHttpRequester = PoolingHttpRequester.getHttpRequester().setConnPoolMaxTotal(2).setConnPoolMaxPerRoute(1);
        int count = 100;
        Map<Integer, Integer> map = new HashMap<>(count, 1);
        CountDownLatch lock = new CountDownLatch(1);
        CountDownLatch countDownLatch = new CountDownLatch(count);
        for (int i = 0; i < count; i++) {
            new Thread(() -> {
                try {
                    lock.await();
                } catch (InterruptedException ignored) {
                }
                try(CloseableHttpClient client = poolingHttpRequester.getHttpClient()) {
                    if (map.containsKey(client.hashCode())) {
                        map.put(client.hashCode(), map.get(client.hashCode()) + 1);
                    } else {
                        map.put(client.hashCode(), 1);
                    }
                    logger.debug("生成的http client为：" + client.toString());
                } catch (IOException e) {
                    logger.error("IO管理失败", e);
                }
                countDownLatch.countDown();
            }).start();
        }
        lock.countDown();
        countDownLatch.await();
        for (Map.Entry entry : map.entrySet()) {
            logger.debug(entry.getKey() + "=" + entry.getValue());
        }
    }
}