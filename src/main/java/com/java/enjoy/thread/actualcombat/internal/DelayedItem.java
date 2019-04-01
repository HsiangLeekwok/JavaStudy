package com.java.enjoy.thread.actualcombat.internal;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * Author: Hsiang Leekwok
 * Date: 2019/03/28
 * Description: 超时对象
 */
public class DelayedItem<Data> implements Delayed {

    // 活跃时长
    private long activeTime;
    // 超时时间
    private long expireTime;
    private Data data;

    public DelayedItem(long activeTime, Data data) {
        // 超时时间是纳秒级别
        this.activeTime = activeTime;
        this.expireTime = TimeUnit.NANOSECONDS.convert(activeTime, TimeUnit.MILLISECONDS) + System.nanoTime();
        this.data = data;
    }

    public long getActiveTime() {
        return activeTime;
    }

    public Data getData() {
        return data;
    }

    @Override
    public long getDelay(TimeUnit unit) {
        return unit.convert(this.expireTime - System.nanoTime(), TimeUnit.NANOSECONDS);
    }

    @Override
    public int compareTo(Delayed o) {
        long c = getDelay(TimeUnit.NANOSECONDS) - o.getDelay(TimeUnit.NANOSECONDS);
        return c == 0 ? 0 : (c > 0 ? 1 : -1);
    }
}
