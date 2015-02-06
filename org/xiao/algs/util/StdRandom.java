package org.xiao.algs.util;

import java.util.Random;
import org.xiao.algs.io.StdOut;

/***
 * 
 * 随机数静态方法库
 * 
 * @author XiaoJian
 *
 */
public final class StdRandom {

    private static Random random;   
    private static long seed;       

    static {
        seed = System.currentTimeMillis();
        random = new Random(seed);
    }

    private StdRandom() { }

    /**
     * 设置随机生成器的种子
     */
    public static void setSeed(long s) {
        seed   = s;
        random = new Random(seed);
    }

    /**
     * 得到种子
     */
    public static long getSeed() {
        return seed;
    }

    /**
     * 返回 0 到 1 之间实数 [0,1)
     */
    public static double uniform() {
        return random.nextDouble();
    }

    /**
     * 返回 0 到 N-1 之间整数 [0,N-1]
     */
    public static int uniform(int N) {
        if (N <= 0) throw new IllegalArgumentException("Parameter N must be positive");
        return random.nextInt(N);
    }

    /**
     * 返回 0 到 1 之间实数 [0,1)
     */
    public static double random() {
        return uniform();
    }

    /**
     * 返回 a 到 b 之间整数 [a, b)
     */
    public static int uniform(int a, int b) {
        if (b <= a) throw new IllegalArgumentException("Invalid range");
        if ((long) b - a >= Integer.MAX_VALUE) throw new IllegalArgumentException("Invalid range");
        return a + uniform(b - a);
    }

    /**
     * 返回 a 到 b 之间实数 [a, b).
     */
    public static double uniform(double a, double b) {
        if (!(a < b)) throw new IllegalArgumentException("Invalid range");
        return a + uniform() * (b-a);
    }

    /**
     * 返回真的概率为p
     */
    public static boolean bernoulli(double p) {
        if (!(p >= 0.0 && p <= 1.0))
            throw new IllegalArgumentException("Probability must be between 0.0 and 1.0");
        return uniform() < p;
    }

    /**
     * 返回真的概率为0.5
     */
    public static boolean bernoulli() {
        return bernoulli(0.5);
    }

    /**
     * 正态分布，期望值为0，标准差为1
     */
    public static double gaussian() {
        // use the polar form of the Box-Muller transform
        double r, x, y;
        do {
            x = uniform(-1.0, 1.0);
            y = uniform(-1.0, 1.0);
            r = x*x + y*y;
        } while (r >= 1 || r == 0);
        return x * Math.sqrt(-2 * Math.log(r) / r);

        // Remark:  y * Math.sqrt(-2 * Math.log(r) / r)
        // is an independent random gaussian
    }

    /**
     * 正态分布，期望值为mean，标准差为stddev
     */
    public static double gaussian(double mean, double stddev) {
        return mean + stddev * gaussian();
    }

    /**
     * Returns an integer with a geometric distribution with mean 1/p.
     */
    public static int geometric(double p) {
        if (!(p >= 0.0 && p <= 1.0))
            throw new IllegalArgumentException("Probability must be between 0.0 and 1.0");
        // using algorithm given by Knuth
        return (int) Math.ceil(Math.log(uniform()) / Math.log(1.0 - p));
    }

    /**
     * Return an integer with a Poisson distribution with mean lambda.
     */
    public static int poisson(double lambda) {
        if (!(lambda > 0.0))
            throw new IllegalArgumentException("Parameter lambda must be positive");
        if (Double.isInfinite(lambda))
            throw new IllegalArgumentException("Parameter lambda must not be infinite");
        // using algorithm given by Knuth
        // see http://en.wikipedia.org/wiki/Poisson_distribution
        int k = 0;
        double p = 1.0;
        double L = Math.exp(-lambda);
        do {
            k++;
            p *= uniform();
        } while (p >= L);
        return k-1;
    }

    /**
     * Returns a real number with a Pareto distribution with parameter alpha.
     */
    public static double pareto(double alpha) {
        if (!(alpha > 0.0))
            throw new IllegalArgumentException("Shape parameter alpha must be positive");
        return Math.pow(1 - uniform(), -1.0/alpha) - 1.0;
    }

    /**
     * Returns a real number with a Cauchy distribution.
     */
    public static double cauchy() {
        return Math.tan(Math.PI * (uniform() - 0.5));
    }

