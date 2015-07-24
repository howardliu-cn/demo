package cn.howardliu.demo.annotation.description;

import java.lang.annotation.*;

/**
 * Column注解的的RetentionPolicy的属性值是 RUNTIME,这样注解处理器可以通过反射，
 * 获取到该注解的属性值，从而去做一些运行时的逻辑处理
 * <br/>create at 15-7-23
 *
 * @author liuxh
 * @since 1.0.0
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Column {
    public String name() default "fieldName";

    public String setFuncName() default "setField";

    public String getFuncName() default "getField";

    public boolean defaultDBValue() default false;
}
