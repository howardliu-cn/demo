package cn.howardliu.demo.storm.alert;

import backtype.storm.tuple.Values;
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
public class MovingAverageFunction extends BaseFunction {
    private static final Logger logger = LoggerFactory.getLogger(MovingAverageFunction.class);
    private EWMA ewma;
    private EWMA.Time emitRatePer;

    public MovingAverageFunction(EWMA ewma, EWMA.Time emitRatePer) {
        this.ewma = ewma;
        this.emitRatePer = emitRatePer;
    }

    @Override
    public void execute(TridentTuple tuple, TridentCollector collector) {
        this.ewma.mark(tuple.getLong(0));
        double averageRatePer = this.ewma.getAverageRatePer(this.emitRatePer);
        logger.debug("Rate:{}", averageRatePer);
        collector.emit(new Values(averageRatePer));
    }
}
