package storm.kafka;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * <br/>create at 16-1-20
 *
 * @author liuxh
 * @since 1.0.0
 */
public class MyKafkaSpout extends KafkaSpout {
    private static final Logger log = LoggerFactory.getLogger(MyKafkaSpout.class);

    public MyKafkaSpout(SpoutConfig spoutConf) {
        super(spoutConf);
    }

    @Override
    public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
        _uuid = context.getStormId();
        log.info("the topologyInstanceId is {}", _uuid);
        super.open(conf, context, collector);
    }
}
