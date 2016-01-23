package cn.howardliu.demo.storm.hbase.simple;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;
import backtype.storm.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * <br/>create at 16-1-23
 *
 * @author liuxh
 * @since 1.0.0
 */
public class SentenceSpout extends BaseRichSpout {
    private static final Logger logger = LoggerFactory.getLogger(SentenceSpout.class);
    private String[] sentences = new String[]{"this is a test", "hello world"};
    private int index = 0;
    private SpoutOutputCollector collector;

    @Override
    public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
        this.collector = collector;
    }

    @Override
    public void nextTuple() {
        String sentence = sentences[index];
        index++;
        if (index >= sentences.length) {
            index = 0;
        }
        logger.info("send one sentence {}", sentence);
        this.collector.emit(new Values(sentence, "newRk1", "newF1"));
        Utils.sleep(5000);
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("sentence", "rk1", "f1"));
    }
}
