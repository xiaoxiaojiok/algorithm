package org.xiao.algs.queue;

import java.util.Iterator;
import java.util.NoSuchElementException;

import org.xiao.algs.io.StdIn;
import org.xiao.algs.io.StdOut;

/***
 * 
 * 用链表实现的队列
 * 
 * @author XiaoJian
 *
 * @param <Item>
 */
public class Queue<Item> implements Iterable<Item> {
	private int N;
	private Node<Item> first;
	private Node<Item> last;

	private static class Node<Item> {
		private Item item;
		private Node<Item> next;
	}

	/**
	 * 创建一个空的队列
	 */
	public Queue() {
		first = null;
		last = null;
		N = 0;
	}

	/**
	 * 队列是否为空
	 */
	public boolean isEmpty() {
		return first == null;
	}

	/**
	 * 队列的元素数量
	 */
	public int size() {
		return N;
	}

	/**
	 * 返回队列首元素
	 */
	public Item peek() {
		if (isEmpty())
			throw new NoSuchElementException("Queue underflow");
		return first.item;
	}

	/**
	 * 添加一个元素
	 */
	public void enqueue(Item item) {
		Node<Item> oldlast = last;
		last = new Node<Item>();
		last.item = item;
		last.next = null;
		if (isEmpty())
			first = last;
		else
			oldlast.next = last;
		N++;
	}

	/**
	 * 删除最早添加的元素
	 */
	public Item dequeue() {
		if (isEmpty())
			throw new NoSuchElementException("Queue underflow");
		Item item = first.item;
		first = first.next;
		N--;
		if (isEmpty())
			last = null;
		return item;
	}

	/**
	 * toString方法
	 */
	public String toString() {
		StringBuilder s = new StringBuilder();
		for (Item item : this)
			s.append(item + " ");
		return s.toString();
	}

	/**
	 * 迭代器实现
	 */
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
	 * more tobe.txt
	 * to be or not to - be - - that - - - is
	 */
	public static void main(String[] args) {
		Queue<String> q = new Queue<String>();
		while (!StdIn.isEmpty()) {
			String item = StdIn.readString();
			if (!item.equals("-"))
				q.enqueue(item);
			else if (!q.isEmpty())
				StdOut.print(q.dequeue() + " ");
		}
		StdOut.println("(" + q.size() + " left on queue)");
	}
}
