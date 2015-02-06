package org.xiao.algs.util;

/***
 * 
 * 计时器
 * 
 * @author XiaoJian
 *
 */
public class Stopwatch { 

    private final long start;

   /**
     * 创建一个计时器对象
     */
    public Stopwatch() {
        start = System.currentTimeMillis();
    } 


   /**
     * 返回计时器自创建起经过的时间：秒
     */
    public double elapsedTime() {
        long now = System.currentTimeMillis();
        return (now - start) / 1000.0;
    }

} 
