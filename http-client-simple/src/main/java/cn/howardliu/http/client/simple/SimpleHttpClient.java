package cn.howardliu.http.client.simple;

import org.apache.http.*;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <br/>create at 15-10-10
 *
 * @author liuxh
 * @since 1.0.0
 */
public class SimpleHttpClient {
    private static final Logger logger = LoggerFactory.getLogger(SimpleHttpClient.class);

    /**
     * GET方式请求url，url中已经包含请求参数或不需要参数。
     *
     * @param url 请求地址
     * @return url响应结果，java.lang.String类型。
     * @throws URISyntaxException 输入的url不合法
     * @throws IOException
     */
    public static String get(String url) throws URISyntaxException, IOException {
        return get(url, Collections.emptyList());
    }

    /**
     * GET方式请求url
     *
     * @param url    请求地址
     * @param params 请求参数
     * @return url响应结果，java.lang.String类型。
     * @throws URISyntaxException 输入的url不合法
     * @throws IOException
     */
    public static String get(String url, Map<String, String> params) throws URISyntaxException, IOException {
        List<NameValuePair> nameValuePairs = new ArrayList<>(params.size());
        nameValuePairs.addAll(params.entrySet().stream()
                .map(entry -> new BasicNameValuePair(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList()));
        return get(url, nameValuePairs);
    }

    /**
     * GET方式请求url
     *
     * @param url    请求地址
     * @param params 请求参数
     * @return url响应结果，java.lang.String类型。
     * @throws URISyntaxException 输入的url不合法
     * @throws IOException
     */
    public static String get(String url, List<NameValuePair> params) throws URISyntaxException, IOException {
        // 1. 验证输入url的有效性：url没有有效的host或url为相对路径，则url无效。
        URI uri = (new URIBuilder(url)).build();
        HttpHost httpHost = URIUtils.extractHost(uri);
        if (httpHost == null) {
            throw new IllegalArgumentException("缺少有效的HOST");
        }
        String respText = null;
        // 2. 创建HttpClient对象
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            // 3. 创建请求方法的实例，并指定请求URL。如果需要发送GET请求，创建HttpGet对象；如果需要发送POST请求，创建HttpPost对象。
            HttpGet httpGet = new HttpGet(uri);
            if (logger.isDebugEnabled()) {
                logger.debug("executing request: ", httpGet.getRequestLine());
            }
            // 4. 如果需要发送请求参数，可调用HttpGet、HttpPost共同的setParams(HttpParams params)方法来添加请求参数；
            //    对于HttpPost对象而言，也可调用setEntity(HttpEntity entity)方法来设置请求参数。
            if (params != null && params.size() > 0) {
                httpGet.setURI(new URI(httpGet.getURI().toString()
                        + "?" + EntityUtils.toString(new UrlEncodedFormEntity(params))));
            }
            // 5. 调用HttpClient对象的execute(HttpUriRequest request)发送请求，该方法返回一个HttpResponse。
            try (CloseableHttpResponse response = client.execute(httpHost, httpGet)) {
                // 6. 调用HttpResponse的getAllHeaders()、getHeaders(String name)等方法可获取服务器的响应头；
                //    调用HttpResponse的getEntity()方法可获取HttpEntity对象，该对象包装了服务器的响应内容，程序可通过该对象获取服务器的响应内容。
                Header[] headers = response.getAllHeaders();
                if (logger.isDebugEnabled()) {
                    Arrays.stream(headers).forEach(header ->
                            logger.debug("the headers of this message ：{} = {}", header.getName(), header.getValue()));
                }
                StatusLine statusLine = response.getStatusLine();
                if (logger.isDebugEnabled()) {
                    logger.debug("the status line of this response is {}", statusLine.getStatusCode());
                }
                if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
                    respText = EntityUtils.toString(response.getEntity(), HttpCharset.UTF8.getContent());
                    if (logger.isDebugEnabled()) {
                        logger.debug("the message entity of this response:\n\r" + respText);
                    }
                } else if (statusLine.getStatusCode() == HttpStatus.SC_MOVED_PERMANENTLY
                        || statusLine.getStatusCode() == HttpStatus.SC_MOVED_TEMPORARILY) {
                    return get(response.getLastHeader("Location").getValue());
                }
            }
        }
        return respText;
    }
}
