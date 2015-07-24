package cn.howardliu.demo.annotation.custom;

import java.lang.annotation.*;

/**
 * 水果颜色注解
 * <br/>create at 15-7-24
 *
 * @author liuxh
 * @since 1.0.0
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FruitColor {
    /**
     * 颜色枚举
     *
     * @author peida
     */
    public enum Color {
        BULE, RED, GREEN
    }

    /**
     * 颜色属性
     *
     * @return
     */
    Color fruitColor() default Color.GREEN;
}