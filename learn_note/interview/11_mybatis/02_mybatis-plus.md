## mybatis-plus原理和实现机制?
### 一、整体定位
MyBatis-Plus（简称MP）是MyBatis的增强工具，只做增强不做修改，完全兼容原生MyBatis。
原生MyBatis需要手写大量CRUD XML/注解SQL；MP封装通用增删改查，无需写SQL，同时保留MyBatis所有能力（自定义 XML、分页、多表、动态 SQL）。
核心口号：无侵入、损耗小、功能强。
### 二、核心底层整体执行链路
Spring启动时自动扫描 Mapper接口；
MP基于MyBatis的MapperScannerConfigurer扩展，自动给所有Mapper接口生成代理对象；
内置BaseMapper封装通用方法（selectById、list、page等），启动时动态拼接SQL语句；
执行方法时，MP把实体类反射解析成表名、字段名，自动拼装原生MyBatis可识别SQL；
交由MyBatis原生执行器去数据库执行。
### 三、核心模块拆解原理
1. Mapper层：BaseMapper、Mapper继承体系
（1）基础父接口
```
public interface BaseMapper<T> {
    // 插入
    int insert(T entity);
    // 根据主键删除
    int deleteById(Serializable id);
    // 主键查询
    T selectById(Serializable id);
    // 条件查询、分页、批量、更新等...
}
```
所有自己写的Mapper继承BaseMapper<T>，泛型T对应数据库实体。不需要写任何XML，这些方法自带实现。

