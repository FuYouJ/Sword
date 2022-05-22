package com.fuyouj.sword.database.object.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 将属性从序列化的清单中移除
 */

@Retention(RetentionPolicy.RUNTIME)
@JacksonAnnotationsInside
@JsonIgnore
public @interface DataPropIgnored {

}
