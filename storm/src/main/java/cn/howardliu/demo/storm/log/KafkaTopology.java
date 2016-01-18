package cn.howardliu.demo.storm.log;

import backtype.storm.spout.SchemeAsMultiScheme;
import backtype.storm.tuple.Fields;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import storm.kafka.*;
import storm.kafka.trident.OpaqueTridentKafkaSpout;
import storm.kafka.trident.TridentKafkaConfig;
import storm.trident.Stream;
import storm.trident.TridentTopology;

/**
 * <br/>create at 16-1-18
 *
 * @author liuxh
 * @since 1.0.0
 */
public class KafkaTopology {
    private static final Logger logger = LoggerFactory.getLogger(KafkaTopology.class);

    public static void main(String[] args) {
        TridentTopology topology = new TridentTopology();
        BrokerHosts zk = new ZkHosts("10.6.2.55:12181");
        TridentKafkaConfig spoutConf = new TridentKafkaConfig(zk, "test-sentence-topic");
        spoutConf.scheme = new SchemeAsMultiScheme(new StringScheme());
        OpaqueTridentKafkaSpout spout = new OpaqueTridentKafkaSpout(spoutConf);
        Stream spoutStream = topology.newStream("kafka-stream", spout);
        Fields sentenceFields = new Fields("sentence");
    }
}
