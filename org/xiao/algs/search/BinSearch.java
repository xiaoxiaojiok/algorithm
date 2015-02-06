package org.xiao.algs.search;

import java.util.Arrays;

/***
 * 二分查找法，一定要先排序
 * @author XiaoJian
 *
 */
public class BinSearch {
	
	/**
	 * 
	 * @param key 要查找的元素
	 * @param a 有序数组
	 * @return 返回数组下标
	 */
	public static int binarySearch(int key,int[] a){
		int lo = 0;
		int hi = a.length-1;
	
		while(lo<=hi){
			int mid = (lo+hi)/2;
			if(a[mid]==key){
				return mid;
			}
			if(a[mid]>key){
				hi = mid-1;
			}
			if(a[mid]<key){
				lo = mid+1;
			}
		}
		
		return -1;
	}
	
	public static void isNotIn(int[] a,int[] b){
		for (int i : b) {
			if(binarySearch(i, a)==-1){
				System.out.println(i);
			}
		}
	}
	
	/**
	 * 测试
	 * java Writelist largeW.txt < largeT.txt
	 */
	public static void main(String[] args) {
		int[] a={5,4,6,8,78,14,44};
		Arrays.sort(a);
//		System.out.println(binarySearch(7, a));
		int[] b={78,1,6,4,34,9,45};
		isNotIn(a, b);
		
		int N =3;
		System.out.println(N);
		a[--N]=9;
		System.out.println(N);
		
	}

}
