package org.xiao.algs.search;

import java.util.NoSuchElementException;

import org.xiao.algs.io.StdIn;
import org.xiao.algs.io.StdOut;
import org.xiao.algs.queue.Queue;

/***
 * 
 * 有序数组实现的符号表(二分查找)
 * 
 * @author XiaoJian
 *
 */
public class BinarySearchST<Key extends Comparable<Key>, Value> {
	private static final int INIT_CAPACITY = 2;
	private Key[] keys;
	private Value[] vals;
	private int N = 0;

	public BinarySearchST() {
		this(INIT_CAPACITY);
	}

	// 初始化容量capacity
	@SuppressWarnings("unchecked")
	public BinarySearchST(int capacity) {
		keys = (Key[]) new Comparable[capacity];
		vals = (Value[]) new Object[capacity];
	}

	// 重新调整数组大小
	@SuppressWarnings("unchecked")
	private void resize(int capacity) {
		assert capacity >= N;
		Key[] tempk = (Key[]) new Comparable[capacity];
		Value[] tempv = (Value[]) new Object[capacity];
		for (int i = 0; i < N; i++) {
			tempk[i] = keys[i];
			tempv[i] = vals[i];
		}
		vals = tempv;
		keys = tempk;
	}

	// key在表中是否有对应的值
	public boolean contains(Key key) {
		return get(key) != null;
	}

	// 返回符号表中的键值对数量
	public int size() { 
		return N;
	}

	// 符号表是否为空
	public boolean isEmpty() {
		return size() == 0;
	}

	// 获取键key对应的值
	public Value get(Key key) {
		if (isEmpty())
			return null;
		int i = rank(key);
		if (i < N && keys[i].compareTo(key) == 0)
			return vals[i];
		return null;
	}

	// 小于key的键的数量
	public int rank(Key key) {
		int lo = 0, hi = N - 1;
		while (lo <= hi) {
			int m = lo + (hi - lo) / 2;
			int cmp = key.compareTo(keys[m]);
			if (cmp < 0)
				hi = m - 1;
			else if (cmp > 0)
				lo = m + 1;
			else
				return m;
		}
		return lo;
	}

	// 将键值对存入表中，若值为空，则将key从表中删除
	public void put(Key key, Value val) {
		if (val == null) {
			delete(key);
			return;
		}

		int i = rank(key);

		// key已经在表中
		if (i < N && keys[i].compareTo(key) == 0) {
			vals[i] = val;
			return;
		}

		// 插入新的键值对
		if (N == keys.length)
			resize(2 * keys.length);

		for (int j = N; j > i; j--) {
			keys[j] = keys[j - 1];
			vals[j] = vals[j - 1];
		}
		keys[i] = key;
		vals[i] = val;
		N++;

		assert check();
	}

	// 从表中删除键key以及对应的值
	public void delete(Key key) {
		if (isEmpty())
			return;

		// 计算排名
		int i = rank(key);

		// 若不在表中
		if (i == N || keys[i].compareTo(key) != 0) {
			return;
		}

		for (int j = i; j < N - 1; j++) {
			keys[j] = keys[j + 1];
			vals[j] = vals[j + 1];
		}

		N--;
		keys[N] = null; // to avoid loitering
		vals[N] = null;

		// resize if 1/4 full
		if (N > 0 && N == keys.length / 4)
			resize(keys.length / 2);

		assert check();
	}

	// 删除最小的键
	public void deleteMin() {
		if (isEmpty())
			throw new NoSuchElementException("Symbol table underflow error");
		delete(min());
	}

	// 删除最大的键
	public void deleteMax() {
		if (isEmpty())
			throw new NoSuchElementException("Symbol table underflow error");
		delete(max());
	}

	// 返回最小的键
	public Key min() {
		if (isEmpty())
			return null;
		return keys[0];
	}

	// 返回最大的键
	public Key max() {
		if (isEmpty())
			return null;
		return keys[N - 1];
	}

	// 返回排名为k的键
	public Key select(int k) {
		if (k < 0 || k >= N)
			return null;
		return keys[k];
	}

	// 小于等于key的最大值
	public Key floor(Key key) {
		int i = rank(key);
		if (i < N && key.compareTo(keys[i]) == 0)
			return keys[i];
		if (i == 0)
			return null;
		else
			return keys[i - 1];
	}

	// 大于等于key的最小值
	public Key ceiling(Key key) {
		int i = rank(key);
		if (i == N)
			return null;
		else
			return keys[i];
	}

	// 表中键值对的数量
	public int size(Key lo, Key hi) {
		if (lo.compareTo(hi) > 0)
			return 0;
		if (contains(hi))
			return rank(hi) - rank(lo) + 1;
		else
			return rank(hi) - rank(lo);
	}

	// 返回表中所有键
	public Iterable<Key> keys() {
		return keys(min(), max());
	}

	public Iterable<Key> keys(Key lo, Key hi) {
		Queue<Key> queue = new Queue<Key>();
		if (lo == null && hi == null)
			return queue;
		if (lo == null)
			throw new NullPointerException("lo is null in keys()");
		if (hi == null)
			throw new NullPointerException("hi is null in keys()");
		if (lo.compareTo(hi) > 0)
			return queue;
		for (int i = rank(lo); i < rank(hi); i++)
			queue.enqueue(keys[i]);
		if (contains(hi))
			queue.enqueue(keys[rank(hi)]);
		return queue;
	}

	// 调试是否已经排序
	private boolean check() {
		return isSorted() && rankCheck();
	}

	// 是否已经排好序
	private boolean isSorted() {
		for (int i = 1; i < size(); i++)
			if (keys[i].compareTo(keys[i - 1]) < 0)
				return false;
		return true;
	}

	// check that rank(select(i)) = i
	private boolean rankCheck() {
		for (int i = 0; i < size(); i++)
			if (i != rank(select(i)))
				return false;
		for (int i = 0; i < size(); i++)
			if (keys[i].compareTo(select(rank(keys[i]))) != 0)
				return false;
		return true;
	}

	/***
	 * 
	 * 测试
	 * 
	 */
	public static void main(String[] args) {
		BinarySearchST<String, Integer> st = new BinarySearchST<String, Integer>();
		for (int i = 0; !StdIn.isEmpty(); i++) {
			String key = StdIn.readString();
			st.put(key, i);
		}
		for (String s : st.keys())
			StdOut.println(s + " " + st.get(s));
	}
}
