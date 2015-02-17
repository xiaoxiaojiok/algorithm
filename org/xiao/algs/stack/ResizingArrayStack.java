package org.xiao.algs.stack;

import java.util.Iterator;
import java.util.NoSuchElementException;

import org.xiao.algs.io.StdIn;
import org.xiao.algs.io.StdOut;

/**
 * 用数组实现的可变可迭代Stack 开销大（每次动态创建并且使用率随着集合大小变化）
 * 
 * @author XiaoJian
 *
 */
public class ResizingArrayStack<Item> implements Iterable<Item> {
	private Item[] a; // array of items
	private int N; // number of elements on stack

	/**
	 * 创建一个空栈
	 */
	@SuppressWarnings("unchecked")
	public ResizingArrayStack() {
		a = (Item[]) new Object[2];
	}

	/**
	 * 栈是否为空
	 */
	public boolean isEmpty() {
		return N == 0;
	}

	/**
	 * 栈的元素数量
	 */
	public int size() {
		return N;
	}

	// 动态调整栈的大小
	@SuppressWarnings("unchecked")
	private void resize(int capacity) {
		// 将栈移动到一个大小为capacity的新数组
		assert capacity >= N;
		Item[] temp = (Item[]) new Object[capacity];
		for (int i = 0; i < N; i++) {
			temp[i] = a[i];
		}
		a = temp;
	}

	/**
	 * 添加一个元素
	 */
	public void push(Item item) {
		if (N == a.length)
			resize(2 * a.length);
		// 将元素添加到栈顶
		a[N++] = item;
	}

	/**
	 * 删除最近添加的元素
	 */
	public Item pop() {
		if (isEmpty())
			throw new NoSuchElementException("Stack underflow");
		// 从栈顶删除元素
		Item item = a[N - 1];
		a[N - 1] = null; // 避免对象游离
		N--;
		if (N > 0 && N == a.length / 4)
			resize(a.length / 2);
		return item;
	}

	/**
	 * 返回栈顶元素，但不删除
	 */
	public Item peek() {
		if (isEmpty())
			throw new NoSuchElementException("Stack underflow");
		return a[N - 1];
	}

	/**
	 * 迭代器实现
	 */
	public Iterator<Item> iterator() {
		return new ReverseArrayIterator();
	}

	private class ReverseArrayIterator implements Iterator<Item> {
		// 支持后进先出的迭代
		private int i;

		public ReverseArrayIterator() {
			i = N;
		}

		public boolean hasNext() {
			return i > 0;
		}

		public void remove() {
			throw new UnsupportedOperationException();
		}

		public Item next() {
			if (!hasNext())
				throw new NoSuchElementException();
			return a[--i];
		}
	}

	/**
	 * 测试 more tobe.txt to be or not to - be - - that - - - is
	 */
	public static void main(String[] args) {
		ResizingArrayStack<String> s = new ResizingArrayStack<String>();
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
