package org.xiao.algs.search;

import org.xiao.algs.io.StdIn;
import org.xiao.algs.io.StdOut;
import org.xiao.algs.queue.Queue;

/**
 * 
 * 二叉查找树的非递归实现(put和get方法)
 * 
 * @author XiaoJian
 *
 */
public class NonrecursiveBST<Key extends Comparable<Key>, Value> {

    private Node root;          // 根节点

    private class Node {
        private Key key;                // 键
        private Value value;            // 值
        private Node left, right;       // 左右子树

        public Node(Key key, Value value) {
            this.key   = key;
            this.value = value;
        }
    }


    /**
     * 插入节点(非递归版本)
     * 
     * @param key
     * @param value
     */
    public void put(Key key, Value value) {
        Node z = new Node(key, value);
        if (root == null) { root = z; return; }
        Node parent = null, x = root;
        while (x != null) {
            parent = x;
            int res = key.compareTo(x.key);
            if      (res < 0) x = x.left;
            else if (res > 0) x = x.right;
            else { x.value = value; return; }   // overwrite duplicate
        }
        int res = key.compareTo(parent.key);
        if (res < 0) parent.left  = z;
        else         parent.right = z;
    }
   

    /**
     * 查找key，非递归版本
     * 
     * @param key
     * @return
     */
    Value get(Key key) {
        Node x = root;
        while (x != null) {
            int res = key.compareTo(x.key);
            if      (res < 0) x = x.left;
            else if (res > 0) x = x.right;
            else return x.value;
        }
        return null;
    }

    //
    public Iterable<Key> keys() {
        Queue<Key> queue = new Queue<Key>();
        keys(root, queue);
        return queue;
    }
    private void keys(Node x, Queue<Key> queue) { 
        if (x == null) return; 
        keys(x.left, queue); 
        queue.enqueue(x.key); 
        keys(x.right, queue); 
    } 


    /**
     * 测试
     * 
     *  % more tiny.txt
	 *  S E A R C H E X A M P L E
	 *  
	 *  % java NonrecursiveBST < tiny.txt
	 *  A 8
	 *  C 4
	 *  E 12
	 *  H 5
	 *  L 11
	 *  M 9
	 *  P 10
	 *  R 3
	 *  S 0
     */
    public static void main(String[] args) { 
        String[] a = StdIn.readAll().split("\\s+");
        int N = a.length;
        NonrecursiveBST<String, Integer> st = new NonrecursiveBST<String, Integer>();
        for (int i = 0; i < N; i++)
            st.put(a[i], i);
        for (String s : st.keys())
            StdOut.println(s + " " + st.get(s));
    }

}