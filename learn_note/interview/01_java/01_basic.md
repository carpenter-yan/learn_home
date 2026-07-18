
## 一、Java 语言核心特点
Java 是静态强类型、纯面向对象语言，依靠 JVM 实现跨平台；
自带 GC 自动管理内存，屏蔽指针保证内存安全；
内置完善多线程并发体系；
拥有庞大成熟开源生态，广泛用于后端微服务、大数据、Android 开发；
缺点是启动慢、GC 存在停顿、代码较繁琐。

## 二、面向对象和面向过程的区别?
一、面向过程
核心：以步骤、函数为核心，数据和操作分离
优点：执行性能更高，无需类实例化，资源开销小；
缺点：代码复用、维护、扩展能力差，耦合度高。
二、面向对象
核心：以对象、类为核心，属性与方法封装一体，具备封装、继承、多态特性
优点：易维护、易复用、易扩展；可搭建低耦合系统，灵活性强，适合大型复杂业务系统。
缺点：性能弱于面向过程，类实例化会产生额外开销，消耗更多资源。
### 一句话速记
面向过程以步骤、函数为核心，胜在性能，难维护拓展；面向对象以对象和类为核心，胜在易复用维护，但存在性能开销。

## 三、接口和抽象类有什么区别？
- 抽象类单继承、有构造器、可包含任意成员变量与任意权限方法，代表is-a，用于抽取公共模板；
- 接口多实现、无构造器、变量全是常量，默认方法 public，代表行为能力，做规范定义；
- 抽象类侧重代码**复用**，接口侧重行为**解耦**。

## 四、重载和重写什么区别？
一、基础定义
重载（Overload）
在同一个类里，多个方法方法名相同，参数列表不同，构成重载。
目的：同一动作提供多种入参形式，方便调用。

重写（Override）
存在于父子类继承关系，子类重新实现父类普通实例方法，方法名、参数完全一致。
目的：实现多态，子类自定义业务逻辑。

二、关键细节补充
重载只看方法名 + 参数，返回值、权限修饰符不参与区分；
重写约束：
父 private 方法子类无法重写，只是新建同名方法；
父 static 方法子类写同名静态方法是方法隐藏，不属于重写；
子类抛出异常只能更少、更小，不能新增更大受检异常。

### 三、极简一句话总结
重载同类同名不同参，方便多形式调用； 重写父子同名同参数，用于多态拓展；重写有权限、异常约束，重载无约束。

## 五、JDK、JRE、JVM三者有什么关系？
1. JVM（Java Virtual Machine，Java 虚拟机）
   最底层核心，负责运行 .class 字节码；实现跨平台。
   只包含虚拟机执行引擎、内存模型、GC、类加载器，没有类库，不能单独运行 Java 程序。
2. JRE（Java Runtime Environment，Java 运行时环境）
   = JVM + Java 基础类库（rt.jar、各种工具类）
   作用：只用来运行已经编译好的 Java 程序，不提供编译工具。
3. JDK（Java Development Kit，Java 开发工具包）
   = JRE + 开发工具（javac编译器、java、jar、jmap、jstack 等调试监控工具）
   作用：给开发者使用，既能编译.java 源码，也能运行程序。 
### 极简背诵版
   JDK包含JRE，JRE 包含 JVM；JVM是执行字节码的虚拟机；JRE提供运行环境；JDK提供编译、开发整套工具。

## 六、访问修饰符 public、private、protected？

修饰符|本类|同包类|不同包子类|任意类
--|--|--|--|--
private|✅|❌|❌|❌
默认|✅|✅|❌|❌
protected|✅|✅|✅|❌
public|✅|✅|✅|✅

### 面试一句话总结
private仅本类可见；默认同包可见；protected同包+跨包子类可见；public全部可见。开发成员变量优先private，对外提供访问方法。

## 七、JAVA 创建对象有哪些方式？
1. new关键字
```
    Person p1 = new Person();
```
2. Class.newInstance
```
    Person p1 = Person.class.newInstance();
```
3. Constructor.newInstance
```
    Constructor<Person> constructor = Person.class.getConstructor();
    Person p1 = constructor.newInstance();
```
4. clone
```
    Person p1 = new Person();
    Person p2 = p1.clone();
```
5. 反序列化
```
    Person p1 = new Person();
    byte[] bytes = SerializationUtils.serialize(p1);
    Person p2 = (Person)SerializationUtils.deserialize(bytes);
```

