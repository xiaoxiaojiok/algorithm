package org.xiao.algs.graph;

import org.xiao.algs.io.In;
import org.xiao.algs.io.StdIn;
import org.xiao.algs.io.StdOut;
import org.xiao.algs.search.ST;
/***
 * 
 * 有向符号图
 * 
 * 用字符串作为顶点名的有向图
 * 
 * @author XiaoJian
 *
 */
public class SymbolDigraph {
    private ST<String, Integer> st;  // 符号表 -> 索引
    private String[] keys;           // 索引  -> 符号表
    private Digraph G;               // 图


    /**  
     * 根据filename指定的文件构造图，使用delimiter来分隔顶点名
     */
    public SymbolDigraph(String filename, String delimiter) {
        st = new ST<String, Integer>();

       
        In in = new In(filename);         // 第一遍
        while (in.hasNextLine()) {        // 构造索引
            String[] a = in.readLine().split(delimiter);  //读取字符串
            for (int i = 0; i < a.length; i++) {          //为每个不同的字符串关联一个索引
                if (!st.contains(a[i]))
                    st.put(a[i], st.size());
            }
        }

        keys = new String[st.size()];  //用来获得顶点名的反向索引是一个数组
        for (String name : st.keys()) {
            keys[st.get(name)] = name;
        }

        G = new Digraph(st.size());   //第二遍
        in = new In(filename);       //构造图
        while (in.hasNextLine()) {
            String[] a = in.readLine().split(delimiter);  //将每一行的第一个顶点和该行的其他顶点相连
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
    public Digraph G() {
        return G;
    }


    /**
     * 测试
     * 
     * java SymbolDigraph routes.txt " "
	 *  JFK
	 *     MCO
	 *     ATL
	 *     ORD
	 *  ATL
	 *     HOU
	 *     MCO
	 *  LAX
     */
    public static void main(String[] args) {
        String filename  = args[0];
        String delimiter = args[1];
        SymbolDigraph sg = new SymbolDigraph(filename, delimiter);
        Digraph G = sg.G();
        while (!StdIn.isEmpty()) {
            String t = StdIn.readLine();
            for (int v : G.adj(sg.index(t))) {
                StdOut.println("   " + sg.name(v));
            }
        }
    }
}
