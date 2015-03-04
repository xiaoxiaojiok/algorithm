package org.xiao.algs.graph;

import org.xiao.algs.io.StdOut;

/***
 * 
 * 加权有向图边
 * 
 * @author XiaoJian
 *
 */
public class DirectedEdge { 
    private final int v;  //边的起点
    private final int w;  //边的终点 
    private final double weight;  //边的权重

    /**
     * 初始化
     */
    public DirectedEdge(int v, int w, double weight) {
        if (v < 0) throw new IndexOutOfBoundsException("Vertex names must be nonnegative integers");
        if (w < 0) throw new IndexOutOfBoundsException("Vertex names must be nonnegative integers");
        if (Double.isNaN(weight)) throw new IllegalArgumentException("Weight is NaN");
        this.v = v;
        this.w = w;
        this.weight = weight;
    }

    /**
     * 指出这条边的顶点
     */
    public int from() {
        return v;
    }

    /**
     * 这条边指向的顶点
     */
    public int to() {
        return w;
    }

    /**
     * 边的权重
     */
    public double weight() {
        return weight;
    }

    /**
     * 字符串表示
     */
    public String toString() {
        return v + "->" + w + " " + String.format("%5.2f", weight);
    }

    /**
     * 测试
     */
    public static void main(String[] args) {
        DirectedEdge e = new DirectedEdge(12, 23, 3.14);
        StdOut.println(e);
    }
}
