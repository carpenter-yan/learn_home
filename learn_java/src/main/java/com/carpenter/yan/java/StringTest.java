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
}
