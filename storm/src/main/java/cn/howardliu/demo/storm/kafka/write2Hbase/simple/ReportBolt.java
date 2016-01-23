package cn.howardliu.demo.storm.kafka.write2Hbase.simple;

import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <br/>create at 16-1-23
 *
 * @author liuxh
 * @since 1.0.0
 */
public class ReportBolt extends BaseBasicBolt {
    private static final Logger logger = LoggerFactory.getLogger(ReportBolt.class);

    @Override
    public void execute(Tuple input, BasicOutputCollector collector) {
        String sentence = input.getStringByField("sentence");
        String rk1 = input.getStringByField("rk1");
        String f1 = input.getStringByField("f1");
        logger.info("the sentence is {}, the row key is {}, the family is {}", sentence, rk1, f1);
        collector.emit(new Values(sentence, rk1, f1));
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("sentence", "rk1", "f1"));
    }
}
