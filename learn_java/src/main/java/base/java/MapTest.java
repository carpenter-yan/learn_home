package base.java;

import org.junit.Test;

import java.util.*;

public class MapTest {
    static final int MAXIMUM_CAPACITY = 1 << 30;

    public static void main(String[] args) {
        hash(12);
    }

    public static void testValuseMethod() {
        Map<Integer, String> englishNumber = new HashMap<>();
        englishNumber.put(1, "one");

        List<String> english = new ArrayList<>(englishNumber.values());

        System.out.println(english.get(0));

        for (Map.Entry<Integer, String> entry : englishNumber.entrySet()) {
            entry.getKey();
        }
    }

    public static void testContain() {
        Map<String, String> map = new HashMap<>();
        System.out.println(map.containsKey(null));
        System.out.println(map.containsValue(null));

    }

    public static final int tableSizeFor(int cap) {
        int n = cap - 1;
        n |= n >>> 1;
        n |= n >>> 2;
        n |= n >>> 4;
        n |= n >>> 8;
        n |= n >>> 16;
        return (n < 0) ? 1 : (n >= MAXIMUM_CAPACITY) ? MAXIMUM_CAPACITY : n + 1;
    }

    static final int hash(int key) {
        int h;
        System.out.println((h = key) ^ (h >>> 16));
        return 0;
    }

    @Test
    public void putNull() {
        Map<String, String> map = new HashMap<>();
    }

    @Test
    public void testLinkedHashMap() {
        LinkedHashMap map1 = new LinkedHashMap();
        map1.put("1", "1");
        map1.put("2", "2");
        LinkedHashMap map2 = new LinkedHashMap();
        map2.put("3", "3");
        map2.put("4", "4");
        map1.putAll(map2);

        map1.forEach((k, v) -> {
            System.out.println(v);
        });
    }
}
