package cn.howardliu.demo.storm.log2Kafka;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import cn.howardliu.demo.storm.kafka.BaseConfigConstants;
import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

import java.util.Properties;


/**
 * <br/>create at 16-1-18
 *
 * @author liuxh
 * @since 1.0.0
 */
public class KafkaAppender extends AppenderBase<ILoggingEvent> {
    private String topic;
    private String zookeeperHost;
    private Formatter formatter;
    private Producer<String, String> producer;

    @Override
    public void start() {
        if(this.formatter == null) {
            this.formatter = new MessageFormatter();
        }
        super.start();
        Properties props = new Properties();
        props.put("zk.connect", this.zookeeperHost);
        props.put("metadata.broker.list", BaseConfigConstants.BROKER_SERVER);
        props.put("serializer.class", "kafka.serializer.StringEncoder");
        ProducerConfig config = new ProducerConfig(props);
        this.producer = new Producer<>(config);
    }

    @Override
    protected void append(ILoggingEvent event) {
        String payload = this.formatter.format(event);
        KeyedMessage<String, String> message = new KeyedMessage<>(this.topic, payload);
        this.producer.send(message);
    }

    @Override
    public void stop() {
        super.stop();
        this.producer.close();
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getZookeeperHost() {
        return zookeeperHost;
    }

    public void setZookeeperHost(String zookeeperHost) {
        this.zookeeperHost = zookeeperHost;
    }

    public Formatter getFormatter() {
        return formatter;
    }

    public void setFormatter(Formatter formatter) {
        this.formatter = formatter;
    }
}
