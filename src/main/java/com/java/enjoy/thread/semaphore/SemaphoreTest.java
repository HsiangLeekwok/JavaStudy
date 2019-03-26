package com.java.enjoy.thread.semaphore;

import com.java.enjoy.thread.tools.ThreadTool;

import java.sql.Connection;
import java.util.Random;

/**
 * Author: Hsiang Leekwok
 * Date: 2019/03/24 12:57
 * Version: v1.0
 * Description: 采用 Semaphore 模拟线程抢占连接池
 */
public class SemaphoreTest {

    private static ConnectionPool pool = new ConnectionPool();

    static class BusinessThread implements Runnable {

        @Override
        public void run() {
            try {
                String name = Thread.currentThread().getName();
                // 让每个线程持有的连接时间不一样
                Random random = new Random();
                long start = System.currentTimeMillis();
                Connection connection = pool.fetchConnection();
                System.out.println("线程" + name + "获取连接共耗时：" + (System.currentTimeMillis() - start) + "ms");
                // 模拟业务操作，持有连接一定的时间
                ThreadTool.sleepMilliseconds(100 + random.nextInt(100));
                System.out.println("线程" + name + "连接使用完毕，归还");
                pool.release(connection);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        for (int i = 0; i < 50; i++) {
            new Thread(new BusinessThread(), (i + 1) + "").start();
        }
    }
}
