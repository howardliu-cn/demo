package cn.howardliu.demo.annotation.custom;

import java.lang.annotation.*;

/**
 * 水果供应者注解
 * <br/>create at 15-7-24
 *
 * @author liuxh
 * @since 1.0.0
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FruitProvider {
    /**
     * 供应商编号
     *
     * @return
     */
    public int id() default -1;

    /**
     * 供应商名称
     *
     * @return
     */
    public String name() default "";

    /**
     * 供应商地址
     *
     * @return
     */
    public String address() default "";
}