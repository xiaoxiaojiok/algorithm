package org.xiao.algs.sort;

import org.xiao.algs.io.StdIn;
import org.xiao.algs.io.StdOut;

/***
 * 
 * 归并排序(自底向上)
 * 
 * @author XiaoJian
 *
 */
public class MergeBU {

    private MergeBU() { }

    // 使用 aux[lo .. hi] 归并 a[lo .. mid] 和 a[mid+1 ..hi]
    private static void merge(Comparable[] a, Comparable[] aux, int lo, int mid, int hi) {

    	// 将a[lo .. hi]复制到aux[lo .. hi]
        for (int k = lo; k <= hi; k++) {
            aux[k] = a[k]; 
        }

        // 归并回到 a[]
        int i = lo, j = mid+1;
        for (int k = lo; k <= hi; k++) {
            if      (i > mid)              a[k] = aux[j++];  // this copying is unneccessary
            else if (j > hi)               a[k] = aux[i++];
            else if (less(aux[j], aux[i])) a[k] = aux[j++];
            else                           a[k] = aux[i++];
        }

    }

    /**
     * 对数组a归并排序(自底向上)
     */
    public static void sort(Comparable[] a) {
        int N = a.length;
        Comparable[] aux = new Comparable[N];
        for (int n = 1; n < N; n = n+n) {
            for (int i = 0; i < N-n; i += n+n) {
                int lo = i;
                int m  = i+n-1;
                int hi = Math.min(i+n+n-1, N-1);
                merge(a, aux, lo, m, hi);
            }
        }
        assert isSorted(a);
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
        MergeBU.sort(a);
        show(a);
    }
}
