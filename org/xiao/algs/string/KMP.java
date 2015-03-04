package org.xiao.algs.string;

import org.xiao.algs.io.StdOut;

/***
 * 
 * Knuth-Morris-Pratt字符串查找算法
 * 
 * 子字符串查找 ViolenceSearch、KMP、BoyerMoore、RabinKarp
 * 
 * 各优点：暴力查找实现简单并且在一般情况下工作良好；KMP能够保证线性级别的性能并且不需要在正文回退；
 * BoyerMoore在一般情况下都是亚线性级别；RabinKarp算法是线性级别的且不需要在正文回退
 * 
 * 各缺点：暴力查找所需的时间可能和MN成正比，KMP和BoyerMoore都需要额外的空间，RabinKarp的内循环很长
 * 
 * 最坏情况2N，一般情况1.1N 空间MR
 * 
 * @author XiaoJian
 *
 */

public class KMP {
    private final int R;       // 基数
    private int[][] dfa;       // 记录匹配失败时候模式指针j应该回退多远

    private char[] pattern;    // 模式数组
    private String pat;        // 模式字符串

    // 根据模式字符串创建一个DFA（有限状态自动机）
    public KMP(String pat) {
        this.R = 256;
        this.pat = pat;

        // 由模式字符串建立 DFA
        int M = pat.length();
        dfa = new int[R][M]; 
        dfa[pat.charAt(0)][0] = 1; 
        for (int X = 0, j = 1; j < M; j++) {
            for (int c = 0; c < R; c++) 
                dfa[c][j] = dfa[c][X];     // 复制匹配失败情况下的值
            dfa[pat.charAt(j)][j] = j+1;   // 设置匹配成功情况下的值
            X = dfa[pat.charAt(j)][X];     // 更新重启状态
        } 
    } 

    // 根据模式字符串数组和R个字符的字母表创建一个DFA
    public KMP(char[] pattern, int R) {
        this.R = R;
        this.pattern = new char[pattern.length];
        for (int j = 0; j < pattern.length; j++)
            this.pattern[j] = pattern[j];

        // 由模式字符串建立 DFA
        int M = pattern.length;
        dfa = new int[R][M]; 
        dfa[pattern[0]][0] = 1; 
        for (int X = 0, j = 1; j < M; j++) {
            for (int c = 0; c < R; c++) 
                dfa[c][j] = dfa[c][X];     // 复制匹配失败情况下的值
            dfa[pattern[j]][j] = j+1;      // 设置匹配成功情况下的值
            X = dfa[pattern[j]][X];        // 更新重启状态
        } 
    } 

    // 返回第一次匹配的索引，若无则返回N
    public int search(String txt) {

        int M = pat.length();
        int N = txt.length();
        int i, j;
        for (i = 0, j = 0; i < N && j < M; i++) {
            j = dfa[txt.charAt(i)][j];
        }
        if (j == M) return i - M;    // 找到匹配（到达模式字符串的结尾）
        return N;                    // 未找到匹配（到达文本字符串的结尾）
    }


    // 返回第一次匹配的索引，若无则返回N
    public int search(char[] text) {

        // simulate operation of DFA on text
        int M = pattern.length;
        int N = text.length;
        int i, j;
        for (i = 0, j = 0; i < N && j < M; i++) {
            j = dfa[text[i]][j];
        }
        if (j == M) return i - M;    // 找到匹配（到达模式字符串的结尾）
        return N;                    // 未找到匹配（到达文本字符串的结尾）
    }


    /***
     *  测试
     *  
     *  java KMP abracadabra abacadabrabracabracadabrabrabracad
	 *  text:    abacadabrabracabracadabrabrabracad 
	 *  pattern:               abracadabra          
	 *
	 *  % java KMP rab abacadabrabracabracadabrabrabracad
	 *  text:    abacadabrabracabracadabrabrabracad 
	 *  pattern:         rab
	 *
	 *  % java KMP bcara abacadabrabracabracadabrabrabracad
	 *  text:    abacadabrabracabracadabrabrabracad 
	 *  pattern:                                   bcara
	 *
	 *  % java KMP rabrabracad abacadabrabracabracadabrabrabracad 
	 *  text:    abacadabrabracabracadabrabrabracad
	 *  pattern:                        rabrabracad
	 *
	 *  % java KMP abacad abacadabrabracabracadabrabrabracad
	 *  text:    abacadabrabracabracadabrabrabracad
	 *  pattern: abacad
     */
    public static void main(String[] args) {
        String pat = args[0];
        String txt = args[1];
        char[] pattern = pat.toCharArray();
        char[] text    = txt.toCharArray();

        KMP kmp1 = new KMP(pat);
        int offset1 = kmp1.search(txt);

        KMP kmp2 = new KMP(pattern, 256);
        int offset2 = kmp2.search(text);

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
