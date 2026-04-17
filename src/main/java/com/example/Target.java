package com.example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

public class Target {
    private final List<String> items = new ArrayList<>();
    private final HashMap<String, String> map = new HashMap<>();
    private final HashSet<String> set = new HashSet<>();
    private final LinkedList<String> queue = new LinkedList<>();

    public void add(String item) {
        items.add(item);
        map.put(item, item.toUpperCase());
        set.add(item);
        queue.addLast(item);
    }
}
