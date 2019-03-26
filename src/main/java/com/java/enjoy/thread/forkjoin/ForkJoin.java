package com.java.enjoy.thread.forkjoin;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

/**
 * Author: Hsiang Leekwok
 * Date: 2019/03/22 23:01
 * Version: v1.0
 * Description: 使用 Fork/Join 方式
 */
public class ForkJoin {

    private static class WorkerTask extends RecursiveTask<Integer> {

        private static final int THRESHOLD = ArrayTool.ARRAY_SIZE / 10;
        private int[] src;
        private int start, end;

        WorkerTask(int[] source, int start, int end) {
            this.src = source;
            this.start = start;
            this.end = end;
        }

        @Override
        protected Integer compute() {
            if (end - start < THRESHOLD) {
                System.out.println("I am counting...");
                int total = 0;
                for (int i = 0; i <= end; i++) {
                    total += src[i];
                }
                return total;
            } else {
                int middle = (start + end) / 2;
                WorkerTask left = new WorkerTask(src, start, middle);
                WorkerTask right = new WorkerTask(src, middle + 1, end);
                invokeAll(left, right);
                return left.join() + right.join();
            }
        }
    }

    public static void main(String[] args) {
        ForkJoinPool pool = new ForkJoinPool();
        int[] src = ArrayTool.makeArray();
        WorkerTask task = new WorkerTask(src, 0, ArrayTool.ARRAY_SIZE - 1);
        long start = System.currentTimeMillis();
        pool.invoke(task);
        System.out.println("task is running...");
        System.out.println("total: " + task.join() + ", time: " + (System.currentTimeMillis() - start));
    }
}
