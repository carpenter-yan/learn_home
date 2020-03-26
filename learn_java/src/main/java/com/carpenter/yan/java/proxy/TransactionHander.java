package com.carpenter.yan.java.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class TransactionHander implements InvocationHandler {
    private Object target;
    private Transaction transaction;

    public TransactionHander(Object target, Transaction transaction) {
        this.target = target;
        this.transaction = transaction;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        transaction.beginTransaction();
        method.invoke(target, args);
        transaction.endTransaction();
        System.out.println(proxy.getClass());
        return proxy;

    }
}
