package org.xiao.algs.string;

import org.xiao.algs.io.StdIn;
import org.xiao.algs.io.StdOut;
/***
 * 
 * 后缀数组（改进版）
 * 
 * 可以保证线性时间内解决最长重复子字符串问题
 * 
 * @author XiaoJian
 *
 */
public class SuffixArrayX {
    private static final int CUTOFF =  5;   // cutoff to insertion sort (any value between 0 and 12)

    private final char[] text;
    private final int[] index;   // index[i] = j means text.substring(j) is ith largest suffix
    private final int N;         // number of characters in text

    /**
     * 初始化
     */
    public SuffixArrayX(String text) {
        N = text.length();
        text = text + '\0';
        this.text = text.toCharArray();
        this.index = new int[N];
        for (int i = 0; i < N; i++)
            index[i] = i;

        // shuffle

        sort(0, N-1, 0);
    }

    // 3-way string quicksort lo..hi starting at dth character
    private void sort(int lo, int hi, int d) { 

        // cutoff to insertion sort for small subarrays
        if (hi <= lo + CUTOFF) {
            insertion(lo, hi, d);
            return;
        }

        int lt = lo, gt = hi;
        char v = text[index[lo] + d];
        int i = lo + 1;
        while (i <= gt) {
            int t = text[index[i] + d];
            if      (t < v) exch(lt++, i++);
            else if (t > v) exch(i, gt--);
            else            i++;
        }

        // a[lo..lt-1] < v = a[lt..gt] < a[gt+1..hi]. 
        sort(lo, lt-1, d);
        if (v > 0) sort(lt, gt, d+1);
        sort(gt+1, hi, d);
    }

    // sort from a[lo] to a[hi], starting at the dth character
    private void insertion(int lo, int hi, int d) {
        for (int i = lo; i <= hi; i++)
            for (int j = i; j > lo && less(index[j], index[j-1], d); j--)
                exch(j, j-1);
    }

    // is text[i+d..N) < text[j+d..N) ?
    private boolean less(int i, int j, int d) {
        if (i == j) return false;
        i = i + d;
        j = j + d;
        while (i < N && j < N) {
            if (text[i] < text[j]) return true;
            if (text[i] > text[j]) return false;
            i++;
            j++;
        }
        return i > j;
    }

    // exchange index[i] and index[j]
    private void exch(int i, int j) {
        int swap = index[i];
        index[i] = index[j];
        index[j] = swap;
    }

    /**
     * 返回文本txt的长度
     */
    public int length() {
        return N;
    }


    /**
     * select(i)的索引
     */
    public int index(int i) {
        if (i < 0 || i >= N) throw new IndexOutOfBoundsException();
        return index[i];
    }

    /**
     * 返回select（i）和select（i-1）的最长公共前缀的长度
     */
    public int lcp(int i) {
        if (i < 1 || i >= N) throw new IndexOutOfBoundsException();
        return lcp(index[i], index[i-1]);
    }

    // longest common prefix of text[i..N) and text[j..N)
    private int lcp(int i, int j) {
        int length = 0;
        while (i < N && j < N) {
            if (text[i] != text[j]) return length;
            i++;
            j++;
            length++;
        }
        return length;
    }

    /**
     * 后缀数组的第i个元素
     */
    public String select(int i) {
        if (i < 0 || i >= N) throw new IndexOutOfBoundsException();
        return new String(text, index[i], N - index[i]);
    }

    /**
     * 小于键key的后缀数量
     */
    public int rank(String query) {
        int lo = 0, hi = N - 1;
        while (lo <= hi) {
            int mid = lo + (hi - lo) / 2;
            int cmp = compare(query, index[mid]);
            if      (cmp < 0) hi = mid - 1;
            else if (cmp > 0) lo = mid + 1;
            else return mid;
        }
        return lo;
    } 

    // is query < text[i..N) ?
    private int compare(String query, int i) {
        int M = query.length();
        int j = 0;
        while (i < N && j < M) {
            if (query.charAt(j) != text[i]) return query.charAt(j) - text[i];
            i++;
            j++;

        }
        if (i < N) return -1;
        if (j < M) return +1;
        return 0;
    }


    /**
     *  测试
     * 
     *  java SuffixArrayX < abra.txt 
	 *    i ind lcp rnk  select
	 *  ---------------------------
	 *    0  11   -   0  !
	 *    1  10   0   1  A!
	 *    2   7   1   2  ABRA!
	 *    3   0   4   3  ABRACADABRA!
	 *    4   3   1   4  ACADABRA!
	 *    5   5   1   5  ADABRA!
	 *    6   8   0   6  BRA!
	 *    7   1   3   7  BRACADABRA!
	 *    8   4   0   8  CADABRA!
	 *    9   6   0   9  DABRA!
	 *   10   9   0  10  RA!
	 *   11   2   2  11  RACADABRA!
     */
    public static void main(String[] args) {
        String s = StdIn.readAll().replaceAll("\n", " ").trim();
        SuffixArrayX suffix = new SuffixArrayX(s);

        SuffixArray  suffixReference = new SuffixArray(s);
        boolean check = true;
        for (int i = 0; check && i < s.length(); i++) {
            if (suffixReference.index(i) != suffix.index(i)) {
                StdOut.println("suffixReference(" + i + ") = " + suffixReference.index(i));
                StdOut.println("suffix(" + i + ") = " + suffix.index(i));
                String ith = "\"" + s.substring(suffix.index(i), Math.min(suffix.index(i) + 50, s.length())) + "\"";
                String jth = "\"" + s.substring(suffixReference.index(i), Math.min(suffixReference.index(i) + 50, s.length())) + "\"";
                StdOut.println(ith);
                StdOut.println(jth);
                check = false;
            }
        }

        // StdOut.println("rank(" + args[0] + ") = " + suffix.rank(args[0]));

        StdOut.println("  i ind lcp rnk  select");
        StdOut.println("---------------------------");

        for (int i = 0; i < s.length(); i++) {
            int index = suffix.index(i);
            String ith = "\"" + s.substring(index, Math.min(index + 50, s.length())) + "\"";
            int rank = suffix.rank(s.substring(index));
            assert s.substring(index).equals(suffix.select(i));
            if (i == 0) {
                StdOut.printf("%3d %3d %3s %3d  %s\n", i, index, "-", rank, ith);
            }
            else {
                // int lcp  = suffix.lcp(suffix.index(i), suffix.index(i-1));
                int lcp  = suffix.lcp(i);
                StdOut.printf("%3d %3d %3d %3d  %s\n", i, index, lcp, rank, ith);
            }
        }
    }

}
