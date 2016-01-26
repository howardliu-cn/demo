package cn.howardliu.demo.storm.alert;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.generated.AlreadyAliveException;
import backtype.storm.generated.AuthorizationException;
import backtype.storm.generated.InvalidTopologyException;
import backtype.storm.generated.StormTopology;
import backtype.storm.spout.SchemeAsMultiScheme;
import backtype.storm.tuple.Fields;
import storm.kafka.BrokerHosts;
import storm.kafka.StringScheme;
import storm.kafka.ZkHosts;
import storm.kafka.trident.OpaqueTridentKafkaSpout;
import storm.kafka.trident.TridentKafkaConfig;
import storm.trident.Stream;
import storm.trident.TridentTopology;

/**
 * <br/>create at 16-1-25
 *
 * @author liuxh
 * @since 1.0.0
 */
public class LogAnalysisTopology {
    public static StormTopology buildTopology() {
        TridentTopology topology = new TridentTopology();
        BrokerHosts zkHosts = new ZkHosts("zk1:2181,zk2:2182,zk3:2183");
        TridentKafkaConfig spoutConf = new TridentKafkaConfig(zkHosts, "log-analysis");
        spoutConf.scheme = new SchemeAsMultiScheme(new StringScheme());
        spoutConf.startOffsetTime = kafka.api.OffsetRequest.LatestTime();

        OpaqueTridentKafkaSpout spout = new OpaqueTridentKafkaSpout(spoutConf);
        Stream spoutStream = topology.newStream("kafka-stream", spout);
        Fields jsonFields = new Fields("level", "timestamp", "message", "logger");
        Stream parsedStream = spoutStream.each(new Fields("str"), new JsonProjectFunction(jsonFields), jsonFields);

        // drop the unparsed JSON to reduce tuple size
        parsedStream = parsedStream.project(jsonFields);

        EWMA ewma = new EWMA().sliding(1.0, EWMA.Time.MINUTES).withAlpha(EWMA.ONE_MINUTE_ALPHA);
        Stream averageStream = parsedStream.each(new Fields("timestamp"),
                new MovingAverageFunction(ewma, EWMA.Time.MINUTES), new Fields("average"));

        ThresholdFilterFunction tff = new ThresholdFilterFunction(50d);
        Stream thresholdStream = averageStream.each(new Fields("average"), tff, new Fields("change", "threshold"));

        Stream filteredStream = thresholdStream.each(new Fields("change"), new BooleanFilter());
        filteredStream.each(filteredStream.getOutputFields(), new XMPPFunction(new NotifyMessageMapper()),
                new Fields());

        return topology.build();
    }

    public static void main(String[] args)
            throws InvalidTopologyException, AuthorizationException, AlreadyAliveException {
        Config conf = new Config();
        conf.put(XMPPFunction.XMPP_USER, "storm@budreau.local");
        conf.put(XMPPFunction.XMPP_PASSWORD, "storm");
        conf.put(XMPPFunction.XMPP_SERVER, "budreau.local");
        conf.put(XMPPFunction.XMPP_TO, "tgoetz@budreau.local");
        conf.setMaxSpoutPending(5);
        if (args.length == 0) {
            LocalCluster cluster = new LocalCluster();
            cluster.submitTopology("log-analysis", conf, buildTopology());
        } else {
            conf.setNumAckers(3);
            StormSubmitter.submitTopology(args[0], conf, buildTopology());
        }
    }
}
