##  Spring Boot・6 张实战卡（社招高频版）

### 主线：从敲下 main，到服务能对外接请求

```
① main 一行起服务   → SpringApplication.run 做了什么   （卡 1）
② 一个注解顶三个    → @SpringBootApplication 三合一     （卡 2）
③ 依赖为什么自动生效 → Starter + 自动装配原理           （卡 3）★
④ 配置怎么层层覆盖  → 配置源优先级                      （卡 4）
⑤ Web 容器从哪来的  → 内嵌 Tomcat 的创建时机            （卡 5）
⑥ 3.x 到底改了什么  → imports 文件 + Jakarta            （卡 6）
```

**开场话术（背下来，30 秒）：**

- Spring Boot **不是新框架**，是 “**Spring 的启动器 + 自动装配器**”。
- 面试只考四件事：**怎么起、怎么自动装配、配置怎么覆盖、Web 容器哪来的**。
- **卡 3 是灵魂**，把它说透，Spring Boot 整块就通了。

**6 张卡一句话定位：**

表格

| 卡 | 主题 | 一句话 | 高频度 |
| --- | --- | --- | --- |
| 1 | 启动流程 | `run()` 造容器 + refresh + 发事件 | ★★★★ |
| 2 | 三合一注解 | 配置类 + 开自动装配 + 扫包 | ★★★★ |
| 3 | 自动装配 | 读 `spring.factories` + 条件过滤 | ★★★★★ |
| 4 | 配置优先级 | 命令行 > 环境变量 > profile > 主配置 | ★★★★ |
| 5 | 内嵌 Tomcat | 把容器当普通 Bean 创建 | ★★★★ |
| 6 | Boot 3 变化 | `imports` 替代 `factories`、`jakarta` 替 `javax` | ★★★★ |

---

## 卡 1　SpringApplication.run 做了什么　★★★★

**关键词**：`SpringApplication` / `createApplicationContext` / `refreshContext` / `callRunners`

**串线**：主线第 ① 环「main 一行起服务」

**Q1 定义（20%）**

- 入口是 `SpringApplication.run(启动类.class, args)`。
- 它做三件事：**创建 `ApplicationContext`、刷新容器（造全部 Bean）、启动内嵌 Web 容器**。
- 最后执行 `ApplicationRunner` / `CommandLineRunner`，**项目启动时的钩子**。

**Q2 场景（50%）**

面试官问：启动阶段有哪些扩展点？

- `ApplicationContextInitializer`：**容器 refresh 之前**，能改环境。
- `ApplicationListener`：监听 `ContextRefreshedEvent` 等事件。
- `ApplicationRunner` / `CommandLineRunner`：**容器完全就绪后**执行。
- `EnvironmentPostProcessor`：**配置文件加载阶段**就能介入。

**Q3 源码（30%）**

追问：`run()` 内部大致几步？

- `prepareEnvironment` → **准备 Environment、加载配置文件**。
- `createApplicationContext` → **按 web 类型选 `AnnotationConfigServletWebServerApplicationContext`**。
- `refreshContext` → **`refresh()` 12 步，等价于上一块讲的 IOC 启动**。
- `afterRefresh` → 调 `callRunners`。
- 关键：**`run()` 本身很薄，重活全在 `refresh()` 里。**

**项目钩子**

- “用 `ApplicationRunner` 做启动自检，服务起来先校验依赖组件是否可用。”

---

## 卡 2　@SpringBootApplication 三合一　★★★★

**关键词**：`@SpringBootConfiguration` / `@EnableAutoConfiguration` / `@ComponentScan`

**串线**：主线第 ② 环「一个注解顶三个」

**Q1 定义（20%）**

- 它由三个注解组成：**`@SpringBootConfiguration` + `@EnableAutoConfiguration` + `@ComponentScan`**。
- `@SpringBootConfiguration` **本质就是 `@Configuration`**，标记这是配置类。
- `@ComponentScan` **默认扫 “启动类所在包及其子包”**。

**Q2 场景（50%）**

面试官问：为什么我的 Bean 扫描不到？

