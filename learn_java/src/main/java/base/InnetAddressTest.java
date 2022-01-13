package base;

import org.junit.Test;

import java.net.InetAddress;

import static java.lang.System.out;

public class InnetAddressTest {
    @Test
    public void test1() throws Exception {
        InetAddress ip = InetAddress.getByName("www.baidu.com");
        out.println("is reachable:" + ip.isReachable(2000));
        out.println("host name:" + ip.getCanonicalHostName());
        out.println("host name:" + ip.getHostName());
        out.println("host name:" + ip.getHostAddress());
        InetAddress local = InetAddress.getByAddress(new byte[]{127, 0, 0, 1});
        out.println("is reachable:" + local.isReachable(2000));
        out.println("host name:" + local.getCanonicalHostName());
    }
}
