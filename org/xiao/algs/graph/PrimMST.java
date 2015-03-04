package org.xiao.algs.graph;

import org.xiao.algs.io.In;
import org.xiao.algs.io.StdOut;
import org.xiao.algs.queue.IndexMinPQ;
import org.xiao.algs.queue.Queue;

/***
 * 
 * 最小生成树的Prim算法的即时实现
 * 
 * PrimMST、KruskalMST
 * 
 * 空间和V成正比，时间和ElogV成正比（最坏情况）
 * 
 * 输入是一幅加权（权值各不相同 保证最小生成树唯一）连通无向图
 *  
 * @author XiaoJian
 *
 */
public class PrimMST {
    private Edge[] edgeTo;        // 距离树最近的边
    private double[] distTo;      // distTo[v] = 这条最短边的权重
    private boolean[] marked;     // marked[v] = 如果v在树上
    private IndexMinPQ<Double> pq; //有效的横切边（使用索引优先队列）

    /**
     * 初始化
     */
    public PrimMST(EdgeWeightedGraph G) {
        edgeTo = new Edge[G.V()];
        distTo = new double[G.V()];
        marked = new boolean[G.V()];
        pq = new IndexMinPQ<Double>(G.V());
        for (int v = 0; v < G.V(); v++) distTo[v] = Double.POSITIVE_INFINITY;

        for (int v = 0; v < G.V(); v++)      // run from each vertex to find
            if (!marked[v]) prim(G, v);      // minimum spanning forest

    }

    // 从s为起点运行Prim算法
    private void prim(EdgeWeightedGraph G, int s) {
        distTo[s] = 0.0;
        pq.insert(s, distTo[s]);  //用顶点s和权重0.0初始化pq
        while (!pq.isEmpty()) {
            int v = pq.delMin();
            scan(G, v);  //将最近的顶点添加到树中
        }
    }

    private void scan(EdgeWeightedGraph G, int v) {
        marked[v] = true;
        for (Edge e : G.adj(v)) {
            int w = e.other(v);
            if (marked[w]) continue;         // v-w 失效
            if (e.weight() < distTo[w]) {
            	//连接w和树的最佳边Edge变为e
                distTo[w] = e.weight();
                edgeTo[w] = e;
                if (pq.contains(w)) pq.decreaseKey(w, distTo[w]);
                else                pq.insert(w, distTo[w]);
            }
        }
    }

    /**
     * 返回最小生成树的所有边
     */
    public Iterable<Edge> edges() {
        Queue<Edge> mst = new Queue<Edge>();
        for (int v = 0; v < edgeTo.length; v++) {
            Edge e = edgeTo[v];
            if (e != null) {
                mst.enqueue(e);
            }
        }
        return mst;
    }

    /**
     * 返回最小生成树的权重
     */
    public double weight() {
        double weight = 0.0;
        for (Edge e : edges())
            weight += e.weight();
        return weight;
    }

    /**
     *  测试
     * 
     *  java PrimMST tinyEWG.txt 
	 *  1-7 0.19000
	 *  0-2 0.26000
	 *  2-3 0.17000
	 *  4-5 0.35000
	 *  5-7 0.28000
	 *  6-2 0.40000
	 *  0-7 0.16000
	 *  1.81000
	 *
	 *  java PrimMST mediumEWG.txt
	 *  1-72   0.06506
	 *  2-86   0.05980
	 *  3-67   0.09725
	 *  4-55   0.06425
	 *  5-102  0.03834
	 *  6-129  0.05363
	 *  7-157  0.00516
	 *  ...
	 *  10.46351
	 *
	 *  java PrimMST largeEWG.txt
	 *  ...
	 *  647.66307
     */
    public static void main(String[] args) {
        In in = new In(args[0]);
        EdgeWeightedGraph G = new EdgeWeightedGraph(in);
        PrimMST mst = new PrimMST(G);
        for (Edge e : mst.edges()) {
            StdOut.println(e);
        }
        StdOut.printf("%.5f\n", mst.weight());
    }


}
