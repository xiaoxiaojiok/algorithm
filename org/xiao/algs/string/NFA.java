package org.xiao.algs.string;

import org.xiao.algs.bag.Bag;
import org.xiao.algs.graph.Digraph;
import org.xiao.algs.graph.DirectedDFS;
import org.xiao.algs.io.StdOut;
import org.xiao.algs.stack.Stack;

/****
 * 
 * 正则表达式的模式匹配
 * 
 * @author XiaoJian
 *
 */
public class NFA { 

    private Digraph G;         // epsilon转换
    private String regexp;     // 正则表达式
    private int M;             // 状态数量

    // 根据给定的正则表达式构造NFA（非确定的有限状态自动机）
    public NFA(String regexp) {
        this.regexp = regexp;
        M = regexp.length();
        Stack<Integer> ops = new Stack<Integer>(); 
        G = new Digraph(M+1); 
        for (int i = 0; i < M; i++) { 
            int lp = i; 
            if (regexp.charAt(i) == '(' || regexp.charAt(i) == '|') 
                ops.push(i); 
            else if (regexp.charAt(i) == ')') {
                int or = ops.pop(); 

                // 2-way or operator
                if (regexp.charAt(or) == '|') { 
                    lp = ops.pop();
                    G.addEdge(lp, or+1);
                    G.addEdge(or, i);
                }
                else if (regexp.charAt(or) == '(')
                    lp = or;
                else assert false;
            } 

            // 查看下一个字符
            if (i < M-1 && regexp.charAt(i+1) == '*') { 
                G.addEdge(lp, i+1); 
                G.addEdge(i+1, lp); 
            } 
            if (regexp.charAt(i) == '(' || regexp.charAt(i) == '*' || regexp.charAt(i) == ')') 
                G.addEdge(i, i+1);
        } 
    } 

    // NFA是否识别文本的txt
    public boolean recognizes(String txt) {
        DirectedDFS dfs = new DirectedDFS(G, 0);
        Bag<Integer> pc = new Bag<Integer>();
        for (int v = 0; v < G.V(); v++)
            if (dfs.marked(v)) pc.add(v);

        // Compute possible NFA states for txt[i+1]
        for (int i = 0; i < txt.length(); i++) {
            Bag<Integer> match = new Bag<Integer>();
            for (int v : pc) {
                if (v == M) continue;
                if ((regexp.charAt(v) == txt.charAt(i)) || regexp.charAt(v) == '.')
                    match.add(v+1); 
            }
            dfs = new DirectedDFS(G, match); 
            pc = new Bag<Integer>();
            for (int v = 0; v < G.V(); v++)
                if (dfs.marked(v)) pc.add(v);

            // optimization if no states reachable
            if (pc.size() == 0) return false;
        }

        // check for accept state
        for (int v : pc)
            if (v == M) return true;
        return false;
    }


    /***
     * 
     *  测试
     *  
	 *  % java NFA "(A*B|AC)D" AAAABD
	 *  true
	 *
	 *  % java NFA "(A*B|AC)D" AAAAC
	 *  false
	 *
	 *  % java NFA "(a|(bc)*d)*" abcbcd
	 *  true
	 *
	 *  % java NFA "(a|(bc)*d)*" abcbcbcdaaaabcbcdaaaddd
	 *  true
     */
    public static void main(String[] args) {
        String regexp = "(" + args[0] + ")";
        String txt = args[1];
        if (txt.indexOf('|') >= 0) {
            throw new IllegalArgumentException("| character in text is not supported");
        }
        NFA nfa = new NFA(regexp);
        StdOut.println(nfa.recognizes(txt));
    }

} 
