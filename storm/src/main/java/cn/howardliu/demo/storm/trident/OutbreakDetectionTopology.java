package cn.howardliu.demo.storm.trident;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.generated.StormTopology;
import backtype.storm.tuple.Fields;
import backtype.storm.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import storm.trident.Stream;
import storm.trident.TridentTopology;
import storm.trident.operation.builtin.Count;

/**
 * <br/>create at 16-1-25
 *
 * @author liuxh
 * @since 1.0.0
 */
public class OutbreakDetectionTopology {
    private static final Logger logger = LoggerFactory.getLogger(OutbreakDetectionTopology.class);

    public static StormTopology buildTopology() {
        TridentTopology topology = new TridentTopology();

        DiagnosisEventSpout spout = new DiagnosisEventSpout();
        Stream inputStream = topology.newStream("event", spout);
        inputStream
                // Filter for critical events
                .each(new Fields("event"), new DiseaseFilter())
                        //locate the closest city
                .each(new Fields("event"), new CityAssignment(), new Fields("city"))
                        // Derive the hour segment
                .each(new Fields("event", "city"), new HourAssignment(), new Fields("hour", "cityDiseaseHour"))
                        // group occurrences in same city and hour
                .groupBy(new Fields("cityDiseaseHour"))
                        // count occurrences and persist the results
                .persistentAggregate(new OutbreakTrendFactory(), new Count(), new Fields("count"))
                .newValuesStream()
                        // detect an outbreak
                .each(new Fields("cityDiseaseHour", "count"), new OutbreakDetector(), new Fields("alert"))
                        // dispatch the alert
                .each(new Fields("alert"), new DispatchAlert(), new Fields());
        return topology.build();
    }

    public static void main(String[] args) {
        Config config = new Config();
        LocalCluster cluster = new LocalCluster();
        cluster.submitTopology("cdc", config, buildTopology());
        Utils.sleep(200000);
        cluster.killTopology("cdc");
        cluster.shutdown();
    }
}
