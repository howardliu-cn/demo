package cn.howardliu.demo.storm.trident;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import storm.trident.state.map.IBackingMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <br/>create at 16-1-25
 *
 * @author liuxh
 * @since 1.0.0
 */
public class OutbreakTrendBackingMap implements IBackingMap<Long> {
    private static final Logger logger = LoggerFactory.getLogger(OutbreakTrendBackingMap.class);
    Map<String, Long> storage = new ConcurrentHashMap<>();

    @Override
    @SuppressWarnings("SuspiciousMethodCalls")
    public List<Long> multiGet(List<List<Object>> keys) {
        List<Long> values = new ArrayList<>();
        for (List<Object> key : keys) {
            Long value = storage.get(key.get(0));
            if (value == null) {
                values.add(0L);
            } else {
                values.add(value);
            }
        }
        return values;
    }

    @Override
    public void multiPut(List<List<Object>> keys, List<Long> vals) {
        for (int i = 0; i < keys.size(); i++) {
            logger.info("persisting [{}] ==> [{}]", keys.get(i).get(0), vals.get(i));
            storage.put(keys.get(i).get(0) + "", vals.get(i));
        }
    }
}
