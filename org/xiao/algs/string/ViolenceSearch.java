package org.xiao.algs.string;

import org.xiao.algs.io.StdOut;

/***
 * 
 * ViolenceSearch暴力字符串查找算法(Java的String的indexOf实现)
 * 
 * 子字符串查找 ViolenceSearch、KMP、BoyerMoore、RabinKarp
 * 
 * 各优点：暴力查找实现简单并且在一般情况下工作良好；KMP能够保证线性级别的性能并且不需要在正文回退；
 * BoyerMoore在一般情况下都是亚线性级别；RabinKarp算法是线性级别的且不需要在正文回退
 * 
 * 各缺点：暴力查找所需的时间可能和MN成正比，KMP和BoyerMoore都需要额外的空间，RabinKarp的内循环很长
 * 
 * 最坏情况MN，一般情况1.1N 空间1
 * 
 * @author XiaoJian
 *
 */
public class ViolenceSearch {
	
	public static int search(String pat,String txt){
		int M = pat.length();
		int N = txt.length();
		for(int i=0;i<=N-M;i++){
			int j;
			for(j = 0 ;j<M;j++){
				if(txt.charAt(i+j)!=pat.charAt(j)){
					break;
				}
			}
			if(j == M){
				return i;
			}
		}
		return N;
	}

	public static void main(String[] args) {
        String pat = args[0];
        String txt = args[1];

        int offset = ViolenceSearch.search(pat,txt);

        // 打印
        StdOut.println("text:    " + txt);

        StdOut.print("pattern: ");
        for (int i = 0; i < offset; i++)
            StdOut.print(" ");
        StdOut.println(pat);
	}

}
