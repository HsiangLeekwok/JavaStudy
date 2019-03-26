package com.java.enjoy.thread.semaphore;

import java.sql.Connection;
import java.util.LinkedList;
import java.util.concurrent.Semaphore;

/**
 * Author: Hsiang Leekwok
 * Date: 2019/03/24 13:00
 * Version: v1.0
 * Description: 数据库连接池
 */
public class ConnectionPool {

    // 默认 10 个数据库连接
    private static final int SIZE = 10;
    private static final LinkedList<Connection> pools = new LinkedList<>();
    // 可用连接数量和已用数量
    private static Semaphore useful, used;

    static {
        // 初始化 10 个连接
        for (int i = 0; i < SIZE; i++) {
            pools.addLast(new ConnectionImpl());
        }
    }

    ConnectionPool() {
        // 初始化时默认可用连接是 10 个
        useful = new Semaphore(SIZE);
        // 初始化是默认已用数量为 0 个
        used = new Semaphore(0);
    }

    /**
     * 拿连接
     */
    public Connection fetchConnection() throws InterruptedException {
        // 可用连接减少
        useful.acquire();
        Connection connection;
        synchronized (pools) {
            connection = pools.removeFirst();
        }
        // 已用连接数增加
        used.release();
        return connection;
    }

    /**
     * 释放连接
     */
    public void release(Connection connection) throws InterruptedException {
        if (null != connection) {
            System.out.println("当前有" + useful.getQueueLength() + "个线程等待, 可用连接数为" + useful.availablePermits());
            used.acquire();
            synchronized (pools) {
                pools.addLast(connection);
            }
            useful.release();
        }
    }
}
