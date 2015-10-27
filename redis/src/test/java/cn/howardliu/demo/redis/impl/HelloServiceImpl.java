package cn.howardliu.demo.redis.impl;

import cn.howardliu.demo.redis.IHelloService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * <br/>create at 15-7-28
 *
 * @author liuxh
 * @since 1.0.0
 */
@Service("helloService")
public class HelloServiceImpl implements IHelloService {
    @Override
    @Cacheable(value = "messageCache", condition = "'Joshua'.equals(#name)")
    public String getMessage(String name) {
        System.out.println("Executing HelloServiceImpl" +
                ".getHelloMessage(\"" + name + "\")");
        return "Hello " + name + "!";
    }
}