（2）实现关键点：动态SQL生成
MP没有生成XML文件，而是利用MyBatis的MybatisMapperAnnotationBuilder、动态SQL提供者SqlSource。
执行BaseMapper任意方法时：反射读取实体T上注解：@TableName表名、@TableId主键、@TableField字段；
自动拼接SQL：insert：INSERT INTO 表(字段1,字段2) VALUES(#{属性1},#{属性2})
selectById：SELECT 所有字段 FROM 表 WHERE 主键=#{id}
封装成MyBatis的DynamicSqlSource，交给MyBatis解析执行。

（3）Mapper代理创建（Spring整合核心）
原生MyBatis：MapperScannerConfigurer扫描接口，生成JDK动态代理。
MP重写ClassPathMapperScanner，替换为自己的MybatisPlusScanner：
扫描所有继承BaseMapper的接口；自动绑定MP内置的通用SQL；依旧生成JDK代理，兼容MyBatis整个执行流程。
2. 实体映射：注解反射机制（字段与数据库绑定）
   MP依靠反射 + 注解完成实体 ↔ 数据库映射，不用手写字段映射：

注解|作用
--|--
@TableName("t_user")|指定当前实体对应哪张表；不写默认类名小写
@TableId(type = IdType.AUTO)|标记主键；支持自增、雪花 ID、UUID、ASSIGN_ID
@TableField("user_name")|属性和数据库字段不一致映射；exist=false 代表非数据库字段
@Version|乐观锁版本号标识

   执行SQL时，MP通过反射拿实体字段 + 注解，自动驼峰转下划线（默认开启驼峰转下划线）。
3. CRUD SQL动态构建核心：AbstractSqlProvider
   MP内置大量Provider类（InsertSqlProvider、SelectSqlProvider、DeleteSqlProvider）。
   MyBatis支持@SelectProvider、@InsertProvider注解，MP把BaseMapper所有方法绑定到这些Provider。
   调用BaseMapper方法 → 进入对应Provider → 反射实体拼装SQL字符串 → MyBatis执行SQL。
   举个简化逻辑：
   执行 mapper.selectById (1)
   进入 SelectSqlProvider.getSelectByIdSql ();
   反射拿到实体表名、主键字段；
   拼接：SELECT id,name,age FROM user WHERE id=#{ew.param1}；
   MyBatis 解析#{参数}，JDBC执行。
4. Wrapper条件构造器（QueryWrapper/LambdaQueryWrapper）原理
   日常高频：QueryWrapper用来拼接where、order by、like、in等条件。
   流程：链式调用.eq("name","张三").like("phone","138")，Wrapper内部维护一个 SQL拼接字符串；
   把Wrapper对象作为参数传入BaseMapper方法；
   Provider拿到Wrapper，取出拼接好的条件语句拼到SQL尾部；预编译参数自动绑定，防止SQL注入。
   LambdaQueryWrapper优势：普通QueryWrapper写字段名字符串容易写错；Lambda通过实体方法引用user -> user.getName()，
   MP利用反射获取方法对应的字段名，编译期校验，杜绝字段名拼写错误。底层使用ASM反射解析Lambda表达式拿到属性名，再映射数据库字段。
5. 分页插件PageInterceptor（MP分页核心）
   MyBatis原生无分页，需要手动limit。MP通过MyBatis拦截器插件实现分页，无侵入。
   完整机制：MP提供MybatisPlusInterceptor分页拦截器，配置分页插件；
   MyBatis执行SQL前，拦截器拦截Executor查询；判断入参有Page对象：先自动执行count统计SQL（SELECT COUNT (*) ...）拿到总条数；
   改造原有SQL拼接limit offset，执行分页数据查询；把总条数、当前页、数据封装进Page返回。
   支持多数据库适配：MySQL LIMIT、Oracle ROWNUM、SQLServer TOP，MP根据数据库类型自动适配分页语法。
6. 主键策略IdType实现 
   @TableId的IdType由MP内置主键生成器IdentifierGenerator实现：
   AUTO：数据库自增，MP不生成，依赖数据库；
   ASSIGN_ID（默认雪花算法）：MP内置雪花算法，通过DefaultIdentifierGenerator生成long 分布式ID，支持多机器、多服务全局唯一；
   UUID：生成32位字符串；主键在insert 执行前，MP拦截填充主键值。
7. 公共字段自动填充（@TableField (fill = xxx)）原理
   创建MetaObjectHandler实现类，重写insertFill、updateFill。
   执行插入/更新时，MyBatis的MetaObject元数据被MP拦截，自动给createTime、updateTime、createBy赋值，不用手动set。
   典型场景：审计字段自动填充。
8. 乐观锁@Version实现原理
   依旧依托MyBatis拦截器：更新前拦截拿到实体version旧值；SQL自动拼接SET version=version+1 WHERE version=旧版本号；
   更新行数=0代表版本已被修改，更新失败，实现乐观锁。
### 四、MP整体执行全链路（从头到尾梳理）
   SpringBoot启动，MP自动配置类MybatisPlusAutoConfiguration生效；
   MapperScanner扫描所有Mapper接口，生成JDK动态代理； 所有BaseMapper方法绑定SqlProvider动态SQL生成器；
   调用mapper.selectList (queryWrapper)： Wrapper拼装查询条件；Provider反射实体生成完整SQL；
   如有分页，分页拦截器改造 SQL、执行count； MyBatis走JDBC执行；结果集反射封装回实体；自定义XML正常执行，MP完全不影响原生MyBatis逻辑。
   五、MP和原生MyBatis 的边界
   MP只管通用CRUD；多表联查、复杂聚合、存储过程依旧手写XML；
   MP所有能力建立在MyBatis之上，底层还是MyBatis的SqlSession、Executor、MappedStatement；
   无侵入：随时可以去掉MP依赖，改回纯MyBatis，业务无改动。
### 六、高频面试问答
   MP会生成XML文件吗？
   不会，运行时通过反射 + Provider 动态拼接 SQL，没有物理 XML。
   BaseMapper的方法SQL什么时候生成？
   不是启动一次性全部生成，调用时实时拼接。
   LambdaQueryWrapper怎么拿到字段名？
   使用ASM解析Lambda函数式接口，获取getter方法，反射对应实体属性，再映射数据库字段，避免硬编码字符串出错。
   MP分页原理？
   MyBatis插件拦截，拦截查询SQL，自动生成count语句，并且给查询SQL拼接对应数据库的limit分页语法。
### 七、极简背诵版（面试口述）
   MyBatis-Plus是 MyBatis增强框架，不改原生MyBatis源码。启动时扫描Mapper，为 BaseMapper提供动态SQL能力；
   运行时反射实体类的注解，自动拼接表、字段SQL；配合Wrapper实现条件组装；依靠MyBatis拦截器实现分页、乐观锁、字段自动填充；
   复杂SQL依旧支持手写XML，兼顾开发效率与灵活性。底层依然依赖MyBatis的代理、SqlSession执行。
------------------------------------------------------------------------------

