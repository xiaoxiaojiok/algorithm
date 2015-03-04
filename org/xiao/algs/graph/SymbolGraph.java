package org.xiao.algs.graph;

import org.xiao.algs.io.In;
import org.xiao.algs.io.StdIn;
import org.xiao.algs.io.StdOut;
import org.xiao.algs.search.ST;

/***
 * 
 * 无向符号图
 * 
 * 用字符串作为顶点名的无向图
 * 
 * @author XiaoJian
 *
 */
public class SymbolGraph {
    private ST<String, Integer> st;  // 符号表 -> 索引
    private String[] keys;           // 索引  -> 符号表
    private Graph G;                 // 图

    /**  
     * 根据filename指定的文件构造图，使用delimiter来分隔顶点名
     */
    public SymbolGraph(String filename, String delimiter) {
        st = new ST<String, Integer>();

        In in = new In(filename);     					 // 第一遍
        // while (in.hasNextLine()) {
        while (!in.isEmpty()) {        					 // 构造索引
            String[] a = in.readLine().split(delimiter); //读取字符串
            for (int i = 0; i < a.length; i++) {         //为每个不同的字符串关联一个索引
                if (!st.contains(a[i]))
                    st.put(a[i], st.size());
            }
        }
        StdOut.println("Done reading " + filename);

        keys = new String[st.size()];      //用来获得顶点名的反向索引是一个数组
        for (String name : st.keys()) {
            keys[st.get(name)] = name;
        }

        G = new Graph(st.size());  //第二遍
        in = new In(filename);   //构造图
        while (in.hasNextLine()) {
            String[] a = in.readLine().split(delimiter); //将每一行的第一个顶点和该行的其他顶点相连
            int v = st.get(a[0]);
            for (int i = 1; i < a.length; i++) {
                int w = st.get(a[i]);
                G.addEdge(v, w);
            }
        }
    }

    /**
     * 图包含s吗
     */
    public boolean contains(String s) {
        return st.contains(s);
    }

    /**
     * 返回s的索引
     */
    public int index(String s) {
        return st.get(s);
    }

    /**
     * 返回索引v的顶点名
     */
    public String name(int v) {
        return keys[v];
    }

    /**
     * 返回Graph对象
     */
    public Graph G() {
        return G;
    }


    /**
     * 测试
     * 
     *  java SymbolGraph routes.txt " "
	 *  JFK
	 *     MCO
	 *     ATL
	 *     ORD
	 *  LAX
	 *     PHX
	 *     LAS
	 *
	 *  java SymbolGraph movies.txt "/"
	 *  Tin Men (1987)
	 *     Hershey, Barbara
	 *     Geppi, Cindy
	 *     Jones, Kathy (II)
	 *     Herr, Marcia
	 *     ...
	 *     Blumenfeld, Alan
	 *     DeBoy, David
	 *  Bacon, Kevin
	 *     Woodsman, The (2004)
	 *     Wild Things (1998)
	 *     Where the Truth Lies (2005)
	 *     Tremors (1990)
	 *     ...
	 *     Apollo 13 (1995)
	 *     Animal House (1978)
     */
    public static void main(String[] args) {
        String filename  = args[0];
        String delimiter = args[1];
        SymbolGraph sg = new SymbolGraph(filename, delimiter);
        Graph G = sg.G();
        while (StdIn.hasNextLine()) {
            String source = StdIn.readLine();
            if (sg.contains(source)) {
                int s = sg.index(source);
                for (int v : G.adj(s)) {
                    StdOut.println("   " + sg.name(v));
                }
            }
            else {
                StdOut.println("input not contain '" + source + "'");
            }
        }
    }
}
