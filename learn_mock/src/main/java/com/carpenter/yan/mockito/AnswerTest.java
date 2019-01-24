package com.carpenter.yan.mockito;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.List;

import static org.mockito.Mockito.*;

public class AnswerTest {

    private class CustomerAnswer implements Answer<String>{
        public String answer(InvocationOnMock invocation) throws Throwable {
            Object[] args = invocation.getArguments();
            Integer num = (Integer) args[0];
            if(num > 3){
                return "yes";
            }else{
                throw new RuntimeException();
            }
        }
    }

    @Test
    public void answerTest1(){
        List<String> mock = mock(List.class);
        when(mock.get(4)).thenAnswer(new CustomerAnswer());//也可用匿名内部类实现
        //doAnswer(new CustomerAnswer()).when(mock).get(4);
        Assert.assertEquals("yes", mock.get(4));
    }
}
