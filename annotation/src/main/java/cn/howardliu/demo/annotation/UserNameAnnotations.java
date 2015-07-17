package cn.howardliu.demo.annotation;

import java.lang.annotation.*;

/**
 * 定义一个用户名的自定义注解
 * Created by liuxh on 15-7-9.
 */
@Documented // 文档
@Inherited // 子类会继承
@Target({ElementType.TYPE, ElementType.METHOD}) // 作用到类，方法，接口上等
@Retention(RetentionPolicy.RUNTIME) // 在运行时可以获取
public @interface UserNameAnnotations {
    public String value() default ""; //使用的时候 @UserNameAnnotations(value="xxx")
}
