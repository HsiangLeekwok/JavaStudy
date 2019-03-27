# 学习笔记：线程基础、现成之间的共享和协作

## 2019/03/27

- 基础概念

###### CPU核心数和线程数的关系

    核心数：线程数 1:1 关系；超线程技术：1:2

###### CPU时间片轮转机制

    RR调度，上下文切换(大约耗费 5k - 20k 时钟周期)

###### 什么是进程和线程

    进程：程序运行资源分配的最小单位，进程内部可以有多个线程，多个线程之间共享进程的资源
    线程：CPU 调度的最小单位，线程本身是不能独立运行的，也不能拥有自己的资源的，必须要依附某个进程

###### 澄清并行和并发

    并发(concurrency)：在单位时间内可以处理事情的能力
        1、两个或多个事件在同一个时间段内发生
        2、同一实体上的多个事件
        3、在一台处理器上“同时”处理多个任务
    并行(parallellism)：同一时刻可以处理事情的能力
        1、两个或多个事件在同一时刻发生
        2、不同实体上的多个事件
        3、在多台处理器上同时处理多个任务

###### 高并发编程的意义、好处和注意事项

    共享资源存在冲突，比如死锁。太多的线程有可能搞垮机器
    
- 认识 Java 里的线程

###### OnlyMain 只有一个 main 方法的进程，启动时会有以下这么多的线程

```JAVA
    public class OnlyMain {

        public static void main(String[] args) {
            ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
            ThreadInfo[] infos = threadMXBean.dumpAllThreads(false, false);
            for (ThreadInfo info : infos) {
                System.out.println("[" + info.getThreadId() + "] " + info.getThreadName());
            }
        }
    }
```

    打印出来的线程列表：
    [6] Monitor Ctrl-Break
    [5] Attach Listener
    [4] Signal Dispatcher
    [3] Finalizer
    [2] Reference Handler
    [1] main
    
###### 让 Java 里的线程停止
    
    1、自然执行完毕
    2、抛出异常
    3、stop()、resume()、suspend()：suspend不会释放资源
    
    4、中断线程
        interrupt() 中断一个线程，并不是强行关闭，只是设置一个标记表示线程需要中断，需要在线程内部自己判断以便中断线程释放资源
        isInterrupt() 判断当前线程是否处于中断状态
        static 方法 interrupted()判断当前线程是否处于中断状态，调用之后会把 interrupt 标志改为 false
    
    线程中的某个方法抛出 InterruptException 的时候，会将 interrupt 标志位改为 false，此时需要在 catch 里手动再 interrupt() 才能正常中断出去
    
###### 守护线程

    finally 在守护线程里不能保证被执行
    
###### 线程间的共享

    synchronized
        1、对象锁：synchronized 关键字放在普通方法或代码块的时候表示对象锁，也即 new 出来的对象这个实例里的锁。
        2、类锁：synchronized 关键字放在 static 方法上，表示类锁。类锁在整个 JVM 里只会有一个，所有线程都会去争夺这个锁。
        
```JAVA
public class Sharing {

    /**
     * 对象锁
     */
    public synchronized void handleObject() {
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
```

    volatile 最轻量级的同步机制，但不能保证操作的原子性
        1、读取变量的时候会从主内存中读取
        2、写入变量之后会强制其他引用了这个变量的线程都去刷新变量的值
        
        只 get()、set()值的时候可以用
        常用在一个线程写，其他线程读的场景
        
    ThreadLocal 线程变量，每个线程都会有一份副本，会造成内存占用很大(n倍占用)，实现方式是里面有一个 Map 来保存 Thread 和变量的映射关系
    
    对象锁：
    wait()
    notify()/notifyAll()
    尽量用notifyAll()来唤醒所有在这个对象上的等待队列以便线程自己判断是否达到条件
    wait(timeout) 等待超时模式的范式

```JAVA
// 连接池
public class ConnectionPool {
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
                
                //*********************************
                // 以下为 wait(timeout) 的使用的范式
                long overTime = System.currentTimeMillis() + milliseconds;
                long remain = milliseconds;
                while (connections.isEmpty() && remain > 0) {
                    // 连接池暂时为空且等待时间还未到则等待
                    connections.wait(remain);
                    // 计算还剩下的等待时间
                    remain = overTime - System.currentTimeMillis();
                }
                //*********************************
                
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
}
```

    join()方法（面试点）
        线程 A 执行了线程 B 的 join() 方法，A 必须要等到 B 执行完了之后才能继续自己的工作。且 A 不能保证 B 会马上就开始执行（这得由 OS 决定）

    yield()/sleep()/wait()/notify()的区别
        yield 虽然放弃了时钟周期，但不会释放已持有的锁，所以其他线程有可能还是获取不到执行的机会
        sleep 也不会释放已持有的锁
        wait 调用之前必须要持有锁，调用之后锁会被 JVM 自动释放，当 wait 方法返回后会自动再持有锁
        notify/notifyAll 调用之前必须要持有锁，但调用之后不会释放锁，会在 sync 代码块完成之后释放锁，所以本方法一般都放在代码块的最后一行

