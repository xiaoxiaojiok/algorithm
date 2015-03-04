package org.xiao.algs.string;

import org.xiao.algs.io.StdIn;
import org.xiao.algs.io.StdOut;

/**
 * 
 * 高位优先的字符串排序算法
 * 
 * 字符串排序算法 LSD、MSD、Quick3string（除了通用排序外的另外三种）
 * 
 * 实际上通用排序都是高位优先的字符串排序算法，因为String的compareTo就是从左到右访问字符串的
 *  
 * 适用于随机字符串
 * 
 * 运行时间在N和Nw之间（w为字符串平均长度），空间为N+WR
 * 
 * @author XiaoJian
 *
 */
public class MSD {
    private static final int BITS_PER_BYTE =   8;
    private static final int BITS_PER_INT  =  32;   // 每一个int是 32 bits 
    private static final int R             = 256;   // 基数
    private static final int CUTOFF        =  15;   // 小数组的切换阀值

    // 排序
    public static void sort(String[] a) {
        int N = a.length;
        String[] aux = new String[N];
        sort(a, 0, N-1, 0, aux);
    }

    // 返回s的第d个字符，若d = s.length 则返回 -1
    private static int charAt(String s, int d) {
        assert d >= 0 && d <= s.length();
        if (d == s.length()) return -1;
        return s.charAt(d);
    }

    // 以第d个字符为键将a[lo] 至 a[hi]排序
    private static void sort(String[] a, int lo, int hi, int d, String[] aux) {

        // 小数组处理
        if (hi <= lo + CUTOFF) {
            insertion(a, lo, hi, d);
            return;
        }

        // 计算频率
        int[] count = new int[R+2];
        for (int i = lo; i <= hi; i++) {
            int c = charAt(a[i], d);
            count[c+2]++;
        }

        // 将频率转换成索引
        for (int r = 0; r < R+1; r++)
            count[r+1] += count[r];

        // 数据分类
        for (int i = lo; i <= hi; i++) {
            int c = charAt(a[i], d);
            aux[count[c+1]++] = a[i];
        }

        // 回写
        for (int i = lo; i <= hi; i++) 
            a[i] = aux[i - lo];


        // 递归的以每个字符为键进行排序
        for (int r = 0; r < R; r++)
            sort(a, lo + count[r], lo + count[r+1] - 1, d+1, aux);
    }


    // 插入排序
    private static void insertion(String[] a, int lo, int hi, int d) {
        for (int i = lo; i <= hi; i++)
            for (int j = i; j > lo && less(a[j], a[j-1], d); j--)
                exch(a, j, j-1);
    }

    // 交换a[i]和a[j]
    private static void exch(String[] a, int i, int j) {
        String temp = a[i];
        a[i] = a[j];
        a[j] = temp;
    }

    // 比较
    private static boolean less(String v, String w, int d) {
        // assert v.substring(0, d).equals(w.substring(0, d));
        for (int i = d; i < Math.min(v.length(), w.length()); i++) {
            if (v.charAt(i) < w.charAt(i)) return true;
            if (v.charAt(i) > w.charAt(i)) return false;
        }
        return v.length() < w.length();
    }


    // 对int数组进行MSD排序
    public static void sort(int[] a) {
        int N = a.length;
        int[] aux = new int[N];
        sort(a, 0, N-1, 0, aux);
    }

    //  以第d个byte为键将a[lo] 至 a[hi]排序
    private static void sort(int[] a, int lo, int hi, int d, int[] aux) {

        if (hi <= lo + CUTOFF) {
            insertion(a, lo, hi, d);
            return;
        }

        int[] count = new int[R+1];
        int mask = R - 1;   // 0xFF;
        int shift = BITS_PER_INT - BITS_PER_BYTE*d - BITS_PER_BYTE;
        for (int i = lo; i <= hi; i++) {
            int c = (a[i] >> shift) & mask;
            count[c + 1]++;
        }

        for (int r = 0; r < R; r++)
            count[r+1] += count[r];

/*************BUGGGY
        // for most significant byte, 0x80-0xFF comes before 0x00-0x7F
        if (d == 0) {
            int shift1 = count[R] - count[R/2];
            int shift2 = count[R/2];
            for (int r = 0; r < R/2; r++)
                count[r] += shift1;
            for (int r = R/2; r < R; r++)
                count[r] -= shift2;
        }
************************************/
        for (int i = lo; i <= hi; i++) {
            int c = (a[i] >> shift) & mask;
            aux[count[c]++] = a[i];
        }

        for (int i = lo; i <= hi; i++) 
            a[i] = aux[i - lo];

        // 没有更多的bits
        if (d == 4) return;

        // 递归的以每一个字符为键进行排序
        if (count[0] > 0)
            sort(a, lo, lo + count[0] - 1, d+1, aux);
        for (int r = 0; r < R; r++)
            if (count[r+1] > count[r])
                sort(a, lo + count[r], lo + count[r+1] - 1, d+1, aux);
    }

    // 以第d个字符为键将a[lo] 至 a[hi]进行插入排序
    private static void insertion(int[] a, int lo, int hi, int d) {
        for (int i = lo; i <= hi; i++)
            for (int j = i; j > lo && a[j] < a[j-1]; j--)
                exch(a, j, j-1);
    }

    // 交换
    private static void exch(int[] a, int i, int j) {
        int temp = a[i];
        a[i] = a[j];
        a[j] = temp;
    }


    /***
     *  测试
     *  
     *  java MSD < shells.txt 
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
        String[] a = StdIn.readAllStrings();
        int N = a.length;
        sort(a);
        for (int i = 0; i < N; i++)
            StdOut.println(a[i]);
    }
}
