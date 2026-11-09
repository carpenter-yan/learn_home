# Java 并发 / 线程・10 张实战卡（按主线顺序）

## 主线：多线程怎么安全高效地共享数据

```
① 线程怎么建和跑   → 线程状态、生命周期
② 共享变量怎么不出错 → JMM、volatile
③ 互斥怎么加       → synchronized、锁升级
④ 更灵活的锁       → ReentrantLock / AQS
⑤ 无锁怎么做       → CAS / 原子类 / ABA
⑥ 线程怎么复用     → 线程池参数与执行流程
⑦ 线程本地变量     → ThreadLocal / 内存泄漏
⑧ 并发容器         → CHM / COW / 阻塞队列
⑨ 线程怎么协作     → CountDownLatch / Semaphore
⑩ 怎么防死锁       → 死锁四条件与排查
```

### 卡 1　线程状态与生命周期　★★★

**关键词**：6 种状态 / BLOCKED vs WAITING

**串线**：主线第 ① 环

**Q1 定义（20%）**

- 6 种状态：NEW、RUNNABLE、BLOCKED、WAITING、TIMED_WAITING、TERMINATED。

**Q2 场景（50%）**

面试官问：BLOCKED 和 WAITING 有什么区别？

- BLOCKED 是**等锁**（synchronized 没抢到）。
- WAITING 是**主动等待**（wait、join、park），需要别人唤醒。
- TIMED_WAITING 是带超时的等待（sleep、wait (timeout)）。

**Q3 源码（30%）**

追问：线程能回到 RUNNABLE 吗？能重新启动吗？

- WAITING/BLOCKED 拿到锁或被唤醒后都能回到 RUNNABLE。
- 但 **TERMINATED 不能重新 start**，会抛 `IllegalThreadStateException`。
- 线程是 “一次性” 的，要复用得靠线程池。

**项目钩子**

- “用 jstack 看线程状态定位阻塞问题。”

### 卡 2　volatile　★★★★

**关键词**：可见性 / 禁止重排序 / 内存屏障 / 不保证原子性

**串线**：主线第 ② 环「共享变量怎么不出错」

**Q1 定义（20%）**

- volatile 保证**可见性**和**禁止指令重排序**。
- 但**不保证原子性**。

**Q2 场景（50%）**

面试官问：`i++` 用 volatile 修饰就线程安全了吗？

- 不。`i++` 是 “读 - 改 - 写” 三步，不是原子操作。
- volatile 只保证每次读到最新值，不保证三步整体原子。
- 要用 `AtomicInteger`（CAS）或加锁。

**Q3 源码（30%）**

追问：volatile 底层怎么实现的？

- 写操作后插入 **StoreLoad 内存屏障**，强制刷回主存。
- 读操作前插入 **LoadLoad / LoadStore 屏障**，强制从主存读。
- 靠 CPU 的 **MESI 缓存一致性协议** 保证其他核看到变化。
- 禁止重排序靠**内存屏障**约束编译器和 CPU。

**项目钩子**

- “双重检查单例用 volatile 防止半初始化对象逸出。”

### 卡 3　synchronized 原理与锁升级　★★★★

**关键词**：对象头 / Monitor / 偏向锁 / 轻量级锁 / 重量级锁

**串线**：主线第 ③ 环「互斥怎么加」

**Q1 定义（20%）**

- synchronized 靠 **对象头 Mark Word + Monitor（管程）** 实现。
- 锁有升级过程：无锁 → 偏向 → 轻量级 → 重量级。

**Q2 场景（50%）**

面试官问：锁升级的过程是什么？

- **偏向锁**：只有一个线程访问，Mark Word 记线程 ID，无 CAS 开销。
- **轻量级锁**：有竞争时，用 CAS 抢锁 + **自旋**等待。
- **重量级锁**：自旋失败，升级为 Monitor，线程**阻塞挂起**。
- 升级是**单向**的，不会降级。
- 注意：**JDK15 已默认弃用偏向锁**（JEP 374）。

**Q3 源码（30%）**

追问：synchronized 和 ReentrantLock 有什么区别？

- synchronized：JVM 层实现、自动释放、不可中断、非公平。
- ReentrantLock：AQS 实现、手动 unlock、可中断、可公平、支持 Condition。
- 简单场景用 synchronized（JDK6 后性能已不差），复杂场景用 Lock。

