package org.xiao.algs.graph;

import org.xiao.algs.io.In;
import org.xiao.algs.io.StdOut;
import org.xiao.algs.queue.Queue;
import org.xiao.algs.stack.Stack;

/***
 * 
 * 使用广度优先搜索查找无向图中的路径
 * 
 * 从起点s到v是否存在一条路径，打印最短的一条（所含的边数最少）(使用队列)
 * 
 * @author XiaoJian
 *
 */
public class BreadthFirstPaths {
	private static final int INFINITY = Integer.MAX_VALUE;
	private boolean[] marked; // 标记顶点
	private int[] edgeTo; // 到达该顶点的已知路径上的最后一个顶点
	private int[] distTo; // 起点到v的最短路径的长度

	/**
	 * 单点初始化
	 */
	public BreadthFirstPaths(Graph G, int s) {
		marked = new boolean[G.V()];
		distTo = new int[G.V()];
		edgeTo = new int[G.V()];
		bfs(G, s);

		assert check(G, s);
	}

	/**
	 * 多点初始化
	 */
	public BreadthFirstPaths(Graph G, Iterable<Integer> sources) {
		marked = new boolean[G.V()];
		distTo = new int[G.V()];
		edgeTo = new int[G.V()];
		for (int v = 0; v < G.V(); v++)
			distTo[v] = INFINITY;
		bfs(G, sources);
	}

	// 从单点进行广度优先搜索
	private void bfs(Graph G, int s) {
		Queue<Integer> q = new Queue<Integer>();
		for (int v = 0; v < G.V(); v++)
			distTo[v] = INFINITY;
		distTo[s] = 0;
		marked[s] = true;
		q.enqueue(s);

		while (!q.isEmpty()) {
			int v = q.dequeue();
			for (int w : G.adj(v)) {
				if (!marked[w]) {
					edgeTo[w] = v;
					distTo[w] = distTo[v] + 1;
					marked[w] = true;
					q.enqueue(w);
				}
			}
		}
	}

	// 从多点进行广度优先搜索
	private void bfs(Graph G, Iterable<Integer> sources) {
		Queue<Integer> q = new Queue<Integer>();
		for (int s : sources) {
			marked[s] = true;
			distTo[s] = 0;
			q.enqueue(s);
		}
		while (!q.isEmpty()) {
			int v = q.dequeue();
			for (int w : G.adj(v)) {
				if (!marked[w]) {
					edgeTo[w] = v;
					distTo[w] = distTo[v] + 1;
					marked[w] = true;
					q.enqueue(w);
				}
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
	 * 返回从起点s到v的最短路径的长度
	 */
	public int distTo(int v) {
		return distTo[v];
	}

	/**
	 * 返回从s到v的最短路径，若不存在返回null
	 */
	public Iterable<Integer> pathTo(int v) {
		if (!hasPathTo(v))
			return null;
		Stack<Integer> path = new Stack<Integer>();
		int x;
		for (x = v; distTo[x] != 0; x = edgeTo[x])
			path.push(x);
		path.push(x);
		return path;
	}

	// 调试
	private boolean check(Graph G, int s) {

		// check that the distance of s = 0
		if (distTo[s] != 0) {
			StdOut.println("distance of source " + s + " to itself = "
					+ distTo[s]);
			return false;
		}

		// check that for each edge v-w dist[w] <= dist[v] + 1
		// provided v is reachable from s
		for (int v = 0; v < G.V(); v++) {
			for (int w : G.adj(v)) {
				if (hasPathTo(v) != hasPathTo(w)) {
					StdOut.println("edge " + v + "-" + w);
					StdOut.println("hasPathTo(" + v + ") = " + hasPathTo(v));
					StdOut.println("hasPathTo(" + w + ") = " + hasPathTo(w));
					return false;
				}
				if (hasPathTo(v) && (distTo[w] > distTo[v] + 1)) {
					StdOut.println("edge " + v + "-" + w);
					StdOut.println("distTo[" + v + "] = " + distTo[v]);
					StdOut.println("distTo[" + w + "] = " + distTo[w]);
					return false;
				}
			}
		}

		// check that v = edgeTo[w] satisfies distTo[w] + distTo[v] + 1
		// provided v is reachable from s
		for (int w = 0; w < G.V(); w++) {
			if (!hasPathTo(w) || w == s)
				continue;
			int v = edgeTo[w];
			if (distTo[w] != distTo[v] + 1) {
				StdOut.println("shortest path edge " + v + "-" + w);
				StdOut.println("distTo[" + v + "] = " + distTo[v]);
				StdOut.println("distTo[" + w + "] = " + distTo[w]);
				return false;
			}
		}

		return true;
	}

	/**
	 * 测试
	 * 
	 * java Graph tinyCG.txt 6 8 0: 2 1 5 1: 0 2 2: 0 1 3 4 3: 5 4 2 4: 3 2 5: 3
	 * 0
	 *
	 * % java BreadthFirstPaths tinyCG.txt 0 0 to 0 (0): 0 0 to 1 (1): 0-1 0 to
	 * 2 (1): 0-2 0 to 3 (2): 0-2-3 0 to 4 (2): 0-2-4 0 to 5 (1): 0-5
	 *
	 * java BreadthFirstPaths largeG.txt 0 0 to 0 (0): 0 0 to 1 (418):
	 * 0-932942-474885-82707-879889-971961-... 0 to 2 (323):
	 * 0-460790-53370-594358-780059-287921-... 0 to 3 (168):
	 * 0-713461-75230-953125-568284-350405-... 0 to 4 (144):
	 * 0-460790-53370-310931-440226-380102-... 0 to 5 (566):
	 * 0-932942-474885-82707-879889-971961-... 0 to 6 (349):
	 * 0-932942-474885-82707-879889-971961-...
	 */
	public static void main(String[] args) {
		In in = new In(args[0]);
		Graph G = new Graph(in);
		// StdOut.println(G);

		int s = Integer.parseInt(args[1]);
		BreadthFirstPaths bfs = new BreadthFirstPaths(G, s);

		for (int v = 0; v < G.V(); v++) {
			if (bfs.hasPathTo(v)) {
				StdOut.printf("%d to %d (%d):  ", s, v, bfs.distTo(v));
				for (int x : bfs.pathTo(v)) {
					if (x == s)
						StdOut.print(x);
					else
						StdOut.print("-" + x);
				}
				StdOut.println();
			}

			else {
				StdOut.printf("%d to %d (-):  not connected\n", s, v);
			}

		}
	}

}
