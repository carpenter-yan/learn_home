## 什么是 MyBatis?
Mybatis是一个半ORM（对象关系映射）框架，它内部封装了JDBC，开发时只需要关注SQL语句本身，不需要花费精力去处理加载驱动、创建连接、创建statement等繁杂的过程。
程序员直接编写原生态sql，可以严格控制sql执行性能，灵活度高。
MyBatis可以使用XML或注解来配置和映射原生信息，将POJO映射成数据库中的记录，避免了几乎所有的JDBC代码和手动设置参数以及获取结果集。
通过xml文件或注解的方式将要执行的各种statement配置起来，并通过java对象和statement中sql的动态参数进行映射生成最终执行的sql语句，
最后由 mybatis框架执行sql并将结果映射为java对象并返回。（从执行sql到返回result的过程）。
------------------------------------------------------------------------------
## ORM 是什么?
ORM（Object Relational Mapping），对象关系映射。
是一种为了解决关系型数据库数据与简单Java对象（POJO）的映射关系的技术。
简单来说，ORM是通过使用描述对象和数据库之间映射的元数据，将程序中的对象自动持久化到关系型数据库中。
------------------------------------------------------------------------------
## 为什么说Mybatis是半自动ORM映射工具？它与全自动的区别在哪里？
Hibernate属于全自动ORM映射工具，使用Hibernate查询关联对象或者关联集合对象时，可以根据对象关系模型直接获取，所以它是全自动的。
而Mybatis在查询关联对象或关联集合对象时，需要手动编写SQL来完成，所以，被称之为半自动ORM映射工具。
------------------------------------------------------------------------------
## MyBatis 的使用过程？
1. 创建SqlSessionFactory。可以从配置或者直接编码来创建SqlSessionFactory
```
StrinString resource = "org/mybatis/example/mybatis-config.xml";
InputStream inputStream = Resources.getResourceAsStream(resource);
SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
```
2. 通过SqlSessionFactory创建SqlSession，SqlSession（会话）可以理解为程序和数据库之间的桥梁
```SqlSession session = sqlSessionFactory.openSession();```
3. 通过sqlsession执行数据库操作 可以通过SqlSession实例来直接执行已映射的SQL语句：
```Blog blog = (Blog)session.selectOne("org.mybatis.example.BlogMapper.selectBlog", 101);```
更常用的方式是先获取Mapper(映射)，然后再执行SQL语句：
```
BlogMapper mapper = session.getMapper(BlogMapper.class);
Blog blog = mapper.selectBlog(101);
```
4. 调用session.commit()提交事务。如果是更新、删除语句，我们还需要提交一下事务。
5. 调用session.close()关闭会话
------------------------------------------------------------------------------
## MyBatis生命周期？
上面提到了几个MyBatis的组件，一般说的MyBatis生命周期就是这些组件的生命周期。
1. SqlSessionFactoryBuilder
一旦创建了SqlSessionFactory，就不再需要它了。因此SqlSessionFactoryBuilder实例的生命周期只存在于方法的内部。
2. SqlSessionFactory
SqlSessionFactory是用来创建SqlSession的，相当于一个数据库连接池，每次创建SqlSessionFactory都会使用数据库资源，多次创建和销毁是对资源的浪费。
    所以SqlSessionFactory是应用级的生命周期，而且应该是单例的。
3. SqlSession
SqlSession相当于JDBC中的Connection，SqlSession的实例不是线程安全的，因此是不能被共享的，所以它的最佳的生命周期是一次请求或一个方法。
4. Mapper
映射器是一些绑定SQL语句的接口。映射器接口的实例是从SqlSession中获得的，它的生命周期在sqlsession事务方法之内，一般会控制在方法级。
**MyBatis通常也是和Spring集成使用，Spring可以帮助我们创建线程安全的、基于事务的SqlSession和映射器，并将它们直接注入到我们的bean中，我们不需要关心它们的创建过程和生命周期**
------------------------------------------------------------------------------
## 是否支持延迟加载？
1. Mybatis支持association关联对象和collection关联集合对象的延迟加载，association指的就是一对一，collection指的就是一对多查询。
   在Mybatis配置文件中，可以配置是否启用延迟加载lazyLoadingEnabled=true|false。
2. 它的原理是，使用CGLIB创建目标对象的代理对象，当调用目标方法时，进入拦截器方法，
   比如调用a.getB().getName()，拦截器invoke()方法发现a.getB()是null值，那么就会单独发送事先保存好的查询关联B对象的sql，把B查询上来，
   然后调用a.setB(b)，于是a的对象b属性就有值了，接着完成a.getB().getName()方法的调用。这就是延迟加载的基本原理。
