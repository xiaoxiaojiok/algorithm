package org.xiao.algs.string;

import org.xiao.algs.io.StdIn;
import org.xiao.algs.io.StdOut;
import org.xiao.algs.util.StdRandom;

/**
 * 
 * 三向字符串快速排序算法
 * 
 * 字符串排序算法 LSD、MSD、Quick3string（除了通用排序外的另外三种）
 * 
 * 实际上通用排序都是高位优先的字符串排序算法，因为String的compareTo就是从左到右访问字符串的
 *  
 * 通用排序算法，可以处理所有高位优先字符串排序不擅长的各种情况，特别适用于含有较长公共前缀的字符串
 * 
 * 运行时间在N和Nw之间（w为字符串平均长度），空间为W+logN
 * 
 * @author XiaoJian
 *
 */

public class Quick3string {
    private static final int CUTOFF =  15;   // 阀值

    // 排序
    public static void sort(String[] a) {
        StdRandom.shuffle(a);
        sort(a, 0, a.length-1, 0);
        assert isSorted(a);
    }

    // 返回s的第d个字符，若d = s.length 则返回 -1
    private static int charAt(String s, int d) { 
        assert d >= 0 && d <= s.length();
        if (d == s.length()) return -1;
        return s.charAt(d);
    }


    // 以第d个字符为键进行三向快速排序
    private static void sort(String[] a, int lo, int hi, int d) { 

        // 小数组处理
        if (hi <= lo + CUTOFF) {
            insertion(a, lo, hi, d);
            return;
        }

        int lt = lo, gt = hi;
        int v = charAt(a[lo], d);
        int i = lo + 1;
        while (i <= gt) {
            int t = charAt(a[i], d);
            if      (t < v) exch(a, lt++, i++);
            else if (t > v) exch(a, i, gt--);
            else              i++;
        }

        sort(a, lo, lt-1, d);
        if (v >= 0) sort(a, lt, gt, d+1);
        sort(a, gt+1, hi, d);
    }

    // 以第d个字符为键进行快速排序
    private static void insertion(String[] a, int lo, int hi, int d) {
        for (int i = lo; i <= hi; i++)
            for (int j = i; j > lo && less(a[j], a[j-1], d); j--)
                exch(a, j, j-1);
    }

    // 交换 a[i] 和 a[j]
    private static void exch(String[] a, int i, int j) {
        String temp = a[i];
        a[i] = a[j];
        a[j] = temp;
    }

    // 比较
    private static boolean less(String v, String w, int d) {
        assert v.substring(0, d).equals(w.substring(0, d));
        for (int i = d; i < Math.min(v.length(), w.length()); i++) {
            if (v.charAt(i) < w.charAt(i)) return true;
            if (v.charAt(i) > w.charAt(i)) return false;
        }
        return v.length() < w.length();
    }

    // 数组是否有序
    private static boolean isSorted(String[] a) {
        for (int i = 1; i < a.length; i++)
            if (a[i].compareTo(a[i-1]) < 0) return false;
        return true;
    }


    /***
     *  测试
     *  
     *  java Quick3string < shell.txt
	 *  are
	 *  by
	 *  sea
	 *  seashells
	 *  seashells
	 *  sells
	 *  sells
	 *  she
	 *  she
	 *  shells
	 *  shore
	 *  surely
	 *  the
	 *  the
     */
    public static void main(String[] args) {

        // 从标准输入读取
        String[] a = StdIn.readAllStrings();
        int N = a.length;

        // 排序
        sort(a);

        // 打印结果
        for (int i = 0; i < N; i++)
            StdOut.println(a[i]);
    }
}
