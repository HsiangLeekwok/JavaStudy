package com.java.enjoy.thread.threadpool;

import java.util.Random;
import java.util.concurrent.*;

/**
 * Author: Hsiang Leekwok
 * Date: 2019/03/26
 * Description: 测试自建线程池
 */
public class Test {

    private static class WorkTask implements Callable<Integer>, Runnable {
        private int count;
        private CountDownLatch latch;

        WorkTask(int count,CountDownLatch latch) {
            this.count = count;
            this.latch=latch;
        }

        @Override
        public Integer call() throws Exception {
            Thread.sleep(count);
            return calculate();
        }

        @Override
        public void run() {
            calculate();
            try {
                Thread.sleep(count);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            latch.countDown();
        }

        private int calculate() {
            int cnt = 0;
            for (int i = 0; i < count; i++) {
                cnt += i;
            }
            return cnt;
        }
    }


    public static void main(String[] args) throws InterruptedException {
        int count = Runtime.getRuntime().availableProcessors();
        int max = 500;
        CountDownLatch latch = new CountDownLatch(max);
        ExecutorService executor = new ThreadPoolExecutor(count, count, 60, TimeUnit.SECONDS, new LinkedBlockingQueue<>(max), new ThreadPoolExecutor.DiscardPolicy());
        Random random = new Random();
        long start = System.currentTimeMillis();
        for (int i = 0; i < max; i++) {
            executor.execute(new WorkTask(random.nextInt(max),latch));
        }
        latch.await();
        long end = System.currentTimeMillis();
        System.out.println("time used: " + (end - start));
        System.out.println("executor will shutdown.");
        executor.shutdown();
    }
}
