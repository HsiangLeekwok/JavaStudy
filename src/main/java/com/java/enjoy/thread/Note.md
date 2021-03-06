##2019/03/23

Fork/Join工具类：

    模块为 N 的问题，N < 阈值，直接解决，反之，将 N 分解为 K 个小规模子问题，子问题互相对立，与原问题形式相同，将子问题的解合并为原问题的解
    分而治之 + 工作密取
	二分查找、快速排序、比较排序
	
	
#### 常用的并发工具类：
	
**CountDownLatch**
		
    作用：是一个或多个线程等待其他所有线程都执行完毕之后才能继续，类似加强版的join
	await: 等待线程用
	
**CyclicBarrier**

    作用：让一组线程到达某个屏障被阻塞，直到这个组中最后一个线程到达时才开放，此时组内所有的线程都可以继续运行。
    构造的时候传入一个数字和Action，当屏障放开时Action会执行


    区别
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
    缺点：内存空间占用(至少2份内存空间)
    CopyOnWriteArrayList
    CopyOnWriteArraySet
	
阻塞队列

	概念、生产者消费者模式：两者之间能力不匹配的问题，加一个容器解决两者之间的耦合
		当队列满的时候，插入元素的线程被阻塞，直到队列不满
		当队列空的时候，获取元素的线程被阻塞，直到队列不空

常用方法：

|方法|抛出异常|返回值|一直阻塞|超时退出|
|----------|----------|----------|----------|----------|
|插入方法|add|offer|put|offer(timeout)|
|移除方法|remove|poll|take|poll(timeout)|
|检查方法|element|peek|-|-|
	
	常用阻塞队列：
		ArrayBlockingQueue：一个由数组结构组成的[有界]阻塞队列。按照FIFO原则，要求设定初始大小
		LinkedBlockingQueue：一个由链表结构组成的[有界]阻塞队列。按照FIFO原则可以不设定初始大小
		    两者不同：
		        1、锁上面，Array只有一个锁，Link用了两个锁
		        2、实现上：Array直接插入元素，Link需要转换
		        
		PriorityBlockingQueue：一个支持优先级排序的[无界]阻塞队列。
		    默认情况下，按照自然顺序排序；或者实现 CompareTo()方法，或者指定构造参数Comparator来指定排序
		    同优先级元素的顺序是随机的
		DelayQueue：一个使用优先级队列实现的[无界]阻塞队列。支持延时获取元素的阻塞队列，元素必须要实现 Delayed 接口
		    如：缓存系统、订单到期、先期支付等
		SynchronousQueue：一个不存储元素的阻塞队列。
		    每一个 put 操作都要等待一个 take 操作
		LinkedTransferQueue：一个由链表结构组成的[无界]阻塞队列。
		    transfer(), tryTransfer()：插入的时候看是否有消费者在等待，如果有则直接交给消费者，否则再插入队列。
		        区别：
		            transfer()必须要消费者消费了以后才会返回
		            tryTransfer()无论消费者是否接收，方法都立即返回
		LinkedBlockingDeque：一个由链表结构组成的双向阻塞队列。
		    队列的头和尾都可以插入和移除元素。可以冲方法名是否带 first/last 来区别是从头还是尾部取元素。
		    add() = addLast(), take() = takeFirst()
		    Fork/Join实现中就是用此链表来实现工作密取

线程池

什么是线程池？为什么要哟个线程池？

    1、降低资源的消耗：降低现成创建和销毁的资源消耗；
    2、提高响应速度：线程的创建时间为 T1，执行时间为 T2，销毁时间为 T3，免去了T1 和 T3 的时间
    3、提高县城的可管理性
    
实现一个自己的线程池

    1、线程必须在池中已经创建好了，并且可以保持住，要有容器来保存这些线程；
    2、线程还要能够接受外部的任务，且可以运行外部的任务
    
合理配置线程池

    根据任务的性质来：计算密集型(CPU)，IO密集型，混合型
        计算密集型：加密、大数分解、正则……
            线程数要适当小一点，最大推荐：大约建议是 CPU 核心数+1（防止页缺失的情况）
                页缺失：线程的资源还未来得及加载到内存（有可能还在磁盘），导致该线程被 CPU 挂起
        IO密集型：读取文件，数据库连接，网络通信等，线程数要适当大一点，大约 CPU 核心数x2。
            业务上线之后需要分析CPU的用户态和和心态之间的占比，如果和心态过多则需要考虑增大线程数量
        混合型：尽量拆分。拆分后如果 IO 密集型远大于计算密集型，则拆分意义不大。如果拆分后IO密集型和计算密集型所占比例不大才能提高效率。
    
    队列的选择上，应该使用有界队列，无界队列可能会导致内存溢出OOM

SDK预定义的线程池

    FixedThreadPool: 创建固定线程数量的，适用于负载较重的服务器，使用了无界队列
    SingleThreadExecutor: 创建单个线程，需要顺序保证执行任务，不会有多个线程活动，使用了无界队列
    CachedThreadPool: 会根据需要来创建新线程的，执行很多短期异步任务的程序，使用了SynchronousQueue
    WorkStealingPool: 基于ForkJoinPool实现，工作密取（JDK 1.7以后）
    ScheduledThreadPoolExecutor: 需要定期执行周期任务，Timer不建议使用了
        newSingleThreadScheduledExecutor：只包含一个线程，只需要单个线程执行周期任务，保证顺序的执行各个任务
        newScheduledThreadPool 可以包含多个线程的，线程执行周期任务，适度控制后台线程数量的时候
        方法说明：
        schedule：只执行一次，任务还可以延时执行
        scheduleAtFixedRate：提交固定时间间隔的任务
        scheduleWithFixedDelay：提交固定延时间隔执行的任务
        scheduleAtFixedRate任务超时：
            规定60s执行一次，有任务执行了80S，下个任务马上开始执行
            第一个任务 时长 80s，第二个任务20s，第三个任务 50s
            第一个任务第0秒开始，第80S结束；
            第二个任务第80s开始，在第100秒结束；
            第三个任务第120s秒开始，170秒结束
            第四个任务从180s开始
        建议在提交给ScheduledThreadPoolExecutor的任务要住catch异常

