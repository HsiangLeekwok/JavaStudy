package com.java.enjoy.thread._01_base_and_cooperation;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;

/**
 * Author: Hsiang Leekwok
 * Date: 2019/03/27
 * Description: 查看 Main 进程的线程列表
 */
public class OnlyMain {

    public static void main(String[] args) {
        ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
        ThreadInfo[] infos = threadMXBean.dumpAllThreads(false, false);
        for (ThreadInfo info : infos) {
            System.out.println("[" + info.getThreadId() + "] " + info.getThreadName());
        }
    }
}
