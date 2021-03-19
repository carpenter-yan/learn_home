package com.carpenter.yan;

public class ThreadTest {
    private static Thread t;
    public static void main(String[] args) {
        for(int i = 0; i < 100; i++){
            if(t== null ||!t.isAlive()){
                t = new Thread(()-> innerThread());
                t.start();
            }
            System.out.println(i);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void innerThread(){
        System.out.println("innerThread is running");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
