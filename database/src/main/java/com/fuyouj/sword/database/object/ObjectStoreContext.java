package com.fuyouj.sword.database.object;

import java.lang.reflect.Field;
import java.util.List;

import com.thingworks.jarvis.persistent.exception.Exceptions;
import com.thingworks.jarvis.persistent.exception.ItemIdNotSpecified;
import com.thingworks.jarvis.persistent.exception.MultipleItemIdSpecified;
import com.thingworks.jarvis.persistent.id.Generator;
import com.thingworks.jarvis.persistent.id.Generators;
import com.thingworks.jarvis.persistent.id.Id;
import com.thingworks.jarvis.persistent.object.audit.AuditProvider;

import net.thingworks.jarvis.utils.log.Loggable;
import net.thingworks.jarvis.utils.type.Lists2;

public class ObjectStoreContext implements Loggable {
    private final ObjectUpdater<?> updater;
    private final Field idField;
    private final Generator<?> idGenerator;
    private final AuditContext auditContext;
    private Object latestId;

    public ObjectStoreContext(final Class<?> itemClass) {
        this.updater = new ObjectUpdater<>(itemClass);
        this.idField = this.checkIdField(itemClass);
        this.auditContext = AuditContext.checkAuditContext(itemClass);
        this.idGenerator = Generators.select(this.idField.getType());
    }

    public synchronized void assignNewId(final Object item) {
        try {
            Object nextId = this.idGenerator.next(latestId);
            this.latestId = nextId;
            idField.set(item, nextId);
        } catch (IllegalAccessException e) {
            warn("Failed to assign new id for item of class [{}]", item.getClass());
        }
    }

    public Object getId(final Object item) {
        try {
            return idField.get(item);
        } catch (IllegalAccessException e) {
            return null;
        }
    }

    public boolean hasId(final Object item) {
        return getId(item) != null;
    }

    public <T> T update(final T itemToUpdate, final T itemUpdateFrom) {
        this.updater.referenceUpdate(itemToUpdate, itemUpdateFrom);

        return itemToUpdate;
    }

    public void updateAuditInfo(final Object item, final AuditProvider auditProvider) {
        this.auditContext.audit(item, auditProvider);
    }

    private Field checkIdField(final Class<?> itemClass) {
        final List<Field> idFields = Lists2.empty();
        this.doCheckIdField(idFields, itemClass);

        if (idFields.isEmpty()) {
            throw new ItemIdNotSpecified(String.format(
                    "[%s] did not specify Id field, it should annotate with @Id",
                    itemClass.getName()
            ));
        }

        if (idFields.size() > 1) {
            throw new MultipleItemIdSpecified();
        }

        Field idField = idFields.get(0);

        if (idField.getType() != String.class && idField.getType() != Long.class) {
            throw Exceptions.idMustBeStringOrLong();
        }

        idField.setAccessible(true);

        return idField;
    }

    private void doCheckIdField(final List<Field> idFields, final Class<?> itemClass) {
        if (itemClass == Object.class) {
            return;
        }

        for (Field declaredField : itemClass.getDeclaredFields()) {
            if (declaredField.getAnnotation(Id.class) != null) {
                idFields.add(declaredField);
            }
        }

        this.doCheckIdField(idFields, itemClass.getSuperclass());
    }
}
