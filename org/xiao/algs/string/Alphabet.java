package org.xiao.algs.string;

import org.xiao.algs.io.StdOut;

/***
 * 
 * 字母表
 * 
 * 用字符索引的数组能够提高算法的效率
 * 
 * 若使用Java的String类，则必须使用一个65536的数组，有了Alphabet类则只需要一个字母表大小的数组即可
 * 
 * @author XiaoJian
 *
 */

public class Alphabet {
	//各种内置的字母表
    public static final Alphabet BINARY         = new Alphabet("01");
    public static final Alphabet OCTAL          = new Alphabet("01234567");
    public static final Alphabet DECIMAL        = new Alphabet("0123456789");
    public static final Alphabet HEXADECIMAL    = new Alphabet("0123456789ABCDEF");
    public static final Alphabet DNA            = new Alphabet("ACTG");
    public static final Alphabet LOWERCASE      = new Alphabet("abcdefghijklmnopqrstuvwxyz");
    public static final Alphabet UPPERCASE      = new Alphabet("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
    public static final Alphabet PROTEIN        = new Alphabet("ACDEFGHIKLMNPQRSTVWY");
    public static final Alphabet BASE64         = new Alphabet("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/");
    public static final Alphabet ASCII          = new Alphabet(128);  //ASCII字符集
    public static final Alphabet EXTENDED_ASCII = new Alphabet(256);  //扩展ASCII字符集
    public static final Alphabet UNICODE16      = new Alphabet(65536);//Unicode字符集

    private char[] alphabet;     // 字母表中的字符
    private int[] inverse;       // 字符索引
    private int R;               // 基数（字母表的字符数量）

    // 根据s中的字符创建一张新的字母表
    public Alphabet(String alpha) {

        // 检测字母表没有重复的字符
        boolean[] unicode = new boolean[Character.MAX_VALUE];
        for (int i = 0; i < alpha.length(); i++) {
            char c = alpha.charAt(i);
            if (unicode[c])
                throw new IllegalArgumentException("Illegal alphabet: repeated character = '" + c + "'");
            unicode[c] = true;
        }

        alphabet = alpha.toCharArray();
        R = alpha.length();
        inverse = new int[Character.MAX_VALUE];
        for (int i = 0; i < inverse.length; i++)
            inverse[i] = -1;

        for (int c = 0; c < R; c++)
            inverse[alphabet[c]] = c;
    }

    //创建一张新的字母表 0 to R-1
    private Alphabet(int R) {
        alphabet = new char[R];
        inverse = new int[R];
        this.R = R;

        // can't use char since R can be as big as 65,536
        for (int i = 0; i < R; i++)
            alphabet[i] = (char) i;
        for (int i = 0; i < R; i++)
            inverse[i] = i;
    }

    // 创建一张新的字母表 chars 0 to 255 (扩展的ASCII)
    public Alphabet() {
        this(256);
    }

    // c在字母表中吗
    public boolean contains(char c) {
        return inverse[c] != -1;
    }

    // 返回基数R（字母表中的字符数量）
    public int R() {
        return R;
    }

    // 表示一个字母表所需要的比特数
    public int lgR() {
        int lgR = 0;
        for (int t = R-1; t >= 1; t /= 2)
            lgR++;
        return lgR;
    }

    // 获取c的索引 0 and R-1 之间
    public int toIndex(char c) {
        if (c < 0 || c >= inverse.length || inverse[c] == -1) {
            throw new IllegalArgumentException("Character " + c + " not in alphabet");
        }
        return inverse[c];
    }

    // 将s转换成R进制的整数
    public int[] toIndices(String s) {
        char[] source = s.toCharArray();
        int[] target  = new int[s.length()];
        for (int i = 0; i < source.length; i++)
            target[i] = toIndex(source[i]);
        return target;
    }

    // 获取字母表中索引位置的字符
    public char toChar(int index) {
        if (index < 0 || index >= R) {
            throw new IndexOutOfBoundsException("Alphabet index out of bounds");
        }
        return alphabet[index];
    }

    //将R进制的整数转换成基于该字母表的字符串
    public String toChars(int[] indices) {
        StringBuilder s = new StringBuilder(indices.length);
        for (int i = 0; i < indices.length; i++)
            s.append(toChar(indices[i]));
        return s.toString();
    }


    /***
     * 测试
     */
    public static void main(String[] args) {
        int[]  encoded1 = Alphabet.BASE64.toIndices("NowIsTheTimeForAllGoodMen");
        for(int i=0;i<encoded1.length;i++){
        	System.out.println(encoded1[i]);
        }
        String decoded1 = Alphabet.BASE64.toChars(encoded1);
        StdOut.println(decoded1);
 
        int[]  encoded2 = Alphabet.DNA.toIndices("AACGAACGGTTTACCCCG");
        String decoded2 = Alphabet.DNA.toChars(encoded2);
        StdOut.println(decoded2);

        int[]  encoded3 = Alphabet.DECIMAL.toIndices("01234567890123456789");
        String decoded3 = Alphabet.DECIMAL.toChars(encoded3);
        StdOut.println(decoded3);
    }
}