## 线程安全(2019/03/27)

### 类的线程安全定义

如果多线程下使用这个类，不论多线程如何使用和调度这个类，这个类总是表示出正确的行为，这个类就是线程安全的。

1. 操作的原子性

2. 内存的可见性

会在多个线程之间共享状态的时候，就会出现线程安全

怎么做到类的线程安全？

- 栈封闭

    ###### 所有的变量都是在方法内部声明的，这些变量都属于栈封闭状态
    
- 无状态

    ###### 没有任何成员变量的类叫无状态类

- 让类不可变

    ###### 让状态不可变，所有包装类型的基本累都是不可变的类
    ###### 对于一个类，要做到线程安全需要做到：
    ###### 1、所有成员变量应该都是私有的，只要可能，用 final 关键字修饰类成员及其持有的引用对象的内部变量
    ###### 2、不提供可修改成员变量的地方，同时成员变量不作为方法的返回值

- volatile

    ###### 保证类的可见性，最适合一个线程写，多个线程读的情况。
    ###### 如 ConcurrentHashMap 的 put 方法中对 Node 节点的 value 值就是个 volatile 类型变量，put 时候加锁，但 get 的时候是不加锁的。

- 加锁和CAS
- 安全的发布
- ThreadLocal

### 线程不安全造成的问题

#### 死锁

    是指两个或两个以上的进程在执行过程中，由于竞争资源或者由于彼此通信而造成的一种阻塞现象，若无外力作用，它们都将无法推进下去。此时称系统处于死锁状态或系统产生了死锁。
    
##### 有哪些死锁

    简单的：
    动态的：保证持有锁的顺序（按照一定的排序顺序来持有锁，如果某个锁持有失败，则放弃所有锁并等待下次再尝试持有锁）
    
##### 怎么解决死锁
##### 其他死锁问题
    
#### 活锁

    由动态锁造成争夺资源线程之间总是无休止的尝试获取锁然后放弃锁，此时在放弃所有锁的同时让线程休眠一个随机时间以便让出机会来让其他线程完成持有全部锁的机会。
    
#### 线程饥饿

    低优先级的线程，总是拿不到执行时间
    
#### 性能和思考

    使用并发的目标是为了提高性能。引入多线程之后，会引入额外的开销。
    衡量应用程序性能：服务时间、延迟时间、吞吐量、可伸缩性
        服务时间、延迟时间：表明有多快
        吞吐量：表示处理能力的指标，完成工作的多少
            多快和多少完全独立，甚至是互相矛盾的。
            对服务器应用来说，多少（可伸缩性，吞吐量）这个方面比多快更受重视
    做应用的时候：
        1、先保证程序正确，确实达不到要求的时候，在提高速度（黄金原则）
        2、一定要以测试为基准
        
    一个应用程序，串行的部分是永远无法避免的
    
    Amdahl定律： 1/(F+(1-N)/N)   F: 必须被串行的部分。程序最好的结果：1/F

#### 影响性能的因素

    上下文切换，一个上下文切换5k - 10k个时钟周期，约几微妙
    内存同步，也即加锁，会增加额外的指令
    阻塞，阻塞导致线程挂起，一个挂起包括两次额外的上下文切换
    
    减少锁的竞争：
    减少锁的粒度：使用锁的时候，锁保护的对象是多个，多个对象是独立变化的时候，不如用多个锁来意义保护这些对象，但要注意避免发生死锁。
    减少锁的范围：对锁的持有，快进快出，尽量缩短持有锁的时间
    避免多余的缩小锁的范围：JVM里有锁粗化机制来解决这个问题，但编程的时候一定要避免多余的缩小锁的范围
    锁分段：ConcurrentHashMap里有分段锁
    替换独占锁：
        1、使用读写锁；
        2、用自旋 CAS
        3、使用系统的并发容器

    单例模式是否安全：
        双重检查锁定，内部复杂类加 volatile 关键字
        解决方案：
        1、饿汉式：在JVM中，队类的加载和类初始化，由虚拟机保证线程安全。
            枚举也属于饿汉式。
        2、懒汉式：类初始化模式，也叫延迟占位模式。如果类的成员很多很复杂，实例化时比较慢，则定义一个私有类来持有当前类的实例
    
### 架构师是什么？

首先是一个 JAVA 高级开发人员

#### 给架构师下个定义

##### 架构设计、软件开发
    
    确认需求
        保证架构师完整准确的理解用户的需求
    系统分解
        系统是否需要分层，如何分层，分层确定以后，确定各层的接口
    技术选型
        适用于当前的业务要求和团队成员的水平，保留适当的前瞻(半年的)
    指定技术规格说明
        体现架构意图，要求团队开发人员按照什么样的架构开发
    
##### 开发管理

    深深接入开发的方方面面
    
##### 沟通协调

    与用户、产品、上级、团队成员
    
#### 作用

    设计架构
    救火：解决问题
    布道：培训新技术

#### 效果

    公关
    信念

#### 职责

    产品架构
    基础服务架构
