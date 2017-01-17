package cn.howardliu.demo.spark;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.storage.StorageLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scala.Tuple2;

import java.util.Map;

/**
 * <br/>created at 16-8-30
 *
 * @author liuxh
 * @since 1.2.0
 */
public class WordCount {
    private static final Logger logger = LoggerFactory.getLogger(WordCount.class);

    public static void main(String[] args) {
        SparkConf conf = new SparkConf().setAppName("WordCount");
        JavaSparkContext sc = new JavaSparkContext(conf);

        JavaRDD<String> lines = sc.textFile(args[0]);
        JavaRDD<Integer> lineLengths = lines.map(String::length);
        lineLengths.persist(StorageLevel.MEMORY_ONLY());
        int totalLength = lineLengths.reduce((a, b) -> a + b);
        System.out.println("totalLength = " + totalLength);

        JavaPairRDD<String, Integer> pairs = lines.mapToPair(s -> new Tuple2<>(s, 1));
        JavaPairRDD<String, Integer> countsRDD = pairs.reduceByKey((a, b) -> a + b).sortByKey();
        countsRDD.saveAsTextFile(args[1]);
        Map<String, Integer> counts = countsRDD.collectAsMap();
        System.out.println("counts = " + counts);

    }
}
