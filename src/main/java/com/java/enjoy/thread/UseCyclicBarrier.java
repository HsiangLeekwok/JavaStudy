package com.java.enjoy.thread;

import com.java.enjoy.thread.tools.ThreadTool;

import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CyclicBarrier;

/**
 * Author: Hsiang Leekwok
 * Date: 2019/03/23 19:30
 * Version: v1.0
 * Description: 使用 CyclicBarrier 进行线程等待尝试
 */
public class UseCyclicBarrier {

    static CyclicBarrier cyclicBarrier;
    static ConcurrentHashMap<String, Long> concurrentHashMap = new ConcurrentHashMap<>();

    // 工作线程，需要等待所有工作线程都准备好之后才进行
    static class WorkThread implements Runnable {
        @Override
        public void run() {
            try {
                long id = Thread.currentThread().getId();
                concurrentHashMap.put(Thread.currentThread().getId() + "", id);
                Random random = new Random();
                if (random.nextBoolean()) {
                    System.out.println("thread_" + id + " is doing sth...");
                    ThreadTool.sleepMilliseconds((int) (2000 + id));
                    System.out.println("thread_" + id + " is doing sth...");
                }
                System.out.println("thread_" + id + " is waiting...");
                cyclicBarrier.await();
                System.out.println("thread_" + id + " is waited and do business.");
                ThreadTool.sleepMilliseconds(1000 + id);
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }
        }
    }

    // 结束之后的收尾工作
    static class ActionCall implements Runnable {

        @Override
        public void run() {
            System.out.println("ActionCall is start.");
            int total = 0;
            for (String key : concurrentHashMap.keySet()) {
                total += concurrentHashMap.get(key);
            }
            System.out.println("ActionCall is completed, total: " + total);
        }
    }

    public static void main(String[] args) throws BrokenBarrierException, InterruptedException {
        int threadCount = 5;
        cyclicBarrier = new CyclicBarrier(threadCount, new ActionCall());
        for (int i = 0; i < threadCount; i++) {
            Thread thread = new Thread(new WorkThread());
            thread.start();
        }
    }
}
