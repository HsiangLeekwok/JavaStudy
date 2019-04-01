package com.java.enjoy.thread.casaqs;

import com.java.enjoy.thread.actualcombat.internal.DelayedItem;
import com.java.enjoy.thread.tools.ThreadTool;

import java.util.Random;
import java.util.concurrent.DelayQueue;

/**
 * Author: Hsiang Leekwok
 * Date: 2019/03/29
 * Description: 延时队列的使用
 */
public class UsingDelayQueue {

    private static class DeleteQueuePeek implements Runnable {
        private DelayQueue<DelayedItem<String>> queue;

        DeleteQueuePeek(DelayQueue<DelayedItem<String>> queue) {
            this.queue = queue;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    if (queue.isEmpty()) {
                        System.out.println("all item is timeout.");
                        break;
                    }
                    DelayedItem<String> item = queue.take();
                    System.out.println("item " + item.getData() + " delay " + item.getActiveTime() + " is timeout.");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public static void main(String[] args) {
        DelayQueue<DelayedItem<String>> queue = new DelayQueue<>();
        Random random = new Random();
        for (int i = 0; i < 100; i++) {
            DelayedItem<String> item = new DelayedItem<>(random.nextInt(600), "item_" + i);
            queue.offer(item);
        }
        new Thread(new DeleteQueuePeek(queue)).start();
        ThreadTool.sleepBySeconds(5);
    }
}
