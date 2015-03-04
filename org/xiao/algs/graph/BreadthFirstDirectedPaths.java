package org.xiao.algs.graph;

import org.xiao.algs.io.In;
import org.xiao.algs.io.StdOut;
import org.xiao.algs.queue.Queue;
import org.xiao.algs.stack.Stack;
/***
 * 
 * 使用广度优先搜索查找有向图中的路径
 * 
 * 从起点s到v是否存在一条有向路径，打印最短的一条（所含的边数最少）(使用队列)
 * 
 * @author XiaoJian
 *
 */
public class BreadthFirstDirectedPaths {
    private static final int INFINITY = Integer.MAX_VALUE;
	private boolean[] marked; // 标记顶点
	private int[] edgeTo; // s -> v的有向路径上的最后一个顶点
	private int[] distTo; // s -> v的有向最短路径的长度

	/**
	 * 单点初始化
	 */
    public BreadthFirstDirectedPaths(Digraph G, int s) {
        marked = new boolean[G.V()];
        distTo = new int[G.V()];
        edgeTo = new int[G.V()];
        for (int v = 0; v < G.V(); v++) distTo[v] = INFINITY;
        bfs(G, s);
    }

	/**
	 * 多点初始化
	 */
    public BreadthFirstDirectedPaths(Digraph G, Iterable<Integer> sources) {
        marked = new boolean[G.V()];
        distTo = new int[G.V()];
        edgeTo = new int[G.V()];
        for (int v = 0; v < G.V(); v++) distTo[v] = INFINITY;
        bfs(G, sources);
    }

    // 从单点进行广度优先搜索
    private void bfs(Digraph G, int s) {
        Queue<Integer> q = new Queue<Integer>();
        marked[s] = true;
        distTo[s] = 0;
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
    private void bfs(Digraph G, Iterable<Integer> sources) {
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
     * 是否存在从s到v的有向路径
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
        if (!hasPathTo(v)) return null;
        Stack<Integer> path = new Stack<Integer>();
        int x;
        for (x = v; distTo[x] != 0; x = edgeTo[x])
            path.push(x);
        path.push(x);
        return path;
    }

    /**
     * 测试
     * 
     * java BreadthFirstDirectedPaths tinyDG.txt 3
	 *  3 to 0 (2):  3->2->0
	 *  3 to 1 (3):  3->2->0->1
	 *  3 to 2 (1):  3->2
	 *  3 to 3 (0):  3
	 *  3 to 4 (2):  3->5->4
	 *  3 to 5 (1):  3->5
	 *  3 to 6 (-):  not connected
	 *  3 to 7 (-):  not connected
	 *  3 to 8 (-):  not connected
	 *  3 to 9 (-):  not connected
	 *  3 to 10 (-):  not connected
	 *  3 to 11 (-):  not connected
	 *  3 to 12 (-):  not connected
     */
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        // StdOut.println(G);

        int s = Integer.parseInt(args[1]);
        BreadthFirstDirectedPaths bfs = new BreadthFirstDirectedPaths(G, s);

        for (int v = 0; v < G.V(); v++) {
            if (bfs.hasPathTo(v)) {
                StdOut.printf("%d to %d (%d):  ", s, v, bfs.distTo(v));
                for (int x : bfs.pathTo(v)) {
                    if (x == s) StdOut.print(x);
                    else        StdOut.print("->" + x);
                }
                StdOut.println();
            }

            else {
                StdOut.printf("%d to %d (-):  not connected\n", s, v);
            }

        }
    }


}
