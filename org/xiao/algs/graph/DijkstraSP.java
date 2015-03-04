package org.xiao.algs.graph;

import org.xiao.algs.io.In;
import org.xiao.algs.io.StdOut;
import org.xiao.algs.queue.IndexMinPQ;
import org.xiao.algs.stack.Stack;
/***
 * 
 * 最短路径的Dijkstra算法
 * 
 * DijkstraSP、AcyclicSP、BellmanFordSP
 * 
 * 所需空间为V，时间一般情况和最坏情况都是ElogV
 * 
 * 适用于边的权重非负的加权有向图的单起点最短路径问题
 * 
 * @author XiaoJian
 *
 */
public class DijkstraSP {
    private double[] distTo;          // distTo[v] = s到v的最短距离，不存在则为无穷大
    private DirectedEdge[] edgeTo;    // edgeTo[v] = s到v的最短路径上的最后一条边
    private IndexMinPQ<Double> pq;    // 有向队列

    /**
     * 初始化
     */
    public DijkstraSP(EdgeWeightedDigraph G, int s) {
        for (DirectedEdge e : G.edges()) {
            if (e.weight() < 0)
                throw new IllegalArgumentException("edge " + e + " has negative weight");
        }

        distTo = new double[G.V()];
        edgeTo = new DirectedEdge[G.V()];
        for (int v = 0; v < G.V(); v++)
            distTo[v] = Double.POSITIVE_INFINITY;
        distTo[s] = 0.0;

        // 松弛技术
        pq = new IndexMinPQ<Double>(G.V());
        pq.insert(s, distTo[s]);
        while (!pq.isEmpty()) {
            int v = pq.delMin();
            for (DirectedEdge e : G.adj(v))
                relax(e);
        }

        // 调试
        assert check(G, s);
    }

    // 松弛边
    private void relax(DirectedEdge e) {
        int v = e.from(), w = e.to();
        if (distTo[w] > distTo[v] + e.weight()) {
            distTo[w] = distTo[v] + e.weight();
            edgeTo[w] = e;
            if (pq.contains(w)) pq.decreaseKey(w, distTo[w]);
            else                pq.insert(w, distTo[w]);
        }
    }

    /**
     * 返回从顶点s到v的距离，如果不存在则路径为无穷大
     */
    public double distTo(int v) {
        return distTo[v];
    }

    /**
     * 是否存在从顶点s到v的路径
     */
    public boolean hasPathTo(int v) {
        return distTo[v] < Double.POSITIVE_INFINITY;
    }

    /**
     * 从顶点s到v的路径，如果不存在则为null
     */
    public Iterable<DirectedEdge> pathTo(int v) {
        if (!hasPathTo(v)) return null;
        Stack<DirectedEdge> path = new Stack<DirectedEdge>();
        for (DirectedEdge e = edgeTo[v]; e != null; e = edgeTo[e.from()]) {
            path.push(e);
        }
        return path;
    }


    // 调试
    private boolean check(EdgeWeightedDigraph G, int s) {

        // check that edge weights are nonnegative
        for (DirectedEdge e : G.edges()) {
            if (e.weight() < 0) {
                System.err.println("negative edge weight detected");
                return false;
            }
        }

        // check that distTo[v] and edgeTo[v] are consistent
        if (distTo[s] != 0.0 || edgeTo[s] != null) {
            System.err.println("distTo[s] and edgeTo[s] inconsistent");
            return false;
        }
        for (int v = 0; v < G.V(); v++) {
            if (v == s) continue;
            if (edgeTo[v] == null && distTo[v] != Double.POSITIVE_INFINITY) {
                System.err.println("distTo[] and edgeTo[] inconsistent");
                return false;
            }
        }

        // check that all edges e = v->w satisfy distTo[w] <= distTo[v] + e.weight()
        for (int v = 0; v < G.V(); v++) {
            for (DirectedEdge e : G.adj(v)) {
                int w = e.to();
                if (distTo[v] + e.weight() < distTo[w]) {
                    System.err.println("edge " + e + " not relaxed");
                    return false;
                }
            }
        }

        // check that all edges e = v->w on SPT satisfy distTo[w] == distTo[v] + e.weight()
        for (int w = 0; w < G.V(); w++) {
            if (edgeTo[w] == null) continue;
            DirectedEdge e = edgeTo[w];
            int v = e.from();
            if (w != e.to()) return false;
            if (distTo[v] + e.weight() != distTo[w]) {
                System.err.println("edge " + e + " on shortest path not tight");
                return false;
            }
        }
        return true;
    }


    /**
     *  测试
     * 
     *  java DijkstraSP tinyEWD.txt 0
	 *  0 to 0 (0.00)  
	 *  0 to 1 (1.05)  0->4  0.38   4->5  0.35   5->1  0.32   
	 *  0 to 2 (0.26)  0->2  0.26   
	 *  0 to 3 (0.99)  0->2  0.26   2->7  0.34   7->3  0.39   
	 *  0 to 4 (0.38)  0->4  0.38   
	 *  0 to 5 (0.73)  0->4  0.38   4->5  0.35   
	 *  0 to 6 (1.51)  0->2  0.26   2->7  0.34   7->3  0.39   3->6  0.52   
	 *  0 to 7 (0.60)  0->2  0.26   2->7  0.34   
	 *
	 *  % java DijkstraSP mediumEWD.txt 0
	 *  0 to 0 (0.00)  
	 *  0 to 1 (0.71)  0->44  0.06   44->93  0.07   ...  107->1  0.07   
	 *  0 to 2 (0.65)  0->44  0.06   44->231  0.10  ...  42->2  0.11   
	 *  0 to 3 (0.46)  0->97  0.08   97->248  0.09  ...  45->3  0.12   
	 *  0 to 4 (0.42)  0->44  0.06   44->93  0.07   ...  77->4  0.11   
	 *  ...
     */
    public static void main(String[] args) {
        In in = new In(args[0]);
        EdgeWeightedDigraph G = new EdgeWeightedDigraph(in);
        int s = Integer.parseInt(args[1]);

        // 计算最短路径
        DijkstraSP sp = new DijkstraSP(G, s);


        // 打印最短路径
        for (int t = 0; t < G.V(); t++) {
            if (sp.hasPathTo(t)) {
                StdOut.printf("%d to %d (%.2f)  ", s, t, sp.distTo(t));
                if (sp.hasPathTo(t)) {
                    for (DirectedEdge e : sp.pathTo(t)) {
                        StdOut.print(e + "   ");
                    }
                }
                StdOut.println();
            }
            else {
                StdOut.printf("%d to %d         no path\n", s, t);
            }
        }
    }

}
