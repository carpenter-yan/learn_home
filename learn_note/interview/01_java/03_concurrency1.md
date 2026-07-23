
## 并行与并发的区别？
从操作系统的角度来看，线程是CPU分配的最小单位。
1、并行就是同一时刻，两个线程都在执行。要求有两个CPU去分别执行两个线程。
2、并发就是同一时刻，只有一个执行，但是一个时间段内，两个线程都执行了。
并发的实现依赖于CPU切换线程，因为切换的时间特别短，所以基本对于用户是无感知的。
------------------------------------------------------------------------------
## 什么是进程？线程？
进程是资源分配最小单位，线程是CPU调度最小单位；线程寄生在进程内部，共享进程资源，切换开销更小。
------------------------------------------------------------------------------
## 线程有几种创建方式？
1. 继承 Thread 类
   自定义类继承 Thread，重写 run() new 对象，调用 start() 启动
```
   class MyThread extends Thread{
       @Override
       public void run() {
       // 任务逻辑
       }
   }
   new MyThread().start();
```
缺点：Java 单继承，占用继承名额，扩展性差。

2. 实现 Runnable 接口（推荐）
   实现 Runnable，重写 run() 传入 Thread 构造器，调用 start ()
```
   class Task implements Runnable{
       @Override
       public void run() {}
   }
   new Thread(new Task()).start();
```
   优点： 只是实现接口，不占用继承； 任务与线程解耦，可复用任务对象。

3. 实现 Callable + FutureTask（有返回值、可抛异常）
   Runnable 无返回、不能抛出受检异常；Callable 解决这个问题：
   实现 Callable<V>，重写 call()，有返回值 包装到 FutureTask，丢给 Thread futureTask.get() 获取返回结果（阻塞）
```
   Callable<Integer> call = () -> 100;
   FutureTask<Integer> task = new FutureTask<>(call);
   new Thread(task).start();
   Integer res = task.get();
```
4. 线程池 ExecutorService（生产环境主流）
   手动 new Thread 无法管控线程、频繁创建销毁开销大，项目统一用线程池：
```
   ExecutorService pool = Executors.newFixedThreadPool(3);
   // 提交 Runnable
   pool.submit(new RunnableTask());
   // 提交 Callable
   pool.submit(new CallableTask());
```
   底层本质还是前两种，只是统一管理复用线程。

### 面试总结一句话
   四种创建方式：继承Thread类；实现Runnable接口；Callable+FutureTask 带返回值；通过线程池Executor框架创建（工作中最常用）。
------------------------------------------------------------------------------
## Runnable和Callable区别？
Runnable 重写的方法是run(), 无返回、不能抛出受检异常，适合无结果异步任务；
Callable 重写的方法是call(), 带泛型返回值、可抛异常，配合Future能获取执行结果、取消任务，适合需要拿到线程计算结果的场景。
------------------------------------------------------------------------------
## 为什么调用start()方法时会执行run()方法，那怎么不直接调用run()方法
JVM执行start方法，会先创建一条线程，由创建出来的新线程去执行thread的run方法，这才起到多线程的效果。 
如果直接调用Thread的run()方法，那么run方法还是运行在主线程中，相当于顺序执行，就起不到多线程的效果。
------------------------------------------------------------------------------
## 线程常用调度相关方法?
### 一、Thread 自身方法
1. sleep(long ms)
   作用：当前线程主动休眠指定毫秒，暂停执行
   特点： 休眠不释放持有的同步锁； 时间结束后回到就绪队列，等待CPU调度； 抛出受检异常InterruptedException。
2. yield() 礼让 
   作用：主动让出CPU执行权，从运行态回到就绪态
   特点：仅给操作系统调度器一个建议，不一定真切换；不释放锁；优先级相同线程才有机会抢到CPU。
3. join()/join (long timeout) 线程插队
   t.join()：当前线程阻塞，等待线程t执行完毕才继续运行。 t.join(1000)：最多等待1秒，超时自动放行
   底层基于wait实现，等待期间释放锁。
