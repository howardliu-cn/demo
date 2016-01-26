package cn.howardliu.demo.storm.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import storm.trident.state.OpaqueValue;
import storm.trident.state.map.IBackingMap;
import storm.trident.state.map.OpaqueMap;

/**
 * <br/>create at 16-1-26
 *
 * @author liuxh
 * @since 1.0.0
 */
public class LogAnalysisState extends OpaqueMap<LogAnalysisPojo> {
    private static final Logger logger = LoggerFactory.getLogger(LogAnalysisState.class);

    protected LogAnalysisState(IBackingMap<OpaqueValue> backing) {
        super(backing);
    }
}
