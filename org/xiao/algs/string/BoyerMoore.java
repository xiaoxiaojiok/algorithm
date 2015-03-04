package org.xiao.algs.string;

import org.xiao.algs.io.StdOut;

/***
 * 
 * BoyerMoore字符串查找算法(启发式的查找不匹配的字符)从右到左
 * 
 * 子字符串查找 ViolenceSearch、KMP、BoyerMoore、RabinKarp
 * 
 * 各优点：暴力查找实现简单并且在一般情况下工作良好；KMP能够保证线性级别的性能并且不需要在正文回退；
 * BoyerMoore在一般情况下都是亚线性级别；RabinKarp算法是线性级别的且不需要在正文回退
 * 
 * 各缺点：暴力查找所需的时间可能和MN成正比，KMP和BoyerMoore都需要额外的空间，RabinKarp的内循环很长
 * 
 * 最坏情况MN，一般情况N/M 空间R
 * 
 * @author XiaoJian
 *
 */
public class BoyerMoore {
    private final int R;     // 基数
    private int[] right;     // 跳跃表

    private char[] pattern;    // 模式数组
    private String pat;        // 模式字符串

    // 根据模式字符串初始化
    public BoyerMoore(String pat) {
        this.R = 256;
        this.pat = pat;

        // 计算跳跃表
        right = new int[R];
        for (int c = 0; c < R; c++)
            right[c] = -1; //不包含在模式字符串中的字符的值为-1
        for (int j = 0; j < pat.length(); j++)
            right[pat.charAt(j)] = j; //包含在模式字符串中的字符的值为它在其中出现的最右位置
    }

    //  根据模式字符串数组和R个字符的字母表初始化
    public BoyerMoore(char[] pattern, int R) {
        this.R = R;
        this.pattern = new char[pattern.length];
        for (int j = 0; j < pattern.length; j++)
            this.pattern[j] = pattern[j];

        // 计算跳跃表
        right = new int[R];
        for (int c = 0; c < R; c++)
            right[c] = -1;
        for (int j = 0; j < pattern.length; j++)
            right[pattern[j]] = j;
    }

    // 返回第一次匹配的索引，若无则返回N
    public int search(String txt) {
        int M = pat.length();
        int N = txt.length();
        int skip;
        for (int i = 0; i <= N - M; i += skip) {
            skip = 0;
            for (int j = M-1; j >= 0; j--) {
                if (pat.charAt(j) != txt.charAt(i+j)) {
                    skip = Math.max(1, j - right[txt.charAt(i+j)]);
                    break;
                }
            }
            if (skip == 0) return i;    // 找到匹配
        }
        return N;                       // 未找到匹配
    }


    // 返回第一次匹配的索引，若无则返回N
    public int search(char[] text) {
        int M = pattern.length;
        int N = text.length;
        int skip;
        for (int i = 0; i <= N - M; i += skip) {
            skip = 0;
            for (int j = M-1; j >= 0; j--) {
                if (pattern[j] != text[i+j]) {
                    skip = Math.max(1, j - right[text[i+j]]);
                    break;
                }
            }
            if (skip == 0) return i;    // 找到匹配
        }
        return N;                       // 未找到匹配
    }



    /***
     * 
     *  测试
     *  
     *  % java BoyerMoore abracadabra abacadabrabracabracadabrabrabracad
	 *  text:    abacadabrabracabracadabrabrabracad 
	 *  pattern:               abracadabra
	 *
	 *  % java BoyerMoore rab abacadabrabracabracadabrabrabracad
	 *  text:    abacadabrabracabracadabrabrabracad 
	 *  pattern:         rab
	 *
	 *  % java BoyerMoore bcara abacadabrabracabracadabrabrabracad
	 *  text:    abacadabrabracabracadabrabrabracad 
	 *  pattern:                                   bcara
	 *
	 *  % java BoyerMoore rabrabracad abacadabrabracabracadabrabrabracad
	 *  text:    abacadabrabracabracadabrabrabracad
	 *  pattern:                        rabrabracad
	 *
	 *  % java BoyerMoore abacad abacadabrabracabracadabrabrabracad
	 *  text:    abacadabrabracabracadabrabrabracad
	 *  pattern: abacad
     */
    
    public static void main(String[] args) {
        String pat = args[0];
        String txt = args[1];
        char[] pattern = pat.toCharArray();
        char[] text    = txt.toCharArray();

        BoyerMoore boyermoore1 = new BoyerMoore(pat);
        BoyerMoore boyermoore2 = new BoyerMoore(pattern, 256);
        int offset1 = boyermoore1.search(txt);
        int offset2 = boyermoore2.search(text);

        // 打印
        StdOut.println("text:    " + txt);

        StdOut.print("pattern: ");
        for (int i = 0; i < offset1; i++)
            StdOut.print(" ");
        StdOut.println(pat);

        StdOut.print("pattern: ");
        for (int i = 0; i < offset2; i++)
            StdOut.print(" ");
        StdOut.println(pat);
    }
}
