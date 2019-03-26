package com.java.enjoy.thread.dbpool;

import java.sql.Connection;
import java.util.LinkedList;

/**
 * Author: Hsiang Leekwok
 * Date: 2019/03/22
 * Description: 模拟数据库连接池
 */
public class DBPool {

    private static final LinkedList<Connection> connections = new LinkedList<>();

    /**
     * 初始化连接池大小
     */
    public DBPool(int poolSize) {
        if (poolSize > 0) {
            for (int i = 0; i < poolSize; i++) {
                connections.addLast(SqlConnectionImpl.getConnection());
            }
        }
    }

    /**
     * 拿连接，超时 milliseconds 之后返回 null
     */
    public Connection fetchConnection(int milliseconds) throws InterruptedException {
        synchronized (connections) {
            if (milliseconds <= 0) {
                // 一直等待直到别的线程释放了连接
                while (connections.isEmpty()) {
                    connections.wait();
                }
                // 从链表头部取一个链接甩出去
                return connections.removeFirst();
            } else {
                long overTime = System.currentTimeMillis() + milliseconds;
                long remain = milliseconds;
                while (connections.isEmpty() && remain > 0) {
                    // 连接池暂时为空且等待时间还未到则等待
                    connections.wait(remain);
                    // 计算还剩下的等待时间
                    remain = overTime - System.currentTimeMillis();
                }
                if (connections.isEmpty()) {
                    // 如果连接池为空则返回 null
                    return null;
                } else {
                    // 否则从头部取一个链接甩出去
                    return connections.removeFirst();
                }
            }
        }
    }

    /**
     * 改进版本：是否真的需要计算等待超时的时间？
     * 错误：没有考虑多个线程在等待一个链接的情况，如果只有一个链接被释放，其他等待线程都会得到 null。
     */
    @Deprecated
    public Connection fetchConnection1(int milliseconds) throws InterruptedException {
        synchronized (connections) {
            while (connections.isEmpty()) {
                if (milliseconds <= 0) {
                    connections.wait();
                } else {
                    /*
                        这里是否确实需要计算剩余等待时间那么麻烦？这里的 wait 直接就会等待 milliseconds 这么长：
                        1、未到时间 wait 就返回了，说明别的线程已经返还了一个链接，此时可以直接取走；
                        2、到时间 wait 返回了，说明此时连接池还是空的，此时直接返回一个 null 即可
                     */
                    connections.wait(milliseconds);
                    // 有 notify 来了或者 wait 到时间了，判断一下
                    if (connections.isEmpty()) {
                        break;
                    }
                }
            }
            // 返回一个链接或 null
            return connections.isEmpty() ? null : connections.removeFirst();
        }
    }

    /**
     * 释放一个链接
     */
    public void releaseConnection(Connection connection) {
        if (null != connection) {
            synchronized (connections) {
                connections.addLast(connection);
                connections.notifyAll();
            }
        }
    }
}
