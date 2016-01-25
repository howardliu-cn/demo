package cn.howardliu.demo.storm.trident;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

/**
 * <br/>create at 16-1-25
 *
 * @author liuxh
 * @since 1.0.0
 */
public class DiagnosisEvent implements Serializable {
    private static final Logger logger = LoggerFactory.getLogger(DiagnosisEvent.class);

    public double lat;
    public double lng;
    public long time;
    public String diagnosisCode;

    public DiagnosisEvent(double lat, double lng, long time, String diagnosisCode) {
        this.lat = lat;
        this.lng = lng;
        this.time = time;
        this.diagnosisCode = diagnosisCode;
    }
}
