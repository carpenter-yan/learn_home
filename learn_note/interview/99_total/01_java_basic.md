## Java 基础・10 张实战卡（按主线顺序）

### 主线：一个对象的一生
```
① 对象怎么创建  → new String("abc") 为什么 2 个对象、拆装箱
② 对象存哪      → 常量池、intern
③ 对象怎么比    → == / equals / hashCode  ←（你刚问的这张卡在这条线上）
④ 对象带啥类型  → 泛型擦除
⑤ 对象怎么被操作 → 反射、注解
⑥ 对象怎么被优化 → JIT 内联、逃逸分析
```
## 卡 1　自动拆装箱 + Integer 缓存　★★★

**关键词**：valueOf / IntegerCache /-128~127 / 拆箱 NPE

**串线**：主线第 ① 环「对象怎么创建」（包装类对象）

**Q1 定义（20%）**

- 基本类型和包装类自动转换，
  装箱调的是 `Integer.valueOf()`，不是 new。
- Integer 缓存 **-128~127**，范围内 `==` 是 true。

**Q2 场景（50%）**

面试官问：`Integer a=127,b=127` 相等吗？换成 128 呢？

- 127 是 true，128 是 false。
- 因为 128 超出缓存，各 new 一个对象。
- 所以**包装类比较永远用 equals**。
- 还有个坑：`Integer a = null; int b = a;`
  自动拆箱直接 NPE。

**Q3 源码（30%）**

追问：缓存机制具体怎么实现的？

- `Integer.valueOf` 先判断
  `i >= IntegerCache.low && i <= IntegerCache.high`，
  命中取 `cache[i + 128]`，不命中才 new。
- IntegerCache 是静态内部类，
  上限可用 `-XX:AutoBoxCacheMax` 调。
- Float、Double 没有缓存（小数无穷多）。
- 隐蔽坑：三目 `true ? a : 0` 会做类型提升，
  把 null 的 Integer 拆箱，照样 NPE。

**项目钩子**

- “做金额计算时统一用 long 存分，避免包装类比较的坑。”

---

## 卡 2　String.intern () 与常量池　★★★★

**关键词**：StringTable / JDK6 vs JDK7 / 编译期折叠

**串线**：主线第 ② 环「对象存哪」

**Q1 定义（20%）**

- intern () 把字符串放进常量池（StringTable）。
- 池里已有，就返回池中引用；
  没有，就把堆里的引用登记进去。
- 用途：大量重复字符串时省内存。

**Q2 场景（50%）**

面试官问：`new String("a") + new String("b")` 之后 intern，
和直接 `"ab"` 相等吗？

- 这是考 JDK 版本差异的经典题。
- **JDK6** 常量池在永久代，intern 是**复制一份进池**，
  返回的池对象和堆对象是两个 → false。
- **JDK7+** 常量池移到堆，
  intern **存的是堆里那个对象的引用** → 这种情况 true。
- 反例：`"java"` 启动时就在池里预置了，
  所以 JDK7 也是 false。

**Q3 源码（30%）**

追问：运行时常量池和 StringTable 是一回事吗？

- **不是。**
- 运行时常量池是**每个类一份的符号登记簿**
  （类名、方法名、字面量索引），在元空间，**不是缓存**。
- StringTable 是**全局一份的字符串仓库**，
  JDK7+ 在堆里，**才是真正的缓存**。
- 两个池名字像，作用完全不同。

**项目钩子**

- “日志 / 枚举场景用 intern 或 Guava Interner 压内存。”

---

## 卡 3　== vs equals vs hashCode　★★★

**关键词**：地址 / 内容 / 契约 / HashMap 定位桶

**串线**：主线第 ③ 环「对象怎么比」→ 上承 “对象创建”，下接 “HashMap”

**Q1 定义（20%）**

- `==` 比地址，基本类型比数值。
- `equals` 默认也是比地址 ——
  也就是 `Object` 的实现 `return this == obj`。
- String、Integer 比内容，是它们**重写了 equals**，不是 equals 天生比内容。
- hashCode 的契约是：equals 相等，hashCode 必须相等。

**Q2 场景（50%）**

面试官问：你重写 equals 忘了 hashCode，出过什么问题？

- 线上真踩过：HashMap 里 put 进去，get 拿不到。
- 原因是 HashMap 两段式：
  先按 hashCode 算桶下标，再在桶内比 equals。
- hashCode 不一致，就落不到同一个桶，
  桶内比较那一步永远不发生。
- 连带三个后果：
  HashMap 数据丢、HashSet 出重复、对象当 key 永远删不掉（内存泄漏）。

**Q3 源码（30%）**

