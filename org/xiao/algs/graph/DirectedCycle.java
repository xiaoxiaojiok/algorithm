package org.xiao.algs.graph;

import org.xiao.algs.io.In;
import org.xiao.algs.io.StdOut;
import org.xiao.algs.stack.Stack;
/***
 * 
 *  检测有向图中的环
 * 
 * @author XiaoJian
 *
 */
public class DirectedCycle {
    private boolean[] marked;        // 顶点是否被标记
    private int[] edgeTo;            // 从起点到一个顶点的已知路径上的最后一个顶点
    private boolean[] onStack;       // 有向环中的所有顶点
    private Stack<Integer> cycle;    // 递归调用的栈上的所有顶点

    /**
     * 初始化
     */
    public DirectedCycle(Digraph G) {
        marked  = new boolean[G.V()];
        onStack = new boolean[G.V()];
        edgeTo  = new int[G.V()];
        for (int v = 0; v < G.V(); v++)
            if (!marked[v]) dfs(G, v);
    }

    // 深度优先搜索
    private void dfs(Digraph G, int v) {
        onStack[v] = true;
        marked[v] = true;
        for (int w : G.adj(v)) {

            // short circuit if directed cycle found
            if (cycle != null) return;

            //found new vertex, so recur
            else if (!marked[w]) {
                edgeTo[w] = v;
                dfs(G, w);
            }

            // trace back directed cycle
            else if (onStack[w]) {
                cycle = new Stack<Integer>();
                for (int x = v; x != w; x = edgeTo[x]) {
                    cycle.push(x);
                }
                cycle.push(w);
                cycle.push(v);
            }
        }

        onStack[v] = false;
    }

    /**
     * G是否含有有向环
     */
    public boolean hasCycle() {
        return cycle != null;
    }

    /**
     * 返回有向环中的所有顶点
     */
    public Iterable<Integer> cycle() {
        return cycle;
    }


    // 调试
    @SuppressWarnings("unused")
	private boolean check(Digraph G) {

        if (hasCycle()) {
            // verify cycle
            int first = -1, last = -1;
            for (int v : cycle()) {
                if (first == -1) first = v;
                last = v;
            }
            if (first != last) {
                System.err.printf("cycle begins with %d and ends with %d\n", first, last);
                return false;
            }
        }


        return true;
    }

    /**
     *  测试
     *  
     *  java DirectedCycle tinyDG.txt 
	 *  Cycle: 3 5 4 3 
	 *
	 *  java DirectedCycle tinyDAG.txt 
	 *  No cycle
     */
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);

        DirectedCycle finder = new DirectedCycle(G);
        if (finder.hasCycle()) {
            StdOut.print("Cycle: ");
            for (int v : finder.cycle()) {
                StdOut.print(v + " ");
            }
            StdOut.println();
        }

        else {
            StdOut.println("No cycle");
        }
    }

}
