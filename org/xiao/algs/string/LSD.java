package org.xiao.algs.string;

import org.xiao.algs.io.StdIn;
import org.xiao.algs.io.StdOut;

/**
 * 
 * 低位优先的字符串排序算法
 * 
 * 字符串排序算法 LSD、MSD、Quick3string（除了通用排序外的另外三种）
 * 
 * 实际上通用排序都是高位优先的字符串排序算法，因为String的compareTo就是从左到右访问字符串的
 *  
 * 适用于较短的定长字符串
 * 
 * 运行时间和NW成正比，空间为N
 * 
 * @author XiaoJian
 *
 */


public class LSD {
    private final static int BITS_PER_BYTE = 8;


    // LSD基数排序
    public static void sort(String[] a, int W) {
        int N = a.length;
        int R = 256;   // 扩展的ASCII字符集
        String[] aux = new String[N];

        //对每个元素都含有W个字符的数组a进行排序，进行W次键索引计数排序：从右到左，以每个位置的字符为键排序一次
        for (int d = W-1; d >= 0; d--) {
            // 通过前w个字符将a[]排序

            // 计算出现频率
            int[] count = new int[R+1];
            for (int i = 0; i < N; i++)
                count[a[i].charAt(d) + 1]++;

            // 将频率转换成索引
            for (int r = 0; r < R; r++)
                count[r+1] += count[r];

            // 将元素分类
            for (int i = 0; i < N; i++)
                aux[count[a[i].charAt(d)]++] = a[i];

            // 回写
            for (int i = 0; i < N; i++)
                a[i] = aux[i];
        }
    }

    // 对int数组进行LSD排序， 每个int看做4个byte
    // 假定数组非负
    // [ 2-3x faster than Arrays.sort() ]
    public static void sort(int[] a) {
        int BITS = 32;                 // 每一个int是32bit
        int W = BITS / BITS_PER_BYTE;  // 每一个int是4byte
        int R = 1 << BITS_PER_BYTE;    // 每一个byte在 0 and 255
        int MASK = R - 1;              // 0xFF

        int N = a.length;
        int[] aux = new int[N];

        for (int d = 0; d < W; d++) {         

            // 计算出现频率
            int[] count = new int[R+1];
            for (int i = 0; i < N; i++) {           
                int c = (a[i] >> BITS_PER_BYTE*d) & MASK;
                count[c + 1]++;
            }

            // 将频率转换成索引
            for (int r = 0; r < R; r++)
                count[r+1] += count[r];

            // for most significant byte, 0x80-0xFF comes before 0x00-0x7F
            if (d == W-1) {
                int shift1 = count[R] - count[R/2];
                int shift2 = count[R/2];
                for (int r = 0; r < R/2; r++)
                    count[r] += shift1;
                for (int r = R/2; r < R; r++)
                    count[r] -= shift2;
            }

            // 将元素分类
            for (int i = 0; i < N; i++) {
                int c = (a[i] >> BITS_PER_BYTE*d) & MASK;
                aux[count[c]++] = a[i];
            }

            // 回写
            for (int i = 0; i < N; i++)
                a[i] = aux[i];
        }
    }

    /***
     *  测试
     *  
     *  java LSD < words3.txt
	 *  all
	 *  bad
	 *  bed
	 *  bug
	 *  dad
	 *  ...
	 *  yes
	 *  yet
	 *  zoo
	 *  
     */
    public static void main(String[] args) {
        String[] a = StdIn.readAllStrings();
        int N = a.length;

        int W = a[0].length();
        for (int i = 0; i < N; i++)
            assert a[i].length() == W : "Strings must have fixed length";

        sort(a, W);

        for (int i = 0; i < N; i++)
            StdOut.println(a[i]);
    }
}
