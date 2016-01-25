package cn.howardliu.demo.storm.trident;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import storm.trident.state.map.IBackingMap;
import storm.trident.state.map.NonTransactionalMap;

/**
 * <br/>create at 16-1-25
 *
 * @author liuxh
 * @since 1.0.0
 */
public class OutbreakTrendState extends NonTransactionalMap<Long> {
    private static final Logger logger = LoggerFactory.getLogger(OutbreakTrendState.class);

    protected OutbreakTrendState(IBackingMap<Long> backing) {
        super(backing);
    }
//    protected OutbreakTrendState(OutbreakTrendBackingMap outbreakTrendBackingMap) {
//        super(outbreakTrendBackingMap);
//    }
}
