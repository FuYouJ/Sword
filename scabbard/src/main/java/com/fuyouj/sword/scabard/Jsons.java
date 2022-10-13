package com.fuyouj.sword.scabard;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fuyouj.sword.scabard.error.ErrorCode;

public abstract class Jsons {

    public static final ErrorCode INVALID_JSON = new InvalidJson();
    private static final ObjectMapper DEFAULT_OBJECT_MAPPER;

    static {
        ObjectMapper objectMapper = objectMapperTemplate();

        DEFAULT_OBJECT_MAPPER = objectMapper;
    }

    public static ObjectMapper buildObjectMapper() {
        return objectMapperTemplate();
    }

    public static <T> T convert(final Object source, final Class<T> targetType) {
        String jsonString = toJsonString(source);
        return toJavaObject(jsonString, targetType);
    }

    public static <T> T convert(final Object source, final Class<T> targetType, final ObjectMapper objectMapper) {
        String jsonString = toJsonString(source, objectMapper);
        return toJavaObject(jsonString, targetType, objectMapper);
    }

    public static ObjectMapper getDefaultObjectMapper() {
        return DEFAULT_OBJECT_MAPPER;
    }

    public static boolean isValidJson(final String jsonString) {
        if (jsonString == null || jsonString.isEmpty()) {
            return false;
        }

        try {
            getDefaultObjectMapper().readTree(jsonString);

            return true;
        } catch (IOException e) {
            //ignore
        }

        return false;
    }

    public static String printPrettyJsonString(final Object obj) {
        try {
            return "\n" + Jsons.getDefaultObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(obj) + "\n";
        } catch (Exception e) {
            return toJsonString(obj);
        }
    }

    public static <T> T toJavaObject(final String jsonString, final Class<T> type, final ObjectMapper objectMapper) {
        Asserts.hasArg(jsonString, "parameter 'jsonString' is required");
        Asserts.hasArg(type, "parameter 'type' is required");
        Asserts.hasArg(objectMapper, "parameter 'objectMapper' is required");

        try {
            return objectMapper.readValue(jsonString, type);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T toJavaObject(final InputStream inputStream, final Class<T> type) {
        return toJavaObject(inputStream, type, getDefaultObjectMapper());
    }

    public static <T> T toJavaObject(final InputStream inputStream, final Class<T> type, final ObjectMapper objectMapper) {
        Asserts.hasArg(inputStream, "parameter 'inputStream' is required");
        Asserts.hasArg(type, "parameter 'type' is required");
        Asserts.hasArg(objectMapper, "parameter 'objectMapper' is required");

        try {
            return objectMapper.readValue(inputStream, type);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T toJavaObject(final String jsonString, final Class<T> type) {
        return toJavaObject(jsonString, type, getDefaultObjectMapper());
    }

    public static String toJsonString(final Object obj,
                                      final ObjectMapper objectMapper,
                                      final boolean throwException) throws JsonProcessingException {
        if (obj == null) {
            return null;
        }

        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            if (throwException) {
                throw e;
            } else {
                return obj.toString();
            }
        }
    }

    public static String toJsonString(final Object obj, final boolean throwException) throws JsonProcessingException {
        return toJsonString(obj, Jsons.getDefaultObjectMapper(), throwException);
    }

    public static String toJsonString(final Object obj, final ObjectMapper objectMapper) {
        if (obj == null) {
            return null;
        }

        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            return obj.toString();
        }
    }

    public static String toJsonString(final Object obj) {
        return toJsonString(obj, Jsons.getDefaultObjectMapper());
    }

    public static Map<String, Object> toMap(final String jsonString) {
        //noinspection unchecked
        return toJavaObject(jsonString, Map.class);
    }

    public static Map<String, Object> toMap(final String jsonString, final ObjectMapper objectMapper) {
        //noinspection unchecked
        return toJavaObject(jsonString, Map.class, objectMapper);
    }

    private static ObjectMapper objectMapperTemplate() {
        ObjectMapper objectMapper = new ObjectMapper();

        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        objectMapper.registerModule(new JavaTimeModule());

        return objectMapper;
    }

    private static class InvalidJson implements ErrorCode {

        @Override
        public String name() {
            return "invalid json";
        }
    }
}

