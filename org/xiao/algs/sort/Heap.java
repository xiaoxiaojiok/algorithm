package org.xiao.algs.sort;

import org.xiao.algs.io.StdIn;
import org.xiao.algs.io.StdOut;

/***
 * 
 * 堆排序
 * 
 * @author XiaoJian
 *
 */

public class Heap {

    private Heap() { }

    /**
     * 对数组pq进行堆排序
     */
  
	public static void sort(Comparable[] pq) {
        int N = pq.length;
        for (int k = N/2; k >= 1; k--)
            sink(pq, k, N);
        while (N > 1) {
            exch(pq, 1, N--);
            sink(pq, 1, N);
        }
    }


    //插入元素时候，元素下沉，使得二叉树有序
    private static void sink(Comparable[] pq, int k, int N) {
        while (2*k <= N) {
            int j = 2*k;
            if (j < N && less(pq, j, j+1)) j++;
            if (!less(pq, k, j)) break;
            exch(pq, k, j);
            k = j;
        }
    }

	private static boolean less(Comparable[] pq, int i, int j) {
        return pq[i-1].compareTo(pq[j-1]) < 0;
    }

    // 交换数组元素 a[i] 和  a[j]
    private static void exch(Object[] pq, int i, int j) {
        Object swap = pq[i-1];
        pq[i-1] = pq[j-1];
        pq[j-1] = swap;
    }

    // is v < w ?
    private static boolean less(Comparable v, Comparable w) {
        return (v.compareTo(w) < 0);
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
     */    public static void main(String[] args) {
        String[] a = StdIn.readAllStrings();
        Heap.sort(a);
        show(a);
    }
}
