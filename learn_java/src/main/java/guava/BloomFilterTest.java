package guava;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import org.openjdk.jol.info.ClassLayout;

import java.nio.charset.Charset;

/**
 * @author yangang12
 * @version 1.0
 * @date 2022/6/24
 */
public class BloomFilterTest {

    @Test
    public void test1() {
        BloomFilter bloomFilter = BloomFilter.create(Funnels.stringFunnel(Charset.defaultCharset()),
                50000000, 0.0001);

        for (int i = 0; i < 1000; i++) {
            bloomFilter.put(RandomStringUtils.randomAlphabetic(16));
        }

        System.out.println(ClassLayout.parseInstance(bloomFilter).toPrintable());
    }
}
