package com.java.enjoy.thread.combat.internal;

import com.java.enjoy.thread.combat.IProcessor;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Author: Hsiang Leekwok
 * Date: 2019/03/28 18:51
 * Version: v1.0
 * Description: 任务信息
 */
public class JobInfo<P> {
    // 任务名字
    private final String jobName;
    // 任务数量
    private final int jobSize;
    // 任务执行的回调
    private final IProcessor<P> processor;

    private AtomicInteger successCount;
    private AtomicInteger processedCount;
    // 任务列表
    //private final ConcurrentHashMap<Object, Boolean> tasks;
    // 任务超时时间
    private final long expireTime;


    public JobInfo(String jobName, int jobSize, IProcessor<P> processor, long expireTime) {
        this.jobName = jobName;
        this.jobSize = jobSize;
        this.processor = processor;
        this.expireTime = expireTime;
        this.successCount = new AtomicInteger(0);
        this.processedCount = new AtomicInteger(0);
        //this.tasks = new ConcurrentHashMap<>();
    }

    /**
     * 获取当前任务的处理器
     */
    public IProcessor<P> getProcessor() {
        return processor;
    }

    /**
     * 获取任务的超时时间
     */
    public long getExpireTime() {
        return expireTime;
    }

    /**
     * 获取已经处理成功了的任务数量
     */
    public int getSuccess() {
        return successCount.get();
    }

    public void increaseSuccess() {
        successCount.incrementAndGet();
    }

    /**
     * 获取已经处理过的任务数量
     */
    public int getProcessed() {
        return processedCount.get();
    }

    public void increaseProcessed() {
        processedCount.incrementAndGet();
    }
}
