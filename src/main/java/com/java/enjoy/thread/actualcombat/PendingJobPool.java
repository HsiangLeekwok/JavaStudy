package com.java.enjoy.thread.actualcombat;

import com.java.enjoy.thread.actualcombat.internal.ITaskProcessor;
import com.java.enjoy.thread.actualcombat.internal.JobInfo;
import com.java.enjoy.thread.actualcombat.internal.TaskResult;
import com.java.enjoy.thread.actualcombat.internal.TaskResultType;

import java.util.concurrent.*;

/**
 * Author: Hsiang Leekwok
 * Date: 2019/03/28
 * Description: 框架主体类
 */
public class PendingJobPool {

    // 线程池中线程的数量
    private static final int THREAD_COUNT = Runtime.getRuntime().availableProcessors();
    // 有界队列
    private static BlockingQueue<Runnable> blockingQueue = new LinkedBlockingQueue<>(5000);
    // 线程池
    private static ExecutorService executor = new ThreadPoolExecutor(THREAD_COUNT, THREAD_COUNT, 60, TimeUnit.SECONDS, blockingQueue);

    // 单例模式
    private PendingJobPool() {

    }

    // 内部类持有单例
    private static class InternalHolder {
        private static PendingJobPool pool = new PendingJobPool();
    }

    /**
     * 返回单例
     */
    public static PendingJobPool getInstance() {
        return InternalHolder.pool;
    }

    // 内部专门执行任务的 runnable
    private static class InternalTask<T, R> implements Runnable {

        private JobInfo<R> job;
        private T data;

        public InternalTask(JobInfo<R> job, T data) {
            this.job = job;
            this.data = data;
        }

        @SuppressWarnings("unchecked")
        @Override
        public void run() {
            ITaskProcessor<T, R> processor = (ITaskProcessor<T, R>) job.getTaskProcessor();
            TaskResult<R> result;
            try {
                result = processor.taskExecute(data);
                if (null == result) {
                    result = new TaskResult<R>(TaskResultType.EXCEPTION, null, "result null");
                }
                if (result.getTaskResultType() == null) {
                    if (result.getReason() == null) {
                        result = new TaskResult<>(TaskResultType.EXCEPTION, result.getReturnValue(), "reason is null");
                    } else {
                        result = new TaskResult<>(TaskResultType.EXCEPTION, result.getReturnValue(), "return null, reason is: " + result.getReason());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                result = new TaskResult<>(TaskResultType.EXCEPTION, null, "exception: " + e.getMessage());
            } finally {
                // 任务执行完毕，且内容填充完毕，加入超时队列等待超时时移除

            }
        }
    }

}