4. setPriority(int)/getPriority()线程优先级
   优先级区间 1~10： MIN_PRIORITY=1（最低） NORM_PRIORITY=5（默认） MAX_PRIORITY=10（最高）
   仅为OS调度建议，不保证严格生效，不同系统实现有差异。
5. interrupt()、isInterrupted()、interrupted() 中断标记
   interrupt()：给线程设置中断标志，不是直接停止线程；
   线程处于sleep/wait/join阻塞时，会立刻抛中断异常清除标记；
   isInterrupted()：实例方法，获取中断标记，不清除；
   interrupted()：静态方法，获取标记并清除标记。
### 二、Object 监视器等待/唤醒方法（必须在 synchronized块中调用）
1. wait()/wait(long ms)
   当前线程释放锁，进入该对象等待池阻塞；无参wait无限等待，带参数限时等待；
   被notify/notifyAll唤醒后需要重新竞争锁才能执行。
2. notify()
   随机唤醒一个在此对象等待池阻塞的线程。
3. notifyAll()
   唤醒所有在此对象等待池阻塞的线程，所有线程重新竞争锁。
### 三、高频对比考点：sleep vs wait
   归属：sleep是Thread 方法；wait是Object方法
   锁：sleep不释放锁；wait释放锁
   使用位置：sleep任意地方；wait必须在同步代码块
   唤醒：sleep时间到自动恢复；wait需要notify唤醒
### 面试口述精简版
   常用调度方法分两类：
   Thread类：sleep休眠、yield礼让 CPU、join等待其他线程、setPriority设置优先级、interrupt中断；
   Object同步方法：wait阻塞等待、notify唤醒单个线程、notifyAll唤醒全部等待线程；
   其中sleep不释放锁，wait会释放锁是高频区分点。
------------------------------------------------------------------------------
## 线程有几种状态？
### 一、六种状态
1. NEW新建：线程对象 new 出来，但还没调用 start()。 没有分配操作系统内核线程，仅 Java 对象。
2. RUNNABLE就绪/运行：调用start()后进入此状态，包含两种细分：
    就绪：等待CPU时间片；
    运行：正在CPU上执行。 
    Java统一合并为RUNNABLE，不分两个状态。
3. BLOCKED 阻塞（同步锁阻塞）： 线程想去执行同步代码块synchronized，但监视器锁被其他线程持有，排队抢锁时进入BLOCKED。 只有抢锁才会进入这个状态。
4. WAITING 无限等待： 无时限阻塞，必须被其他线程主动唤醒，进入该状态的调用：
    Object.wait() 无参   Thread.join() 无参   LockSupport.park()  
    唤醒：notify()/notifyAll()/目标线程执行完/unpark()
5. TIMED_WAITING 限时等待：带超时时间的等待，时间到自动苏醒，方法：
    Thread.sleep(ms) Object.wait(ms) Thread.join(ms) LockSupport.parkNanos() / parkUntil()
6. TERMINATED 终止：run()方法执行完毕，或中途异常退出，线程生命周期结束，不可再次 start()。
### 二、简易状态流转图
NEW → start () → RUNNABLE
RUNNABLE几种去向：
抢不到synchronized锁 → BLOCKED；抢到锁回到RUNNABLE
调用wait()/join() → WAITING；唤醒后去竞争锁，BLOCKED→RUNNABLE
调用sleep/带参wait → TIMED_WAITING；超时/唤醒回到 RUNNABLE
run执行完/异常 → TERMINATED
### 三、高频区分考点
BLOCKED和WAITING不一样：
BLOCKED：等锁； WAITING/TIMED_WAITING：主动放弃锁，等别人唤醒/等时间。
yield()不会切换出RUNNABLE，只是让出CPU，状态不变。
调用interrupt()不会直接改变状态，只是打中断标记； 若线程在WAITING/TIMED_WAITING，会抛异常回到 RUNNABLE。
### 面试一句话背诵
Java 线程一共6种状态：NEW 新建、RUNNABLE可运行、BLOCKED同步阻塞、WAITING无限等待、TIMED_WAITING限时等待、TERMINATED终止。

