package cn.howardliu.demo.annotation.custom;

import java.lang.annotation.*;

/**
 * 水果名称注解
 * <br/>create at 15-7-24
 *
 * @author liuxh
 * @since 1.0.0
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FruitName {
    String value() default "";
}