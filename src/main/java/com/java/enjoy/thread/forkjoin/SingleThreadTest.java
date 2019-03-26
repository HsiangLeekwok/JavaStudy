package com.java.enjoy.thread.forkjoin;

/**
 * Author: Hsiang Leekwok
 * Date: 2019/03/22 22:42
 * Version: v1.0
 * Description: 单线程计算大量数据
 */
public class SingleThreadTest {

    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        long total = 0;
        int[] array = ArrayTool.makeArray();
        for (int i = 0; i < ArrayTool.ARRAY_SIZE; i++) {
            //ThreadTool.sleepMilliseconds(1);
            total += array[i];
        }
        long end = System.currentTimeMillis();
        System.out.println("total: " + total + ", time: " + (end - start));
    }
}