------------------------------------------------------------------------------
## 什么是守护线程？
Java中的线程分为两类，分别为daemon线程（守护线程）和user线程（用户线程）。
在JVM启动时会调用main函数，main函数所在的线程就是一个用户线程。其实在JVM内部同时还启动了很多守护线程， 如：垃圾回收线程。
那么守护线程和用户线程有什么区别呢？区别之一是当最后一个非守护线程束时，JVM会正常退出，而不管当前是否存在守护线程，也就是说守护线程是否结束并不影响JVM退出。
------------------------------------------------------------------------------
## 线程间有哪些通信方式？
1. 共享变量（volatile可见性）
   最简单的通信，多个线程共享同一成员变量，volatile保证可见性、禁止重排序。
   适用：简单标记控制启停。
```
   volatile boolean flag = true;
   // 线程1
   while(flag){}
   // 线程2 修改flag=false，通知线程1退出
```
缺点：只能传递布尔标记，无法精准通知、无等待唤醒机制。

2. synchronized+wait()/notify()/notifyAll()
   基础等待通知模型，Object 监视器方法。
   同步锁内wait()：释放锁，进入等待； 其他线程 notify()唤醒一个/notifyAll() 唤醒全部；唤醒后重新竞争锁再执行。
   适用：生产者消费者、任务等待。
3. Lock锁 + Condition（替代 wait/notify）
   ReentrantLock搭配Condition，功能更强：
   一把锁可创建多个Condition，精准分组唤醒（而notify随机唤醒）；await()等价wait，signal()/signalAll()等价notify。
   生产者消费者首选，解耦等待队列。
4. JUC同步工具类
   - (1) CountDownLatch
   计数器倒减，主线程await()等待所有子线程完成，子线程 countDown() 计数 - 1。
   场景：多任务汇总，等待一批线程全部执行完再往下走。
   - (2) CyclicBarrier
   循环屏障，固定数量线程全部到达屏障点才一起放行；可重复使用。
   场景：多线程分段计算，到齐后合并结果。
   - (3) Semaphore信号量
   控制并发线程数量，限流通信。
   场景：连接池、接口限流。
5. 管道流PipedInputStream/PipedOutputStream
   线程间字节/字符直接传输数据，一个线程写管道，另一个读管道。
   适合：线程间传递数据流，业务极少使用。
6. 线程池、Callable + Future
   子线程计算结果返回给主线程，带返回值通信。Future.get() 阻塞获取子线程执行结果，实现结果传递。
### 分类总结（面试背诵）
   共享内存：volatile共享变量、synchronized wait/notify、Lock Condition
   同步工具：CountDownLatch、CyclicBarrier、Semaphore
   数据传递：管道流
   返回结果通信：Callable + Future
------------------------------------------------------------------------------
## 原子性、可见性、有序性的理解？
原子性、有序性、可见性是并发编程中非常重要的基础概念，JMM的很多技术都是围绕着这三大特性展开。
1. 原子性指的是一个操作是不可分割、不可中断的，要么全部执行并且执行的过程不会被任何因素打断，要么就全都不执行。
2. 可见性指的是一个线程修改了某一个共享变量的值时，其它线程能够立即知道这个修改。
3. 有序性指的是对于一个线程的执行代码，从前往后依次执行，单线程下可以认为程序是有序的，但是并发时有可能会发生指令重排。
------------------------------------------------------------------------------
## 什么是指令重排？
在执行程序时，为了提高性能，编译器和处理器常常会对指令做重排序。重排序分3种类型。
1. 编译器优化的重排序。编译器在不改变单线程程序语义的前提下，可以重新安排语句的执行顺序。
2. 指令级并行的重排序。现代处理器采用了指令级并行技术（Instruction-Level Parallelism，ILP）来将多条指令重叠执行。
   如果不存在数据依赖性，处理器可以改变语句对应 机器指令的执行顺序。
