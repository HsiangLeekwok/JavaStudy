package com.java.enjoy.thread.countdownlatch;

import com.java.enjoy.thread.tools.ThreadTool;

import java.util.concurrent.CountDownLatch;

/**
 * Author: Hsiang Leekwok
 * Date: 2019/03/23 13:18
 * Version: v1.0
 * Description: 使用 CountDownLatch 进行线程等待尝试
 */
public class UseCountDownLatch {

    private static CountDownLatch countDownLatch;

    // 实现 CountDownLatch 计数的线程
    static class CountThread extends Thread {
        @Override
        public void run() {
            ThreadTool.sleepMilliseconds(5000);
            countDownLatch.countDown();
        }
    }

    // 等待 CountDownLatch 信号的线程
    static class WaitThread implements Runnable {

        @Override
        public void run() {
            try {
                System.out.println("WaitThread is waiting...");
                countDownLatch.await();
                System.out.println("WaitThread await ok.");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        countDownLatch = new CountDownLatch(100);
        for (int i = 0; i < 100; i++) {
            CountThread thread = new CountThread();
            thread.start();
        }
        Thread thread = new Thread(new WaitThread());
        thread.start();

        System.out.println("main is waiting...");
        countDownLatch.await();
        System.out.println("main await ok.");
    }
}
