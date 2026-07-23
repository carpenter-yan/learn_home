## 什么是CAS?有什么风险？
### 一、什么是 CAS
CAS 全称：Compare And Swap，比较并交换，是一种无锁的乐观锁机制，底层 CPU 硬件指令，具备原子性。
执行逻辑三个参数：内存值 V、预期旧值 A、要更新的新值 B
比较：判断内存中当前值V是否等于预期值 A；
相等：说明期间没人修改，直接把内存值更新为 B，操作成功；
不相等：说明数据被其他线程修改，更新失败，一般自旋重试。

二、CAS 三大风险（高频考点）
1. ABA 问题
   现象 线程1读取变量值A，准备 CAS 修改； 线程2先把A改成B，又改回A；
   线程1执行CAS时发现还是A，认为数据没改动，正常修改，感知不到中间发生过修改，引发业务错误。
   解决方案
   使用版本号/时间戳，每次修改携带版本，对比时同时校验版本，代表类：AtomicStampedReference。
2. 自旋消耗 CPU（循环空转）
   CAS 失败会循环重试自旋抢锁，如果高并发下大量线程持续竞争，CAS一直失败，线程无限循环占用CPU，造成 CPU 飙升。
   解决方案
   限制自旋次数，超时放弃； 改用重量级锁，线程阻塞释放 CPU； 自适应自旋（JVM 轻量级锁优化）。
3. 只能保证单个变量原子操作，无法保证多行复合操作
   CAS 仅能原子更新一个变量，像i++、多变量同步修改这类复合操作，无法用CAS保证整体原子性。
   解决方案
   多个原子对象配合；
   使用锁（synchronized/ReentrantLock）。
### 3 三、极简口述版
CAS是比较并交换的乐观锁，依靠CPU硬件指令实现原子操作；先对比内存值与预期值，一致则更新，不一致重试。
存在三大风险：
ABA问题，数据被中途修改又复原无法感知，用带版本号的原子引用解决；
自旋循环消耗CPU，高竞争场景性能差；限制自旋次数或自适应自旋解决；
仅支持单个变量原子修改，无法处理复合操作。封装多个变量为对象解决；
------------------------------------------------------------------------------
## 原子操作类有哪些？
全部位于java.util.concurrent.atomic，一共五大类，适配不同场景。
### 一、基本类型原子类（单个基础数据）
用来保证boolean、int、long原子更新
AtomicInteger：整型原子类  AtomicLong：长整型原子类  AtomicBoolean：布尔原子类
常用方法：get、set、incrementAndGet、getAndAdd、compareAndSet（CAS）。
局限：只支持这三种基础类型，double、float没有原生原子类。
### 二、数组类型原子类（数组内元素原子修改）
数组里的元素可以并发安全更新，数组整体共享，元素CAS修改
AtomicIntegerArray：int 数组  AtomicLongArray：long 数组  AtomicReferenceArray：引用类型数组
示例：array.getAndAdd(下标, 增量)，只原子修改数组指定位置。
### 三、引用类型原子类（对象引用、自定义对象）
适合更新整个对象，解决对象引用并发修改；顺带解决ABA问题
AtomicReference：普通对象引用，CAS 更新对象
AtomicStampedReference：带版本号戳，解决 ABA
AtomicMarkableReference：带布尔标记（true/false），区分是否被修改过，不记录次数
使用场景：需要修改 POJO、实体对象，需要感知中途改动。
### 四、字段更新原子类（不用封装整个对象，只修改对象某个成员变量）
无需把整个对象包装原子类，直接操作普通实体的某一个属性，属性必须volatile修饰
AtomicIntegerFieldUpdater：修改对象int字段
AtomicLongFieldUpdater：修改对象long字段
AtomicReferenceFieldUpdater：修改对象引用类型字段
使用限制：字段必须volatile；不能是private（一般用 public/protected）。
适合：已有业务实体，不想改动原有代码，只让某个字段原子更新。
### 五、Adder累加器（高并发计数专用，LongAdder最常用）
高并发大量线程频繁自增时，解决AtomicInteger大量CAS自旋CPU飙高的问题。
分段累加：多Cell分散竞争，最终汇总求和，吞吐量远高于AtomicLong。
LongAdder：long 高频累加，适合统计计数、PV、UV
DoubleAdder：double 累加
LongAccumulator：自定义long运算规则（不限于加减，可以自定义计算逻辑）
DoubleAccumulator：自定义double运算规则
特点：只适合统计求和；读取最终值有弱一致性，不适合需要强实时CAS判断的业务。

