package cn.howardliu.demo.storm.log;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.spout.SchemeAsMultiScheme;
import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Tuple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import storm.kafka.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * <br/>create at 16-1-18
 *
 * @author liuxh
 * @since 1.0.0
 */
public class KafkaTopology2 {
    private static final Logger logger = LoggerFactory.getLogger(KafkaTopology2.class);

    public static void main(String[] args) {
        String topic = "test";
        String offsetZkServers = "10.6.2.55";
        String offsetZkPort = "2181";
        String offsetZkRoot = "/stormExample";
        String offsetZkId = "storm-example";
        BrokerHosts brokerHosts = new ZkHosts("10.6.2.55:2181");

        SpoutConfig spoutConfig = new SpoutConfig(brokerHosts, topic, offsetZkRoot, offsetZkId);
        spoutConfig.zkRoot = offsetZkRoot;
        spoutConfig.zkPort = Integer.parseInt(offsetZkPort);
        spoutConfig.zkServers = Arrays.asList(offsetZkServers);
        spoutConfig.id = offsetZkId;
        spoutConfig.scheme = new SchemeAsMultiScheme(new StringScheme());

        KafkaSpout spout = new KafkaSpout(spoutConfig);
        TopologyBuilder builder = new TopologyBuilder();
        builder.setSpout("spout", spout, 1);
        builder.setBolt("bolt", new BaseRichBolt() {
            private Logger logger = LoggerFactory.getLogger(getClass());

            @Override
            public void prepare(Map map, TopologyContext topologyContext, OutputCollector outputCollector) {
            }

            @Override
            public void execute(Tuple tuple) {
                List<Object> list = tuple.getValues();
                logger.info("the data from spout is {}", list);
            }

            @Override
            public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
            }
        }, 1).shuffleGrouping("spout");

        Config config = new Config();
        LocalCluster cluster = new LocalCluster();
        cluster.submitTopology("test-kafka", config, builder.createTopology());
    }
}
