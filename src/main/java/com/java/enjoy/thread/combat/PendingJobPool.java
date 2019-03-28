package com.java.enjoy.thread.combat;

import com.java.enjoy.thread.combat.internal.JobInfo;

import java.util.concurrent.*;

/**
 * Author: Hsiang Leekwok
 * Date: 2019/03/28 18:53
 * Version: v1.0
 * Description:
 */
public class PendingJobPool {

    private final int THREAD_COUNT = Runtime.getRuntime().availableProcessors();

    private final int MAX_POOL_SIZE = 5000;

    private static CountDownLatch latch;

    // 执行任务的线程池
    private final ExecutorService executor = new ThreadPoolExecutor(THREAD_COUNT, THREAD_COUNT, 60, TimeUnit.SECONDS, new LinkedBlockingQueue<>(MAX_POOL_SIZE));

    // 保存的任务列表
    private final ConcurrentHashMap<String, JobInfo<?>> jobs = new ConcurrentHashMap<>();

    private PendingJobPool() {

    }

    /**
     * 内部类持有单一实例
     */
    private static class InnerHolder {
        private static PendingJobPool pool = new PendingJobPool();
    }

    /**
     * 获取单一实例的任务执行池
     */
    public static PendingJobPool getInstance() {
        return InnerHolder.pool;
    }

    /**
     * 内部类，用来包装外部传进来的任务并交给线程池执行
     */
    private static class InnerExecutor<T> implements Runnable {

        private JobInfo<?> jobInfo;
        private T data;

        InnerExecutor(JobInfo<?> jobInfo, T data) {
            this.jobInfo = jobInfo;
            this.data = data;
        }

        @Override
        public void run() {
            IProcessor<T> processor = (IProcessor<T>) jobInfo.getProcessor();
            boolean handled = true;
            try {
                processor.taskExecute(data);
            } catch (Exception e) {
                handled = false;
            } finally {
                // 任务执行完毕，设置任务的状态
                jobInfo.increaseProcessed();
                if (handled) {
                    jobInfo.increaseSuccess();
                }
                latch.countDown();
            }
        }
    }

    /**
     * 从map中拿取已有的任务或者新增任务
     */
    private JobInfo<?> getJobInfo(String jobName, int jobSize, int expireTime, IProcessor<?> processor) {
        JobInfo<?> job = jobs.get(jobName);
        if (null == job) {
            job = new JobInfo<>(jobName, jobSize, processor, expireTime);
            jobs.put(jobName, job);
        }
        return job;
    }

    /**
     * 注册任务
     */
    public void registerTask(String jobName, int jobSize, int expireTime, IProcessor<?> processor) {
        JobInfo<?> job = getJobInfo(jobName, jobSize, expireTime, processor);
        latch = new CountDownLatch(jobSize);
        for (int i = 0; i < jobSize; i++) {
            executor.execute(new InnerExecutor<>(job, i));
        }
    }

    public void waitWorkComplete() throws InterruptedException {
        if (null != latch) {
            latch.await();
        }
    }

    public int queryWorkProcessed(String jobName) {
        JobInfo<?> job = jobs.get(jobName);
        if (null == job) {
            throw new NullPointerException("No job named: " + jobName);
        }
        return job.getProcessed();
    }

    public int queryWorkSucceed(String jobName) {
        JobInfo<?> job = jobs.get(jobName);
        if (null == job) {
            throw new NullPointerException("No job named: " + jobName);
        }
        return job.getSuccess();
    }

    private static class ExpireCheckProcessor {
    }
}
