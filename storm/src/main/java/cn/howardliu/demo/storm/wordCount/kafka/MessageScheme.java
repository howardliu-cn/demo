package cn.howardliu.demo.storm.wordCount.kafka;

import backtype.storm.spout.Scheme;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * <br/>create at 16-1-19
 *
 * @author liuxh
 * @since 1.0.0
 */
public class MessageScheme implements Scheme {
    private static final Logger logger = LoggerFactory.getLogger(MessageScheme.class);

    @Override
    public List<Object> deserialize(byte[] bytes) {
        try {
            String msg = new String(bytes, "UTF-8");
            return new Values(msg);
        } catch (UnsupportedEncodingException ignored) {
        }
        return null;
    }

    @Override
    public Fields getOutputFields() {
        return new Fields("msg");
    }
}