- 因为**启动类放错包了**—— 它只能扫自己所在包往下。
- 解决：**把启动类放到最顶层包**，或手动加 `scanBasePackages`。
- 这也是 “**为什么 Spring Boot 项目都是 `com.xxx` 下一堆子包**” 的原因。

**Q3 源码（30%）**

追问：`@EnableAutoConfiguration` 里到底有什么？

- 它 = **`@AutoConfigurationPackage` + `@Import(AutoConfigurationImportSelector.class)`**。
- `@AutoConfigurationPackage` 干一件事：**把启动类所在包登记下来**，供 JPA 等组件扫描实体。
- `AutoConfigurationImportSelector` **才是自动装配的执行者**，下一张卡讲它。

**项目钩子**

- “多模块项目里启动类必须放根包，否则子模块组件扫描不到。”

---

## 卡 3　自动装配原理　★★★★★（最核心）

**关键词**：`spring.factories` / `AutoConfigurationImportSelector` / `@ConditionalOnClass` / SPI

**串线**：主线第 ③ 环「依赖为什么自动生效」

**Q1 定义（20%）**

- 自动装配 = **扫描所有 jar 里的 `META-INF/spring.factories`**，把候选配置类捞出来。
- 再按 **`@ConditionalOnXxx` 条件注解过滤**，只留下当前环境该生效的。
- **所以引一个 starter，本质是引了一堆 “待条件生效” 的配置类。**

**Q2 场景（50%）**

面试官问：为什么加了 `spring-boot-starter-data-redis` 就有 `RedisTemplate`？

- starter 本身**只有依赖，没有代码**（除了极少数）。
- 真正的装配来自 **`spring-boot-autoconfigure` 里的 `RedisAutoConfiguration`**。
- 它在 `spring.factories` 里登记，**被 `AutoConfigurationImportSelector` 捞出来**。
- 再被 **`@ConditionalOnClass(RedisOperations.class)`** 判断 ——**没引 redis 客户端就不生效**。

**Q3 源码（30%）**

追问：完整链路走一遍？

- `selectImports()` → `getAutoConfigurationEntry()` → `getCandidateConfigurations()`。
- 后者调 **`SpringFactoriesLoader.loadFactoryNames(EnableAutoConfiguration.class)`**。
- **它遍历所有 classpath 的 `META-INF/spring.factories`**，读 key 对应的类名列表。
- 然后**去重、排除 `@EnableAutoConfiguration` 里的 exclusions、按条件注解过滤**。
- 最后返回的类名数组**被当作配置类导入容器**，里面的 `@Bean` 方法就生效了。

**五个最高频的条件注解（必须背）：**

- **`@ConditionalOnClass`**：classpath 有某个类才生效。
- **`@ConditionalOnMissingBean`**：**容器里没有才生效** → **这就是用户自定义覆盖默认配置的原理**。
- **`@ConditionalOnProperty`**：配置项满足才生效。
- **`@ConditionalOnWebApplication`**：是 Web 应用才生效。
- **`@ConditionalOnBean`**：有某个 Bean 才生效。

**项目钩子**

- “写过自定义 starter，把自己的配置类登记到 `spring.factories`，引依赖即生效。”

---

## 卡 4　配置加载优先级　★★★★

**关键词**：`PropertySource` / profile / `@ConfigurationProperties` / `@Value`

**串线**：主线第 ④ 环「配置怎么层层覆盖」

**Q1 定义（20%）**

- Spring Boot 有 **10+ 种配置源**，按**固定优先级层层覆盖**，高的赢。
- 优先级从高到低：**命令行 > 环境变量 > profile 文件 > 主配置文件 > 默认值**。
- `application-{profile}.yml` **覆盖 `application.yml`**，不是合并 —— 同 key 后者被前者替换。

**Q2 场景（50%）**

面试官问：`@Value` 和 `@ConfigurationProperties` 怎么选？

- **`@Value`**：单个值、支持 SpEL，**不支持复杂类型**。
- **`@ConfigurationProperties`**：**批量绑定一整个前缀**，支持嵌套对象、列表、校验。
- 实践：**配置项少用 `@Value`，成组配置（如数据源）用 `@ConfigurationProperties`**。

