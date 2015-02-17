package org.xiao.algs.search;

import org.xiao.algs.io.StdIn;
import org.xiao.algs.io.StdOut;
import org.xiao.algs.queue.Queue;

/***
 * 
 * 基于拉链法的散列表(链表数组)
 * 
 * @author XiaoJian
 *
 */
public class SeparateChainingHashST<Key, Value> {
    private static final int INIT_CAPACITY = 4;

    // 将散列表的大小设置为素数
    // 因为动态调整数组大小时总是2的幂，这样hash方法就只使用了hashCode返回值的低位，可以通过这种方法避免
/*    private static int lgM;
      private static final int[] PRIMES = {
        7, 13, 31, 61, 127, 251, 509, 1021, 2039, 4093, 8191, 16381,
        32749, 65521, 131071, 262139, 524287, 1048573, 2097143, 4194301,
        8388593, 16777213, 33554393, 67108859, 134217689, 268435399,
        536870909, 1073741789, 2147483647
     };
      private int hash(Key key) {
        int t = key.hashCode() & 0x7fffffff;
        if(lgM < 26){
        	t = t % PRIMES[lgM + 5];
        }
        return t % M;
    } */

    private int N;                                // 键值对总数
    private int M;                                // 散列表大小
    private SequentialSearchST<Key, Value>[] st;  // 存放链表对象的数组


    // 初始化时候创建一定数量的链表
    public SeparateChainingHashST() {
        this(INIT_CAPACITY);
    } 

    // 创建M条链表
    @SuppressWarnings("unchecked")
	public SeparateChainingHashST(int M) {
//    	lgM = (int) Math.log(M);
        this.M = M;
        st = (SequentialSearchST<Key, Value>[]) new SequentialSearchST[M];
        for (int i = 0; i < M; i++)
            st[i] = new SequentialSearchST<Key, Value>();
    } 

    // 动态调整数组大小
    private void resize(int chains) {
        SeparateChainingHashST<Key, Value> temp = new SeparateChainingHashST<Key, Value>(chains);
        for (int i = 0; i < M; i++) {
            for (Key key : st[i].keys()) {
                temp.put(key, st[i].get(key));
            }
        }
        this.M  = temp.M;
        this.N  = temp.N;
        this.st = temp.st;
    }

    // 返回0 到  M-1 之间的hase值
   private int hash(Key key) {
        return (key.hashCode() & 0x7fffffff) % M;
    } 
    
    // 返回键值对大小
    public int size() {
        return N;
    } 

    // 符号表是否为空
    public boolean isEmpty() {
        return size() == 0;
    }

    // 表中是否包含key
    public boolean contains(Key key) {
        return get(key) != null;
    } 

    // 返回key对应的值
    public Value get(Key key) {
        int i = hash(key);
        return st[i].get(key);
    } 

    // 插入键值对
    public void put(Key key, Value val) {
        if (val == null) {
            delete(key);
            return;
        }

        // double table size if average length of list >= 10
        if (N >= 10*M) resize(2*M);

        int i = hash(key);
        if (!st[i].contains(key)) N++;
        st[i].put(key, val);
    } 

    // 删除key和对应的值
    public void delete(Key key) {
        int i = hash(key);
        if (st[i].contains(key)) N--;
        st[i].delete(key);

        // halve table size if average length of list <= 2
        if (M > INIT_CAPACITY && N <= 2*M) resize(M/2);
    } 

    // 返回所有的key的迭代器
    public Iterable<Key> keys() {
        Queue<Key> queue = new Queue<Key>();
        for (int i = 0; i < M; i++) {
            for (Key key : st[i].keys())
                queue.enqueue(key);
        }
        return queue;
    } 


   /**
    * 测试
    */
    public static void main(String[] args) { 
        SeparateChainingHashST<String, Integer> st = new SeparateChainingHashST<String, Integer>();
        for (int i = 0; !StdIn.isEmpty(); i++) {
            String key = StdIn.readString();
            st.put(key, i);
        }

        // print keys
        for (String s : st.keys()) 
            StdOut.println(s + " " + st.get(s)); 

    }

}