**项目钩子**

- “高并发计数器从 synchronized 换成 LongAdder。”

### 卡 4　AQS　★★★★

**关键词**：state / CLH 队列 / 独占 vs 共享

**串线**：主线第 ④ 环「更灵活的锁」

**Q1 定义（20%）**

- AQS 是 `AbstractQueuedSynchronizer`，是 ReentrantLock、Semaphore 等的**共同基础**。
- 核心是 **volatile int state** + **CLH 双向等待队列**。

**Q2 场景（50%）**

面试官问：AQS 是怎么让线程排队的？

- 抢不到锁的线程封装成 Node，**入队**（CAS 挂到队尾）。
- 然后 `park()` 挂起。
- 前驱节点释放锁时，`unpark()` 唤醒后继。
- state 用 CAS 修改，表示锁的持有次数 / 许可数。

**Q3 源码（30%）**

追问：独占和共享模式区别？

- 独占：state=1 表示已占用，ReentrantLock 用它。
- 共享：state=n 表示剩余许可，Semaphore / CountDownLatch 用它。
- 共享模式下多个线程可同时获取，靠 `tryAcquireShared` 返回值判断。

**项目钩子**

- “读 AQS 源码理解 ReentrantLock 的公平 / 非公平实现。”

### 卡 5　CAS 与 ABA　★★★★

**关键词**：CompareAndSwap / 自旋 / ABA / AtomicStampedReference

**串线**：主线第 ⑤ 环「无锁怎么做」

**Q1 定义（20%）**

- CAS：比较内存值和预期值，相等才更新，否则失败重试。
- 靠 CPU 的 `cmpxchg` 指令保证原子。

**Q2 场景（50%）**

面试官问：CAS 有什么问题？

- **ABA 问题**：值从 A→B→A，CAS 以为没变，其实变过。
- **自旋开销**：竞争激烈时一直空转，浪费 CPU。
- **只能保证一个变量**：多变量原子性做不到（要 AtomicReference 打包）。

**Q3 源码（30%）**

追问：ABA 怎么解决？

- 用 `AtomicStampedReference`，加**版本号**（stamp）。
- 每次修改版本号 +1，ABA 时版本号变了，CAS 会失败。
- 代价是多了版本号维护开销。

**项目钩子**

- “无锁队列 / 计数器优先用 LongAdder 而不是 AtomicLong。”

### 卡 6　线程池核心参数与执行流程　★★★★

**关键词**：7 个参数 /core → queue → max → reject

**串线**：主线第 ⑥ 环「线程怎么复用」

**Q1 定义（20%）**

- 7 个参数：corePoolSize、maximumPoolSize、keepAliveTime、unit、workQueue、threadFactory、handler。

**Q2 场景（50%）**

面试官问：提交任务后线程池怎么处理？

- ① 线程数 < core：**新建核心线程**执行。
- ② 核心满了：**任务进队列**排队。
- ③ 队列满了：**新建非核心线程**直到 maximum。
- ④ 也满了：**执行拒绝策略**。
- 关键：**先入队，再扩到最大线程**—— 这个顺序很多人答错。

**Q3 源码（30%）**

追问：四种拒绝策略是什么？

- `AbortPolicy`：抛 `RejectedExecutionException`（**默认**）。
- `CallerRunsPolicy`：由提交任务的线程自己执行（**降级不丢任务**）。
- `DiscardPolicy`：直接丢弃，不报错。
- `DiscardOldestPolicy`：丢掉队列最老的任务，再尝试提交。

**项目钩子**

- “线上线程池用 CallerRunsPolicy 做背压，避免任务丢失。”

### 卡 7　线程池大小怎么定　★★★

**关键词**：CPU 密集 / IO 密集 / 压测

**串线**：主线第 ⑥ 环

**Q1 定义（20%）**

- CPU 密集：`N + 1`（N 为核数）。
- IO 密集：`2N` 甚至更多。

**Q2 场景（50%）**

面试官问：你们线上线程池设多大？怎么验证？

- 公式只是起点：`N * (1 + 等待时间/计算时间)`。
- 实际必须**压测**，看 CPU 利用率和队列积压。
- 还要看任务类型：DB 查询、RPC 调用这类 IO 可以更大。
- 结论：**公式定初始值，压测定最终值**。

