package org.xiao.algs.queue;

/*************************************************************************
 *  Compilation:  javac ResizingArrayQueue.java
 *  Execution:    java ResizingArrayQueue < input.txt
 *  Data files:   http://algs4.cs.princeton.edu/13stacks/tobe.txt  
 *  
 *  Queue implementation with a resizing array.
 *
 *  % java ResizingArrayQueue < tobe.txt 
 *  to be or not to be (2 left on queue)
 *
 *************************************************************************/

import java.util.Iterator;
import java.util.NoSuchElementException;

import org.xiao.algs.io.StdIn;
import org.xiao.algs.io.StdOut;

/**
 * 用数组实现的可变可迭代Queue 开销大（每次动态创建并且使用率随着集合大小变化）
 * 
 * @author XiaoJian
 *
 */
public class ResizingArrayQueue<Item> implements Iterable<Item> {
	private Item[] q;
	private int N = 0;
	private int first = 0;
	private int last = 0;

	/**
	 * 创建一个空的队列
	 */
	public ResizingArrayQueue() {
		q = (Item[]) new Object[2];
	}

	/**
	 * 队列是否为空
	 */
	public boolean isEmpty() {
		return N == 0;
	}

	/**
	 * 队列元素的数量
	 */
	public int size() {
		return N;
	}

	// 重新调整队列的大小
	private void resize(int max) {
		assert max >= N;
		Item[] temp = (Item[]) new Object[max];
		for (int i = 0; i < N; i++) {
			temp[i] = q[(first + i) % q.length];
		}
		q = temp;
		first = 0;
		last = N;
	}

	/**
	 * 添加一个元素
	 */
	public void enqueue(Item item) {
		if (N == q.length)
			resize(2 * q.length);
		q[last++] = item;
		if (last == q.length)
			last = 0;
		N++;
	}

	/**
	 * 删除最早添加的元素
	 */
	public Item dequeue() {
		if (isEmpty())
			throw new NoSuchElementException("Queue underflow");
		Item item = q[first];
		q[first] = null; // 避免对象游离
		N--;
		first++;
		if (first == q.length)
			first = 0;
		if (N > 0 && N == q.length / 4)
			resize(q.length / 2);
		return item;
	}

	/**
	 * 返回队列头元素，但不删除
	 */
	public Item peek() {
		if (isEmpty())
			throw new NoSuchElementException("Queue underflow");
		return q[first];
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
			Item item = q[(i + first) % q.length];
			i++;
			return item;
		}
	}

	/**
	 * 测试
	 * more tobe.txt
	 * to be or not to - be - - that - - - is
	 */
	public static void main(String[] args) {
		ResizingArrayQueue<String> q = new ResizingArrayQueue<String>();
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
