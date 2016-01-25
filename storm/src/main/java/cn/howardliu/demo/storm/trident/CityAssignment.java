package cn.howardliu.demo.storm.trident;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import storm.trident.operation.BaseFunction;
import storm.trident.operation.TridentCollector;
import storm.trident.tuple.TridentTuple;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <br/>create at 16-1-25
 *
 * @author liuxh
 * @since 1.0.0
 */
public class CityAssignment extends BaseFunction {
    private static final Logger logger = LoggerFactory.getLogger(CityAssignment.class);
    private static final Map<String, double[]> cities = new HashMap<>();

    {
        cities.put("PHL", new double[]{39.875365, -75.249524});
        cities.put("NYC", new double[]{40.71448, -74.00598});
        cities.put("SF", new double[]{-31.4250142, -62.0841809});
        cities.put("LA", new double[]{-34.05374, -118.24307});
    }

    @Override
    public void execute(TridentTuple tuple, TridentCollector collector) {
        DiagnosisEvent diagnosis = (DiagnosisEvent) tuple.getValue(0);
        double leastDistance = Double.MAX_VALUE;
        String closestCity = "NONE";

        for (Map.Entry<String, double[]> city : cities.entrySet()) {
            double R = 6371;// km
            double x = (city.getValue()[0] - diagnosis.lng) * Math.cos((city.getValue()[0] + diagnosis.lng) / 2);
            double y = (city.getValue()[1] - diagnosis.lat);
            double d = Math.sqrt(x * x + y * y) * R;
            if (d < leastDistance) {
                leastDistance = d;
                closestCity = city.getKey();
            }
        }
        // emit the value
        List<Object> values = new ArrayList<>();
        values.add(closestCity);
        logger.info("Closest city to lat=[{}], lng=[{}], d=[{}]", diagnosis.lat, diagnosis.lng, leastDistance);
        collector.emit(values);
    }
}
