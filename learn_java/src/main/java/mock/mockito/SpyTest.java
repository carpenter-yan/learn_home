package mock.mockito;

import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class SpyTest {
    @Test
    public void spyTest1() {
        List list = new LinkedList();
        List spy = spy(list);
        //optionally, you can stub out some methods:
        when(spy.size()).thenReturn(100);//建议用doXXX方法保证不会调用到真实对象
        doReturn(100).when(spy).size();
        //using the spy calls real methods
        spy.add("one");
        spy.add("two");
        //prints "one" - the first element of a list
        assertEquals("one", spy.get(0));
        //size() method was stubbed - 100 is printed
        assertEquals(100, spy.size());
        //optionally, you can verify
        verify(spy).add("one");
        verify(spy).add("two");
    }


}
