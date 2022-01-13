package base.java8;

import org.junit.Test;

public class JDK8InfImpl implements JDK8Inf {
    @Override
    public void normalMethod() {
        System.out.println("normal method in subclass");
    }

    @Test
    public void test1(){
        JDK8Inf impl = new JDK8InfImpl();
        impl.defaultMethod();
        impl.normalMethod();
    }
}
