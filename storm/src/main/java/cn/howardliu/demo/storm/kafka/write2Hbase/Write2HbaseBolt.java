package cn.howardliu.demo.storm.kafka.write2Hbase;

import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import org.apache.storm.hbase.bolt.mapper.SimpleHBaseMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <br/>create at 16-1-22
 *
 * @author liuxh
 * @since 1.0.0
 */
public class Write2HbaseBolt extends BaseBasicBolt {
    private static final Logger logger = LoggerFactory.getLogger(Write2HbaseBolt.class);

    @Override
    public void execute(Tuple input, BasicOutputCollector collector) {
        String message = input.getStringByField("message");
        logger.info("got one message {}", message);
        SimpleHBaseMapper mapper = new SimpleHBaseMapper()
                .withRowKeyField("word")
                .withColumnFamily("cf")
                .withColumnFields(new Fields("word"))
                .withCounterFields(new Fields("count"));
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
    }
}
