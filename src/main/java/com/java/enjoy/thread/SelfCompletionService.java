package com.java.enjoy.thread;

import java.util.concurrent.*;

/**
 * Author: Hsiang Leekwok
 * Date: 2019/03/28
 * Description: 自定义按完成顺序拿结果的线程池
 */
public class SelfCompletionService<V> {

    // 执行任务的线程池
    private Executor executor;
    // 结果队列
    private BlockingQueue<Future<V>> blockingQueue;

    // 内部类，包装了 Callable 和 Runnable
    private class Node extends FutureTask<Void> {
        Node(RunnableFuture<V> task) {
            super(task, null);
            this.task = task;
        }

        @Override
        protected void done() {
            blockingQueue.add(task);
        }

        private Future<V> task;
    }

    public SelfCompletionService() {

    }
}
