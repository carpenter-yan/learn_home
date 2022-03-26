# Java基础
<!-- GFM-TOC -->
* [Java 基础](#java-基础)
    * [一、数据类型](#一数据类型)
        * [基本类型](#基本类型)
        * [包装类型](#包装类型)
        * [缓冲池](#缓冲池)
        * [BigDecimal](#BigDecimal)
    * [二、String](#二string)
        * [不可变的好处](#不可变的好处)
        * [String Constant Pool](#string-constant-pool)
        * [String StringBuffer and StringBuilder](#string-stringbuffer-and-stringbuilder)
    * [三、运算](#三运算)
        * [参数传递](#参数传递)
        * [float 与 double](#float-与-double)
        * [隐式类型转换](#隐式类型转换)
        * [switch](#switch)
    * [四、关键字](#四关键字)
        * [final](#final)
        * [static](#static)
        * [native](#native)
        * [instance of](#instance-of)
    * [五、Object 通用方法](#五object-通用方法)
        * [equals()](#equals)
        * [hashCode()](#hashcode)
        * [toString()](#tostring)
        * [clone()](#clone)
        * [wait notify notifyAll](#wait-notify-notifyall)
    * [六、继承](#六继承)
        * [访问权限](#访问权限)
        * [抽象类与接口](#抽象类与接口)
        * [super](#super)
        * [重写与重载](#重写与重载)
    * [七、反射](#七反射)
        * [class对象获取方法](#class对象获取方法)
        * [创建对象的几种方式](#创建对象的几种方式)
    * [八、异常](#八异常)
    * [九、泛型](#九泛型)
    * [十、注解](#十注解)
    * [十一、枚举类](#十一枚举类)
    * [十二、特性](#十一特性)
        * [Java 各版本的新特性](#java-各版本的新特性)
        * [Java 与 C++ 的区别](#java-与-c-的区别)
        * [JRE or JDK](#jre-or-jdk)
    * [参考资料](#参考资料)
<!-- GFM-TOC -->

## 一、数据类型

### 基本类型

八大基本数据类型
- byte/8(-128~127)
- short/16(-32768~32767)
- int/32
- long/64
- float/32
- double/64
- char/16
- boolean/~
  
boolean只有true或false，可以使用1bit来存储，但具体大小没有明确规定。
JVM在编译时期将boolean类型数据转换为int，但boolean数组却是通过byte数组来实现

short和char: 都占用4个字节，但short是对数值编码，首位为符号位。char是对字符编码，无符号位(0~65535)

基本数据类型转换关系：byte→short(char)→int→long→float→double

- [Primitive Data Types](https://docs.oracle.com/javase/tutorial/java/nutsandbolts/datatypes.html)
- [The Java® Virtual Machine Specification](https://docs.oracle.com/javase/specs/jvms/se8/jvms8.pdf)

### 包装类型

包装类有以下用途

- 集合不允许存放基本数据类型，故常用包装类
- 作为基本数据类型对应的类类型，提供了一系列实用的对象操作，如类型转换，进制转换等
- 包含了每种基本类型的相关属性，如最大值，最小值，所占位数等

> 包装类都为final 不可继承 \
> 包装类型都继承了Number抽象类

```
Integer x = 2;     // 装箱 调用了 Integer.valueOf(2)
int y = x;         // 拆箱 调用了 X.intValue()
```

new Integer(123) 与 Integer.valueOf(123) 的区别在于：

- new Integer(123) 每次都会新建一个对象；
- Integer.valueOf(123) 会使用缓存池中的对象，多次调用会取得同一个对象的引用。

> valueOf() 方法的实现比较简单，先判断值是否在缓存池中，如果在则返回缓存池中的实例。

- [Autoboxing and Unboxing](https://docs.oracle.com/javase/tutorial/java/data/autoboxing.html)
- [StackOverflow : Differences between new Integer(123), Integer.valueOf(123) and just 123
](https://stackoverflow.com/questions/9030817/differences-between-new-integer123-integer-valueof123-and-just-123)
  
### 缓冲池

包装类型内存使用 private static class IntegerCache，声明一个内部使用的缓存池
> 如Integer中有个静态内部类IntegerCache，里面有个cache[],也就是Integer常量池，常量池的大小为一个字节（-128~127）\
> 缓冲池可以减少大量装箱时频繁新建对象  
> 为啥把缓存设置为[-128，127]区间？性能和资源之间的权衡。

在 jdk 1.8 所有的数值类缓冲池中，Integer 的缓冲池 IntegerCache 很特殊，这个缓冲池的下界是 - 128，上界默认是 127，但是这个上界是可调的，在启动 jvm 的时候，通过 `-XX:AutoBoxCacheMax=<size>` 来指定这个缓冲池的大小。

基本类型对应的缓冲池如下：
- boolean values: true and false
- all byte values
- short values: between -128 and 127
- int values: between -128 and 127
- char: in the range \u0000 to \u007F

### BigDecimal

BigDecimal 主要用于处理解决精度丢失问题
> float和double类型主要是为了科学计算和工程计算而设计的。在广泛的数字范围上提供较为精确的快速近似计算而精心设计的。然而，它们并没有提供完全精确的结果

```java
class demo {
    public static void main(String[] args) {
        float a = 1.0f - 0.9f;
        float b = 0.9f - 0.8f;
        System.out.println(a);// 0.100000024
        System.out.println(b);// 0.099999964
        System.out.println(a == b);// false
    }
}
```

BigDecimal保持精度的原理是内部记录有效位数和小数点后位数，将小数转化为BigInteger计算再按小数位转换回小数  

- [BigDecimal的浮点数运算能保证精度的原理](https://zhuanlan.zhihu.com/p/71796835)  

BigInteger的原理是将大数转换为int数组再按位运算  

[BACK TO TOP](#Java基础)

## 二、String

String被声明为final，因此它不可被继承。 在Java8中，String内部使用char数组存储数据。

```java
public final class String
    implements java.io.Serializable, Comparable<String>, CharSequence {
    /** The value is used for character storage. */
    private final char value[];
}
```

在Java9之后，String类的实现改用byte数组存储字符串，同时使用`coder`来标识使用了哪种编码。

```java
public final class String
    implements java.io.Serializable, Comparable<String>, CharSequence {
    /** The value is used for character storage. */
    private final byte[] value;

    /** The identifier of the encoding used to encode the bytes in {@code value}. */
    private final byte coder;
}
```

value数组被声明为final，初始化之后就不能再引用其它数组。并且String内部没有改变value数组的方法，因此可以保证String不可变。

### 不可变的好处

- **可以缓存hash值**  
String的hash值经常被使用，如String用做HashMap的key。不可变性使得hash值也不可变，因此只需要进行一次计算。  

- **String Pool的需要**  
一个String对象已经被创建过了，就会从String Pool中取得引用。只有String是不可变的，才能使用String Pool。  

- **安全性**  
String 经常作为参数，String不可变性可以保证参数不可变。如网络传输  

- **线程安全**  
String 不可变性天生具备线程安全，可以在多个线程中安全地使用。  

### String Constant Pool

1. 字符串常量池的定义和使用  
字符串常量池（String Pool）是JVM为了最小化在堆上存储具有重复字符串对象所造成的冗余和内存浪费而在留出一个特殊区域。

- 不使用new关键字创建的字符串对象存储在堆的**字符串常量池**部分
- 还可以使用String的intern()方法在运行过程将字符串添加到**字符串常量池**中
- 使用new关键字创建的字符串对象存储在堆的**普通内存**部分

在Java7之前，String Pool被放在运行时常量池中，它属于永久代。而在Java7，String Pool被移到堆中。
这是因为永久代的空间有限，在大量使用字符串的场景下会导致OutOfMemoryError错误。

2. String对象创建问题
> new String("abc")创建两String对象。(前提是String Pool 中还没有 "abc" 字符串对象)。
> "abc" 属于字符串字面量，因此编译时期会在 String Pool 中创建一个字符串对象，指向这个 "abc" 字符串字面量。
> 而使用 new 的方式会在堆中创建一个字符串对象。

```java
class demo{
    public static void main(String[] args){
        String s1 = "abc";
        String s2 = "abc";
        String s3 = new String("abc");
        System.out.println(s1 == s2);// true
        System.out.println(s1 == s3);// false

        String a = "hello2";
        String b = "hello";
        String c = b + 2;
        System.out.println((a == c));//输出结果为:false。由于有符号引用的存在，所以  String c = b + 2;不会在编译期间被优化，不会把b+2当做字面常量来处理的

        String a = "hello2";
        final String b = "hello";
        String c = b + 2;
        System.out.println((a == c));//输出结果为：true。对于被final修饰的变量，会在class文件常量池中保存一个副本，也就是说不会通过连接而进行访问
    }
}
```

以下是 String构造函数的源码，可以看到，在将一个字符串对象作为另一个字符串对象的构造函数参数时，并不会完全复制value数组内容，而是都会指向同一个value数组。

```java
class String{
    public String(String original) {
        this.value = original.value;
        this.hash = original.hash;
    }
}
```

3. String.intern()方法

String.intern()是一个native的方法。通过分析OpenJDK7源码，它的大体实现结构就是:JAVA使用jni调用c++实现的StringTable的intern方法, 
StringTable的intern方法跟Java中的HashMap的实现是差不多的, 只是不能自动扩容。默认大小是1009。

要注意的是，String的String Pool是一个固定大小的Hashtable，默认值大小长度是1009，如果放进String Pool的String非常多，
就会造成Hash冲突严重，从而导致链表会很长，而链表长了后直接会造成的影响就是当调用String.intern时性能会大幅下降。

在jdk6中StringTable是固定的，就是1009的长度，所以如果常量池中的字符串过多就会导致效率下降很快。
在jdk7中，StringTable的长度可以通过一个参数指定：

> -XX:StringTableSize=99991

- new String都是在堆上创建字符串对象。当调用intern()方法时，编译器会将字符串添加到常量池中（stringTable维护），并返回指向该常量的引用。
- 通过字面量赋值创建字符串（如：String str=”twm”）时，会先在常量池中查找是否存在相同的字符串，
  若存在，则将栈中的引用直接指向该字符串；若不存在，则在常量池中生成一个字符串，再将栈中的引用指向该字符串。
- 常量字符串的“+”操作，编译阶段直接会合成为一个字符串。如string str=”JA”+”VA”，在编译阶段会直接合并成语句String str=”JAVA”，
  于是会去常量池中查找是否存在”JAVA”,从而进行创建或引用。 
- 对于final字段，编译期直接进行了常量替换（而对于非final字段则是在运行期进行赋值处理的）。
- 常量字符串和变量拼接时（如：String str3=baseStr + “01”;）会调用stringBuilder.append()在堆上创建新的对象。
- JDK7后，intern方法还是会先去查询常量池中是否有已经存在，如果存在，则返回常量池中的引用，这一点与之前没有区别，
  区别在于，如果在常量池找不到对应的字符串，则不会再将字符串拷贝到常量池，而只是在常量池中生成一个对原字符串的引用。
  简单的说，就是往常量池放的东西变了：原来在常量池中找不到时，复制一个副本放到常量池，1.7后则是将在堆上的地址引用复制到常量池。

举例说明：

```java
class demo{
    public static void main(String[] args){
        String str2 = new String("str") + new String("01");
        str2.intern();//常量池中保存的是"str01"的应用，即str2
        String str1 = "str01";
        System.out.println(str1 == str2);//true
    }
}
```

在JDK 1.7下，当执行str2.intern();时，因为常量池中没有“str01”这个字符串，所以会在常量池中生成一个对堆中的“str01”的引用
(注意这里是引用 ，就是这个区别于JDK 1.6的地方。在JDK1.6下是生成原字符串的拷贝)，而在进行String str1 = “str01”;
字面量赋值的时候，常量池中已经存在一个引用，所以直接返回了该引用，因此str1和str2都指向堆中的同一个字符串，返回true。

```java
class demo{
    public static void main(String[] args){
        String str2 = new String("str") + new String("01");
        String str1 = "str01";
        str2.intern();//常量池中已经存在"str01"，intern方法会返回常量池"str01"的应用，但这里没有对返回值重新赋值到str2，所以str2仍然指向普通堆中的"str01"
        System.out.println(str1 == str2);//false
        str2=str2.intern();
        System.out.println(str1 == str2);//true
    }
}
```

将中间两行调换位置以后，因为在进行字面量赋值（String str1 = “str01″）的时候，常量池中不存在，所以str1指向的常量池中的位置，
而str2指向的是堆中的对象，再进行intern方法时，对str1和str2已经没有影响了，所以返回false。

- [字符串常量池String Constant Pool](https://www.cnblogs.com/LinQingYang/p/12524949.html#importantPointsToRememberLabel)
- [StackOverflow : What is String interning?](https://stackoverflow.com/questions/10578984/what-is-string-interning)
- [深入解析 String#intern](https://tech.meituan.com/in_depth_understanding_string_intern.html)


### String StringBuffer and StringBuilder

**1. 可变性**

- String 不可变
- StringBuffer 和 StringBuilder 可变

**2. 线程安全**

- String 不可变，因此是线程安全的
- StringBuilder 不是线程安全的
- StringBuffer 是线程安全的，内部使用 synchronized 进行同步

**3. 执行效率**
StringBuilder > StringBuffer > String。这个实验结果是相对而言的，不一定在所有情况下都是这样。
> 比如String str = "hello"+ "world"的效率就比 StringBuilder st = new StringBuilder().append("hello").append("world")要高。

**4. 使用的总结**
- 操作少量的数据: 适用String
- 单线程操作字符串缓冲区下操作大量数据: 适用StringBuilder
- 多线程操作字符串缓冲区下操作大量数据: 适用StringBuffer

- [StackOverflow : String, StringBuffer, and StringBuilder](https://stackoverflow.com/questions/2971315/string-stringbuffer-and-stringbuilder)

[BACK TO TOP](#Java基础)

## 三、运算

### 参数传递

1. 按值调用(call by value)表示方法接收的是调用者提供的值，
2. 而按引用调用（call by reference)表示方法接收的是调用者提供的变量地址。
3. 方法体传递参数时，无论是值还是对象都是“值”传递。引用类型传递的是引用变量的地址。

```java
class demo {
    public static void main(String[] args) {
        Student s1 = new Student("小张");
        Student s2 = new Student("小李");
        Test.swap(s1, s2);
        System.out.println("s1:" + s1.getName());//s1:小张
        System.out.println("s2:" + s2.getName());//s2:小李
    }

    public static void swap(Student x, Student y) {
        Student temp = x;
        x = y;
        y = temp;
        System.out.println("x:" + x.getName());//x:小李
        System.out.println("y:" + y.getName());//y:小张
    }
}
```

[StackOverflow: Is Java “pass-by-reference” or “pass-by-value”?](https://stackoverflow.com/questions/40480/is-java-pass-by-reference-or-pass-by-value)

### float 与 double

Java 不能隐式执行向下转型，因为这会使得精度降低。

1.1 字面量属于 double 类型，不能直接将 1.1 直接赋值给 float 变量，因为这是向下转型。

```java
// float f = 1.1;
```

1.1f 字面量才是 float 类型。

```
float f = 1.1f;
```

### 隐式类型转换

因为字面量 1 是 int 类型，它比 short 类型精度要高，因此不能隐式地将 int 类型向下转型为 short 类型。

```
short s1 = 1;
// s1 = s1 + 1;
```

但是使用 += 或者 ++ 运算符会执行隐式类型转换。

```
s1 += 1;
s1++;
```

上面的语句相当于将 s1 + 1 的计算结果进行了向下转型：

```
s1 = (short) (s1 + 1);
```

[StackOverflow : Why don't Java's +=, -=, *=, /= compound assignment operators require casting?](https://stackoverflow.com/questions/8710619/why-dont-javas-compound-assignment-operators-require-casting)

### switch

从 Java 7 开始，可以在 switch 条件判断语句中使用 String 对象。

```
String s = "a";
switch (s) {
    case "a":
        System.out.println("aaa");
        break;
    case "b":
        System.out.println("bbb");
        break;
}
```

switch 不支持 long、float、double，是因为 switch 的设计初衷是对那些只有少数几个值的类型进行等值判断，如果值过于复杂，那么还是用 if 比较合适。

```java
// long x = 111;
// switch (x) { // Incompatible types. Found: 'long', required: 'char, byte, short, int, Character, Byte, Short, Integer, String, or an enum'
//     case 111:
//         System.out.println(111);
//         break;
//     case 222:
//         System.out.println(222);
//         break;
// }
```

[StackOverflow : Why can't your switch statement data type be long, Java?](https://stackoverflow.com/questions/2676210/why-cant-your-switch-statement-data-type-be-long-java)

[BACK TO TOP](#Java基础)

## 四、关键字

### final

**1. 数据**

声明数据为常量，可以是编译时常量，也可以是在运行时被初始化后不能被改变的常量。
>对于基本类型，final使数值不变；
>对于引用类型，final使引用不变，也就不能引用其它对象，但是被引用的对象本身是可以修改的。

```
final int x = 1;
// x = 2;  // cannot assign value to final variable 'x'
final A y = new A();
y.a = 1;
```

**2. 方法**

声明方法不能被子类重写。

private方法隐式地被指定为final，如果在子类中定义的方法和基类中的一个private方法签名相同，
此时子类的方法不是重写基类方法，而是在子类中定义了一个新的方法。

**3. 类**

声明类不允许被继承。

### static

**1. 静态变量**

- 静态变量：又称为类变量，也就是说这个变量属于类的，类所有的实例都共享静态变量，可以直接通过类名来访问它。静态变量在内存中只存在一份。
- 实例变量：每创建一个实例就会产生一个实例变量，它与该实例同生共死。

```java
public class A {

    private int x;         // 实例变量
    private static int y;  // 静态变量

    public static void main(String[] args) {
        // int x = A.x;  // Non-static field 'x' cannot be referenced from a static context
        A a = new A();
        int x = a.x;
        int y = A.y;
    }
}
```

**2. 静态方法**

静态方法在类加载的时候就存在了，它不依赖于任何实例。所以静态方法必须有实现，也就是说它不能是抽象方法。

```java
public abstract class A {
    public static void func1(){
    }
    // public abstract static void func2();  // Illegal combination of modifiers: 'abstract' and 'static'
}
```

只能访问所属类的静态字段和静态方法，方法中不能有 this 和 super 关键字，因为这两个关键字与具体对象关联。

```java
public class A {

    private static int x;
    private int y;

    public static void func1(){
        int a = x;
        // int b = y;  // Non-static field 'y' cannot be referenced from a static context
        // int b = this.y;     // 'A.this' cannot be referenced from a static context
    }
}
```

**3. 静态语句块**

静态语句块在类初始化时运行一次。

```java
public class A {
    static {
        System.out.println("123");
    }

    public static void main(String[] args) {
        A a1 = new A();
        A a2 = new A();
    }
}
```

```html
123
```

**4. 静态内部类**

非静态内部类依赖于外部类的实例，也就是说需要先创建外部类实例，才能用这个实例去创建非静态内部类。而静态内部类不需要。

```java
public class OuterClass {

    class InnerClass {
    }

    static class StaticInnerClass {
    }

    public static void main(String[] args) {
        // InnerClass innerClass = new InnerClass(); // 'OuterClass.this' cannot be referenced from a static context
        OuterClass outerClass = new OuterClass();
        InnerClass innerClass = outerClass.new InnerClass();
        StaticInnerClass staticInnerClass = new StaticInnerClass();
    }
}
```

静态内部类不能访问外部类的非静态的变量和方法。

**5. 静态导包**

在使用静态变量和方法时不用再指明 ClassName，从而简化代码，但可读性大大降低。

```
import static com.xxx.ClassName.*
```

**6. 初始化顺序**

静态变量和静态语句块优先于实例变量和普通语句块，静态变量和静态语句块的初始化顺序取决于它们在代码中的顺序。最后才是构造函数的初始化。

```java
class Demo {
    public static String staticField = "静态变量";

    static {
        System.out.println("静态语句块");
    }

    public String field = "实例变量";

    {
        System.out.println("普通语句块");
    }

    public Demo() {
        System.out.println("构造函数");
    }
}
```

存在继承的情况下，初始化顺序为：

- 父类（静态变量、静态语句块）
- 子类（静态变量、静态语句块）
- 父类（实例变量、普通语句块）
- 父类（构造函数）
- 子类（实例变量、普通语句块）
- 子类（构造函数）

### native

- [Java关键字(二)——native](https://www.cnblogs.com/ysocean/p/8476933.html)

### instance of

- [Java关键字(一)——instanceof](https://www.cnblogs.com/ysocean/p/8486500.html)

[BACK TO TOP](#Java基础)

## 五、Object 通用方法

```java
class Object{
    public native int hashCode();

    public boolean equals(Object obj);

    protected native Object clone() throws CloneNotSupportedException;

    public String toString();

    public final native Class<?> getClass();

    protected void finalize() throws Throwable {}

    public final native void notify();

    public final native void notifyAll();

    public final native void wait(long timeout) throws InterruptedException;

    public final void wait(long timeout, int nanos) throws InterruptedException;

    public final void wait() throws InterruptedException;
}

```

### equals()

**1. 等价关系**

两个对象具有等价关系，需要满足以下五个条件：

Ⅰ 自反性

```
x.equals(x); // true
```

Ⅱ 对称性

```
x.equals(y) == y.equals(x); // true
```

Ⅲ 传递性

```
if (x.equals(y) && y.equals(z))
    x.equals(z); // true;
```

Ⅳ 一致性

多次调用 equals() 方法结果不变

```
x.equals(y) == x.equals(y); // true
```

Ⅴ 与 null 的比较

对任何不是 null 的对象 x 调用 x.equals(null) 结果都为 false

```
x.equals(null); // false;
```

**2. 等价与相等**

- 对于基本类型，== 判断两个值是否相等，基本类型没有 equals() 方法。
- 对于引用类型，== 判断两个变量是否引用同一个对象，而 equals() 判断引用的对象是否等价。

```
Integer x = new Integer(1);
Integer y = new Integer(1);
System.out.println(x.equals(y)); // true
System.out.println(x == y);      // false
```

**3. 实现**

- 检查是否为同一个对象的引用，如果是直接返回 true；
- 检查是否是同一个类型，如果不是，直接返回 false；
- 将 Object 对象进行转型；
- 判断每个关键域是否相等。

```java
public class EqualExample {

    private int x;
    private int y;
    private int z;

    public EqualExample(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EqualExample that = (EqualExample) o;

        if (x != that.x) return false;
        if (y != that.y) return false;
        return z == that.z;
    }
}
```

### hashCode()

hashCode() 返回哈希值，而 equals() 是用来判断两个对象是否等价。等价的两个对象散列值一定相同，
但是散列值相同的两个对象不一定等价，这是因为计算哈希值具有随机性，两个值不同的对象可能计算出相同的哈希值。

在覆盖 equals() 方法时应当总是覆盖 hashCode() 方法，保证等价的两个对象哈希值也相等。

HashSet和HashMap等集合类使用了hashCode()方法来计算对象应该存储的位置，因此要将对象添加到这些集合类中，需要让对应的类实现 hashCode()方法。

下面的代码中，新建了两个等价的对象，并将它们添加到 HashSet 中。我们希望将这两个对象当成一样的，只在集合中添加一个对象。
但是 EqualExample 没有实现 hashCode() 方法，因此这两个对象的哈希值是不同的，最终导致集合添加了两个等价的对象。

```java
class EqualExample {
    public static void main(String[] args) {
        EqualExample e1 = new EqualExample(1, 1, 1);
        EqualExample e2 = new EqualExample(1, 1, 1);
        System.out.println(e1.equals(e2)); // true
        HashSet<EqualExample> set = new HashSet<>();
        set.add(e1);
        set.add(e2);
        System.out.println(set.size());   // 2
    }
}
```

理想的哈希函数应当具有均匀性，即不相等的对象应当均匀分布到所有可能的哈希值上。这就要求了哈希函数要把所有域的值都考虑进来。
可以将每个域都当成 R 进制的某一位，然后组成一个 R 进制的整数。

R 一般取 31，因为它是一个奇素数，如果是偶数的话，当出现乘法溢出，信息就会丢失，因为与 2 相乘相当于向左移一位，最左边的位丢失。
并且一个数与 31 相乘可以转换成移位和减法：`31*x == (x<<5)-x`，编译器会自动进行这个优化。

```
@Override
public int hashCode() {
    int result = 17;
    result = 31 * result + x;
    result = 31 * result + y;
    result = 31 * result + z;
    return result;
}
```

### toString()

默认返回 ToStringExample@4554617c 这种形式，其中@后面的数值为散列码的无符号十六进制表示。

```java
public class ToStringExample {

    private int number;

    public ToStringExample(int number) {
        this.number = number;
    }
}
```

```
ToStringExample example = new ToStringExample(123);
System.out.println(example.toString());
```

```html
ToStringExample@4554617c
```

### clone()

**1. cloneable**

clone() 是Object的protected方法，它不是public，一个类不显式去重写clone()，其它类就不能直接去调用该类实例的clone()方法。

```java
public class CloneExample {
    private int a;
    private int b;
}
```

```
CloneExample e1 = new CloneExample();
// CloneExample e2 = e1.clone(); // 'clone()' has protected access in 'java.lang.Object'
```

重写 clone() 得到以下实现：

```java
public class CloneExample {
    private int a;
    private int b;

    @Override
    public CloneExample clone() throws CloneNotSupportedException {
        return (CloneExample)super.clone();
    }

    public static void main(String[] args) {
        CloneExample e1 = new CloneExample();
        try {
            CloneExample e2 = e1.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
    }
}
```

```html
java.lang.CloneNotSupportedException: CloneExample
```

以上抛出了 CloneNotSupportedException，这是因为CloneExample没有实现Cloneable接口。

应该注意的是，clone()方法并不是Cloneable接口的方法，而是Object的一个 protected方法。
Cloneable 接口只是规定，如果一个类没有实现Cloneable 接口又调用了 clone()方法，就会抛出 CloneNotSupportedException。

```java
public class CloneExample implements Cloneable {
    private int a;
    private int b;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
```

**2. 浅拷贝**

创建一个新对象，然后将当前对象的非静态字段复制到该新对象，如果字段是值类型的，那么对该字段执行复制；
如果该字段是引用类型的话，则复制引用但不复制引用的对象。因此，原始对象及其副本引用同一个对象。

```java
public class ShallowCloneExample implements Cloneable {

    private int[] arr;

    public ShallowCloneExample() {
        arr = new int[10];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = i;
        }
    }

    public void set(int index, int value) {
        arr[index] = value;
    }

    public int get(int index) {
        return arr[index];
    }

    @Override
    protected ShallowCloneExample clone() throws CloneNotSupportedException {
        return (ShallowCloneExample) super.clone();
    }

    public static void main(String[] args) {
        ShallowCloneExample e1 = new ShallowCloneExample();
        ShallowCloneExample e2 = null;
        try {
            e2 = e1.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        e1.set(2, 222);
        System.out.println(e2.get(2)); // 222
    }
}
```

**3. 深拷贝**

创建一个新对象，然后将当前对象的非静态字段复制到该新对象，无论该字段是值类型的还是引用类型，都复制独立的一份。
当你修改其中一个对象的任何内容时，都不会影响另一个对象的内容。

```java
public class DeepCloneExample implements Cloneable {

    private int[] arr;

    public DeepCloneExample() {
        arr = new int[10];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = i;
        }
    }

    public void set(int index, int value) {
        arr[index] = value;
    }

    public int get(int index) {
        return arr[index];
    }

    @Override
    protected DeepCloneExample clone() throws CloneNotSupportedException {
        DeepCloneExample result = (DeepCloneExample) super.clone();
        result.arr = new int[arr.length];
        for (int i = 0; i < arr.length; i++) {
            result.arr[i] = arr[i];
        }
        return result;
    }

    public static void main(String[] args) {
        DeepCloneExample e1 = new DeepCloneExample();
        DeepCloneExample e2 = null;
        try {
            e2 = e1.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        e1.set(2, 222);
        System.out.println(e2.get(2)); // 2
    }
}
```

**4. 如何实现深拷贝**

1. 让每个引用类型属性内部都重写clone()方法。但是这种做法有个弊端，如果引用类型嵌套太深，那么代码量显然会很大，所以这种方法不太合适。

2. 利用序列化。序列化是将对象写到流中便于传输，而反序列化则是把对象从流中读取出来。这里写到流中的对象则是原始对象的一个拷贝，
   因为原始对象还存在JVM中， 所以我们可以利用对象的序列化产生克隆对象，然后通过反序列化获取这个对象。

> 注意每个需要序列化的类都要实现 Serializable接口，如果有某个属性不需要序列化，可以将其声明为transient，即将其排除在克隆属性之外。
> String类型属于深拷贝。

- [谈谈java里的深拷贝和浅拷贝](https://blog.csdn.net/ailiwanzi/article/details/88751250)
- [Java的深拷贝和浅拷贝](https://www.cnblogs.com/ysocean/p/8482979.html)

### wait notify notifyAll

如果当前线程没有获得一个对象的监听器，调用该方法就会抛出一个IllegalMonitorStateException
```
{
   Test test = new Test();
   //  直接调用报错
   //   test.wait();
   synchronized (test) {
      test.wait();
   }
}
// synchronized 方法不抛异常
synchronized void test() throws InterruptedException {
     this.wait();
 }
```

获得当前对象的监听器的方式：
- 执行此对象的同步 (`Sychronized`) 实例方法
- 执行在此对象上进行同步的 synchronized 语句的方法
- 对于 Class 类型的对象，执行该类的同步静态方法

[BACK TO TOP](#Java基础)

## 六、继承

### 访问权限

Java 中有三个访问权限修饰符：private、protected 以及 public，如果不加访问修饰符，表示包级可见。

可以对类或类中的成员（字段和方法）加上访问修饰符。

- 类可见表示其它类可以用这个类创建实例对象。
- 成员可见表示其它类可以用这个类的实例对象访问到该成员；

protected 用于修饰成员，表示在继承体系中成员对于子类可见，但是这个访问修饰符对于类没有意义。

设计良好的模块会隐藏所有的实现细节，把它的 API 与它的实现清晰地隔离开来。模块之间只通过它们的 API 进行通信，一个模块不需要知道其他模块的内部工作情况，这个概念被称为信息隐藏或封装。因此访问权限应当尽可能地使每个类或者成员不被外界访问。

如果子类的方法重写了父类的方法，那么子类中该方法的访问级别不允许低于父类的访问级别。这是为了确保可以使用父类实例的地方都可以使用子类实例去代替，也就是确保满足里氏替换原则。

字段决不能是公有的，因为这么做的话就失去了对这个字段修改行为的控制，客户端可以对其随意修改。例如下面的例子中，AccessExample 拥有 id 公有字段，如果在某个时刻，我们想要使用 int 存储 id 字段，那么就需要修改所有的客户端代码。

```java
public class AccessExample {
    public String id;
}
```

可以使用公有的 getter 和 setter 方法来替换公有字段，这样的话就可以控制对字段的修改行为。

```java
public class AccessExample {

    private int id;

    public String getId() {
        return id + "";
    }

    public void setId(String id) {
        this.id = Integer.valueOf(id);
    }
}
```

但是也有例外，如果是包级私有的类或者私有的嵌套类，那么直接暴露成员不会有特别大的影响。

```java
public class AccessWithInnerClassExample {

    private class InnerClass {
        int x;
    }

    private InnerClass innerClass;

    public AccessWithInnerClassExample() {
        innerClass = new InnerClass();
    }

    public int getValue() {
        return innerClass.x;  // 直接访问
    }
}
```

### 抽象类与接口

抽象类和抽象方法都使用 abstract 关键字进行声明。如果一个类中包含抽象方法，那么这个类必须声明为抽象类。

抽象类和普通类最大的区别是，抽象类不能被实例化，只能被继承。

```java
public abstract class AbstractClassExample {

    protected int x;
    private int y;

    public abstract void func1();

    public void func2() {
        System.out.println("func2");
    }
}
```

```java
public class AbstractExtendClassExample extends AbstractClassExample {
    @Override
    public void func1() {
        System.out.println("func1");
    }
}
```

```
// AbstractClassExample ac1 = new AbstractClassExample(); // 'AbstractClassExample' is abstract; cannot be instantiated
AbstractClassExample ac2 = new AbstractExtendClassExample();
ac2.func1();
```

接口是抽象类的延伸，在 Java 8 之前，它可以看成是一个完全抽象的类，也就是说它不能有任何的方法实现。

从 Java 8 开始，接口也可以拥有默认的方法实现，这是因为不支持默认方法的接口的维护成本太高了。在 Java 8 之前，如果一个接口想要添加新的方法，那么要修改所有实现了该接口的类，让它们都实现新增的方法。

接口的成员（字段 + 方法）默认都是 public 的，并且不允许定义为 private 或者 protected。从 Java 9 开始，允许将方法定义为 private，这样就能定义某些复用的代码又不会把方法暴露出去。

接口的字段默认都是 static 和 final 的。

```java
public interface InterfaceExample {

    void func1();

    default void func2(){
        System.out.println("func2");
    }

    int x = 123;
    // int y;               // Variable 'y' might not have been initialized
    public int z = 0;       // Modifier 'public' is redundant for interface fields
    // private int k = 0;   // Modifier 'private' not allowed here
    // protected int l = 0; // Modifier 'protected' not allowed here
    // private void fun3(); // Modifier 'private' not allowed here
}
```

```java
public class InterfaceImplementExample implements InterfaceExample {
    @Override
    public void func1() {
        System.out.println("func1");
    }
}
```

```
// InterfaceExample ie1 = new InterfaceExample(); // 'InterfaceExample' is abstract; cannot be instantiated
InterfaceExample ie2 = new InterfaceImplementExample();
ie2.func1();
System.out.println(InterfaceExample.x);
```


**比较**

从设计层面上看
- 抽象类的实现目的，是代码复用，一种模板设计的方式，可以让这些类都派生于一个抽象类。
- 接口的设计目的，是对类的行为进行约束（更准确的说是一种“有”约束，因为接口不能规定类不可以有什么行为），也就是提供一种机制，可以强制要求不同的类具有相同的行为。

从使用上来看，一个类可以实现多个接口，但是不能继承多个抽象类。
- 接口的字段只能是 static 和 final 类型的，而抽象类的字段没有这种限制。
- 接口的成员只能是 public 的，而抽象类的成员可以有多种访问权限。

设计上对比：
- 抽象类： 拓展继承该抽象类的模块的类的行为功能（开放闭合原则）
- 接口：约束继承该接口的类行为（依赖倒置原则）

**使用选择**

使用接口：

- 需要让不相关的类都实现一个方法，例如不相关的类都可以实现 Comparable 接口中的 compareTo() 方法；
- 需要使用多重继承。

使用抽象类：

- 需要在几个相关的类中共享代码。
- 需要能控制继承来的成员的访问权限，而不是都为 public。
- 需要继承非静态和非常量字段。

在很多情况下，接口优先于抽象类。因为接口没有抽象类严格的类层次结构要求，可以灵活地为一个类添加行为。并且从 Java 8 开始，接口也可以有默认的方法实现，使得修改接口的成本也变的很低。

- [Abstract Methods and Classes](https://docs.oracle.com/javase/tutorial/java/IandI/abstract.html)
- [深入理解 abstract class 和 interface](https://www.ibm.com/developerworks/cn/java/l-javainterface-abstract/)
- [When to Use Abstract Class and Interface](https://dzone.com/articles/when-to-use-abstract-class-and-intreface)
- [Java 9 Private Methods in Interfaces](https://www.journaldev.com/12850/java-9-private-methods-interfaces)


### super

- 访问父类的构造函数：可以使用 super() 函数访问父类的构造函数，从而委托父类完成一些初始化的工作。应该注意到，子类一定会调用父类的构造函数来完成初始化工作，一般是调用父类的默认构造函数，如果子类需要调用父类其它构造函数，那么就可以使用 super() 函数。
- 访问父类的成员：如果子类重写了父类的某个方法，可以通过使用 super 关键字来引用父类的方法实现。
- 泛型中用于约束泛型的下界。如`< ? super Apple>`

```java
public class SuperExample {

    protected int x;
    protected int y;

    public SuperExample(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void func() {
        System.out.println("SuperExample.func()");
    }
}
```

```java
public class SuperExtendExample extends SuperExample {

    private int z;

    public SuperExtendExample(int x, int y, int z) {
        super(x, y);
        this.z = z;
    }

    @Override
    public void func() {
        super.func();
        System.out.println("SuperExtendExample.func()");
    }
}
```

```
SuperExample e = new SuperExtendExample(1, 2, 3);
e.func();
```

```html
SuperExample.func()
SuperExtendExample.func()
```

[Using the Keyword super](https://docs.oracle.com/javase/tutorial/java/IandI/super.html)

### 重写与重载

存在于继承体系中，指子类实现了一个与父类在方法声明上完全相同的一个方法。

为了满足里式替换原则，重写有以下三个限制：

- 子类方法的访问权限必须大于等于父类方法；
- 子类方法的返回类型必须是父类方法返回类型或为其子类型。
- 子类方法抛出的异常类型必须是父类抛出异常类型或为其子类型。

使用 @Override 注解，可以让编译器帮忙检查是否满足上面的三个限制条件。

下面的示例中，SubClass 为 SuperClass 的子类，SubClass 重写了 SuperClass 的 func() 方法。其中：

- 子类方法访问权限为 public，大于父类的 protected。
- 子类的返回类型为 ArrayList\<Integer\>，是父类返回类型 List\<Integer\> 的子类。
- 子类抛出的异常类型为 Exception，是父类抛出异常 Throwable 的子类。
- 子类重写方法使用 @Override 注解，从而让编译器自动检查是否满足限制条件。

```java
class SuperClass {
    protected List<Integer> func() throws Throwable {
        return new ArrayList<>();
    }
}

class SubClass extends SuperClass {
    @Override
    public ArrayList<Integer> func() throws Exception {
        return new ArrayList<>();
    }
}
```

在调用一个方法时，先从本类中查找看是否有对应的方法，如果没有再到父类中查看，看是否从父类继承来。否则就要对参数进行转型，转成父类之后看是否有对应的方法。总的来说，方法调用的优先级为：

- this.func(this)
- super.func(this)
- this.func(super)
- super.func(super)


```java
/*
    A
    |
    B
    |
    C
    |
    D
 */


class A {

    public void show(A obj) {
        System.out.println("A.show(A)");
    }

    public void show(C obj) {
        System.out.println("A.show(C)");
    }
}

class B extends A {

    @Override
    public void show(A obj) {
        System.out.println("B.show(A)");
    }
}

class C extends B {
}

class D extends C {
}
```

```
public static void main(String[] args) {

    A a = new A();
    B b = new B();
    C c = new C();
    D d = new D();

    // 在 A 中存在 show(A obj)，直接调用
    a.show(a); // A.show(A)
    // 在 A 中不存在 show(B obj)，将 B 转型成其父类 A
    a.show(b); // A.show(A)
    // 在 B 中存在从 A 继承来的 show(C obj)，直接调用
    b.show(c); // A.show(C)
    // 在 B 中不存在 show(D obj)，但是存在从 A 继承来的 show(C obj)，将 D 转型成其父类 C
    b.show(d); // A.show(C)

    // 引用的还是 B 对象，所以 ba 和 b 的调用结果一样
    A ba = new B();
    ba.show(c); // A.show(C)
    ba.show(d); // A.show(C)
}
```

存在于同一个类中，指一个方法与已经存在的方法名称上相同，但是参数类型、个数、顺序至少有一个不同。

应该注意的是，返回值不同，其它都相同不算是重载。

```java
class OverloadingExample {
    public void show(int x) {
        System.out.println(x);
    }

    public void show(int x, String y) {
        System.out.println(x + " " + y);
    }
    public static void main(String[] args) {
        OverloadingExample example = new OverloadingExample();
        example.show(1);
        example.show(1, "2");
    }
}
```

[BACK TO TOP](#Java基础)

## 七、反射

每个类都有一个   **Class**   对象，包含了与类有关的信息。当编译一个新类时，会产生一个同名的 .class 文件，该文件内容保存着 Class 对象。

类加载相当于 Class 对象的加载，类在第一次使用时才动态加载到 JVM 中。也可以使用 `Class.forName("com.mysql.jdbc.Driver")` 这种方式来控制类的加载，该方法会返回一个 Class 对象。

反射可以提供运行时的类信息，并且这个类可以在运行时才加载进来，甚至在编译时期该类的 .class 不存在也可以加载进来。

Class 和 java.lang.reflect 一起对反射提供了支持，java.lang.reflect 类库主要包含了以下三个类：

-  **Field**  ：可以使用 get() 和 set() 方法读取和修改 Field 对象关联的字段；
-  **Method**  ：可以使用 invoke() 方法调用与 Method 对象关联的方法；
-  **Constructor**  ：可以用 Constructor 的 newInstance() 创建新的对象。

**反射的优点：**

-  **可扩展性**   ：应用程序可以利用全限定名创建可扩展对象的实例，来使用来自外部的用户自定义类。
-  **类浏览器和可视化开发环境**   ：一个类浏览器需要可以枚举类的成员。可视化开发环境（如 IDE）可以从利用反射中可用的类型信息中受益，以帮助程序员编写正确的代码。
-  **调试器和测试工具**   ： 调试器需要能够检查一个类里的私有成员。测试工具可以利用反射来自动地调用类里定义的可被发现的 API 定义，以确保一组测试中有较高的代码覆盖率。

**反射的缺点：**

尽管反射非常强大，但也不能滥用。如果一个功能可以不用反射完成，那么最好就不用。在我们使用反射技术时，下面几条内容应该牢记于心。

-  **性能开销**   ：反射涉及了动态类型的解析，所以 JVM 无法对这些代码进行优化。因此，反射操作的效率要比那些非反射操作低得多。我们应该避免在经常被执行的代码或对性能要求很高的程序中使用反射。

-  **安全限制**   ：使用反射技术要求程序必须在一个没有安全限制的环境中运行。如果一个程序必须在有安全限制的环境中运行，如 Applet，那么这就是个问题了。

-  **内部暴露**   ：由于反射允许代码执行一些在正常情况下不被允许的操作（比如访问私有的属性和方法），所以使用反射可能会导致意料之外的副作用，这可能导致代码功能失调并破坏可移植性。反射代码破坏了抽象性，因此当平台发生改变的时候，代码的行为就有可能也随着变化。

- [Trail: The Reflection API](https://docs.oracle.com/javase/tutorial/reflect/index.html)
- [深入解析 Java 反射（1）- 基础](http://www.sczyh30.com/posts/Java/java-reflection-1/)

### class对象获取方法

**1.类名.class**

说明： JVM将使用类装载器, 将类装入内存(前提是:类还没有装入内存),不做类的初始化工作.返回Class的对象

**2.Class.forName()**

参数类名字符串是包名+类名。装入类,并做类的静态初始化，返回Class的对象

**3.实例对象.getClass()**

对类进行静态初始化、非静态初始化；返回引用o运行时真正所指的对象(因为:子对象的引用可能会赋给父对象的引用变量中)所属的类的Class的对象

```java
class TestClassType {

    //构造函数

    public TestClassType() {

        System.out.println("----构造函数---");

    }

    //静态的参数初始化

    static {

        System.out.println("---静态的参数初始化---");

    }

    //非静态的参数初始化

    {

        System.out.println("----非静态的参数初始化---");

    }
}

class TestClass {
    public  static void main(String[] args)
    {
        try {
        //测试.class
        Class testTypeClass=TestClassType.class;
        System.out.println("testTypeClass---"+testTypeClass);
        
        
        //测试Class.forName()
        Class testTypeForName=Class.forName("TestClassType");
        System.out.println("testTypeForName---"+testTypeForName);
        
        
        //测试Object.getClass()
        TestClassType testTypeGetClass= new TestClassType();
        System.out.println("testTypeGetClass---"+testTypeGetClass.getClass());
        
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
```

测试的结果如下：

```
testTypeClass---class TestClassType

---静态的参数初始化---
testTypeForName---class TestClassType

----非静态的参数初始化---
----构造函数---
testTypeGetClass---class TestClassType
```

根据结果可以发现，三种生成的Class对象一样的。并且程序只打印一次“静态的参数初始化”。

我们知道，静态的方法属性初始化，是在加载类的时候初始化。而非静态方法属性初始化，是new类实例对象的时候加载。

因此，这段程序说明，三种方式生成Class对象，其实只有一个Class对象。在生成Class对象的时候，首先判断内存中是否已经加载。

所以，生成Class对象的过程其实是如此的：

当我们编写一个新的java类时,JVM就会帮我们编译成class对象,存放在同名的.class文件中。 在运行时，当需要生成这个类的对象，
JVM就会检查此类是否已经装载内存中。若是没有装载，则把.class文件装入到内存中。若是装载，则根据class文件生成实例对象。

### 创建对象的几种方式

1. 通过new关键字

2. 通过Class类的newInstance()方法，这种默认是调用类的无参构造方法创建对象。

3. 通过Constructor类的newInstance方法，可以指定某个构造器来创建对象。

4. 利用Clone方法， Clone是Object 类中的一个方法，通过clone创建一个一模一样的对象。

5. 反序列化，调用ObjectInputStream类的readObject()方法。

[BACK TO TOP](#Java基础)

## 八、异常

Throwable 可以用来表示任何可以作为异常抛出的类，分为两种： Error 和 Exception。
> 其中 Error 用来表示 JVM 无法处理的错误，

Exception 分为两种：
1. 受检异常 ：需要用 try...catch... 语句捕获并进行处理，并且可以从异常中恢复；
2. 非受检异常 ：是程序运行时错误，例如除 0 会引发 Arithmetic Exception，此时程序崩溃并且无法恢复
3. 运行时异常（runtime exception）

RuntimeException是一种Unchecked
Exception，即表示编译器不会检查程序是否对RuntimeException作了处理，在程序中不必捕获RuntimException类型的异常，也不必在方法体声明抛出RuntimeException类。一般来说，RuntimeException发生的时候，表示程序中出现了编程错误，所以应该找出错误修改程序，而不是去捕获RuntimeException。
> 常见RuntimeException异常：NullPointException、ClassCastException、IllegalArgumentException、IndexOutOfBoundException

try语句return问题：**如果try语句里有return，返回的是try语句块中变量值**。详细执行过程如下：

1. 如果有返回值，就把返回值保存到局部变量中；
2. 执行jsr指令跳到finally语句里执行；
3. 执行完finally语句后，返回之前保存在局部变量表里的值。
4. 针对对象引用的返回，如果finally中有修改值，返回的是引用的对象。
   **如果try，finally语句里均有return，忽略try的return，而使用finally的return.**

- [Java Exception Interview Questions and Answers](https://www.journaldev.com/2167/java-exception-interview-questions-and-answersl)
- [Java提高篇——Java 异常处理](https://www.cnblogs.com/Qian123/p/5715402.html)

[BACK TO TOP](#Java基础)

## 九、泛型

泛型的本质是参数化类型，也就是所操作的数据类型被指定为一个参数。
- 在集合中存储对象并在使用前进行类型转换是不方便的。泛型防止了那种情况的发生。它提供了编译期的类型安全，确保你只能把正确类型的对象放入集合中，避免了在运行时出现ClassCastException。
- 使用T, E or K,V等被广泛认可的类型占位符。

泛型有三种常用的使用方式：泛型类，泛型接口和泛型方法。

限定通配符和非限定通配符
1. 非限定通配符：另一方面<?>表示了非限定通配符，因为<?>可以用任意类型来替代。
2. 一种是`<? extends T>`它通过确保类型必须是T的子类来设定类型的上界
3. 另一种是`<? super T>`它通过确保类型必须是T的父类来设定类型的下界
4. 泛型类型必须用限定内的类型来进行初始化，否则会导致编译错误。

### 类型擦除

类型擦除: Java的泛型基本上都是在编译器这个层次上实现的，在生成的字节码中是不包含泛型中的类型信息的，使用泛型的时候加上类型参数，在编译器编译的时候会去掉，这个过程成为类型擦除。
- 如在代码中定义`List<Object>`和`List<String>`等类型，在编译后都会变成List，JVM看到的只是List，而由泛型附加的类型信息对JVM是看不到的。
- 类型擦除后保留的原始类型，最后在字节码中的类型变量变成真正类型。无论何时定义一个泛型，相应的原始类型都会被自动提供，无限定的变量用Object替换。

泛型擦除的例子： 本应该只能储存Integer，在通过反射调用方法时，却可以添加String数据
```
public static void main(String[] args) throws Exception {
   ArrayList<Integer> list = new ArrayList<Integer>();
   list.add(1);  //这样调用 add 方法只能存储整形，因为泛型类型的实例为 Integer
   list.getClass().getMethod("add", Object.class).invoke(list, "asd");
   for (int i = 0; i < list.size(); i++) {
      System.out.println(list.get(i));
   }
}
// output
//1
//asd
```

**类型擦除后保留的原始类型**：在调用泛型方法时，可以指定泛型，也可以不指定泛型。
- 在不指定泛型的情况下，泛型变量的类型为该方法中的几种类型的同一父类的最小级，直到Object
- 在指定泛型的情况下，该方法的几种类型必须是该泛型的实例的类型或者其子类

```
Number f = Test.add(1, 1.2); //这两个参数一个是Integer，另一个是Float，所以取同一父类的最小级，为Number  
Object o = Test.add(1, "asd"); //这两个参数一个是Integer，另一个是String，所以取同一父类的最小级，为Object  
  
//这是一个简单的泛型方法  
public static <T> T add(T x,T y){  
    return y;  
}    
```

泛型使用的一个例子
```
public class Box<T> {
    // T stands for "Type"
    private T t;
    public void set(T t) { this.t = t; }
    public T get() { return t; }
    public <E> E get(E param) {
        // do some logic 
        return (E)obj;
    }
}
```

Java不能实现真正的泛型，只能使用类型擦除来实现伪泛型，这样虽然不会有类型膨胀问题，但是也引起来许多新问题

- [Java 泛型详解](https://www.cnblogs.com/Blue-Keroro/p/8875898.html)
- [10 道 Java 泛型面试题](https://cloud.tencent.com/developer/article/1033693)

[BACK TO TOP](#Java基础)

## 十、注解

### 内置注解

Java 定义了一套注解，共有 7 个，3 个在 java.lang 中，剩下 4 个在 java.lang.annotation 中。

作用在代码的注解:
- `@Override` - 检查该方法是否是重写方法。如果发现其父类，或者是引用的接口中并没有该方法时，会报编译错误。
- `@Deprecated` - 标记过时方法。如果使用该方法，会报编译警告。
- `@SuppressWarnings` - 指示编译器去忽略注解中声明的警告。

作用在其他注解的注解(或者说元注解)是:
- `@Retention` - 标识这个注解怎么保存，是只在代码中，还是编入class文件中，或者是在运行时可以通过反射访问。
- `@Documented` - 标记这些注解是否包含在用户文档中。
- `@Target` - 标记这个注解应该是哪种 Java 成员。
- @Inherited - 标记这个注解是继承于哪个注解类(默认 注解并没有继承于任何子类)

从 Java 7 开始，额外添加了 3 个注解:
- `@SafeVarargs` - Java 7 开始支持，忽略任何使用参数为泛型变量的方法或构造函数调用产生的警告。
- `@FunctionalInterface` - Java 8 开始支持，标识一个匿名函数或函数式接口。
- `@Repeatable` - Java 8 开始支持，标识某注解可以在同一个声明上使用多次。

### 元注解

java.lang.annotation 提供了四种元注解，专门注解其他的注解（在自定义注解的时候，需要使用到元注解）：

1. `@Documented`：注解是否将包含在JavaDoc中
2. `@Retention`：什么时候使用该注解
    - `RetentionPolicy.SOURCE` : 在编译阶段丢弃。这些注解在编译结束之后就不再有任何意义，所以它们不会写入字节码。@Override, @SuppressWarnings都属于这类注解。
    - `RetentionPolicy.CLASS` : 在类加载的时候丢弃。在字节码文件的处理中有用。注解默认使用这种方式
    - `RetentionPolicy.RUNTIME` : 始终不会丢弃，运行期也保留该注解，因此可以使用反射机制读取该注解的信息。我们自定义的注解通常使用这种方式。
3. `@Target`：注解用于什么地方
    - `ElementType.CONSTRUCTOR`: 用于描述构造器
    - `ElementType.FIELD`: 成员变量、对象、属性（包括enum实例）
    - `ElementType.LOCAL_VARIABLE`: 用于描述局部变量
    - `ElementType.METHOD`: 用于描述方法
    - `ElementType.PACKAGE`: 用于描述包
    - `ElementType.PARAMETER`: 用于描述参数
    - `ElementType.TYPE`: 用于描述类、接口(包括注解类型) 或enum声明 常见的@Component、@Service
4. `@Inherited` – 是否允许子类继承该注解
   > `@Inherited` 元注解是一个标记注解，`@Inherited` 阐述了某个被标注的类型是被继承的。如果一个使用了@Inherited 修饰的annotation 类型被用于一个class，则这个annotation
   将被用于该class 的子类

编写注解的规则
1. Annotation 型定义为`@interface。`
2. 参数成员只能用public 或默认(default) 这两个访问权修饰
3. 参数成员只能用基本类型byte、short、char、int、long、float、double、boolean八种基本数据类型和String、Enum、Class、annotations等数据类型，以及这一些类型的数组。
4. 要获取类方法和字段的注解信息，必须通过Java的反射技术来获取 Annotation 对象

```
@Target(FIELD)
@Retention(RUNTIME)
@Documented
public @interface FruitName {
   String value() default "";
}
```

[注解 Annotation 实现原理与自定义注解例子](https://www.cnblogs.com/acm-bingzi/p/javaAnnotation.html)


[BACK TO TOP](#Java基础)

## 十一、枚举类

枚举类比较使用==，同样也可以使用equals方法，Enum类中重写了equals实际上还是调用==方法。
```
/**
* Returns true if the specified object is equal to this
* enum constant.
*
* @param other the object to be compared for equality with this object.
* @return  true if the specified object is equal to this
*          enum constant.
*/
public final boolean equals(Object other) {
   return this==other;
}
```

为什么使用==比较？
> 因为枚举类在jvm编译成class文件后，实际编译成使用final 修饰的class，final修饰就意味着实例化后不可修改，且都指向堆中的同一个对象


普通的一个枚举类
```
public enum t {
   SPRING,SUMMER,AUTUMN,WINTER;
}
```

反编译后的代码
```
public final class T extends Enum
{
    private T(String s, int i)
    {
        super(s, i);
    }
    public static T[] values()
    {
        T at[];
        int i;
        T at1[];
        System.arraycopy(at = ENUM$VALUES, 0, at1 = new T[i = at.length], 0, i);
        return at1;
    }

    public static T valueOf(String s)
    {
        return (T)Enum.valueOf(demo/T, s);
    }

    public static final T SPRING;
    public static final T SUMMER;
    public static final T AUTUMN;
    public static final T WINTER;
    private static final T ENUM$VALUES[];
    static
    {
        SPRING = new T("SPRING", 0);
        SUMMER = new T("SUMMER", 1);
        AUTUMN = new T("AUTUMN", 2);
        WINTER = new T("WINTER", 3);
        ENUM$VALUES = (new T[] {
            SPRING, SUMMER, AUTUMN, WINTER
        });
    }
}
```

[BACK TO TOP](#Java基础)

## 十二、特性

### Java 各版本的新特性

**New highlights in Java SE 8**

1. Lambda Expressions
2. Pipelines and Streams
3. Date and Time API
4. Default Methods
5. Type Annotations
6. Nashhorn JavaScript Engine
7. Concurrent Accumulators
8. Parallel operations
9. PermGen Error Removed

**New highlights in Java SE 7**

1. Strings in Switch Statement
2. Type Inference for Generic Instance Creation
3. Multiple Exception Handling
4. Support for Dynamic Languages
5. Try with Resources
6. Java nio Package
7. Binary Literals, Underscore in literals
8. Diamond Syntax

- [Difference between Java 1.8 and Java 1.7?](http://www.selfgrowth.com/articles/difference-between-java-18-and-java-17)
- [Java 8 特性](http://www.importnew.com/19345.html)

### Java 与 C++ 的区别

- Java 是纯粹的面向对象语言，所有的对象都继承自 java.lang.Object，C++ 为了兼容 C 即支持面向对象也支持面向过程。
- Java 通过虚拟机从而实现跨平台特性，但是 C++ 依赖于特定的平台。
- Java 没有指针，它的引用可以理解为安全指针，而 C++ 具有和 C 一样的指针。
- Java 支持自动垃圾回收，而 C++ 需要手动回收。
- Java 不支持多重继承，只能通过实现多个接口来达到相同目的，而 C++ 支持多重继承。
- Java 不支持操作符重载，虽然可以对两个 String 对象执行加法运算，但是这是语言内置支持的操作，不属于操作符重载，而 C++ 可以。
- Java 的 goto 是保留字，但是不可用，C++ 可以使用 goto。

[What are the main differences between Java and C++?](http://cs-fundamentals.com/tech-interview/java/differences-between-java-and-cpp.php)

### JRE or JDK

- JRE：Java Runtime Environment，Java 运行环境的简称，为 Java 的运行提供了所需的环境。它是一个 JVM 程序，主要包括了 JVM 的标准实现和一些 Java 基本类库。
- JDK：Java Development Kit，Java 开发工具包，提供了 Java 的开发及运行环境。JDK 是 Java 开发的核心，集成了 JRE 以及一些其它的工具，比如编译 Java 源码的编译器 javac 等。

[BACK TO TOP](#Java基础)