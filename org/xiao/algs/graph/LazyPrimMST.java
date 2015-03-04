package org.xiao.algs.graph;

import org.xiao.algs.io.In;
import org.xiao.algs.io.StdOut;
import org.xiao.algs.queue.MinPQ;
import org.xiao.algs.queue.Queue;
/***
 * 
 * 最小生成树的Prim算法的延时实现
 * 
 * 空间和E成正比，时间和ElogE成正比（最坏情况）
 * 
 * 输入是一幅加权（权值各不相同 保证最小生成树唯一）连通无向图
 *  
 * @author XiaoJian
 *
 */
public class LazyPrimMST {
    private double weight;       // 权重
    private Queue<Edge> mst;     // 最小生成树的边
    private boolean[] marked;    // 最小生成树的顶点
    private MinPQ<Edge> pq;      // 横切边（包含失效的边）

    /**
     * 初始化
     */
    public LazyPrimMST(EdgeWeightedGraph G) {
        mst = new Queue<Edge>();
        pq = new MinPQ<Edge>();
        marked = new boolean[G.V()];
        for (int v = 0; v < G.V(); v++)     
            if (!marked[v]) prim(G, v);     

    }

    // 运行Prim算法
    private void prim(EdgeWeightedGraph G, int s) {
        scan(G, s);
        while (!pq.isEmpty()) {                        
            Edge e = pq.delMin();                      // 从pq中得到权重最小的边
            int v = e.either(), w = e.other(v);        // 得到两个端点
            assert marked[v] || marked[w];
            if (marked[v] && marked[w]) continue;      // 跳过失效的边
            mst.enqueue(e);                            // 将边添加到树中
            weight += e.weight();
            if (!marked[v]) scan(G, v);               // 将顶点v或者w添加到树中
            if (!marked[w]) scan(G, w);               
        }
    }

    // 标记顶点v并将所有连接v和未被标记顶点的边加入pq
    private void scan(EdgeWeightedGraph G, int v) {
        assert !marked[v];
        marked[v] = true;
        for (Edge e : G.adj(v))
            if (!marked[e.other(v)]) pq.insert(e);
    }
        
    /**
     * 返回最小生成树的所有边
     */
    public Iterable<Edge> edges() {
        return mst;
    }

    /**
     * 返回最小生成树的权重
     */
    public double weight() {
        return weight;
    }

    
    /**
     *  测试
     * 
     *  java LazyPrimMST tinyEWG.txt 
	 *  0-7 0.16000
	 *  1-7 0.19000
	 *  0-2 0.26000
	 *  2-3 0.17000
	 *  5-7 0.28000
	 *  4-5 0.35000
	 *  6-2 0.40000
	 *  1.81000
	 *
	 *  java LazyPrimMST mediumEWG.txt
	 *  0-225   0.02383
	 *  49-225  0.03314
	 *  44-49   0.02107
	 *  44-204  0.01774
	 *  49-97   0.03121
	 *  202-204 0.04207
	 *  176-202 0.04299
	 *  176-191 0.02089
	 *  68-176  0.04396
	 *  58-68   0.04795
	 *  10.46351
	 *
	 *  java LazyPrimMST largeEWG.txt
	 *  ...
	 *  647.66307
     */
    public static void main(String[] args) {
        In in = new In(args[0]);
        EdgeWeightedGraph G = new EdgeWeightedGraph(in);
        LazyPrimMST mst = new LazyPrimMST(G);
        for (Edge e : mst.edges()) {
            StdOut.println(e);
        }
        StdOut.printf("%.5f\n", mst.weight());
    }

}
