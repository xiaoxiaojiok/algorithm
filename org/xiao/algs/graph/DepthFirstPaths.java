package org.xiao.algs.graph;

import org.xiao.algs.io.In;
import org.xiao.algs.io.StdOut;
import org.xiao.algs.stack.Stack;

/***
 * 
 * 使用深度优先搜索查找无向图中的路径
 * 
 * 单点路径
 * 
 * 从起点s到v是否存在一条路径，打印该路径(使用stack)
 * 
 * @author XiaoJian
 *
 */
public class DepthFirstPaths {
	private boolean[] marked; // 这个顶点上调用过dfs吗
	private int[] edgeTo; // 从起点到一个顶点的已知路径上的最后一个顶点
	private final int s; // 起点

	/**
	 * 初始化函数
	 */
	public DepthFirstPaths(Graph G, int s) {
		this.s = s;
		edgeTo = new int[G.V()];
		marked = new boolean[G.V()];
		dfs(G, s);
	}

	// 从v开始深度优先搜索
	private void dfs(Graph G, int v) {
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
		if (!hasPathTo(v))
			return null;
		Stack<Integer> path = new Stack<Integer>();
		for (int x = v; x != s; x = edgeTo[x])
			path.push(x);
		path.push(s);
		return path;
	}

	/**
	 * 测试
	 * 
	 * java Graph tinyCG.txt 6 8 0: 2 1 5 1: 0 2 2: 0 1 3 4 3: 5 4 2 4: 3 2 5: 3
	 * 0
	 *
	 * java DepthFirstPaths tinyCG.txt 0 0 to 0: 0 0 to 1: 0-2-1 0 to 2: 0-2 0
	 * to 3: 0-2-3 0 to 4: 0-2-3-4 0 to 5: 0-2-3-5
	 */
	public static void main(String[] args) {
		In in = new In(args[0]);
		Graph G = new Graph(in);
		int s = Integer.parseInt(args[1]);
		DepthFirstPaths dfs = new DepthFirstPaths(G, s);

		for (int v = 0; v < G.V(); v++) {
			if (dfs.hasPathTo(v)) {
				StdOut.printf("%d to %d:  ", s, v);
				for (int x : dfs.pathTo(v)) {
					if (x == s)
						StdOut.print(x);
					else
						StdOut.print("-" + x);
				}
				StdOut.println();
			}

			else {
				StdOut.printf("%d to %d:  not connected\n", s, v);
			}

		}
	}

}
