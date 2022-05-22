package com.fuyouj.sword.database.object.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * 所有需要被持久化的对象，需要增加@DataObject的Annotation，在运行时系统会自动扫描指定的包，
 * 并自动注册到持久层中
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME)
@JacksonAnnotationsInside
public @interface DataObject {
    String collection();
}
