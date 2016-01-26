package cn.howardliu.demo.storm.alert;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import storm.trident.tuple.TridentTuple;

import java.util.Date;

/**
 * <br/>create at 16-1-25
 *
 * @author liuxh
 * @since 1.0.0
 */
public class NotifyMessageMapper implements MessageMapper {
    private static final Logger logger = LoggerFactory.getLogger(NotifyMessageMapper.class);

    @Override
    public String toMessageBody(TridentTuple tuple) {
        StringBuilder sb = new StringBuilder();
        sb.append("On ").append(new Date(tuple.getLongByField("timestamp"))).append(" ");
        sb.append("the application \"").append(tuple.getStringByField("logger")).append("\" ");
        sb.append("changed alert state based on a threshold of ").append(tuple.getDoubleByField("threshold"))
                .append(".\n");
        sb.append("The last value was ").append(tuple.getDoubleByField("average")).append("\n");
        sb.append("The last message was ").append(tuple.getStringByField("message")).append("\"");
        return sb.toString();
    }
}
