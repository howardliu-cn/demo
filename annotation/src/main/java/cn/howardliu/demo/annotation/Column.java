package cn.howardliu.demo.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <br/>create at 15-7-23
 *
 * @author liuxh
 * @since 1.0.0
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Column {
    public String name() default "fieldName";

    public String setFuncName() default "setField";

    public String getFuncName() default "getField";

    public boolean defaultDBValue() default false;
}
