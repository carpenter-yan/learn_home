## 适配器模式 Adapter
1、核心概念
适配器属于结构型设计模式。作用：把一个类的接口转换成客户端期望的另一个接口，让原本因为接口不匹配无法协作的两个类可以一起工作。
生活类比：国标插座转万能转换插头、Type-C 转耳机圆孔。
分两类：
类适配器：通过继承实现，Java 是单继承，使用极少；
对象适配器（主流）：组合持有源对象，推荐业务全部用这种。

2、使用场景
已有旧接口无法修改，新系统要对接旧逻辑；做兼容、适配、接入第三方老旧 SDK。
经典框架落地：SpringMVC HandlerAdapter，适配多种 Controller。

3、对象适配器完整 Java 示例（最常用）
场景说明
老系统：只有OldCharge，提供chargeMicroUsb()安卓 Micro 接口充电。
新手机：只支持 Type-C 充电接口chargeTypeC()。
写适配器，把 MicroUSB 接口适配成 Type-C，新手机正常充电。
步骤 1：目标接口（客户端想要的接口 TypeC）
```
// 目标：新手机需要Type-C充电接口
public interface TypeC {
    void chargeTypeC();
}
```
步骤 2：被适配者（旧的、不兼容的类 MicroUSB）
```
// 老旧充电线：只有Micro USB方法
public class OldMicroUsbCharge {
    public void chargeMicroUsb() {
        System.out.println("使用Micro USB接口充电");
    }
}
```

步骤 3：适配器（转接器，持有旧对象，实现目标接口）
```
//适配器：MicroUSB转TypeC
public class UsbAdapter implements TypeC {
    // 组合持有被适配对象（推荐，优于继承）
    private OldMicroUsbCharge oldCharge;
    
    public UsbAdapter(OldMicroUsbCharge oldCharge) {
        this.oldCharge = oldCharge;
    }

    // 对外暴露TypeC接口，内部调用旧Micro方法完成适配
    @Override
    public void chargeTypeC() {
        System.out.println("适配器转接：TypeC -> MicroUSB");
        oldCharge.chargeMicroUsb();
    }
}
```

步骤 4：客户端调用（新手机）
```
public class PhoneClient {
    public static void main(String[] args) {
    // 旧充电头
    OldMicroUsbCharge oldLine = new OldMicroUsbCharge();
    // 插上适配器
    TypeC adapter = new UsbAdapter(oldLine);
    // 手机只调用TypeC方法，完全不用关心底层是MicroUSB
    adapter.chargeTypeC();
    }
}
```
5、适配器和装饰器、代理区分（面试必问）
适配器：改接口，做兼容适配，接口前后不一样；
装饰器：接口完全不变，叠加新功能（IO流Buffered）；
代理：接口不变，管控访问（AOP、MyBatis 代理）。
6、框架经典落地举例
SpringMVC HandlerAdapter：适配多种Controller（普通 Controller、HttpRequestHandler），DispatcherServlet 不用改代码；
Spring ResourceLoader适配多种资源路径；
Redis客户端适配不同连接工具；
新旧支付渠道接口兼容适配。
7、面试一句话总结
适配器通过组合/继承转换接口，解决接口不兼容问题，不用修改原有旧代码实现兼容，项目对接老旧接口、第三方 SDK 高频使用。
日常开发优先对象适配器，不用类适配器。
------------------------------------------------------------------------------
## 装饰器模式（Decorator）
### 一、定义
装饰器属于结构型设计模式。动态地给对象增加额外功能，不用修改原类代码，支持多层灵活叠加增强；原有接口完全不变。
核心特点：被装饰类、所有装饰器实现同一个顶层接口；装饰器内部持有原对象，包装调用。
适用场景：功能分层叠加，比如日志、缓存、加校验、IO 缓冲，不想写一堆子类。
### 二、经典场景
Java IO：FileInputStream原生读文件；BufferedInputStream装饰加缓冲；DataInputStream装饰加类型读取，多层随意组合。
业务：接口原有查询逻辑，可灵活加「日志打印、超时统计、权限校验、限流」。
### 三、极简代码案例：咖啡加料
需求：基础咖啡，可以额外加牛奶、加糖，自由搭配，不用新建大量子类。
1. 顶层统一接口（所有咖啡、装饰器都实现该接口）
```
public interface Coffee {
    // 获取价格
    double getPrice();
    // 获取描述
    String getDesc();
}
```


2. 被装饰原始对象：基础黑咖啡
```
// 原生基础咖啡
public class OriginalCoffee implements Coffee {
    @Override
    public double getPrice() {
        return 10.0;
    }
    
    @Override
    public String getDesc() {
        return "原味黑咖啡";
    }
}
```


3. 通用装饰器父类（所有装饰器继承它）
   持有 Coffee 对象，统一包装
```
public abstract class CoffeeDecorator implements Coffee {
    protected Coffee coffee;
    
    public CoffeeDecorator(Coffee coffee) {
        this.coffee = coffee;
    }
}
```


4. 具体装饰器 1：加牛奶
```
public class MilkDecorator extends CoffeeDecorator {
    public MilkDecorator(Coffee coffee) {
        super(coffee);
    }
    
    @Override
    public double getPrice() {
        // 原有价格 + 牛奶3元
        return coffee.getPrice() + 3;
    }
    
    @Override
    public String getDesc() {
        return coffee.getDesc() + " + 牛奶";
    }
}
```


5. 具体装饰器 2：加糖
```
public class SugarDecorator extends CoffeeDecorator {
    public SugarDecorator(Coffee coffee) {
        super(coffee);
    }
    
    @Override
    public double getPrice() {
        return coffee.getPrice() + 1.5;
    }
    
    @Override
    public String getDesc() {
        return coffee.getDesc() + " + 白糖";
    }
}
```
6. 客户端测试，自由多层包装
```
public class Client {
    public static void main(String[] args) {
        // 1.纯原味咖啡
        Coffee coffee1 = new OriginalCoffee();
        System.out.println(coffee1.getDesc() + "，总价：" + coffee1.getPrice());
        
        // 2.咖啡 + 牛奶
        Coffee coffee2 = new MilkDecorator(new OriginalCoffee());
        System.out.println(coffee2.getDesc() + "，总价：" + coffee2.getPrice());
        
        // 3.咖啡 + 牛奶 + 糖（多层嵌套装饰，灵活叠加）
        Coffee coffee3 = new SugarDecorator(new MilkDecorator(new OriginalCoffee()));
        System.out.println(coffee3.getDesc() + "，总价：" + coffee3.getPrice());
    }
}
```
### 五、优缺点
优点
拓展灵活，功能任意排列组合；不用创建爆炸式子类；
开闭原则，新增功能不加改原有业务代码。
缺点
多层嵌套过多，代码可读性下降。
### 六、面试必背区分
装饰器：接口不变，叠加功能；多层包装；
适配器：接口改造，用来兼容；
代理：侧重管控访问（权限、事务）；装饰侧重功能增强。
七、框架落地
Java IO全系列：BufferedInputStream、LineNumberReader；层层装饰增强；
Spring Cache多层缓存装饰；
Sentinel包装RestTemplate 增加限流熔断，属于装饰思想。