    /**
     * Returns a number from a discrete distribution: i with probability a[i].
     * throws IllegalArgumentException if sum of array entries is not (very nearly) equal to <tt>1.0</tt>
     * throws IllegalArgumentException unless <tt>a[i] >= 0.0</tt> for each index <tt>i</tt>
     */
    public static int discrete(double[] a) {
        double EPSILON = 1E-14;
        double sum = 0.0;
        for (int i = 0; i < a.length; i++) {
            if (!(a[i] >= 0.0)) throw new IllegalArgumentException("array entry " + i + " must be nonnegative: " + a[i]);
            sum = sum + a[i];
        }
        if (sum > 1.0 + EPSILON || sum < 1.0 - EPSILON)
            throw new IllegalArgumentException("sum of array entries does not approximately equal 1.0: " + sum);

        // the for loop may not return a value when both r is (nearly) 1.0 and when the
        // cumulative sum is less than 1.0 (as a result of floating-point roundoff error)
        while (true) {
            double r = uniform();
            sum = 0.0;
            for (int i = 0; i < a.length; i++) {
                sum = sum + a[i];
                if (sum > r) return i;
            }
        }
    }

    /**
     * Returns a real number from an exponential distribution with rate lambda.
     * @throws IllegalArgumentException unless <tt>lambda > 0.0</tt>
     */
    public static double exp(double lambda) {
        if (!(lambda > 0.0))
            throw new IllegalArgumentException("Rate lambda must be positive");
        return -Math.log(1 - uniform()) / lambda;
    }

    /**
     * 将数组a随机排序
     */
    public static void shuffle(Object[] a) {
        int N = a.length;
        for (int i = 0; i < N; i++) {
            int r = i + uniform(N-i);     // between i and N-1
            Object temp = a[i];
            a[i] = a[r];
            a[r] = temp;
        }
    }

    /**
     * 将数组a随机排序
     */
    public static void shuffle(double[] a) {
        int N = a.length;
        for (int i = 0; i < N; i++) {
            int r = i + uniform(N-i);     // between i and N-1
            double temp = a[i];
            a[i] = a[r];
            a[r] = temp;
        }
    }

    /**
     * 将数组a随机排序
     */
    public static void shuffle(int[] a) {
        int N = a.length;
        for (int i = 0; i < N; i++) {
            int r = i + uniform(N-i);     // between i and N-1
            int temp = a[i];
            a[i] = a[r];
            a[r] = temp;
        }
    }


    /**
     * 将数组 a[lo..hi] 随机排序 
     */
    public static void shuffle(Object[] a, int lo, int hi) {
        if (lo < 0 || lo > hi || hi >= a.length) {
            throw new IndexOutOfBoundsException("Illegal subarray range");
        }
        for (int i = lo; i <= hi; i++) {
            int r = i + uniform(hi-i+1);     // between i and hi
            Object temp = a[i];
            a[i] = a[r];
            a[r] = temp;
        }
    }

    /**
     * 将数组 a[lo..hi] 随机排序 
     */
    public static void shuffle(double[] a, int lo, int hi) {
        if (lo < 0 || lo > hi || hi >= a.length) {
            throw new IndexOutOfBoundsException("Illegal subarray range");
        }
        for (int i = lo; i <= hi; i++) {
            int r = i + uniform(hi-i+1);     // between i and hi
            double temp = a[i];
            a[i] = a[r];
            a[r] = temp;
        }
    }

    /**
     * 将数组 a[lo..hi] 随机排序 
     */
    public static void shuffle(int[] a, int lo, int hi) {
        if (lo < 0 || lo > hi || hi >= a.length) {
            throw new IndexOutOfBoundsException("Illegal subarray range");
        }
        for (int i = lo; i <= hi; i++) {
            int r = i + uniform(hi-i+1);     // between i and hi
            int temp = a[i];
            a[i] = a[r];
            a[r] = temp;
        }
    }

    /**
     * 测试
     */
    public static void main(String[] args) {
        int N = Integer.parseInt(args[0]);
        if (args.length == 2) StdRandom.setSeed(Long.parseLong(args[1]));
        double[] t = { .5, .3, .1, .1 };

        StdOut.println("seed = " + StdRandom.getSeed());
        for (int i = 0; i < N; i++) {
            StdOut.printf("%2d "  , uniform(100));
            StdOut.printf("%8.5f ", uniform(10.0, 99.0));
            StdOut.printf("%5b "  , bernoulli(.5));
            StdOut.printf("%7.5f ", gaussian(9.0, .2));
            StdOut.printf("%2d "  , discrete(t));
            StdOut.println();
        }

        String[] a = "A B C D E F G".split(" ");
        for (String s : a)
            StdOut.print(s + " ");
        StdOut.println();
    }

}
