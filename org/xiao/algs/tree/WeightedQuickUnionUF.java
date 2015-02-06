package org.xiao.algs.tree;

import org.xiao.algs.io.StdIn;
import org.xiao.algs.io.StdOut;

/***
 * 加权动态连通性 quick-union 算法实现（算法平均复杂度NlogN线性对数型）
 * 
 * @author XiaoJian
 *
 */
public class WeightedQuickUnionUF {
	private int[] id; // 父链接数组（由触点索引组成）
	private int[] sz; // s由触点索引的各个根节点多对应的分量的大小（权重，将权重小的连接到权重大的，即小树连接大树）
	private int count; // 连通分量的数量（要使得连通分量为1，至少需要N-1个整数对）

	/**
	 * 以整数标志（0到N）初始化N个触点
	 */
	public WeightedQuickUnionUF(int N) {
		count = N;
		id = new int[N];
		sz = new int[N];
		for (int i = 0; i < N; i++) {
			id[i] = i;
			sz[i] = 1;
		}
	}

	/**
	 * 返回数量
	 */
	public int count() {
		return count;
	}

	/**
	 * p所在分量的标志符
	 */
	public int find(int p) {
		// 跟随连接找到根节点
		while (p != id[p])
			p = id[p];
		return p;
	}

	/**
	 * 如果p和q存在于同一个分量重则返回true
	 */
	public boolean connected(int p, int q) {
		return find(p) == find(q);
	}

	/**
	 * 在p和q之间添加一条连接
	 */
	public void union(int p, int q) {
		int rootP = find(p);
		int rootQ = find(q);
		if (rootP == rootQ)
			return;

		// 将小树的根节点连接到大树的根节点
		if (sz[rootP] < sz[rootQ]) {
			id[rootP] = rootQ;
			sz[rootQ] += sz[rootP];
		} else {
			id[rootQ] = rootP;
			sz[rootP] += sz[rootQ];
		}
		count--;
	}

	/**
	 * 测试
	 * java WeightedQuickUnionUK < tinyUF.txt
	 * java WeightedQuickUnionUK < mediumUF.txt (900)
	 * jaav WeightedQuickUnionUK < largeUF.txt  (200万)
	 */
	public static void main(String[] args) {
		int N = StdIn.readInt();
		WeightedQuickUnionUF uf = new WeightedQuickUnionUF(N);
		while (!StdIn.isEmpty()) {
			int p = StdIn.readInt();
			int q = StdIn.readInt();
			if (uf.connected(p, q))
				continue;
			uf.union(p, q);
			StdOut.println(p + " " + q);
		}
		StdOut.println(uf.count() + " components");
	}

}
