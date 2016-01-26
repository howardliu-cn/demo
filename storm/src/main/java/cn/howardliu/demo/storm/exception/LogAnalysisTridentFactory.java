package cn.howardliu.demo.storm.exception;

import backtype.storm.task.IMetricsContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import storm.trident.state.State;
import storm.trident.state.StateFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * <br/>create at 16-1-26
 *
 * @author liuxh
 * @since 1.0.0
 */
public class LogAnalysisTridentFactory implements StateFactory {
    private static final Logger logger = LoggerFactory.getLogger(LogAnalysisTridentFactory.class);
    private String dataSourceClassName;
    private String dataSourceUrl;
    private String dataSourceUser;
    private String dataSourcePassword;
    private Integer queryTimeoutSecs;

    public LogAnalysisTridentFactory(String dataSourceClassName, String dataSourceUrl, String dataSourceUser,
            String dataSourcePassword) {
        this(dataSourceClassName, dataSourceUrl, dataSourceUser, dataSourcePassword, null);
    }

    public LogAnalysisTridentFactory(String dataSourceClassName, String dataSourceUrl, String dataSourceUser,
            String dataSourcePassword, Integer queryTimeoutSecs) {
        this.dataSourceClassName = dataSourceClassName;
        this.dataSourceUrl = dataSourceUrl;
        this.dataSourceUser = dataSourceUser;
        this.dataSourcePassword = dataSourcePassword;
        this.queryTimeoutSecs = queryTimeoutSecs;
    }

    @Override
    public State makeState(Map conf, IMetricsContext metrics, int partitionIndex, int numPartitions) {
        Map<String, Object> map = new HashMap<>();
        map.put("dataSourceClassName", dataSourceClassName);
        map.put("dataSource.url", dataSourceUrl);
        map.put("dataSource.user", dataSourceUser);
        map.put("dataSource.password", dataSourcePassword);
        return new LogAnalysisState(new LogAnalysisBackingMap(map, queryTimeoutSecs));
    }
}
