package cn.howardliu.http.client.simple;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertNotNull;

public class SimpleHttpClientTest {
    @Test
    public void testGet() throws Exception {
        assertNotNull(SimpleHttpClient.get("http://www.baidu.com"));
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("wd", "faljfdlasjflkjasdlfjlasjflkajflkdajslkfjsa");
        assertNotNull(SimpleHttpClient.get("http://www.baidu.com/s", paramsMap));
        List<NameValuePair> paramsList = new ArrayList<>();
        paramsList.add(new BasicNameValuePair("wd", "faljfdlasjflkjasdlfjlasjflkajflkdajslkfjsa"));
        assertNotNull(SimpleHttpClient.get("http://www.baidu.com/s", paramsList));
    }
}