package cn.howardliu.javassistDemo.clazz;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <br>created at 17-4-5
 *
 * @author liuxh
 * @since 1.0.0
 */
public final class Walk implements Move {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void action() throws Exception {
        System.out.println("walk");
    }
}