### 极简速记清单
基础：AtomicInteger/Long/ Boolean
数组：AtomicIntegerArray/LongArray/ReferenceArray
对象引用：AtomicReference、AtomicStampedReference、AtomicMarkableReference
对象字段：xxxFieldUpdater
高并发计数：LongAdder、DoubleAdder、Accumulator
### 选型口诀
普通单个数字：AtomicInteger/AtomicLong
要防 ABA：AtomicStampedReference
只改对象某个属性：字段 Updater
超高并发计数统计：LongAdder
数组元素并发改：数组原子类
------------------------------------------------------------------------------
## AtomicInteger实现原理？
一句话概括：使用CAS实现。
以AtomicInteger的添加方法为例：
```
public final int getAndIncrement() {
    return unsafe.getAndAddInt(this, valueOffset, 1);
}
```
通过Unsafe类的实例来进行添加操作，来看看具体的CAS操作：
```
public final int getAndAddInt(Object var1, long var2, int var4) {
    int var5;
    do {
        var5 = this.getIntVolatile(var1, var2);
    } while(!this.compareAndSwapInt(var1, var2, var5, var5 + var4));

    return var5;
}
```
compareAndSwapInt是一个native方法，基于CAS来操作int类型变量。其它的原子操作类基本都是大同小异。

------------------------------------------------------------------------------
## ThreadLocal是什么？
### 一、定义
ThreadLocal是线程本地存储工具，为每个线程单独创建一份独立变量副本。
多线程共用同一个ThreadLocal对象，但每个线程读写的是自己专属的数据，线程之间数据完全隔离，天然不存在共享竞争，不用加锁就能规避并发问题。
### 二、底层原理
每个Thread线程内部自带ThreadLocalMap（key：ThreadLocal实例，value：线程存放的值）；
线程读写 threadLocal.set()/get()，本质是操作当前线程自己的ThreadLocalMap，不会碰其他线程的Map；
不同线程各自拥有独立容器，数据互不干扰。
### 三、核心常用API
set(T value)：给当前线程存入值 get()：获取当前线程绑定的值
remove()：删除当前线程的数据（必做，防止内存泄漏） initialValue()：重写该方法，设置初始默认值
### 四、典型业务场景
链路透传：全链路traceId、登录用户ID、账号信息，不用层层传参；
非线程安全对象复用：SimpleDateFormat、数据库连接，每个线程一份；
事务上下文、Mybatis分页参数、请求上下文存放。
### 五、两大重点问题
1. 内存泄漏原因
   ThreadLocalMap的key是弱引用，ThreadLocal对象被回收后key会变成 null；但value是强引用。
   如果线程长期存活（线程池复用线程），value一直无法被GC，堆积造成内存泄漏。
   解决办法 使用完必须手动调用remove()；不要在线程池中长期存放大对象。
2. 父子线程拿不到数据
   ThreadLocal数据只在当前线程有效，新开子线程无法继承。 需要跨父子线程传值使用InheritableThreadLocal。
### 六、优缺点
   优点：
   线程隔离，彻底规避并发竞争，无需锁； 方便上下文传递，简化代码传参。
   缺点：
   线程池场景极易内存泄漏，必须remove； 无法多线程共享数据，不能用来做线程间通信； 父子线程数据不通。
### 七、极简口述背诵版
   ThreadLocal给每个线程分配独立数据副本，数据线程隔离，避免多线程争抢不用加锁。底层依靠Thread内部的ThreadLocalMap存数据。
   多用于存放请求上下文、用户信息。使用线程池时必须调用remove，否则value强引用会发生内存泄漏；父子线程传值用InheritableThreadLocal。
### 八、高频区分
   synchronized/Atomic：管控共享数据；
   ThreadLocal：消灭共享，一人一份数据，思路完全不同。

------------------------------------------------------------------------------
## 什么是线程池？
### 一、概念
线程池是预先创建一批可复用线程的容器，统一管理线程的创建、执行、销毁。 不用每次新建Thread、用完销毁，复用线程执行任务，避免频繁创建销毁线程带来CPU开销。

