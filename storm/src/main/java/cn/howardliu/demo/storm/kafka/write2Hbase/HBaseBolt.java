package cn.howardliu.demo.storm.kafka.write2Hbase;

import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Tuple;
import org.apache.hadoop.hbase.client.Durability;
import org.apache.hadoop.hbase.client.Mutation;
import org.apache.storm.hbase.bolt.AbstractHBaseBolt;
import org.apache.storm.hbase.bolt.mapper.HBaseMapper;
import org.apache.storm.hbase.common.ColumnList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * <br/>create at 16-1-27
 *
 * @author liuxh
 * @since 1.0.0
 */
public class HBaseBolt extends AbstractHBaseBolt {
    private static final Logger logger = LoggerFactory.getLogger(HBaseBolt.class);

    boolean writeToWAL = true;

    public HBaseBolt(String tableName, HBaseMapper mapper) {
        super(tableName, mapper);
    }

    public HBaseBolt writeToWAL(boolean writeToWAL) {
        this.writeToWAL = writeToWAL;
        return this;
    }

    public HBaseBolt withConfigKey(String configKey) {
        this.configKey = configKey;
        return this;
    }

    @Override
    public void execute(Tuple tuple) {
        logger.info("get word {}", tuple.getValue(0));
        logger.info("get count {}", tuple.getValue(1));

        try {
            byte[] rowKey = this.mapper.rowKey(tuple);
            logger.info("rowKey = {}", new String(rowKey, "UTF-8"));
            ColumnList cols = this.mapper.columns(tuple);
            logger.info("cols = {}", cols);
            List<Mutation> mutations = hBaseClient.constructMutationReq(rowKey, cols, writeToWAL? Durability.SYNC_WAL : Durability.SKIP_WAL);
            logger.info("mutations = {}", mutations);
            this.hBaseClient.batchMutate(mutations);
            logger.info("write successfully");
        } catch(Exception e){
            this.collector.reportError(e);
            this.collector.fail(tuple);
            logger.error("向hbase写数据异常", e);
            return;
        }
        this.collector.ack(tuple);
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
    }
}
