package base.java;

import org.junit.Test;

import java.math.BigDecimal;

public class BigDecimalTest {
    public static void main(String[] args) {
        testOpr();
    }

    public static void testNew() {
        double d1 = new BigDecimal("0.012").doubleValue();
        double d2 = BigDecimal.valueOf(0.012).doubleValue();
        double d3 = new BigDecimal(0.012).doubleValue();
        System.out.println(d1 == d2);
        System.out.println(d1 == d3);
        System.out.println(d2 == d3);
    }

    public static void testOpr() {
        BigDecimal b1 = BigDecimal.valueOf(0.05);
        BigDecimal b2 = BigDecimal.valueOf(0.01);
        System.out.println(b1.divide(b2));
    }

    @Test
    public void test1() {
        for (Bank bank : Bank.values()) {
            BigDecimal rate = new BigDecimal(bank.getRate());
            BigDecimal year = new BigDecimal(bank.getYear());

            BigDecimal result = rate.multiply(year).add(new BigDecimal("1")).pow(bank.getPow());
            System.out.println(bank.name() + ":" + result.setScale(3, BigDecimal.ROUND_HALF_UP));
        }
    }

    private static enum Bank {
        YS_5_6("5", "0.039", 6),
        Y_5_6("5", "0.037", 6),
        Y_3_10("3", "0.034", 10),
        Y_2_15("2", "0.0285", 15),
        Y_1_30("1", "0.0225", 30),
        M_6_60("0.5", "0.0205", 60),
        M_3_120("0.25", "0.0185", 120),
        Y_3_6("3", "0.034", 4),
        ;
        private String year;
        private String rate;
        private int pow;

        Bank(String year, String rate, int pow) {
            this.year = year;
            this.rate = rate;
            this.pow = pow;
        }

        public String getYear() {
            return year;
        }

        public String getRate() {
            return rate;
        }

        public int getPow() {
            return pow;
        }
    }
}
