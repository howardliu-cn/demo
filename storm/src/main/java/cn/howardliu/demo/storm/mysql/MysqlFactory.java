package cn.howardliu.demo.storm.mysql;

import backtype.storm.task.IMetricsContext;
import storm.trident.state.OpaqueValue;
import storm.trident.state.State;
import storm.trident.state.StateFactory;
import storm.trident.state.TransactionalValue;
import storm.trident.state.map.*;

import java.util.Map;

/**
 * <br/>create at 16-1-26
 *
 * @author liuxh
 * @since 1.0.0
 */
public class MysqlFactory implements StateFactory {
    private MysqlStateConfig config;

    public MysqlFactory(MysqlStateConfig config) {
        this.config = config;
    }

    @Override
    @SuppressWarnings("unchecked")
    public State makeState(Map conf, IMetricsContext metrics, int partitionIndex, int numPartitions) {
        IBackingMap map = new CachedMap<>(new MysqlBackingMap<>(config), config.getCacheSize());
        switch (config.getType()) {
            case OPAQUE: {
                return OpaqueMap.build(map);
            }
            case TRANSACTIONAL: {
                return TransactionalMap.build(map);
            }
            default: {
                return NonTransactionalMap.build(map);
            }
        }
    }
}
