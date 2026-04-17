package com.example;

import java.util.*;

public class StarImportUserB {
    private final Deque<String> stack = new ArrayDeque<>();
    private final Queue<Integer> queue = new LinkedList<>();
    private final NavigableSet<Object> sorted = new TreeSet<>();

    public Iterator<String> iter() {
        return stack.iterator();
    }
}