## 八、值传递和引用传递的区别？
1. 值传递：指的是在方法调用时，传递的参数是按值的拷贝传递，传递的是值的拷贝，也就是说传递后就 互不相关了。
2. 引用传递：指的是在方法调用时，传递的参数是按引用进行传递，其实传递的是引用的地址，也就是变 量所对应的内存空间的地址。也就是说传递前和传递后都指向同一个引用（同一个内存空间）

`基本类型作为参数被传递时肯定是值传递；引用类型作为参数被传递时也是值传递，只不过“值”为对应的引用`

## 九、==和equals有什么区别？
1. == ：如果是基本数据类型，比较两个值是否相等；如果是对象，比较两个对象的引用是否相等，指向同一块内存区域
2. equals：用于对象之间，比较两个对象的值是否相等。

## 十、hashCode() 的作用？
### 一、核心定位
hashCode()是Object提供的native方法，返回对象int哈希值，专为哈希容器服务：HashMap、HashSet、Hashtable、ConcurrentHashMap。

### 二、两大核心作用
1. 确定对象存放的哈希桶（减少equals对比次数，提升查询效率）
   HashMap底层数组 + 链表 / 红黑树： 调用key.hashCode () 计算哈希，取模定位数组下标（哈希桶）；
   只有多个key落在同一个桶（哈希冲突）时，才调用 equals() 精确对比内容；
   如果没有hashCode：存入/查找都要遍历全部元素挨个equals，时间复杂度 O (n)； 有hashCode：先分桶，大部分情况一次定位，平均O(1)。
2. 保证哈希容器去重（HashSet 判重逻辑）
   HashSet不允许重复元素，判断流程： 先对比两个对象hashCode是否相等；
   hash 不等 → 直接判定为不同对象，不用 equals；
   hash 相等 → 再调用equals判断内容是否真正相等。
### 三、equals 与 hashCode 强制约定（重写必遵守）
   如果 a.equals(b) == true，则 a.hashCode() 必须等于 b.hashCode()；
   违反：两个相等对象分到不同桶，Map 会存两份相同 key，出现逻辑错误。
   如果 a.hashCode() == b.hashCode()，a.equals(b) 不一定为 true（哈希冲突正常现象）。
   若 equals() 返回 false，hashCode 允许相同或不同，无强制约束。
### 四、原生Object.hashCode()特点
   默认返回对象内存地址转换的数字；
   两个内容相同的新对象，hashCode不一样，会导致 HashMap中无法覆盖，HashSet存重复数据。
   因此自定义实体类重写 equals 时，必须同步重写 hashCode。
### 五、面试口述标准答案
   hashCode 主要服务于 HashMap、HashSet 这类哈希集合：
   通过哈希值快速定位哈希桶，大幅减少equals对比次数，让存取查询接近O(1)；
   作为去重前置判断，HashSet先比较哈希值，哈希不同直接判定对象不重复，提升性能；
   有强制规范：equals相等的两个对象，hashCode必须相等；自定义类重写equals一定要重写hashCode，否则会出现Map存重复key、数据查询不到的bug。

## 十一、String有哪些特性?
1. 不变性：String是只读字符串，是一个典型的immutable对象，对它进行任何操作，其实都是创建一个新的对象，再把引用指向该对象。
    不变模式的主要作用在于当一个对象需要被多线程共享并频繁访问时，可以保证数据的一致性；
2. 常量池优化：String对象创建之后，会在字符串常量池中进行缓存，如果下次创建同样的对象时，会直接返回缓存的引用
3. final：使用final来定义 String类，表示 String类不能被继承，提高了系统的安全性。

## 十二、String、StringBuffer、StringBuilder区别？
1. String。采用 final修饰，对象不可变，线程安全。如果对一个已经存在的String对象修改，会重新创建一个新对象，并把值放进去。
2. StringBuffer，采用 synchronized 关键字修饰，线程安全
3. StringBuilder，非线程安全，但效率会更高些，适用于单线程。

## 十三、Error和Exception区别？
### 一、继承体系
Throwable是所有异常/错误的顶层父类，下分两大分支：
Error：系统级严重错误
Exception：程序可捕获、可处理异常

### 二、Error 错误
定义：JVM运行时、系统底层严重故障，程序无法修复
特点： 无需手动捕获，不需要 try-catch； 代码层面无能为力，只能终止程序；
常见例子：
StackOverflowError 栈溢出
OutOfMemoryError OOM 内存溢出
NoClassDefFoundError 类加载失败

