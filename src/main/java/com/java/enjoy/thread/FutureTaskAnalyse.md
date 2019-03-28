# JDK 中 FutureTask 原理分析

#### 小果 2019/03/28

FutureTask 简介

    FutureTask 是 JDK 中提供的一个能够执行任务并按照一定的约定返回结果的一个工具，其任务会在线程中执行，并不占用主线程的工作时间，
    主线程只需要关注本身该做的事情，以及在恰当的时候等待 FutureTask 的返回结果即可，期间主线程可以随时取消任务。
    
FutureTask 继承自 RunnableFuture, RunnableFuture 继承自 Runnable 和 Future。RunnableFuture 和 Runnable 会提供 run 方法暴露给线程执行，而 Future 会提供以下几个方法来进行任务的取消和获取结果（FutureTask 重载了这几个方法）

- cancel(boolean mayInterruptIfRunning)：取消任务，成功返回 true，失败返回 false，mayInterruptIfRunning 为 true 时会中断已经开始执行的任务

    如果任务还未开始执行，则使用CAS将状态设置为 CANCELLED(mayInterruptIfRunning = false，表示直接取消任务，后续将不执行) 或者 INTERRUPTING(mayInterruptIfRunning = true，表示正在中断任务)
    
    如果任务已经执行完毕，无论 mayInterruptIfRunning 是否为 true，都返回 false，因为无法取消已经完成了的任务
    
    如果任务正在执行过程中，且 mayInterruptIfRunning 为 true 则中断正在执行的线程，将 state 置为 INTERRUPTED，然后返回 true，表示取消成功
    
- isCancelled()：判断任务是否已经在执行完毕之前被取消了
- isDone()：判断任务是否已完成，包括正常执行完毕的、异常结束的和已取消的
- get()：获取任务执行结果的返回值，是个阻塞方法，会一直阻塞直到任务完成
- get(long timeout, TimeUnit unit)：在指定时间内获取执行结果返回值，如果等待超时将会返回 null
    
需要注意的几个主要状态参数以及 task 的状态变化规律：

    private volatile int state;// 保存 task 的运行状态
    // 几个状态常量
    private static final int NEW          = 0;
    private static final int COMPLETING   = 1;
    private static final int NORMAL       = 2;
    private static final int EXCEPTIONAL  = 3;
    private static final int CANCELLED    = 4;
    private static final int INTERRUPTING = 5;
    private static final int INTERRUPTED  = 6;

    状态变化范围：
    NEW -> COMPLETING -> NORMAL         // 新任务 -> 执行 -> 正常结束
    NEW -> COMPLETING -> EXCEPTIONAL    // 新任务 -> 执行 -> 异常结束
    NEW -> CANCELLED                    // 新任务 -> 取消任务
    NEW -> INTERRUPTING -> INTERRUPTED  // 新任务 -> 中断 -> 中断完成
    
1、FutureTask的创建
    
    FutureTask的创建可以传入 Callable 或者 Runnable，但无论传入的是 Callable 还是 Runnable，都会被当作 Callable 来处理。
    PS：如果传入的是 Runnable，会被包装成 Callable，因为 Callable 才有返回值。
    
2、FutureTask的执行过程(run)

    