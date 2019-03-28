package com.java.enjoy.thread.actualcombat.internal;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Author: Hsiang Leekwok
 * Date: 2019/03/28
 * Description: 任务实体类
 */
public class JobInfo<R> {
    // 任务名称
    private final String jobName;
    // 任务长度
    private final int jobLength;
    // 任务执行的 processor
    private final ITaskProcessor<?, ?> taskProcessor;
    // 处理成功的任务个数
    private final AtomicInteger successCount;
    // 已经处理的任务个数
    private final AtomicInteger processedCount;
    // 结果队列，从头开始拿
    private final LinkedBlockingQueue<TaskResult<R>> blockingQueue;
    // 超时时间
    private final long expireTime;

    public JobInfo(String jobName, int jobLength, ITaskProcessor<?, ?> taskProcessor, long expireTime) {
        this.jobName = jobName;
        this.jobLength = jobLength;
        this.taskProcessor = taskProcessor;
        this.expireTime = expireTime;
        this.successCount = new AtomicInteger(0);
        this.processedCount = new AtomicInteger(0);
        this.blockingQueue = new LinkedBlockingQueue<>(jobLength);
    }

    public ITaskProcessor<?, ?> getTaskProcessor() {
        return taskProcessor;
    }

    // 返回成功处理的个数
    public int getSuccessCount() {
        return successCount.get();
    }

    // 返回处理失败的个数
    public int getFailureCount() {
        return processedCount.get() - successCount.get();
    }

    // 返回已处理的个数
    public int getProcessedCount() {
        return processedCount.get();
    }

    public String getTotalProcessed() {
        return "Success[" + successCount.get() + "]/Current["
                + processedCount.get() + "] Total[" + jobLength + "]";
    }

    // 获取工作中每个任务的处理详情
    public List<TaskResult<R>> getTaskDetail() {
        List<TaskResult<R>> list = new LinkedList<>();
        TaskResult<R> result;
        while ((result = blockingQueue.poll()) != null) {
            list.add(result);
        }
        return list;
    }
}
