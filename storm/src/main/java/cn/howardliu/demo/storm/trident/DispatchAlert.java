package cn.howardliu.demo.storm.trident;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import storm.trident.operation.BaseFunction;
import storm.trident.operation.TridentCollector;
import storm.trident.tuple.TridentTuple;

/**
 * <br/>create at 16-1-25
 *
 * @author liuxh
 * @since 1.0.0
 */
public class DispatchAlert extends BaseFunction {
    private static final Logger logger = LoggerFactory.getLogger(DispatchAlert.class);

    @Override
    public void execute(TridentTuple tuple, TridentCollector collector) {
        String alert = (String) tuple.getValue(0);
        logger.error("ALERT RECEIVED [{}]", alert);
        logger.error("Dispatch the national guard!");
        System.exit(0);
    }
}
