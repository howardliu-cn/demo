package cn.howardliu.demo.storm.exception;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.generated.AlreadyAliveException;
import backtype.storm.generated.AuthorizationException;
import backtype.storm.generated.InvalidTopologyException;
import backtype.storm.generated.StormTopology;
import backtype.storm.spout.SchemeAsMultiScheme;
import backtype.storm.tuple.Fields;
import backtype.storm.utils.Utils;
import cn.howardliu.demo.storm.alert.JsonProjectFunction;
import cn.howardliu.demo.storm.mysql.MysqlFactory;
import cn.howardliu.demo.storm.mysql.MysqlStateConfig;
import storm.kafka.StringScheme;
import storm.kafka.ZkHosts;
import storm.kafka.trident.OpaqueTridentKafkaSpout;
import storm.kafka.trident.TridentKafkaConfig;
import storm.trident.Stream;
import storm.trident.TridentTopology;
import storm.trident.fluent.GroupedStream;
import storm.trident.operation.builtin.Count;
import storm.trident.state.StateType;

/**
 * <br/>create at 16-1-26
 *
 * @author liuxh
 * @since 1.0.0
 */
public class LogAnalysisTopology {
    public static StormTopology buildTopology() {
        TridentTopology topology = new TridentTopology();
        ZkHosts zkHosts = new ZkHosts("zk1:2181,zk2:2182,zk3:2183");
        TridentKafkaConfig spoutConf = new TridentKafkaConfig(zkHosts, "log-analysis");
        spoutConf.scheme = new SchemeAsMultiScheme(new StringScheme());
        // spoutConf.startOffsetTime = kafka.api.OffsetRequest.LatestTime();
        OpaqueTridentKafkaSpout spout = new OpaqueTridentKafkaSpout(spoutConf);
        Stream spoutStream = topology.newStream("log-analysis-stream", spout);
        Fields jsonFields = new Fields("level", "timestamp");
        GroupedStream stream = spoutStream
                .each(new Fields("str"), new JsonProjectFunction(jsonFields), jsonFields)
                .project(jsonFields)
                .groupBy(new Fields("level", "timestamp"));
        MysqlStateConfig config = new MysqlStateConfig();
        config.setUrl("jdbc:mysql://localhost:3306/auth");
        config.setUsername("root");
        config.setPassword("root");
        config.setTable("log_analysis");
        config.setKeyColumns(new String[]{"level", "timestamp"});
        config.setValueColumns(new String[]{"count"});
        config.setType(StateType.OPAQUE);
        config.setCacheSize(1000);
        stream.persistentAggregate(new MysqlFactory(config), new Count(), new Fields("count"));

        return topology.build();
    }

    public static void main(String[] args)
            throws InvalidTopologyException, AuthorizationException, AlreadyAliveException {
        Config config = new Config();
        if (args.length == 0) {
            LocalCluster cluster = new LocalCluster();
            cluster.submitTopology("log-analysis", config, buildTopology());
            Utils.sleep(20000);
            cluster.killTopology("log-analysis");
            cluster.shutdown();
        } else {
            StormSubmitter.submitTopology(args[0], config, buildTopology());
        }
    }
}
