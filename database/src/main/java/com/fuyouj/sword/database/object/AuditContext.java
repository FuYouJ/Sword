package com.fuyouj.sword.database.object;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.fuyouj.sword.database.exception.AuditingTypeMissMatch;
import com.fuyouj.sword.database.object.annotation.DataObject;
import com.fuyouj.sword.database.object.audit.AuditProvider;
import com.fuyouj.sword.database.object.audit.CreatedBy;
import com.fuyouj.sword.database.object.audit.CreatedTime;
import com.fuyouj.sword.database.object.audit.LastModifiedBy;
import com.fuyouj.sword.database.object.audit.LastModifiedTime;
import com.fuyouj.sword.scabard.Classes2;
import com.fuyouj.sword.scabard.DateTimes2;
import com.fuyouj.sword.scabard.Exceptions2;
import com.fuyouj.sword.scabard.Lists2;
import com.fuyouj.sword.scabard.Maps2;
import com.fuyouj.sword.scabard.Objects2;

import lombok.extern.slf4j.Slf4j;

@Slf4j
class AuditContext{
    private static final Map<Class<?>, AuditTimeConverter<?>> MAPPED_CONVERTERS = Maps2.of(
            String.class, (AuditTimeConverter<String>) DateTimes2::format,
            Long.class, (AuditTimeConverter<Long>) DateTimes2::toMills,
            LocalDateTime.class, (AuditTimeConverter<LocalDateTime>) now -> now,
            ZonedDateTime.class, (AuditTimeConverter<ZonedDateTime>) now -> DateTimes2.toZoned(now).orElse(null)
    );

    private final List<Field> createdByFields;
    private final List<Field> createdTimeFields;
    private final List<Field> lastModifiedByFields;
    private final List<Field> lastModifiedTimeFields;

    private AuditContext(final List<Field> createdByFields,
                         final List<Field> createdTimeFields,
                         final List<Field> lastModifiedByFields,
                         final List<Field> lastModifiedTimeFields) {
        this.createdByFields = createdByFields;
        this.createdTimeFields = createdTimeFields;
        this.lastModifiedByFields = lastModifiedByFields;
        this.lastModifiedTimeFields = lastModifiedTimeFields;
    }

    public static AuditContext checkAuditContext(final Class<?> itemClass) {
        final DataObject dataObject = itemClass.getAnnotation(DataObject.class);

        List<Field> lastModifiedTimeFields = Classes2.findFieldsAnnotatedBy(itemClass, LastModifiedTime.class);
        List<Field> lastModifiedByFields = Classes2.findFieldsAnnotatedBy(itemClass, LastModifiedBy.class);
        List<Field> createdByFields = Classes2.findFieldsAnnotatedBy(itemClass, CreatedBy.class);
        List<Field> createdTimeFields = Classes2.findFieldsAnnotatedBy(itemClass, CreatedTime.class);

        setAccessible(lastModifiedByFields);
        setAccessible(lastModifiedTimeFields);
        setAccessible(createdByFields);
        setAccessible(createdTimeFields);

        expectedType(lastModifiedByFields, String.class);
        expectedType(createdByFields, String.class);
        expectedType(lastModifiedTimeFields, String.class, Long.class, LocalDateTime.class, ZonedDateTime.class);
        expectedType(createdTimeFields, String.class, Long.class, LocalDateTime.class, ZonedDateTime.class);

        return new AuditContext(
                createdByFields,
                createdTimeFields,
                lastModifiedByFields,
                lastModifiedTimeFields);
    }

    public void audit(final Object item, final AuditProvider auditProvider) {
        auditCreatedBy(item, auditProvider);
        auditCreatedTime(item, auditProvider);
        auditModifiedBy(item, auditProvider);
        auditModifiedTime(item, auditProvider);
    }

    private static void expectedType(final List<Field> fields, final Class<?>... classes) {
        for (Field field : fields) {
            boolean matched = false;
            for (Class<?> aClass : classes) {
                if (field.getType() == aClass) {
                    matched = true;
                    break;
                }
            }

            if (!matched) {
                throw new AuditingTypeMissMatch(String.format(
                        "field [%s] expect to be type one of %s",
                        field.getName(),
                        Objects2.asJsonString(Lists2.map(Arrays.asList(classes), Class::getName))
                ));
            }
        }
    }

    private static void setAccessible(final List<Field> fields) {
        for (Field field : fields) {
            field.setAccessible(true);
        }
    }

    private void auditCreatedBy(final Object item, final AuditProvider auditProvider) {
        for (Field field : createdByFields) {
            try {
                if (field.get(item) == null) {
                    field.set(item, auditProvider.getAuditor());
                }
            } catch (IllegalAccessException e) {
                log.debug("failed to update field [{}], because of [{}]",
                        field.getName(),
                        Exceptions2.extractMessage(e));
            }
        }
    }

    private void auditCreatedTime(final Object item, final AuditProvider auditProvider) {
        for (Field field : createdTimeFields) {
            try {
                if (field.get(item) == null) {
                    Object auditTime = MAPPED_CONVERTERS.get(field.getType()).convert(auditProvider.getNow());
                    field.set(item, auditTime);
                }
            } catch (IllegalAccessException e) {
                log.debug("failed to update field [{}], because of [{}]",
                        field.getName(),
                        Exceptions2.extractMessage(e));
            }
        }
    }

    private void auditModifiedBy(final Object item, final AuditProvider auditProvider) {
        for (Field field : lastModifiedByFields) {
            try {
                field.set(item, auditProvider.getAuditor());
            } catch (IllegalAccessException e) {
                log.debug("failed to update field [{}], because of [{}]",
                        field.getName(),
                        Exceptions2.extractMessage(e));
            }
        }
    }

    private void auditModifiedTime(final Object item, final AuditProvider auditProvider) {
        for (Field field : lastModifiedTimeFields) {
            try {
                field.set(item, MAPPED_CONVERTERS.get(field.getType()).convert(auditProvider.getNow()));
            } catch (IllegalAccessException e) {
                log.debug("failed to update field [{}], because of [{}]",
                        field.getName(),
                        Exceptions2.extractMessage(e));
            }
        }
    }

    interface AuditTimeConverter<T> {
        T convert(LocalDateTime now);
    }
}
