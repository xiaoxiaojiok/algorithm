package org.xiao.algs.sort;

import org.xiao.algs.io.StdIn;
import org.xiao.algs.io.StdOut;
import org.xiao.algs.util.StdRandom;
/***
 * 
 * 快速排序
 * 
 * @author XiaoJian
 *
 */
public class Quick {

    private Quick() { }

    /**
     * 对数组a进行快速排序
     */
    public static void sort(Comparable[] a) {
    	//加入随机化，消除对输入的依赖
        StdRandom.shuffle(a);
        sort(a, 0, a.length - 1);
        assert isSorted(a);
    }

    // 快速排序
    private static void sort(Comparable[] a, int lo, int hi) { 
        if (hi <= lo) return;
        int j = partition(a, lo, hi); //切分
        sort(a, lo, j-1); //将左半部分排序
        sort(a, j+1, hi); //将右半部分排序
        assert isSorted(a, lo, hi);
    }

    // 将数组切分为a[lo..i-1],a[i],a[i+1..hi]
    private static int partition(Comparable[] a, int lo, int hi) {
        int i = lo; //左右扫描指针
        int j = hi + 1;
        Comparable v = a[lo]; //切分元素
        while (true) { 
        	//扫描左右，检查扫描是否结束并交换元素
            while (less(a[++i], v))
                if (i == hi) break;

            while (less(v, a[--j]))
                if (j == lo) break;    

            if (i >= j) break;

            exch(a, i, j); 
        }

      //将v = a[j]放入正确的位置
        exch(a, lo, j);

        // a[lo .. j-1] <= a[j] <= a[j+1 .. hi]达成
        return j;
    }

    /**
     * 选择第K小的元素
     * 可以通过快速排序的切分来查找中位数
     */
    public static Comparable select(Comparable[] a, int k) {
        if (k < 0 || k >= a.length) {
            throw new IndexOutOfBoundsException("Selected element out of bounds");
        }
        StdRandom.shuffle(a);
        int lo = 0, hi = a.length - 1;
        while (hi > lo) {
            int i = partition(a, lo, hi);
            if      (i > k) hi = i - 1;
            else if (i < k) lo = i + 1;
            else return a[i];
        }
        return a[lo];
    }

    // is v < w ?
    private static boolean less(Comparable v, Comparable w) {
        return (v.compareTo(w) < 0);
    }
        
    // 交换数组元素 a[i] 和  a[j]
    private static void exch(Object[] a, int i, int j) {
        Object swap = a[i];
        a[i] = a[j];
        a[j] = swap;
    }

    // 用于调试数组是否有序
    private static boolean isSorted(Comparable[] a) {
        return isSorted(a, 0, a.length - 1);
    }
        
    // 用于调试数组是否有序 a[lo] to a[hi] 
    private static boolean isSorted(Comparable[] a, int lo, int hi) {
        for (int i = lo + 1; i <= hi; i++)
            if (less(a[i], a[i-1])) return false;
        return true;
    }


    // 打印数组
    private static void show(Comparable[] a) {
        for (int i = 0; i < a.length; i++) {
            StdOut.println(a[i]);
        }
    }

    /**
     * 测试
     * more tiny.txt
     * S O R T E X A M P L E
     * more word3.txt
     * bed dad yes zoo ... all bad yet
     */
    public static void main(String[] args) {
        String[] a = StdIn.readAllStrings();
        Quick.sort(a);
        show(a);

        // 加入随机化
        StdRandom.shuffle(a);

        // 通过选择第K小元素排序a数组
        StdOut.println();
        for (int i = 0; i < a.length; i++) {
            String ith = (String) Quick.select(a, i);
            StdOut.println(ith);
        }
    }

}
