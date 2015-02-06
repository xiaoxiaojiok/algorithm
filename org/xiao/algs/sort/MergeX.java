package org.xiao.algs.sort;

import org.xiao.algs.io.StdIn;
import org.xiao.algs.io.StdOut;
/***
 * 
 * 改进的归并排序
 * 
 * 一、对规模比较小的数组采用简单的插入排序算法。

 * 二、如果第二个子数组的第一个元素比第一个子数组最后一个元素还大的情况，说明整个数组已经有序了。终止程序。

 * 三、省去对辅助数组的赋初始值的过程。但还是得用一个数组来存放最终排序号的数组，所以能省去一些时间但并不能省去必须的空间。
 * 
 * @author XiaoJian
 *
 */
public class MergeX {
	
    private static final int CUTOFF = 7;  

    private MergeX() { }

    private static void merge(Comparable[] src, Comparable[] dst, int lo, int mid, int hi) {

        assert isSorted(src, lo, mid);
        assert isSorted(src, mid+1, hi);

        int i = lo, j = mid+1;
        for (int k = lo; k <= hi; k++) {
            if      (i > mid)              dst[k] = src[j++];
            else if (j > hi)               dst[k] = src[i++];
            else if (less(src[j], src[i])) dst[k] = src[j++];   // to ensure stability
            else                           dst[k] = src[i++];
        }

        // postcondition: dst[lo .. hi] is sorted subarray
        assert isSorted(dst, lo, hi);
    }

    private static void sort(Comparable[] src, Comparable[] dst, int lo, int hi) {
        // if (hi <= lo) return;
        if (hi <= lo + CUTOFF) { 
            insertionSort(dst, lo, hi);
            return;
        }
        int mid = lo + (hi - lo) / 2;
        sort(dst, src, lo, mid);
        sort(dst, src, mid+1, hi);

        // if (!less(src[mid+1], src[mid])) {
        //    for (int i = lo; i <= hi; i++) dst[i] = src[i];
        //    return;
        // }

        // using System.arraycopy() is a bit faster than the above loop
        if (!less(src[mid+1], src[mid])) {
            System.arraycopy(src, lo, dst, lo, hi - lo + 1);
            return;
        }

        merge(src, dst, lo, mid, hi);
    }

    /**
     * 改进的归并排序
     */
    public static void sort(Comparable[] a) {
        Comparable[] aux = a.clone();
        sort(aux, a, 0, a.length-1);  
        assert isSorted(a);
    }


    // 使用插入排序
    private static void insertionSort(Comparable[] a, int lo, int hi) {
        for (int i = lo; i <= hi; i++)
            for (int j = i; j > lo && less(a[j], a[j-1]); j--)
                exch(a, j, j-1);
    }


    // 交换数组元素 a[i] 和  a[j]
    private static void exch(Comparable[] a, int i, int j) {
        Comparable swap = a[i];
        a[i] = a[j];
        a[j] = swap;
    }

    // is a[i] < a[j]?
    private static boolean less(Comparable a, Comparable b) {
        return (a.compareTo(b) < 0);
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
        MergeX.sort(a);
        show(a);
    }
}
