package base.java.proxy;

public class TransactionImpl implements  Transaction{
    @Override
    public void beginTransaction(){
        System.out.println("---begin transaction");
    }

    @Override
    public void endTransaction(){
        System.out.println("---end transaction");
    }
}
