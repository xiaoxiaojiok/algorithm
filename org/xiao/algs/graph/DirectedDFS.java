package org.xiao.algs.graph;

import org.xiao.algs.bag.Bag;
import org.xiao.algs.io.In;
import org.xiao.algs.io.StdOut;

/***
 * 
 * 使用深度优先搜索实现有向图的可达性
 * 
 * 可以判断给定的一个或者一组顶点能到达哪些顶点
 * 
 * @author XiaoJian
 *
 */
public class DirectedDFS {
    private boolean[] marked;  // 标记v是否可达
    private int count;         // 从s开始可达顶点的数目

    /**
     * 在G中找到从s可达的所有顶点
     */
    public DirectedDFS(Digraph G, int s) {
        marked = new boolean[G.V()];
        dfs(G, s);
    }

    /**
     * 在G中找到从sources中的所有顶点可达的所有顶点
     */
    public DirectedDFS(Digraph G, Iterable<Integer> sources) {
        marked = new boolean[G.V()];
        for (int v : sources) {
            if (!marked[v]) dfs(G, v);
        }
    }

    //深度优先搜索
    private void dfs(Digraph G, int v) { 
        count++;
        marked[v] = true;
        for (int w : G.adj(v)) {
            if (!marked[w]) dfs(G, w);
        }
    }

    /**
     * v是可达的吗
     */
    public boolean marked(int v) {
        return marked[v];
    }

    /**
     * 返回可达的顶点数目
     */
    public int count() {
        return count;
    }

    /**
     *  测试
     * 
     *  java DirectedDFS tinyDG.txt 1
	 *  1
	 *
	 *  java DirectedDFS tinyDG.txt 2
	 *  0 1 2 3 4 5
	 *
	 *  java DirectedDFS tinyDG.txt 1 2 6
	 *  0 1 2 3 4 5 6 8 9 10 11 12 
     */
    public static void main(String[] args) {

        // 从命令行读取图
        In in = new In(args[0]);
        Digraph G = new Digraph(in);

        // 读取给定的一组顶点
        Bag<Integer> sources = new Bag<Integer>();
        for (int i = 1; i < args.length; i++) {
            int s = Integer.parseInt(args[i]);
            sources.add(s);
        }

        // 多点可达性
        DirectedDFS dfs = new DirectedDFS(G, sources);

        // 打印可到达的顶点
        for (int v = 0; v < G.V(); v++) {
            if (dfs.marked(v)) StdOut.print(v + " ");
        }
        StdOut.println();
    }

}