核心好处
降低资源消耗：复用线程，减少线程频繁创建、销毁的系统开销；
提高响应速度：任务来了直接有线程干活，不用等待新建线程；
方便管控：限制最大并发数、定时执行、拒绝策略、监控线程运行。
### 二、核心七大参数（面试必考）
1. corePoolSize 核心线程数 常驻存活的线程，正常不会回收，即使空闲。
2. maximumPoolSize 最大线程数 线程池最多容纳线程总数。 最大线程数 = 核心线程 + 非核心空闲线程
3. keepAliveTime 空闲存活时间 非核心线程闲置超过该时长，会被回收；核心线程默认不会回收。
4. unit 时间单位 毫秒、秒、分钟等，配合 keepAliveTime。
5. workQueue 阻塞队列 核心线程满了，新任务放进队列排队。常见：ArrayBlockingQueue、LinkedBlockingQueue。
6. threadFactory 线程工厂 用来创建线程，可以自定义线程名、优先级、守护线程。
7. handler 拒绝策略 核心线程、队列、最大线程全部打满，触发拒绝策略。
### 三、任务执行流程（必背流程）
提交任务，当前运行线程数 < 核心线程数 → 创建核心线程执行；
核心线程已满，任务丢进阻塞队列排队；
队列塞满，总线程数 < 最大线程数 → 创建非核心线程执行；
总线程达到最大值，队列也满 → 执行拒绝策略。
### 四、4 种内置拒绝策略
AbortPolicy（默认）：直接抛出 RejectedExecutionException，报错终止；
CallerRunsPolicy：让提交任务的主线程自己执行任务，不会抛异常；
DiscardPolicy：默默丢弃当前新任务，无报错；
DiscardOldestPolicy：丢掉队列队首最老任务，把新任务入队。
### 五、Executors 四大工具线程池（阿里开发规范禁止使用）
newFixedThreadPool：固定线程数；无界队列，任务堆积会OOM；
newSingleThreadExecutor：单一线程串行执行；无界队列OOM 风险；
newCachedThreadPool：可缓存线程，无核心线程，最大线程无限；线程暴涨OOM；
newScheduledThreadPool：定时延时任务线程池。
阿里规范：禁止用Executors快捷创建，手动用ThreadPoolExecutor自定义参数，使用有界队列，避免内存溢出。
### 六、常见分类
普通线程池：执行常规异步任务；
定时线程池ScheduledThreadPool：定时、周期性执行；
ForkJoinPool：分支合并，适合大数据拆分计算。
### 七、关键问题：核心线程会被回收吗
默认不会。开启allowCoreThreadTimeOut(true)后，核心线程空闲超时同样回收。
### 八、极简背诵口语版
线程池复用线程，避免频繁建删线程浪费性能。ThreadPoolExecutor有七大参数。
任务先走核心线程，再进队列，队列满开非核心线程，全部打满走拒绝策略。
禁止用Executors创建，手动自定义线程池 + 有界队列防止OOM。四大拒绝策略分别是抛异常、调用者执行、丢新任务、丢老任务。
------------------------------------------------------------------------------
## 线程池有哪几种工作队列？
线程池常用阻塞队列（面试高频，全部实现 BlockingQueue）
1. ArrayBlockingQueue（最推荐，业务首选）
   有界队列，必须初始化指定固定容量，数组实现；
   先进先出 FIFO；队列满了才会新建非核心线程；
   优势：有边界，不会无限堆积任务，规避 OOM，阿里规范推荐搭配线程池使用。
2. LinkedBlockingQueue
   链表实现阻塞队列，默认无界，也可手动设置上限。
   不传容量：队列无限大。任务持续涌入只会塞满队列，永远达不到最大线程数，非核心线程永远不会创建；高并发大量任务会内存溢出 OOM。
   指定容量 → 变成有界队列，用法和 ArrayBlockingQueue 接近。
   Executors 的 Fixed、Single 线程池底层默认用无界 LinkedBlockingQueue，这也是阿里禁用 Executors 的核心原因。
3. SynchronousQueue
   不存储元素，无容量，不缓存任务。
   插入操作必须等待对应删除操作。
   提交任务时，如果没有空闲线程，直接新建线程，不会排队。
   适合任务量大、执行快的场景；newCachedThreadPool 默认使用此队列。
4. PriorityBlockingQueue
   优先级无界阻塞队列。按照任务优先级排序执行，不再遵守 FIFO；
   无界，存在 OOM 风险；适合需要按优先级调度的任务，普通业务很少用。
5. DelayQueue
   延迟无界队列。任务必须实现 Delayed 接口，只有延时到期才能被取出执行；
   多用于定时任务、订单超时、重试延迟场景，Scheduled 线程池底层依赖它；同样无界，有 OOM 隐患。
### 面试精简总结
   日常业务自定义线程池：优先ArrayBlockingQueue（有界），安全可控；
   LinkedBlockingQueue：无界易OOM；
   SynchronousQueue：不排队，来任务就找线程，适配Cached线程池；
   PriorityBlockingQueue：优先级；DelayQueue：延时定时。
### 选型口诀
   常规业务选有界 ArrayBlockingQueue；
   需要弹性扩容无排队用 SynchronousQueue；
   要优先级用 PriorityBlockingQueue；
   需要延迟执行用 DelayQueue。

