## 什么是AQS？
### 一、AQS是什么
AQS 全称：AbstractQueuedSynchronizer，抽象队列同步器
是java.util.concurrent.locks 包下的抽象类，JUC并发工具底层核心基础框架。
ReentrantLock、CountDownLatch、Semaphore、CyclicBarrier、ReentrantReadWriteLock全部底层基于AQS实现。
作用：封装了同步等待队列、线程阻塞/唤醒、同步状态管理、CAS操作，开发者只需要简单实现少量方法，就能快速实现锁、同步工具，不用重复写线程排队逻辑。

### 二、AQS 三大核心成员
1. 同步状态变量state（核心标记）
   `private volatile int state;` volatile修饰，保证多线程可见；
   含义由子类自定义：
   ReentrantLock：state=0 无锁；state>0 代表锁被持有，数值 = 重入次数；
   CountDownLatch：state = 计数器初始值，countDown() 让state-1；
   Semaphore：state = 剩余可用信号量。
   修改state必须通过CAS保证原子性：compareAndSetState()
2. 双向阻塞等待队列（CLH 队列变种）
   当线程抢锁失败，不会无限自旋，会封装成Node节点，放入FIFO双向阻塞队列排队。
   Node存储：线程、等待状态、前驱节点、后继节点。 队列是双向链表，有head头节点、tail尾节点。
3. 内部Node节点（排队线程封装）
   存储等待线程，还有等待状态 waitStatus：
   SIGNAL：后继线程需要被唤醒
   CONDITION：线程在 Condition 条件队列等待
   CANCELLED：线程超时 / 中断，取消等待
   多个状态控制线程阻塞、唤醒、取消。

### 三、两大核心模式（AQS 两种资源竞争逻辑）
1. 独占模式（Exclusive）
   同一时间只允许一个线程持有资源 代表：ReentrantLock、WriteLock 写锁
   同一时刻只有一个线程能修改state，其他线程进队列阻塞。
2. 共享模式（Shared）
   多线程可同时获取资源，支持并发 代表：Semaphore、CountDownLatch、ReadLock读锁
   多个线程同时获取，state剩余资源数减少，state=0 后新线程阻塞。

### 四、核心流程（独占锁举例，ReentrantLock）
线程调用lock()，调用AQS acquire()； 尝试tryAcquire()：CAS修改state，抢占锁；
成功：直接执行业务代码； 失败：把当前线程封装为Node，加入队尾；
队列内线程循环自旋尝试抢锁，多次失败后调用LockSupport.park() 阻塞线程（释放 CPU）；

持有锁的线程unlock()，调用release()，修改state；
state=0 释放成功，唤醒队列head后继阻塞线程； 被唤醒线程重新竞争锁，循环往复。

### 五、Condition 条件队列（配套功能）
AQS同步队列是阻塞抢锁队列； 每个Condition拥有独立单向条件队列。
调用await()：释放锁，线程移入条件队列；
调用signal()：将条件队列节点移回AQS同步队列，重新竞争锁。
对应ReentrantLock的多条件精准唤醒能力。

### 六、面试精简背诵版
AQS是抽象队列同步器，JUC所有锁、同步工具底层基础。
内部核心：volatile修饰的同步状态state、CLH双向阻塞队列、Node节点。
分为独占、共享两种模式；通过CAS修改state保证原子性，抢锁失败线程进入队列，由LockSupport阻塞/唤醒线程。
子类只需实现tryAcquire/tryRelease（独占）或 tryAcquireShared/tryReleaseShared（共享），快速实现同步器；
同时支持Condition条件队列实现精准等待唤醒。
### 高频补充考点
AQS阻塞线程依靠LockSupport.park/unpark，底层调用操作系统内核阻塞；
AQS队列是FIFO，配合ReentrantLock可实现公平锁；非公平锁线程可以插队抢锁；
state使用volatile保证可见性，CAS保证原子修改；
synchronized依靠Monitor，AQS纯Java代码 + CAS 实现，不依赖操作系统监视器。

