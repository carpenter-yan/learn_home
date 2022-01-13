package mock.mockito;

import org.junit.Test;

import java.util.Iterator;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class SimpleTest {
    @Test
    public void simpleTest1(){
        Iterator i=mock(Iterator.class, "simple1");
        when(i.next()).thenReturn("Hello").thenReturn("World");
        String result=i.next()+" "+i.next();
        verify(i, times(2)).next();
        assertEquals("Hello World", result);
        String outStubResult = i.next() + "";
        assertEquals("World", outStubResult);
    }

    @Test
    public void SimpleTest2(){
        Iterator i = mock(Iterator.class, "simple2");
        when(i.next()).thenReturn("Hello", "World").thenThrow(new RuntimeException());
        String result = i.next() + " " + i.next();
        assertEquals("Hello World", result);
    }

    @Test
    public void SimpleTest3(){
        Iterator i = mock(Iterator.class, "simple3");
        doReturn("Hello").doReturn("World").when(i).next();
        String result = i.next() + " " + i.next();
        assertEquals("Hello World", result);
    }
}
