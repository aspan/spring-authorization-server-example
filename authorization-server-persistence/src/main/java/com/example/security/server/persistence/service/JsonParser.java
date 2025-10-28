package com.example.security.server.persistence.service;

import java.util.Map;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

import tools.jackson.databind.json.JsonMapper;

@Service
public class JsonParser {
    private final JsonMapper jacksonJsonMapper;

    public JsonParser(JsonMapper jacksonJsonMapper) {
        this.jacksonJsonMapper = jacksonJsonMapper;
    }

    public Map<String, Object> parseMap(String data) {
        if (data == null || data.isBlank()) {
            return Map.of();
        }
        try {
            final var typeReference = new ParameterizedTypeReference<Map<String, Object>>() {
            };
            var javaType = this.jacksonJsonMapper.getTypeFactory()
                                                 .constructType(typeReference.getType());
            return this.jacksonJsonMapper.readValue(data, javaType);
        } catch (Exception ex) {
            throw new IllegalArgumentException(ex.getMessage(), ex);
        }
    }

    public String writeMap(Map<String, Object> data) {
        if (data == null || data.isEmpty()) {
            return null;
        }
        try {
            return this.jacksonJsonMapper.writeValueAsString(data);
        } catch (Exception ex) {
            throw new IllegalArgumentException(ex.getMessage(), ex);
        }
    }
}
