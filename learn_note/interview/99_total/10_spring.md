## Spring 原理・6 张实战卡（社招高频版）

### 主线：一个 Bean 从生到用，容器做了什么

```
① 容器怎么起        → IOC 启动流程 refresh          （卡 1）
② Bean 怎么被造     → 生命周期四阶段                 （卡 2）
③ 属性怎么被塞      → @Autowired / @Resource        （卡 3）
④ 互相依赖怎么办    → 循环依赖 + 三级缓存            （卡 4）★
⑤ 方法怎么被增强    → AOP 织入 + 拦截器链            （卡 5）
⑥ 事务怎么落到库里  → @Transactional + 8 种失效      （卡 6）★
```

**开场话术（背下来，30 秒）：**

- Spring 面试不考注解列表，考的是 “**这个 Bean 是怎么被造出来、怎么被增强、怎么落到数据库的**”。
- 这条链一旦串通，**循环依赖、事务失效、AOP 原理**三大高频题一次全部覆盖。
- **卡 4 和卡 6 是必问核心，其余是加分项。**

**6 张卡一句话定位：**

表格

| 卡 | 主题 | 一句话 | 高频度 |
| --- | --- | --- | --- |
| 1 | IOC 启动 | `refresh()` 12 步，第 11 步最重 | ★★★★ |
| 2 | Bean 生命周期 | 实例化→注入→初始化→销毁 | ★★★★★ |
| 3 | 依赖注入 | `@Autowired` 按类型，`@Resource` 按名 | ★★★★ |
| 4 | 循环依赖 | 三级缓存，第三级为 AOP 而生 | ★★★★★ |
| 5 | AOP 织入 | 在初始化最后一步换掉 Bean | ★★★★★ |
| 6 | 事务失效 | 8 种场景，条条踩过都是坑 | ★★★★★ |

---

## 卡 1　IOC 容器启动流程　★★★★

**关键词**：`refresh()` / `BeanFactoryPostProcessor` / `BeanPostProcessor` / 第 11 步

**串线**：主线第 ① 环「容器怎么起」

**Q1 定义（20%）**

- 容器启动的总入口是 `AbstractApplicationContext.refresh()`。
- 它一共 12 步，**本质是 “准备 → 注册钩子 → 实例化所有 Bean → 收尾”**。
- 生死点在**第 11 步**：`finishBeanFactoryInitialization`。

**Q2 场景（50%）**

面试官问：Spring 启动阶段到底做了什么？

- 前 5 步：**准备环境、加载 BeanDefinition、执行 `BeanFactoryPostProcessor`**。
- 第 6 步：**注册 `BeanPostProcessor`**，为后面的初始化拦截做准备。
- 第 11 步：**`preInstantiateSingletons()` 把所有非懒加载单例 Bean 全部造出来**。
- 最后一步：**发布 `ContextRefreshedEvent`**，启动完成。

**Q3 源码（30%）**

追问：为什么启动慢？能不能优化？

- 慢就慢在**第 11 步串行实例化所有单例**，一个依赖一个。
- 优化手段：**`@Lazy` 延迟初始化、拆分模块、减少 `@PostConstruct` 里的重活**。
- 注意：`BeanFactoryPostProcessor` **早于所有普通 Bean 实例化**，常用来做配置替换。
- `BeanPostProcessor` **本身也是 Bean**，但注册早、实例化也早（第 6 步注册时创建）。

**项目钩子**

- “排查过启动耗时，定位到某个 Bean 在 `@PostConstruct` 里做了远程调用。”

---

## 卡 2　Bean 生命周期四阶段　★★★★★

**关键词**：`doCreateBean` / `populateBean` / `initializeBean` / Aware 接口

**串线**：主线第 ② 环「Bean 怎么被造」

**Q1 定义（20%）**

- 四阶段：**实例化 → 属性填充 → 初始化 → 销毁**。
- 对应源码三段：`createBeanInstance`、`populateBean`、`initializeBean`。
- **AOP 代理是在初始化的最后一步生成的**，不是创建时。

**Q2 场景（50%）**

面试官问：`@PostConstruct` 和 `afterPropertiesSet` 谁先？

