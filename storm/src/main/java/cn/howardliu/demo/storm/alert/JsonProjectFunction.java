package cn.howardliu.demo.storm.alert;

import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import storm.trident.operation.BaseFunction;
import storm.trident.operation.TridentCollector;
import storm.trident.tuple.TridentTuple;

/**
 * <br/>create at 16-1-25
 *
 * @author liuxh
 * @since 1.0.0
 */
public class JsonProjectFunction extends BaseFunction {
    private Fields fields;

    public JsonProjectFunction(Fields fields) {
        this.fields = fields;
    }

    @Override
    public void execute(TridentTuple tuple, TridentCollector collector) {
        String json = tuple.getString(0);
        JSONObject j = (JSONObject) JSONValue.parse(json);
        if (j == null) {
            return;
        }
        Values values = new Values();
        for (int i = 0; i < this.fields.size(); i++) {
            //noinspection unchecked
            values.add(j.get(this.fields.get(i)));
        }
        collector.emit(values);
    }
}
