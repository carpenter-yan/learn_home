package com.carpenter.yan.java;

import org.junit.Test;

public class StringTest {
    public static void main(String[] args) {
        testStringFormat();
    }

    public static void testSplit() {
        String src = "a|||b";
        String[] dest = src.split("\\|\\|\\|");
        for(String each : dest){
            System.out.println(each);
        }
    }

    public static void testUUIDHashCode(){
        String uuid = "dd280e0b1e4b2f4e89603946824ba1a4";
        System.out.println(uuid.substring(uuid.length()-4).hashCode() % 100);
    }

    public static void testStringFormat(){
        System.out.println(String.format("%d", 1009));
    }

    public static void testToChar(){
        String uuid = "dd280e0b1e4b2f4e89603946824ba1a4";
        char[] uuidArray = uuid.toCharArray();
        for(int i = uuidArray.length -1; i >=20; i--){
            System.out.println((int)uuidArray[i]);
        }
    }

    public static void testIntern(){
        String s1 = "abc";
        String s2 = "abc";
        System.out.println(s1 == s2);
        String s3 = s1.intern();
        String s4 = s2.intern();
        System.out.println(s3 == s4);
    }

    public static void testHashCode(){
        System.out.println(Math.abs("JIMI_3276670118A34EBC96736805184734C6".hashCode()%100));
    }

    @Test
    public void testEquals(){
        String a = "123";
        Long b = 123L;
        System.out.println(a.equals(String.valueOf(b)));
    }

    @Test
    public void test111(){
        System.out.println("\\\"" + ":" + "\\\\\"");
    }

    @Test
    public void testSubStr(){
        StringBuilder sb = new StringBuilder("123,456,");
        if(sb.length()>1){
            sb.deleteCharAt(sb.length()-1);
        }
        System.out.println(sb.toString());
    }

    @Test
    public void testFinal(){
        final char[] array = {'1', '2', '3'};
        array[0] = '4';
        System.out.println(array);
        String a= "123";
        StringBuilder sb = new StringBuilder("123");
        sb.append("45");
    }

    @Test
    public void testFormat(){
        System.out.println(String.format("%.2f", 0.0));
        System.out.println(String.format("%.2f", 0.10));
        System.out.println(String.format("%.2f", 0.01));
        System.out.println(String.format("%.2f", 1.012));
    }

    @Test
    public void testTrans(){
        String ori = "<p><a href=\\\"https://item.jd.com/100010799910.html\\\" target=\\\"_self\\\" class=\\\"J_Link\\";
        System.out.println(ori);

        if(ori.contains("href") && ori.contains("http")){
            System.out.println("1111");
        }

        String fin = ori.replaceAll("\\\"","\\\\\"");
        System.out.println(fin);
    }

    @Test
    public void testHash(){
        String a = "阎刚";
        System.out.println((a.hashCode() % 100 + 100) % 100);
        a = "jd_92831234";
        System.out.println((a.hashCode() % 100 + 100) % 100);
    }

    @Test
    public void testDouble(){
        Double d = Double.valueOf("100");
        System.out.print(d == 100.0);
    }

    @Test
    public void testCompareTo(){
        String a = "9.3.0";
        String b = "8.5.12-5.2";
        System.out.println(b);
        System.out.println(a.compareTo(b.substring(0, a.length())));
    }

    @Test
    public void testContain(){
        System.out.println("小米售后技能组".contains("售后"));
    }
}
