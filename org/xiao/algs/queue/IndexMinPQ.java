package org.xiao.algs.queue;

import java.util.Iterator;
import java.util.NoSuchElementException;

import org.xiao.algs.io.StdOut;

/***
 * 
 * 索引优先队列
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
 */
public class IndexMinPQ<Key extends Comparable<Key>> implements
		Iterable<Integer> {
	private int NMAX; // maximum number of elements on PQ
	private int N; // number of elements on PQ
	private int[] pq; // binary heap using 1-based indexing
	private int[] qp; // inverse of pq - qp[pq[i]] = pq[qp[i]] = i
	private Key[] keys; // keys[i] = priority of i

	/**
	 * 初始化一个最大容量为NMAX的优先队列
	 */
	@SuppressWarnings("unchecked")
	public IndexMinPQ(int NMAX) {
		if (NMAX < 0)
			throw new IllegalArgumentException();
		this.NMAX = NMAX;
		keys = (Key[]) new Comparable[NMAX + 1];
		pq = new int[NMAX + 1];
		qp = new int[NMAX + 1];
		for (int i = 0; i <= NMAX; i++)
			qp[i] = -1;
	}

	/**
	 * 返回优先队列是否为空
	 */
	public boolean isEmpty() {
		return N == 0;
	}

	/**
	 * 是否存在索引为i的元素
	 */
	public boolean contains(int i) {
		if (i < 0 || i >= NMAX)
			throw new IndexOutOfBoundsException();
		return qp[i] != -1;
	}

	/**
	 * 返回优先队列元素的个数
	 */
	public int size() {
		return N;
	}

	/**
	 * 插入一个元素，把它和索引i关联
	 */
	public void insert(int i, Key key) {
		if (i < 0 || i >= NMAX)
			throw new IndexOutOfBoundsException();
		if (contains(i))
			throw new IllegalArgumentException(
					"index is already in the priority queue");
		N++;
		qp[i] = N;
		pq[N] = i;
		keys[i] = key;
		swim(N);
	}

	/**
	 * 返回最小元素的索引
	 */
	public int minIndex() {
		if (N == 0)
			throw new NoSuchElementException("Priority queue underflow");
		return pq[1];
	}

	/**
	 * 返回最小元素
	 */
	public Key minKey() {
		if (N == 0)
			throw new NoSuchElementException("Priority queue underflow");
		return keys[pq[1]];
	}

	/**
	 * 删除最小元素并返回它的索引
	 */
	public int delMin() {
		if (N == 0)
			throw new NoSuchElementException("Priority queue underflow");
		int min = pq[1];
		exch(1, N--);
		sink(1);
		qp[min] = -1; // delete
		keys[pq[N + 1]] = null; // to help with garbage collection
		pq[N + 1] = -1; // not needed
		return min;
	}

	/**
	 * 返回索引i关联的key
	 */
	public Key keyOf(int i) {
		if (i < 0 || i >= NMAX)
			throw new IndexOutOfBoundsException();
		if (!contains(i))
			throw new NoSuchElementException(
					"index is not in the priority queue");
		else
			return keys[i];
	}

	/**
	 * 将索引为i的元素设为key
	 */
	@Deprecated
	public void change(int i, Key key) {
		changeKey(i, key);
	}

	/**
	 * 将索引为i的元素设为key
	 */
	public void changeKey(int i, Key key) {
		if (i < 0 || i >= NMAX)
			throw new IndexOutOfBoundsException();
		if (!contains(i))
			throw new NoSuchElementException(
					"index is not in the priority queue");
		keys[i] = key;
		swim(qp[i]);
		sink(qp[i]);
	}

	/**
	 * Decrease the key associated with index i to the specified value.
	 */
	public void decreaseKey(int i, Key key) {
		if (i < 0 || i >= NMAX)
			throw new IndexOutOfBoundsException();
		if (!contains(i))
			throw new NoSuchElementException(
					"index is not in the priority queue");
		if (keys[i].compareTo(key) <= 0)
			throw new IllegalArgumentException(
					"Calling decreaseKey() with given argument would not strictly decrease the key");
		keys[i] = key;
		swim(qp[i]);
	}

	/**
	 * Increase the key associated with index i to the specified value.
	 */
	public void increaseKey(int i, Key key) {
		if (i < 0 || i >= NMAX)
			throw new IndexOutOfBoundsException();
		if (!contains(i))
			throw new NoSuchElementException(
					"index is not in the priority queue");
		if (keys[i].compareTo(key) >= 0)
			throw new IllegalArgumentException(
					"Calling increaseKey() with given argument would not strictly increase the key");
		keys[i] = key;
		sink(qp[i]);
	}

	/**
	 * 删除索引i及其相关联的元素
	 */
	public void delete(int i) {
		if (i < 0 || i >= NMAX)
			throw new IndexOutOfBoundsException();
		if (!contains(i))
			throw new NoSuchElementException(
					"index is not in the priority queue");
		int index = qp[i];
		exch(index, N--);
		swim(index);
		sink(index);
		keys[i] = null;
		qp[i] = -1;
	}

	private boolean greater(int i, int j) {
		return keys[pq[i]].compareTo(keys[pq[j]]) > 0;
	}

	private void exch(int i, int j) {
		int swap = pq[i];
		pq[i] = pq[j];
		pq[j] = swap;
		qp[pq[i]] = i;
		qp[pq[j]] = j;
	}

	// 上浮
	private void swim(int k) {
		while (k > 1 && greater(k / 2, k)) {
			exch(k, k / 2);
			k = k / 2;
		}
	}

	// 下沉
	private void sink(int k) {
		while (2 * k <= N) {
			int j = 2 * k;
			if (j < N && greater(j, j + 1))
				j++;
			if (!greater(k, j))
				break;
			exch(k, j);
			k = j;
		}
	}

	/**
	 * 迭代器实现
	 */
	public Iterator<Integer> iterator() {
		return new HeapIterator();
	}

	private class HeapIterator implements Iterator<Integer> {
		// create a new pq
		private IndexMinPQ<Key> copy;

		// add all elements to copy of heap
		// takes linear time since already in heap order so no keys move
		public HeapIterator() {
			copy = new IndexMinPQ<Key>(pq.length - 1);
			for (int i = 1; i <= N; i++)
				copy.insert(pq[i], keys[pq[i]]);
		}

		public boolean hasNext() {
			return !copy.isEmpty();
		}

		public void remove() {
			throw new UnsupportedOperationException();
		}

		public Integer next() {
			if (!hasNext())
				throw new NoSuchElementException();
			return copy.delMin();
		}
	}

	/**
	 * 测试 Multiway
	 */
	public static void main(String[] args) {
		String[] strings = { "it", "was", "the", "best", "of", "times", "it",
				"was", "the", "worst" };

		IndexMinPQ<String> pq = new IndexMinPQ<String>(strings.length);
		for (int i = 0; i < strings.length; i++) {
			pq.insert(i, strings[i]);
		}

		// 删除并打印每一个key
		while (!pq.isEmpty()) {
			int i = pq.delMin();
			StdOut.println(i + " " + strings[i]);
		}
		StdOut.println();

		// 重新插入相同的字符串
		for (int i = 0; i < strings.length; i++) {
			pq.insert(i, strings[i]);
		}

		// 输出结果
		for (int i : pq) {
			StdOut.println(i + " " + strings[i]);
		}
		while (!pq.isEmpty()) {
			pq.delMin();
		}

	}
}
