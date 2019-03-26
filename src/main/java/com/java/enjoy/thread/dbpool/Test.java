package com.java.enjoy.thread.dbpool;

import java.sql.Connection;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Author: Hsiang Leekwok
 * Date: 2019/03/22
 * Description: 测试
 */
public class Test {

    private static DBPool pool = new DBPool(10);
    static CountDownLatch end;

    public static void main(String[] args) throws InterruptedException {
        int threadCount = 50, count = 20;
        end = new CountDownLatch(threadCount);
        AtomicInteger got = new AtomicInteger();
        AtomicInteger loss = new AtomicInteger();
        for (int i = 0; i < threadCount; i++) {
            Thread thread = new Thread(new Worker(count, got, loss),"thread(" + i + ")");
            thread.start();
        }
        end.await();
        System.out.println("All count: " + threadCount * count);
        System.out.println("All got: " + got.get());
        System.out.println("All loss: " + loss.get());
    }

    static class Worker implements Runnable {

        int count;
        AtomicInteger got, loss;

        Worker(int count, AtomicInteger got, AtomicInteger loss) {
            this.count = count;
            this.got = got;
            this.loss = loss;
        }

        @Override
        public void run() {
            while (count > 0) {
                try {
                    Connection connection = pool.fetchConnection(1000);
                    if (null != connection) {
                        try {
                            connection.createStatement();
                            connection.commit();
                        } finally {
                            pool.releaseConnection(connection);
                            got.incrementAndGet();
                        }
                    } else {
                        loss.incrementAndGet();
                        System.out.println(Thread.currentThread().getName() + " wait timeout.");
                    }
                } catch (Exception e) {
                    //e.printStackTrace();
                } finally {
                    count--;
                }
            }
            end.countDown();
        }
    }

}
