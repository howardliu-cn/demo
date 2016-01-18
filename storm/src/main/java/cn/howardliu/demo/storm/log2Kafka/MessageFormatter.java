package cn.howardliu.demo.storm.log2Kafka;

import ch.qos.logback.classic.spi.ILoggingEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <br/>create at 16-1-18
 *
 * @author liuxh
 * @since 1.0.0
 */
public class MessageFormatter implements Formatter {
    private static final Logger logger = LoggerFactory.getLogger(MessageFormatter.class);

    @Override
    public String format(ILoggingEvent event) {
        return event.getFormattedMessage();
    }
}
