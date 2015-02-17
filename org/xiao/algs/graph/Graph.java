package org.xiao.algs.graph;

import org.xiao.algs.bag.Bag;
import org.xiao.algs.io.In;
import org.xiao.algs.io.StdOut;
import org.xiao.algs.stack.Stack;

/***
 * 
 * 无向图(邻接表实现)
 * 
 * 若需要添加、删除顶点，删除边，检查边是否存在等操作，则需要用到SET代替Bag，称为邻接集
 * 
 * 支持平行边(两点之间多条边)和自环
 * 
 * @author XiaoJian
 *
 */
public class Graph {
	private final int V; // 顶点数目
	private int E; // 边的数目
	private Bag<Integer>[] adj; // 邻接表

	/**
	 * 用V个顶点初始化无向图(不包含边)
	 */
	@SuppressWarnings("unchecked")
	public Graph(int V) {
		if (V < 0)
			throw new IllegalArgumentException(
					"Number of vertices must be nonnegative");
		this.V = V;
		this.E = 0;
		adj = (Bag<Integer>[]) new Bag[V]; // 创建邻接表
		for (int v = 0; v < V; v++) {
			adj[v] = new Bag<Integer>(); // 将所有链表初始化为空
		}
	}

	/**
	 * 从标准输入初始化一个无向图
	 */
	public Graph(In in) {
		this(in.readInt());
		int E = in.readInt();
		if (E < 0)
			throw new IllegalArgumentException(
					"Number of edges must be nonnegative");
		for (int i = 0; i < E; i++) {
			int v = in.readInt();
			int w = in.readInt();
			addEdge(v, w);
		}
	}

	/**
	 * 从已有的图G初始化一个无向图
	 */
	public Graph(Graph G) {
		this(G.V());
		this.E = G.E();
		for (int v = 0; v < G.V(); v++) {
			// reverse so that adjacency list is in same order as original
			Stack<Integer> reverse = new Stack<Integer>();
			for (int w : G.adj[v]) {
				reverse.push(w);
			}
			for (int w : reverse) {
				adj[v].add(w);
			}
		}
	}

	/**
	 * 返回顶点数目
	 */
	public int V() {
		return V;
	}

	/**
	 * 返回边的数目
	 */
	public int E() {
		return E;
	}

	/**
	 * 为无向图添加一条边 v-w
	 */
	public void addEdge(int v, int w) {
		if (v < 0 || v >= V)
			throw new IndexOutOfBoundsException();
		if (w < 0 || w >= V)
			throw new IndexOutOfBoundsException();
		E++;
		adj[v].add(w); // 将w添加到v的链表中
		adj[w].add(v); // 将v添加到w的链表中
	}

	/**
	 * 返回和v相邻的所有顶点
	 */
	public Iterable<Integer> adj(int v) {
		if (v < 0 || v >= V)
			throw new IndexOutOfBoundsException();
		return adj[v];
	}

	/**
	 * 字符串表示
	 */
	public String toString() {
		StringBuilder s = new StringBuilder();
		String NEWLINE = "\n";
		s.append(V + " vertices, " + E + " edges " + NEWLINE);
		for (int v = 0; v < V; v++) {
			s.append(v + ": ");
			for (int w : adj[v]) {
				s.append(w + " ");
			}
			s.append(NEWLINE);
		}
		return s.toString();
	}

	/**
	 * 测试
	 * 
	 * % java Graph tinyG.txt 13 vertices, 13 edges 0: 6 2 1 5 1: 0 2: 0 3: 5 4
	 * 4: 5 6 3 5: 3 4 0 6: 0 4 7: 8 8: 7 9: 11 10 12 10: 9 11: 9 12 12: 11 9
	 *
	 * % java Graph mediumG.txt 250 vertices, 1273 edges 0: 225 222 211 209 204
	 * 202 191 176 163 160 149 114 97 80 68 59 58 49 44 24 15 1: 220 203 200 194
	 * 189 164 150 130 107 72 2: 141 110 108 86 79 51 42 18 14 ...
	 */

	public static void main(String[] args) {
		In in = new In(args[0]);
		Graph G = new Graph(in);
		StdOut.println(G);
	}

}
