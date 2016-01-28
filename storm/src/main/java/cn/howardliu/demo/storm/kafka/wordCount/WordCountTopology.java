package cn.howardliu.demo.storm.kafka.wordCount;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.spout.SchemeAsMultiScheme;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.tuple.Fields;
import backtype.storm.utils.Utils;
import cn.howardliu.demo.storm.kafka.BaseConfigConstants;
import cn.howardliu.demo.storm.kafka.MessageScheme;
import storm.kafka.*;
import storm.kafka.bolt.KafkaBolt;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * <br/>create at 16-1-19
 *
 * @author liuxh
 * @since 1.0.0
 */
public class WordCountTopology {
    private static final String KAFKA_SPOUT_ID = "sentenceKafkaSpout";
    private static final String SENTENCE_BOLT_ID = "sentenceBolt";
    private static final String SPLIT_BOLT_ID = "sentenceSplitBolt";
    private static final String WORD_COUNT_BOLT_ID = "sentenceWordCountBolt";
    private static final String REPORT_BOLT_ID = "sentenceReportBolt";
    private static final String KAFKA_BOLT_ID = "sentenceKafkabolt";
    private static final String CONSUME_TOPIC = "sentenceTopic";
    private static final String PRODUCT_TOPIC = "wordCountTopic";
    private static final String ZK_ROOT = "/storm/topology/root";
    private static final String ZK_ID = "wordCount";
    private static final String DEFAULT_TOPOLOGY_NAME = "sentenceWordCountKafka";

    public static void main(String[] args) throws Exception {
        // 配置Zookeeper地址
        BrokerHosts brokerHosts = new ZkHosts(BaseConfigConstants.ZK_SERVER, "/kafka/brokers");
        // 配置Kafka订阅的Topic，以及zookeeper中数据节点目录和名字
        SpoutConfig spoutConfig = new SpoutConfig(brokerHosts, CONSUME_TOPIC, ZK_ROOT, ZK_ID);
        spoutConfig.scheme = new SchemeAsMultiScheme(new MessageScheme());

        TopologyBuilder builder = new TopologyBuilder();
        builder.setSpout(KAFKA_SPOUT_ID, new KafkaSpout(spoutConfig));
        builder.setBolt(SENTENCE_BOLT_ID, new SentenceBolt()).shuffleGrouping(KAFKA_SPOUT_ID);
        builder.setBolt(SPLIT_BOLT_ID, new SplitSentenceBolt()).shuffleGrouping(KAFKA_SPOUT_ID);
        builder.setBolt(WORD_COUNT_BOLT_ID, new WordCountBolt()).fieldsGrouping(SPLIT_BOLT_ID, new Fields("word"));
        builder.setBolt(REPORT_BOLT_ID, new ReportBolt()).shuffleGrouping(WORD_COUNT_BOLT_ID);
        builder.setBolt(KAFKA_BOLT_ID, new KafkaBolt<String, Long>()).shuffleGrouping(REPORT_BOLT_ID);

        Config config = new Config();
        Map<String, String> map = new HashMap<>();
        map.put("metadata.broker.list", BaseConfigConstants.BROKER_SERVER);// 配置Kafka broker地址
        map.put("serializer.class", "kafka.serializer.StringEncoder");// serializer.class为消息的序列化类
        config.put("kafka.broker.properties", map);// 配置KafkaBolt中的kafka.broker.properties
        config.put("topic", PRODUCT_TOPIC);// 配置KafkaBolt生成的topic

        if (args.length == 0) {
            LocalCluster cluster = new LocalCluster();
            cluster.submitTopology(DEFAULT_TOPOLOGY_NAME, config, builder.createTopology());
//            Utils.sleep(100000);
//            cluster.killTopology(DEFAULT_TOPOLOGY_NAME);
//            cluster.shutdown();
        } else {
            config.setNumWorkers(1);
            StormSubmitter.submitTopology(args[0], config, builder.createTopology());
        }
    }
}
