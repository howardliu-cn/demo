package cn.howardliu.javassistDemo.clazz;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * <br>created at 17-4-5
 *
 * @author liuxh
 * @since 1.0.0
 */
public class Run implements Move {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void action() throws Exception {
        TimeUnit.SECONDS.sleep(new Random(10).nextInt(5));
        System.out.println("run");
    }
}
