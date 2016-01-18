package cn.howardliu.demo.storm.log;

import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;
import org.apache.storm.shade.org.json.simple.JSONValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import storm.trident.operation.BaseFunction;
import storm.trident.operation.TridentCollector;
import storm.trident.tuple.TridentTuple;

import java.util.Map;

/**
 * <br/>create at 16-1-18
 *
 * @author liuxh
 * @since 1.0.0
 */
public class JsonProjectFunction extends BaseFunction {
    private static final Logger logger = LoggerFactory.getLogger(JsonProjectFunction.class);
    private Fields fields;

    public JsonProjectFunction(Fields fields) {
        this.fields = fields;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void execute(TridentTuple tridentTuple, TridentCollector tridentCollector) {
        String json = tridentTuple.getString(0);
        Map<String, Object> map = (Map<String, Object>) JSONValue.parse(json);
        Values values = new Values();
        for (int i = 0; i < this.fields.size(); i++) {
            values.add(map.get(this.fields.get(i)));
        }
        tridentCollector.emit(values);
    }
}
