package base.java8;

public class LambdaTest {
    public static void main(String[] args) {
        test1();
    }

    public static void process(Runnable r){
        r.run();
    }

    public static void test1(){
        Runnable r1 = () -> System.out.println("hello world 1");
        Runnable r2 = new Runnable() {
            @Override
            public void run() {
                System.out.println("hello world 2");
            }
        };
        process(r1);
        process(r2);
    }
}
