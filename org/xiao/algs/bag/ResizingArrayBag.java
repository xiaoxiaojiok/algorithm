package org.xiao.algs.bag;

import java.util.Iterator;
import java.util.NoSuchElementException;

import org.xiao.algs.io.StdOut;

/**
 * 用数组实现的可变可迭代Bag 开销大（每次动态创建并且使用率随着集合大小变化）
 * 
 * @author XiaoJian
 *
 */
public class ResizingArrayBag<Item> implements Iterable<Item> {
	private Item[] a;
	private int N = 0;

	/**
	 * 初始化一个空背包
	 */
	public ResizingArrayBag() {
		a = (Item[]) new Object[2];
	}

	/**
	 * 背包是否为空
	 */
	public boolean isEmpty() {
		return N == 0;
	}

	/**
	 * 背包元素数量
	 */
	public int size() {
		return N;
	}

	// 重新调整背包大小
	private void resize(int capacity) {
		assert capacity >= N;
		Item[] temp = (Item[]) new Object[capacity];
		for (int i = 0; i < N; i++)
			temp[i] = a[i];
		a = temp;
	}

	/**
	 * 添加一个元素
	 */
	public void add(Item item) {
		if (N == a.length)
			resize(2 * a.length);
		a[N++] = item;
	}

	/**
	 * 迭代器实现
	 */
	public Iterator<Item> iterator() {
		return new ArrayIterator();
	}

	private class ArrayIterator implements Iterator<Item> {
		private int i = 0;

		public boolean hasNext() {
			return i < N;
		}

		public void remove() {
			throw new UnsupportedOperationException();
		}

		public Item next() {
			if (!hasNext())
				throw new NoSuchElementException();
			return a[i++];
		}
	}

	/**
	 * 测试
	 */
	public static void main(String[] args) {
		ResizingArrayBag<String> bag = new ResizingArrayBag<String>();
		bag.add("Hello");
		bag.add("World");
		bag.add("how");
		bag.add("are");
		bag.add("you");

		for (String s : bag)
			StdOut.println(s);
	}

}
