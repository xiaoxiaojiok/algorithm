package org.xiao.algs.graph;

import org.xiao.algs.bag.Bag;
import org.xiao.algs.io.In;
import org.xiao.algs.io.StdOut;
import org.xiao.algs.stack.Stack;
/***
 * 
 * 加权无向图
 * 
 * @author XiaoJian
 *
 */
public class EdgeWeightedGraph {
    private final int V;  //图的顶点数
    private int E;        //图的边数
    private Bag<Edge>[] adj;   //和v相邻的所有边
    
	/**
	 * 用V个顶点初始化无向图(不包含边)
	 */
    @SuppressWarnings("unchecked")
	public EdgeWeightedGraph(int V) {
        if (V < 0) throw new IllegalArgumentException("Number of vertices must be nonnegative");
        this.V = V;
        this.E = 0;
        adj = (Bag<Edge>[]) new Bag[V];
        for (int v = 0; v < V; v++) {
            adj[v] = new Bag<Edge>();
        }
    }

	/**
	 * 用V个顶点和E条边初始化无向图
	 */
    public EdgeWeightedGraph(int V, int E) {
        this(V);
        if (E < 0) throw new IllegalArgumentException("Number of edges must be nonnegative");
        for (int i = 0; i < E; i++) {
            int v = (int) (Math.random() * V);
            int w = (int) (Math.random() * V);
            double weight = Math.round(100 * Math.random()) / 100.0;
            Edge e = new Edge(v, w, weight);
            addEdge(e);
        }
    }

	/**
	 * 从标准输入初始化一个无向图
	 */
    public EdgeWeightedGraph(In in) {
        this(in.readInt());
        int E = in.readInt();
        if (E < 0) throw new IllegalArgumentException("Number of edges must be nonnegative");
        for (int i = 0; i < E; i++) {
            int v = in.readInt();
            int w = in.readInt();
            double weight = in.readDouble();
            Edge e = new Edge(v, w, weight);
            addEdge(e);
        }
    }

	/**
	 * 从已有的图G初始化一个无向图
	 */
    public EdgeWeightedGraph(EdgeWeightedGraph G) {
        this(G.V());
        this.E = G.E();
        for (int v = 0; v < G.V(); v++) {
            // reverse so that adjacency list is in same order as original
            Stack<Edge> reverse = new Stack<Edge>();
            for (Edge e : G.adj[v]) {
                reverse.push(e);
            }
            for (Edge e : reverse) {
                adj[v].add(e);
            }
        }
    }


	/**
	 * 返回顶点数目
	 */
	public int V() {
		return V;
	}

	/**
	 * 返回边的数目
	 */
	public int E() {
		return E;
	}

	/**
	 * 为无向图添加一条边 v-w
	 */
    public void addEdge(Edge e) {
        int v = e.either();
        int w = e.other(v);
        if (v < 0 || v >= V) throw new IndexOutOfBoundsException("vertex " + v + " is not between 0 and " + (V-1));
        if (w < 0 || w >= V) throw new IndexOutOfBoundsException("vertex " + w + " is not between 0 and " + (V-1));
        adj[v].add(e);
        adj[w].add(e);
        E++;
    }

	/**
	 * 返回和v相邻的所有边
	 */
    public Iterable<Edge> adj(int v) {
        if (v < 0 || v >= V) throw new IndexOutOfBoundsException("vertex " + v + " is not between 0 and " + (V-1));
        return adj[v];
    }

    /**
     * 返回图的所有边
     */
    public Iterable<Edge> edges() {
        Bag<Edge> list = new Bag<Edge>();
        for (int v = 0; v < V; v++) {
            int selfLoops = 0;
            for (Edge e : adj(v)) {
                if (e.other(v) > v) {
                    list.add(e);
                }
                // only add one copy of each self loop (self loops will be consecutive)
                else if (e.other(v) == v) {
                    if (selfLoops % 2 == 0) list.add(e);
                    selfLoops++;
                }
            }
        }
        return list;
    }

    /**
     * 对象的字符串表示
     */
    public String toString() {
        String NEWLINE = System.getProperty("line.separator");
        StringBuilder s = new StringBuilder();
        s.append(V + " " + E + NEWLINE);
        for (int v = 0; v < V; v++) {
            s.append(v + ": ");
            for (Edge e : adj[v]) {
                s.append(e + "  ");
            }
            s.append(NEWLINE);
        }
        return s.toString();
    }

    /**
     *  测试
     * 
     *  java EdgeWeightedGraph tinyEWG.txt 
	 *  8 16
	 *  0: 6-0 0.58000  0-2 0.26000  0-4 0.38000  0-7 0.16000  
	 *  1: 1-3 0.29000  1-2 0.36000  1-7 0.19000  1-5 0.32000  
	 *  2: 6-2 0.40000  2-7 0.34000  1-2 0.36000  0-2 0.26000  2-3 0.17000  
	 *  3: 3-6 0.52000  1-3 0.29000  2-3 0.17000  
	 *  4: 6-4 0.93000  0-4 0.38000  4-7 0.37000  4-5 0.35000  
	 *  5: 1-5 0.32000  5-7 0.28000  4-5 0.35000  
	 *  6: 6-4 0.93000  6-0 0.58000  3-6 0.52000  6-2 0.40000
	 *  7: 2-7 0.34000  1-7 0.19000  0-7 0.16000  5-7 0.28000  4-7 0.37000
     */
    public static void main(String[] args) {
        In in = new In(args[0]);
        EdgeWeightedGraph G = new EdgeWeightedGraph(in);
        StdOut.println(G);
    }

}
