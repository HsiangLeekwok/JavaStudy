package com.java.enjoy.thread._01_base_and_cooperation;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * Author: Hsiang Leekwok
 * Date: 2019/03/27
 * Description: 线程的调用方式以及中断
 */
public class Interrupt {

    private static class WorkThread extends Thread {

        @Override
        public void run() {
            while (!isInterrupted()) {
                System.out.println("WorkThread running...");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    System.out.println("WorkThread isInterrupted: " + isInterrupted());
                    // 重新 interrupt 一次才能正常中断
                    interrupt();
                    e.printStackTrace();
                }
            }
            System.out.println("WorkThread will exit.");
        }
    }

    private static class WorkRunnable implements Runnable {
        @Override
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                System.out.println("WorkRunnable running...");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    System.out.println("WorkRunnable isInterrupted: " + Thread.currentThread().isInterrupted());
                    // 重新 interrupt 一次才能正常中断
                    Thread.currentThread().interrupt();
                    e.printStackTrace();
                }
            }
            System.out.println("WorkRunnable will exit.");
        }
    }

    private static class WorkCallable implements Callable<String> {
        @Override
        public String call() throws Exception {
            while (!Thread.currentThread().isInterrupted()) {
                System.out.println("WorkCallable running...");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    System.out.println("WorkCallable isInterrupted: " + Thread.currentThread().isInterrupted());
                    // 重新 interrupt 一次才能正常中断
                    Thread.currentThread().interrupt();
                    e.printStackTrace();
                }
            }
            System.out.println("WorkCallable will exit.");
            return "WorkCallable is return.";
        }
    }

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        Thread thread;
//        thread = new WorkThread();
//        thread = new Thread(new WorkRunnable());

        FutureTask<String> task = new FutureTask<>(new WorkCallable());
        thread = new Thread(task);
        thread.start();
        Thread.sleep(2000);
        thread.interrupt();
        System.out.println(task.get());
    }
}
