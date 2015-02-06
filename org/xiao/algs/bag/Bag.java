package org.xiao.algs.bag;

import java.util.Iterator;
import java.util.NoSuchElementException;

import org.xiao.algs.io.StdIn;
import org.xiao.algs.io.StdOut;

/**
 * 用链表实现的背包
 * 
 * @author XiaoJian
 *
 */
public class Bag<Item> implements Iterable<Item> {
	private int N;
	private Node<Item> first;

	private static class Node<Item> {
		private Item item;
		private Node<Item> next;
	}

	/**
	 * 创建一个空背包
	 */
	public Bag() {
		first = null;
		N = 0;
	}

	/**
	 * 背包是否为空
	 */
	public boolean isEmpty() {
		return first == null;
	}

	/**
	 * 背包中的元素数量
	 */
	public int size() {
		return N;
	}

	/**
	 * 添加一个元素
	 */
	public void add(Item item) {
		Node<Item> oldfirst = first;
		first = new Node<Item>();
		first.item = item;
		first.next = oldfirst;
		N++;
	}

	/**
	 * 迭代器实现
	 */
	@Override
	public Iterator<Item> iterator() {
		return new ListIterator<Item>(first);
	}

	@SuppressWarnings("hiding")
	private class ListIterator<Item> implements Iterator<Item> {
		private Node<Item> current;

		public ListIterator(Node<Item> first) {
			current = first;
		}

		public boolean hasNext() {
			return current != null;
		}

		public void remove() {
			throw new UnsupportedOperationException();
		}

		public Item next() {
			if (!hasNext())
				throw new NoSuchElementException();
			Item item = current.item;
			current = current.next;
			return item;
		}
	}

	/**
	 * 测试
	 */
	public static void main(String[] args) {
		Bag<String> bag = new Bag<String>();
		while (!StdIn.isEmpty()) {
			String item = StdIn.readString();
			bag.add(item);
		}

		StdOut.println("size of bag = " + bag.size());
		for (String s : bag) {
			StdOut.println(s);
		}
	}

}
