package com.java.enjoy;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * Author: Hsiang Leekwok
 * Date: 2019/03/24 22:50
 * Version: v1.0
 * Description: 实现自己的锁(类似 ReentrantLock)
 */
public class SelfLock implements Lock {

    private static class AQS extends AbstractQueuedSynchronizer {

        /**
         * 当前没有线程持有锁
         */
        private static final int STATE_0 = 0;
        /**
         * 当前已有线程持有锁
         */
        private static final int STATE_1 = 1;

        @Override
        protected boolean tryAcquire(int arg) {
            // 如果成功从状态 0 转到状态 1，则说明线程已经成功持有锁
            if (compareAndSetState(STATE_0, STATE_1)) {
                // 设置当前已经持有锁的线程
                setExclusiveOwnerThread(Thread.currentThread());
                return true;
            }
            return false;
        }

        @Override
        protected boolean tryRelease(int arg) {
            // 由于是独占锁，释放的时候可以直接释放状态到 0
            setState(STATE_0);
            // 同时设置持有的线程为 null
            setExclusiveOwnerThread(null);
            return true;
        }

        /**
         * 当前锁是否已被占用
         */
        @Override
        protected boolean isHeldExclusively() {
            return super.isHeldExclusively();
        }

        @Override
        protected int tryAcquireShared(int arg) {
            return super.tryAcquireShared(arg);
        }

        @Override
        protected boolean tryReleaseShared(int arg) {
            return super.tryReleaseShared(arg);
        }
    }

    @Override
    public void lock() {

    }

    @Override
    public void lockInterruptibly() throws InterruptedException {

    }

    @Override
    public boolean tryLock() {
        return false;
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return false;
    }

    @Override
    public void unlock() {

    }

    @Override
    public Condition newCondition() {
        return null;
    }

}
