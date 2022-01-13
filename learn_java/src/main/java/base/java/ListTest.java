package base.java;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ListTest {
    @Test
    public void testSetOperation(){
        List<String> del = Arrays.asList("1", "2");
        List<String> add = Arrays.asList("2", "3");

        List<String> finalDel = new ArrayList<>();
        List<String> finalAdd = new ArrayList<>();
        finalDel.addAll(del);
        finalDel.removeAll(add);
        finalAdd.addAll(add);
        finalAdd.removeAll(del);

        System.out.println(finalAdd.toString());
        System.out.println(finalDel.toString());
    }

    @Test
    public void testBasic(){
        List<String> oneArray = new ArrayList<>();
        oneArray.add("1");
        oneArray.remove("1");
        oneArray.addAll(Arrays.asList("1", "2"));
        oneArray.get(1);
        System.out.println(oneArray.removeAll(Arrays.asList("3")));
    }

    @Test
    public void testSubList(){
        List<String> sup = Arrays.asList("0", "1", "2", "3", "4");
        List<String> sub = sup.subList(1,3);
        sub.set(1, "9");
        System.out.println(sub);
        System.out.println(sup);
    }

    @Test
    public void testIterator(){
        Map<Long, String> map = new HashMap<>();
        map.put(1L, "1");
        List<Map<Long, String>> list = new ArrayList<>();
        list.add(map);
        list.forEach(each->each.put(1L, "2"));
        System.out.println(list);
    }

    @Test
    public void testRemove(){
        Fruit red = new Fruit("red", 10);
        Fruit yellow = new Fruit("yellow", 20);
        Fruit blue = new Fruit("blue", 30);

        List<Fruit> list = new ArrayList<>();
        list.add(red);
        list.add(yellow);
        list.add(blue);
        Fruit del = null;
        for(int i = list.size() - 1; i >= 0; i--){
            if(i == list.size() -1){
                del = list.get(i);
            }
            if(i == 0){
                list.remove(del);
            }
        }
        list.remove(null);

        list.stream().forEach(a -> System.out.println(a.getColor()));

    }

    @Test
    public void testContain(){
        List<String> s = new ArrayList<>();
        s.add("11230304522");
        System.out.println(s.contains("11230304522"));
        System.out.println(s.contains(11230304522L));
    }

    @Test
    public void isHighIntent(){
        List<String> browseTimes = Arrays.asList("5", null, null, null, null, null, null);
        int browseTotal = 0;
        if(CollectionUtils.isNotEmpty(browseTimes)){
            for(String browseTime : browseTimes){
                browseTotal += StringUtils.isNumeric(browseTime) ? Integer.parseInt(browseTime) : 0;
            }
        }
        System.out.println(browseTotal >= 5);
    }

    @Test
    public void NullTest(){
        List<Object> list = new ArrayList<>();
        list.add(null);
        System.out.println(list.size());
        list.add(null);
        System.out.println(list.size());
        System.out.println(CollectionUtils.isEmpty(list));
    }

    @Test
    public void testToString(){
        List<String> list = new ArrayList<>();
        list.add("11020238412");
        System.out.println("silent:order:predict:sku:" + list);
    }

    @Test
    public void testArray(){
        String[] ab = new String[]{"1", "2", "3"};
        System.out.println(ab[0]);
        System.out.println(ab.length);
    }

    @Test
    public void testIntegerContain(){
        List<Integer> list = new ArrayList<>();
        list.add(new Integer(10000));
        Integer p = new Integer(10000);
        System.out.println(list.contains(p));
    }

    @Test
    public void testLinkedList(){
        LinkedList<Integer> demo = new LinkedList<>();
        demo.add(2);
        demo.add(3);
        demo.addFirst(1);
        for (Integer item : demo) {
            System.out.println(item);
        }
    }
}
