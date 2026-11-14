## MyBatis / MyBatis-Plus・6 张实战卡（社招高频版）

### 主线：一次方法调用怎么变成 SQL，再变回对象

```
① 接口没实现类怎么调   → Mapper 动态代理 + 执行流程   （卡 1）
② 参数怎么安全进 SQL   → #{} vs ${}                  （卡 2）★
③ 结果怎么变成对象     → ResultMap + TypeHandler      （卡 3）
④ 重复查询怎么变快     → 一级缓存 / 二级缓存          （卡 4）★
⑤ 分页怎么插进去       → 插件机制（Interceptor）      （卡 5）
⑥ 单表 CRUD 怎么少写   → MyBatis-Plus                （卡 6）
```

**开场话术（背下来，30 秒）：**

- MyBatis 只有一条主线：**一次方法调用怎么变成 SQL，又怎么变回对象**。
- 六张卡走完这条链：**代理 → 参数 → 结果 → 缓存 → 插件 → 增强**。
- **卡 2（`#{}` 和 `${}`）和卡 4（缓存）是两题必问**，其余是加分项。
- MyBatis-Plus 一句话：**“只做增强，不做改变”**。

**6 张卡一句话定位：**

表格

| 卡 | 主题 | 一句话 | 高频度 |
| --- | --- | --- | --- |
| 1 | Mapper 代理 | 接口没实现类，靠 JDK 动态代理 | ★★★★★ |
| 2 | `#{}` vs `${}` | 预编译 vs 字符串替换 | ★★★★★ |
| 3 | ResultMap | 列名怎么对上属性名 | ★★★★ |
| 4 | 缓存 | 一级 Session 级、二级 namespace 级 | ★★★★★ |
| 5 | 插件 | 只能拦四大对象 | ★★★★ |
| 6 | MyBatis-Plus | BaseMapper + 分页插件 + 逻辑删除 | ★★★★ |

---

## 卡 1　Mapper 代理与执行流程　★★★★★

**关键词**：`MapperProxy` / `SqlSession` / `Executor` / 四大对象

**串线**：主线第 ① 环「接口没实现类怎么调」

**Q1 定义（20%）**

- Mapper 接口**没有实现类**，靠 **JDK 动态代理**生成 `MapperProxy`。
- 一次调用要穿过 **SqlSession → Executor → StatementHandler → ResultSetHandler**。
- **中间有四个对象，统称 “四大对象”**，也是插件的唯一拦截点。

**Q2 场景（50%）**

面试官问：为什么 Mapper 接口能直接用？

- 启动时 `MapperRegistry` 扫描接口，**为每个接口注册一个 `MapperProxyFactory`**。
- 真正注入的是 **`MapperProxy` 生成的代理对象**。
- 代理拿到方法信息 → **找到对应的 `MappedStatement`** → 走 `SqlSession` 执行。

**Q3 源码（30%）**

追问：执行流程展开说说？

- `SqlSession.selectOne()` → **`Executor.query()`**。
- `Executor` 有三种：**Simple / Reuse / Batch**，默认 **SimpleExecutor**。
- 然后 **`StatementHandler.prepare()` 创建 `PreparedStatement`**。
- **`ParameterHandler` 设置参数**，执行，**`ResultSetHandler` 映射结果**。
- **这四层每一层都可被插件拦截**，这就是卡 5 的基础。

**项目钩子**

- “加过 SQL 耗时监控插件，就是拦 `Executor.query` 做的。”

---

## 卡 2　#{} vs ${}　★★★★★（必问）

**关键词**：预编译 / `PreparedStatement` / 字符串替换 / SQL 注入

**串线**：主线第 ② 环「参数怎么安全进 SQL」

**Q1 定义（20%）**

- **`#{}`** 是**预编译占位符**，最终变成 `?`，靠 `PreparedStatement` 绑定参数。
- **`${}`** 是**字符串直接替换**，SQL 文本在解析阶段就被拼好了。
- 一句话：**`#{}` 传值，`${}` 拼串。**

**Q2 场景（50%）**

面试官问：为什么 `${}` 会 SQL 注入？什么时候不得不用？

- `#{}` 参数和 SQL **分离传输**，**天然防注入**。
- `${}` 是**文本替换进 SQL**，用户传什么就拼什么，**直接注进去**。
- 不得不用 `${}` 的场景：**动态表名、动态列名、`ORDER BY` 字段**。
- 因为这些是 **SQL 标识符**，**预编译不支持占位符替换标识符**。
- 用 `${}` 必须做 **白名单校验**，不能来自用户输入。

**Q3 源码（30%）**

追问：底层差别在哪？

- `#{}` 在 `SqlSource` 解析时生成 **`ParameterMapping`**，运行时由 **`ParameterHandler.setParameters()`** 调 `setXxx()`。
- `${}` 在 **`SqlSource` 构建阶段就被 `PropertyTokenizer` 替换掉**，SQL 里直接是最终文本。
- 所以 **`${}` 连类型转换的机会都没有**，纯字符串。
- 记住结论：**能用 `#{}` 一律用 `#{}`。**

**项目钩子**

- “安全扫描报过 `${}` 注入，最后改成白名单映射动态列名。”

---

## 卡 3　ResultMap 与结果映射　★★★★

**关键词**：`ResultMap` / 自动映射 / 驼峰 / `TypeHandler` / 嵌套映射

**串线**：主线第 ③ 环「结果怎么变成对象」

**Q1 定义（20%）**

- `resultType` 走**自动映射**：列名和属性名对上就填。
- `resultMap` 走**显式映射**：处理字段不一致、嵌套对象、集合。
- 两者不能同时用，**二选一**。

**Q2 场景（50%）**

面试官问：为什么查出来一堆 null？

