package cn.howardliu.demo.storm.alert;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import storm.trident.operation.BaseFilter;
import storm.trident.tuple.TridentTuple;

/**
 * <br/>create at 16-1-25
 *
 * @author liuxh
 * @since 1.0.0
 */
public class BooleanFilter extends BaseFilter {
    private static final Logger logger = LoggerFactory.getLogger(BooleanFilter.class);

    @Override
    public boolean isKeep(TridentTuple tuple) {
        return tuple.getBoolean(0);
    }
}
