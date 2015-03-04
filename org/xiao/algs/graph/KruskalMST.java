package org.xiao.algs.graph;

import org.xiao.algs.io.In;
import org.xiao.algs.io.StdOut;
import org.xiao.algs.queue.MinPQ;
import org.xiao.algs.queue.Queue;
/***
 * 
 * 最小生成树的Kruskal算法实现
 * 
 * PrimMST、KruskalMST
 *  
 * 空间和E成正比，时间和ElogE成正比（最坏情况）
 * 
 * 输入是一幅加权（权值各不相同 保证最小生成树唯一）连通无向图
 * 
 * @author XiaoJian
 *
 */
public class KruskalMST {
    private double weight;  // 权重
    private Queue<Edge> mst = new Queue<Edge>();  // 最小生成树的边

    /**
     * 初始化
     */
    public KruskalMST(EdgeWeightedGraph G) {
        MinPQ<Edge> pq = new MinPQ<Edge>();
        for (Edge e : G.edges()) {
            pq.insert(e);
        }

        // 运行贪心算法
        WeightedQuickUnionUF uf = new WeightedQuickUnionUF(G.V());
        while (!pq.isEmpty() && mst.size() < G.V() - 1) {
            Edge e = pq.delMin();    //从pq得到权重最小的边和它的顶点
            int v = e.either();
            int w = e.other(v);
            if (!uf.connected(v, w)) {    //忽略失效的边 
                uf.union(v, w);  // 合并分量
                mst.enqueue(e);  // 将边添加到最小生成树中
                weight += e.weight();
            }
        }

        // 调试
        assert check(G);
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
    
    //调试
    private boolean check(EdgeWeightedGraph G) {

        // 检测所有权重
        double total = 0.0;
        for (Edge e : edges()) {
            total += e.weight();
        }
        double EPSILON = 1E-12;
        if (Math.abs(total - weight()) > EPSILON) {
            System.err.printf("Weight of edges does not equal weight(): %f vs. %f\n", total, weight());
            return false;
        }

        // 检测是否是无环图
        WeightedQuickUnionUF uf = new WeightedQuickUnionUF(G.V());
        for (Edge e : edges()) {
            int v = e.either(), w = e.other(v);
            if (uf.connected(v, w)) {
                System.err.println("Not a forest");
                return false;
            }
            uf.union(v, w);
        }

        // 检测是否是最小生成森林
        for (Edge e : G.edges()) {
            int v = e.either(), w = e.other(v);
            if (!uf.connected(v, w)) {
                System.err.println("Not a spanning forest");
                return false;
            }
        }

        // check that it is a minimal spanning forest (cut optimality conditions)
        for (Edge e : edges()) {

            // all edges in MST except e
            uf = new WeightedQuickUnionUF(G.V());
            for (Edge f : mst) {
                int x = f.either(), y = f.other(x);
                if (f != e) uf.union(x, y);
            }
            
            // check that e is min weight edge in crossing cut
            for (Edge f : G.edges()) {
                int x = f.either(), y = f.other(x);
                if (!uf.connected(x, y)) {
                    if (f.weight() < e.weight()) {
                        System.err.println("Edge " + f + " violates cut optimality conditions");
                        return false;
                    }
                }
            }

        }

        return true;
    }


    /**
     *  测试
     * 
     *  java KruskalMST tinyEWG.txt 
	 *  0-7 0.16000
	 *  2-3 0.17000
	 *  1-7 0.19000
	 *  0-2 0.26000
	 *  5-7 0.28000
	 *  4-5 0.35000
	 *  6-2 0.40000
	 *  1.81000
	 *
	 *  java KruskalMST mediumEWG.txt
	 *  168-231 0.00268
	 *  151-208 0.00391
	 *  7-157   0.00516
	 *  122-205 0.00647
	 *  8-152   0.00702
	 *  156-219 0.00745
	 *  28-198  0.00775
	 *  38-126  0.00845
	 *  10-123  0.00886
	 *  ...
	 *  10.46351
     */
    public static void main(String[] args) {
        In in = new In(args[0]);
        EdgeWeightedGraph G = new EdgeWeightedGraph(in);
        KruskalMST mst = new KruskalMST(G);
        for (Edge e : mst.edges()) {
            StdOut.println(e);
        }
        StdOut.printf("%.5f\n", mst.weight());
    }

}

