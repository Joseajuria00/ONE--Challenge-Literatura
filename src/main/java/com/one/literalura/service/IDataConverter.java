package com.one.literalura.service;

public interface IDataConverter {
    <T> T obtenerDatos(String json, Class<T> clase);
}
