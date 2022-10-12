package com.fuyouj.sword.database.object;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import com.fuyouj.sword.database.exception.Exceptions;
import com.fuyouj.sword.scabard.Asserts;

public class ObjectUpdater<T> {
    private final Class<T> tClass;
    private final List<Field> fields;

    public ObjectUpdater(final Class<T> tClass) {
        this.tClass = tClass;
        this.fields = new ArrayList<>();
        this.init(this.tClass);
    }

    public void referenceUpdate(final Object objectToUpdate, final Object objectUpdateFrom) {
        checkUpdatable(objectToUpdate);
        checkUpdatable(objectUpdateFrom);

        for (Field field : fields) {
            try {
                field.set(objectToUpdate, field.get(objectUpdateFrom));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    private void checkUpdatable(final Object obj) {
        Asserts.hasArg(obj, "object must NOT be null to update");
        if (!tClass.isAssignableFrom(obj.getClass())) {
            throw Exceptions.objectMustAssignableToBeAbleToUpdate();
        }
    }

    private void init(final Class<?> tClass) {
        if (tClass == Object.class) {
            return;
        }

        Field[] declaredFields = tClass.getDeclaredFields();
        for (Field declaredField : declaredFields) {
            boolean aStatic = Modifier.isStatic(declaredField.getModifiers());
            if (!aStatic) {
                declaredField.setAccessible(true);
                this.fields.add(declaredField);
            }
        }

        init(tClass.getSuperclass());
    }
}
