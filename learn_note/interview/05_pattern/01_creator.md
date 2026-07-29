## 一、单例模式定义
单例模式（Singleton Pattern）属于创建型设计模式。
保证一个类在整个程序进程中，有且仅有一个实例对象，对外提供全局统一获取该实例的入口；节约频繁创建销毁对象的开销，适合工具类、连接客户端、配置管理器。
核心特点： 私有构造方法，禁止外部new实例； 类内自行创建唯一实例；
对外提供公共静态方法获取这个实例。
### 二、4 种主流 Java 实现（后端面试重点）
1. 饿汉式（最简单，天生线程安全，推荐简单工具使用）
   类加载直接初始化实例，类加载阶段就创建对象，不存在并发问题。 缺点：类只要被加载，不管用不用都创建，造成内存浪费。
```
public class HungrySingleton {
    // 类加载直接创建唯一实例
    private static final HungrySingleton INSTANCE = new HungrySingleton();
    
    // 私有构造，外部不能new
    private HungrySingleton() {}
    
    // 全局获取实例
    public static HungrySingleton getInstance() {
        return INSTANCE;
    }
}
```

2. 懒汉式（不加锁，线程不安全，禁止线上使用）
   调用时才创建，多线程并发会创建多个实例。仅作反面例子。
```
public class LazyBadSingleton {
    private static LazyBadSingleton instance;
    
    private LazyBadSingleton() {}
    
    // 多线程并发会创建多个对象
    public static LazyBadSingleton getInstance() {
        if (instance == null) {
            instance = new LazyBadSingleton();
        }
        return instance;
    }
}
```

3. 双重检查锁DCL（Double-Check Locking，业务最常用懒加载）
   延迟创建 + 线程安全，兼顾性能与懒加载，面试最高频。 必须加volatile：禁止指令重排，避免返回半初始化对象。
```
public class DCLSingleton {
    // volatile 防止指令重排
    private static volatile DCLSingleton instance;
    
    private DCLSingleton() {}
    
    public static DCLSingleton getInstance() {
        // 第一次判断：不为空直接返回，不用抢锁
        if (instance == null) {
            synchronized (DCLSingleton.class) {
                // 第二次判断：防止多线程等待锁重复创建
                if (instance == null) {
                    instance = new DCLSingleton();
                }
            }
        }
        return instance;
    }
}
```

4. 静态内部类（最优懒加载，写法干净，推荐）
   利用类加载机制天然保证线程安全，懒加载，无需锁、volatile。
   外部类加载不会初始化内部类，只有调用getInstance才加载，延迟创建。
```
public class InnerClassSingleton {

    private InnerClassSingleton() {}
    
    // 静态内部类
    private static class SingletonHolder {
        private static final InnerClassSingleton INSTANCE = new InnerClassSingleton();
    }
    
    public static InnerClassSingleton getInstance() {
        return SingletonHolder.INSTANCE;
    }
}
```
------------------------------------------------------------------------------
## 工厂模式 Factory Pattern（创建型 Creational）
工厂模式分为三类：简单工厂、工厂方法、抽象工厂，后端面试重点掌握前两个即可。
核心思想 把new对象的创建逻辑抽离到专门工厂类，调用方不用直接写new，解耦创建与使用。新增产品不用修改调用代码，符合开闭原则。
### 一、简单工厂（静态工厂，日常最常用）
不属于 GOF23 种标准工厂，但业务使用最多。 逻辑：一个工厂类，根据传入标识判断，返回不同实例。
```
public class MessageFactory {
    // 静态方法创建对象
    public static Message getMessage(String type) {
        if ("sms".equals(type)) {
            return new SmsMessage();
        } else if ("ding".equals(type)) {
            return new DingMessage();
        } else if ("email".equals(type)) {
            return new EmailMessage();
        }
        throw new RuntimeException("不支持的消息类型");
    }
}
```
### 二、工厂方法 Factory Method（标准 23 种设计模式）
规则：一个产品对应一个工厂。
```
public interface MessageFactory {
    Message createMessage();
}

#每种消息单独对应自己的工厂
// 短信工厂
public class SmsFactory implements MessageFactory {
    @Override
    public Message createMessage() {
        return new SmsMessage();
    }
}

// 钉钉工厂
public class DingFactory implements MessageFactory {
    @Override
    public Message createMessage() {
        return new DingMessage();
    }
}

public class Test {
    public static void main(String[] args) {
        MessageFactory factory = new EmailFactory();
        Message msg = factory.createMessage();
        msg.send("通知内容");
    }
}
```
优点：新增类型只加产品类 + 工厂类，不改原有代码，满足开闭原则；
缺点：类数量爆炸，业务繁琐。
