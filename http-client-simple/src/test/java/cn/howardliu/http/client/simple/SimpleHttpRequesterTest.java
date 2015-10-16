package cn.howardliu.http.client.simple;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertNotNull;

public class SimpleHttpRequesterTest {
    @Test
    public void testGet() throws Exception {
        assertNotNull(SimpleHttpRequester.getHttpRequester().get("http://www.baidu.com"));
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("wd", "faljfdlasjflkjasdlfjlasjflkajflkdajslkfjsa");
        assertNotNull(SimpleHttpRequester.getHttpRequester().get("http://www.baidu.com/s", paramsMap));
        List<NameValuePair> paramsList = new ArrayList<>();
        paramsList.add(new BasicNameValuePair("wd", "faljfdlasjflkjasdlfjlasjflkajflkdajslkfjsa"));
        assertNotNull(SimpleHttpRequester.getHttpRequester().get("http://www.baidu.com/s", paramsList));
    }

    @Test
    public void testPost() throws Exception {
        assertNotNull(SimpleHttpRequester.getHttpRequester()
                .post("http://10.6.2.48:8042/pcm-inner-sdc/organization/selectShop.htm", "{\"itemCode\":40000100}"));
    }
}