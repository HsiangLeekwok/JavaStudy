##2019/03/23

Fork/Join工具类：

    分而治之 + 工作密取
	二分查找、快速排序、比较排序
	
#### 常用的并发工具类：
	
**CountDownLatch**
		
    作用：是一个或多个线程等待其他所有线程都执行完毕之后才能继续，类似加强版的join
	await: 等待线程用
	
**CyclicBarrier**

作用：让一组线程到达某个屏障被阻塞，直到这个组中最后一个线程到达时才开放，此时组内所有的线程都可以继续运行。

构造的时候传入一个数字和Action，当屏障放开时Action会执行


**区别**

	1、CountDownLoat 由第三者控制，CyclicBarrier有线程组自己控制
	2、CountDownLatch 放行条件可以大于等于线程数，CyclicBarrier放行条件等于线程数
		

**Semaphore**：

    控制同时访问某个资源的线程数量（常用在流量控制方面）
	
**Exchange**：

    用于线程间（两个）的数据交换，exchange方法是个阻塞方法，当两个线程都达到这里时开始开始交换双方持有的数据。
	
**Callable，Future，FutureTask**：

	isDown()：标记是否已结束，不管是正常还是异常结束的，还是自己取消的，都为true
	isCanceled()：任务完成前被取消，true
	cancel(boolean): 取消某个任务，Java内的中断都是协作中断（通知中断），不是强占式
		1、任务还没开始，返回false
		2、任务已经启动，cancel(true)，则中断任务，中断成功，返回true；cancel(false)不会去中断已经开始的任务
		3、任务已经结束，返回false
		
**CAS(Compare And Swap)** 原子操作

    syn基于阻塞的锁机制
    1、被阻塞的线程优先级很高
    2、拿到锁的线程一直不是放锁
    3、大量竞争，消耗cpu，同时带来死锁或者其他安全
    
#### CAS的原理

    CAS(Comare And Swap)，指令级别保证这是一个原子操作
    有三个运算符：一个地址V，一个期望值A，一个新值B
    基本思路：如果地址V上的值和A相等，就给地址V里面的值赋一个新值B；如果不是则不作任何操作
    
**显式锁和AQS**

    Lock 和 synchronized
    synchronized：代码简洁，不可以被中断；Lock：获取可以被中断，超时获取、尝试获取；
    
    ReentrantLock：
    可重入锁，用在递归或者循环嵌套调用带有锁的方法时。默认是非公平锁，可以显示的指定公平锁或非公平锁
    每调用一次计数器加 1，调用完毕之后减 1。
    
    ReentrantLock和synchronized都是独占锁
    
    每个显式锁都可以用多个 Condition 来提供多个条件的 signal/notify
    
    公平锁和非公平锁
    公平锁：按照请求锁的时序来分配锁，每个线程获取锁的几率一样；
    非公平锁：每个线程随机获取到锁；如: 
        A --> 已经获取到了锁并持有，此时 
        B 尝试获取锁，肯定失败，OS有可能会将 B 直接挂起
        过一段时间之后又有 C 来获取锁，恰好可能此时 A 已经用完了锁释放，OS 会在活动线程列表里找一个线程来获取并持有锁（比如C），而不是去挂起列表里恢复某个线程再让其持有锁
        因为在活动列表里让一个线程直接获取锁消耗的时间比去恢复一个挂起的线程再让其持有锁消耗的时间短得多，所以OS会选择C而不是B
        
AQS(AbstractQueueSynchronize)


## 2019/03/25

#### 并发容器

ConcurrentHashTable

    Hash: 哈希(散列)，把任意长度的输入通过一种算法变成固定长度的输出，这个输出内容就是散列值，属于压缩映射，容易造成哈希冲突
        常见算法：直接取余
        碰撞解决办法：
            1、开放寻址：产生冲突之后通过另外一种算法，将结果放到另外一个地方
            2、再散列：产生碰撞之后通过另外一种方式再次哈希
            3、链地址：把产生冲突的哈希值用一个链表保存起来
        md4/md5/sha都属于哈希算法的一种，属于摘要算法
    HasnMap在多线程环境下，Entry会产生一个环形结构造成死循环
    ConcurrentHashMap：
    1.7以前：一个segment队列，segment里有一个table，table的每一个节点对应一个链表(解决散列碰撞)。
    也即解决两个问题：
        1、初始化做了什么事？
        2、如何确定元素的位置；
        3、如何保证线程安全
        
        get()
            定位segment：obj的hash再次hash，高位取模得到在segment队列的位置(快速获取segment的位置)，然后在根据obj的hash值找到在segment中的位置，循环查找链表，找到则返回，反之返回null
        put()
            对segment加锁(segment本身就是一个可重入锁)
        扩容：
            segment本身不会扩容，只会扩容segment下的table，扩容之后index会加上下标的倍数
        size():
            先不加锁统计两次数量，如果两次数量一样，则直接返回；否则会将segment列表加锁之后再统计一次，返回数量
        弱一致性的
        
    1.8 的变化
        1、取消了segment数组，直接用table保存数据，锁的粒度更小，减少并发冲突的概率
        2、链表 + 红黑树的形式。
            纯链表的形式，时间复杂度为O(n)；
            红黑树则为O(log n)，性能提升很大。
            链表转红黑树的时机：链表长度超过8个时，小于6个时会重新转换成链表。
        主要数据结构：
            Node 类存放实际的 key 和 value 值
            sizeCtl：
                负数：表示进行初始化或者扩容
                -n：表示 n 个线程正在进行等待
                -1：表示正在初始化
                0：表示还没有被初始化
                大于 0 的正数：初始化或者是下一次进行扩容的阈值
            TreeNode：用在红黑树中的节点。
            TreeBin：实际放在table数组中，代表了这个红黑树的根
                
        初始化时：只是给成员变量赋值，put()时进行实际数据的填充
        get()：
            定位：把 key 的 hash 值的高 16 位与本身异或运算得到散列地址
        put()：
        扩容操作：
            transfer()方法进行实际扩容操作，table大小也是翻倍的形式，有一个并发扩容机制(put时的线程会帮助进行扩容，然后再插入数据)
        size()方法：只是个估计的大概数量，无法表示精确的数量。
        一致性：弱一致性
        
ConcurrentSkipListMap 和 ConcurrentSkipListSet

    TreeMap 和 TreeSet，有序的容器，也即这两种容器的并发版本
    SkipList：跳表，以空间换时间，概率数据结构
    
ConcurrentLinkedQueue

    LinkedList 的并发版本，无界非阻塞队列，底层是个链表，遵循 FIFO 原则
    add(), offer()都是将元素插入到尾部
    peek()拿头部的数据，但是不移除
    poll()拿头部的数据，但拿完后移除
    
写时复制容器
    
    只能保证最终一致性，不能保证实时一致性，用在读多写少的场景（如白名单、黑名单、商品类目更新等）
    CopyOnWriteArrayList
    CopyOnWriteArraySet