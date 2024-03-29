## Dubbo扩展机制SPI
[Dubbo SPI](https://dubbo.apache.org/zh/docs/v2.7/dev/source/dubbo-spi/)  
[SPI 自适应拓展](https://dubbo.apache.org/zh/docs/v2.7/dev/source/adaptive-extension/)  
[服务导出](https://dubbo.apache.org/zh/docs/v2.7/dev/source/export-service/)  
[服务引用](https://dubbo.apache.org/zh/docs/v2.7/dev/source/refer-service/)  
[服务目录](https://dubbo.apache.org/zh/docs/v2.7/dev/source/directory/)  
[服务路由](https://dubbo.apache.org/zh/docs/v2.7/dev/source/router/)  
[集群](https://dubbo.apache.org/zh/docs/v2.7/dev/source/cluster/)  
[负载均衡](https://dubbo.apache.org/zh/docs/v2.7/dev/source/loadbalance/)  
[服务调用过程](https://dubbo.apache.org/zh/docs/v2.7/dev/source/service-invoking-process/)  

[SPI 自适应拓展](https://dubbo.apache.org/zh/docs/v2.7/dev/source/adaptive-extension/)  
[Dubbo源码解析（一）Hello,Dubbo](https://segmentfault.com/a/1190000016741532)  
[Dubbo源码解析（二）Dubbo扩展机制SPI](https://segmentfault.com/a/1190000016842868) 
[Dubbo内核解析（DubboSPI机制、Adaptive机制、Wrapper机制、Activate机制）](https://blog.csdn.net/weixin_41947378/article/details/108431966)  


dubbo自己实现了一套SPI机制，改进了JDK标准的SPI机制：

1. JDK标准的SPI只能通过遍历来查找扩展点和实例化，有可能导致一次性加载所有的扩展点，如果不是所有的扩展点都被用到，就会导致资源的浪费。
   dubbo每个扩展点都有多种实现，例如com.alibaba.dubbo.rpc.Protocol接口有InjvmProtocol、DubboProtocol、RmiProtocol、
   HttpProtocol、HessianProtocol等实现，如果只是用到其中一个实现，可是加载了全部的实现，会导致资源的浪费。
2. 把配置文件中扩展实现的格式修改，例如META-INF/dubbo/com.xxx.Protocol里的com.foo.XxxProtocol格式改为了
   xxx = com.foo.XxxProtocol这种以键值对的形式，这样做的目的是为了让我们更容易的定位到问题，比如由于第三方库不存在，无法初始化，
   导致无法加载扩展名（“A”），当用户配置使用A时，dubbo就会报无法加载扩展名的错误，而不是报哪些扩展名的实现加载失败以及错误原因，
   这是因为原来的配置格式没有把扩展名的id记录，导致dubbo无法抛出较为精准的异常，这会加大排查问题的难度。所以改成key-value的形式来进行配置。
3. dubbo的SPI机制增加了对IOC、AOP的支持，一个扩展点可以直接通过setter注入到其他扩展点。

### （一）注解@SPI
在某个接口上加上@SPI注解后，表明该接口为可扩展接口。
用协议扩展接口Protocol来举例子，如果使用者在<dubbo:protocol />、<dubbo:service />、<dubbo:reference />都没有指定protocol属性的话，
那么就会默认DubboProtocol就是接口Protocol，因为在Protocol接口上有@SPI("dubbo")注解。而这个protocol属性值或者默认值会被当作该接口的
实现类中的一个key，dubbo会去META-INFdubbointernalcom.alibaba.dubbo.rpc.Protocol文件中找该key对应的value。
Protocol.java
```
@SPI("dubbo")
public interface Protocol {
    ......
}
```
META-INF/dubbo/internal/com.alibaba.dubbo.rpc.Protocol
```
dubbo=com.alibaba.dubbo.rpc.protocol.dubbo.DubboProtocol
```
value就是该Protocol接口的实现类DubboProtocol，这样就做到了SPI扩展。

### （二）注解@Adaptive
该注解为了保证dubbo在内部调用具体实现的时候不是硬编码来指定引用哪个实现，也就是为了适配一个接口的多种实现，
这样做符合模块接口设计的可插拔原则，也增加了整个框架的灵活性，该注解也实现了扩展点自动装配的特性。

dubbo提供了两种方式来实现接口的适配器
1. 在实现类上面加上@Adaptive注解，表明该实现类是该接口的适配器。
   举个例子dubbo中的ExtensionFactory接口就有一个实现类AdaptiveExtensionFactory，加了@Adaptive注解，
   AdaptiveExtensionFactory就不提供具体业务支持，只用来适配ExtensionFactory的SpiExtensionFactory和SpringExtensionFactory这两种实现。
   AdaptiveExtensionFactory会根据在运行时的一些状态来选择具体调用ExtensionFactory的哪个实现。
2. 在接口方法上加@Adaptive注解，dubbo会动态生成适配器类。
   我们从Transporter接口的源码来解释这种方法。
```
@SPI("netty")
public interface Transporter {
    @Adaptive({Constants.SERVER_KEY, Constants.TRANSPORTER_KEY})
    Server bind(URL url, ChannelHandler handler) throws RemotingException;

    @Adaptive({Constants.CLIENT_KEY, Constants.TRANSPORTER_KEY})
    Client connect(URL url, ChannelHandler handler) throws RemotingException;
}
```
我们可以看到在这个接口的bind和connect方法上都有@Adaptive注解，有该注解的方法的参数必须包含URL，
ExtensionLoader会通过createAdaptiveExtensionClassCode方法动态生成一个Transporter$Adaptive类，生成的代码如下：
```
package com.alibaba.dubbo.remoting;
import com.alibaba.dubbo.common.extension.ExtensionLoader;
public class Transporter$Adaptive implements com.alibaba.dubbo.remoting.Transporter{
    
    public com.alibaba.dubbo.remoting.Client connect(com.alibaba.dubbo.common.URL arg0, 
      com.alibaba.dubbo.remoting.ChannelHandler arg1) 
      throws com.alibaba.dubbo.remoting.RemotingException {
        //URL参数为空则抛出异常。
        if (arg0 == null) 
            throw new IllegalArgumentException("url == null");
        
        com.alibaba.dubbo.common.URL url = arg0;
        //这里的getParameter方法可以在源码中具体查看
        String extName = url.getParameter("client", url.getParameter("transporter", "netty"));
        if(extName == null) 
            throw new IllegalStateException("Fail to get extension(com.alibaba.dubbo.remoting.Transporter) name from url(" + url.toString() + ") use keys([client, transporter])");
        //这里我在后面会有详细介绍
        com.alibaba.dubbo.remoting.Transporter extension = (com.alibaba.dubbo.remoting.Transporter)ExtensionLoader.getExtensionLoader
        
        (com.alibaba.dubbo.remoting.Transporter.class).getExtension(extName);
        return extension.connect(arg0, arg1);
    }
    public com.alibaba.dubbo.remoting.Server bind(com.alibaba.dubbo.common.URL arg0, 
      com.alibaba.dubbo.remoting.ChannelHandler arg1) 
      throws com.alibaba.dubbo.remoting.RemotingException {
        if (arg0 == null) 
            throw new IllegalArgumentException("url == null");
        com.alibaba.dubbo.common.URL url = arg0;
        String extName = url.getParameter("server", url.getParameter("transporter", "netty"));
        if(extName == null) 
            throw new IllegalStateException("Fail to get extension(com.alibaba.dubbo.remoting.Transporter) name from url(" + url.toString() + ") use keys([server, transporter])");
        com.alibaba.dubbo.remoting.Transporter extension = (com.alibaba.dubbo.remoting.Transporter)ExtensionLoader.getExtensionLoader
        (com.alibaba.dubbo.remoting.Transporter.class).getExtension(extName);
        
        return extension.bind(arg0, arg1);
    }
}
```
1. 所有扩展点都通过传递URL携带配置信息，所以适配器中的方法必须携带URL参数，才能根据URL中的配置来选择对应的扩展实现。
2. @Adaptive注解中有一些key值，比如connect方法的注解中有两个key，分别为“client”和“transporter”，
   URL会首先去取client对应的value来作为注解@SPI中写到的key值，如果为空，则去取transporter对应的value，
   如果还是为空，则会根据SPI默认的key，也就是netty去调用扩展的实现类，如果@SPI没有设定默认值，则会抛出IllegalStateException异常。

这样就比较清楚这个适配器如何去选择哪个实现类作为本次需要调用的类，这里最关键的还是强调了dubbo以URL为总线，
运行过程中所有的状态数据信息都可以通过URL来获取，比如当前系统采用什么序列化，采用什么通信，采用什么负载均衡等信息，
都是通过URL的参数来呈现的，所以在框架运行过程中，运行到某个阶段需要相应的数据，都可以通过对应的Key从URL的参数列表中获取。

### （三）注解@Activate
扩展点自动激活加载的注解，就是用条件来控制该扩展点实现是否被自动激活加载，在扩展实现类上面使用，实现了扩展点自动激活的特性，
它可以设置两个参数，分别是group和value。具体的介绍可以参照官方文档。

### （四）接口ExtensionFactory
```
@SPI
public interface ExtensionFactory {
    <T> T getExtension(Class<T> type, String name);
}
```
该接口是扩展工厂接口类，它本身也是一个扩展接口，有SPI的注解。该工厂接口提供的就是获取实现类的实例，它也有两种扩展实现，
分别是SpiExtensionFactory和SpringExtensionFactory代表着两种不同方式去获取实例。
而具体选择哪种方式去获取实现类的实例，则在适配器AdaptiveExtensionFactory中制定了规则。

### （五）ExtensionLoader
该类是扩展加载器，这是dubbo实现SPI扩展机制等核心，几乎所有实现的逻辑都被封装在ExtensionLoader中。
1. 属性
```
public class ExtensionLoader<T> {
    // 1.关于存放配置文件的路径变量
    // dubbo为了兼容jdk的SPI扩展机制思想而设存在的
    private static final String SERVICES_DIRECTORY = "META-INF/services/";
    // 用户自定义的扩展实现配置文件存放
    private static final String DUBBO_DIRECTORY = "META-INF/dubbo/";
    // dubbo内部提供的扩展的配置文件路径
    private static final String DUBBO_INTERNAL_DIRECTORY = DUBBO_DIRECTORY + "internal/";
    // 2.扩展加载器集合，key为扩展接口，例如Protocol等
    private static final ConcurrentMap<Class<?>, ExtensionLoader<?>> EXTENSION_LOADERS = new ConcurrentHashMap<>();
    // 3.扩展实现类集合，key为扩展实现类，value为扩展对象
    private static final ConcurrentMap<Class<?>, Object> EXTENSION_INSTANCES = new ConcurrentHashMap<Class<?>, Object>();
    /** 4.以下属性都是cache开头的，都是出于性能和资源的优化做的缓存，读取扩展配置后，会先进行缓存，
     * 等到真正需要用到某个实现时，再对该实现类的对象进行初始化，然后对该对象也进行缓存。*/
    //以下提到的扩展名就是在配置文件中的key值，类似于“dubbo”等
    //缓存的扩展名与拓展类映射，和cachedClasses的key和value对换。
    private final ConcurrentMap<Class<?>, String> cachedNames = new ConcurrentHashMap<>();
    //缓存的扩展实现类集合
    private final Holder<Map<String, Class<?>>> cachedClasses = new Holder<>();
    //扩展名与加有@Activate的自动激活类的映射
    private final Map<String, Activate> cachedActivates = new ConcurrentHashMap<>();
    //缓存的扩展对象集合，key为扩展名，value为扩展对象。例如Protocol扩展，key为dubbo，value为DubboProcotol
    private final ConcurrentMap<String, Holder<Object>> cachedInstances = new ConcurrentHashMap<>();
    //缓存的自适应( Adaptive )扩展对象，例如例如AdaptiveExtensionFactory类的对象
    private final Holder<Object> cachedAdaptiveInstance = new Holder<Object>();
    //缓存的自适应扩展对象的类，例如AdaptiveExtensionFactory类
    private volatile Class<?> cachedAdaptiveClass = null;
    //缓存的默认扩展名，就是@SPI中设置的值
    private String cachedDefaultName;
    //创建cachedAdaptiveInstance异常
    private volatile Throwable createAdaptiveInstanceError;
    //拓展Wrapper实现类集合
    private Set<Class<?>> cachedWrapperClasses;
    //拓展名与加载对应拓展类发生的异常的映射
    private Map<String, IllegalStateException> exceptions = new ConcurrentHashMap<>();
}
```
Wrapper类也实现了扩展接口，但是Wrapper类的用途是ExtensionLoader返回扩展点时，包装在真正的扩展点实现外，这实现了扩展点自动包装的特性。
通俗点说，就是一个接口有很多的实现类，这些实现类会有一些公共的逻辑，如果在每个实现类写一遍这个公共逻辑，那么代码就会重复，
所以增加了这个Wrapper类来包装，把公共逻辑写到Wrapper类中，有点类似AOP切面编程思想。

2. 根据扩展点接口来获得扩展加载器
```
    public static <T> ExtensionLoader<T> getExtensionLoader(Class<T> type) {
        //扩展点接口为空，抛出异常
        if (type == null)
            throw new IllegalArgumentException("Extension type == null");
        //判断type是否是一个接口类
        if (!type.isInterface()) {
            throw new IllegalArgumentException("Extension type(" + type + ") is not interface!");
        }
        //判断是否为可扩展的接口
        if (!withExtensionAnnotation(type)) {
            throw new IllegalArgumentException("Extension type(" + type +
                    ") is not extension, because WITHOUT @" + SPI.class.getSimpleName() + " Annotation!");
        }
        //从扩展加载器集合中取出扩展接口对应的扩展加载器
        ExtensionLoader<T> loader = (ExtensionLoader<T>) EXTENSION_LOADERS.get(type);
        //如果为空，则创建该扩展接口的扩展加载器，并且添加到EXTENSION_LOADERS
        if (loader == null) {
            EXTENSION_LOADERS.putIfAbsent(type, new ExtensionLoader<T>(type));
            loader = (ExtensionLoader<T>) EXTENSION_LOADERS.get(type);
        }
        return loader;
    }
```

3. 获得符合自动激活条件的扩展实现类对象集合
```
    public List<T> getActivateExtension(URL url, String key) {
        return getActivateExtension(url, key, null);
    }

    public List<T> getActivateExtension(URL url, String[] values) {
        return getActivateExtension(url, values, null);
    }

    public List<T> getActivateExtension(URL url, String key, String group) {
        String value = url.getParameter(key);
        return getActivateExtension(url, value == null || value.length() == 0 ? null : Constants.COMMA_SPLIT_PATTERN.split(value), group);
    }

    public List<T> getActivateExtension(URL url, String[] values, String group) {
        List<T> exts = new ArrayList<T>();
        List<String> names = values == null ? new ArrayList<String>(0) : Arrays.asList(values);
        //判断不存在配置 `"-name"` 。例如，<dubbo:service filter="-default" /> ，代表移除所有默认过滤器。
        if (!names.contains(Constants.REMOVE_VALUE_PREFIX + Constants.DEFAULT_KEY)) {
            //获得扩展实现类数组，把扩展实现类放到cachedClasses中
            getExtensionClasses();
            for (Map.Entry<String, Activate> entry : cachedActivates.entrySet()) {
                String name = entry.getKey();
                Activate activate = entry.getValue();
                //获得扩展实现类数组，把扩展实现类放到cachedClasses中
                if (isMatchGroup(group, activate.group())) {
                    //不包含在自定义配置里。如果包含，会在下面的代码处理。
                    //判断是否配置移除。例如 <dubbo:service filter="-monitor" />，则 MonitorFilter 会被移除
                    //判断是否激活
                    if (!names.contains(name)
                            && !names.contains(Constants.REMOVE_VALUE_PREFIX + name)
                            && isActive(activate, url)) {
                        //通过扩展名获得拓展对象
                        T ext = getExtension(name);
                        exts.add(ext);
                    }
                }
            }
            Collections.sort(exts, ActivateComparator.COMPARATOR);
        }
        List<T> usrs = new ArrayList<T>();
        for (int i = 0; i < names.size(); i++) {
            String name = names.get(i);
            //还是判断是否是被移除的配置
            if (!name.startsWith(Constants.REMOVE_VALUE_PREFIX)
                    && !names.contains(Constants.REMOVE_VALUE_PREFIX + name)) {
                //在配置中把自定义的配置放在自动激活的扩展对象前面，可以让自定义的配置先加载
                //例如，<dubbo:service filter="demo,default,demo2" /> ，则 DemoFilter 就会放在默认的过滤器前面。
                if (Constants.DEFAULT_KEY.equals(name)) {
                    if (!usrs.isEmpty()) {
                        exts.addAll(0, usrs);
                        usrs.clear();
                    }
                } else {
                    T ext = getExtension(name);
                    usrs.add(ext);
                }
            }
        }
        if (!usrs.isEmpty()) {
            exts.addAll(usrs);
        }
        return exts;
    }
```
可以看到getActivateExtension重载了四个方法，其实最终的实现都是在最后一个重载方法，
因为自动激活类的条件可以分为无条件、只有value以及有group和value三种

4.  获得通过扩展名获得扩展对象
```
    public T getExtension(String name) {
        if (name == null || name.length() == 0)
            throw new IllegalArgumentException("Extension name == null");
        //查找默认的扩展实现，也就是@SPI中的默认值作为key
        if ("true".equals(name)) {
            return getDefaultExtension();
        }
        //缓存中获取对应的扩展对象
        Holder<Object> holder = cachedInstances.get(name);
        if (holder == null) {
            cachedInstances.putIfAbsent(name, new Holder<Object>());
            holder = cachedInstances.get(name);
        }
        Object instance = holder.get();
        if (instance == null) {
            synchronized (holder) {
                instance = holder.get();
                if (instance == null) {
                    //通过扩展名创建接口实现类的对象
                    instance = createExtension(name);
                    //把创建的扩展对象放入缓存
                    holder.set(instance);
                }
            }
        }
        return (T) instance;
    }
```

5. 查找默认的扩展实现
```
    public T getDefaultExtension() {
        //获得扩展接口的实现类数组
        getExtensionClasses();
        if (null == cachedDefaultName || cachedDefaultName.length() == 0
                || "true".equals(cachedDefaultName)) {
            return null;
        }
        //又重新去调用了getExtension
        return getExtension(cachedDefaultName);
    }
```

6. 扩展接口的实现类
```
    public void addExtension(String name, Class<?> clazz) {
        getExtensionClasses(); // load classes

        //该类是否是接口的本身或子类
        if (!type.isAssignableFrom(clazz)) {
            throw new IllegalStateException("Input type " +
                    clazz + "not implement Extension " + type);
        }
        
        //该类是否被激活
        if (clazz.isInterface()) {
            throw new IllegalStateException("Input type " +
                    clazz + "can not be interface!");
        }

        //判断是否为适配器
        if (!clazz.isAnnotationPresent(Adaptive.class)) {
            if (StringUtils.isBlank(name)) {
                throw new IllegalStateException("Extension name is blank (Extension " + type + ")!");
            }
            if (cachedClasses.get().containsKey(name)) {
                throw new IllegalStateException("Extension name " +
                        name + " already existed(Extension " + type + ")!");
            }
            //把扩展名和扩展接口的实现类放入缓存
            cachedNames.put(clazz, name);
            cachedClasses.get().put(name, clazz);
        } else {
            if (cachedAdaptiveClass != null) {
                throw new IllegalStateException("Adaptive Extension already existed(Extension " + type + ")!");
            }
            cachedAdaptiveClass = clazz;
        }
    }
```

7. 获得自适应扩展对象，也就是接口的适配器对象
```
    public T getAdaptiveExtension() {
        Object instance = cachedAdaptiveInstance.get();
        if (instance == null) {
            if (createAdaptiveInstanceError == null) {
                synchronized (cachedAdaptiveInstance) {
                    instance = cachedAdaptiveInstance.get();
                    if (instance == null) {
                        try {
                            //创建适配器对象
                            instance = createAdaptiveExtension();
                            cachedAdaptiveInstance.set(instance);
                        } catch (Throwable t) {
                            createAdaptiveInstanceError = t;
                            throw new IllegalStateException("fail to create adaptive instance: " + t.toString(), t);
                        }
                    }
                }
            } else {
                throw new IllegalStateException("fail to create adaptive instance: " + createAdaptiveInstanceError.toString(), createAdaptiveInstanceError);
            }
        }

        return (T) instance;
    }
```
思路就是先从缓存中取适配器类的对象，如果没有，则创建一个适配器对象，然后放入缓存

8. 通过扩展名创建扩展接口实现类的对象
```
    private T createExtension(String name) {
        //获得扩展名对应的扩展实现类
        Class<?> clazz = getExtensionClasses().get(name);
        if (clazz == null) {
            throw findException(name);
        }
        try {
            //看缓存中是否有该类的对象
            T instance = (T) EXTENSION_INSTANCES.get(clazz);
            if (instance == null) {
                EXTENSION_INSTANCES.putIfAbsent(clazz, clazz.newInstance());
                instance = (T) EXTENSION_INSTANCES.get(clazz);
            }
            //向对象中注入依赖的属性（自动装配）
            injectExtension(instance);
            //创建 Wrapper 扩展对象（自动包装）
            Set<Class<?>> wrapperClasses = cachedWrapperClasses;
            if (wrapperClasses != null && !wrapperClasses.isEmpty()) {
                for (Class<?> wrapperClass : wrapperClasses) {
                    instance = injectExtension((T) wrapperClass.getConstructor(type).newInstance(instance));
                }
            }
            return instance;
        } catch (Throwable t) {
            throw new IllegalStateException("Extension instance(name: " + name + ", class: " +
                    type + ")  could not be instantiated: " + t.getMessage(), t);
        }
    }
```
这里运用到了两个扩展点的特性，分别是自动装配和自动包装

9. 向创建的拓展注入其依赖的属性
```
    private T injectExtension(T instance) {
        try {
            if (objectFactory != null) {
                //反射获得该类中所有的方法
                for (Method method : instance.getClass().getMethods()) {
                    //如果是set方法
                    if (method.getName().startsWith("set")
                            && method.getParameterTypes().length == 1
                            && Modifier.isPublic(method.getModifiers())) {
                        Class<?> pt = method.getParameterTypes()[0];
                        try {
                            //获得属性，比如StubProxyFactoryWrapper类中有Protocol protocol属性，
                            String property = method.getName().length() > 3 ? method.getName().substring(3, 4).toLowerCase() + method.getName().substring(4) : "";
                            //获得属性值，比如Protocol对象，也可能是Bean对象
                            Object object = objectFactory.getExtension(pt, property);
                            if (object != null) {
                                //注入依赖属性
                                method.invoke(instance, object);
                            }
                        } catch (Exception e) {
                            logger.error("fail to inject via method " + method.getName() + " of interface " + type.getName() + ": " + e.getMessage(), e);
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return instance;
    }
```
思路就是是先通过反射获得类中的所有方法，然后找到set方法，找到需要依赖注入的属性，然后把对象注入进去。

10. 获得扩展名对应的扩展实现类
```
    private Class<?> getExtensionClass(String name) {
        if (type == null)
            throw new IllegalArgumentException("Extension type == null");
        if (name == null)
            throw new IllegalArgumentException("Extension name == null");
        Class<?> clazz = getExtensionClasses().get(name);
        if (clazz == null)
            throw new IllegalStateException("No such extension \"" + name + "\" for " + type.getName() + "!");
        return clazz;
    }
```

11. 获得扩展实现类数组
```
    private Map<String, Class<?>> getExtensionClasses() {
        Map<String, Class<?>> classes = cachedClasses.get();
        if (classes == null) {
            synchronized (cachedClasses) {
                classes = cachedClasses.get();
                if (classes == null) {
                    //从配置文件中，加载扩展实现类数组
                    classes = loadExtensionClasses();
                    cachedClasses.set(classes);
                }
            }
        }
        return classes;
    }
```
这里思路就是先从缓存中取，如果缓存为空，则从配置文件中读取扩展实现类

12. 从配置文件中，加载拓展实现类数组
```
   private Map<String, Class<?>> loadExtensionClasses() {
        final SPI defaultAnnotation = type.getAnnotation(SPI.class);
        if (defaultAnnotation != null) {
            //@SPI内的默认值
            String value = defaultAnnotation.value();
            if ((value = value.trim()).length() > 0) {
                String[] names = NAME_SEPARATOR.split(value);
                //只允许有一个默认值
                if (names.length > 1) {
                    throw new IllegalStateException("more than 1 default extension name on extension " + type.getName() + ": " + Arrays.toString(names));
                }
                if (names.length == 1) cachedDefaultName = names[0];
            }
        }

        //从配置文件中加载实现类数组
        Map<String, Class<?>> extensionClasses = new HashMap<String, Class<?>>();
        loadDirectory(extensionClasses, DUBBO_INTERNAL_DIRECTORY);
        loadDirectory(extensionClasses, DUBBO_DIRECTORY);
        loadDirectory(extensionClasses, SERVICES_DIRECTORY);
        return extensionClasses;
    }
```
前一部分逻辑是在把SPI注解中的默认值放到缓存中去，加载实现类数组的逻辑是在后面几行，关键的就是loadDirectory方法，
并且这里可以看出去找配置文件访问的资源路径顺序。

13. 从一个配置文件中，加载拓展实现类数组
```
    private void loadDirectory(Map<String, Class<?>> extensionClasses, String dir) {
        //拼接接口全限定名，得到完整的文件名
        String fileName = dir + type.getName();
        try {
            Enumeration<java.net.URL> urls;
            //获取ExtensionLoader类信息
            ClassLoader classLoader = findClassLoader();
            if (classLoader != null) {
                urls = classLoader.getResources(fileName);
            } else {
                urls = ClassLoader.getSystemResources(fileName);
            }
            if (urls != null) {
                //遍历文件
                while (urls.hasMoreElements()) {
                    java.net.URL resourceURL = urls.nextElement();
                    loadResource(extensionClasses, classLoader, resourceURL);
                }
            }
        } catch (Throwable t) {
            logger.error("Exception when load extension class(interface: " +
                    type + ", description file: " + fileName + ").", t);
        }
    }
```
这边的思路是先获得完整的文件名，遍历每一个文件，在loadResource方法中去加载每个文件的内容

14. 加载文件中的内容
```
   private void loadResource(Map<String, Class<?>> extensionClasses, ClassLoader classLoader, java.net.URL resourceURL) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(resourceURL.openStream(), "utf-8"));
            try {
                String line;
                while ((line = reader.readLine()) != null) {
                    //跳过被#注释的内容
                    final int ci = line.indexOf('#');
                    if (ci >= 0) line = line.substring(0, ci);
                    line = line.trim();
                    if (line.length() > 0) {
                        try {
                            String name = null;
                            int i = line.indexOf('=');
                            if (i > 0) {
                                //根据"="拆分key跟value
                                name = line.substring(0, i).trim();
                                line = line.substring(i + 1).trim();
                            }
                            if (line.length() > 0) {
                                //加载扩展类
                                loadClass(extensionClasses, resourceURL, Class.forName(line, true, classLoader), name);
                            }
                        } catch (Throwable t) {
                            IllegalStateException e = new IllegalStateException("Failed to load extension class(interface: " + type + ", class line: " + line + ") in " + resourceURL + ", cause: " + t.getMessage(), t);
                            exceptions.put(line, e);
                        }
                    }
                }
            } finally {
                reader.close();
            }
        } catch (Throwable t) {
            logger.error("Exception when load extension class(interface: " +
                    type + ", class file: " + resourceURL + ") in " + resourceURL, t);
        }
    }
```
该类的主要的逻辑就是读取里面的内容，跳过“#”注释的内容，根据配置文件中的key=value的形式去分割，然后去加载value对应的类。

15. 根据配置文件中的value加载扩展类
```
   private void loadClass(Map<String, Class<?>> extensionClasses, java.net.URL resourceURL, Class<?> clazz, String name) throws NoSuchMethodException {
        //该类是否实现扩展接口
        if (!type.isAssignableFrom(clazz)) {
            throw new IllegalStateException("Error when load extension class(interface: " + type + ", class line: " + clazz.getName() + "), class " + clazz.getName() + "is not subtype of interface.");
        }
        //判断该类是否为扩展接口的适配器
        if (clazz.isAnnotationPresent(Adaptive.class)) {
            if (cachedAdaptiveClass == null) {
                cachedAdaptiveClass = clazz;
            } else if (!cachedAdaptiveClass.equals(clazz)) {
                throw new IllegalStateException("More than 1 adaptive class found: " + cachedAdaptiveClass.getClass().getName() + ", " + clazz.getClass().getName());
            }
        } else if (isWrapperClass(clazz)) {
            Set<Class<?>> wrappers = cachedWrapperClasses;
            if (wrappers == null) {
                cachedWrapperClasses = new ConcurrentHashSet<Class<?>>();
                wrappers = cachedWrapperClasses;
            }
            wrappers.add(clazz);
        } else {
            //通过反射获得构造器对象
            clazz.getConstructor();
            //未配置扩展名，自动生成，例如DemoFilter为 demo，主要用于兼容java SPI的配置。
            if (name == null || name.length() == 0) {
                name = findAnnotationName(clazz);
                if (name.length() == 0) {
                    throw new IllegalStateException("No such extension name for the class " + clazz.getName() + " in the config " + resourceURL);
                }
            }
            // 获得扩展名，可以是数组，有多个拓扩展名。
            String[] names = NAME_SEPARATOR.split(name);
            if (names != null && names.length > 0) {
                Activate activate = clazz.getAnnotation(Activate.class);
                //如果是自动激活的实现类，则加入到缓存
                if (activate != null) {
                    cachedActivates.put(names[0], activate);
                }
                for (String n : names) {
                    if (!cachedNames.containsKey(clazz)) {
                        cachedNames.put(clazz, n);
                    }
                    //缓存扩展实现类
                    Class<?> c = extensionClasses.get(n);
                    if (c == null) {
                        extensionClasses.put(n, clazz);
                    } else if (c != clazz) {
                        throw new IllegalStateException("Duplicate extension " + type.getName() + " name " + n + " on " + c.getName() + " and " + clazz.getName());
                    }
                }
            }
        }
    }
```
重点关注该方法中兼容了jdk的SPI思想。因为jdk的SPI相关的配置文件中是xx.yyy.DemoFilter，并没有key，也就是没有扩展名的概念，
所有为了兼容，通过xx.yyy.DemoFilter生成的扩展名为demo

16. 创建适配器类，类似于dubbo动态生成的Transporter$Adpative这样的类
```
    private Class<?> createAdaptiveExtensionClass() {
        //创建动态生成的适配器类代码
        String code = createAdaptiveExtensionClassCode();
        ClassLoader classLoader = findClassLoader();
        com.alibaba.dubbo.common.compiler.Compiler compiler = ExtensionLoader.getExtensionLoader(com.alibaba.dubbo.common.compiler.Compiler.class).getAdaptiveExtension();
        //编译代码，返回该类
        return compiler.compile(code, classLoader);
    }
```

部分方法比较浅显易懂，并且没有影响主功能，这里强调一点，其中的逻辑不难，难的是属性的含义要充分去品读理解，
弄清楚各个属性的含义后，再看一些逻辑就很浅显易懂了。如果真的看不懂属性的含义，可以进入到调用的地方，结合“语境”去理解。

### （六）AdaptiveExtensionFactory
```
@Adaptive
public class AdaptiveExtensionFactory implements ExtensionFactory {

    //扩展对象的集合，默认的可以分为dubbo 的SPI中接口实现类对象或者Spring bean对象
    private final List<ExtensionFactory> factories;

    public AdaptiveExtensionFactory() {
        ExtensionLoader<ExtensionFactory> loader = ExtensionLoader.getExtensionLoader(ExtensionFactory.class);
        List<ExtensionFactory> list = new ArrayList<ExtensionFactory>();
        //遍历所有支持的扩展名
        for (String name : loader.getSupportedExtensions()) {
            //扩展对象加入到集合中
            list.add(loader.getExtension(name));
        }
        //返回一个不可修改的集合
        factories = Collections.unmodifiableList(list);
    }

    @Override
    public <T> T getExtension(Class<T> type, String name) {
        for (ExtensionFactory factory : factories) {
            //通过扩展接口和扩展名获得扩展对象
            T extension = factory.getExtension(type, name);
            if (extension != null) {
                return extension;
            }
        }
        return null;
    }
}
```
1. factories是扩展对象的集合，当用户没有自己实现ExtensionFactory接口，则这个属性就只会有两种对象，
   分别是 SpiExtensionFactory和SpringExtensionFactory。
2. 构造器中是把所有支持的扩展名的扩展对象加入到集合
3. 实现了接口的getExtension方法，通过接口和扩展名来获取扩展对象。

### （七）SpiExtensionFactory
```
public class SpiExtensionFactory implements ExtensionFactory {
    @Override
    public <T> T getExtension(Class<T> type, String name) {
        //判断是否为接口，接口上是否有@SPI注解
        if (type.isInterface() && type.isAnnotationPresent(SPI.class)) {
            //获得扩展加载器
            ExtensionLoader<T> loader = ExtensionLoader.getExtensionLoader(type);
            if (!loader.getSupportedExtensions().isEmpty()) {
                //返回适配器类的对象
                return loader.getAdaptiveExtension();
            }
        }
        return null;
    }

}
```

### （八）ActivateComparator
该类在ExtensionLoader类的getActivateExtension方法中被运用到，作为自动激活拓展对象的排序器。
```
public class ActivateComparator implements Comparator<Object> {
    public static final Comparator<Object> COMPARATOR = new ActivateComparator();
    @Override
    public int compare(Object o1, Object o2) {
        //基本排序
        if (o1 == null && o2 == null) {
            return 0;
        }
        if (o1 == null) {
            return -1;
        }
        if (o2 == null) {
            return 1;
        }
        if (o1.equals(o2)) {
            return 0;
        }
        Activate a1 = o1.getClass().getAnnotation(Activate.class);
        Activate a2 = o2.getClass().getAnnotation(Activate.class);
        //使用Activate注解的 `after` 和 `before` 属性，排序
        if ((a1.before().length > 0 || a1.after().length > 0
                || a2.before().length > 0 || a2.after().length > 0)
                && o1.getClass().getInterfaces().length > 0
                && o1.getClass().getInterfaces()[0].isAnnotationPresent(SPI.class)) {
            ExtensionLoader<?> extensionLoader = ExtensionLoader.getExtensionLoader(o1.getClass().getInterfaces()[0]);
            if (a1.before().length > 0 || a1.after().length > 0) {
                String n2 = extensionLoader.getExtensionName(o2.getClass());
                for (String before : a1.before()) {
                    if (before.equals(n2)) {
                        return -1;
                    }
                }
                for (String after : a1.after()) {
                    if (after.equals(n2)) {
                        return 1;
                    }
                }
            }
            if (a2.before().length > 0 || a2.after().length > 0) {
                String n1 = extensionLoader.getExtensionName(o1.getClass());
                for (String before : a2.before()) {
                    if (before.equals(n1)) {
                        return 1;
                    }
                }
                for (String after : a2.after()) {
                    if (after.equals(n1)) {
                        return -1;
                    }
                }
            }
        }
        // 使用Activate注解的 `order` 属性，排序。
        int n1 = a1 == null ? 0 : a1.order();
        int n2 = a2 == null ? 0 : a2.order();
        // never return 0 even if n1 equals n2, otherwise, o1 and o2 will override each other in collection like HashSet
        return n1 > n2 ? 1 : -1;
    }
}
```

该文章讲解了dubbo的SPI扩展机制的实现原理，最关键的是弄清楚dubbo跟jdk在实现SPI的思想上做了哪些改进和优化，
解读dubbo SPI扩展机制最关键的是弄清楚@SPI、@Adaptive、@Activate三个注解的含义，大部分逻辑都被封装在ExtensionLoader类中。