------------------------------------------------------------------------------
## ReentrantLock实现原理？
1. 底层基础
   核心基于AQS（AbstractQueuedSynchronizer）实现，纯Java代码，依靠CAS + CLH双向阻塞队列 + LockSupport完成锁竞争、线程阻塞唤醒。
   内部分为两个内部类：NonfairSync（默认非公平锁）、FairSync（公平锁），都继承 AQS。
2. 核心变量AQS state
   volatile int state：同步状态，代表锁重入次数
   state=0：无线程持有锁 state>0：锁被占用，数值 = 重入次数，实现可重入
   当前持有锁线程由exclusiveOwnerThread记录。
3. 加锁流程（以非公平锁lock()举例）
   线程上来先CAS尝试把state从0改为1，成功则直接持有锁；
   CAS失败：调用acquire(1)，执行tryAcquire再次尝试抢占；
   抢占失败：封装当前线程为AQS的Node节点，加入双向阻塞队列尾部；
   队列内线程自旋循环尝试抢锁，多次失败调用LockSupport.park()阻塞线程；
   公平锁区别：抢锁前会先判断队列是否有等待线程，有则直接排队，不插队。
4. 解锁流程unlock()
   调用release(1)，执行tryRelease，state减1；
   重入场景：多次加锁需要多次unlock，state归零才算完全释放；
   state归0释放锁成功，清空持有线程；
   唤醒队列头部后继阻塞线程（LockSupport.unpark），被唤醒线程重新竞争锁。
5. 可重入实现
   同一线程重复获取锁，tryAcquire判断当前线程等于持有线程，state直接累加，不会阻塞；解锁时逐层递减state，必须全部释放锁，其他线程才能抢占。
6. Condition等待唤醒原理
   每个Condition拥有独立单向条件队列：
   await()：释放当前锁，线程进入条件队列等待；
   signal()：将条件队列节点转移到AQS同步队列，重新排队竞争锁；
   可创建多个Condition，实现多条件精准唤醒，这是synchronized不具备的能力。
7. 关键配套能力底层实现
   tryLock()：无阻塞尝试CAS抢锁，不进队列；
   tryLock(超时)：限时自旋抢锁，超时自动放弃；
   lockInterruptibly()：阻塞等待时响应中断，抛出异常。
