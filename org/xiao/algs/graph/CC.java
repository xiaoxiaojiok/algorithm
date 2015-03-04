package org.xiao.algs.graph;

import org.xiao.algs.io.In;
import org.xiao.algs.io.StdOut;
import org.xiao.algs.queue.Queue;

/***
 * 
 * 使用深度优先搜索找出无向图中的所有连通分量
 * 
 * 和union-find算法类似，理论上深度优先搜索更快，因为它保证所需时间是常数
 * 但在实际应用中union-find算法更快，因为不需要完整地构造并表示一张图 更重要是union-find是一种动态算法，不需要对图进行预处理
 * 在只需要判断连通性或者需要完成大量连通性查询和插入操作混合时候倾向使用union-find算法，
 * 而深度优先搜索更适合于实现图的抽象数据结构，因为它更加有效地利用了已有的数据结构
 * 
 * @author XiaoJian
 *
 */
public class CC {
	private boolean[] marked; // 标记顶点
	private int[] id; // id[v] = v所在的连通分量的标记符
	private int[] size; // 每个连通分量里的顶点数目
	private int count; // 连通分量的数目

	/**
	 * 预处理构造函数
	 */
	public CC(Graph G) {
		marked = new boolean[G.V()];
		id = new int[G.V()];
		size = new int[G.V()];
		for (int v = 0; v < G.V(); v++) {
			if (!marked[v]) {
				dfs(G, v);
				count++;
			}
		}
	}

	// 深度优先搜索
	private void dfs(Graph G, int v) {
		marked[v] = true;
		id[v] = count;
		size[count]++;
		for (int w : G.adj(v)) {
			if (!marked[w]) {
				dfs(G, w);
			}
		}
	}

	/**
	 * v所在的连通分量的标记符(0 ~ count-1)
	 */
	public int id(int v) {
		return id[v];
	}

	/**
	 * Returns the number of vertices in the connected component containing
	 * vertex <tt>v</tt>.
	 * 
	 * @param v
	 *            the vertex
	 * @return the number of vertices in the connected component containing
	 *         vertex <tt>v</tt>
	 */
	public int size(int v) {
		return size[id[v]];
	}

	/**
	 * 返回连通分量的数目
	 */
	public int count() {
		return count;
	}

	/**
	 * v和w连通吗
	 */
	public boolean connected(int v, int w) {
		return id(v) == id(w);
	}

	/**
	 * v和w连通吗
	 * 
	 * @deprecated Use connected(v, w) instead.
	 */
	public boolean areConnected(int v, int w) {
		return id(v) == id(w);
	}

	/**
	 * 测试
	 * 
	 * java CC tinyG.txt 3 components 0 1 2 3 4 5 6 7 8 9 10 11 12
	 *
	 * java CC mediumG.txt 1 components 0 1 2 3 4 5 6 7 8 9 10 ...
	 *
	 * java -Xss50m CC largeG.txt 1 components 0 1 2 3 4 5 6 7 8 9 10 ...
	 */
	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		In in = new In(args[0]);
		Graph G = new Graph(in);
		CC cc = new CC(G);

		// 连通分量的数目
		int M = cc.count();
		StdOut.println(M + " components");

		// 计算每个连通分量里面的顶点
		Queue<Integer>[] components = (Queue<Integer>[]) new Queue[M];
		for (int i = 0; i < M; i++) {
			components[i] = new Queue<Integer>();
		}
		for (int v = 0; v < G.V(); v++) {
			components[cc.id(v)].enqueue(v);
		}

		// 打印结果
		for (int i = 0; i < M; i++) {
			for (int v : components[i]) {
				StdOut.print(v + " ");
			}
			StdOut.println();
		}
	}
}
