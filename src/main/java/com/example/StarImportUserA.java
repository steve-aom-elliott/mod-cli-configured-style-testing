package com.example;

import java.util.*;

public class StarImportUserA {
    private final List<String> items = new ArrayList<>();
    private final Map<String, Object> cache = new HashMap<>();
    private final Set<String> keys = new HashSet<>();

    public Optional<String> find(String key) {
        return Optional.ofNullable(cache.get(key)).map(Object::toString);
    }

    public Collection<String> all() {
        return Collections.unmodifiableList(items);
    }
}
