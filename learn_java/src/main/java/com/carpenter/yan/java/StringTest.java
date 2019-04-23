package com.carpenter.yan.java;

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
        int suffix = 9;
        System.out.println(String.format("%02d", 19));
    }

    public static void testToChar(){
        String uuid = "dd280e0b1e4b2f4e89603946824ba1a4";
        char[] uuidArray = uuid.toCharArray();
        for(int i = uuidArray.length -1; i >=20; i--){
            System.out.println((int)uuidArray[i]);
        }
    }
}
