package cn.howardliu.demo.http;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

/**
 * Http请求工具
 * <p>create at 15-7-10</p>
 *
 * @author liufl, liuxh
 * @since 1.0.0
 */
public abstract class HttpRequester {
    private static final HttpClientConnectionManager cm;

    static {
        PoolingHttpClientConnectionManager _cm = new PoolingHttpClientConnectionManager();
        _cm.setMaxTotal(200);
        _cm.setDefaultMaxPerRoute(50);
        cm = _cm;
    }

    /**
     * 使用Get方法请求返回字符串内容。
     *
     * @param uriWithParameters 带有请求参数的Get URL字符串，应包含主机地址
     * @return 返回的文本内容。
     * @throws java.io.IOException         如果网络IO异常或响应的字节长度过长（超过 {@link Integer#MAX_VALUE}）
     * @throws HttpRequestException        如果服务器返回非200 OK响应或无法按照参数进行请求（如未指定主机）
     * @throws java.net.URISyntaxException 如果参数不是合法URI
     */
    public static String httpGetString(String uriWithParameters) throws IOException, HttpRequestException,
            URISyntaxException {
        URIBuilder builder = new URIBuilder(uriWithParameters);
        URI uri = builder.build();
        HttpHost httpHost = URIUtils.extractHost(uri);
        if (httpHost == null) {
            throw new HttpRequestException("缺少有效的HOST");
        }
        HttpClient client = HttpClients.custom().setConnectionManager(cm).build();
        HttpGet httpGet = new HttpGet(uri);
        HttpResponse httpResponse = client.execute(httpHost, httpGet);
        StatusLine statusLine = httpResponse.getStatusLine();
        if (statusLine.getStatusCode() == 200) {
            InputStream contentStream = httpResponse.getEntity().getContent();
            long contentLength = httpResponse.getEntity().getContentLength();
            byte[] content;
            if (contentLength > 0) {
                if (contentLength > Integer.MAX_VALUE) {
                    throw new IOException("Http response too long");
                }
                content = new byte[(int) contentLength];
                int read = contentStream.read(content);
                assert read == content.length;
            } else {
                ByteArrayOutputStream byteBuff = new ByteArrayOutputStream();
                byte[] buff = new byte[512];
                int c;
                while ((c = contentStream.read(buff)) != -1) {
                    byteBuff.write(buff, 0, c);
                }
                byteBuff.close();
                content = byteBuff.toByteArray();
            }
            String responseString = new String(content);
            return responseString;
        } else {
            throw new HttpRequestException(statusLine.toString());
        }
    }

    /**
     * 使用Get方法请求返回字符串内容
     *
     * @param uri    URL字符串，应包含主机地址
     * @param params 参数。如有同名或多值参数，请将值使用“,”间隔后放入value
     * @return 返回的文本内容。
     * @throws java.net.URISyntaxException 如果参数不是合法URI
     * @throws java.io.IOException         如果网络IO异常或响应的字节长度过长（超过 {@link Integer#MAX_VALUE}）
     * @throws HttpRequestException        如果服务器返回非200 OK响应或无法按照参数进行请求（如未指定主机）
     */
    public static String httpGetString(String uri, Map<String, String> params)
            throws URISyntaxException, IOException, HttpRequestException {
        URIBuilder builder = new URIBuilder(uri);
        if (params != null) {
            for (String paramName : params.keySet()) {
                String paramValue = params.get(paramName);
                builder.addParameter(paramName, paramValue);
            }
        }
        String uriWithParameters = builder.build().toASCIIString();
        return httpGetString(uriWithParameters);
    }

    /**
     * 使用post方式请求返回字符串内容个
     *
     * @param uriStr     URL字符串，包含主机地址
     * @param parameters 参数内容。直接将参数写入POST请求的body中，非form方式请求。
     * @return 返回的文本内容
     * @throws java.io.IOException
     * @throws HttpRequestException
     * @throws java.net.URISyntaxException
     */
    public static String httpPostString(String uriStr, String parameters) throws IOException, HttpRequestException,
            URISyntaxException {
        URI uri = (new URIBuilder(uriStr)).build();// 构建uri
        HttpHost httpHost = URIUtils.extractHost(uri);// 构建host
        if (httpHost == null) {
            throw new HttpRequestException("缺少有效的HOST");
        }
        HttpClient client = HttpClients.custom().setConnectionManager(cm).build();
        HttpPost httpPost = new HttpPost(uri);
        httpPost.setEntity(new StringEntity(parameters));
        HttpResponse httpResponse = client.execute(httpHost, httpPost);
        StatusLine statusLine = httpResponse.getStatusLine();
        String body;
        if (statusLine.getStatusCode() == 200) {
            body = EntityUtils.toString(httpResponse.getEntity());
        } else {
            throw new HttpRequestException(statusLine.toString());
        }
        return body;
    }

}
