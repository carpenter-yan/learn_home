# Java容器

<!-- GFM-TOC -->

* [Java 容器](#java-容器)
    * [一、概览](#一概览)
        * [Collection](#collection)
        * [Map](#map)
    * [二、容器中的设计模式](#二容器中的设计模式)
        * [迭代器模式](#迭代器模式)
        * [适配器模式](#适配器模式)
    * [三、List源码分析](#三List源码分析)
        * [ArrayList](#arraylist)
        * [Vector](#vector)
        * [LinkedList](#linkedlist)
    * [四、Map源码分析](#四Map源码分析)
        * [HashMap](#hashmap)
        * [LinkedHashMap](#linkedhashmap)
        * [WeakHashMap](#weakhashmap)
        * [TreeMap](#treemap)
    * [五、Set源码分析](#五Set源码分析)
        * [HashSet](#hashset)
        * [LinkedHashSet](#linkedhashset)
        * [TreeSet](#treeset)
    * [六、Queue源码分析](#六Queue源码分析)
        * [PriorityQueue](#priorityqueue)
        * [ArrayDeque](#arraydeque)
    * [七、集合工具类](#四集合工具类)
    * [参考资料](#参考资料)

<!-- GFM-TOC -->

## 一、概览

- [Java8容器源码札记](https://blog.csdn.net/panweiwei1994/category_9269683.html)

Java数组长度不可变化，为了保存数量不确定的数据，以及具有映射关系的数据（也称关联数组），Java提供了集合类。

容器主要包括Collection和Map两种，Collection存储着对象的集合，而Map存储着键值对（两个对象）的映射表。

Java容器只能存储对象，对基本类型支持不友好，只能通过包装器类保存，增加了内存占用和装箱拆箱的计算量

- [换个数据结构，一不小心节约了591台机器！](https://www.cnblogs.com/thisiswhy/p/16066548.html)

### Collection

<div align="center"><img src="https://cs-notes-1256109796.cos.ap-guangzhou.myqcloud.com/image-20191208220948084.png"/></div><br>

#### 1. Set

- HashSet：基于哈希表实现，支持快速查找，但不支持有序性操作。并且失去了元素的插入顺序信息，也就是说使用 Iterator 遍历 HashSet 得到的结果是不确定的。

- TreeSet：基于红黑树实现，支持有序性操作，例如根据一个范围查找元素的操作。但是查找效率不如 HashSet，HashSet 查找的时间复杂度为 O(1)，TreeSet 则为 O(logN)。

- LinkedHashSet：具有 HashSet 的查找效率，并且内部使用双向链表维护元素的插入顺序。

#### 2. List

- ArrayList：基于动态数组实现，支持随机访问。

- Vector：和 ArrayList 类似，但它是线程安全的。

- LinkedList：基于双向链表实现，只能顺序访问，但是可以快速地在链表中间插入和删除元素。不仅如此，LinkedList 还可以用作栈、队列和双向队列。

#### 3. Queue

- LinkedList：可以用它来实现双向队列。

- PriorityQueue：基于堆结构实现，可以用它来实现优先队列。

### Map

<div align="center"><img src="https://cs-notes-1256109796.cos.ap-guangzhou.myqcloud.com/image-20201101234335837.png"/></div><br>

- HashMap：基于哈希表实现。

- TreeMap：基于红黑树实现。

- HashTable：和HashMap 类似，但它是线程安全的，这意味着同一时刻多个线程同时写入HashTable不会导致数据不一致。 它是遗留类，不应该去使用它，而是使用
  ConcurrentHashMap来支持线程安全，ConcurrentHashMap的效率会更高，因为ConcurrentHashMap引入了分段锁。

- LinkedHashMap：使用双向链表来维护元素的顺序，顺序为插入顺序或者最近最少使用（LRU）顺序。

[BACK TO TOP](#Java容器)

## 二、容器中的设计模式

### 迭代器模式

<div align="center"> <img src="https://cs-notes-1256109796.cos.ap-guangzhou.myqcloud.com/image-20191208225301973.png"/> </div><br>

Collection 继承了 Iterable 接口，其中的 iterator() 方法能够产生一个 Iterator 对象，通过这个对象就可以迭代遍历 Collection 中的元素。

从 JDK 1.5 之后可以使用 foreach 方法来遍历实现了 Iterable 接口的聚合对象。

```java
class demo {
    public static void main(String[] args) {
        List<String> list = new ArrayList<>();
        list.add("a");
        list.add("b");
        for (String item : list) {
            System.out.println(item);
        }
    }
}
```

### 适配器模式

java.util.Arrays#asList() 可以把数组类型转换为 List 类型。

```java
class Arrays {
    public static <T> List<T> asList(T... a);

    public static void main(String[] args) {
        Integer[] arr = {1, 2, 3};
        List list = Arrays.asList(arr);

        List list = Arrays.asList(1, 2, 3);//也可以使用以下方式调用 asList()：
    }
}
```

应该注意的是 asList() 的参数为泛型的变长参数，不能使用基本类型数组作为参数，只能使用相应的包装类型数组。

[BACK TO TOP](#Java容器)

## 三、List源码分析

如果没有特别说明，以下源码分析基于JDK 1.8。

在 IDEA 中 double shift 调出 Search EveryWhere，查找源码文件，找到之后就可以阅读源码。

### ArrayList

#### 1. 概览

因为ArrayList是基于数组实现的，所以支持快速随机访问。RandomAccess接口标识着该类支持快速随机访问。

```java
public class ArrayList<E> extends AbstractList<E>
        implements List<E>, RandomAccess, Cloneable, java.io.Serializable {
    private static final int DEFAULT_CAPACITY = 10;  //数组的默认大小为 10。

}
```

<div align="center"> <img src="https://cs-notes-1256109796.cos.ap-guangzhou.myqcloud.com/image-20191208232221265.png"/> </div><br>

#### 2. 扩容

添加元素时使用ensureCapacityInternal()方法来保证容量足够，如果不够时，需要使用grow() 方法进行扩容，新容量的大小为 `oldCapacity + (oldCapacity >> 1)`， 即
oldCapacity+oldCapacity/2。其中 oldCapacity >> 1 需要取整，所以新容量大约是旧容量的1.5 倍左右。（oldCapacity 为偶数就是 1.5 倍，为奇数就是 1.5 倍-0.5）

扩容操作需要调用 `Arrays.copyOf()` 把原数组整个复制到新数组中，这个操作代价很高，因此最好在创建ArrayList对象时就指定大概的容量大小，减少扩容操作的次数。

```java
class ArrayList {
    public boolean add(E e) {
        ensureCapacityInternal(size + 1);  // Increments modCount!!
        elementData[size++] = e;
        return true;
    }

    private void ensureCapacityInternal(int minCapacity) {
        if (elementData == DEFAULTCAPACITY_EMPTY_ELEMENTDATA) {
            minCapacity = Math.max(DEFAULT_CAPACITY, minCapacity);
        }
        ensureExplicitCapacity(minCapacity);
    }

    private void ensureExplicitCapacity(int minCapacity) {
        modCount++;
        // overflow-conscious code
        if (minCapacity - elementData.length > 0)
            grow(minCapacity);
    }

    private void grow(int minCapacity) {
        // overflow-conscious code
        int oldCapacity = elementData.length;
        int newCapacity = oldCapacity + (oldCapacity >> 1);
        if (newCapacity - minCapacity < 0)
            newCapacity = minCapacity;
        if (newCapacity - MAX_ARRAY_SIZE > 0)
            newCapacity = hugeCapacity(minCapacity);
        // minCapacity is usually close to size, so this is a win:
        elementData = Arrays.copyOf(elementData, newCapacity);
    }
}
```

#### 3. 删除元素

需要调用System.arraycopy() 将index+1后面的元素都复制到index位置上，该操作的时间复杂度为O(N)，可以看到ArrayList删除元素的代价是非常高的。

```java
class ArrayList {
    public E remove(int index) {
        rangeCheck(index);
        modCount++;
        E oldValue = elementData(index);
        int numMoved = size - index - 1;
        if (numMoved > 0)
            System.arraycopy(elementData, index + 1, elementData, index, numMoved);
        elementData[--size] = null; // clear to let GC do its work
        return oldValue;
    }
}
```

#### 4. 序列化

ArrayList基于数组实现，并且具有动态扩容特性，因此保存元素的数组不一定都会被使用(部分元素没值)，那么就没必要全部进行序列化。

保存元素的数组elementData使用transient修饰，该关键字声明数组默认不会被序列化。

```java
class ArrayList {
    transient Object[] elementData; // non-private to simplify nested class access

    private void readObject(java.io.ObjectInputStream s)
            throws java.io.IOException, ClassNotFoundException {
        elementData = EMPTY_ELEMENTDATA;

        // Read in size, and any hidden stuff
        s.defaultReadObject();

        // Read in capacity
        s.readInt(); // ignored

        if (size > 0) {
            // be like clone(), allocate array based upon size not capacity
            ensureCapacityInternal(size);

            Object[] a = elementData;
            // Read in all elements in the proper order.
            for (int i = 0; i < size; i++) {
                a[i] = s.readObject();
            }
        }
    }

    private void writeObject(java.io.ObjectOutputStream s)
            throws java.io.IOException {
        // Write out element count, and any hidden stuff
        int expectedModCount = modCount;
        s.defaultWriteObject();

        // Write out size as capacity for behavioural compatibility with clone()
        s.writeInt(size);

        // Write out all elements in the proper order.
        for (int i = 0; i < size; i++) {
            s.writeObject(elementData[i]);
        }

        if (modCount != expectedModCount) {
            throw new ConcurrentModificationException();
        }
    }

    public static void main(String[] args) {
        ArrayList list = new ArrayList();
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
        oos.writeObject(list);
    }
}
```

ArrayList实现了 writeObject() 和 readObject() 来控制只序列化数组中有元素填充那部分内容。

序列化时需要使用ObjectOutputStream的writeObject()将对象转换为字节流并输出。 而writeObject()方法在传入的对象存在writeObject()的时候会去反射调用该对象的writeObject()
来实现序列化。 反序列化使用的是ObjectInputStream的readObject()方法，原理类似。

#### 5. Fail-Fast

modCount用来记录ArrayList结构发生变化的次数。结构发生变化是指添加或者删除至少一个元素的所有操作，或者是调整内部数组的大小，仅仅只是设置元素的值不算结构发生变化。

在进行序列化或者迭代等操作时，需要比较操作前后modCount是否改变，如果改变了需要抛出ConcurrentModificationException。代码参考上节序列化中的writeObject()方法。

```java
class ArrayList {
    public Iterator<E> iterator() {
        return new Itr();
    }

    private class Itr implements Iterator<E> {
        int cursor;       // index of next element to return
        int lastRet = -1; // index of last element returned; -1 if no such
        int expectedModCount = modCount; //创建迭代器是初始化为modCount

        Itr() {
        }

        final void checkForComodification() {
            /** 当调用ArrayList的add,remove等方法时会改变modCount。但expectedModCount初始化后不变，所以能快速失败
             但在多线程环境下可能不准确，官方写的是经历准确抛出快速失败
             */
            if (modCount != expectedModCount)
                throw new ConcurrentModificationException();
        }

        public boolean hasNext() {
            return cursor != size;
        }

        @SuppressWarnings("unchecked")
        public E next() {
            checkForComodification();
            int i = cursor;
            if (i >= size)
                throw new NoSuchElementException();
            Object[] elementData = ArrayList.this.elementData;
            if (i >= elementData.length)
                throw new ConcurrentModificationException();
            cursor = i + 1;
            return (E) elementData[lastRet = i];
        }

        public void remove() {
            if (lastRet < 0)
                throw new IllegalStateException();
            checkForComodification();

            try {
                ArrayList.this.remove(lastRet);
                cursor = lastRet;
                lastRet = -1;
                expectedModCount = modCount;//迭代器的remove方法最后修改了expectedModCount,所以不会fail-fast
            } catch (IndexOutOfBoundsException ex) {
                throw new ConcurrentModificationException();
            }
        }
    }
}

```

#### subList

subList返回的是ArrayList的内部类SubList，是ArrayList的一个快照，对该子数组的任意修改会影响原有数组。 当原有数组修改后，子数据再操作时会报ConcurrentModificationException异常。

#### listIterator

新的迭代器实现，相比普通的iterator增加了向前遍历的方法。

#### spliterator

Spliterator是Java 8中加入的一个新接口；这个名字代表“可拆分迭代器”（splitable iterator）。 和Iterator一样，Spliterator也用于遍历数据源中的元素，但它是为了并行执行而设计的。 Java
8已经为集合框架中包含的所有数据结构提供了一个默认的Spliterator实现。集合实现了Spliterator接口，接口提供了一个spliterator方法。

[BACK TO TOP](#Java容器)

### Vector

#### 1. 同步

它的实现与ArrayList类似，但是使用了synchronized进行同步。

```java
class Vector {
    public synchronized boolean add(E e) {
        modCount++;
        ensureCapacityHelper(elementCount + 1);
        elementData[elementCount++] = e;
        return true;
    }

    public synchronized E get(int index) {
        if (index >= elementCount)
            throw new ArrayIndexOutOfBoundsException(index);

        return elementData(index);
    }
}
```

#### 2. 扩容

Vector的构造函数可以传入capacityIncrement参数，它的作用是在扩容时使容量capacity增长capacityIncrement。 如果这个参数的值小于等于0，扩容时每次都令capacity为原来的两倍。

```java
class Vector {
    public Vector(int initialCapacity, int capacityIncrement) {
        super();
        if (initialCapacity < 0)
            throw new IllegalArgumentException("Illegal Capacity: " +
                    initialCapacity);
        this.elementData = new Object[initialCapacity];
        this.capacityIncrement = capacityIncrement;
    }

    public Vector(int initialCapacity) {
        this(initialCapacity, 0);
    }

    public Vector() {
        this(10);
    }

    private void grow(int minCapacity) {
        // overflow-conscious code
        int oldCapacity = elementData.length;
        int newCapacity = oldCapacity + ((capacityIncrement > 0) ?
                capacityIncrement : oldCapacity);
        if (newCapacity - minCapacity < 0)
            newCapacity = minCapacity;
        if (newCapacity - MAX_ARRAY_SIZE > 0)
            newCapacity = hugeCapacity(minCapacity);
        elementData = Arrays.copyOf(elementData, newCapacity);
    }
}
```

调用没有capacityIncrement的构造函数时，capacityIncrement值被设置为0，也就是说默认情况下 Vector 每次扩容时容量都会翻倍。

#### 3. 与ArrayList的比较

- Vector 是同步的，因此开销就比ArrayList 要大，访问速度更慢。最好使用ArrayList而不是Vector，因为同步操作完全可以由程序员自己来控制；
- Vector 每次扩容请求其大小的2倍（也可以通过构造函数设置增长的容量），而 ArrayList是1.5倍。

#### 4. 替代方案

可以使用 `Collections.synchronizedList();` 得到一个线程安全的ArrayList。 也可以使用concurrent并发包下的CopyOnWriteArrayList类。

1 构造机制不同
> SynchronizedList：可将所有的List的子类转成线程安全的类，例如可以把ArrayList和LinkedList直接转换成SynchronizedList，而无需改变底层数据结构  
> Vector：底层结构固定，无法转换

2 扩容机制不同
> SynchronizedList：如果用ArrayList构建SynchronizedList的话，那么SynchronizedList为1.5倍扩容  
> Vector：Vector缺省情况下2倍扩容

3 同步机制不同
> SynchronizedList：使用同步代码块实现同步，锁定的对象为mutex（构造函数可以传入一个Object，如果在调用的时候显示的传入一个对象，那么锁定的就是用户传入的对象，反之锁定this对象）。
> Vector：使用同步方法实现同步，锁定this对象  
> 由于两者同步内容相同，所以同步代码块和同步方法性能消耗基本相同，不同的是SynchronizedList遍历时要手动进行同步处理

#### 5. 适用场景

CopyOnWriteArrayList在写操作的同时允许读操作，大大提高了读操作的性能，因此很适合读多写少的应用场景。

但是 CopyOnWriteArrayList有其缺陷：

- 内存占用：在写操作时需要复制一个新的数组，使得内存占用为原来的两倍左右；
- 数据不一致：读操作不能读取实时性的数据，因为部分写操作的数据还未同步到读数组中。

所以CopyOnWriteArrayList不适合内存敏感以及对实时性要求很高的场景。

### LinkedList

#### 1. 概览

基于双向链表实现，使用 Node 存储链表节点信息。每个链表存储了first和last 指针：

```java
class LinkedList {
    transient Node<E> first;
    transient Node<E> last;

    private static class Node<E> {
        E item;
        Node<E> next;
        Node<E> prev;
    }
}
```

<div align="center"> <img src="https://cs-notes-1256109796.cos.ap-guangzhou.myqcloud.com/image-20191208233940066.png"/> </div><br>

#### 2. 与ArrayList的比较

ArrayList基于动态数组实现，LinkedList基于双向链表实现。ArrayList和LinkedList 的区别可以归结为数组和链表的区别：

- 数组支持随机访问，但插入删除的代价很高，需要移动大量元素；
- 链表不支持随机访问，但插入删除只需要改变指针。

[BACK TO TOP](#Java容器)

## 四、Map源码分析

### HashMap

#### 1. 存储结构

在JDK1.7及之前，是用数组加链表的方式存储的。但是，众所周知，当链表的长度特别长的时候，查询效率将直线下降，查询的时间复杂度为 O(n)。 因此，JDK1.8 把它设计为达到一个特定的阈值之后，就将链表转化为红黑树。

<div align="center"><img src="https://img2020.cnblogs.com/other/1714084/202004/1714084-20200413101247874-715772294.jpg"/></div><br>

```java
class HashMap {
    //默认的初始化容量为16，必须是2的n次幂
    static final int DEFAULT_INITIAL_CAPACITY = 1 << 4; // aka 16

    //最大容量为 2^30
    static final int MAXIMUM_CAPACITY = 1 << 30;

    //默认的加载因子0.75，乘以数组容量得到的值，用来表示元素个数达到多少时，需要扩容。
    //为什么设置 0.75 这个值呢，简单来说就是时间和空间的权衡。
    //若小于0.75如0.5，则数组长度达到一半大小就需要扩容，空间使用率大大降低，
    //若大于0.75如0.8，则会增大hash冲突的概率，影响查询效率。
    static final float DEFAULT_LOAD_FACTOR = 0.75f;

    //刚才提到了当链表长度过长时，会有一个阈值，超过这个阈值8就会转化为红黑树
    static final int TREEIFY_THRESHOLD = 8;

    //当红黑树上的元素个数，减少到6个时，就退化为链表
    static final int UNTREEIFY_THRESHOLD = 6;

    //链表转化为红黑树，除了有阈值的限制，还有另外一个限制，需要数组容量至少达到64，才会树化。
    //这是为了避免，数组刚初始化时容量较小导致链表长度达到阈值引擎不必要的树化。
    static final int MIN_TREEIFY_CAPACITY = 64;

    //存放所有Node节点的数组
    transient Node<K, V>[] table;

    //存放所有的键值对
    transient Set<Map.Entry<K, V>> entrySet;

    //map中的实际键值对个数，即数组中元素个数
    transient int size;

    //每次结构改变时，都会自增，fail-fast机制，这是一种错误检测机制。
    //当迭代集合的时候，如果结构发生改变，则会发生 fail-fast，抛出异常。
    transient int modCount;

    //数组扩容阈值
    int threshold;

    //加载因子
    final float loadFactor;

    //普通单向链表节点类
    static class Node<K, V> implements Map.Entry<K, V> {
        //key的hash值，put和get的时候都需要用到它来确定元素在数组中的位置
        final int hash;
        final K key;
        V value;
        //指向单链表的下一个节点
        Node<K, V> next;

        Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    //转化为红黑树的节点类
    static final class TreeNode<K, V> extends LinkedHashMap.Entry<K, V> {
        //当前节点的父节点
        TreeNode<K, V> parent;
        //左孩子节点
        TreeNode<K, V> left;
        //右孩子节点
        TreeNode<K, V> right;
        //指向前一个节点
        TreeNode<K, V> prev;    // needed to unlink next upon deletion
        //当前节点是红色或者黑色的标识
        boolean red;

        TreeNode(int hash, K key, V val, Node<K, V> next) {
            super(hash, key, val, next);
        }
    }
}
```

#### 2. tableSizeFor()

在初始化时传入capability参数时，调用该方法返回的就是大于当前传入值的最小（最接近当前值）的一个2的n次幂的值。

```java
class HashMap {
    static final int tableSizeFor(int cap) {
        int n = cap - 1;
        n |= n >>> 1;
        n |= n >>> 2;
        n |= n >>> 4;
        n |= n >>> 8;
        n |= n >>> 16;
        return (n < 0) ? 1 : (n >= MAXIMUM_CAPACITY) ? MAXIMUM_CAPACITY : n + 1;
    }
}
```

#### 3.put()方法详解

```java
class HashMap {
    //put方法，会先调用一个hash()方法，得到当前key的一个hash值，用于确定当前key应该存放在数组的哪个下标位置
    public V put(K key, V value) {
        return putVal(hash(key), key, value, false, true);
    }

    //把hash值和当前的key，value传入进来
    //这里onlyIfAbsent如果为true，表明不能修改已经存在的值，因此我们传入false
    //evict只有在方法 afterNodeInsertion(boolean evict) {}用到，可以看到它是一个空实现，因此不用关注这个参数
    final V putVal(int hash, K key, V value, boolean onlyIfAbsent, boolean evict) {
        Node<K, V>[] tab;
        Node<K, V> p;
        int n, i;
        //判断table是否为空，如果空的话，会先调用resize扩容:延迟初始化机制
        if ((tab = table) == null || (n = tab.length) == 0) {
            n = (tab = resize()).length;
        }

        //根据当前key的hash值找到它在数组中的下标，判断当前下标位置是否已经存在元素，
        //若没有，则把key、value包装成Node节点，直接添加到此位置。
        // i = (n - 1) & hash 是计算下标位置的，为什么这样算，后边讲
        if ((p = tab[i = (n - 1) & hash]) == null) {
            tab[i] = newNode(hash, key, value, null);
        } else {
            //如果当前位置已经有元素了，分为三种情况。
            Node<K, V> e;
            K k;
            //1.当前位置元素的hash值等于传过来的hash，并且他们的key值也相等，
            //则把p赋值给e，跳转到①处，后续需要做值的覆盖处理
            if (p.hash == hash &&
                    ((k = p.key) == key || (key != null && key.equals(k))))
                e = p;
                //2.如果当前是红黑树结构，则把它加入到红黑树 
            else if (p instanceof TreeNode)
                e = ((TreeNode<K, V>) p).putTreeVal(this, tab, hash, key, value);
            else {
                //3.说明此位置已存在元素，并且是普通链表结构，则采用尾插法(1.8开始使用)，把新节点加入到链表尾部
                for (int binCount = 0; ; ++binCount) {
                    if ((e = p.next) == null) {
                        //如果头结点的下一个节点为空，则插入新节点
                        p.next = newNode(hash, key, value, null);
                        //如果在插入的过程中，链表长度超过了8，则转化为红黑树
                        if (binCount >= TREEIFY_THRESHOLD - 1) // -1 for 1st
                            treeifyBin(tab, hash);
                        //插入成功之后，跳出循环，跳转到①处
                        break;
                    }
                    //若在链表中找到了相同key的话，直接退出循环，跳转到①处
                    if (e.hash == hash &&
                            ((k = e.key) == key || (key != null && key.equals(k))))
                        break;
                    p = e;
                }
            }
            //① 
            //说明发生了碰撞，e代表的是旧值，因此节点位置不变，但是需要替换为新值
            if (e != null) { // existing mapping for key
                V oldValue = e.value;
                //用新值替换旧值，并返回旧值
                if (!onlyIfAbsent || oldValue == null)
                    e.value = value;
                //看方法名字即可知，这是在node被访问之后需要做的操作。其实此处是一个空实现，
                //只有在LinkedHashMap才会实现，用于实现根据访问先后顺序对元素进行排序，hashmap不提供排序功能
                afterNodeAccess(e);
                return oldValue;
            }
        }
        //fail-fast机制
        ++modCount;
        //如果当前数组中的元素个数超过阈值，则扩容
        if (++size > threshold)
            resize();
        //同样的空实现
        afterNodeInsertion(evict);
        return null;
    }
}
```

整体思路:

1. 通过hash()计算桶下标，找到要插入的位置t。
2. 判断数组是否为空，为空则对其初始化。
3. 判断数组t位置上内容

- 如果为空，将新值插入到该位置
- 如果不为空，分3种情况：

> 当前节点和新增的key一致，替换该节点的值为新值
> 当前节点为红黑树节点，调用树的节点添加方法
> 当前节点为链表，这遍历链表，如果不存在相同的key，则添加到链表末尾，如果存在相同的key则替换

4. 如果添加完节点后map的size大于了扩容阈值则进行扩容

#### 4.hash()计算原理

一次16位右位移异或混合  
混合后的低位掺杂了高位的部分特征，这样高位的信息也被变相保留下来。  
混合原始哈希码的高位和低位，以此来加大低位的随机性。

```java
class HashMap {
    static final int hash(Object key) {
        int h;
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
    }
}
```

问题：为什么采用hashcode的高16位和低16位异或能降低hash碰撞？hash函数能不能直接用key的hashcode？

- 上述解释的点，低位与高位混合，加大hash的随机性。
- key的hashcode可能被重写，重写的hash函数冲突的概率无法保证。因此hashMap需要在此基础使用自己的hash加大随机性。

#### 5.resize()扩容机制

在上边 put 方法中，我们会发现，当数组为空的时候，会调用 resize 方法，当数组的 size 大于阈值的时候，也会调用 resize方法。

```java
class HashMap {
    final Node<K, V>[] resize() {
        //旧数组
        Node<K, V>[] oldTab = table;
        //旧数组的容量
        int oldCap = (oldTab == null) ? 0 : oldTab.length;
        //旧数组的扩容阈值，注意看，这里取的是当前对象的threshold值，下边的第2种情况会用到。
        int oldThr = threshold;
        //初始化新数组的容量和阈值，分三种情况讨论。
        int newCap, newThr = 0;
        //1.当旧数组的容量大于0时，说明在这之前肯定调用过resize扩容过一次，才会导致旧容量不为0。
        if (oldCap > 0) {
            //容量达到了最大值
            if (oldCap >= MAXIMUM_CAPACITY) {
                threshold = Integer.MAX_VALUE;
                return oldTab;
            }
            //新数组的容量和阈值都扩大原来的2倍
            else if ((newCap = oldCap << 1) < MAXIMUM_CAPACITY &&
                    oldCap >= DEFAULT_INITIAL_CAPACITY)
                newThr = oldThr << 1; // double threshold
        } else if (oldThr > 0) {
            //2.到这里，说明 oldCap <= 0，并且 oldThr(threshold) > 0，这就是map初始化的时候，第一次调用resize的情况
            //而oldThr的值等 threshold，此时的threshold是通过tableSizeFor方法得到的一个2的n次幂的值(我们以16为例)。
            //因此，需要把oldThr的值，也就是threshold，赋值给新数组的容量newCap，以保证数组的容量是2的n次幂。
            //所以我们可以得出结论，当map第一次put元素的时候，就会走到这个分支，把数组的容量设置为正确的值（2的n次幂)
            //但是，此时 threshold的值也是2的n次幂，这不对啊，它应该是数组的容量乘以加载因子才对。别着急，这个会在③处理。
            newCap = oldThr;
        } else {
            //3.到这里，说明oldCap和oldThr都是小于等于0的。也说明我们的map是通过默认无参构造来创建的，
            //于是，数组的容量和阈值都取默认值就可以了，即 16 和 12。
            newCap = DEFAULT_INITIAL_CAPACITY;
            newThr = (int) (DEFAULT_LOAD_FACTOR * DEFAULT_INITIAL_CAPACITY);
        }
        //③ 这里就是处理第2种情况，因为只有这种情况 newThr 才为0，
        //因此计算 newThr（用 newCap即16 乘以加载因子 0.75，得到 12） ，并把它赋值给 threshold
        if (newThr == 0) {
            float ft = (float) newCap * loadFactor;
            newThr = (newCap < MAXIMUM_CAPACITY && ft < (float) MAXIMUM_CAPACITY ?
                    (int) ft : Integer.MAX_VALUE);
        }
        //赋予threshold正确的值，表示数组下次需要扩容的阈值（此时就把原来的 16 修正为了12）。
        threshold = newThr;
        @SuppressWarnings({"rawtypes", "unchecked"})
        Node<K, V>[] newTab = (Node<K, V>[]) new Node[newCap];
        table = newTab;
        //如果原来的数组不为空，那么我们就需要把原来数组中的元素重新分配到新的数组中
        //如果是第2种情况，由于是第一次调用resize，此时数组肯定是空的，因此也就不需要重新分配元素。
        if (oldTab != null) {
            //遍历旧数组
            for (int j = 0; j < oldCap; ++j) {
                Node<K, V> e;
                //取到当前下标的第一个元素，如果存在，则分三种情况重新分配位置
                if ((e = oldTab[j]) != null) {
                    oldTab[j] = null;
                    if (e.next == null) {
                        //1.如果当前元素的下一个元素为空，则说明此处只有一个元素
                        //则直接用它的hash()值和新数组的容量取模就可以了，得到新的下标位置。
                        newTab[e.hash & (newCap - 1)] = e;
                    } else if (e instanceof TreeNode) {
                        //2.如果是红黑树结构，则拆分红黑树，必要时有可能退化为链表
                        ((TreeNode<K, V>) e).split(this, newTab, j, oldCap);
                    } else {
                        //3.到这里说明，这是一个长度大于1的普通链表，则需要计算并
                        //判断当前位置的链表是否需要移动到新的位置
                        // loHead 和 loTail 分别代表链表旧位置的头尾节点
                        Node<K, V> loHead = null, loTail = null;
                        // hiHead 和 hiTail分别代表链表移动到新位置的头尾节点
                        Node<K, V> hiHead = null, hiTail = null;
                        Node<K, V> next;
                        do {
                            next = e.next;
                            if ((e.hash & oldCap) == 0) {
                                //如果当前元素的hash值和oldCap做与运算为0，则原位置不变
                                if (loTail == null)
                                    loHead = e;
                                else
                                    loTail.next = e;
                                loTail = e;
                            } else {
                                //否则，需要移动到新的位置
                                if (hiTail == null)
                                    hiHead = e;
                                else
                                    hiTail.next = e;
                                hiTail = e;
                            }
                        } while ((e = next) != null);
                        //原位置不变的一条链表，数组下标不变
                        if (loTail != null) {
                            loTail.next = null;
                            newTab[j] = loHead;
                        }
                        //移动到新位置的一条链表，数组下标为原下标加上旧数组的容量
                        if (hiTail != null) {
                            hiTail.next = null;
                            newTab[j + oldCap] = hiHead;
                        }
                    }
                }
            }
        }
        return newTab;
    }
}

```

下边这个判断，它用于把原来的普通链表拆分为两条链表，位置不变或者放在新的位置。（要么在原有位置，要么在原有位置+oldCap位置上） 这也是为什么要求capability为2的幂，并且每次扩容为2倍的原因。
因为这样在扩容时原有1条链表最多只会拆为2条，减少了新链表的数量，降低了复杂度。

```
    if ((e.hash & oldCap) == 0) {} else {}
```

#### 6. 为什么HashMap链表会形成死循环

Java 1.7中使用头插发，在多线程的情况下，在rehash的过程中会造成链表形成环，然后在get方法中就会造成死循环。

#### 1.8主要的优化：

- 数组+链表改成了数组+链表或红黑树；
- 链表的插入方式从头插法改成了尾插法，简单说就是插入时，如果数组位置上已经有元素，1.7将新元素放到数组中，原始节点作为新节点的后继节点，1.8遍历链表，将元素放置到链表的最后；
- 扩容的时候1.7需要对原数组中的元素进行重新hash定位在新数组的位置，1.8采用更简单的判断逻辑，位置不变或索引+旧容量大小；
- 在插入时，1.7先判断是否需要扩容，再插入，1.8先进行插入，插入完成再判断是否需要扩容；

好处：

- 防止发生hash冲突，链表长度过长，将时间复杂度由O(n)降为O(logn);
- 因为1.7头插法扩容时，头插法会使链表发生反转，多线程环境下会产生环；

> 在多线程环境下，1.7 会产生死循环、数据丢失、数据覆盖的问题，1.8 中会有数据覆盖的问题。

[深入理解哈希表](https://www.cnblogs.com/LiLihongqiang/p/7655823.html)  
[美团关于HashMap的讲解](https://tech.meituan.com/2016/06/24/java-hashmap.html)  
[hashMap头插法和尾插法区别](https://blog.csdn.net/weixin_35523284/article/details/112096437)  
[面试官再问你 HashMap 底层原理，就把这篇文章甩给他看](https://www.cnblogs.com/starry-skys/p/12689683.html)  
[HashMap？ConcurrentHashMap？相信看完这篇没人能难住你！](https://blog.csdn.net/weixin_44460333/article/details/86770169)

[BACK TO TOP](#Java容器)

### LinkedHashMap

#### 存储结构

继承自HashMap，因此具有和HashMap一样的快速查找特性。内部维护了一个双向链表，用来维护插入顺序或者LRU顺序。

```java
public class LinkedHashMap<K, V> extends HashMap<K, V> implements Map<K, V> {
    // The head (eldest) of the doubly linked list. head最老
    transient LinkedHashMap.Entry<K, V> head;

    // The tail (youngest) of the doubly linked list. tail最新
    transient LinkedHashMap.Entry<K, V> tail;

    // accessOrder 决定了顺序，默认为false，此时维护的是插入顺序。
    final boolean accessOrder;

    // LinkedHashMap 最重要的是以下用于维护顺序的函数，它们会在put、get 等方法中调用。
    void afterNodeAccess(Node<K, V> p) {
    }

    void afterNodeInsertion(boolean evict) {
    }
}
```

#### afterNodeAccess()

当一个节点被访问时，如果accessOrder为true，则会将该节点移到链表尾部。也就是说指定为LRU顺序之后， 在每次访问一个节点时，会将这个节点移到链表尾部，保证链表尾部是最近访问的节点，那么链表首部就是最近最久未使用的节点。

```java
class LinkedHashMap {
    void afterNodeAccess(Node<K, V> e) { // move node to last
        LinkedHashMap.Entry<K, V> last;
        if (accessOrder && (last = tail) != e) {
            LinkedHashMap.Entry<K, V> p =
                    (LinkedHashMap.Entry<K, V>) e, b = p.before, a = p.after;
            p.after = null;
            if (b == null)
                head = a;
            else
                b.after = a;
            if (a != null)
                a.before = b;
            else
                last = b;
            if (last == null)
                head = p;
            else {
                p.before = last;
                last.after = p;
            }
            tail = p;
            ++modCount;
        }
    }
}
```

#### afterNodeInsertion()

在 put等操作之后执行，当removeEldestEntry()方法返回true时会移除最晚的节点，也就是链表首部节点first。

evict只有在构建Map的时候才为false，在这里为 true。

```java
class LinkedHashMap {
    void afterNodeInsertion(boolean evict) { // possibly remove eldest
        LinkedHashMap.Entry<K, V> first;
        if (evict && (first = head) != null && removeEldestEntry(first)) {
            K key = first.key;
            removeNode(hash(key), key, null, false, true);
        }
    }

    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        return false;
    }
}
```

removeEldestEntry() 默认为false，如果需要让它为true，需要继承LinkedHashMap并且覆盖这个方法的实现， 这在实现LRU
的缓存中特别有用，通过移除最近最久未使用的节点，从而保证缓存空间足够，并且缓存的数据都是热点数据。

#### LRU 缓存

以下是使用 LinkedHashMap实现的一个LRU缓存：

- 设定最大缓存空间MAX_ENTRIES为3；
- 使用LinkedHashMap的构造函数将accessOrder设置为 true，开启LRU顺序；
- 覆盖removeEldestEntry()方法实现，在节点多于MAX_ENTRIES就会将最近最久未使用的数据移除。

```java
class LRUCache<K, V> extends LinkedHashMap<K, V> {
    private static final int MAX_ENTRIES = 3;

    protected boolean removeEldestEntry(Map.Entry eldest) {
        return size() > MAX_ENTRIES;
    }

    LRUCache() {
        super(MAX_ENTRIES, 0.75f, true);
    }

    public static void main(String[] args) {
        LRUCache<Integer, String> cache = new LRUCache<>();
        cache.put(1, "a");
        cache.put(2, "b");
        cache.put(3, "c");
        cache.get(1);
        cache.put(4, "d");
        System.out.println(cache.keySet());
    }
}
```

```html
[3, 1, 4]
```

### WeakHashMap

#### 存储结构

WeakHashMap的Entry继承自WeakReference，被WeakReference关联的对象在下一次垃圾回收时会被回收。

WeakHashMap主要用来实现缓存，通过使用WeakHashMap来引用缓存对象，由JVM对这部分缓存进行回收。

```java
private static class Entry<K, V> extends WeakReference<Object> implements Map.Entry<K, V> {
}
```

#### ConcurrentCache

Tomcat中的ConcurrentCache使用了WeakHashMap来实现缓存功能。

ConcurrentCache 采取的是分代缓存：

- 经常使用的对象放入eden中，eden使用ConcurrentHashMap实现，不用担心会被回收（伊甸园）；
- 不常用的对象放入longterm，longterm使用WeakHashMap实现，这些老对象会被垃圾收集器回收。
- 当调用get()方法时，会先从eden区获取，如果没有找到的话再到longterm获取，当从longterm获取到就把对象放入eden中，从而保证经常被访问的节点不容易被回收。
- 当调用put()方法时，如果eden的大小超过了size，那么就将eden中的所有对象都放入longterm中，利用虚拟机回收掉一部分不经常使用的对象。

```java
public final class ConcurrentCache<K, V> {

    private final int size;

    private final Map<K, V> eden;

    private final Map<K, V> longterm;

    public ConcurrentCache(int size) {
        this.size = size;
        this.eden = new ConcurrentHashMap<>(size);
        this.longterm = new WeakHashMap<>(size);
    }

    public V get(K k) {
        V v = this.eden.get(k);
        if (v == null) {
            v = this.longterm.get(k);
            if (v != null)
                this.eden.put(k, v);
        }
        return v;
    }

    public void put(K k, V v) {
        if (this.eden.size() >= size) {
            this.longterm.putAll(this.eden);
            this.eden.clear();
        }
        this.eden.put(k, v);
    }
}
```

相比使用LinkedHashMap实现的LRU，longterm使得内存占用翻倍，虽然能在下次垃圾回收是清理，但增加了垃圾回收的负担。 LinkedHashMap使用了额外的pre和next记录顺序，内存使用有效率更低。

### TreeMap

定义了一个Entry的节点，基于红黑树的实现

```java
public class TreeMap<K, V> extends AbstractMap<K, V> implements NavigableMap<K, V>, Cloneable, java.io.Serializable {
    //treeMap的排序规则，如果为null，则根据键的自然顺序进行排序
    private final Comparator<? super K> comparator;
    //红黑数的根节点
    private transient Entry<K, V> root;
    //红黑树节点的个数
    private transient int size = 0;
    //treeMap的结构性修改次数。实现fast-fail机制的关键
    private transient int modCount = 0;

    //使用key的自然排序来构造一个空的treeMap
    public TreeMap() {
        comparator = null;
    }

    //使用给定的比较器来构造一个空的treeMap
    public TreeMap(Comparator<? super K> comparator) {
        this.comparator = comparator;
    }

    //使用key的自然排序来构造一个treeMap，treeMap包含给定map中所有的键值对
    public TreeMap(Map<? extends K, ? extends V> m) {
        comparator = null;
        putAll(m);
    }

    //使用指定的sortedMap来构造treeMap。treeMap中含有sortedMap中所有的键值对，键值对顺序和sortedMap中相同
    public TreeMap(SortedMap<K, ? extends V> m) {
        comparator = m.comparator();
        try {
            buildFromSorted(m.size(), m.entrySet().iterator(), null, null);
        } catch (java.io.IOException cannotHappen) {
        } catch (ClassNotFoundException cannotHappen) {
        }
    }

    //返回指定的key对应的value，如果value为null，则返回null
    public V get(Object key) {
        Entry<K, V> p = getEntry(key);
        return (p == null ? null : p.value);
    }

    //返回节点，如果没有则返回null
    final Entry<K, V> getEntry(Object key) {
        //如果比较器为不为null
        if (comparator != null)
            //通过比较器来获取结果。
            return getEntryUsingComparator(key);
        //如果key为null，抛出NullPointerException
        if (key == null)
            throw new NullPointerException();
        @SuppressWarnings("unchecked")
        Comparable<? super K> k = (Comparable<? super K>) key;
        Entry<K, V> p = root;
        //按照二叉树搜索的方式进行搜索，搜到返回
        while (p != null) {
            //比较节点的key和参数key
            int cmp = k.compareTo(p.key);
            //如果节点的key小于参数key
            if (cmp < 0)
                //向左遍历
                p = p.left;
            else if (cmp > 0)//如果节点的key大于参数key
                //向左遍历
                p = p.right;
            else//如果节点的key等于参数key
                return p;
        }
        //如果遍历完依然没有找到对应的节点，返回null
        return null;
    }

    //使用comparator获取节点
    final Entry<K, V> getEntryUsingComparator(Object key) {
        @SuppressWarnings("unchecked")
        K k = (K) key;
        Comparator<? super K> cpr = comparator;
        if (cpr != null) {
            Entry<K, V> p = root;
            //按照二叉树搜索的方式进行搜索，搜到返回
            while (p != null) {
                //比较节点的key和参数key
                int cmp = k.compareTo(p.key);
                //如果节点的key小于参数key
                if (cmp < 0)
                    //向左遍历
                    p = p.left;
                else if (cmp > 0)//如果节点的key大于参数key
                    //向左遍历
                    p = p.right;
                else//如果节点的key等于参数key
                    return p;
            }
        }
        return null;
    }

    /**
     * 将指定参数key和指定参数value插入map中，如果key已经存在，那就替换key对应的value
     * @return 如果value被替换，则返回旧的value，否则返回null。当然，可能key对应的value就是null
     */
    public V put(K key, V value) {
        Entry<K, V> t = root;
        //如果根节点为空
        if (t == null) {
            compare(key, key); // 对key是否为null进行检查type

            //创建一个根节点，返回null
            root = new Entry<>(key, value, null);
            size = 1;
            modCount++;
            return null;
        }
        //记录比较结果
        int cmp;
        Entry<K, V> parent;
        // split comparator and comparable paths
        Comparator<? super K> cpr = comparator;
        //如果comparator不为null
        if (cpr != null) {
            //循环查找key要插入的位置
            do {
                //记录上次循环的节点t
                parent = t;
                //比较节点t的key和参数key的大小
                cmp = cpr.compare(key, t.key);
                //如果节点t的key > 参数key
                if (cmp < 0)
                    t = t.left;
                else if (cmp > 0)//如果节点t的key < 参数key
                    t = t.right;
                else//如果节点t的key = 参数key，替换value，返回旧value
                    return t.setValue(value);
            } while (t != null);//t为null，没有要比较的节点，代表已经找到新节点要插入的位置
        } else {
            //如果comparator为null，，则使用key作为比较器进行比较，并且key必须实现Comparable接口
            //如果key为null，抛出NullPointerException
            if (key == null)
                throw new NullPointerException();
            @SuppressWarnings("unchecked")
            Comparable<? super K> k = (Comparable<? super K>) key;
            do {//循环查找key要插入的位置
                parent = t;
                cmp = k.compareTo(t.key);
                if (cmp < 0)
                    t = t.left;
                else if (cmp > 0)
                    t = t.right;
                else
                    return t.setValue(value);
            } while (t != null);
        }
        // 找到新节点的父节点后，创建节点对象
        Entry<K, V> e = new Entry<>(key, value, parent);
        //如果新节点key的值小于父节点key的值，则插在父节点的左侧
        if (cmp < 0)
            parent.left = e;
        else//否则插在父节点的左侧
            parent.right = e;
        //插入新的节点后，为了保持红黑树平衡，对红黑树进行调整
        fixAfterInsertion(e);
        size++;
        modCount++;
        //这种情况下没有替换旧value，返回努力了
        return null;
    }

    public V remove(Object key) {
        Entry<K, V> p = getEntry(key);
        if (p == null)
            return null;

        V oldValue = p.value;
        deleteEntry(p);
        return oldValue;
    }

    private void deleteEntry(Entry<K, V> p) {
        modCount++;
        size--;

        // If strictly internal, copy successor's element to p and then make p
        // point to successor.
        if (p.left != null && p.right != null) {
            Entry<K, V> s = successor(p);
            p.key = s.key;
            p.value = s.value;
            p = s;
        } // p has 2 children

        // Start fixup at replacement node, if it exists.
        Entry<K, V> replacement = (p.left != null ? p.left : p.right);

        if (replacement != null) {
            // Link replacement to parent
            replacement.parent = p.parent;
            if (p.parent == null)
                root = replacement;
            else if (p == p.parent.left)
                p.parent.left = replacement;
            else
                p.parent.right = replacement;

            // Null out links so they are OK to use by fixAfterDeletion.
            p.left = p.right = p.parent = null;

            // Fix replacement
            if (p.color == BLACK)
                fixAfterDeletion(replacement);
        } else if (p.parent == null) { // return if we are the only node.
            root = null;
        } else { //  No children. Use self as phantom replacement and unlink.
            if (p.color == BLACK)
                fixAfterDeletion(p);

            if (p.parent != null) {
                if (p == p.parent.left)
                    p.parent.left = null;
                else if (p == p.parent.right)
                    p.parent.right = null;
                p.parent = null;
            }
        }
    }

    //获取TreeMap中大于或等于key的最小的节点，若不存在，就返回null
    final Entry<K, V> getCeilingEntry(K key);

    //获取TreeMap中小于或等于key的最大的节点，若不存在，就返回null
    final Entry<K, V> getFloorEntry(K key);

    //获取TreeMap中大于key的最小的节点，若不存在，返回null
    final Entry<K, V> getHigherEntry(K key);

    //获取TreeMap中小于key的最大的节点，若不存在，就返回null
    final Entry<K, V> getLowerEntry(K key);
}
```

#### TreeMap与HashMap比较
1. 不同点
- hashmap数据结构使用数组+链表+红黑树
- hashmap节点无序，treemap有序
- treemap实现了NavigableMap
- hashmap允许key为null，treemap不允许
- hashmap的增删改查时间复杂度为o(1)，treemap为log(n)
2. 相同点
- 都以键值对的形式存储数据
- 都继承了AbstractMap，实现了Map、Cloneable、Serializable
- 都是非同步的

[BACK TO TOP](#Java容器)

## 五、Set源码分析

### HashSet

## LinkedHashSet

### TreeSet

[BACK TO TOP](#Java容器)

## 六、Queue源码分析

### PriorityQueue

## ArrayDeque

[BACK TO TOP](#Java容器)

## 七、集合工具类

## 参考资料

- Eckel B. Java 编程思想 [M]. 机械工业出版社, 2002.
- [Java Collection Framework](https://www.w3resource.com/java-tutorial/java-collections.php)
- [Iterator 模式](https://openhome.cc/Gossip/DesignPattern/IteratorPattern.htm)
- [Java 8 系列之重新认识 HashMap](https://tech.meituan.com/java_hashmap.html)
- [What is difference between HashMap and Hashtable in Java?](http://javarevisited.blogspot.hk/2010/10/difference-between-hashmap-and.html)
- [Java 集合之 HashMap](http://www.zhangchangle.com/2018/02/07/Java%E9%9B%86%E5%90%88%E4%B9%8BHashMap/)
- [The principle of ConcurrentHashMap analysis](http://www.programering.com/a/MDO3QDNwATM.html)
- [探索 ConcurrentHashMap 高并发性的实现机制](https://www.ibm.com/developerworks/cn/java/java-lo-concurrenthashmap/)
- [HashMap 相关面试题及其解答](https://www.jianshu.com/p/75adf47958a7)
- [Java 集合细节（二）：asList 的缺陷](http://wiki.jikexueyuan.com/project/java-enhancement/java-thirtysix.html)
- [Java Collection Framework – The LinkedList Class](http://javaconceptoftheday.com/java-collection-framework-linkedlist-class/)

