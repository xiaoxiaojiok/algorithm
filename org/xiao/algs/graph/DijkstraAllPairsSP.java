package org.xiao.algs.graph;
/***
 * 
 * 任意顶点对之间的最短路径的Dijkstra算法
 * 
 * 所需空间和时间都和EVlogV成正比
 * 
 * @author XiaoJian
 *
 */
public class DijkstraAllPairsSP {
    private DijkstraSP[] all;

    /**
     * 初始化
     */
    public DijkstraAllPairsSP(EdgeWeightedDigraph G) {
        all  = new DijkstraSP[G.V()];
        for (int v = 0; v < G.V(); v++)
            all[v] = new DijkstraSP(G, v);
    }

    /**
     * 返回s到t的最短路径
     */
    public Iterable<DirectedEdge> path(int s, int t) {
        return all[s].pathTo(t);
    }

    /**
     * s到t是否有一条路径
     */
    public boolean hasPath(int s, int t) {
        return dist(s, t) < Double.POSITIVE_INFINITY;
    }

    /**
     * 返回s到t之间最短路径的距离
     */
    public double dist(int s, int t) {
        return all[s].distTo(t);
    }
}