3. 当然了，不光是Mybatis，几乎所有的包括Hibernate，支持延迟加载的原理都是一样的。
------------------------------------------------------------------------------
## 如何获取自动生成的主键值?
- 新增标签中添加：keyProperty="ID"即可
```
<insert id="insert" useGeneratedKeys="true" keyProperty="userId" >
    insert into user(
    user_name, user_password, create_time)
    values(#{userName}, #{userPassword} , #{createTime, jdbcType= TIMESTAMP})
</insert>
```
这时候就可以完成回填主键
```
    mapper.insert(user);
    user.getId;
```
------------------------------------------------------------------------------
## 支持动态SQL吗？
Mybatis 动态sql可以在Xml映射文件内，以标签的形式编写动态sql，执行原理是根据表达式的值完成逻辑判断并动态拼接sql的功能。
Mybatis 提供了9种动态sql标签：trim、 where、 set、foreach、if、 choose、 when、 otherwise、 bind
1. if，根据条件来组成where子句
```
<select id="findActiveBlogWithTitleLike" resultType="Blog">
    SELECT * FROM BLOG WHERE state = ‘ACTIVE’
    <if test="title != null">
        AND title like #{title}
    </if>
</select>
```
2. foreach，用来循环的，可以对集合进行遍历
```
<select id="selectPostIn" resultType="domain.blog.Post">
  SELECT * FROM POST P
  <where>
    <foreach item="item" index="index" collection="list"
        open="ID in (" separator="," close=")" nullable="true">
          #{item}
    </foreach>
  </where>
</select>
```
------------------------------------------------------------------------------
## 不同xml映射文件id是否可以重复？
不同的xml映射文件，如果配置了namespace，那么id可以重复；如果没有配置namespace，那么id不能重复。
原因namespace+id作为Map<String，MapperStatement>的key使用，如果没有namespace，就剩下id，那么id重复会导致数据互相覆盖。
有了namespace，自然id就可以重复，namespace不同，namespace+id自然也不同。
------------------------------------------------------------------------------
## Mybatis 的一级、二级缓存？
MyBatis缓存用于减少重复SQL查询，降低DB压力，分为一级缓存（默认开启，SqlSession 级别）、二级缓存（默认关闭，Mapper 命名空间级别）。
查询优先级：二级缓存 → 一级缓存 → 数据库。
1. 一级缓存: 基于PerpetualCache的HashMap本地缓存，其存储作用域为Session，各个SqlSession之间的缓存相互隔离。
    当Session flush或close之后，该Session中的所有Cache就将清空，默认打开一级缓存。
2. 二级缓存与一级缓存其机制相同，默认也是采用PerpetualCache，HashMap存储，不同之处在于其存储作用域 Mapper(Namespace)，可以在多个SqlSession之间共享，
   并且可自定义存储源，如Ehcache。默认不打开二级缓存，要开启二级缓存，使用二级缓存属性类需要实现Serializable序列化接口(可用来保存对象的状态)，可在它的映射文件中配置。
3. 对于缓存数据更新机制，当某一个作用域(一级缓存Session/二级缓存Namespaces)的进行了C/U/D操作后，默认该作用域下所有select中的缓存将被clear。
------------------------------------------------------------------------------
## MyBatis的工作原理？
1. 读取MyBatis配置文件——mybatis-config.xml、加载映射文件——映射文件即SQL映射文件，文件中配置了操作数据库的SQL语句。最后生成一个配置对象。
2. 构造会话工厂：通过MyBatis的环境等配置信息构建会话工厂SqlSessionFactory。
3. 创建会话对象：由会话工厂创建SqlSession对象，该对象中包含了执行SQL语句的所有方法。
4. Executor执行器：MyBatis底层定义了一个Executor接口来操作数据库，它将根据SqlSession传递的参数动态地生成需要执行的SQL语句，同时负责查询缓存的维护。
5. StatementHandler：数据库会话器，串联起参数映射的处理和运行结果映射的处理。
6. 参数处理：对输入参数的类型进行处理，并预编译。
7. 结果处理：对返回结果的类型进行处理，根据对象映射规则，返回相应的对象。
------------------------------------------------------------------------------
## Mybatis 都有哪些 Executor 执行器？
Mybatis有三种基本的Executor执行器，SimpleExecutor、ReuseExecutor、BatchExecutor。
- SimpleExecutor：每执行一次update或select，就开启一个Statement对象，用完立刻关闭Statement对象。
- ReuseExecutor：执行update或select，以sql作为key查找Statement对象，存在就使用，不存在就创建，
    用完后，不关闭Statement对象，而是放置于Map<String, Statement>内，供下一次使用。简言之，就是重复使用Statement对象。
- BatchExecutor：执行update（没有select，JDBC批处理不支持select），将所有sql都添加到批处理中（addBatch()），等待统一执行（executeBatch()），
    它缓存了多个Statement对象，每个Statement对象都是addBatch()完毕后，等待逐一执行executeBatch()批处理。与JDBC批处理相同。
