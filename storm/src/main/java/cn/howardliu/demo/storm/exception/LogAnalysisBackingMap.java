package cn.howardliu.demo.storm.exception;

import backtype.storm.Config;
import org.apache.storm.jdbc.common.Column;
import org.apache.storm.jdbc.common.ConnectionProvider;
import org.apache.storm.jdbc.common.HikariCPConnectionProvider;
import org.apache.storm.jdbc.common.JdbcClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import storm.trident.state.OpaqueValue;
import storm.trident.state.map.IBackingMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <br/>create at 16-1-26
 *
 * @author liuxh
 * @since 1.0.0
 */
public class LogAnalysisBackingMap implements IBackingMap<OpaqueValue> {
    private static final Logger logger = LoggerFactory.getLogger(LogAnalysisBackingMap.class);
    private JdbcClient jdbcClient;

    public LogAnalysisBackingMap(Map<String, Object> map, Integer queryTimeoutSecs) {
        ConnectionProvider connectionProvider = new HikariCPConnectionProvider(map);
        connectionProvider.prepare();

        if (queryTimeoutSecs == null) {
            queryTimeoutSecs = Integer.parseInt(map.get(Config.TOPOLOGY_MESSAGE_TIMEOUT_SECS).toString());
        }

        this.jdbcClient = new JdbcClient(connectionProvider, queryTimeoutSecs);
    }

    @Override
    public List<OpaqueValue> multiGet(List<List<Object>> keys) {
        return new ArrayList<>();
    }

    @Override
    public void multiPut(List<List<Object>> keys, List<OpaqueValue> vals) {
        logger.info("vals={}", vals);
    }
}
