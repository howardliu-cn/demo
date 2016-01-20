package cn.howardliu.demo.storm.wordCount.newer;

import backtype.storm.Config;
import backtype.storm.StormSubmitter;
import backtype.storm.spout.SchemeAsMultiScheme;
import backtype.storm.topology.TopologyBuilder;
import storm.kafka.KafkaSpout;
import storm.kafka.SpoutConfig;
import storm.kafka.ZkHosts;
import storm.kafka.bolt.KafkaBolt;

import java.util.HashMap;
import java.util.Map;

/**
 * <br/>create at 16-1-20
 *
 * @author liuxh
 * @since 1.0.0
 */
public class WordCountTopology {
    public static void main(String[] args) throws Exception {
        ZkHosts zkHosts = new ZkHosts("zk1:2181,zk2:2281,zk3:2381");
        SpoutConfig spoutConfig = new SpoutConfig(zkHosts, "topic1", "/zkkafkaspout", "kafkaspout");
        Config config = new Config();
        Map<String, String> map = new HashMap<>();
        map.put("metadata.broker.list", "dev2_55.wfj-search:9092");
        map.put("serializer.class", "kafka.serializer.StringEncoder");
        config.put("kafka.broker.properties", map);
        config.put("topic", "topic2");
        spoutConfig.scheme = new SchemeAsMultiScheme(new MessageScheme());
        TopologyBuilder builder = new TopologyBuilder();
        builder.setSpout("spout", new KafkaSpout(spoutConfig));
        builder.setBolt("bolt", new SentenceBolt()).shuffleGrouping("spout");
        builder.setBolt("kafkabolt", new KafkaBolt()).shuffleGrouping("bolt");
        config.setNumWorkers(1);
        StormSubmitter.submitTopology(args[0], config, builder.createTopology());
    }
}
