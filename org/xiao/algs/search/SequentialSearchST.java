package org.xiao.algs.search;

import org.xiao.algs.io.StdIn;
import org.xiao.algs.io.StdOut;
import org.xiao.algs.queue.Queue;

/***
 * 
 * 无序链表实现的符号表(顺序查找)
 * 
 * @author XiaoJian
 *
 */
public class SequentialSearchST<Key, Value> {
    private int N;           // 键值对的数量
    private Node first;      // 首结点

    // 结点类
    private class Node {
        private Key key;
        private Value val;
        private Node next;

        public Node(Key key, Value val, Node next)  {
            this.key  = key;
            this.val  = val;
            this.next = next;
        }
    }

    public SequentialSearchST() {
    }

    /**
     * 返回表中键值对的数量
     */
    public int size() {
        return N;
    }

    /**
     * 符号表是否为空
     */
    public boolean isEmpty() {
        return size() == 0;
    }

    /**
     * key对应的值是否存在
     */
    public boolean contains(Key key) {
        return get(key) != null;
    }

    /**
     * 获取键key对应的值
     */
    public Value get(Key key) {
        for (Node x = first; x != null; x = x.next) {
            if (key.equals(x.key)) return x.val;
        }
        return null;
    }

    /**
     * 将键值对存入表中，若值为空，则将key从表中删除
     */
    public void put(Key key, Value val) {
        if (val == null) { delete(key); return; }
        for (Node x = first; x != null; x = x.next)
            if (key.equals(x.key)) { x.val = val; return; }
        first = new Node(key, val, first);
        N++;
    }

    /**
     * 从表中删除键key以及对应的值
     */
    public void delete(Key key) {
        first = delete(first, key);
    }

    // 删除key
    private Node delete(Node x, Key key) {
        if (x == null) return null;
        if (key.equals(x.key)) { N--; return x.next; }
        x.next = delete(x.next, key);
        return x;
    }


    /**
     * 返回所有key
     */
    public Iterable<Key> keys()  {
        Queue<Key> queue = new Queue<Key>();
        for (Node x = first; x != null; x = x.next)
            queue.enqueue(x.key);
        return queue;
    }


    /**
     * 测试
     */
    public static void main(String[] args) {
        SequentialSearchST<String, Integer> st = new SequentialSearchST<String, Integer>();
        for (int i = 0; !StdIn.isEmpty(); i++) {
            String key = StdIn.readString();
            st.put(key, i);
        }
        for (String s : st.keys())
            StdOut.println(s + " " + st.get(s));
    }
}