3. 内存系统的重排序。由于处理器使用缓存和读/写缓冲区，这使得加载和存储操作看上去可能是在乱序执行。
------------------------------------------------------------------------------
## 什么是 happens-before，它有什么作用？
### 一、happens-before 是什么（标准定义 + 通俗解释）
1. 官方两层定义
   1）如果操作A happens-before操作B：A的执行结果对B可见，并且语义上A优先执行。
   2）存在happens-before关系≠CPU指令必须物理顺序执行。
     只要重排序后的运行结果和顺序执行结果一致，JVM、CPU允许重排序优化；一旦重排序会改变运行结果，就是非法重排，必须禁止。
2. 大白话
   它是JMM（Java 内存模型）制定的一套「可见性、有序性」契约规则，和CPU、缓存、内存屏障这些底层硬件做了一层抽象隔离，不靠物理时序管控代码，靠规则约束多线程的数据行为。
   包含6条原生内置规则：程序顺序、锁、volatile、传递性、start、join 规则。
### 二、happens-before 的核心作用
   1. 平衡性能与并发安全（JVM 底层核心目的）
   编译器、CPU、内存系统都会做重排序提速程序。
   happens-before充当一把标尺：
   不改写程序结果的重排 → 放行，保留硬件优化，提升执行效率；
   会造成多线程错乱的重排 → 判定非法，JVM自动插入内存屏障阻止重排。
   既不让程序出错，又不会一刀切关闭所有CPU优化。
   2. 统一所有同步机制的底层原理，打通知识体系
   synchronized、volatile、Lock、join、start、JUC工具类的可见性、有序性全部依托happens-before实现。
   不用孤立死记各个关键字特性，一套规则就能解释所有同步行为。
   3. 屏蔽底层硬件差异，实现Java跨平台一致性
   不同CPU架构的缓存、重排序行为不一样。Java依靠happens-before统一规范，我们编写的并发代码在Windows、Linux、各类CPU 上行为一致，不用适配底层硬件细节。
   4. 指导日常开发，合理编写代码、精简同步逻辑
   依靠内置规则，很多场景自带可见性：主线程start()前赋值变量、join()后读取子线程数据、锁解锁后其他线程加锁读取数据，天然满足可见性，无需无脑加volatile、加锁，减少性能损耗。
   5. 排查并发疑难 BUG
   读到旧值、标识变量无法及时感知、DCL半初始化对象等偶现问题，本质就是读写共享变量之间没有建立happens-before关系。对照规则就能精准补上同步方案解决问题。
### 面试一句话背诵版
   happens-before是JMM定义的先行发生契约，规定了操作之间的可见性与语义先后关系；
   作用是划分合法与非法重排序、兼顾运行性能与并发正确性，屏蔽底层硬件差异，统一各类同步机制的底层原理，
   同时指导我们正确使用同步 API、排查多线程隐形 bug。
------------------------------------------------------------------------------
## happens-before六大规则？
1. 程序次序规则
   同一个线程内，书写在前面的代码happens-before该线程内所有后面的操作。
   通俗：单线程无论怎么指令重排，最终执行效果和代码从上到下执行结果一致，单线程永远安全。
2. 监视器锁（synchronized）规则
   对同一个锁的解锁操作，happens-before后续任意一次对该锁的加锁操作。
   通俗：线程释放锁时刷新所有共享变量，后面拿到锁的线程一定能读取到最新数据。
3. volatile变量规则
   对volatile变量的写操作，happens-before之后任意一次对该volatile变量的读操作。
   通俗：volatile写入的数据，其他线程立刻可见，同时禁止上下代码跨volatile重排。
4. 传递性规则
   如果A happens-before B，B happens-before C，那么A happens-before C。
   链式传递，多个操作可以层层传递可见性。
5. Thread.start()规则
   主线程调用thread.start()这个操作，happens-before被启动线程内部的全部代码。
   通俗：start方法执行之前主线程修改的变量，子线程一定可以读到最新值。
