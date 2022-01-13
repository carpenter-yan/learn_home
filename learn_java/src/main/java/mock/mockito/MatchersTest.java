package mock.mockito;

import org.junit.Test;
import org.mockito.ArgumentMatcher;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

public class MatchersTest {
    @Test
    public void argumentMatchersTest1(){
        List<String> mock = mock(List.class);
        when(mock.get(anyInt())).thenReturn("Hello").thenReturn("World");
        String result=mock.get(100)+" "+mock.get(200);
        verify(mock,times(2)).get(anyInt());
        assertEquals("Hello World",result);
    }

    @Test
    public void argumentMatchersTest2(){
        Map mapMock = mock(Map.class);
        when(mapMock.put(anyInt(), anyString())).thenReturn("world");
        mapMock.put(1, "hello");
        //verify(mapMock).put(anyInt(), "hello");  error
        verify(mapMock).put(1, "hello");
        verify(mapMock).put(anyInt(), anyString());
        verify(mapMock).put(eq(1), eq("hello"));
    }

    private class TwoArgumentMatcher implements ArgumentMatcher<List> {
        public boolean matches(List argument) {
            return argument.size() == 2;
        }
    }

    @Test
    public void argumentMatchersTest3(){
        List<String> mock = mock(List.class);
        when(mock.addAll(argThat(new TwoArgumentMatcher()))).thenReturn(true);
        assertTrue(mock.addAll(Arrays.asList("1", "2")));
        verify(mock).addAll(argThat(new TwoArgumentMatcher()));
    }
}
