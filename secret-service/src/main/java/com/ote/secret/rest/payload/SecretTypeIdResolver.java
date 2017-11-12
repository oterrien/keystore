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
        return aClass.getSimpleName();
    }

    @Override
    public JavaType typeFromId(DatabindContext databindContext, String s) {
        String packageName = this.getClass().getPackage().getName();
        String className = packageName + "." + s;
        try {
            return TypeFactory.defaultInstance().constructType(ClassLoader.getSystemClassLoader().loadClass(className));
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Unable to find class: " + s, e);
        }
    }

    @Override
    public String getDescForKnownTypeIds() {
        return null;
    }
}
