package cn.howardliu.demo.spark;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

/**
 * <br/>created at 16-6-20
 *
 * @author liuxh
 * @since 1.2.0
 */
public class SimpleSum {
    private static final Logger logger = LoggerFactory.getLogger(SimpleSum.class);

    public static void main(String[] args) {
        SparkConf conf = new SparkConf().setAppName("SimpleSum");
        JavaSparkContext sc = new JavaSparkContext(conf);
        List<Integer> data = Arrays.asList(1, 2, 3, 4, 5);
        JavaRDD<Integer> distData = sc.parallelize(data);
        Integer result = distData.reduce((v1, v2) -> v1 + v2);
        logger.info("result={}", result);
    }
}
