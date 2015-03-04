package org.xiao.algs.string;

import java.util.Arrays;

import org.xiao.algs.io.StdIn;
import org.xiao.algs.io.StdOut;

/***
 * 
 * 后缀数组（初级实现）
 * 
 * 可以解决最长重复子字符串问题
 *  
 * @author XiaoJian
 *
 */
public class SuffixArray {
    private Suffix[] suffixes;

    /**
     * 初始化
     */
    public SuffixArray(String text) {
        int N = text.length();
        this.suffixes = new Suffix[N];
        for (int i = 0; i < N; i++)
            suffixes[i] = new Suffix(text, i);
        Arrays.sort(suffixes);
    }

    private static class Suffix implements Comparable<Suffix> {
        private final String text;
        private final int index;

        private Suffix(String text, int index) {
            this.text = text;
            this.index = index;
        }
        private int length() {
            return text.length() - index;
        }
        private char charAt(int i) {
            return text.charAt(index + i);
        }

        public int compareTo(Suffix that) {
            if (this == that) return 0;  // optimization
            int N = Math.min(this.length(), that.length());
            for (int i = 0; i < N; i++) {
                if (this.charAt(i) < that.charAt(i)) return -1;
                if (this.charAt(i) > that.charAt(i)) return +1;
            }
            return this.length() - that.length();
        }

        public String toString() {
            return text.substring(index);
        }
    }

    /**
     * 返回文本txt的长度
     */
    public int length() {
        return suffixes.length;
    }


    /**
     * select(i)的索引
     */
    public int index(int i) {
        if (i < 0 || i >= suffixes.length) throw new IndexOutOfBoundsException();
        return suffixes[i].index;
    }


    /**
     * 返回select（i）和select（i-1）的最长公共前缀的长度
     */
    public int lcp(int i) {
        if (i < 1 || i >= suffixes.length) throw new IndexOutOfBoundsException();
        return lcp(suffixes[i], suffixes[i-1]);
    }

    // 返回两个字符串最长的公共前缀
    private static int lcp(Suffix s, Suffix t) {
        int N = Math.min(s.length(), t.length());
        for (int i = 0; i < N; i++) {
            if (s.charAt(i) != t.charAt(i)) return i;
        }
        return N;
    }

    /**
     * 后缀数组的第i个元素
     */
    public String select(int i) {
        if (i < 0 || i >= suffixes.length) throw new IndexOutOfBoundsException();
         return suffixes[i].toString();
    }

    /**
     * 小于键key的后缀数量
     */
    public int rank(String query) {
        int lo = 0, hi = suffixes.length - 1;
        while (lo <= hi) {
            int mid = lo + (hi - lo) / 2;
            int cmp = compare(query, suffixes[mid]);
            if (cmp < 0) hi = mid - 1;
            else if (cmp > 0) lo = mid + 1;
            else return mid;
        }
        return lo;
    }

    // compare query string to suffix
    private static int compare(String query, Suffix suffix) {
        int N = Math.min(query.length(), suffix.length());
        for (int i = 0; i < N; i++) {
            if (query.charAt(i) < suffix.charAt(i)) return -1;
            if (query.charAt(i) > suffix.charAt(i)) return +1;
        }
        return query.length() - suffix.length();
    }

    /**
     *  测试
     * 
     *  java SuffixArray < abra.txt
	 *    i ind lcp rnk  select
	 *   ---------------------------
	 *    0  11   -   0  "!"
	 *    1  10   0   1  "A!"
	 *    2   7   1   2  "ABRA!"
	 *    3   0   4   3  "ABRACADABRA!"
	 *    4   3   1   4  "ACADABRA!"
	 *    5   5   1   5  "ADABRA!"
	 *    6   8   0   6  "BRA!"
	 *    7   1   3   7  "BRACADABRA!"
	 *    8   4   0   8  "CADABRA!"
	 *    9   6   0   9  "DABRA!"
	 *   10   9   0  10  "RA!"
	 *   11   2   2  11  "RACADABRA!"
     */
    public static void main(String[] args) {
        String s = StdIn.readAll().replaceAll("\\s+", " ").trim();
        SuffixArray suffix = new SuffixArray(s);

        // StdOut.println("rank(" + args[0] + ") = " + suffix.rank(args[0]));

        StdOut.println("  i ind lcp rnk select");
        StdOut.println("---------------------------");

        for (int i = 0; i < s.length(); i++) {
            int index = suffix.index(i);
            String ith = "\"" + s.substring(index, Math.min(index + 50, s.length())) + "\"";
            assert s.substring(index).equals(suffix.select(i));
            int rank = suffix.rank(s.substring(index));
            if (i == 0) {
                StdOut.printf("%3d %3d %3s %3d %s\n", i, index, "-", rank, ith);
            }
            else {
                int lcp = suffix.lcp(i);
                StdOut.printf("%3d %3d %3d %3d %s\n", i, index, lcp, rank, ith);
            }
        }
    }

}
