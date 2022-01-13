package base.java;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class ReflectionTest {
    @Test
    public void testClassName() {
        List<String> abc = new ArrayList<>();
        System.out.println(abc.getClass() == ArrayList.class);
    }

}
