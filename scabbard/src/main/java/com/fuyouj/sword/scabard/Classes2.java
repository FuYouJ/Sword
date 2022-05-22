package com.fuyouj.sword.scabard;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Classes2 {
    public static <T extends Annotation> List<Field> findFieldsAnnotatedBy(final Class<?> aClass,
                                                                           final Class<T> annotationClass) {
        Asserts.hasArg(aClass, "itemClass must not null");
        Asserts.hasArg(annotationClass, "annotationClass must not null");

        return Lists2.stream(getAllFields(aClass))
                .filter(field -> field.isAnnotationPresent(annotationClass))
                .collect(Collectors.toList());
    }

    /**
     * 获得某一个Class的所有字段，包括父类继承而来的字段
     *
     * @param aClass
     * @return
     */
    public static List<Field> getAllFields(final Class<?> aClass) {
        if (aClass == Object.class) {
            return Lists2.staticEmpty();
        }

        return Lists2.concat(
                Arrays.asList(aClass.getDeclaredFields()),
                getAllFields(aClass.getSuperclass())
        );
    }

    public static boolean isPrimitive(final Class<?> type) {
        return type == Boolean.class
                || type == Integer.class
                || type == Long.class
                || type == String.class
                || type == Float.class
                || type == Double.class
                || type == Byte.class
                || type == Short.class
                || type == Character.class;
    }
}

