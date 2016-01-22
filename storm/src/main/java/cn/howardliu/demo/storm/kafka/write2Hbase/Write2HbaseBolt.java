package cn.howardliu.demo.storm.kafka.write2Hbase;

import backtype.storm.task.TopologyContext;
import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.HConnection;
import org.apache.hadoop.hbase.client.HConnectionManager;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;

/**
 * <br/>create at 16-1-22
 *
 * @author liuxh
 * @since 1.0.0
 */
public class Write2HbaseBolt extends BaseBasicBolt {
    private static final Logger logger = LoggerFactory.getLogger(Write2HbaseBolt.class);
    private HConnection connection;

    @Override
    public void prepare(Map stormConf, TopologyContext context) {
        Configuration config = HBaseConfiguration.create();
        try {
            connection = HConnectionManager.createConnection(config);
        } catch (IOException e) {
            logger.error("get an exception when building connection to hbase", e);
        }
    }

    @Override
    public void execute(Tuple input, BasicOutputCollector collector) {
        try {
            String word = input.getStringByField("word");
            Long count = input.getLongByField("count");
            try (HTableInterface table = connection.getTable("WordCount")) {
                Put p = new Put("word".getBytes());
                p.add("cf".getBytes(), "word".getBytes(), Bytes.toBytes(word));
                p.add("cf".getBytes(), "count".getBytes(), Bytes.toBytes(count));
                table.put(p);
            }
            collector.emit(new Values(word, count));
        } catch (Exception e) {
            logger.error("bolt error", e);
            e.printStackTrace();
            collector.reportError(e);
        }
    }

    @Override
    public void cleanup() {
        logger.info("cleanup called");
        try {
            connection.close();
            logger.info("hbase closed");
        } catch (Exception e) {
            logger.error("cleanup error", e);
        }
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("word", "count"));
    }
}
