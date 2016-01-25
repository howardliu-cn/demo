package cn.howardliu.demo.storm.trident;

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
public class DiseaseFilter extends BaseFilter {
    private static final Logger logger = LoggerFactory.getLogger(DiseaseFilter.class);

    @Override
    public boolean isKeep(TridentTuple tuple) {
        DiagnosisEvent diagnosis = (DiagnosisEvent) tuple.getValue(0);
        Integer code = Integer.parseInt(diagnosis.diagnosisCode);
        if (code <= 322) {
            logger.info("emitting disease [{}]", diagnosis.diagnosisCode);
            return true;
        }
        logger.info("Filtering disease [{}]", diagnosis.diagnosisCode);
        return false;
    }
}
