package cn.howardliu.demo.storm.trident;

import backtype.storm.task.IMetricsContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import storm.trident.state.State;
import storm.trident.state.StateFactory;

import java.util.Map;

/**
 * <br/>create at 16-1-25
 *
 * @author liuxh
 * @since 1.0.0
 */
public class OutbreakTrendFactory implements StateFactory {
    private static final Logger logger = LoggerFactory.getLogger(OutbreakTrendFactory.class);

    @Override
    public State makeState(Map conf, IMetricsContext metrics, int partitionIndex, int numPartitions) {
        return new OutbreakTrendState(new OutbreakTrendBackingMap());
    }
}
