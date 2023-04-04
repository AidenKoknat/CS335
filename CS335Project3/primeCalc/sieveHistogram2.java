import java.io.*;
class sieveHistogram2 {
    static boolean[] prime = new boolean [1000000];
    static {
        boolean[] prime = new boolean [1000000];
        for (int i = 2; i < 1000000; i++) {
            prime[i] = true;
        }
        int k = 2;
        for (int i = 2; i < 1000000; i++) {
            k = 2; // multiplier
            while ( ((i * k) < prime.length) && (prime[i] == true) ) {
                prime[i * k] = false;
                k++;
            }
        }
    }

    public static boolean primeChecker(int number) {
        return(prime[number]);
    }

    public static int sieveHistogram2(boolean prime[], int start, int finish) {
        int i;
        int sum = 0;
        for (i = start; i <= finish; i++) {
            if (prime[i] == true) {
                sum++;
            }
        }
        return sum;
    }

    public static void main(String[] args) {

        int sum = 0;
        int j;
        for (j = 2; j < 1000000; j++) {
            //System.out.printf("%b\n", prime[j]);
            if (prime[j] == true) {
                sum++;
            }
        }
        /*
        System.out.println("The CS335 Prime Number Histogram (# of Primes in each interval)");
        System.out.printf("2-99,999: %d\n", sieveHistogram(prime, 2, 99999));
        System.out.printf("100,000 - 199,999: %d\n", sieveHistogram(prime, 100000, 199999));

        System.out.printf("200,000 - 299,999: %d\n", sieveHistogram(prime, 200000, 299999));
        System.out.printf("300,000 - 399,999: %d\n", sieveHistogram(prime, 300000, 399999));
        System.out.printf("400,000 - 499,999: %d\n", sieveHistogram(prime, 400000, 499999));
        System.out.printf("500,000 - 599,999: %d\n", sieveHistogram(prime, 500000, 599999));
        System.out.printf("600,000 - 699,999: %d\n", sieveHistogram(prime, 600000, 699999));
        System.out.printf("700,000 - 799,999: %d\n", sieveHistogram(prime, 700000, 799999));
        System.out.printf("800,000 - 899,999: %d\n", sieveHistogram(prime, 800000, 899999));
        System.out.printf("900,000 - 999,999: %d\n", sieveHistogram(prime, 900000, 999999));

         */
    }
}


