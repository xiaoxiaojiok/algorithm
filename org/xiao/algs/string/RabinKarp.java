package org.xiao.algs.string;
/***
 * 
 * RabinKarp字符串查找算法(蒙特卡洛算法)
 * 
 * 子字符串查找 ViolenceSearch、KMP、BoyerMoore、RabinKarp
 * 
 * 各优点：暴力查找实现简单并且在一般情况下工作良好；KMP能够保证线性级别的性能并且不需要在正文回退；
 * BoyerMoore在一般情况下都是亚线性级别；RabinKarp算法是线性级别的且不需要在正文回退
 * 
 * 各缺点：暴力查找所需的时间可能和MN成正比，KMP和BoyerMoore都需要额外的空间，RabinKarp的内循环很长
 * 
 * 最坏情况7N，一般情况7N 空间1
 * 
 * @author XiaoJian
 *
 */
import java.math.BigInteger;
import java.util.Random;

import org.xiao.algs.io.StdOut;

public class RabinKarp {
    private String pat;      // 模式字符串（仅拉斯维加斯算法需要）
    private long patHash;    // 模式字符串的散列值
    private int M;           // 模式字符串的长度
    private long Q;          // 一个很大的素数
    private int R;           // 基数
    private long RM;         // R^(M-1) % Q

    public RabinKarp(int R, char[] pattern) {
        throw new UnsupportedOperationException("Operation not supported yet");
    }

    public RabinKarp(String pat) {
        this.pat = pat;      // 保存模式字符串（仅拉斯维加斯算法需要）
        R = 256;
        M = pat.length();
        Q = longRandomPrime();

        // 预先计算 R^(M-1) % Q 
        RM = 1;
        for (int i = 1; i <= M-1; i++)
           RM = (R * RM) % Q; //用于减去第一个数字时的计算
        patHash = hash(pat, M);
    } 

    // 计算 hash for key[0..M-1]. 
    private long hash(String key, int M) { 
        long h = 0; 
        for (int j = 0; j < M; j++) 
            h = (R * h + key.charAt(j)) % Q; 
        return h; 
    } 

    // 对于拉斯维加斯版本，检查模式与txt[i..i-M+1]匹配
    private boolean check(String txt, int i) {
        for (int j = 0; j < M; j++) 
            if (pat.charAt(j) != txt.charAt(i + j)) 
                return false; 
        return true;
    }

    // 蒙特卡洛算法：总是返回true
    @SuppressWarnings("unused")
	private boolean check(int i) {
        return true;
    }

    // 在正文中查找相等的散列值
    public int search(String txt) {
        int N = txt.length(); 
        if (N < M) return N;
        long txtHash = hash(txt, M); 

        // 一开始就匹配成功
        if ((patHash == txtHash) && check(txt, 0))
            return 0;

        // 减去第一个数字，加上最后一个数字，再次检测匹配
        for (int i = M; i < N; i++) {
            // Remove leading digit, add trailing digit, check for match. 
            txtHash = (txtHash + Q - RM*txt.charAt(i-M) % Q) % Q; 
            txtHash = (txtHash*R + txt.charAt(i)) % Q; 

            // 找到匹配
            int offset = i - M + 1;
            if ((patHash == txtHash) && check(txt, offset))
                return offset;
        }

        // 没有找到
        return N;
    }


    // a random 31-bit prime
    private static long longRandomPrime() {
        BigInteger prime = BigInteger.probablePrime(31, new Random());
        return prime.longValue();
    }

    /***
     * 
     *  测试
     * 
     *  % java RabinKarp abracadabra abacadabrabracabracadabrabrabracad
	 *  pattern: abracadabra
	 *  text:    abacadabrabracabracadabrabrabracad 
	 *  match:                 abracadabra          
	 *
	 *  % java RabinKarp rab abacadabrabracabracadabrabrabracad
	 *  pattern: rab
	 *  text:    abacadabrabracabracadabrabrabracad 
	 *  match:           rab                         
	 *
	 *  % java RabinKarp bcara abacadabrabracabracadabrabrabracad
	 *  pattern: bcara
	 *  text:         abacadabrabracabracadabrabrabracad 
	 *
	 *  %  java RabinKarp rabrabracad abacadabrabracabracadabrabrabracad
	 *  text:    abacadabrabracabracadabrabrabracad
	 *  pattern:                        rabrabracad
	 *
	 *  % java RabinKarp abacad abacadabrabracabracadabrabrabracad
	 *  text:    abacadabrabracabracadabrabrabracad
	 *  pattern: abacad
     */
    public static void main(String[] args) {
        String pat = args[0];
        String txt = args[1];

        RabinKarp searcher = new RabinKarp(pat);
        int offset = searcher.search(txt);

        // 打印
        StdOut.println("text:    " + txt);

        StdOut.print("pattern: ");
        for (int i = 0; i < offset; i++)
            StdOut.print(" ");
        StdOut.println(pat);
    }
}
