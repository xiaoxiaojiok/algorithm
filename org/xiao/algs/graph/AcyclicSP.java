package org.xiao.algs.graph;

import org.xiao.algs.io.In;
import org.xiao.algs.io.StdOut;
import org.xiao.algs.stack.Stack;
/***
 * 
 * 最短路径的拓扑排序算法
 * 
 * 是无环图中的最优算法
 * 
 * DijkstraSP、AcyclicSP、BellmanFordSP
 *  
 * 所需空间为V，时间一般情况和最坏情况都是E+V
 * 
 * 只适用于无环加权有向图，能够处理负权重的边
 * 
 * @author XiaoJian
 *
 */
public class AcyclicSP {
    private double[] distTo;         // distTo[v] = s到v的最短距离，不存在则为无穷大
    private DirectedEdge[] edgeTo;   // edgeTo[v] = s到v的最短路径上的最后一条边


    /**
     * 初始化
     */
    public AcyclicSP(EdgeWeightedDigraph G, int s) {
        distTo = new double[G.V()];
        edgeTo = new DirectedEdge[G.V()];
        for (int v = 0; v < G.V(); v++)
            distTo[v] = Double.POSITIVE_INFINITY;
        distTo[s] = 0.0;

        // 按照拓扑排序顺序处理无环有向图的顶点
        Topological topological = new Topological(G);
        if (!topological.hasOrder())
            throw new IllegalArgumentException("Digraph is not acyclic.");
        for (int v : topological.order()) {
            for (DirectedEdge e : G.adj(v))
                relax(e);
        }
    }

    // 松弛边e
    private void relax(DirectedEdge e) {
        int v = e.from(), w = e.to();
        if (distTo[w] > distTo[v] + e.weight()) {
            distTo[w] = distTo[v] + e.weight();
            edgeTo[w] = e;
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
     * 返回从顶点s到v的最短路径，如果不存在则为null
     */
    public Iterable<DirectedEdge> pathTo(int v) {
        if (!hasPathTo(v)) return null;
        Stack<DirectedEdge> path = new Stack<DirectedEdge>();
        for (DirectedEdge e = edgeTo[v]; e != null; e = edgeTo[e.from()]) {
            path.push(e);
        }
        return path;
    }


    /**
     *  测试
     * 
     *  java AcyclicSP tinyEWDAG.txt 5
	 *  5 to 0 (0.73)  5->4  0.35   4->0  0.38   
	 *  5 to 1 (0.32)  5->1  0.32   
	 *  5 to 2 (0.62)  5->7  0.28   7->2  0.34   
	 *  5 to 3 (0.61)  5->1  0.32   1->3  0.29   
	 *  5 to 4 (0.35)  5->4  0.35   
	 *  5 to 5 (0.00)  
	 *  5 to 6 (1.13)  5->1  0.32   1->3  0.29   3->6  0.52   
	 *  5 to 7 (0.28)  5->7  0.28
     */
    public static void main(String[] args) {
        In in = new In(args[0]);
        int s = Integer.parseInt(args[1]);
        EdgeWeightedDigraph G = new EdgeWeightedDigraph(in);

        // 找出加权有向图G中s到每一个顶点的最短路径
        AcyclicSP sp = new AcyclicSP(G, s);
        for (int v = 0; v < G.V(); v++) {
            if (sp.hasPathTo(v)) {
                StdOut.printf("%d to %d (%.2f)  ", s, v, sp.distTo(v));
                for (DirectedEdge e : sp.pathTo(v)) {
                    StdOut.print(e + "   ");
                }
                StdOut.println();
            }
            else {
                StdOut.printf("%d to %d         no path\n", s, v);
            }
        }
    }
}