追问：HashMap 凭什么这么定位？

- `getNode` 里是：
  `p.hash == hash && ((k = p.key) == key || key.equals(k))`
- 顺序是**先比 hash、再比 ==、最后比 equals**。
- 所以 hashCode 是 “找对桶” 的索引，
  equals 是 “桶内认人”，两者必须成对重写。

**项目钩子**

- 讲完补一句：“我做订单缓存时遇到过 key 对象没重写 hashCode，
  导致缓存命中率异常。”

---

## 卡 4　BigDecimal / 浮点精度　★★★

**关键词**：二进制近似 /valueOf vs new /compareTo

**串线**：主线第 ③ 环「对象怎么比」（数值比较）

**Q1 定义（20%）**

- double 是二进制浮点，
  **无法精确表示十进制的 0.1**。
- 所以金额绝不能用 double，要用 BigDecimal。

**Q2 场景（50%）**

面试官问：`new BigDecimal(0.1)` 和 `BigDecimal.valueOf(0.1)` 一样吗？

- 不一样。
- **new 进去的是 double 的二进制近似**，
  会得到 0.1000000000000000055511151231257827…。
- `valueOf` 内部走 `Double.toString` 再构造，
  得到干净的 0.1。
- 所以构造金额一律用字符串或 valueOf。
- 还有个坑：BigDecimal 的 **equals 会比 scale**，
  `1.0` 和 `1.00` equals 是 false，比值必须用 `compareTo`。

**Q3 源码（30%）**

追问：BigDecimal 怎么做到精确的？

- 内部是 **`intVal`（BigInteger）+ `scale`（小数位数）**。
- 值 = intVal × 10^(-scale)，
  本质是**整数运算 + 十进制标度**，所以精确。
- JDK9 加了 `intCompact`（long）优化小数值。
- 注意 `divide` 必须指定 scale 和舍入模式，
  否则除不尽直接抛 `ArithmeticException`。

**项目钩子**

- “支付 / 对账全部用 BigDecimal + HALF_UP，规模小的用 long 存分。”

---

## 卡 5　泛型（类型擦除）　★★★

**关键词**：擦除到边界 / 桥方法 /reifiable/ Signature

**串线**：主线第 ④ 环「对象带什么类型信息」

**Q1 定义（20%）**

- 泛型是编译期检查、运行期擦除。
- 无边界擦成 Object，有边界擦成第一个边界。
- 所以 `List<String>` 和 `List<Integer>` 运行时是**同一个类**。

**Q2 场景（50%）**

面试官问：为什么 `instanceof List<String>` 编译报错？

- 因为 instanceof 要求类型是 **reifiable（可具体化）** 的。
- 擦除后泛型参数不存在，JLS §15.20.2 直接禁止。
- `List` 和 `List<?>` 可以（无界通配符视为可具体化），
  `List<String>` 不行。
- 本质是：**强转是 “我承诺”，instanceof 是 “我做判断”**。
- 判断一个运行期不存在的类型，会造假分支。

**Q3 源码（30%）**

追问：那框架怎么拿到泛型信息的？

- 编译时泛型信息存在 class 文件的 **Signature 属性**里。
- 反射用 `getGenericSuperclass` 能读出来。
- Spring 的 `ParameterizedTypeReference`、
  Jackson 的 `TypeReference` 都是**用匿名子类把类型参数 “焊” 进父类签名**。
- `new TypeReference<List<User>>(){}` 那个 `{}` 不能少。
- 擦除还有个副作用是**桥方法**：
  编译器生成一个桥方法保证多态正确。

**项目钩子**

- “写通用 RPC 客户端时用 TypeReference 拿泛型。”

---

## 卡 6　注解原理　★★★

**关键词**：Annotation 接口 / @Retention / SOURCE vs RUNTIME / 动态代理

**串线**：主线第 ⑤ 环「对象怎么被操作」

**Q1 定义（20%）**

- 注解是元数据，本身不干活。
- 靠**编译期处理器（APT）或运行时反射**才生效。
- Spring 就是靠反射读注解做装配。

**Q2 场景（50%）**

面试官问：@Retention 三档区别？
框架注解为什么都是 RUNTIME？

- **SOURCE**：源码级，编译后丢弃，Lombok 用这个。
- **CLASS**：默认档，写进 class 文件但 JVM 不加载。
- **RUNTIME**：保留到运行期，反射能读。
- Spring、JUnit、MyBatis 都必须 RUNTIME，因为它们全靠反射。

**Q3 源码（30%）**

追问：注解本质是什么？

- 注解本质是一个
  **继承 `java.lang.annotation.Annotation` 的接口**。
