package com.ote.cucumber;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ScenarioContext {

    private final Map<String, Object> context = new HashMap<>();

    public <T> void put(String key, T object) {
        context.put(key, object);
    }

    public <T> T getOneByKey(String key, Class<T> objectClass) {
        return objectClass.cast(getOneByKey(key));
    }

    public Object getOneByKey(String key) {
        return context.get(key);
    }

    public void clear() {
        context.clear();
    }

    public <T> Optional<T> getOneByPredicate(Predicate<Map.Entry<String, Object>> predicate, Class<T> objectClass) {
        return context.entrySet().
                stream().
                filter(predicate).
                filter(entry -> objectClass.isInstance(entry.getValue())).
                map(entry -> objectClass.cast(entry.getValue())).
                findAny();
    }

    public <T> List<T> getManyByPredicate(Predicate<Map.Entry<String, Object>> predicate, Class<T> objectClass) {
        return context.entrySet().
                stream().
                filter(predicate).
                filter(entry -> objectClass.isInstance(entry.getValue())).
                map(entry -> objectClass.cast(entry.getValue())).
                collect(Collectors.toList());
    }

}