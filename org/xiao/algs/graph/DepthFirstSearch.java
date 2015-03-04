package org.xiao.algs.graph;

import org.xiao.algs.io.In;
import org.xiao.algs.io.StdOut;

/***
 * 
 * 深度优先搜索
 * 
 * 单点连通性
 * 
 * 判断两个顶点是否连通，打印所有和s连通的顶点，判断无向图的连通性
 * 
 * 还可以找出连通分量、检测环、双色问题(是否二分图)
 * 
 * @author XiaoJian
 *
 */
public class DepthFirstSearch {
	private boolean[] marked; // 标记走过的顶点
	private int count; // 和s连通的顶点数目

	/**
	 * 找到和起点s连通的所有顶点
	 */
	public DepthFirstSearch(Graph G, int s) {
		marked = new boolean[G.V()];
		dfs(G, s);
	}

	// 从v开始深度优先搜索
	private void dfs(Graph G, int v) {
		count++;
		marked[v] = true;
		for (int w : G.adj(v)) {
			if (!marked[w]) {
				dfs(G, w);
			}
		}
	}

	/**
	 * v和s是连通的吗
	 */
	public boolean marked(int v) {
		return marked[v];
	}

	/**
	 * 和s连通的顶点的总数
	 */
	public int count() {
		return count;
	}

	/**
	 * 测试
	 * 
	 * Runs in O(E + V) time.
	 *
	 * java DepthFirstSearch tinyG.txt 0 0 1 2 3 4 5 6 NOT connected
	 *
	 * java DepthFirstSearch tinyG.txt 9 9 10 11 12 NOT connected
	 */
	public static void main(String[] args) {
		In in = new In(args[0]);
		Graph G = new Graph(in);
		int s = Integer.parseInt(args[1]);
		DepthFirstSearch search = new DepthFirstSearch(G, s);
		for (int v = 0; v < G.V(); v++) {
			// 打印和s连通的顶点
			if (search.marked(v))
				StdOut.print(v + " ");
		}

		StdOut.println();

		// 判断无向图的连通性
		if (search.count() != G.V())
			StdOut.println("NOT connected");
		else
			StdOut.println("connected");
	}

}
