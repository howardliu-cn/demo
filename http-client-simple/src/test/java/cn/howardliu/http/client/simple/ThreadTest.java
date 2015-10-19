package cn.howardliu.http.client.simple;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HttpContext;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * <br/>create at 15-10-19
 *
 * @author liuxh
 * @since 1.0.0
 */
public class ThreadTest {
    static Map<Integer, Integer> map = new HashMap<>();
    @Test
    public void test9() throws InterruptedException, ExecutionException, IOException {
        PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager();
        connManager.setMaxTotal(200);//设置最大连接数200
        connManager.setDefaultMaxPerRoute(3);//设置每个路由默认连接数
        HttpHost host = new HttpHost("webservice.webxml.com.cn");//针对的主机
        connManager.setMaxPerRoute(new HttpRoute(host), 5);//每个路由器对每个服务器允许最大5个并发访问
        CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(connManager).build();
        String[] urisToGet = {
                "http://webservice.webxml.com.cn/WebServices/MobileCodeWS.asmx/getDatabaseInfo",
                "http://webservice.webxml.com.cn/WebServices/MobileCodeWS.asmx/getDatabaseInfo",
                "http://webservice.webxml.com.cn/WebServices/MobileCodeWS.asmx/getDatabaseInfo",
                "http://webservice.webxml.com.cn/WebServices/MobileCodeWS.asmx/getDatabaseInfo",
                "http://webservice.webxml.com.cn/WebServices/MobileCodeWS.asmx/getDatabaseInfo",
                "http://webservice.webxml.com.cn/WebServices/MobileCodeWS.asmx/getDatabaseInfo",
                "http://webservice.webxml.com.cn/WebServices/MobileCodeWS.asmx/getDatabaseInfo",
                "http://webservice.webxml.com.cn/WebServices/MobileCodeWS.asmx/getDatabaseInfo",
                "http://webservice.webxml.com.cn/WebServices/MobileCodeWS.asmx/getDatabaseInfo",
                "http://webservice.webxml.com.cn/WebServices/MobileCodeWS.asmx/getDatabaseInfo"
        };
        GetThread[] threads = new GetThread[urisToGet.length];
        for (int i = 0; i < threads.length; i++) {
            HttpGet httpget = new HttpGet(urisToGet[i]);
            threads[i] = new GetThread(httpClient, httpget);
        }
        for (GetThread thread : threads) {
            thread.start();
        }
        for (GetThread thread : threads) {
            thread.join();
        }
        for (Map.Entry entry : map.entrySet()) {
            System.out.println(entry.getKey() + "=" + entry.getValue());
        }
    }

    static class GetThread extends Thread {
        private final CloseableHttpClient httpClient;
        private final HttpContext context;
        private final HttpGet httpget;

        public GetThread(CloseableHttpClient httpClient, HttpGet httpget) {
            if (map.containsKey(httpClient.hashCode())) {
                map.put(httpClient.hashCode(), map.get(httpClient.hashCode()) + 1);
            } else {
                map.put(httpClient.hashCode(), 1);
            }
            this.httpClient = httpClient;
            this.context = HttpClientContext.create();
            this.httpget = httpget;
        }

        @Override
        public void run() {
            try {
                try (CloseableHttpResponse response = httpClient.execute(httpget, context)) {
                    HttpEntity entity = response.getEntity();
                    if (entity != null) {
                        System.out.println("test");
                    }
                }
            } catch (IOException ignored) {
            }
        }
    }
}