6. Thread.join()规则
   主线程等待thread.join()执行完毕返回，被等待线程里的所有操作，全部happens-before join方法之后主线程的代码。
   通俗：join结束后，子线程所有的数据修改，主线程都能拿到最新结果。
------------------------------------------------------------------------------
## volatile实现原理？
1. 一、两大核心能力
保证共享变量的可见性；禁止指令重排序，保障有序性；volatile不保证原子性。
2. 二、可见性实现原理
基于CPU缓存一致性协议（MESI），配合写内存屏障、读内存屏障
写volatile变量：线程修改完毕，强制把自身CPU缓存中的数据立刻刷新写入主内存；
读volatile变量：直接失效当前CPU本地缓存，强制从主内存加载最新数据。
其它线程读取该变量时，缓存行失效，必须去主存拉取最新值，解决了CPU缓存带来的数据不一致问题。
3. 三、有序性实现原理：内存屏障（Memory Barrier）
编译阶段，JVM会在 volatile修饰变量的读写位置自动插入对应内存屏障，拦截非法指令重排：
StoreLoad屏障（volatile写之后） volatile写入操作后面插入StoreLoad屏障。 禁止：volatile写，和它之后的读写指令发生重排序。
StoreStore屏障（volatile写之前） 普通写操作 ↔ volatile 写禁止重排。
LoadLoad屏障（volatile读之前） volatile读 ↔ 前方普通读禁止重排。
LoadStore屏障（volatile 读之后）volatile 读 ↔ 后方普通写禁止重排。
精简总结屏障规则：
写volatile：前后指令不能跑到 volatile 写的后面
读volatile：前后指令不能跑到 volatile 读的前面
4. 四、字节码层面佐证
被volatile修饰的变量，class字节码文件中会多出一个ACC_VOLATILE标识。JVM识别该标识，编译生成机器码时插入上述四层内存屏障。
5. 五、硬件层面
内存屏障本质就是CPU指令，CPU收到屏障指令，必须等待屏障之前所有读写操作执行完成，才能执行屏障之后的指令，从硬件层面锁住执行顺序。
6. 六、经典应用场景
状态标记位（线程启停标识） DCL 双重检查锁单例（防止对象初始化重排，拿到半初始化对象） 读写分离、保证多线程感知数据刷新
### 面试一句话背诵
volatile通过字节码ACC_VOLATILE标记，底层插入四类内存屏障；依靠内存屏障禁止指令重排实现有序性，
结合MESI缓存协议、强制刷新主存 + 失效本地缓存保证可见性；无法保证复合操作的原子性。
补充：为什么i++这种操作volatile没用
i++分为：读取值→运算+1→写回主存三步，三步不是原子操作，volatile只能保证每次读取最新值，无法锁住三步整体，高并发下依旧会出现数据错乱。
------------------------------------------------------------------------------
## synchronized如何使用？
synchronized是Java内置互斥锁，能同时保证：原子性、可见性、有序性。 
锁本质：对象监视器（monitor），任何锁都必须绑定一个对象。
一、四种写法（两大类：对象锁、类锁）
1. 实例方法上锁【对象锁】 锁住当前实例this，不同实例互不阻塞
```
// 锁：this
public synchronized void test(){
}
```
2. 同步代码块 (this)【对象锁】 粒度更小，推荐优先使用
```
public void test(){
    synchronized (this) {
    
    }
}
```
   1、2 属于对象锁：同一个对象多线程竞争互斥；不同对象，互不影响。
3. 静态同步方法【类锁】
   锁的是类名.class对象
```
// 锁：Test.class
public static synchronized void test(){
}
```
4. 代码块锁Class对象【类锁】
```
public static void test(){
    synchronized (Test.class) {
    }
}
```
   3、4 属于类锁：所有实例共用同一把锁，全局互斥。
