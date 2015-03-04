package org.xiao.algs.graph;

import org.xiao.algs.io.In;
import org.xiao.algs.io.StdOut;
import org.xiao.algs.queue.Queue;
import org.xiao.algs.stack.Stack;
/***
 * 
 * 有向图中基于深度搜索优先的顶点排序
 * 
 * @author XiaoJian
 *
 */
public class DepthFirstOrder {
    private boolean[] marked;          // 顶点是否被标记
    private int[] pre;                 // pre[v]    = 顶点v的前序排序号
    private int[] post;                // post[v]   = 顶点v的后序排序号
    private Queue<Integer> preorder;   // 所有顶点的前序排序
    private Queue<Integer> postorder;  // 所有顶点的后序排序
    private int preCounter;            // 前序排序计数器
    private int postCounter;           // 后序排序计数器

    /**
     * 从有向图初始化
     */
    public DepthFirstOrder(Digraph G) {
        pre    = new int[G.V()];
        post   = new int[G.V()];
        postorder = new Queue<Integer>();
        preorder  = new Queue<Integer>();
        marked    = new boolean[G.V()];
        for (int v = 0; v < G.V(); v++)
            if (!marked[v]) dfs(G, v);
    }

    /**
     * 从加权有向图初始化
     */
    public DepthFirstOrder(EdgeWeightedDigraph G) {
        pre    = new int[G.V()];
        post   = new int[G.V()];
        postorder = new Queue<Integer>();
        preorder  = new Queue<Integer>();
        marked    = new boolean[G.V()];
        for (int v = 0; v < G.V(); v++)
            if (!marked[v]) dfs(G, v);
    }

    // 在有向图G中从v开始计算所有顶点的前序或者后序排列
    private void dfs(Digraph G, int v) {
        marked[v] = true;
        pre[v] = preCounter++;
        preorder.enqueue(v);
        for (int w : G.adj(v)) {
            if (!marked[w]) {
                dfs(G, w);
            }
        }
        postorder.enqueue(v);
        post[v] = postCounter++;
    }

    // 在加权有向图G中从v开始计算所有顶点的前序或者后序排列
    private void dfs(EdgeWeightedDigraph G, int v) {
        marked[v] = true;
        pre[v] = preCounter++;
        preorder.enqueue(v);
        for (DirectedEdge e : G.adj(v)) {
            int w = e.to();
            if (!marked[w]) {
                dfs(G, w);
            }
        }
        postorder.enqueue(v);
        post[v] = postCounter++;
    }

    /**
     * 返回顶点v的前序排序号
     */
    public int pre(int v) {
        return pre[v];
    }

    /**
     * 返回顶点v的后序排序号
     */
    public int post(int v) {
        return post[v];
    }

    /**
     * 返回所有顶点的后序排列
     */
    public Iterable<Integer> post() {
        return postorder;
    }

    /**
     * 返回所有顶点的前序排列
     */
    public Iterable<Integer> pre() {
        return preorder;
    }

    /**
     * 返回所有顶点的逆后序排列
     */
    public Iterable<Integer> reversePost() {
        Stack<Integer> reverse = new Stack<Integer>();
        for (int v : postorder)
            reverse.push(v);
        return reverse;
    }


    // 调试
    @SuppressWarnings("unused")
	private boolean check(Digraph G) {

        // check that post(v) is consistent with post()
        int r = 0;
        for (int v : post()) {
            if (post(v) != r) {
                StdOut.println("post(v) and post() inconsistent");
                return false;
            }
            r++;
        }

        // check that pre(v) is consistent with pre()
        r = 0;
        for (int v : pre()) {
            if (pre(v) != r) {
                StdOut.println("pre(v) and pre() inconsistent");
                return false;
            }
            r++;
        }


        return true;
    }

    /**
     * 测试
     * 
     * java DepthFirstOrder tinyDAG.txt
	 *     v  pre post
	 *  --------------
	 *     0    0    8
	 *     1    3    2
	 *     2    9   10
	 *     3   10    9
	 *     4    2    0
	 *     5    1    1
	 *     6    4    7
	 *     7   11   11
	 *     8   12   12
	 *     9    5    6
	 *    10    8    5
	 *    11    6    4
	 *    12    7    3
	 *  Preorder:  0 5 4 1 6 9 11 12 10 2 3 7 8 
	 *  Postorder: 4 5 1 12 11 10 9 6 0 3 2 7 8 
	 *  Reverse postorder: 8 7 2 3 0 6 9 10 11 12 1 5 4 
     */
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);

        DepthFirstOrder dfs = new DepthFirstOrder(G);
        StdOut.println("   v  pre post");
        StdOut.println("--------------");
        for (int v = 0; v < G.V(); v++) {
            StdOut.printf("%4d %4d %4d\n", v, dfs.pre(v), dfs.post(v));
        }

        StdOut.print("Preorder:  ");
        for (int v : dfs.pre()) {
            StdOut.print(v + " ");
        }
        StdOut.println();

        StdOut.print("Postorder: ");
        for (int v : dfs.post()) {
            StdOut.print(v + " ");
        }
        StdOut.println();

        StdOut.print("Reverse postorder: ");
        for (int v : dfs.reversePost()) {
            StdOut.print(v + " ");
        }
        StdOut.println();


    }

}
