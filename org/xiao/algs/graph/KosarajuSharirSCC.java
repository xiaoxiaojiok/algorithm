package org.xiao.algs.graph;

import org.xiao.algs.io.In;
import org.xiao.algs.io.StdOut;
import org.xiao.algs.queue.Queue;
/***
 * 
 * 计算有向图强连通分量的Kosaraju算法
 * 
 * <p>
 * 如果v和w是互相可达的，则称它们是强连通的，若图中任意两个顶点都是强连通的，则称这幅有向图也是强连通的
 * </p>
 * @author XiaoJian
 *
 */
public class KosarajuSharirSCC {
    private boolean[] marked;     // 已经访问过的顶点
    private int[] id;             // 强连通分量的标记符
    private int count;            // 强连通分量的数量

    /**
     * 预处理构造函数
     */
    public KosarajuSharirSCC(Digraph G) {

        // 在反向图中进行深度优先搜索
        DepthFirstOrder dfs = new DepthFirstOrder(G.reverse());

        // 得到深度优先搜索的逆后序
        marked = new boolean[G.V()];
        id = new int[G.V()];
        for (int v : dfs.reversePost()) {
            if (!marked[v]) {
                dfs(G, v);
                count++;
            }
        }

    }

    // 深度优先搜索
    private void dfs(Digraph G, int v) { 
        marked[v] = true;
        id[v] = count;
        for (int w : G.adj(v)) {
            if (!marked[w]) dfs(G, w);
        }
    }

    /**
     * 图中强连通分量的总数
     */
    public int count() {
        return count;
    }

    /**
     * v和w是强连通的吗
     */
    public boolean stronglyConnected(int v, int w) {
        return id[v] == id[w];
    }

    /**
     * v所在的强连通分量的标记符
     */
    public int id(int v) {
        return id[v];
    }


    /**
     *  测试
     *  
     *  java KosarajuSCC tinyDG.txt
	 *  5 components
	 *  1 
	 *  0 2 3 4 5 
	 *  9 10 11 12 
	 *  6 8 
	 *  7
	 *
	 *  java KosarajuSharirSCC mediumDG.txt 
	 *  10 components
	 *  21 
	 *  2 5 6 8 9 11 12 13 15 16 18 19 22 23 25 26 28 29 30 31 32 33 34 35 37 38 39 40 42 43 44 46 47 48 49 
	 *  14 
	 *  3 4 17 20 24 27 36 
	 *  41 
	 *  7 
	 *  45 
	 *  1 
	 *  0 
	 *  10 
	 *
	 *  java -Xss50m KosarajuSharirSCC mediumDG.txt 
	 *  25 components
     */
    @SuppressWarnings("unchecked")
	public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        KosarajuSharirSCC scc = new KosarajuSharirSCC(G);

        // 连通分量数
        int M = scc.count();
        StdOut.println(M + " components");

        // 计算每个强连通分量的顶点数
        Queue<Integer>[] components = (Queue<Integer>[]) new Queue[M];
        for (int i = 0; i < M; i++) {
            components[i] = new Queue<Integer>();
        }
        for (int v = 0; v < G.V(); v++) {
            components[scc.id(v)].enqueue(v);
        }

        // 打印结果
        for (int i = 0; i < M; i++) {
            for (int v : components[i]) {
                StdOut.print(v + " ");
            }
            StdOut.println();
        }

    }

}
