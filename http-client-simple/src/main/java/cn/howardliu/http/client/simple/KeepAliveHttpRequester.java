package cn.howardliu.http.client.simple;

import org.apache.http.impl.client.CloseableHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <br/>create at 15-10-13
 *
 * @author liuxh
 * @since 1.0.0
 */
public class KeepAliveHttpRequester extends HttpRequester {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private KeepAliveHttpRequester() {
    }

    public static HttpRequester getHttpRequester() {
        return new KeepAliveHttpRequester();
    }

    @Override
    public CloseableHttpClient getHttpClient() {
//        PoolingHttpClientConnectionManager _cm = new PoolingHttpClientConnectionManager();
//        _cm.setMaxTotal(64);
//        _cm.setDefaultMaxPerRoute(8);
//        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create().useSystemProperties();
//        httpClientBuilder.setConnectionManager(_cm);
//        return httpClientBuilder.build();
        // TODO 实现长连接
        return null;
    }
}