**Q3 源码（30%）**

追问：优先级顺序能不能说全？

- 高 → 低：**命令行参数 → `SPRING_APPLICATION_JSON` → Servlet 参数 → JNDI**
- → **Java System 属性（`-D`）→ OS 环境变量 → profile 文件**
- → **`application.yml` → `@PropertySource` → 默认值**。
- 细节：**profile 文件又分 “jar 外” 和 “jar 内”，外部的优先级更高**—— 这是容器化部署的常用手段。

**项目钩子**

- “多环境用 profile 拆分，敏感配置走环境变量，避免打进镜像。”

---

## 卡 5　内嵌 Tomcat 从哪来的　★★★★

**关键词**：`ServletWebServerFactory` / `onRefresh` / `TomcatWebServer`

**串线**：主线第 ⑤ 环「Web 容器从哪来的」

**Q1 定义（20%）**

- **Spring Boot 不部署到外部 Tomcat，而是把 Tomcat 当普通 Bean 创建。**
- 这是它和传统 Spring MVC 最本质的区别：**容器是应用的一部分**。
- 所以打出来的 jar **可以直接 `java -jar` 跑**，自带 Web 容器。

**Q2 场景（50%）**

面试官问：怎么换成 Jetty 或 Undertow？

- 排除 `spring-boot-starter-tomcat`，加 `spring-boot-starter-jetty` 即可。
- 原理：**`ServletWebServerFactory` 是个策略接口**，换实现就换容器。
- 注意：**换容器会带来行为差异**（如 Tomcat 的 `maxThreads` 配置在 Jetty 里不适用）。

**Q3 源码（30%）**

追问：容器是什么时候启动的？

- 在 `ServletWebServerApplicationContext.onRefresh()` 里。
- 它调 **`createWebServer()` → `getWebServerFactory().getWebServer(...)`**。
- 默认工厂是 **`TomcatServletWebServerFactory`**，返回 `TomcatWebServer`。
- **`TomcatWebServer` 构造时 `initialize()` 就 `tomcat.start()` 了**，早于容器完全 ready。
- 最后 `finishRefresh()` 发布 `ServletWebServerInitializedEvent`，**端口此时才真正对外可用**。

**项目钩子**

- “为了压测稳定性，替换过 Web 容器，也调过 Tomcat 的线程池和连接数参数。”

---

## 卡 6　Spring Boot 3 改了什么　★★★★

**关键词**：`AutoConfiguration.imports` / Jakarta EE 9 / Java 17 / GraalVM

**串线**：主线第 ⑥ 环「3.x 到底改了什么」

**Q1 定义（20%）**

- Boot 3 是一次**破坏性升级**，不是小版本迭代。
- 三件大事：**基线升到 Java 17、`javax.*` 全换 `jakarta.*`、装配文件换位置**。
- 所以 **2.x 升 3.x 基本都要改代码，不是改配置就能过**。

**Q2 场景（50%）**

面试官问：升级踩过什么坑？

- **`javax.servlet` → `jakarta.servlet`**，所有相关 import 全要改。
- **`spring.factories` 里的装配 key 不再生效**，要迁到新文件。
- 第三方 starter **如果没发布 3.x 兼容版，直接不可用**（这是最大阻塞）。
- 配置项也有改名和废弃，**`spring.xxx` 一批属性失效**。

**Q3 源码（30%）**

追问：装配文件具体怎么变的？

- Boot 2.7 起引入新文件：**`META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports`**。
- 格式变了：**从 `key=value 列表`变成 “一行一个类名”**，更简洁。
- Boot 3.0 **`spring.factories` 的自动装配 key 彻底不再被读取**。
- 底层原因：**Jakarta EE 9 把 `javax` 包名迁移到 `jakarta`**，为了跟旧生态彻底切割。

**项目钩子**

- “做过 Boot 2 升 3 的迁移，主要工作是改包名和替换不兼容的 starter。”