package com.carpenter.yan.clean;

public class GeneratePrimes {
    private static boolean[] crossOut;
    private static int[] result;

    public static void main(String[] args) {
        int[] primes = generatePrimes(10);
        for(int eachPrime : primes){
            System.out.println(eachPrime);
        }
    }

    public static int[] generatePrimes(int maxValue) {
        if (maxValue > 2) {

            uncrossIntegersUpTo(maxValue);
            crossOutMultiples();
            putUncrossedIntegersIntoResult();
            return result;
        } else {
            return new int[0];
        }
    }

    private static void putUncrossedIntegersIntoResult() {
        int count = 0;
        for (int i = 0; i < crossOut.length; i++) {
            if (crossOut[i]) {
                count++;
            }
        }

        result = new int[count];
        for (int i = 0, j = 0; i < crossOut.length; i++) {
            if (crossOut[i]) {
                result[j++] = i;
            }
        }
    }

    private static void crossOutMultiples() {
        for (int i = 2; i < Math.sqrt(crossOut.length) + 1; i++) {
            if (crossOut[i]) {
                for (int j = 2 * i; j < crossOut.length; j += i) {
                    crossOut[j] = false;
                }
            }
        }
    }

    private static void uncrossIntegersUpTo(int maxValue) {
        crossOut = new boolean[maxValue + 1];
        for (int i = 2; i < crossOut.length; i++) {
            crossOut[i] = true;
        }
        crossOut[0] = crossOut[1] = false;
    }
}
