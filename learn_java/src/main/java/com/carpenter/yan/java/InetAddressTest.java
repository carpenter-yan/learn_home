package com.carpenter.yan.java;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class InetAddressTest {
    public static void main(String[] args) {
        try {
            getIP();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }
    public static void getIP() throws UnknownHostException {
        InetAddress addr = InetAddress.getLocalHost();
        String ip=addr.getHostAddress(); //获取本机ip
        String hostName=addr.getHostName(); //获取本机计算机名称
        System.out.println(ip);
        System.out.println(hostName);
    }
}
