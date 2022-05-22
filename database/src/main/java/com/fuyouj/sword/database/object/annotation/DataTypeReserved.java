package com.fuyouj.sword.database.object.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * 保留序列化的数据类型，className（非全限定名）,
 * 如果需要自定义名称需要再增加<b>@JsonTypeName("{自定义名称}")</b>
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.ANNOTATION_TYPE, ElementType.TYPE, ElementType.FIELD})
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME)
@JacksonAnnotationsInside
public @interface DataTypeReserved {

}
