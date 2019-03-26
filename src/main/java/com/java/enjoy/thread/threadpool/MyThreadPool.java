package com.java.enjoy.thread.threadpool;

/**
 * Author: Hsiang Leekwok
 * Date: 2019/03/26
 * Description: 实现自己的线程池
 */
public class MyThreadPool {

    private static int THREAD_COUNT = 5;
    private static int MAX_WORK_COUNT = 100;

    private int thCount = 0, wkCount = 0;

    /**
     * 默认构建
     */
    public MyThreadPool() {
        this(THREAD_COUNT, MAX_WORK_COUNT);
    }

    /**
     * 指定线程池大小并且制定最大工作量
     */
    public MyThreadPool(int threadCount, int workCount) {
        this.thCount = threadCount <= 0 ? THREAD_COUNT : threadCount;
        this.wkCount = workCount <= 0 ? MAX_WORK_COUNT : workCount;
    }
}
