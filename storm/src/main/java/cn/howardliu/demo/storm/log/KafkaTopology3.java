package cn.howardliu.demo.storm.log;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.spout.Scheme;
import backtype.storm.spout.SchemeAsMultiScheme;
import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;
import storm.kafka.KafkaSpout;
import storm.kafka.SpoutConfig;
import storm.kafka.ZkHosts;
import storm.kafka.bolt.KafkaBolt;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <br/>create at 16-1-18
 *
 * @author liuxh
 * @since 1.0.0
 */
public class KafkaTopology3 {
    public static void main(String[] args) throws Exception {
        // 配置Zookeeper地址
        ZkHosts brokerHosts = new ZkHosts("zk1:2181,zk2:2281,zk3:2381");
        // 配置Kafka订阅的Topic，以及zookeeper中数据节点目录和名字
        SpoutConfig spoutConfig = new SpoutConfig(brokerHosts, "topic1", "/zkkafkaspout", "kafkaspout");
        // 配置KafkaBolt中的kafka.broker.properties
        Config conf = new Config();
        Map<String, String> map = new HashMap<>();
        // 配置Kafka broker地址
        map.put("metadata.broker.list", "dev2_55.wfj-search:9092");
        // serializer.class为消息的序列化类
        map.put("serializer.class", "kafka.serializer.StringEncoder");
        conf.put("kafka.broker.properties", map);
        // 配置KafkaBolt生成的topic
        conf.put("topic", "topic2");
        spoutConfig.scheme = new SchemeAsMultiScheme(new KafkaTopology3.MessageScheme());
        TopologyBuilder builder = new TopologyBuilder();
        builder.setSpout("spout", new KafkaSpout(spoutConfig));
        builder.setBolt("bolt", new KafkaTopology3.SentenceBolt()).shuffleGrouping("spout");
        builder.setBolt("kafkabolt", new KafkaBolt()).shuffleGrouping("bolt");
        if(args.length == 0) {
            LocalCluster cluster = new LocalCluster();
            cluster.submitTopology("KafkaTopology3", conf, builder.createTopology());
//            Utils.sleep(100000L);
//            cluster.killTopology("KafkaTopology3");
//            cluster.shutdown();
        } else {
            conf.setNumWorkers(3);
            StormSubmitter.submitTopology(args[0], conf, builder.createTopology());
        }
    }

    public static class SentenceBolt extends BaseBasicBolt {
        @Override
        public void execute(Tuple input, BasicOutputCollector collector) {
            String word = (String)input.getValue(0);
            String out = "I\'m " + word + "!";
            System.out.println("out=" + out);
            collector.emit(new Values(out));
        }

        @Override
        public void declareOutputFields(OutputFieldsDeclarer declarer) {
            declarer.declare(new Fields("message"));
        }
    }

    public static class MessageScheme implements Scheme {
        @Override
        public List<Object> deserialize(byte[] ser) {
            try {
                String msg = new String(ser, "UTF-8");
                return new Values(msg);
            } catch (UnsupportedEncodingException ignored) {
                return null;
            }
        }

        @Override
        public Fields getOutputFields() {
            return new Fields("msg");
        }
    }
}
