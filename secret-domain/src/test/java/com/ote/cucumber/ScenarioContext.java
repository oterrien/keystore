package com.ote.cucumber;

import java.util.HashMap;
import java.util.Map;

public class ScenarioContext {

    private final Map<String, Object> context = new HashMap<>();

    public <T> void put(String key, T object) {
        context.put(key, object);
    }

    public <T> T get(String key, Class<T> objectClass) {
        return objectClass.cast(get(key));
    }

    public Object get(String key) {
        return context.get(key);
    }

    public void clear() {
        context.clear();
    }
}