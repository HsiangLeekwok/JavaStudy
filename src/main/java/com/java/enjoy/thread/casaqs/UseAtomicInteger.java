package com.java.enjoy.thread.casaqs;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Author: Hsiang Leekwok
 * Date: 2019/03/24 16:25
 * Version: v1.0
 * Description:
 */
public class UseAtomicInteger {

    // 线程安全的原子操作类
    private static AtomicInteger ai = new AtomicInteger();
    private static CyclicBarrier barrier = new CyclicBarrier(5);
    private static CountDownLatch latch=new CountDownLatch(5);

    static class AtomThread implements Runnable {
        @Override
        public void run() {
            try {
                // 阻止线程开始就自动计算，直到所有线程都到这里时一起开始计算
                barrier.await();
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }
            String name = Thread.currentThread().getName();
            for (int i = 0; i < 10; i++) {
                // 线程安全的计算结果
                int value = ai.incrementAndGet();
                System.out.println(name + " incrementAndGet " + i + ": " + value);
            }
            latch.countDown();
        }
    }


    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < 5; i++) {
            Thread thread = new Thread(new AtomThread(), (i + 1) + "");
            thread.start();
        }
        latch.await();
        System.out.println("final: " + ai.get());
    }
}
