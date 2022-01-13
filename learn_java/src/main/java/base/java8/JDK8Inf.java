package base.java8;

public interface JDK8Inf {
    static void staticMethod() {
        System.out.println("static method");
    }

    default void defaultMethod() {
        System.out.println("default method");
    }

    void normalMethod();
}
