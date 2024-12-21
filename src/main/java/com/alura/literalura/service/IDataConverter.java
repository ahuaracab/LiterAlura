package com.alura.literalura.service;

public interface IDataConverter {
    <T> T fetchData(String json, Class<T> clazz);
}