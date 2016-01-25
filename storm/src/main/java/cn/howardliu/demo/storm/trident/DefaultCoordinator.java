package cn.howardliu.demo.storm.trident;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import storm.trident.spout.ITridentSpout;

import java.io.Serializable;

/**
 * <br/>create at 16-1-25
 *
 * @author liuxh
 * @since 1.0.0
 */
public class DefaultCoordinator implements ITridentSpout.BatchCoordinator<Long>, Serializable {
    private static final Logger logger = LoggerFactory.getLogger(DefaultCoordinator.class);

    @Override
    public Long initializeTransaction(long txid, Long prevMetadata, Long currMetadata) {
        logger.info("Initializing Transaction [{}]", txid);
        return null;
    }

    @Override
    public void success(long txid) {
        logger.info("Successful Transaction [{}]", txid);
    }

    @Override
    public boolean isReady(long txid) {
        return true;
    }

    @Override
    public void close() {
    }
}
