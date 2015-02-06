package org.xiao.algs.sort;

import org.xiao.algs.io.StdIn;
import org.xiao.algs.io.StdOut;

/***
 * 
 * 改进的快速排序
 * 
 * 当递归调用快速排序算法时，处理的数组的规模会越来越小，
 * 当小的一定程度时，采用比较简单的插入排序算法会比快速排序更有优势；
 * 同时结合采用三者取中划分方法和三路快排方法
 * 
 * @author XiaoJian
 *
 */
public class QuickX {
    private static final int CUTOFF = 8;  // cutoff to insertion sort, must be >= 1

    private QuickX() { }

    /**
     * 使用改进的快速排序对a进行排序
     */
    public static void sort(Comparable[] a) {
        sort(a, 0, a.length - 1);
    }

    private static void sort(Comparable[] a, int lo, int hi) { 
        int N = hi - lo + 1;

        // 使用快速排序
        if (N <= CUTOFF) {
            insertionSort(a, lo, hi);
            return;
        }

        // 使用三者取中划分方法
        else if (N <= 40) {
            int m = median3(a, lo, lo + N/2, hi);
            exch(a, m, lo);
        }

        // use Tukey ninther as partitioning element
        else  {
        	//结合采用三者取中划分方法和三路快排方法
            int eps = N/8;
            int mid = lo + N/2;
            int m1 = median3(a, lo, lo + eps, lo + eps + eps);
            int m2 = median3(a, mid - eps, mid, mid + eps);
            int m3 = median3(a, hi - eps - eps, hi - eps, hi); 
            int ninther = median3(a, m1, m2, m3);
            exch(a, ninther, lo);
        }

        // Bentley-McIlroy 3-way partitioning
        int i = lo, j = hi+1;
        int p = lo, q = hi+1;
        Comparable v = a[lo];
        while (true) {
            while (less(a[++i], v))
                if (i == hi) break;
            while (less(v, a[--j]))
                if (j == lo) break;

            // pointers cross
            if (i == j && eq(a[i], v))
                exch(a, ++p, i);
            if (i >= j) break;

            exch(a, i, j);
            if (eq(a[i], v)) exch(a, ++p, i);
            if (eq(a[j], v)) exch(a, --q, j);
        }


        i = j + 1;
        for (int k = lo; k <= p; k++) exch(a, k, j--);
        for (int k = hi; k >= q; k--) exch(a, k, i++);

        sort(a, lo, j);
        sort(a, i, hi);
    }


    // 插入排序
    private static void insertionSort(Comparable[] a, int lo, int hi) {
        for (int i = lo; i <= hi; i++)
            for (int j = i; j > lo && less(a[j], a[j-1]); j--)
                exch(a, j, j-1);
    }


    // return the index of the median element among a[i], a[j], and a[k]
    private static int median3(Comparable[] a, int i, int j, int k) {
        return (less(a[i], a[j]) ?
               (less(a[j], a[k]) ? j : less(a[i], a[k]) ? k : i) :
               (less(a[k], a[j]) ? j : less(a[k], a[i]) ? k : i));
    }

    // is v < w ?
    private static boolean less(Comparable v, Comparable w) {
        return (v.compareTo(w) < 0);
    }

    // does v == w ?
    private static boolean eq(Comparable v, Comparable w) {
        return (v.compareTo(w) == 0);
    }
        
    // 交换数组元素 a[i] 和  a[j]
    private static void exch(Object[] a, int i, int j) {
        Object swap = a[i];
        a[i] = a[j];
        a[j] = swap;
    }

 // 用于调试数组是否有序
    private static boolean isSorted(Comparable[] a) {
        for (int i = 1; i < a.length; i++)
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
        QuickX.sort(a);
        show(a);
    }

}
