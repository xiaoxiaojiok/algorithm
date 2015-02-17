package org.xiao.algs.search;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.TreeMap;

import org.xiao.algs.io.StdIn;
import org.xiao.algs.io.StdOut;

/***
 * 
 * 符号表(采用Java TreeMap(java红黑树)实现)
 * 
 * 一种符号表API，多种实现(Java标准库中的TreeMap是红黑树实现，而HashMap是基于拉链法的散列表实现)
 * 
 * 其他实现(无序链表、有序数组、二叉查找树、红黑树、散列表(基于拉链和基于线性探测)等实现参考同目录下其他类)
 * 
 * @author XiaoJian
 *
 */
public class ST<Key extends Comparable<Key>, Value> implements Iterable<Key> {

	private TreeMap<Key, Value> st;

	/**
	 * 创建一个空的符号表
	 */
	public ST() {
		st = new TreeMap<Key, Value>();
	}

	/**
	 * 返回key对应的值
	 */
	public Value get(Key key) {
		if (key == null)
			throw new NullPointerException("called get() with null key");
		return st.get(key);
	}

	/**
	 * 插入键值对，若值为空，则删除key和值，若key为空则抛出异常
	 */
	public void put(Key key, Value val) {
		if (key == null)
			throw new NullPointerException("called put() with null key");
		if (val == null)
			st.remove(key);
		else
			st.put(key, val);
	}

	/**
	 * 删除key和对应的值
	 */
	public void delete(Key key) {
		if (key == null)
			throw new NullPointerException("called delete() with null key");
		st.remove(key);
	}

	/**
	 * key是否在表中
	 */
	public boolean contains(Key key) {
		if (key == null)
			throw new NullPointerException("called contains() with null key");
		return st.containsKey(key);
	}

	/**
	 * 返回符号表中的键值对数量
	 */
	public int size() {
		return st.size();
	}

	/**
	 * 符号表是否为空
	 */
	public boolean isEmpty() {
		return size() == 0;
	}

	/**
	 * 返回所有的key
	 */
	public Iterable<Key> keys() {
		return st.keySet();
	}

	/**
	 * 返回所有key的迭代器
	 */
	public Iterator<Key> iterator() {
		return st.keySet().iterator();
	}

	/**
	 * 返回最小值
	 */
	public Key min() {
		if (isEmpty())
			throw new NoSuchElementException(
					"called min() with empty symbol table");
		return st.firstKey();
	}

	/**
	 * 返回最大值
	 */
	public Key max() {
		if (isEmpty())
			throw new NoSuchElementException(
					"called max() with empty symbol table");
		return st.lastKey();
	}

	/**
	 * 返回大于等于key的最小值
	 */
	public Key ceiling(Key key) {
		if (key == null)
			throw new NullPointerException("called ceiling() with null key");
		Key k = st.ceilingKey(key);
		if (k == null)
			throw new NoSuchElementException("all keys are less than " + key);
		return k;
	}

	/**
	 * 返回小于等于key的最大值
	 */
	public Key floor(Key key) {
		if (key == null)
			throw new NullPointerException("called floor() with null key");
		Key k = st.floorKey(key);
		if (k == null)
			throw new NoSuchElementException("all keys are greater than " + key);
		return k;
	}

	/**
	 * 测试
	 */
	public static void main(String[] args) {
		ST<String, Integer> st = new ST<String, Integer>();
		for (int i = 0; !StdIn.isEmpty(); i++) {
			String key = StdIn.readString();
			st.put(key, i);
		}
		for (String s : st.keys())
			StdOut.println(s + " " + st.get(s));
	}
}
