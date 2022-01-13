package base.java;

import org.junit.Test;

import java.util.Arrays;

public class ArrayTest {
    @Test
    public void test1() {
        String[] a1 = new String[]{"1", "2"};
        Arrays.stream(a1).forEach(System.out::println);
    }

    @Test
    public void test2() {
        String aaa = "[{\"chn\":\"DD\",\"bot\":\"74\",\"sku\":\"45786966810\",\"typ\":\"3\",\"pin\":\"jd_6695250d30fac\",\"ord\":\"232062141559\"},{\"chn\":\"SMS\",\"bot\":\"80\",\"sku\":\"10036346102365\",\"typ\":\"4\",\"pin\":\"jd_IhBQCOmRmwbb\",\"ord\":\"231806724125\"}]";
        System.out.println(aaa.length());
    }
}
