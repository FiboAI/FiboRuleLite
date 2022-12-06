package com.fibo.rule.server.utils;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.json.JsonReadFeature;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonObjectFormatVisitor;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.PropertyWriter;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;

import java.io.IOException;

public final class JacksonUtils {
    //filter beans&ignores
    private final static FilterProvider icePropertyFilter = new SimpleFilterProvider().addFilter("icePropertyFilter", new IcePropertyFilter());

    private static ObjectMapper mapper() {
        return JsonMapper.builder()
                .serializationInclusion(JsonInclude.Include.NON_NULL)
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .configure(JsonGenerator.Feature.IGNORE_UNKNOWN, true)
                .configure(JsonParser.Feature.ALLOW_COMMENTS, true)
                .configure(JsonReadFeature.ALLOW_MISSING_VALUES.mappedFeature(), true)
                .configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true)
                .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
                .build();
    }

    private static ObjectMapper withNullMapper() {
        return JsonMapper.builder()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .configure(JsonGenerator.Feature.IGNORE_UNKNOWN, true)
                .configure(JsonParser.Feature.ALLOW_COMMENTS, true)
                .configure(JsonReadFeature.ALLOW_MISSING_VALUES.mappedFeature(), true)
                .configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true)
                .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
                .build();
    }

    public static String toJsonString(Object obj) {
        try {
            return mapper().writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            //ignore
        }
        return null;
    }

    public static String toJsonStringWithIceFilter(Object obj) {
        try {
            return withNullMapper().setFilterProvider(icePropertyFilter).writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            //ignore
        }
        return null;
    }

    public static byte[] toJsonBytes(Object obj) {
        try {
            return mapper().writeValueAsBytes(obj);
        } catch (JsonProcessingException e) {
            //ignore
        }
        return null;
    }

    public static <T> T readJson(String json, Class<T> clazz) throws JsonProcessingException {
        return mapper().readValue(json, clazz);
    }

    public static <T> T readJsonBytes(byte[] jsonBytes, Class<T> clazz) throws IOException {
        return mapper().readValue(jsonBytes, clazz);
    }

    public static JsonNode readTree(String json) {
        if (json == null || json.equals("{}")) {
            return null;
        }
        try {
            return mapper().readTree(json);
        } catch (Exception e) {
            //ignore
            return null;
        }
    }

    public static boolean isJson(String json) {
        try {
            mapper().readTree(json);
            return true;
        } catch (Exception e) {
            //ignore
            return false;
        }
    }

    public static boolean isJsonObject(String json) {
        try {
            JsonNode node = mapper().readTree(json);
            return node.isObject();
        } catch (Exception e) {
            //ignore
            return false;
        }
    }

    public static boolean isJsonArray(String json) {
        try {
            JsonNode node = mapper().readTree(json);
            return node.isArray();
        } catch (Exception e) {
            //ignore
            return false;
        }
    }
    
    public static class IcePropertyFilter extends SimpleBeanPropertyFilter {

//        public boolean include(PropertyWriter writer) {
//            return writer.getAnnotation(IceIgnore.class) == null && !IceBeanUtils.containsBean(writer.getName());
//        }

        @Override
        public void serializeAsField(Object pojo, JsonGenerator gen, SerializerProvider prov, PropertyWriter writer) throws Exception {
            if (include(writer)) {
                writer.serializeAsField(pojo, gen, prov);
            }
        }

        @Override
        public void serializeAsElement(Object elementValue, JsonGenerator gen, SerializerProvider prov, PropertyWriter writer) throws Exception {
            if (include(writer)) {
                writer.serializeAsElement(elementValue, gen, prov);
            }
        }

        @Override
        public void depositSchemaProperty(PropertyWriter writer, ObjectNode propertiesNode, SerializerProvider provider) throws JsonMappingException {
//            if (!IceBeanUtils.containsBean(writer.getName())) {
//                writer.depositSchemaProperty(propertiesNode, provider);
//            }
        }

        @Override
        public void depositSchemaProperty(PropertyWriter writer, JsonObjectFormatVisitor objectVisitor, SerializerProvider provider) throws JsonMappingException {
            if (include(writer)) {
                writer.depositSchemaProperty(objectVisitor, provider);
            }
        }
    }
}