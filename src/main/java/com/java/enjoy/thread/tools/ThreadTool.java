package com.java.enjoy.thread.tools;

import java.util.concurrent.TimeUnit;

/**
 * Author: Hsiang Leekwok
 * Date: 2019/03/22 22:50
 * Version: v1.0
 * Description: 线程简单睡眠工具
 */
public class ThreadTool {

    /**
     * 睡眠指定的秒数
     */
    public static void sleepBySeconds(int seconds) {
        try {
            TimeUnit.SECONDS.sleep(seconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 睡眠指定的毫秒数
     */
    public static void sleepMilliseconds(int milliseconds) {
        sleepMilliseconds((long) milliseconds);
    }

    /**
     * 睡眠指定的毫秒数
     */
    public static void sleepMilliseconds(long milliseconds) {
        try {
            TimeUnit.MILLISECONDS.sleep(milliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
