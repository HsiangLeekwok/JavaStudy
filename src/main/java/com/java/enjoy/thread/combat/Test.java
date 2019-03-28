package com.java.enjoy.thread.combat;

import java.util.Random;

/**
 * Author: Hsiang Leekwok
 * Date: 2019/03/28 18:49
 * Version: v1.0
 * Description:
 */
public class Test {

    private static class Worker implements IProcessor<Integer> {
        @Override
        public void taskExecute(Integer data) {
            Random random = new Random();
            int sleep = random.nextInt(100);
            try {
                Thread.sleep(sleep);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (sleep % 10 == 9) {
                throw new NullPointerException("Exception happened.");
            }
        }
    }


    public static void main(String[] args) throws InterruptedException {
        String name = "Test";
        PendingJobPool pool = PendingJobPool.getInstance();
        long start = System.currentTimeMillis();
        pool.registerTask(name, 500, 60, new Worker());
        Thread.sleep(10000);
        System.out.println("job \"" + name + "\" processed: " + pool.queryWorkProcessed(name) + ", succeed: " + pool.queryWorkSucceed(name));
        pool.waitWorkComplete();
        long end = System.currentTimeMillis();
        System.out.println("job \"" + name + "\" processed: " + pool.queryWorkProcessed(name) + ", succeed: " + pool.queryWorkSucceed(name) + ", time used: " + (end - start));
    }
}