- 顺序是：**`BeanPostProcessor.postProcessBeforeInitialization`**
- → **`@PostConstruct`** → **`InitializingBean.afterPropertiesSet`**
- → **`init-method`** → **`BeanPostProcessor.postProcessAfterInitialization`**。
- 所以：**`@PostConstruct` 早于 `afterPropertiesSet`，`init-method` 最晚。**

**Q3 源码（30%）**

追问：Aware 接口在什么时候被调？

- `BeanNameAware` / `BeanFactoryAware` 在**初始化阶段最前面**被调。
- `ApplicationContextAware` 更早，**在 `invokeAwareMethods` 里**。
- **`@Autowired` 是在 `populateBean`（第二阶段）处理的**，不是初始化阶段。
- 关键洞察：**属性注入早于 `@PostConstruct`**，所以 `@PostConstruct` 里可以放心用注入的依赖。

**项目钩子**

- “在 Bean 初始化前后用 `BeanPostProcessor` 做动态代理替换，实现自定义切面。”

---

## 卡 3　依赖注入：@Autowired vs @Resource　★★★★

**关键词**：byType / byName / `AutowiredAnnotationBeanPostProcessor` / `@Qualifier`

**串线**：主线第 ③ 环「属性怎么被塞」

**Q1 定义（20%）**

- `@Autowired` 是 **Spring 的**，默认**按类型（byType）**注入。
- `@Resource` 是 **JSR-250 标准**的，默认**按名称（byName）**注入。
- 前者可配 `required=false`，后者**没有这个属性**。

**Q2 场景（50%）**

面试官问：多个实现类怎么办？

- `@Autowired` 遇到多个候选会**报 `NoUniqueBeanDefinitionException`**。
- 解决：加 **`@Qualifier("beanName")`** 或把字段名改成 Bean 名。
- `@Resource(name="xxx")` 直接指定，**不会因为多实现报错**。
- 经验：**Spring 项目里 `@Autowired` 更常见，需要精确指定时才用 `@Resource`。**

**Q3 源码（30%）**

追问：注入是在哪个阶段做的？

- `AutowiredAnnotationBeanPostProcessor` 实现了 `InstantiationAwareBeanPostProcessor`。
- 它的 `postProcessProperties` 在 **`populateBean` 阶段被回调**，完成注入。
- 真正解析依赖的是 `DefaultListableBeanFactory.resolveDependency()`。
- 细节：**先按类型找候选 → 有 `@Qualifier` 再过滤 → 还多个就按字段名匹配 → 都不行报错。**

**项目钩子**

- “多支付渠道实现同一接口，用 `@Qualifier` 精确指定，避免启动报歧义。”

---

## 卡 4　循环依赖与三级缓存　★★★★★

**关键词**：`singletonObjects` / `earlySingletonObjects` / `singletonFactories` / 提前暴露

**串线**：主线第 ④ 环「互相依赖怎么办」

**Q1 定义（20%）**

- 循环依赖：**A 依赖 B，B 又依赖 A**，形成环。
- Spring 靠**三级缓存 + 提前暴露**打破环：A 造到一半先把自己放进缓存。
- **但只能解决 “属性注入” 的循环，解决不了 “构造器注入” 的循环。**

**Q2 场景（50%）**

面试官问：Spring 怎么解决循环依赖？

- **第一级 `singletonObjects`**：完整的成品 Bean。
- **第二级 `earlySingletonObjects`**：早期暴露的半成品。
- **第三级 `singletonFactories`**：存 `ObjectFactory`，**按需生成早期引用**。
- 流程：A 实例化后**先放第三级**→ 注入时发现要 B → 造 B → B 要 A → **从第三级拿到工厂，生成 A 的早期引用**。

**Q3 源码（30%）**

追问：为什么非要第三级，两级不行吗？

- 两级能破环，但**会破坏 AOP 代理**。
- 因为早期暴露的对象如果是**原始对象**，别人已经拿到引用，**后面再生成代理也换不掉了**。
- 第三级存的是 **`ObjectFactory` 这个 lambda**，**“等真有人要的时候才决定生成原对象还是代理”**。
- 对应 `getEarlyBeanReference()`：**AOP 的 `AbstractAutoProxyCreator` 在这里提前生成代理，且只生成一次**。
- **这就是 “为了 AOP 而生的一级缓存”。**

