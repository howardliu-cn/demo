package cn.howardliu.demo.es.transport;

import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * <br/>create at 16-2-2
 *
 * @author liuxh
 * @since 1.0.0
 */
public class TransportClientMain {
    public static void main(String[] args) throws UnknownHostException {
        Settings settings = Settings.settingsBuilder().put("cluster.name", "development").build();
        Client client = TransportClient.builder().settings(settings).build()
                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("10.6.2.151"), 9300));

        Map<String, Object> json = new HashMap<>();
        json.put("user", "kimchy");
        json.put("postDate", new Date());
        json.put("message", "trying out Elasticsearch");

        IndexResponse response = client
                .prepareIndex("demo-twitter", "tweet", "1")
                .setSource(json)
                .get();

        System.out.println(response);
        System.out.println("index name is " + response.getIndex());
        System.out.println("type name is " + response.getType());
        System.out.println("document id (generated or not) is " + response.getId());
        System.out.println("version is " + response.getVersion()
                + "(if it's the first time you index this document, you will get: 1) ");
        System.out.println(
                "isCreated() is true if the document is a new one, false if it has bean updated, the value is "
                        + response.isCreated());

        GetResponse getResponse = client.prepareGet("demo-twitter", "tweet", "1").get();
        System.out.println("index name is " + getResponse.getIndex());
        System.out.println("type name is " + getResponse.getType());
        System.out.println("document id is " + getResponse.getId());
        System.out.println("version is " + getResponse.getVersion());
        System.out.println("the source of the document (as a string) is " + getResponse.getSourceAsString());
    }
}
