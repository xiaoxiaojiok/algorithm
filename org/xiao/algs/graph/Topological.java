package org.xiao.algs.graph;

import org.xiao.algs.io.StdOut;
/***
 * 
 * 拓扑排序
 * 
 * @author XiaoJian
 *
 */
public class Topological {
    private Iterable<Integer> order;    // 顶点的拓扑排序

    /**
     * 从有向图初始化
     */
    public Topological(Digraph G) {
        DirectedCycle finder = new DirectedCycle(G);
        if (!finder.hasCycle()) {  //检测是否存在环
            DepthFirstOrder dfs = new DepthFirstOrder(G);  //一个有向无环图的拓扑顺序就是所有顶点dfs的逆后序排序
            order = dfs.reversePost();
        }
    }

    /**
     * 从加权有向图初始化
     */
    public Topological(EdgeWeightedDigraph G) {
        EdgeWeightedDirectedCycle finder = new EdgeWeightedDirectedCycle(G);
        if (!finder.hasCycle()) {   //检测是否存在环
            DepthFirstOrder dfs = new DepthFirstOrder(G);  //一个有向无环图的拓扑顺序就是所有顶点dfs的逆后序排序
            order = dfs.reversePost();
        }
    }

    /**
     * 返回拓扑有序的所有顶点
     */
    public Iterable<Integer> order() {
        return order;
    }

    /**
     * 是否拓扑有序
     */
    public boolean hasOrder() {
        return order != null;
    }

    /**
     * 测试
     * 
     * java Topological jobs.txt "/"
	 *  Calculus
	 *  Linear Algebra
	 *  Introduction to CS
	 *  Programming Systems
	 *  Algorithms
	 *  Theoretical CS
	 *  Artificial Intelligence
	 *  Machine Learning
	 *  Neural Networks
	 *  Robotics
	 *  Scientific Computing
	 *  Computational Biology
	 *  Databases
     */
    public static void main(String[] args) {
        String filename  = args[0];
        String delimiter = args[1];
        SymbolDigraph sg = new SymbolDigraph(filename, delimiter);
        Topological topological = new Topological(sg.G());
        for (int v : topological.order()) {
            StdOut.println(sg.name(v));
        }
    }

}
