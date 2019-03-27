package com.java.enjoy.thread._01_base_and_cooperation;

/**
 * Author: Hsiang Leekwok
 * Date: 2019/03/27
 * Description: 线程间的共享
 */
public class Sharing {


    /**
     * 对象锁
     */
    public synchronized void handleObject() {
        notifyAll();
    }

    /**
     * 对象锁
     */
    public void handleObject1() {
        synchronized (this) {

        }
    }

    /**
     * 类锁
     */
    public static synchronized void handleClass() {

    }
}