**项目钩子**

- “遇到循环依赖优先拆设计，实在拆不掉才用 `@Lazy` 打破构造期依赖。”

---

## 卡 5　AOP 织入与拦截器链　★★★★★

**关键词**：`AbstractAutoProxyCreator` / `ReflectiveMethodInvocation` / 递归 / `@Order`

**串线**：主线第 ⑤ 环「方法怎么被增强」

**Q1 定义（20%）**

- Spring AOP 的织入点是**初始化阶段的最后一步**：`postProcessAfterInitialization`。
- 它在此时把**原始 Bean 替换成代理对象**，放进容器。
- **所以容器里拿到的本来就是代理，不是原对象。**

**Q2 场景（50%）**

面试官问：多个切面怎么保证顺序？

- 实现 `Ordered` 接口或加 `@Order(n)`，**值越小越先执行**。
- 多个通知（Before / After / Around）**被组织成一个拦截器链**。
- 执行时**递归推进**，类似责任链，**每个拦截器决定 “继续” 还是 “短路”**。
- 注意：**`@Around` 可以完全控制是否调用 `proceed()`**，这是它比 Before/After 强的地方。

**Q3 源码（30%）**

追问：代理对象里方法调用怎么走？

- `JdkDynamicAopProxy.invoke()` 会构造 **`ReflectiveMethodInvocation`**。
- 它持有 `List<Interceptor>` 和 `currentInterceptorIndex`，**`proceed()` 里 index++ 递归**。
- 走完了才真正反射调用目标方法。
- 关键：**CGLIB 路径也一样，最终都回归到 `MethodInvocation.proceed()` 这个统一抽象。**

**项目钩子**

- “日志、鉴权、灰度都做成切面，用 `@Order` 明确先后，避免互相干扰。”

---

## 卡 6　@Transactional 与 8 种失效场景　★★★★★

**关键词**：`TransactionInterceptor` / 传播行为 / 自调用 / `rollbackFor`

**串线**：主线第 ⑥ 环「事务怎么落到库里」

**Q1 定义（20%）**

- `@Transactional` **本质是 AOP + `TransactionInterceptor`**。
- 拦截器在**方法前后开启 / 提交 / 回滚事务**。
- 它**依赖代理**，所以**所有 AOP 失效的场景，事务也随之失效**。

**Q2 场景（50%）**

面试官问：哪些情况 `@Transactional` 会失效？

- **① 方法不是 `public`**：静默不生效，**不报错，最容易踩**。
- **② 同类自调用**：`this.methodB()` 不走代理，AOP 直接失效。
- **③ 异常被吞**：`try-catch` 后没重新抛出，拦截器收不到异常。
- **④ 异常类型不匹配**：默认只回滚 **`RuntimeException` 和 `Error`**，受检异常不回滚。

**继续补完剩下四条（这张卡一定要说全 8 条）：**

- **⑤ 表引擎不支持**：MyISAM 没有事务，只有 InnoDB 有。
- **⑥ 类没被 Spring 管**：`new` 出来的对象不是 Bean，没有代理。
- **⑦ 传播行为设错**：`NOT_SUPPORTED` / `NEVER` 会挂起或禁止事务。
- **⑧ 多数据源没指定**：多个 `TransactionManager` 时不指定会用错或报错。

**Q3 源码（30%）**

追问：默认回滚规则是怎么定的？

- `TransactionAttribute` 默认 `rollbackOn = RuntimeException | Error`。
- **受检异常（`IOException` 等）默认不回滚**，必须写 `rollbackFor = Exception.class`。
- 传播行为 7 种：**`REQUIRED`（默认）/ `REQUIRES_NEW` / `SUPPORTS` / `NOT_SUPPORTED` / `MANDATORY` / `NEVER` / `NESTED`**。
- 自调用的解法：**注入自己、或 `AopContext.currentProxy()`、或拆到另一个 Bean**。

**项目钩子**

- “排查过一次事务不回滚，最后定位到同类自调用 + 吞异常，两个坑叠在一起。”