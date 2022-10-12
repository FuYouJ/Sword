package com.fuyouj.sword.database.object.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Documented
@Repeatable(UniqueIndexes.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueIndex {
    String[] value();

    /**
     * 只对单个的uniqueValue生效
     *
     * @return
     */
    boolean sparse() default true;
}
