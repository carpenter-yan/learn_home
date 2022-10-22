import org.junit.Test;

/**
 * @author yangang12
 * @version 1.0
 * @date 2022/9/2
 */
public class BankTest {
    @Test
    public  void test1() {
        int begin = 40000;
        double rate = 1.102;
        int fetch = 5000;
        System.out.println(begin);
        for(;;){
            int after = (int) (begin * rate);
            int afterFetch = after - fetch;
            System.out.println(after + "-" + fetch + "="+afterFetch);
            begin = afterFetch;
            if(afterFetch < fetch){
                break;
            }
        }
    }
}
