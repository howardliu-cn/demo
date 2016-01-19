package cn.howardliu.demo.storm.wordCount.simple;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * <br/>create at 16-1-17
 *
 * @author liuxh
 * @since 1.0.0
 */
public class SentenceSpout extends BaseRichSpout {
    private static final Logger logger = LoggerFactory.getLogger(SentenceSpout.class);
    private ConcurrentHashMap<UUID, Values> pending;
    private SpoutOutputCollector collector;
    private String[] sentences = {
            "my dog has fleas",
            "i like cold beverages",
            "the dog ate my homework",
            "don't have a cow man",
            "i don't think i like fleas"
    };
    private int index = 0;

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        outputFieldsDeclarer.declare(new Fields("sentence"));
    }

    @Override
    public void open(Map map, TopologyContext topologyContext, SpoutOutputCollector spoutOutputCollector) {
        this.collector = spoutOutputCollector;
        this.pending = new ConcurrentHashMap<>();
    }

    @Override
    public void nextTuple() {
        Values values = new Values(sentences[index]);
        UUID msgId = UUID.randomUUID();
        this.pending.put(msgId, values);
        logger.debug("sentence is {}, the msgId is {} ", values, msgId);
        this.collector.emit(values, msgId);
        index++;
        if (index >= sentences.length) {
            index = 0;
        }
        try {
            TimeUnit.MILLISECONDS.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    @SuppressWarnings("SuspiciousMethodCalls")
    public void ack(Object msgId) {
        this.pending.remove(msgId);
    }

    @Override
    @SuppressWarnings("SuspiciousMethodCalls")
    public void fail(Object msgId) {
        this.collector.emit(this.pending.get(msgId), msgId);
    }
}
