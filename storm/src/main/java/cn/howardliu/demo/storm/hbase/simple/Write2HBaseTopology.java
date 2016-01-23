package cn.howardliu.demo.storm.hbase.simple;

import backtype.storm.Config;
import backtype.storm.StormSubmitter;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.tuple.Fields;
import com.google.common.collect.Maps;
import org.apache.storm.hbase.bolt.HBaseBolt;
import org.apache.storm.hbase.bolt.mapper.SimpleHBaseMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * <br/>create at 16-1-23
 *
 * @author liuxh
 * @since 1.0.0
 */
public class Write2HBaseTopology {
    private static final Logger logger = LoggerFactory.getLogger(Write2HBaseTopology.class);

    public static void main(String[] args) throws Exception {
        TopologyBuilder builder = new TopologyBuilder();
        builder.setSpout("sentenceSpout", new SentenceSpout(), 1);

        SimpleHBaseMapper mapper = new SimpleHBaseMapper()
                .withRowKeyField("rk1")
                .withColumnFamily("f1")
                .withColumnFields(new Fields("sentence"));
        HBaseBolt hBaseBolt = new HBaseBolt("t1", mapper).withConfigKey("hbConfig");
        builder.setBolt("reportBolt", new ReportBolt(), 1).shuffleGrouping("sentenceSpout");
        builder.setBolt("hbaseBolt", hBaseBolt, 1).shuffleGrouping("reportBolt");

        Map<String, String> HBConfig = Maps.newHashMap();
        // HBConfig.put("hbase.rootdir","hdfs://10.6.2.56:9000/hbase");
        Config config = new Config();
        config.put("hbConfig",HBConfig);
        config.setNumWorkers(1);
        StormSubmitter.submitTopology(args[0], config, builder.createTopology());
    }
}
