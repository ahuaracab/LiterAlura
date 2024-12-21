package com.alura.literalura.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class DataConverter implements IDataConverter {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public <T> T fetchData(String json, Class<T> clazz) {
        try {
            return objectMapper.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error al convertir JSON a " + clazz.getSimpleName(), e);
        }
    }


}