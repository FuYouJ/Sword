package com.fuyouj.sword.database;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fuyouj.sword.database.object.DataObjectMapperProvider;
import com.fuyouj.sword.database.utils.JacksonFactory;
import com.fuyouj.sword.database.wal.Command;
import com.fuyouj.sword.scabard.Exceptions2;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JsonSerializerDeserializer implements Serializer<Command>, Deserializer {
    private static final Pattern TYPE_EXTRACTOR_PATTERN = Pattern.compile("^\\{\"@type\":\"([^\"]+)\".+");
    private static final String TYPE_ALIAS_KEY = "@type";
    private final DataObjectMapperProvider objectMapperProvider;
    private final ObjectMapper objectMapper;

    public JsonSerializerDeserializer(final DataObjectMapperProvider objectMapperProvider) {
        this.objectMapperProvider = objectMapperProvider;
        this.objectMapper = JacksonFactory.createDefault();
    }

    @Override
    public <T> T deserialize(final byte[] serialized, final Class<T> tClass) {
        String source = new String(serialized);

        Class<?> classToDeserializeTo = this.selectClassToDeserializeTo(serialized, tClass);

        try {
            final Object obj = objectMapper.readValue(source, classToDeserializeTo);

            if (tClass.isInstance(obj)) {
                return tClass.cast(obj);
            }

            return null;
        } catch (JsonProcessingException e) {
            log.warn("failed to deserialize item [{}], because of [{}]", source, Exceptions2.extractMessage(e));
            return null;
        }
    }

    @Override
    public Object deserialize(final byte[] serialized) {
        return deserialize(serialized, Object.class);
    }

    @Override
    public byte[] serialize(final Command object) {
        try {
            return this.objectMapper.writeValueAsBytes(object);
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    private <T> Class<?> selectClassToDeserializeTo(final byte[] serialized, final Class<T> tClass) {
        final String source = new String(serialized);
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