### 三、Exception 异常
程序逻辑产生，可捕获、可处理，分两类：
1. 受检异常 CheckedException
   编译期强制检查，要么try-catch捕获，要么方法上throws抛出 示例：IOException、SQLException、FileNotFoundException
2. 非受检异常RuntimeException
   编译不报错，运行时才抛出，不用强制捕获/声明 示例：NullPointerException、ArrayIndexOutOfBoundsException、ClassCastException

### 四、核心对比

   维度|Error|Exception
   --|--|--
   严重程度|致命，程序崩溃|普通业务异常，可恢复
   产生来源|JVM / 操作系统底层|代码逻辑问题
   是否需要捕获|不建议捕获，捕获也无法处理|可 try-catch 处理
   处理方式|重启服务、调 JVM 参数、扩容机器|补判空、参数校验、重试、兜底返回

### 五、面试口述标准答案
   Error和Exception都继承自Throwable，核心区别：
   Error是JVM、系统级致命错误，比如OOM、栈溢出，程序代码无法修复，不需要捕获，出现后服务基本只能重启；
   Exception是代码逻辑产生的异常，能够捕获处理，分为受检异常和运行时异常：
   受检异常编译强制要求处理，如IO、数据库异常；
   RuntimeException运行时才报错，编译无提示，空指针、数组越界都属于这类；
   简单说：Error管系统崩溃，Exception管业务代码出错。

## 十四、什么是反射？
### 一、定义
反射（Reflection）：Java在运行时，动态获取类的完整信息、调用类任意成员（构造方法、普通方法、属性）的机制。 正常编码是编译期操作类；反射是运行期逆向解析类字节码。

### 二、核心作用
运行时获取类名、父类、接口、字段、方法、注解；
无视权限修饰符（private/protected/public），实例化对象、赋值、调用方法；
框架底层核心：Spring IoC、MyBatis、SpringMVC、注解、动态代理全依赖反射。

### 三、获取Class对象的 3 种方式
```java
// 1. 类名.class（编译期，性能最好）
Class<User> clazz = User.class;

// 2. 对象.getClass()（已有实例）
User user = new User();
Class<?> clazz2 = user.getClass();

// 3. Class.forName("全类名")（运行时动态加载，框架最常用）
Class<?> clazz3 = Class.forName("com.entity.User");
```

### 四、反射常用 API
构造器：getConstructor()/getDeclaredConstructor() → newInstance() 创建对象
成员变量：getField()/getDeclaredField() → set()/get() 读写属性
成员方法：getMethod()/getDeclaredMethod() → invoke() 执行方法
setAccessible(true)：暴力破解，访问 private 私有成员

### 五、优缺点
优点 : 高度灵活，框架解耦，无需new硬编码； 配合配置文件/注解，实现通用工具类。
缺点
性能低：绕过编译优化，大量反射会拖慢程序；
破坏封装：可强行访问私有变量、私有方法；
可读性差，代码晦涩，异常难排查；
存在安全风险，部分安全管理器会限制反射。

### 六、优化手段（加分）
缓存Class、Method、Field对象，避免重复获取；
JDK8+使用MethodHandle替代传统反射；
引入反射工具：Spring ReflectionUtils、Apache BeanUtils；
高并发场景用字节码生成（CGLIB、ASM）替代反射。

### 七、面试口述标准答案
反射是Java运行时动态解析类的机制：正常代码编译期确定类调用，反射在程序运行阶段逆向读取字节码；
可以动态拿到类的构造、属性、方法，甚至暴力访问私有成员、创建对象、执行方法；
Spring、MyBatis等主流框架底层都大量使用反射实现通用化；
缺点是性能较差、破坏封装；优化可以缓存反射元数据，或使用CGLIB字节码技术替代。


## 十五、什么是泛型 ?
### 一、定义
泛型（Generic）：编译期类型参数化，把类型作为参数传给类、接口、方法，实现一套代码适配多种数据类型。
核心作用：类型检查 + 避免强制类型转换，编译期约束类型，消除类型转换异常风险。

### 二、三大使用形式
```java
// 1. 泛型类/泛型接口
public class ArrayList<E> {}
public interface List<E> {}
// <E> 代表类型形参，实例化时指定真实类型：List<String>。

//2. 泛型方法 
public <T> T getFirst(List<T> list){
    return list.get(0);
}
// 方法单独定义类型参数，作用域仅限当前方法：

// 3. 泛型限定（上下界）
// 上界 <? extends Animal> 只能读，不能存；代表Animal及其子类，用于读取数据。
// 下界 <? super Dog>只能存，不能随便读；代表Dog及其父类，用于写入数据。
```

