package com.java.enjoy;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Author: Hsiang Leekwok
 * Date: 2019/03/24 22:29
 * Version: v1.0
 * Description: 实现自增的原子 Integer 类
 */
public class SelfIncreaseAtomicInteger {

    private static AtomicInteger ai = new AtomicInteger();

    /**
     * 实现原子操作级别的自增
     */
    public void selfIncrease() {
        for (; ; ) {
            int value = ai.get();
            boolean success = ai.compareAndSet(value, ++value);
            if (success) {
                break;
            }
        }
    }

    /**
     * 获取值
     */
    public int get() {
        return ai.get();
    }
}