**Q3 源码（30%）**

追问：线程池为什么不用 Executors 创建？

- `newFixedThreadPool` / `newSingleThreadExecutor` 用**无界队列**，可能 OOM。
- `newCachedThreadPool` 最大线程数是 `Integer.MAX_VALUE`，可能创建过多线程。
- 阿里规范明确要求**手动 new ThreadPoolExecutor**，明确参数。

**项目钩子**

- “线上统一用 ThreadPoolExecutor 显式指定队列容量和拒绝策略。”

### 卡 8　ThreadLocal 与内存泄漏　★★★★

**关键词**：ThreadLocalMap / 弱引用 key /remove

**串线**：主线第 ⑦ 环「线程本地变量」

**Q1 定义（20%）**

- 每个 Thread 内部有一个 `ThreadLocalMap`。
- key 是 **ThreadLocal 的弱引用**，value 是强引用。
- 用来做线程隔离，比如用户上下文、事务连接。

**Q2 场景（50%）**

面试官问：ThreadLocal 为什么会内存泄漏？

- key 是弱引用，GC 时 ThreadLocal 对象可能被回收，key 变成 null。
- 但 **value 还是强引用**，被 Thread → ThreadLocalMap → Entry 持有。
- 线程池里线程长期存活 → value 一直不释放 → 泄漏。
- 解决：**用完必须 `remove()`**。

**Q3 源码（30%）**

追问：为什么 key 要用弱引用？

- 为了让 ThreadLocal 对象没外部引用时能被回收。
- 配合 `set/get` 时的 `expungeStaleEntry` 清理 key 为 null 的 entry。
- 但**清理是惰性的**，所以还是得主动 remove。
- 正确用法：try-finally 里 remove。

**项目钩子**

- “请求上下文用 ThreadLocal，finally 里 remove，防止线程池复用污染。”

### 卡 9　并发容器与阻塞队列　★★★

**关键词**：CHM / CopyOnWriteArrayList / ArrayBlockingQueue / LinkedBlockingQueue

**串线**：主线第 ⑧ 环「并发容器」

**Q1 定义（20%）**

- ConcurrentHashMap：高并发 key-value。
- CopyOnWriteArrayList：**读多写极少**。
- ArrayBlockingQueue：有界数组队列。
- LinkedBlockingQueue：链表队列（默认无界，要指定容量）。

**Q2 场景（50%）**

面试官问：CopyOnWriteArrayList 什么场景用？

- 写时**复制整个数组**再替换，读不加锁。
- 适合**读远多于写**、数据量小的场景（如监听器列表）。
- 不适合频繁写：每次写都 O (n) 复制，内存翻倍。
- 而且**读不到最新数据**（弱一致）。

**Q3 源码（30%）**

追问：ArrayBlockingQueue 和 LinkedBlockingQueue 区别？

- ArrayBlockingQueue：**有界**、数组、一把锁。
- LinkedBlockingQueue：链表、**默认无界**（`Integer.MAX_VALUE`）、两把锁（入队 / 出队分离）。
- 生产环境**一律用有界队列**，防止任务堆积 OOM。

**项目钩子**

- “线程池队列用有界 ArrayBlockingQueue + 拒绝策略做保护。”

### 卡 10　死锁　★★★

**关键词**：四条件 / 循环等待 /jstack 排查

**串线**：主线第 ⑩ 环「怎么防死锁」

**Q1 定义（20%）**

- 死锁四个必要条件：**互斥、占有并等待、不可抢占、循环等待**。
- 破坏任意一个就能避免死锁。

**Q2 场景（50%）**

面试官问：怎么排查线上死锁？

- `jstack <pid>` 会直接输出 **"Found one Java-level deadlock"**。
- 或者用 Arthas 的 `thread` 命令。
- 看到两个线程互相等待对方持有的锁，就是死锁。

**Q3 源码（30%）**

追问：怎么预防死锁？

- **固定加锁顺序**（最有效）：所有线程按同一顺序申请锁。
- **超时放弃**：`tryLock(timeout)` 拿不到就释放已持有的锁。
- 减少锁持有时间、减少嵌套锁。

**项目钩子**

- “转账场景固定按账户 ID 顺序加锁，避免 ABBA 死锁。”