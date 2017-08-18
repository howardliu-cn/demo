package cn.howardliu.demo.jdk8Time;

import java.time.Duration;
import java.time.LocalTime;
import java.time.temporal.TemporalUnit;

/**
 * <br>created at 17-4-27
 *
 * @author liuxh
 * @version 1.0.0
 * @since 1.0.0
 */
public class DurationDemo {
    public static void main(String[] args) {
        LocalTime time = LocalTime.now();
        Duration duration = Duration.ofSeconds(1);
        System.out.println(duration);
        System.out.println(duration.plusSeconds(1));
        System.out.println(duration.plusSeconds(1).plusSeconds(1));
        System.out.println(duration.minus(duration.plusSeconds(1)));
    }
}
