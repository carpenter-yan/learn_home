package com.carpenter.yan.java;

import java.util.LinkedHashMap;
import java.util.Map;

public class LRU <K, V> extends LinkedHashMap<K, V> {
    public static final int MAX_ENTRY = 4;
    @Override
    public boolean removeEldestEntry(Map.Entry<K,V> eldest) {
        return size() > MAX_ENTRY;
    }

    LRU(){
        super(MAX_ENTRY, 0.75f, true);
    }

    public static void main(String[] args) {
        LRU<String, String> lru = new LRU<>();
        lru.put("1", "1");
        lru.put("2", "2");
        lru.put("3", "3");
        lru.put("4", "4");
        lru.put("5", "5");
        System.out.println(lru.keySet());
    }
}