⚠️重要区分
对象锁（this）和类锁（XXX.class）互不阻塞！ 一个线程拿对象锁，另一个拿类锁，可以同时进入同步代码，不会互斥。
------------------------------------------------------------------------------
## synchronized底层原理？
synchronized 是Java内置监视器锁。编译后，同步代码块前后会生成 monitorenter 和 monitorexit 字节码指令；
每个Java对象的对象头都可以关联一个Monitor监视器对象，这也是任意对象都可以作为synchronized锁的根本原因。

Monitor内部维护锁计数器、持有者线程，还有两个队列 entryList 和 waitSet：
1. 多个线程竞争锁时，未获取到锁的线程进入 entryList；
2. 线程获取Monitor，锁计数器+1，支持可重入，多次获取计数器持续累加；
3. 如果线程调用wait()，释放锁，计数器递减，线程移入 waitSet 等待唤醒；
4. 执行notify/notifyAll后，waitSet中的线程移出，进入entryList重新竞争锁；
5. 线程正常执行完毕执行monitorexit，计数器-1；计数器归0代表锁完全释放。

注意：上述带有entryList、waitSet、依赖操作系统互斥锁的模型，是synchronized膨胀到【重量级锁】时才具备的机制。
JDK1.6对synchronized做了优化，引入锁升级机制：偏向锁 → 轻量级锁 → 重量级锁。
偏向锁、轻量级锁依靠CAS操作实现，没有操作系统Monitor、没有阻塞队列；
只有竞争激烈时，才会膨胀为重量级锁，使用操作系统内核互斥锁。
------------------------------------------------------------------------------
## synchronized锁升级过程？
在Java1.6之前的版本中，synchronized属于重量级锁，效率低下，锁是cpu一个重量级的资源，每次获取锁都要和cpu申请，非常消耗性能。
Jdk1.6之后，为了减少获得锁和释放锁所带来的性能消耗，引入了偏向锁和轻量级锁，增加了锁升级的过程，无锁->偏向锁->自旋锁->重量级锁

### synchronized 锁升级原理：
在锁对象的对象头里面有一个threadid 字段，在第一次访问的时候threadid 为空，jvm让其持有偏向锁，并将 threadid设置为其线程 id。
再次进入的时候会先判断threadid是否与其线程id一致，如果一致则可以直接使用此对象，如果不一致，则升级偏向锁为轻量级锁，通过自旋循环一定次数来获取锁，
执行一定次数之后，如果还没有正常获取到要使用的对象，此时就会把锁从轻量级升级为重量级锁，此过程就构成了 synchronized 锁的升级。

### 锁的升级的目的：
锁升级是为了减低了锁带来的性能消耗。在Java 6之后优化synchronized的实现方式，使用了偏向锁升级为轻量级锁再升级到重量级锁的方式，从而减低了锁带来的性能消耗。
------------------------------------------------------------------------------
## synchronized优化有哪些？
Java的开发团队一直在对synchronized优化，其中最大的一次优化就是在jdk6的时候，新增了两个锁状态，通过锁消除、锁粗化、自旋锁等方法使用各种场景，给synchronized性能带来了很大的提升。

1. 锁膨胀 上面讲到锁有四种状态，会因实际情况进行膨胀升级，其膨胀方向是：无锁——>偏向锁——>轻量级锁——>重量级锁，并且膨胀方向不可逆。
- 1.1 偏向锁 一句话总结它的作用：减少单一线程获取锁的代价。在大多数情况下，锁不存在多线程竞争，总是由同一线程多次获得，那么此时就是偏向锁。 
      核心思想：如果一个线程获得了锁，那么锁就进入偏向模式，此时**Mark Word**的结构也就变为偏向锁结构，
      当该线程再次请求锁时，无需再做任何同步操作，即获取锁的过程只需要检查**Mark Word**的锁标记位为偏向锁
      以及当前线程ID等于**Mark Word**的ThreadID即可，这样就省去了大量有关锁申请的操作。

- 1.2 轻量级锁 轻量级锁是由偏向锁升级而来，当存在第二个线程申请同一个锁对象时，偏向锁就会立即升级为轻量级锁。
      注意这里的第二个线程只是申请锁，不存在两个线程同时竞争锁，可以是一前一后地交替执行同步块。

