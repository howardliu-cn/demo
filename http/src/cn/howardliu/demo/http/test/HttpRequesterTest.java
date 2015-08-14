package cn.howardliu.demo.http.test;

import cn.howardliu.demo.http.HttpRequester;

/**
 * <p>create at 15-8-14</p>
 *
 * @author liuxh
 * @since 1.0.0
 */
public class HttpRequesterTest {
    public static void main(String[] args) throws Exception {
        String uri = "http://127.0.0.1:8080/monitor-service/app/read.json";
        String params = "{messageBody:{start=0, limit=1000}, caller:'admin'}";
        String result = HttpRequester.httpPostString(uri, params);
        System.out.println(result);
    }
}