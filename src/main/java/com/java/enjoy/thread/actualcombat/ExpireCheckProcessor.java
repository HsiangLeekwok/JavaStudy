package com.java.enjoy.thread.actualcombat;

import com.java.enjoy.thread.actualcombat.internal.DelayedItem;

import java.util.concurrent.DelayQueue;

/**
 * Author: Hsiang Leekwok
 * Date: 2019/03/28
 * Description: 超时查询处理
 */
public class ExpireCheckProcessor {

    private static final DelayQueue<DelayedItem<String>> queue = new DelayQueue<>();

    // 单例模式
    private ExpireCheckProcessor() {

    }

    // 内部类延迟初始化
    private static class InnerHolder {
        static final ExpireCheckProcessor holder = new ExpireCheckProcessor();
    }

    /**
     * 获取超时查询的单例
     */
    public static ExpireCheckProcessor getInstance() {
        return InnerHolder.holder;
    }

    private static class ExpireTask implements Runnable {
        @Override
        public void run() {
            while (true) {
                try {
                    if (queue.isEmpty()) {
                        // 如果队列为空的话，休眠100毫秒之后再尝试查询
                        Thread.sleep(100);
                    } else {
                        // 队列不为空的尝试获取第一个超时的任务
                        DelayedItem<String> item = queue.take();
                        System.out.println("任务" + item.getData() + "已超时移除缓存");
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 将任务超时信息加入队列，相同的任务不会覆盖
     */
    public void addTask(String jobName, int expireTime) {
        queue.offer(new DelayedItem<>(expireTime, jobName));
    }

    static {
        Thread thread = new Thread(new ExpireTask());
        thread.setDaemon(true);
        thread.start();
        System.out.println("启动任务超时检索守护线程...........");
    }
}
