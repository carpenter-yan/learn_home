##加载
###使用样例
MyTestBean bean=(MyTestBean) bf.getBean("myTestBean");

AbstractBeanFactory.getBean
```
public Object getBean(String name) throws BeansException {
    return doGetBean(name, null, null, false);
}
```

AbstractBeanFactory.doGetBean
```
protected <T> T doGetBean(
        final String name, final Class<T> requiredType, final Object[] args, boolean typeCheckOnly)
        throws BeansException {

    //1. 转换对应beanName
    /** 或许很多人不理解转换对应beanName是什么意思，传人的参数name不就是beanName吗？其实不是，这里传入的参数可能是别名，也可能是FactoryBean，
      *所以需要进行一系列的解析，这些解析内容包括如下内容。
      * 1.1去除FactoryBean的修饰符，也就是如果name="&aa"，那么会首先去除&而使name="aa" 。
      * 1.2取指定alias所表示的最终beanName，例如别名A指向名称为B的bean则返回B;若别名A指向别名B，而B又指向名称为C的bean 则返回C。*/
    final String beanName = transformedBeanName(name);
    Object bean;

    // Eagerly check singleton cache for manually registered singletons.
    //2. 尝试从缓存中加载单例
    /** 单例在Spring的同一个容器内只会被创建一次，后续再获取bean，就直接从单例缓存中获取了。
     * 当然这里也只是尝试加载，首先尝试从缓存中加载，如果加载不成功则再次尝试从singletonFactories中加载。
     * 因为在创建单例bean的时候会存在依赖注入的情况，而在创建依赖的时候为了避免循环依赖，
     * 在Spring中创建bean的原则是不等bean创建完成就会将创建bean的ObjectFactory提早曝光加入到缓存中，
     * 一旦下一个bean创建时候需要依赖上一个bean则直接使用objectFactory。*/
    Object sharedInstance = getSingleton(beanName);
    if (sharedInstance != null && args == null) {
        if (logger.isDebugEnabled()) {
            if (isSingletonCurrentlyInCreation(beanName)) {
                logger.debug("Returning eagerly cached instance of singleton bean '" + beanName +
                        "' that is not fully initialized yet - a consequence of a circular reference");
            }
            else {
                logger.debug("Returning cached instance of singleton bean '" + beanName + "'");
            }
        }
        //3. bean的实例化
        /** 如果从缓存中得到了bean的原始状态，则需要对bean进行实例化。
         * 这里有必要强调一下，缓存中记录的只是最原始的bean状态，并不一定是我们最终想要的bean。
         * 举个例子，假如我们需要对工厂bean进行处理，那么这里得到的其实是工厂bean的初始状态，
         * 但是我们真正需要的是工厂bean中定义的factory-method方法中返回的bean，而getObjectForBeanInstance就是完成这个工作的。*/
        bean = getObjectForBeanInstance(sharedInstance, name, beanName, null);
    }
    else {
        // Fail if we're already creating this bean instance:
        // We're assumably within a circular reference.
        //4.循环依赖检查
        /** 只有在单例情况下才会尝试解决循环依赖，如果存在A中有B的属性，B中有A的属性，
         * 那么当依赖注入的时候，就会产生当A还未创建完的时候因为对于B的创建再次返回创建A,造成循环依赖，
         * 也就是情况：isPrototypeCurrentlyInCreation(beanName)判断true 。*/
        if (isPrototypeCurrentlyInCreation(beanName)) {
            throw new BeanCurrentlyInCreationException(beanName);
        }

        // Check if bean definition exists in this factory.
        //5.如果beanDefinitionMap中也就是在所有已经加载的类中不包括beanName则尝试从parentBeanFactory中检测
        /** 从代码上看，如果缓存没有数据的话直接转到父类工厂上去加载了，这是为什么呢？
         * 可能读者忽略了一个很重要的判断条件：parentBeanFactory != null && !containsBeanDefinition(beanName) 。
         * parentBeanFactory如果为空， 则其他一切都是浮云，这个没什么说的，
         * 但是！containsBeanDefinition(beanName）就比较重要了，
         * 它是在检测如果当前加载的XML配置文件中不包含beanName所对应的配置，就只能到parentBeanFactory去尝试下了，
         * 然后再去递归的调用getBean方法。*/
        BeanFactory parentBeanFactory = getParentBeanFactory();
        if (parentBeanFactory != null && !containsBeanDefinition(beanName)) {
            // Not found -> check parent.
            String nameToLookup = originalBeanName(name);
            //递归到parentBeanFactory中寻找
            if (args != null) {
                // Delegation to parent with explicit args.
                return (T) parentBeanFactory.getBean(nameToLookup, args);
            }
            else {
                // No args -> delegate to standard getBean method.
                return parentBeanFactory.getBean(nameToLookup, requiredType);
            }
        }

        //如果不是仅仅做类型检查则创建Bean，这里做创建记录。
        if (!typeCheckOnly) {
            markBeanAsCreated(beanName);
        }

        try {
            //6.将存储XML配置文件的GenericBeanDefinition转换为RootBeanDefinition
            //如果指定BeanName是子Bean的话同时会合并父类的相关属性
            /** 因为从XML配置文件中读取到的bean信息是存储在GenericBeanDefinition中的，
             * 但是所有的bean后续处理都是针对于RootBeanDefinition的，所以这里需要进行一个转换，
             * 转换的同时如果父类bean不为空的话，则会一并合并父类的属性。*/
            final RootBeanDefinition mbd = getMergedLocalBeanDefinition(beanName);
            checkMergedBeanDefinition(mbd, beanName, args);

            // Guarantee initialization of beans that the current bean depends on.
            //7.如果存在依赖则递归实例化依赖的Bean
            /** 因为bean的初始化过程中很可能会用到某些属性，而某些属性很可能是动态配置的，并且配置成依赖于其他的bean，
             * 那么这个时候就有必要先加载依赖的bean，所以，在Spring的加载顺序中，在初始化某一个bean的时候首先会初始化这个bean所对应的依赖。*/
            String[] dependsOn = mbd.getDependsOn();
            if (dependsOn != null) {
                for (String dep : dependsOn) {
                    if (isDependent(beanName, dep)) {
                        throw new BeanCreationException(mbd.getResourceDescription(), beanName,
                                "Circular depends-on relationship between '" + beanName + "' and '" + dep + "'");
                    }
                    //缓存依赖信息
                    registerDependentBean(dep, beanName);
                    getBean(dep);
                }
            }

            // Create bean instance.
            //8. 针对不同的scope进行bean的创建
            /** 我们都知道，在Spring中存在着不同的scope，其中默认的是singleton，但是还有些其他的配置诸如prototype、request之类的。
             * 在这个步骤中，Spring会根据不同的配置进行不同的初始化策略。*/
            //单例Bean实例化
            if (mbd.isSingleton()) {
                sharedInstance = getSingleton(beanName, new ObjectFactory<Object>() {
                    @Override
                    public Object getObject() throws BeansException {
                        try {
                            //*8*.普通bean封装一层FactoryBean
                            return createBean(beanName, mbd, args);
                        }
                        catch (BeansException ex) {
                            // Explicitly remove instance from singleton cache: It might have been put there
                            // eagerly by the creation process, to allow for circular reference resolution.
                            // Also remove any beans that received a temporary reference to the bean.
                            destroySingleton(beanName);
                            throw ex;
                        }
                    }
                });
                bean = getObjectForBeanInstance(sharedInstance, name, beanName, mbd);
            }
            //原型bean实例化
            else if (mbd.isPrototype()) {
                // It's a prototype -> create a new instance.
                Object prototypeInstance = null;
                try {
                    beforePrototypeCreation(beanName);
                    prototypeInstance = createBean(beanName, mbd, args);
                }
                finally {
                    afterPrototypeCreation(beanName);
                }
                bean = getObjectForBeanInstance(prototypeInstance, name, beanName, mbd);
            }
            //指定scope类型bean实例化
            else {
                String scopeName = mbd.getScope();
                final Scope scope = this.scopes.get(scopeName);
                if (scope == null) {
                    throw new IllegalStateException("No Scope registered for scope name '" + scopeName + "'");
                }
                try {
                    Object scopedInstance = scope.get(beanName, new ObjectFactory<Object>() {
                        @Override
                        public Object getObject() throws BeansException {
                            beforePrototypeCreation(beanName);
                            try {
                                return createBean(beanName, mbd, args);
                            }
                            finally {
                                afterPrototypeCreation(beanName);
                            }
                        }
                    });
                    bean = getObjectForBeanInstance(scopedInstance, name, beanName, mbd);
                }
                catch (IllegalStateException ex) {
                    throw new BeanCreationException(beanName,
                            "Scope '" + scopeName + "' is not active for the current thread; consider " +
                            "defining a scoped proxy for this bean if you intend to refer to it from a singleton",
                            ex);
                }
            }
        }
        catch (BeansException ex) {
            cleanupAfterBeanCreationFailure(beanName);
            throw ex;
        }
    }

    // Check if required type matches the type of the actual bean instance.
    //9. 类型转换
    /** 程序到这里返回bean后已经基本结束了，通常对该方法的调用参数requiredType是为空的，
     * 但是可能会存在这样的情况，返回的bean其实是个String ，但是requiredType却传入Integer类型，那么这时候本步骤就会起作用了，
     * 它的功能是将返回的bean转换为requiredType所指定的类型。当然，String转换为Integer是最简单的一种转换，
     * 在Spring中提供了各种各样的转换器，用户也可以自己扩展转换器来满足需求。*/
    if (requiredType != null && bean != null && !requiredType.isInstance(bean)) {
        try {
            return getTypeConverter().convertIfNecessary(bean, requiredType);
        }
        catch (TypeMismatchException ex) {
            if (logger.isDebugEnabled()) {
                logger.debug("Failed to convert bean '" + name + "' to required type '" +
                        ClassUtils.getQualifiedName(requiredType) + "'", ex);
            }
            throw new BeanNotOfRequiredTypeException(name, requiredType, bean.getClass());
        }
    }
    return (T) bean;
}
```
###FactoryBean的使用
当配置文件中<bean>的class属性是FactoryBean时，通过getBean()方法返回的不是FactoryBean本身，而是FactoryBean#getObject()方法所返回的对象，
相当于FactoryBean#getObject()代理了getBean()方法。

<bean id="car" class="com.test.factoryBean.CarFactoryBean" carInfo="超级跑次，400,200000”/>
当调用getBean("car")时，Spring通过反射机制发现CarFactoryBean实现了FactoryBean的接口，
这时Spring容器就调用接口方法CarFactoryBean#getObject()方法返回。
如果希望获取CarFactoryBean的实例，则需要在使用getBean(beanName)方法时在beanName前显示的加上"&"前缀，例如getBean("&car");