- 反射拿到的其实是 **JDK 动态代理（AnnotationInvocationHandler）**，
  不是真实实例。
- `@Retention` 自己也是注解，叫**元注解**。
- 它管 “这注解活多久”，`@Target` 管 “能贴哪”。
- `@Inherited` 只对类级注解生效、不穿透接口。

**项目钩子**

- “我做过自定义限流注解 + AOP 拦截。”

---

## 卡 7　反射　★★★

**关键词**：Class /getDeclared vs get / 为什么慢 /inflation/ MethodHandle

**串线**：主线第 ⑤ 环「对象怎么被操作」

**Q1 定义（20%）**

- 反射是运行时获取类结构并操作对象，
  核心入口是 `Class`。
- Spring 的 IOC 本质就是：
  “扫描 + 反射创建 + 反射注入”。

**Q2 场景（50%）**

面试官问：反射为什么慢？怎么优化？

- 四个原因：
  **参数要打包成 Object [] 并装箱**、
  **每次调用有访问检查**、
  **动态调用 JIT 难内联**、
  **Method 查找本身有开销**。
- 优化：缓存 Method、
  `setAccessible(true)` 跳过检查、
  再往上用 MethodHandle。

**Q3 源码（30%）**

追问：有没有更深一层？

- 有 ——**inflation 机制**：
  前 15 次走 native（`inflationThreshold` 默认 15）。
- 超过阈值后 JVM 动态生成字节码版本的 MethodAccessor，
  性能大幅提升。
- 所以反射是 “先慢后快”，不是一直慢。
- 它慢的**根**是 JIT 看不见、没法内联。
- MethodHandle 走 invokedynamic 能被内联，
  字节码生成干脆就是普通字节码。
- JDK18 的 **JEP 416** 已经用 MethodHandle 重写了核心反射。

**项目钩子**

- “写通用导出时缓存 Method + setAccessible。”

---

## 卡 8　static /final　★★★

**关键词**：类加载初始化 / 执行顺序 /final 引用 / 编译期内联

**串线**：主线第 ⑥ 环「对象初始化与优化」

**Q1 定义（20%）**

- static 属于类，类加载时就初始化。
- final 修饰变量不可改、方法不可重写、类不可继承。

**Q2 场景（50%）**

面试官问：static 块、实例块、构造器的执行顺序？

- **父静态 → 子静态 → 父实例块 → 父构造 → 子实例块 → 子构造**。
- 口诀：**静态先行、父先子后**。
- 另一问：final 修饰引用，指向的对象内容能改吗？
- ——**能**，final 只锁引用不锁内容。

**Q3 源码（30%）**

追问：`static final` 常量有什么坑？

- `static final` 的**编译期常量会被内联进调用方字节码**。
- 你改了值但没重新编译引用方，还是旧值，
  这是线上 “改了配置不生效” 的隐藏原因。
- 另外 static 方法不参与多态，
  子类同名方法是 “**隐藏**” 不是重写。
- final 还有 JMM 语义：
  **final 字段在构造完成后对其他线程可见**，是一种安全发布。

**项目钩子**

- “配置常量不要用 static final 硬编码，走配置中心。”

---

## 卡 9　Stream / Lambda　★★★

**关键词**：惰性求值 / 一次流水线 /invokedynamic/commonPool

**串线**：主线第 ⑥ 环「对象怎么被优化 / 调用」（编程范式角度）

**Q1 定义（20%）**

- Stream 是惰性流。
- 中间操作（filter/map）只是登记、不执行。
- 遇到终端操作（forEach/collect）才一次性流水线执行。

**Q2 场景（50%）**

面试官问：`filter().map().forEach()` 是遍历三次还是一次？

- **一次**。
- 这是 Stream 和传统 for 循环最大的区别：
  它是**内部迭代**，元素一个个穿过整条流水线，
  不是每个操作各遍历一遍。
- 所以也支持短路（findFirst 找到就停）。

**Q3 源码（30%）**

追问：Lambda 底层是匿名内部类吗？parallelStream 有什么坑？

- Lambda **不是匿名内部类**，
  底层是 `invokedynamic + LambdaMetafactory`，
  运行时生成实现函数式接口的类。
- 所以能内联、比匿名类轻。
- parallelStream 的坑：
  **默认用全局共享的 ForkJoinPool.commonPool**，
  并行度是核数 - 1。
- Web 容器里做 IO 密集会**阻塞公共池、拖垮整个 JVM**。
- 所以现实里基本只用 Stream，不用 parallelStream。

**项目钩子**

- “批量任务我用自定义线程池，不用 parallelStream。”