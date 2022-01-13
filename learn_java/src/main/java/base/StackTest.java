package base;

import org.junit.Test;

public class StackTest {
    @Test
    public void methodA() {
        methodB();
    }

    private void methodB() {
        methodC(1, 2);
    }

    private int methodC(int a, int b) {
        return a + b;
    }
}
