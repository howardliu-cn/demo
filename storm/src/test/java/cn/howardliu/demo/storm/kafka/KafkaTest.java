package cn.howardliu.demo.storm.kafka;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;
import org.apache.storm.shade.org.json.simple.JSONValue;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class KafkaTest {
    @Test
    public void test() {
        String brokerList = "10.6.2.56:9092,10.6.2.57:9092,10.6.2.58:9092";
        String loggingTopic = "log-analysis";
        Properties configs = new Properties();
        configs.setProperty("metadata.broker.list", brokerList);
        configs.setProperty("serializer.class", "kafka.serializer.StringEncoder");
        Producer<Integer, String> producer = new Producer<>(new ProducerConfig(configs));
        Map<String, Object> map = new HashMap<>();
        map.put("level", "00");
        map.put("timestamp", "1454040421630");
        producer.send(new KeyedMessage<Integer, String>(loggingTopic, null, 0, JSONValue.toJSONString(map)));
    }
}