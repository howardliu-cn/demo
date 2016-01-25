package cn.howardliu.demo.storm.trident;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import storm.trident.operation.BaseFunction;
import storm.trident.operation.TridentCollector;
import storm.trident.tuple.TridentTuple;

import java.util.ArrayList;
import java.util.List;

/**
 * <br/>create at 16-1-25
 *
 * @author liuxh
 * @since 1.0.0
 */
public class OutbreakDetector extends BaseFunction {
    private static final Logger logger = LoggerFactory.getLogger(OutbreakDetector.class);
    public static final int THRESHOLD = 10000;

    @Override
    public void execute(TridentTuple tuple, TridentCollector collector) {
        String key = (String) tuple.getValue(0);
        Long count = (Long) tuple.getValue(1);
        if (count > THRESHOLD) {
            List<Object> values = new ArrayList<>();
            values.add("Outbreak detected for [" + key + "]");
            collector.emit(values);
        }
    }
}
