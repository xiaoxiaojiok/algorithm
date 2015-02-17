package org.xiao.algs.test;

import org.xiao.algs.io.In;
import org.xiao.algs.io.StdIn;
import org.xiao.algs.io.StdOut;
import org.xiao.algs.queue.Queue;
import org.xiao.algs.search.ST;
/***
 * 
 * 索引和反向索引的查找
 * 
 * 符号表的典型应用
 * 
 * @author XiaoJian
 * 
 * 测试
 * 
 * % java LookupIndex aminoI.csv ","
 *  Serine
 *    TCT
 *    TCA
 *    TCG
 *    AGT
 *    AGC
 *  TCG
 *    Serine
 *
 *  % java LookupIndex movies.txt "/"
 *  Bacon, Kevin
 *    Animal House (1978)
 *    Apollo 13 (1995)
 *    Beauty Shop (2005)
 *    Diner (1982)
 *    Few Good Men, A (1992)
 *    Flatliners (1990)
 *    Footloose (1984)
 *    Friday the 13th (1980)
 *    ...
 *  Tin Men (1987)
 *    DeBoy, David
 *    Blumenfeld, Alan
 *    ...
 *
 */
public class LookupIndex { 

    public static void main(String[] args) {
        String filename  = args[0]; //索引数据库
        String separator = args[1]; //分隔符
        In in = new In(filename);

        ST<String, Queue<String>> st = new ST<String, Queue<String>>();
        ST<String, Queue<String>> ts = new ST<String, Queue<String>>();

        while (in.hasNextLine()) {
            String line = in.readLine();
            String[] fields = line.split(separator);
            String key = fields[0];
            for (int i = 1; i < fields.length; i++) {
                String val = fields[i];
                if (!st.contains(key)) st.put(key, new Queue<String>());
                if (!ts.contains(val)) ts.put(val, new Queue<String>());
                st.get(key).enqueue(val);
                ts.get(val).enqueue(key);
            }
        }

        StdOut.println("Done indexing");

        // 输入标准输入的查询
        while (!StdIn.isEmpty()) {
            String query = StdIn.readLine();
            if (st.contains(query)) 
                for (String vals : st.get(query))
                    StdOut.println("  " + vals);
            if (ts.contains(query)) 
                for (String keys : ts.get(query))
                    StdOut.println("  " + keys);
        }

    }

}
