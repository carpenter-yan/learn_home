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
### 五、Adder累加器（高并发计数专用，LongAdder 最常用）
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
