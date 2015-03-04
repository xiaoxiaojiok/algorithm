package org.xiao.algs.graph;

import org.xiao.algs.io.In;
import org.xiao.algs.io.StdOut;
import org.xiao.algs.stack.Stack;
/***
 * 
 * 使用深度优先搜索查找有向图中的路径
 * 
 * 单点路径
 * 
 * 从起点s到v是否存在一条有向路径，打印该路径(使用stack)
 * 
 * @author XiaoJian
 *
 */
public class DepthFirstDirectedPaths {
	private boolean[] marked; // 这个顶点上调用过dfs吗
	private int[] edgeTo; // 从起点到一个顶点的已知路径上的最后一个顶点
	private final int s; // 起点

	/**
	 * 初始化函数
	 */
    public DepthFirstDirectedPaths(Digraph G, int s) {
        marked = new boolean[G.V()];
        edgeTo = new int[G.V()];
        this.s = s;
        dfs(G, s);
    }
    
    // 从v开始深度优先搜索
    private void dfs(Digraph G, int v) { 
        marked[v] = true;
        for (int w : G.adj(v)) {
            if (!marked[w]) {
                edgeTo[w] = v;
                dfs(G, w);
            }
        }
    }

	/**
	 * 是否存在从s到v的路径
	 */
    public boolean hasPathTo(int v) {
        return marked[v];
    }

    
    /**
	 * 返回从s到v的路径，若不存在返回null
	 */
    public Iterable<Integer> pathTo(int v) {
        if (!hasPathTo(v)) return null;
        Stack<Integer> path = new Stack<Integer>();
        for (int x = v; x != s; x = edgeTo[x])
            path.push(x);
        path.push(s);
        return path;
    }

    /**
     *  测试
     * 
     *  java DepthFirstDirectedPaths tinyDG.txt 3
	 *  3 to 0:  3-5-4-2-0
	 *  3 to 1:  3-5-4-2-0-1
	 *  3 to 2:  3-5-4-2
	 *  3 to 3:  3
	 *  3 to 4:  3-5-4
	 *  3 to 5:  3-5
	 *  3 to 6:  not connected
	 *  3 to 7:  not connected
	 *  3 to 8:  not connected
	 *  3 to 9:  not connected
	 *  3 to 10:  not connected
	 *  3 to 11:  not connected
	 *  3 to 12:  not connected
     */
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        // StdOut.println(G);

        int s = Integer.parseInt(args[1]);
        DepthFirstDirectedPaths dfs = new DepthFirstDirectedPaths(G, s);

        for (int v = 0; v < G.V(); v++) {
            if (dfs.hasPathTo(v)) {
                StdOut.printf("%d to %d:  ", s, v);
                for (int x : dfs.pathTo(v)) {
                    if (x == s) StdOut.print(x);
                    else        StdOut.print("-" + x);
                }
                StdOut.println();
            }

            else {
                StdOut.printf("%d to %d:  not connected\n", s, v);
            }

        }
    }

}
