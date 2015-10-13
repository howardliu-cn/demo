package cn.howardliu.http.client.simple;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.*;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.entity.StringEntity;
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
                        + "?" + EntityUtils.toString(new UrlEncodedFormEntity(params, Consts.UTF_8))));
            }
            // 5. 调用HttpClient对象的execute(HttpUriRequest request)发送请求，该方法返回一个HttpResponse。
            respText = execute(client, httpHost, httpGet);
        }
        return respText;
    }

    /**
     * POST方式请求url
     *
     * @param url 请求地址
     * @return url响应结果，java.lang.String类型。
     * @throws URISyntaxException 输入的url不合法
     * @throws IOException
     */
    public static String post(String url) throws URISyntaxException, IOException {
        return get(url);
    }

    /**
     * POST方式请求url
     *
     * @param url         请求地址
     * @param postMessage 请求体，直接将参数写入POST请求的body中。
     * @return url响应结果，java.lang.String类型。
     * @throws URISyntaxException 输入的url不合法
     * @throws IOException
     */
    public static String post(String url, String postMessage) throws URISyntaxException, IOException {
        HttpEntity httpEntity = null;
        if (StringUtils.isNotEmpty(postMessage)) {
            httpEntity = new StringEntity(postMessage, Consts.UTF_8);
        }
        return post(url, httpEntity);
    }

    /**
     * POST方式请求url
     *
     * @param url        请求地址
     * @param httpEntity 请求实体，包含http请求所需参数。
     * @return url响应结果，java.lang.String类型。
     * @throws URISyntaxException 输入的url不合法
     * @throws IOException
     */
    public static String post(String url, HttpEntity httpEntity) throws URISyntaxException, IOException {
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
            HttpPost httpPost = new HttpPost(uri);
            if (logger.isDebugEnabled()) {
                logger.debug("executing request: ", httpPost.getRequestLine());
            }
            // 4. 如果需要发送请求参数，可调用HttpGet、HttpPost共同的setParams(HttpParams params)方法来添加请求参数；
            //    对于HttpPost对象而言，也可调用setEntity(HttpEntity entity)方法来设置请求参数。
            if (httpEntity != null) {
                httpPost.setEntity(httpEntity);
            }
            // 5. 调用HttpClient对象的execute(HttpUriRequest request)发送请求，该方法返回一个HttpResponse。
            respText = execute(client, httpHost, httpPost);
        }
        return respText;
    }

    /**
     * 通过client，以httpRequest为请求参数，调用httpHost，并解析响应，返回{@code java.lang.String} 类型的响应内容。
     *
     * @param client      HTTP请求客户端
     * @param httpHost    包含HTTP连接所需参数（远端Host名、端口、scheme）的对象
     * @param httpRequest 包含从客户端到服务端的请求信息
     * @return url响应结果
     * @throws IOException
     * @throws URISyntaxException 如果返回为301或302跳转，则会调用GET请求，如果返回的url不正确，会抛出该异常。
     */
    private static String execute(CloseableHttpClient client, HttpHost httpHost, HttpRequest httpRequest)
            throws IOException, URISyntaxException {
        String respText = null;
        try (CloseableHttpResponse response = client.execute(httpHost, httpRequest)) {
            // 调用HttpResponse的getAllHeaders()、getHeaders(String name)等方法可获取服务器的响应头；
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
                // 调用HttpResponse的getEntity()方法可获取HttpEntity对象，该对象包装了服务器的响应内容。
                // EntityUtils.toString获取response返回的content，首先获取响应头的字符集，如果响应头中无定义，使用UTF-8字符集。
                respText = EntityUtils.toString(response.getEntity(), Consts.UTF_8);
                if (logger.isDebugEnabled()) {
                    logger.debug("the message entity of this response:\n\r" + respText);
                }
            } else if (statusLine.getStatusCode() == HttpStatus.SC_MOVED_PERMANENTLY
                    || statusLine.getStatusCode() == HttpStatus.SC_MOVED_TEMPORARILY) {
                return get(response.getLastHeader(HttpHeaders.LOCATION).getValue());
            }
        }
        return respText;
    }
}
