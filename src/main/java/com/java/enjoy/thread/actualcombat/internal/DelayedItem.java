package com.java.enjoy.thread.actualcombat.internal;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * Author: Hsiang Leekwok
 * Date: 2019/03/28
 * Description: 超时对象
 */
public class DelayedItem<T> implements Delayed {

    private long activeTime;
    private T data;

    public DelayedItem(long activeTime, T data) {
        // 超时时间是纳秒级别
        this.activeTime = TimeUnit.NANOSECONDS.convert(activeTime, TimeUnit.MILLISECONDS) + activeTime;
        this.data = data;
    }

    public long getActiveTime() {
        return activeTime;
    }

    public T getData() {
        return data;
    }

    @Override
    public long getDelay(TimeUnit unit) {
        return unit.convert(this.activeTime - System.nanoTime(), TimeUnit.NANOSECONDS);
    }

    @Override
    public int compareTo(Delayed o) {
        long c = getDelay(TimeUnit.NANOSECONDS) - o.getDelay(TimeUnit.NANOSECONDS);
        return c == 0 ? 0 : (c > 0 ? 1 : -1);
    }
}
