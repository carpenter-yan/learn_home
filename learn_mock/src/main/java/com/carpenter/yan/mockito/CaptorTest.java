package com.carpenter.yan.mockito;

import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.util.List;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class CaptorTest {
    @Test
    public void argumentCaptorTest1() {
        List mock = mock(List.class);
        List mock2 = mock(List.class);
        mock.add("John");
        mock2.add("Brian");
        mock2.add("Jim");
        ArgumentCaptor argument = ArgumentCaptor.forClass(String.class);
        verify(mock).add(argument.capture());
        assertEquals("John", argument.getValue());
        verify(mock2, times(2)).add(argument.capture());
        assertEquals("Jim", argument.getValue());
        assertArrayEquals(new Object[]{"John", "Brian","Jim"},argument.getAllValues().toArray());
    }
}
