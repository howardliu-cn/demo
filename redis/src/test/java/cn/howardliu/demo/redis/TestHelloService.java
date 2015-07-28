package cn.howardliu.demo.redis;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * <br/>create at 15-7-28
 *
 * @author liuxh
 * @since 1.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath*:spring.xml")
public class TestHelloService {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    
    @Autowired
    private IHelloService helloService;

    @Test
    public void testGetMessage() {
        //First method execution using key="Josh", not cached
        logger.debug("message: " + helloService.getMessage("Josh"));
        //Second method execution using key="Josh", still not cached
        logger.debug("message: " + helloService.getMessage("Josh"));
        //First method execution using key="Joshua", not cached
        logger.debug("message: " + helloService.getMessage("Joshua"));
        //Second method execution using key="Joshua", cached
        logger.debug("message: " + helloService.getMessage("Joshua"));
        logger.debug("Done.");
    }
}