- 1.3 重量级锁 重量级锁是由轻量级锁升级而来，当同一时间有多个线程竞争锁时，锁就会被升级成重量级锁，此时其申请锁带来的开销也就变大。
      重量级锁一般使用场景会在追求吞吐量，同步块或者同步方法执行时间较长的场景。

2. 锁消除 消除锁是虚拟机另外一种锁的优化，这种优化更彻底，在JIT编译时，对运行上下文进行扫描，去除不可能存在竞争的锁。
    比如下面代码的method1和method2的执行效率是一样的，因为object锁是私有变量，不存在所得竞争关系。
```
public void method1() {
    Object object = new object();
    synchronized(object){
        System.out.println("hello world");
    }
}
public void method2() {
    Object object = new object();
    System.out.println("hello world");
}
```

3. 锁粗化 锁粗化是虚拟机对另一种极端情况的优化处理，通过扩大锁的范围，避免反复加锁和释放锁。
比如下面method3经过锁粗化优化之后就和 method4执行效率一样了
```
public void test3() {
    for (int i = 0; i < 10000; i++) {
        synchronized(test4.class) {
            System.out.println("hello world");
        }
    }
}
public void test4() {
    synchronized(test4.class) {
        for (int i = 0; i < 10000; i++) {
            System.out.println("hello world");
        }
    }
}
```

4. 自旋锁与自适应自旋锁
轻量级锁失败后，虚拟机为了避免线程真实地在操作系统层面挂起，还会进行一项称为自旋锁的优化手段。

自旋锁：许多情况下，共享数据的锁定状态持续时间较短，切换线程不值得，通过让线程执行循环等待锁的释放，不让出CPU。
    如果得到锁，就顺利进入临界区。如果还不能获得锁，那就会将线程在操作系统层面挂起，这就是自旋锁的优化方式。
    但是它也存在缺点：如果锁被其他线程长时间占用，一直不释放CPU，会带来许多的性能开销。

自适应自旋锁：这种相当于是对上面自旋锁优化方式的进一步优化，
    它的自旋的次数不再固定，其自旋的次数由前一次在同一个锁上的自旋时间及锁的拥有者的状态来决定，这就解决了自旋锁带来的缺点。

### 为什么要引入偏向锁和轻量级锁？为什么重量级锁开销大？
重量级锁底层依赖于系统的同步函数来实现，在linux中使用pthread_mutex_t（互斥锁）来实现。
这些底层的同步函数操作会涉及到：操作系统用户态和内核态的切换、进程的上下文切换，而这些操作都是比较耗时的， 因此重量级锁操作的开销比较大。
而在很多情况下，可能获取锁时只有一个线程，或者是多个线程交替获取锁，在这种情况下，使用重量级锁就不划算了，因此引入了偏向锁和轻量级锁来降低没有并发竞争时的锁开销。
------------------------------------------------------------------------------
## synchronized和ReentrantLock的区别？
- 底层：synchronized是JVM 关键字，依靠Monitor与锁升级；ReentrantLock基于AQS、CAS纯Java 实现。
- 中断：synchronized等待锁不可中断；ReentrantLock支持lockInterruptibly中断等待。
- 公平锁：synchronized仅非公平；ReentrantLock可选公平/非公平锁。
- 非阻塞抢锁：synchronized只能阻塞等待；ReentrantLock有tryLock限时尝试。
- 等待唤醒：synchronized只有一个等待队列，notify随机唤醒；ReentrantLock多Condition，精准分组唤醒。
- 释放机制：synchronized自动释放；ReentrantLock需手动finally解锁，否则死锁。
- 监控能力：ReentrantLock提供API查看锁占用、等待线程，synchronized无相关API。
  两者都支持锁重入。
### 开发选择建议
   简单同步、代码简短、无特殊需求：优先synchronized，简洁不易出错。
   需要公平锁、限时抢锁、可中断、精准唤醒、监控锁状态：选用ReentrantLock。
------------------------------------------------------------------------------
