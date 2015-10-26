package cn.howardliu.demo.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.support.AbstractCacheManager;

import java.util.Collection;

/**
 * <br/>create at 15-10-26
 *
 * @author liuxh
 * @since 1.0.0
 */
public class LettueClusterCacheManager extends AbstractCacheManager {
    private static final Logger logger = LoggerFactory.getLogger(LettueClusterCacheManager.class);

    @Override
    protected Collection<? extends Cache> loadCaches() {
        return null;
    }
}