- **列名和属性名对不上**，且**驼峰映射没开**。
- 比如 `user_name` 对 `userName`，**需要 `mapUnderscoreToCamelCase=true`**。
- **关键坑：原生 MyBatis 默认是 `false`**，**MyBatis-Plus 默认是 `true`**。
- 所以纯 MyBatis 项目一定要显式配这一项。

**Q3 源码（30%）**

追问：复杂映射怎么处理？

- 一对一用 **`<association>`**，一对多用 **`<collection>`**。
- 底层靠 **`ResultSetWrapper` + `TypeHandler`** 做类型转换。
- `TypeHandler` 可以自定义，**枚举、JSON 字段都靠它映射**。
- 嵌套查询会引发 **N+1**，用 **`fetchType="lazy"`** 延迟加载缓解，但**根治要改成 join**。

**项目钩子**

- “枚举字段落库是 code，出库要转枚举，靠自定义 `TypeHandler` 统一处理。”

---

## 卡 4　一级缓存 / 二级缓存　★★★★★（必问）

**关键词**：`SqlSession` /namespace/ 失效条件 / 脏读

**串线**：主线第 ④ 环「重复查询怎么变快」

**Q1 定义（20%）**

- **一级缓存**：**`SqlSession` 级别**，**默认开启，无法关闭**。
- **二级缓存**：**namespace（Mapper）级别**，**默认关闭**，要 `<cache/>` 才开。
- 底层都是 `PerpetualCache`，**本质就是 HashMap**。

**Q2 场景（50%）**

面试官问：为什么生产不建议开二级缓存？

- 二级缓存**跨 `SqlSession` 共享**，**同一 namespace 的增删改会清空它**。
- **但多表关联查询时，另一张表更新，本表缓存不会被清** → **脏读**。
- 而且**多实例部署时，各节点缓存不同步** → 更脏。
- 结论：**生产环境基本不推荐开二级缓存，用 Redis 做业务缓存更可靠。**

**Q3 源码（30%）**

追问：一级缓存什么时候失效？

- **执行 `insert/update/delete` 会自动 `clearCache()`**（同一 `SqlSession` 内）。
- **`SqlSession.close()`** 或**事务提交**后失效。
- 手动 **`sqlSession.clearCache()`** 也会清。
- 冷知识：**`localCacheScope=STATEMENT` 可以把它降级成 “语句级”**，相当于变相关闭。

**项目钩子**

- “同一 `SqlSession` 内先查后改再查，查出旧值，就是一级缓存没清干净。”

---

## 卡 5　插件机制（Interceptor）　★★★★

**关键词**：`Interceptor` / 四大对象 / `Plugin.wrap` / PageHelper

**串线**：主线第 ⑤ 环「分页怎么插进去」

**Q1 定义（20%）**

- MyBatis 插件 = **拦截器 + JDK 动态代理**，**只作用于四大对象**。
- 四大对象：**`Executor`、`StatementHandler`、`ParameterHandler`、`ResultSetHandler`**。
- 多个插件会组成**拦截器链**，**按配置顺序依次包装**。

**Q2 场景（50%）**

面试官问：PageHelper 是怎么实现分页的？

- 它拦截 **`Executor.query`**。
- `PageHelper.startPage()` 把分页参数**塞进 `ThreadLocal`**。
- 拦截时**读取 `ThreadLocal`，在 SQL 后拼 `LIMIT`**，然后 `finally` 清掉。
- **所以坑是：`startPage()` 后面必须紧跟查询**，否则参数被别的查询吃掉。

**Q3 源码（30%）**

追问：插件是怎么织进去的？

- 配置的插件会被 **`Plugin.wrap(target, interceptor)` 动态代理包装**。
- 调用时先进 **`InterceptorChain.pluginAll()`**，链式执行 `intercept()`。
- 只有**方法签名匹配 `@Intercepts` 声明**的才生效，否则直接放行。
- 典型用途：**分页、多租户隔离、SQL 改写、读写分离**。

**项目钩子**

- “多租户场景用插件统一在 SQL 里拼 `tenant_id`，业务代码零侵入。”

---

## 卡 6　MyBatis-Plus 核心能力　★★★★

**关键词**：`BaseMapper` / `LambdaQueryWrapper` / 分页插件 / 逻辑删除

**串线**：主线第 ⑥ 环「单表 CRUD 怎么少写」

**Q1 定义（20%）**

- MP 是 MyBatis 的**增强工具**，**只做增强不做改变**，原生写法仍可用。
- 核心是 **`BaseMapper<T>`**：内置 insert /deleteById/updateById /selectById 等。
- 条件构造器 **`LambdaQueryWrapper`** 用方法引用，**类型安全不用写字段名**。

**Q2 场景（50%）**

面试官问：MP 最常用的几个能力？

- **分页**：`MybatisPlusInterceptor` + `PaginationInnerInterceptor`。
- **逻辑删除**：`@TableLogic`，**查删自动加条件，不做物理删除**。
- **自动填充**：`@TableField(fill=INSERT)` + `MetaObjectHandler` 填创建时间。
- **乐观锁**：`@Version` + `OptimisticLockerInnerInterceptor`。

**Q3 源码（30%）**

追问：MP 有哪些坑？

- **分页插件不注册就不生效**——**这是最高频的坑**，很多人以为引了依赖就能分页。
- **`updateById` 只更新非 null 字段**，**想全量更新要用 `update` + wrapper**。
- **`saveBatch` 默认走循环单条 insert**，要开 **`rewriteBatchedStatements=true`** 才真批量。
- **多表关联 MP 不支持**，还得回手写 SQL + `resultMap`。

**项目钩子**

- “批量导入从 `saveBatch` 换成手写批量 insert，耗时从 40s 降到 3s。”