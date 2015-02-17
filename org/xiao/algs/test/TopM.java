package org.xiao.algs.test;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;

import org.xiao.algs.io.StdIn;
import org.xiao.algs.io.StdOut;
import org.xiao.algs.queue.MinPQ;
import org.xiao.algs.stack.Stack;
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
public class TopM {   

    private TopM() { }

    /***
     *  测试输入流中最大的M个元素
     *  
     *  more tinyBatch.txt
     *  Turing 6/17/1990 644.08
     *  vonNeumann 3/26/2002 4121.85
     *  ...
     *  ...
     *  
     *  java TopM 5 < tinyBatch.txt
     */
    public static void main(String[] args) {
    	//打印输入流中最大的M行
        int M = Integer.parseInt(args[0]); 
        MinPQ<Transaction> pq = new MinPQ<Transaction>(M+1); 

        while (StdIn.hasNextLine()) {
        	//为下一行的输入创建一个元素并放入优先队列中
            String line = StdIn.readLine();
            Transaction transaction = new Transaction(line);
            pq.insert(transaction); 

            if (pq.size() > M) 
            	//如果优先队列中存在M+1个元素则删除其中最小的元素
                pq.delMin();
        }   

        // 打印最大的M个元素
        Stack<Transaction> stack = new Stack<Transaction>();
        for (Transaction transaction : pq)
            stack.push(transaction);
        for (Transaction transaction : stack)
            StdOut.println(transaction);
    } 
} 

class Transaction implements Comparable<Transaction> {
    private final String  who;      // customer
    private final Date    when;     // date
    private final double  amount;   // amount

    public Transaction(String who, Date when, double amount) {
        if (Double.isNaN(amount) || Double.isInfinite(amount))
            throw new IllegalArgumentException("Amount cannot be NaN or infinite");
        this.who    = who;
        this.when   = when;
        if (amount == 0.0) this.amount = 0.0;  // to handle -0.0
        else               this.amount = amount;
    }


    @SuppressWarnings("deprecation")
	public Transaction(String transaction) {
        String[] a = transaction.split("\\s+");
        who    = a[0];
        when   = new Date(a[1]);
        double value = Double.parseDouble(a[2]);
        if (value == 0.0) amount = 0.0;  // convert -0.0 0.0
        else              amount = value;
        if (Double.isNaN(amount) || Double.isInfinite(amount))
            throw new IllegalArgumentException("Amount cannot be NaN or infinite");
    }


    public String who() {
        return who;
    }
 

    public Date when() {
        return when;
    }
 

    public double amount() {
       return amount;
    }


    public String toString() {
        return String.format("%-10s %10s %8.2f", who, when, amount);
    }


    public int compareTo(Transaction that) {
        if      (this.amount < that.amount) return -1;
        else if (this.amount > that.amount) return +1;
        else                                return  0;
    }    


    public boolean equals(Object x) {
        if (x == this) return true;
        if (x == null) return false;
        if (x.getClass() != this.getClass()) return false;
        Transaction that = (Transaction) x;
        return (this.amount == that.amount) && (this.who.equals(that.who))
                                            && (this.when.equals(that.when));
    }


    public int hashCode() {
        int hash = 17;
        hash = 31*hash + who.hashCode();
        hash = 31*hash + when.hashCode();
        hash = 31*hash + ((Double) amount).hashCode();
        return hash;
    }


    public static class WhoOrder implements Comparator<Transaction> {
        public int compare(Transaction v, Transaction w) {
            return v.who.compareTo(w.who);
        }
    }


    public static class WhenOrder implements Comparator<Transaction> {
        public int compare(Transaction v, Transaction w) {
            return v.when.compareTo(w.when);
        }
    }


    public static class HowMuchOrder implements Comparator<Transaction> {
        public int compare(Transaction v, Transaction w) {
            if      (v.amount < w.amount) return -1;
            else if (v.amount > w.amount) return +1;
            else                          return  0;
        }
    }


    public static void test(String[] args) {
        Transaction[] a = new Transaction[4];
        a[0] = new Transaction("Turing   6/17/1990  644.08");
        a[1] = new Transaction("Tarjan   3/26/2002 4121.85");
        a[2] = new Transaction("Knuth    6/14/1999  288.34");
        a[3] = new Transaction("Dijkstra 8/22/2007 2678.40");

        StdOut.println("Unsorted");
        for (int i = 0; i < a.length; i++)
            StdOut.println(a[i]);
        StdOut.println();
        
        StdOut.println("Sort by date");
        Arrays.sort(a, new Transaction.WhenOrder());
        for (int i = 0; i < a.length; i++)
            StdOut.println(a[i]);
        StdOut.println();

        StdOut.println("Sort by customer");
        Arrays.sort(a, new Transaction.WhoOrder());
        for (int i = 0; i < a.length; i++)
            StdOut.println(a[i]);
        StdOut.println();

        StdOut.println("Sort by amount");
        Arrays.sort(a, new Transaction.HowMuchOrder());
        for (int i = 0; i < a.length; i++)
            StdOut.println(a[i]);
        StdOut.println();
    }

}