### 四、类型擦除（核心底层考点）
泛型只存在于编译阶段，编译生成字节码时会擦除泛型信息：无边界泛型T → 擦除为Object；有上界T extends Number → 擦除为Number； 字节码中自动插入强制类型转换代码。
擦除带来的限制
   - 不能用泛型类型创建对象：new T()；
   - 不能定义泛型数组：T[] arr = new T[10]；
静态变量不能使用类泛型；
运行时无法获取泛型真实类型（需通过反射方法参数/子类继承捕获）；
重载方法擦除后签名冲突。

###b五、通配符？
- ? 无界通配符：任意类型，只读； 
- ? extends T 上界：T 及子类，生产者模式（读）； 
- ? super T 下界：T 及父类，消费者模式（写）。

### 六、泛型优点
- 编译期类型校验，类型错误提前暴露；
- 省去大量强制类型转换代码；
- 代码复用，一套容器 / 工具类兼容所有类型；
- 避免频繁类型转换带来的运行时异常。

### 七、面试口述标准答案
   泛型是Java提供的类型参数化机制：
   可以给类、接口、方法定义类型参数，编译期约束存入集合的元素类型，消除强制转换，把类型错误提前到编译阶段；
   底层采用类型擦除，泛型仅编译有效，字节码中会替换为边界父类，并自动插入强转；
   通配符分三类：无界?、上界extends适合读取、下界super适合写入；
   因为类型擦除，泛型存在诸多限制，不能new泛型对象、不能泛型数组、静态成员无法使用类泛型。
   简单来说：泛型就是为了类型安全、代码复用。

## 十六、序列化、反序列化是什么？
### 一、定义
序列化Serialization
把内存中的Java 对象，转换成可传输/持久化的二进制字节流（字节数组、文件、网络报文）。
对象只存活在堆内存，进程关闭就销毁；序列化后可以落地保存或跨进程传输。

反序列化Deserialization
将二进制字节流恢复成内存中的Java对象。

### 二、核心用途
网络传输：RPC、Dubbo、HTTP 接口、消息队列传递对象；
持久化存储：对象写入本地文件、Redis 缓存；
进程间传递对象：Servlet Session钝化、深拷贝对象。

### 三、JDK 原生序列化
1. 实现方式
   类实现标记接口 java.io.Serializable（无方法，仅做标识）
```java
   class User implements Serializable {
       private Long id;
       private String name;
   }
```
   工具类：ObjectOutputStream 序列化 / ObjectInputStream 反序列化

2. transient 关键字
   被 transient修饰的字段不会参与序列化，反序列化时值为默认值（数字 0、引用 null）。 适用场景：临时缓存、密码、会话敏感数据。

3. serialVersionUID 版本号（高频考点）
```java
private static final long serialVersionUID = 1L;
```
作用：
   序列化时写入版本号，反序列化校验版本；
   手动固定值：类增删字段后，旧序列化文件仍能正常反序列化；
   不手动定义：JDK自动根据类结构生成，修改类后版本号变化，反序列化抛InvalidClassException。

### 四、常见序列化方案对比
   JDK 原生序列化 优点：JDK 自带，无需依赖包； 缺点：字节体积大、性能差、只能Java语言互通、存在反序列化安全漏洞。
   JSON 序列化（Jackson/Fastjson） 转JSON字符串，可读性好，跨语言通用；体积比二进制大。
   Protobuf/Hessian/Kryo 二进制序列化，体积小、速度快，RPC 框架主流方案。

### 五、安全风险（必背）
   原生反序列化存在严重安全漏洞： 攻击者构造恶意序列化字节，反序列化时触发恶意代码执行。
   生产规范：尽量不使用JDK原生序列化，改用Protobuf、JSON 等更安全方案。

### 六、面试口述标准答案
   序列化是将内存Java对象转为二进制字节流，用于文件存储、网络传输；反序列化是把字节流还原成对象。
   JDK原生序列化需要实现Serializable接口，transient修饰字段不会序列化；serialVersionUID用于版本兼容，不定义则类改动后旧数据无法解析。
   常用方案有JDK 原生、JSON、Protobuf等；原生序列化性能差、有远程代码执行安全漏洞，线上RPC一般使用 Kryo、Protobuf。