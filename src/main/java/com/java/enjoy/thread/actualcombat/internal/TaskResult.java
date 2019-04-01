package com.java.enjoy.thread.actualcombat.internal;

/**
 * Author: Hsiang Leekwok
 * Date: 2019/03/28
 * Description: 任务返回类
 */
public class TaskResult<Result> {
    // 结果返回状态
    private final TaskResultType taskResultType;
    // 方法的返回数据
    private final Result returnValue;
    // 方法的结果描述
    private final String reason;

    public TaskResult(TaskResultType taskResultType, Result returnValue, String reason) {
        this.taskResultType = taskResultType;
        this.returnValue = returnValue;
        this.reason = reason;
    }

    public TaskResult(TaskResultType taskResultType, Result returnValue) {
        this.taskResultType = taskResultType;
        this.returnValue = returnValue;
        this.reason = "Success";
    }

    public TaskResultType getTaskResultType() {
        return taskResultType;
    }

    public Result getReturnValue() {
        return returnValue;
    }

    public String getReason() {
        return reason;
    }

    @Override
    public String toString() {
        return "TaskResult = [taskResultType = " + taskResultType + ", returnValue = " + returnValue + ", reason = " + reason + "]";
    }
}