------------------------------------------------------------------------------
## 线程池怎么关闭？
线程池两种正规关闭方式：shutdown ()、shutdownNow ()，面试必区分
### 一、shutdown () 温和关闭（日常最常用）
执行逻辑
拒绝接收新提交的任务； 已经进入队列、正在运行的任务全部正常执行完毕；
等所有任务跑完，线程池最终变为 TERMINATED。 不会中断正在执行的线程。
使用场景
正常业务下线、应用优雅停机，要保证已有任务做完。
配套判断方法
isShutdown()：只要调用过shutdown/shutdownNow，返回 true；
isTerminated()：所有任务全部结束、线程池彻底关闭才为 true。
可配合等待：
awaitTermination(time, unit)：限时等待线程池结束，超时返回布尔值。
### 二、shutdownNow () 强制关闭
执行逻辑
拒绝新任务； 尝试中断正在运行的工作线程（调用 Thread.interrupt ()）；
返回队列里还没执行的任务集合； 正在执行且不能响应中断的代码（sleep/wait 之外的死循环）依旧跑不完。
使用场景 紧急停服、故障止损，不需要继续跑完剩余任务。
三、关键区别对照表

方法|新任务|运行中任务|队列等待任务|中断线程
--|--|--|--|--
shutdown|拒绝|正常跑完|正常跑完|不中断
shutdownNow|拒绝|尝试中断|全部丢弃返回|发起中断

### 四、优雅关闭标准写法（线上规范模板）
```
executor.shutdown();
try {
    // 限时等待，给任务收尾时间
    if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
        // 超时没结束，强制收尾
        executor.shutdownNow();
        // 再等一次
        if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
        // 仍未关闭，日志告警
        }
    }
} catch (InterruptedException e) {
    executor.shutdownNow();
}
```
先温柔 shutdown，超时没走完再强制 shutdownNow。
### 五、补充知识点
没有办法只停一部分线程；只能整体关闭； SpringBoot 项目：依靠钩子 / 线程池配置自动关闭，不用手动写代码； 线程池一旦关闭，无法重新恢复使用，只能新建线程池。
### 六、极简背诵版
shutdown：不再接新任务，已有任务全部跑完，不中断线程，优雅停机；
shutdownNow：拒绝新任务，中断运行线程，未执行任务全部返回，紧急止损；
线上标准方案：先shutdown，awaitTermination限时等待，超时调用shutdownNow兜底。
------------------------------------------------------------------------------
## 线程池的线程数如何配置？
### 一、两大核心场景计算公式
1）CPU密集型（大量计算、少等待：加密、排序、循环运算、解析）
CPU 全程跑满，几乎无IO阻塞 公式： 核心线程数 = CPU 核心数 + 1
+1作用：防止某个线程偶然停顿（页缺失、操作系统短暂调度），浪费CPU。
举例：8 核 CPU → 设置 9 个线程。 上限不要超过CPU核心数太多，多了只会频繁上下文切换，CPU更卡。
2）IO密集型（后端最常见：数据库、Redis、MQ、网络请求、文件读写）
大量时间阻塞等待，CPU空闲多，可以多开线程压榨等待时间
公式： 核心线程数 = CPU 核心数 × (1 + 平均 IO 耗时 / CPU 计算耗时)
简化经验公式（线上最常用）： 线程数 = CPU 核心数 * 2 ~ CPU 核心数 * 5
举例：8 核机器，IO 等待远长于计算，常规设置 16~40 之间。
通用经验： 普通 Web 接口、数据库多：2~4 倍 CPU 核心
大量调用三方接口、多 Redis/MQ：4~5 倍 CPU 核心
### 二、线上生产实操配置（后端落地规则）
绝对不要拍脑袋写固定值，优先根据压测调优，公式只是初始参考值；
必须搭配有界队列（ArrayBlockingQueue），不能用无界队列；队列长度建议100~500，根据业务量；
拒绝策略优先CallerRunsPolicy，保障任务不丢，不会疯狂报错。
### 三、特殊场景单独配置
混合型任务（既有计算又有IO）
拆分两个线程池：计算任务走 CPU 密集线程池，IO 任务走 IO 密集线程池，不要混在一个池里。
定时任务 ScheduledThreadPool
定时任务一般不会并发极高，线程数设置 5~20，按定时任务总数决定，保证不同定时不互相阻塞。
批量文件、大文件下载
IO很重，线程数不宜过大，防止打满磁盘IO，建议小线程数 + 分段处理。
Redis/MQ 消费线程池
受中间件服务端连接数限制，不能无限开大；一般配置10~30，配合中间件最大连接数对齐。
### 四、极简背诵版
CPU 密集：核心线程 = CPU 核数 + 1，避免上下文切换；
IO 密集：公式核数 *(1+IO 耗时 / CPU 耗时)，经验取 2~5 倍 CPU；
公式仅初始值，最终以压测QPS、CPU 使用率、RT调优；
搭配有界队列，禁止无界队列；混合任务拆分成多个线程池。
六、调优判断指标
压测观察这3 项：
CPU 使用率稳定 70%~80% 最佳，长期满 90% 说明线程太多；
阻塞队列持续堆满，需要加大线程数；
RT持续变长、大量任务被拒绝，适当扩容线程/队列。