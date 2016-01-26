package cn.howardliu.demo.storm.log2Kafka;

import ch.qos.logback.classic.spi.ILoggingEvent;
import net.sf.json.JSONObject;

/**
 * <br/>create at 16-1-18
 *
 * @author liuxh
 * @since 1.0.0
 */
public class JsonFormatter implements Formatter {
    private boolean expectJson = false;

    @Override
    public String format(ILoggingEvent event) {
        JSONObject json = new JSONObject();
        json.put("level", event.getLevel().levelStr);
        json.put("logger", event.getLoggerName());
        json.put("timestamp", event.getTimeStamp());
        if (this.expectJson) {
            json.put("message", "'" + event.getFormattedMessage() + "'");
        } else {
            json.put("message", "'" + event.getLoggerName() + "'");
        }
        return json.toString();
    }

    @SuppressWarnings("UnusedDeclaration")
    public boolean isExpectJson() {
        return expectJson;
    }

    @SuppressWarnings("UnusedDeclaration")
    public void setExpectJson(boolean expectJson) {
        this.expectJson = expectJson;
    }
}
