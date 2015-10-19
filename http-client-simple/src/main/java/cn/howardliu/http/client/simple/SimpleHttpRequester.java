package cn.howardliu.http.client.simple;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <br/>create at 15-10-10
 *
 * @author liuxh
 * @since 1.0.0
 */
public class SimpleHttpRequester extends HttpRequester {
    private static final Logger logger = LoggerFactory.getLogger(SimpleHttpRequester.class);

    private SimpleHttpRequester() {
    }

    public static SimpleHttpRequester getHttpRequester() {
        return new SimpleHttpRequester();
    }

    @Override
    CloseableHttpClient getHttpClient() {
        return HttpClients.createDefault();
    }
}
