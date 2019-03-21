package com.carpenter.yan.powermock;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.lang.reflect.Method;

@RunWith(PowerMockRunner.class)
public class MutiPriMethodClassTest {
    @Test
    @PrepareForTest({MutiPriMethodClass.class})
    public void testStringReport() throws Exception {
        MutiPriMethodClass testObj = new MutiPriMethodClass();
        Method method = testObj.getClass().getDeclaredMethod("stringReport");
        method.setAccessible(true);

        MutiPriMethodClass testObj2 = PowerMockito.spy(testObj);
        PowerMockito.when(testObj2, "content").thenReturn("mock");

        String result = (String) method.invoke(testObj2);
        System.out.println(result);
    }
}
