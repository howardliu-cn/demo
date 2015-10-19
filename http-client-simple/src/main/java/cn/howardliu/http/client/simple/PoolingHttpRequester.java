package cn.howardliu.http.client.simple;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.ContentEncodingHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <br/>create at 15-10-13
 *
 * @author liuxh
 * @since 1.0.0
 */
public class PoolingHttpRequester extends HttpRequester {
    private static final Object lock = new Object();
    private static PoolingHttpRequester poolingHttpRequester;
    private static final PoolingHttpClientConnectionManager _cm = new PoolingHttpClientConnectionManager();

    static {
        _cm.setMaxTotal(64);
        _cm.setDefaultMaxPerRoute(8);
    }

    private PoolingHttpRequester() {
    }

    public static PoolingHttpRequester getHttpRequester() {
        if (poolingHttpRequester == null) {
            synchronized (lock) {
                if (poolingHttpRequester == null) {
                    poolingHttpRequester = new PoolingHttpRequester();
                }
            }
        }
        return poolingHttpRequester;
    }

    @Override
    CloseableHttpClient getHttpClient() {
        return HttpClients.custom().setConnectionManager(_cm).build();
    }

    public PoolingHttpRequester setConnPoolMaxTotal(int max) {
        _cm.setMaxTotal(max);
        return this;
    }

    public PoolingHttpRequester setConnPoolMaxPerRoute( int max) {
        _cm.setDefaultMaxPerRoute(max);
        return this;
    }
}
