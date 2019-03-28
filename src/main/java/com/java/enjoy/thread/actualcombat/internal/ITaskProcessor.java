package com.java.enjoy.thread.actualcombat.internal;

/**
 * Author: Hsiang Leekwok
 * Date: 2019/03/28
 * Description: 开发人员需要实现的接口
 */
public interface ITaskProcessor<T, R> {

    /**
     * 需要开发人员实现的接口
     * */
    TaskResult<R> taskExecute(T data);
}
