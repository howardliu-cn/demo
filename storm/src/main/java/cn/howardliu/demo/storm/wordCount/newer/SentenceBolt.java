package cn.howardliu.demo.storm.wordCount.newer;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * <br/>create at 16-1-20
 *
 * @author liuxh
 * @since 1.0.0
 */
public class SentenceBolt extends BaseRichBolt {
    private static final Logger logger = LoggerFactory.getLogger(SentenceBolt.class);
    private OutputCollector collector;

    @Override
    public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
        this.collector = collector;
    }

    @Override
    public void execute(Tuple input) {
        String msg = (String) input.getValue(0);
        logger.info("get one message '{}'", msg);
        String out = "this message is '" + msg + "'!";
        collector.emit(new Values(out));
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("message"));
    }
}
