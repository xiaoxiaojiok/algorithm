package org.xiao.algs.test;

import org.xiao.algs.io.In;
import org.xiao.algs.io.StdOut;
import org.xiao.algs.queue.IndexMinPQ;

/***
 * 
 * 基于优先队列的多向归并
 * 
 * @author XiaoJian
 *
 */
public class Multiway { 

    private Multiway() { }

    // 将多个有序输入流合并成为一个输入流
    private static void merge(In[] streams) { 
        int N = streams.length; 
        IndexMinPQ<String> pq = new IndexMinPQ<String>(N); 
        for (int i = 0; i < N; i++) 
            if (!streams[i].isEmpty()) 
            	//先轮流读取stream[i]的一个String
                pq.insert(i, streams[i].readString()); 

    	//然后对这几个String输出最小的key，并返回所在索引i(索引表示第i个stream)
    	//后再从该stream[i]读取下一个String，再输出最小的key，如此循环
        while (!pq.isEmpty()) {
            StdOut.print(pq.minKey() + " "); 
            int i = pq.delMin(); 
            if (!streams[i].isEmpty()) 
                pq.insert(i, streams[i].readString()); 
        }
        StdOut.println();
    } 


    /**
     *  测试
     *  
     *  从多个已经排好序的文件读入
     *  
     *  然后对这些排好序的文件归并成为一个有序的输出流
     *  
     *  其中每个stream[i]表示一个文件流，stream[i].readString()表示从一个文件里面读取下一个字符串
     *  
     *  每次轮流读取stream[i]的一个String，然后对这几个String输出最小的key，并返回所在索引i(索引表示第i个stream)
     *  然后再从该stream[i]读取下一个String，再输出最小的key，如此循环
     *  
     *  more m1.txt 
     *  A B C F G I I Z
     *  more m2.txt
     *  B D H P Q Q
     *  more m3.txt
     *  A B E F J N
     *  
     *  java Multiway m1.txt m2.txt m3.txt
     *  A A B B B C D E F G H I I J N P Q Q Z
     */
    public static void main(String[] args) { 
        int N = args.length; 
        In[] streams = new In[N]; 
        for (int i = 0; i < N; i++) 
            streams[i] = new In(args[i]); 
        merge(streams); 
    } 
} 
