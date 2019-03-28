package com.java.enjoy.thread.combat;

/**
 * Author: Hsiang Leekwok
 * Date: 2019/03/28 18:52
 * Version: v1.0
 * Description: 任务的处理器
 */
public interface IProcessor<P> {
    /**
     * 任务的执行过程
     */
    void taskExecute(P data);
}
