package com.carpenter.yan.mockito;

import org.junit.Test;
import org.mockito.InOrder;

import java.util.List;

import static org.mockito.Mockito.*;

public class VerifyTest {
    @Test
    public void verifyTest1() {
        List<String> mock = mock(List.class);
        List<String> mock2 = mock(List.class);
        when(mock.get(0)).thenReturn("hello");
        mock.get(0);
        mock.get(1);
        mock.get(2);
        verifyZeroInteractions(mock2);
        mock2.get(0);
        verify(mock).get(2);
        verify(mock, times(1)).get(0);
        verify(mock, atLeast(1)).get(1);
        verify(mock, atMost(1)).get(2);
        verify(mock, never()).get(3);
        //verifyNoMoreInteractions(mock); error
        //verifyZeroInteractions(mock2); error
        //verify(mock, timeout(1).times(2)).get(0); //调用2次1秒超时验证
    }

    @Test
    public void verifyTest2(){
        List<String> firstMock = mock(List.class);
        List<String> secondMock = mock(List.class);
        firstMock.add("was called first");
        firstMock.add("was called first");
        secondMock.add("was called second");
        secondMock.add("was called third");
        InOrder inOrder = inOrder(secondMock, firstMock);
        inOrder.verify(firstMock,times(2)).add("was called first");
        inOrder.verify(secondMock).add("was called second");
        inOrder.verify(secondMock).add("was called third");
        inOrder.verifyNoMoreInteractions();
    }
}