------------------------------------------------------------------------------
## MyBatis如何进行分页？分页插件的原理是什么？
### 一、MyBatis分页两种原生写法
1. 逻辑分页（内存分页，严禁大数据使用）
   实现
   先查出全表所有数据到Java内存，通过List.subList(start, end) 截取分页。
```
   List<User> allList = mapper.selectAll();
   // 第2页，每页10条：pageNum=2,pageSize=10
   List<User> pageData = allList.subList(10, 20);
```
   致命缺点 百万数据全部加载进内存，OOM内存溢出；数据库压力极大；只适合几百条以内小表，线上业务禁止。
2. 物理分页（推荐，数据库分页）
   利用数据库原生分页语法，只查当前页数据。不同数据库语法不同：MySQL：limit offset, size
```
   <select id="listUser">
       select id,name from user limit #{offset},#{pageSize}
   </select>
```
   入参：offset=(pageNum-1)*pageSize Oracle：rownum、分页嵌套 SQL Server：top/offset fetch
   缺点：1. 每种数据库分页语法不一样，多数据库适配需要写多套SQL；
        2. 每个Mapper都要手动计算offset，重复代码多；
        3. 深度分页limit 100000,10 效率极低，需要游标分页优化。
### 二、主流分页插件：PageHelper（最通用）
   市面上几乎统一使用 PageHelper，不用手写 limit。
   基础使用Maven引入依赖；调用静态方法设置分页参数，紧跟查询Mapper：
```
   // pageNum页码，pageSize每页条数
   PageHelper.startPage(2, 10);
   // 紧跟的第一条select自动分页
   List<User> list = userMapper.selectAll();
   // 封装分页结果
   PageInfo<User> pageInfo = new PageInfo<>(list);
```
### 三、PageHelper 底层完整原理（面试核心）
   核心：MyBatis拦截器Interceptor动态修改SQL
   MyBatis提供四大拦截点：Executor、StatementHandler、ParameterHandler、ResultSetHandler。
   PageHelper拦截Executor的query方法。整体流程分6步：
   1. ThreadLocal 保存分页参数
   PageHelper.startPage(pageNum,pageSize) 把页码、页大小、是否统计总数存入当前线程的 ThreadLocal。线程隔离，多线程互不干扰。
   2. 执行 Mapper 查询，进入 MyBatis Executor.query 拦截器
   PageHelper 拦截器被触发，从当前线程 ThreadLocal 取出分页配置。
   3. 获取原始执行 SQL
   拿到 Mapper 写的原生 SQL：select id,name from user。
   4. 动态拼接分页 SQL
   根据当前数据库类型（MySQL/Oracle）拼接对应分页语句： 
   原生 SQL → 改造为 select id,name from user limit 10,10。
   5. 额外生成 count 统计 SQL（总数）
   自动把原 SQL 改为 select count(*) from (原SQL) tmp，执行一次查询总条数。
   6. 清理 ThreadLocal，防止参数污染
   查询结束清空 ThreadLocal。避免本次分页参数串到下一次无关查询。
   7. 结果封装
   数据库返回当前页数据 + 总条数，封装为 Page 对象；最终 PageInfo 包装分页全部信息（总页数、当前页、是否上一页下一页等）。
#### 关键规则：只拦截紧跟其后的第一条SQL
   必须保证 startPage 和 Mapper 查询紧挨着，中间不能有任何其他查询。
   如果中间多查了别的表，别的 SQL会被错误分页。
### 五、深度分页问题（订单高频踩坑）
   MySQL limit 100000,10：数据库需要扫描前 10 万行再丢弃，很慢。
   PageHelper本身解决不了，优化方案：
   游标分页：where id > 上一页最大id limit 10，利用主键索引；
   大分页、报表走ES； 禁止前端传超大页码。
### 六、常见坑
   startPage 之后有多条查询：只有第一条分页，后面全部不分页；
   多线程异步：ThreadLocal 失效，分页错乱；异步代码不能用 PageHelper，手动分页；
   带 union、复杂子查询的 SQL，自动 count 语句可能报错，可以手动写 countSQL；
   分页参数没清干净，线程池复用导致随机分页。
### 七、MyBatis 分页整体选型总结
   绝对不用内存分页；
   简单项目手动写limit物理分页；多表、多数据库用PageHelper；
   分布式、大数据量、深度分页不用数据库分页，用ES；
   异步线程不能使用 PageHelper。
### 八、极简背诵版
   分页分内存分页（全查内存截取，禁止大数据）、物理分页（数据库limit）。
   PageHelper依靠MyBatis拦截器拦截Executor，ThreadLocal存分页参数。
   拦截后动态改造SQL拼接分页语法，额外执行count查总数，最后清空ThreadLocal。
   只对紧挨着的第一条SQL生效；适配多数据库；异步线程不可用。
   MySQL深度分页插件无法优化，需要主键游标方案。