package mock.powermock;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.File;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(PowerMockRunner.class)
public class SampleTest {
    @Test
    public void testCallArgumentInstance() {
        //Mock参数传递的对象
        File file = PowerMockito.mock(File.class);
        ClassUnderTest underTest = new ClassUnderTest();
        PowerMockito.when(file.exists()).thenReturn(true);
        assertTrue(underTest.callArgumentInstance(file));
    }

    @Test
    @PrepareForTest(ClassUnderTest.class)
    public void testCallInternalInstance() throws Exception {
        //Mock方法内部new出来的对象
        //注解里写的类是需要mock的new对象代码所在的类
        File file = PowerMockito.mock(File.class);
        ClassUnderTest underTest = new ClassUnderTest();
        PowerMockito.whenNew(File.class).withArguments("bbb").thenReturn(file);
        PowerMockito.when(file.exists()).thenReturn(true);
        assertTrue(underTest.callInternalInstance("bbb"));
    }

    @Test
    @PrepareForTest(ClassDependency.class)
    public void testCallFinalMethod() {
        //Mock普通对象的final方法
        //注解里写的类是需要mock的final方法所在的类
        ClassDependency depencency = PowerMockito.mock(ClassDependency.class);
        ClassUnderTest underTest = new ClassUnderTest();
        PowerMockito.when(depencency.isAlive()).thenReturn(true);
        assertTrue(underTest.callFinalMethod(depencency));
    }

    @Test
    @PrepareForTest(ClassDependency.class)
    public void testCallStaticMethod() {
        //Mock静态方法
        //注解里写的类是需要mock的静态方法所在的类
        ClassUnderTest underTest = new ClassUnderTest();
        PowerMockito.mockStatic(ClassDependency.class);
        PowerMockito.when(ClassDependency.isExist()).thenReturn(true);
        assertTrue(underTest.callStaticMethod());
    }

    @Test
    @PrepareForTest(ClassUnderTest.class)
    public void testCallSystemStaticMethod() {
        //Mock JDK中类的静态方法   final方法同testCallFinalMethod
        //只不过注解里写的类不一样，注解里写的类是需要调用系统方法所在的类
        ClassUnderTest underTest = new ClassUnderTest();
        PowerMockito.mockStatic(System.class);
        PowerMockito.when(System.getProperty("aaa")).thenReturn("bbb");
        assertEquals("bbb", underTest.callJDKStaticMethod("aaa"));
    }

    @Test
    @PrepareForTest(ClassUnderTest.class)
    public void testCallPrivateMethod() throws Exception {
        ClassUnderTest underTest = PowerMockito.mock(ClassUnderTest.class);
        PowerMockito.when(underTest.callPrivateMethod()).thenCallRealMethod();
        PowerMockito.when(underTest, "isExist").thenReturn(true);
        assertTrue(underTest.callPrivateMethod());
    }
}
