package org.xiao.algs.queue;

import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

import org.xiao.algs.io.StdIn;
import org.xiao.algs.io.StdOut;

/***
 * 
 * 基于堆的优先队列 
 * 
 * 插入和删除元素的复杂度都为logN，而用数组或者链表实现的复杂度为线性复杂度
 *  
 * 当数据量太大的时候，无法排序(甚至无法装入内存)，例如从10亿个元素中找出最大的10个，那么不可能对10亿个排序
 * 可以用优先队列，只需要一个可以储存10个元素的队列就可以了
 * 
 * 优先队列可以查看输入流最大的M个元素(TopM)，也可以将M个输入流归并为一个有序的输出流(Multiway)
 * 
 * @author XiaoJian
 *
 * @param <Key>
 */

public class MaxPQ<Key> implements Iterable<Key> {
    private Key[] pq;                    // 基于堆得完全二叉树，从1开始存储
    private int N;                        
    private Comparator<Key> comparator;  

    /**
     * 创建一个初始容量为 initCapacity 的优先队列
     */
    @SuppressWarnings("unchecked")
	public MaxPQ(int initCapacity) {
        pq = (Key[]) new Object[initCapacity + 1];
        N = 0;
    }

    /**
     * 创建一个空的优先队列
     */
    public MaxPQ() {
        this(1);
    }

    /**
     * 创建一个初始容量为 initCapacity 的优先队列
     */
    @SuppressWarnings("unchecked")
	public MaxPQ(int initCapacity, Comparator<Key> comparator) {
        this.comparator = comparator;
        pq = (Key[]) new Object[initCapacity + 1];
        N = 0;
    }

    /**
     * 创建一个空的优先队列
     */
    public MaxPQ(Comparator<Key> comparator) {
        this(1, comparator);
    }

    /**
     * 用keys中的元素创建一个优先队列
     */
    @SuppressWarnings("unchecked")
	public MaxPQ(Key[] keys) {
        N = keys.length;
        pq = (Key[]) new Object[keys.length + 1]; 
        for (int i = 0; i < N; i++)
            pq[i+1] = keys[i];
        for (int k = N/2; k >= 1; k--)
            sink(k);
        assert isMaxHeap();
    }

    /**
     * 返回优先队列是否为空
     */
    public boolean isEmpty() {
        return N == 0;
    }

    /**
     * 返回优先队列中的元素个数
     */
    public int size() {
        return N;
    }

    /**
     * 返回最大元素
     */
    public Key max() {
        if (isEmpty()) throw new NoSuchElementException("Priority queue underflow");
        return pq[1];
    }

    // 重新调整数组大小
    @SuppressWarnings("unchecked")
	private void resize(int capacity) {
        assert capacity > N;
        Key[] temp = (Key[]) new Object[capacity];
        for (int i = 1; i <= N; i++) temp[i] = pq[i];
        pq = temp;
    }


    /**
     * 向优先队列插入一个元素
     */
    public void insert(Key x) {

        // double size of array if necessary
        if (N >= pq.length - 1) resize(2 * pq.length);

        // add x, and percolate it up to maintain heap invariant
        pq[++N] = x;
        swim(N);
        assert isMaxHeap();
    }


    /**
     * 删除并返回最大元素
     */
    public Key delMax() {
        if (isEmpty()) throw new NoSuchElementException("Priority queue underflow");
        Key max = pq[1];
        exch(1, N--);
        sink(1);
        pq[N+1] = null;     // to avoid loiterig and help with garbage collection
        if ((N > 0) && (N == (pq.length - 1) / 4)) resize(pq.length / 2);
        assert isMaxHeap();
        return max;
    }


    //上浮
    private void swim(int k) {
        while (k > 1 && less(k/2, k)) {
            exch(k, k/2);
            k = k/2;
        }
    }

    //下沉
    private void sink(int k) {
        while (2*k <= N) {
            int j = 2*k;
            if (j < N && less(j, j+1)) j++;
            if (!less(k, j)) break;
            exch(k, j);
            k = j;
        }
    }

    @SuppressWarnings("unchecked")
	private boolean less(int i, int j) {
        if (comparator == null) {
            return ((Comparable<Key>) pq[i]).compareTo(pq[j]) < 0;
        }
        else {
            return comparator.compare(pq[i], pq[j]) < 0;
        }
    }

  //交换元素
    private void exch(int i, int j) {
        Key swap = pq[i];
        pq[i] = pq[j];
        pq[j] = swap;
    }

    // 调试是否是最小堆
    private boolean isMaxHeap() {
        return isMaxHeap(1);
    }

    // 调试是否是最小堆（根元素为k）
    private boolean isMaxHeap(int k) {
        if (k > N) return true;
        int left = 2*k, right = 2*k + 1;
        if (left  <= N && less(k, left))  return false;
        if (right <= N && less(k, right)) return false;
        return isMaxHeap(left) && isMaxHeap(right);
    }


    /**
     * 迭代器实现
     */
    public Iterator<Key> iterator() { return new HeapIterator(); }

    private class HeapIterator implements Iterator<Key> {

        // create a new pq
        private MaxPQ<Key> copy;

        // add all items to copy of heap
        // takes linear time since already in heap order so no keys move
        public HeapIterator() {
            if (comparator == null) copy = new MaxPQ<Key>(size());
            else                    copy = new MaxPQ<Key>(size(), comparator);
            for (int i = 1; i <= N; i++)
                copy.insert(pq[i]);
        }

        public boolean hasNext()  { return !copy.isEmpty();                     }
        public void remove()      { throw new UnsupportedOperationException();  }

        public Key next() {
            if (!hasNext()) throw new NoSuchElementException();
            return copy.delMax();
        }
    }

    /**
     * 测试 TopM
     */
    public static void main(String[] args) {
        MaxPQ<String> pq = new MaxPQ<String>();
        while (!StdIn.isEmpty()) {
            String item = StdIn.readString();
            if (!item.equals("-")) pq.insert(item);
            else if (!pq.isEmpty()) StdOut.print(pq.delMax() + " ");
        }
        StdOut.println("(" + pq.size() + " left on pq)");
    }

}
