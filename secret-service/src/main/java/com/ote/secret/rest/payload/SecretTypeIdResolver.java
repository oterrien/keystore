package com.ote.secret.rest.payload;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.DatabindContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class SecretTypeIdResolver implements TypeIdResolver {

    private JavaType baseType;

    @Override
    public void init(JavaType baseType) {
        this.baseType = baseType;
    }

    @Override
    public JsonTypeInfo.Id getMechanism() {
        return JsonTypeInfo.Id.CUSTOM;
    }

    @Override
    public String idFromBaseType() {
        return idFromValueAndType(null, baseType.getRawClass());
    }

    @Override
    public String idFromValue(Object obj) {
        return idFromValueAndType(obj, obj.getClass());
    }

    @Override
    public String idFromValueAndType(Object obj, Class<?> aClass) {
        return SecretType.of(aClass).getType();
    }

    @Override
    public JavaType typeFromId(DatabindContext databindContext, String type) {
        return TypeFactory.defaultInstance().constructType(SecretType.of(type).getClazz());
    }

    @Override
    public String getDescForKnownTypeIds() {
        return null;
    }
}
