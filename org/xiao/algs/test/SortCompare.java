package org.xiao.algs.test;

import org.xiao.algs.io.StdOut;
import org.xiao.algs.sort.Heap;
import org.xiao.algs.sort.Insertion;
import org.xiao.algs.sort.Merge;
import org.xiao.algs.sort.Quick;
import org.xiao.algs.sort.Selection;
import org.xiao.algs.sort.Shell;
import org.xiao.algs.util.StdRandom;
import org.xiao.algs.util.Stopwatch;

/***
 * 
 * 比较两种排序算法
 * 
 * @author XiaoJian
 *
 */
public class SortCompare {

	static final String INSERTTION_SORT = "Insertion"; // 插入排序
	static final String SELECTION_SORT = "Selection"; // 选择排序
	static final String SHELL_SORT = "Shell"; // 希尔排序
	static final String MERGE_SORT = "Merge"; // 归并排序
	static final String QUICK_SORT = "Quick"; // 快速排序
	static final String HEAP_SORT = "Heap"; // 堆排序

	/**
	 * 针对给定输入，为某种排序算法计时
	 */
	public static double time(String alg, Double[] a) {
		Stopwatch timer = new Stopwatch();
		if (alg.equals(INSERTTION_SORT)) {
			Insertion.sort(a);
		}
		if (alg.equals(SELECTION_SORT)) {
			Selection.sort(a);
		}
		if (alg.equals(SHELL_SORT)) {
			Shell.sort(a);
		}
		if (alg.equals(MERGE_SORT)) {
			Merge.sort(a);
		}
		if (alg.equals(QUICK_SORT)) {
			Quick.sort(a);
		}
		if (alg.equals(HEAP_SORT)) {
			Heap.sort(a);
		}
		return timer.elapsedTime();
	}

	/**
	 * 使用算法alg将T个长度为N的数组排序，计算算法总时间
	 */
	public static double timeRandomInput(String alg, int N, int T) {
		double total = 0.0;
		Double[] a = new Double[N];
		for (int t = 0; t < T; t++) {
			// 进行一次测试,生成一个数组并排序
			for (int i = 0; i < N; i++) {
				a[i] = StdRandom.uniform();
			}
			total += time(alg, a);
		}
		return total;
	}

	/**
	 * 测试
	 * java SortCompare Insertion Selection 1000 100
	 */
	public static void main(String[] args) {
		if(args.length != 4){
			System.out.println("Usage:java SortCompare Insertion Selection 1000 100");
		}
		String alg1 = args[0];
		String alg2 = args[1];
		int N = Integer.parseInt(args[2]);
		int T = Integer.parseInt(args[3]);
		double t1 = timeRandomInput(alg1, N, T);// 计算算法1的总时间
		double t2 = timeRandomInput(alg2, N, T);// 计算算法2的总时间
		StdOut.printf("For %d random Doubles\n	%s is", N, alg1);
		StdOut.printf(" %.1f times faster than %s\n", t2 / t1, alg2);
	}

}
