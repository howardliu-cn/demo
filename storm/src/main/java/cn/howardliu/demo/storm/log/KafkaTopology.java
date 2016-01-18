package cn.howardliu.demo.storm.log;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.generated.AlreadyAliveException;
import backtype.storm.generated.AuthorizationException;
import backtype.storm.generated.InvalidTopologyException;
import backtype.storm.generated.StormTopology;
import backtype.storm.spout.SchemeAsMultiScheme;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import storm.kafka.*;
import storm.kafka.bolt.KafkaBolt;
import storm.kafka.trident.*;
import storm.kafka.trident.mapper.FieldNameBasedTupleToKafkaMapper;
import storm.kafka.trident.selector.DefaultTopicSelector;
import storm.kafka.trident.selector.KafkaTopicSelector;
import storm.trident.Stream;
import storm.trident.TridentTopology;
import storm.trident.testing.FixedBatchSpout;

import java.util.Properties;
import java.util.concurrent.TimeUnit;

/**
 * <br/>create at 16-1-18
 *
 * @author liuxh
 * @since 1.0.0
 */
public class KafkaTopology {
    private static final Logger logger = LoggerFactory.getLogger(KafkaTopology.class);

    public static StormTopology buildTopology() {
        TridentTopology topology = new TridentTopology();
        BrokerHosts zk = new ZkHosts("10.6.2.55:2181");
        TridentKafkaConfig spoutConf = new TridentKafkaConfig(zk, "test-sentence-topic");
        spoutConf.scheme = new SchemeAsMultiScheme(new StringScheme());
        spoutConf.startOffsetTime = kafka.api.OffsetRequest.EarliestTime();// read from the beginning of the topic
        spoutConf.startOffsetTime = kafka.api.OffsetRequest.LatestTime();// read from the end of the topic

//        OpaqueTridentKafkaSpout spout = new OpaqueTridentKafkaSpout(spoutConf);
//
//        Stream spoutStream = topology.newStream("kafka-stream", spout);
//        Fields jsonFields = new Fields("level", "timestamp", "message", "logger");
//        Stream parsedStream = spoutStream.each(new Fields("str"), new JsonProjectFunction(jsonFields), jsonFields);
//        parsedStream = parsedStream.project(jsonFields);

        return topology.build();
    }

    public static void main(String[] args) throws Exception {
        BrokerHosts zk = new ZkHosts("10.6.2.55:2181");
        TridentKafkaConfig spoutConf = new TridentKafkaConfig(zk, "test-sentence-topic");
        spoutConf.scheme = new SchemeAsMultiScheme(new StringScheme());
        spoutConf.startOffsetTime = kafka.api.OffsetRequest.EarliestTime();// read from the beginning of the topic
        spoutConf.startOffsetTime = kafka.api.OffsetRequest.LatestTime();// read from the end of the topic

        Fields fields = new Fields("word", "count");
        FixedBatchSpout spout = new FixedBatchSpout(fields, 4,
                new Values("storm", "1"),
                new Values("trident", "1"),
                new Values("needs", "1"),
                new Values("javadoc", "1")
        );
        spout.setCycle(true);

        TridentTopology topology = new TridentTopology();
        Stream stream = topology.newStream("spout", spout);
        TridentKafkaStateFactory stateFactory = new TridentKafkaStateFactory()
                .withKafkaTopicSelector(new DefaultTopicSelector("test"))
                .withTridentTupleToKafkaMapper(new FieldNameBasedTupleToKafkaMapper("word", "count"));
        stream.partitionPersist(stateFactory, fields, new TridentKafkaUpdater(), new Fields());

        Config conf = new Config();
        //set producer properties.
        Properties props = new Properties();
        props.put("metadata.broker.list", "10.6.2.55:9092");
        props.put("request.required.acks", "1");
        props.put("serializer.class", "kafka.serializer.StringEncoder");
        conf.put(TridentKafkaState.KAFKA_BROKER_PROPERTIES, props);


        if (args.length == 0) {
            LocalCluster cluster = new LocalCluster();
            cluster.submitTopology("kafkaTridentTest", conf, topology.build());
            try {
                TimeUnit.SECONDS.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            cluster.killTopology("kafkaTridentTest");
            cluster.shutdown();
        } else {
            StormSubmitter.submitTopology(args[0], conf, topology.build());
            logger.info("================= the word count topology is submitted =================");
        }

    }
}
