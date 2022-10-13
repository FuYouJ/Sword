package com.fuyouj.sword.database;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fuyouj.sword.database.exception.JsonDeserializeException;
import com.fuyouj.sword.database.object.DataObjectMapperProvider;
import com.fuyouj.sword.scabard.Exceptions2;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JsonSerializerDeserializer implements Serializer, Deserializer {
    private static final Pattern TYPE_EXTRACTOR_PATTERN = Pattern.compile("^\\{\"@t\" *: *\"([^\"]+)\".+");
    private static final String TYPE_ALIAS_KEY = "@t";
    private final DataObjectMapperProvider objectMapperProvider;
    private final ObjectMapper objectMapper;

    public JsonSerializerDeserializer(final DataObjectMapperProvider objectMapperProvider,
                                      final ObjectMapper objectMapper) {
        this.objectMapperProvider = objectMapperProvider;
        this.objectMapper = objectMapper;
    }

    @Override
    public <T> T deserialize(final byte[] serialized, final Class<T> tClass, final boolean throwError) {
        String source = new String(serialized);

        Class<?> classToDeserializeTo = this.selectClassToDeserializeTo(source, tClass);

        try {
            final Object obj = objectMapper.readValue(source, classToDeserializeTo);

            if (tClass.isInstance(obj)) {
                return tClass.cast(obj);
            }

            return null;
        } catch (JsonProcessingException e) {
            String message = String.format(
                    "failed to deserialize item [%s], because of [%s]",
                    source, Exceptions2.extractMessage(e)
            );

            if (throwError) {
                throw new JsonDeserializeException(message, e);
            } else {
                log.warn(message);
                return null;
            }
        }
    }

    @Override
    public <T> T deserialize(final byte[] serialized, final Class<T> tClass) {
        return deserialize(serialized, tClass, false);
    }

    @Override
    public Object deserialize(final byte[] serialized) {
        return deserialize(serialized, Object.class, false);
    }

    @Override
    public Object deserialize(final byte[] serialized, final boolean throwError) {
        return deserialize(serialized, Object.class, throwError);
    }

    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    @Override
    public byte[] serialize(final Object object) {
        try {
            return this.objectMapper.writeValueAsBytes(object);
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    private <T> Class<?> selectClassToDeserializeTo(final String source, final Class<T> tClass) {
        final Matcher matcher = TYPE_EXTRACTOR_PATTERN.matcher(source);
        if (matcher.matches()) {
            String alias = matcher.group(1);

            Class<?> aClass = this.objectMapperProvider.getByAlias(alias);

            if (aClass != null) {
                return aClass;
            }
        }

        try {
            Map map = this.objectMapper.readValue(source, Map.class);

            final Object type = map.get(TYPE_ALIAS_KEY);

            if (!(type instanceof String)) {
                return tClass;
            }

            final Class<?> byAlias = this.objectMapperProvider.getByAlias((String) type);

            if (byAlias != null) {
                return byAlias;
            }

            return tClass;
        } catch (JsonProcessingException e) {
            return tClass;
        }
    }
}
