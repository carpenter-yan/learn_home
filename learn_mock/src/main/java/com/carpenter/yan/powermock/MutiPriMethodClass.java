package com.carpenter.yan.powermock;

public class MutiPriMethodClass {

    public String report(){
        return "report:" + stringReport();
    }

    private String stringReport(){
        return "string:" + content();
    }

    private String content(){
        return "content";
    }
}
