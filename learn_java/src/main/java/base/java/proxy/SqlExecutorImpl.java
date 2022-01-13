package base.java.proxy;

public class SqlExecutorImpl implements SqlExecutor{
    @Override
    public void executeSql(String sql) {
        System.out.println("execute sql:" + sql);
    }
}
