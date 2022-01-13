package commons;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class StringUtilsTest {
    public static void main(String[] args) {
        testJoin();
    }
    public static void testIsNumber(){
        System.out.println(StringUtils.isAlphanumeric("1234_b"));
    }

    public static void testRemoveStart(){
        String var = "JIMI_3276670118A34EBC96736805184734C6";
        if(StringUtils.startsWith(var, "JIMI_")){
            var = StringUtils.removeStart(var, "JIMI_");
        }
        System.out.println(var);
    }

    public static void testJoin() {
        List<String> list = new ArrayList<>();
        list.add(null);
        list.add(null);
        System.out.println(StringUtils.join(list, ","));
    }

}
