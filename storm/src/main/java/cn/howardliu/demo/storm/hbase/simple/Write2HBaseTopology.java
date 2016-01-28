package cn.howardliu.demo.storm.hbase.simple;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.tuple.Fields;
import cn.howardliu.demo.storm.kafka.write2Hbase.HBaseBolt;
import com.google.common.collect.Maps;
import org.apache.storm.hbase.bolt.mapper.SimpleHBaseMapper;

import java.util.Map;

/**
 * <br/>create at 16-1-23
 *
 * @author liuxh
 * @since 1.0.0
 */
public class Write2HBaseTopology {
    public static void main(String[] args) throws Exception {
        TopologyBuilder builder = new TopologyBuilder();
        builder.setSpout("sentenceSpout", new SentenceSpout(), 1);

        SimpleHBaseMapper mapper = new SimpleHBaseMapper()
                .withRowKeyField("rk1")
                .withColumnFamily("f1")
                .withColumnFields(new Fields("sentence"));
        HBaseBolt hBaseBolt = new HBaseBolt("t1", mapper).withConfigKey("hbConfig");
        builder.setBolt("reportBolt", new ReportBolt(), 1).shuffleGrouping("sentenceSpout");
        builder.setBolt("hbaseBolt", hBaseBolt, 1).fieldsGrouping("reportBolt", new Fields("sentence"));

        Map<String, String> HBConfig = Maps.newHashMap();
        HBConfig.put("hbase.rootdir", "hdfs://s55:9000/hbase");
        HBConfig.put("hbase.tmp.dir", "/opt/hbase/data");
        HBConfig.put("hbase.zookeeper.quorum", "s55");
        HBConfig.put("hbase.zookeeper.property.clientPort", "12181");

        Config config = new Config();
        config.put("hbConfig", HBConfig);
        config.setNumWorkers(1);

        if (args.length == 0) {
            LocalCluster cluster = new LocalCluster();
            cluster.submitTopology("SimpleWrite2HBaseTopology", config, builder.createTopology());
        } else {
            StormSubmitter.submitTopology(args[0], config, builder.createTopology());
        }
    }
}