### 极简口述版
ReentrantLock底层基于AQS 同步队列器，分公平锁、非公平锁两种实现；
依靠volatile state记录锁重入次数，CAS原子修改同步状态；
抢锁失败线程封装为Node进入CLH双向队列，通过LockSupport阻塞唤醒线程；
支持锁可重入，提供tryLock、可中断加锁能力；
搭配 Condition实现多条件独立等待队列，精准唤醒线程。
------------------------------------------------------------------------------
## 什么是ReadWriteLock？
ReentrantLock某些时候有局限，如果使用ReentrantLock，可能本身是为了防止线程A在写数据，线程B在读数据造成的数据不一致，
但这样，如果线程C在读数据、线程D也在读数据，读数据是不会改变数据的，没有必要加锁，但是还是加锁了，降低了程序的性能。
因为这个，才诞生了读写锁ReadWriteLock。ReadWriteLock是一个读写锁接口，ReentrantReadWriteLock是ReadWriteLock接口的一个具体实现，
实现了读写的分离，读锁是共享的，写锁是独占的，读和读之间不会互斥，读和写、写和读、写和写之间才会互斥，提升了读写的性能
------------------------------------------------------------------------------
## 什么是CountDownLatch？
### 一、定义
CountDownLatch是JUC下的同步工具类，线程等待计数器，底层基于AQS共享模式实现。
作用：让一组线程全部执行完成后，主线程再继续执行；也可实现一个线程等待多个其他线程完成任务。
### 二、核心原理
构造方法传入初始计数器count，对应AQS的state； countDown()：计数器state-1，共享模式释放同步状态；
await()：主线程阻塞等待，直到state减为0才唤醒放行； 计数器只能递减，不能重置，用完即废，无法重复使用。
### 三、核心常用方法
await()：阻塞当前线程，直到计数器归0；
await(long time, TimeUnit unit)：限时等待，超时直接放行；
countDown()：计数器减一，线程执行完毕调用；
getCount()：获取当前剩余计数。
### 四、代码示例
```
// 初始计数器3
CountDownLatch latch = new CountDownLatch(3);
for (int i = 0; i < 3; i++) {
    new Thread(() -> {
        try {
            // 执行业务
        } finally {
            latch.countDown(); // 任务完成，计数-1
        }
    }).start();
}
latch.await(); // 阻塞，等3个线程全部执行完再往下走
System.out.println("所有任务执行完毕");
```
### 极简口述背诵版
CountDownLatch是JUC同步工具，底层AQS共享模式实现，内部维护计数器。
构造指定初始计数，线程调用countDown让计数器递减，主线程调用await阻塞等待，计数器归0后唤醒主线程。
计数器只能递减、不能重置，一次性使用；常用于多线程并行任务，主线程等待全部子线程完成后汇总处理。
------------------------------------------------------------------------------
什么是 CyclicBarrier？
### 一、基础定义
CyclicBarrier循环屏障，JUC同步工具，底层不基于AQS，依靠ReentrantLock + Condition实现。
作用：让一批线程互相等待，所有线程全部抵达屏障点，才统一解除阻塞、继续往下执行；计数器可以自动重置，能够循环反复使用，Cyclic = 循环。
### 二、核心机制
构造传入参数parties：需要等待的线程总数量；
所有执行barrier.await() 的线程阻塞；
凑齐规定数量线程，屏障打开，全部线程同时放行；计数器自动复原，可再次新一轮等待；
支持传入屏障回调任务：所有线程到位后，优先由其中一个线程执行回调Runnable，再放行所有线程。
### 三、常用 API
await()：线程到达屏障，阻塞等待所有人集齐；可被中断
await(long timeout, TimeUnit unit)：限时等待，超时直接抛出异常
reset()：手动重置屏障，中途打断当前等待，开启新一轮
getNumberWaiting ()：当前正在等待的线程数
### 四、示例代码
```
CyclicBarrier barrier = new CyclicBarrier(3);
for (int i = 0; i < 3; i++) {
    new Thread(() -> {
        try {
            System.out.println(Thread.currentThread().getName() + "执行完成，等待其他线程");
            barrier.await();
            System.out.println(Thread.currentThread().getName() + "全员到齐，继续执行");
        } catch (Exception e) {}
    }).start();
}
```
### 五、极简口述版
CyclicBarrier是可循环使用的线程屏障，依靠锁+条件队列实现。
设定需要凑齐的线程数，每个线程调用await阻塞，全部线程抵达屏障后集体唤醒，计数器自动重置支持重复使用，还能配置集齐后执行回调。
多用于多线程分阶段协同计算、统一并发发起；和CountDownLatch最大区别是可循环，且是线程互相等待，而非单向等待。
### 高频坑点补充
如果等待中途有线程超时、中断，屏障会被损坏，其他等待线程全部抛出BrokenBarrierException；调用reset可修复。
------------------------------------------------------------------------------
## CountDownLatch和CyclicBarrier区别？

CyclicBarrier|CountDownLatch
--|--
CyclicBarrier是可重用的，其中的线程会等待所有的线程完成任务。届时，屏障将被拆除，并可以选择性地做一些回调动作。| CountDownLatch是一次性的，不同的线程在同一个计数器上工作，直到计数器为0.
CyclicBarrier面向的是线程数|CountDownLatch面向的是任务数
在使用CyclicBarrier时，你必须在构造中指定参与协作的线程数，这些线程必须调用await()方法|使用CountDownLatch时，则必须要指定任务数，至于这些任务由哪些线程完成无关紧要
CyclicBarrier可以在所有的线程释放后重新使用|CountDownLatch在计数器为0时不能再使用
在CyclicBarrier中，如果某个线程遇到了中断、超时等问题时，则处于await的线程都会出现问题|在CountDownLatch中，如果某个线程出现问题，其他线程不受影响
------------------------------------------------------------------------------
## 什么是Semaphore？
1. 定义
   Semaphore，信号量，JUC同步工具，基于AQS共享模式实现。
   核心作用：控制同一时间并发访问资源的最大线程数量，用来限流。
2. 核心参数与原理
   构造传入permits（许可数量），对应AQS的state值；
   acquire()：申请1个许可，state-1。无许可则线程阻塞；
   release()：归还1个许可，state+1，唤醒等待线程；
   支持公平、非公平两种模式，默认非公平。
