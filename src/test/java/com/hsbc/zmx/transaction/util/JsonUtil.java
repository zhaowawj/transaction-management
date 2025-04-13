package com.hsbc.zmx.transaction.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JsonUtil {

    private static final ObjectMapper om = new ObjectMapper();

    static {
        om.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        om.registerModule(new JavaTimeModule());
        om.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        om.configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, true);
    }

    public static String objectToJson(Object object) {
        try {
            return om.writeValueAsString(object);
        } catch (Exception ex) {
            log.error("fail to convert object to json: ", ex);
            return "";
        }
    }

    public static <T> T jsonToObject(String json, Class<T> clz) {
        try {
            return om.readValue(json, clz);
        } catch (Exception ex) {
            log.error("fail to convert json to object: ", ex);
            return null;
        }
    }

    public static <T> T jsonToObject(String json, TypeReference<T> typeReference) {
        try {
            return om.readValue(json, typeReference);
        } catch (Exception ex) {
            log.error("fail to convert json to object: ", ex);
            return null;
        }
    }
}
