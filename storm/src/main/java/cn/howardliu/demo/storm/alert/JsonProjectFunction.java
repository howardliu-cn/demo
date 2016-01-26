package cn.howardliu.demo.storm.alert;

import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import storm.trident.operation.BaseFunction;
import storm.trident.operation.TridentCollector;
import storm.trident.tuple.TridentTuple;

import java.util.Map;

/**
 * <br/>create at 16-1-25
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
    public void execute(TridentTuple tuple, TridentCollector collector) {
        String json = tuple.getString(0);
        //noinspection unchecked
        Map<String, Object> map = JSONObject.fromObject(json);
        Values values = new Values();
        for (int i = 0; i < this.fields.size(); i++) {
            values.add(map.get(this.fields.get(i)));
        }
        collector.emit(values);
    }
}
