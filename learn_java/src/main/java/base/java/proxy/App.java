package base.java.proxy;

import java.lang.reflect.Proxy;

public class App {
    public static void main(String[] args) {
        SqlExecutor executor = new SqlExecutorImpl();
        Transaction transaction = new TransactionImpl();
        TransactionHander hander = new TransactionHander(executor, transaction);
        SqlExecutor proxy = (SqlExecutor) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), executor.getClass().getInterfaces(), hander);
        proxy.executeSql("select * from dual");
    }
}