3. 关键特点
   共享模式，多线程可同时拿到许可并发执行； 许可可以由任意线程释放，不要求获取和释放是同一个线程；
4. 常用API
   acquire()：阻塞拿许可，拿不到一直等 acquire(int n)：一次性拿 n 个许可
   tryAcquire()：非阻塞尝试获取，失败直接返回false tryAcquire(超时,时间单位)：限时等待
   release()：归还1个许可 release(int n)：一次性归还多个许可
5. 典型使用场景
   接口限流：限制单机同时最多20个请求访问数据库/第三方接口，防止打垮下游；
   连接池管控：文件连接、硬件资源、少量设备，限制并发使用数；
   分布式限流一般用Redis，Semaphore只做单机本地限流。
6. 极简示例代码
```
Semaphore semaphore = new Semaphore(3);
for (int i = 0; i < 6; i++) {
    new Thread(() -> {
        try {
            semaphore.acquire();
            System.out.println(Thread.currentThread().getName() + "获取许可，执行业务");
            Thread.sleep(1000);
        finally {
            semaphore.release();
            System.out.println(Thread.currentThread().getName() + "归还许可");
        }
    }).start();
}
```
6个线程抢3个许可，同一时刻最多3个线程运行。
7. 高频面试区分
   Semaphore：控制并发线程上限，限流工具，允许多线程同时运行；
   synchronized/ReentrantLock：同一时间只允许1个线程执行，排他。
8. 口述背诵精简版
   Semaphore是基于AQS共享模式的信号量，用来做单机并发限流。
   初始化指定许可数，acquire获取许可、release归还；许可充足线程直接运行，耗尽则阻塞。支持公平/非公平，不限定归还线程；
   多用于限制访问资源的最大并发数，实现本地限流。
9. 坑点提醒
   不要随意在别的线程release，会造成许可数量越变越多，限流失效；规范写法：谁acquire谁release。
------------------------------------------------------------------------------
## 什么是Exchanger？
### 一、基础概念
Exchanger是JUC并发工具，作用：两个线程成对交换数据。 一个线程调用exchange()阻塞等待，直到另一条线程也调用exchange；
双方互相把自己携带的数据传给对方，然后同时唤醒继续执行。 只支持两两配对，无法多个线程互换。底层依靠Lock+Condition 实现。
### 二、核心原理
线程A带着数据执行exchange(dataA)，阻塞挂起； 线程B带着数据执行exchange(dataB)；
两者匹配成功：A拿到dataB，B拿到dataA；双双解除阻塞； 多个线程争抢时，会形成等待队列，按顺序两两配对。
### 三、常用方法
V exchange(V x)：携带数据阻塞，直到配对完成交换，可被中断
exchange(V x, long timeout, TimeUnit unit)：限时等待，超时抛出异常
### 四、极简代码示例
```
Exchanger<String> exchanger = new Exchanger<>();
// 线程A
new Thread(() -> {
    try {
        String msg = exchanger.exchange("我是线程A的数据");
        System.out.println("线程A收到：" + msg);
    } catch (Exception e) {}
}).start();

// 线程B
new Thread(() -> {
    try {
        String msg = exchanger.exchange("我是线程B的数据");
        System.out.println("线程B收到：" + msg);
    } catch (Exception e) {}
}).start();
输出：
线程A收到：我是线程B的数据
线程B收到：我是线程A的数据
```
### 五、适用场景
两个线程双向数据传输、缓冲区互换； 生产者消费者双缓冲区交替读写（一个缓冲区读，另一个写，交换切换）； 两个独立线程需要互相传递参数。
### 六、特点与缺点
只能一对一交换，不能三个及以上线程互传； 必须凑一对，单独一个线程调用exchange会一直阻塞；
没有公平策略，先到先配对； 线程中断、超时会终止等待。
### 七、口语精简背诵版
Exchanger用于两个线程配对交换数据。线程调用exchange携带数据阻塞，等到另一个线程也调用方法，双方互换数据后继续运行。
多用于双线程双向传参、双缓存交替，只支持两两配对，单独线程会阻塞。
------------------------------------------------------------------------------
