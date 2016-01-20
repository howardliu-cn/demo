package cn.howardliu.demo.storm.log;

import backtype.storm.Config;
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
        builder.setBolt("bolt", new WordCountTopology.SentenceBolt()).shuffleGrouping("spout");
        builder.setBolt("kafkabolt", new KafkaBolt()).shuffleGrouping("bolt");
        config.setNumWorkers(1);
        StormSubmitter.submitTopology(args[0], config, builder.createTopology());
    }

    public static class SentenceBolt extends BaseBasicBolt {
        @Override
        public void execute(Tuple input, BasicOutputCollector collector) {
            String word = (String) input.getValue(0);
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
