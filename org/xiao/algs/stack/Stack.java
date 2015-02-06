package org.xiao.algs.stack;

import java.util.Iterator;
import java.util.NoSuchElementException;

import org.xiao.algs.io.StdIn;
import org.xiao.algs.io.StdOut;

/**
 * 用链表实现的Stack
 * 
 * @author XiaoJian
 *
 */
public class Stack<Item> implements Iterable<Item> {
	private int N;
	private Node<Item> first;

	private static class Node<Item> {
		private Item item;
		private Node<Item> next;
	}

	/**
	 * 创建一个空栈
	 */
	public Stack() {
		first = null;
		N = 0;
	}

	/**
	 * 栈是否为空
	 */
	public boolean isEmpty() {
		return first == null;
	}

	/**
	 * 栈中的元素数量
	 */
	public int size() {
		return N;
	}

	/**
	 * 添加一个元素
	 */
	public void push(Item item) {
		Node<Item> oldfirst = first;
		first = new Node<Item>();
		first.item = item;
		first.next = oldfirst;
		N++;
	}

	/**
	 * 删除最近添加的元素.
	 */
	public Item pop() {
		if (isEmpty())
			throw new NoSuchElementException("Stack underflow");
		Item item = first.item;
		first = first.next;
		N--;
		return item;
	}

	/**
	 * 返回栈顶元素，但不删除
	 */
	public Item peek() {
		if (isEmpty())
			throw new NoSuchElementException("Stack underflow");
		return first.item;
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
		Stack<String> s = new Stack<String>();
		while (!StdIn.isEmpty()) {
			String item = StdIn.readString();
			if (!item.equals("-"))
				s.push(item);
			else if (!s.isEmpty())
				StdOut.print(s.pop() + " ");
		}
		StdOut.println("(" + s.size() + " left on stack)");
	}
}
