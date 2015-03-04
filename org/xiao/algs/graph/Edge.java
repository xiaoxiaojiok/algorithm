package org.xiao.algs.graph;

import org.xiao.algs.io.StdOut;
/***
 * 
 * 无向图加权边
 * 
 * @author XiaoJian
 *
 */
public class Edge implements Comparable<Edge> { 

    private final int v;  //顶点之一
    private final int w;  //另一个顶点
    private final double weight; //边的权重

    /**
     * 初始化
     */
    public Edge(int v, int w, double weight) {
        if (v < 0) throw new IndexOutOfBoundsException("Vertex name must be a nonnegative integer");
        if (w < 0) throw new IndexOutOfBoundsException("Vertex name must be a nonnegative integer");
        if (Double.isNaN(weight)) throw new IllegalArgumentException("Weight is NaN");
        this.v = v;
        this.w = w;
        this.weight = weight;
    }

    /**
     * 边的权重
     */
    public double weight() {
        return weight;
    }

    /**
     * 边两端的顶点之一
     */
    public int either() {
        return v;
    }

    /**
     * 另一个顶点
     */
    public int other(int vertex) {
        if      (vertex == v) return w;
        else if (vertex == w) return v;
        else throw new IllegalArgumentException("Illegal endpoint");
    }

    /**
     * 将这条边和that比较
     */
    public int compareTo(Edge that) {
        if      (this.weight() < that.weight()) return -1;
        else if (this.weight() > that.weight()) return +1;
        else                                    return  0;
    }

    /**
     * 字符串表示
     */
    public String toString() {
        return String.format("%d-%d %.5f", v, w, weight);
    }

    /**
     * 测试
     */
    public static void main(String[] args) {
        Edge e = new Edge(12, 23, 3.14);
        StdOut.println(e);
    }
}
