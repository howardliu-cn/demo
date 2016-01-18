package cn.howardliu.demo.storm.log2Kafka;

import ch.qos.logback.classic.spi.ILoggingEvent;

/**
 * <br/>create at 16-1-18
 *
 * @author liuxh
 * @since 1.0.0
 */
public interface Formatter {
    String format(ILoggingEvent event);
}
