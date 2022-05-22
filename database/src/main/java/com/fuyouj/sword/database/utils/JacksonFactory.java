package com.fuyouj.sword.database.utils;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class JacksonFactory {
    public static final ObjectMapper DEFAULT = createDefault();

    public static ObjectMapper createDefault() {
        final ObjectMapper objectMapper = new ObjectMapper();

        configUnknownAndNullProperties(objectMapper);
        configWhoShouldSerialized(objectMapper);
        handleJavaTime(objectMapper);

        return objectMapper;
    }

    private static void configUnknownAndNullProperties(final ObjectMapper objectMapper) {
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    private static void configWhoShouldSerialized(final ObjectMapper objectMapper) {
        objectMapper.disable(MapperFeature.AUTO_DETECT_GETTERS);
        objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
    }

    private static void handleJavaTime(final ObjectMapper objectMapper) {
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.registerModule(new JavaTimeModule());
    }
}
