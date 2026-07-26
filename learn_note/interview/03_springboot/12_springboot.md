## 什么是 Spring Boot？
### 一、定位
SpringBoot不是新框架，基于Spring、SpringMVC的快速开发脚手架。
核心思想：约定优于配置，消灭繁杂XML、大量手动配置，内置服务器，做到开箱即用。
解决传统SSM痛点：XML配置繁多、版本依赖冲突、Tomcat需要额外部署、环境配置麻烦。

### 二、两大核心机制
1. 自动配置AutoConfiguration依托@EnableAutoConfiguration。
   Maven引入依赖后，SpringBoot自动推断场景，自动创建对应Bean，无需手动写配置。
   举例：
   引入mybatis依赖 → 自动装配SqlSessionFactory、Mapper扫描；
   引入spring-web → 自动装配DispatcherServlet、Tomcat；
   引入Redis → 自动创建RedisTemplate。
   底层：META-INF/spring.factories配置批量自动配置类，按需加载。
2. starter场景启动器
   把一组相关依赖打包成starter，不用手动管理一堆jar包和版本号，杜绝版本错乱。
   格式：spring-boot-starter-xxx
   常用：
   spring-boot-starter-web：web开发（SpringMVC+Tomcat）
   spring-boot-starter-test：单元测试
   spring-boot-starter-data-redis
   mybatis-spring-boot-starter
   好处：只需要引入一个starter，配套所有依赖全部自动导入，版本由Boot统一管控。
### 三、标志性注解 @SpringBootApplication
   三合一注解：
```
   @SpringBootConfiguration //本质@Configuration，标记配置类
   @EnableAutoConfiguration //开启自动配置
   @ComponentScan //默认扫描当前启动类所在包及所有子包
```
   一个注解包揽配置、自动装配、包扫描。
### 四、核心优势
   - 无XML 绝大多数场景零XML，配置写在application.yml/application.properties。
   - 内置Web容器 内置Tomcat（默认）、Jetty、Undertow，项目打成jar包，java-jar直接运行，不用单独装Tomcat、部署war包。
   - 极简配置 通用配置统一写配置文件：端口、数据库、连接池、日志。支持多环境配置（dev/test/prod）。
   - 适配Spring全家桶 无缝对接SpringCloud、SpringSecurity、SpringData、Seata等。
   - 自带监控运维 actuator组件，查看服务健康、线程、日志、接口、配置，运维方便。
   - 易集成中间件 Redis、MQ、ES、MyBatis 全部一键适配。
### 五、配置体系
   全局配置文件：application.yml/application.properties；
   多环境：application-dev.yml、application-prod.yml，激活指定环境；
   自定义配置：@ConfigurationProperties 批量绑定配置到实体类；@Value单个取值。
### 六、和Spring/SpringMVC/SpringCloud关系
   Spring：底层核心，IOC、AOP、事务；
   SpringMVC：Spring的Web层，负责接口收发；
   SpringBoot：简化Spring+MVC配置，快速单体开发；
   SpringCloud：一套微服务组件全家桶，基于SpringBoot开发。
   层级：Spring → SpringBoot → SpringCloud。
### 七、常见特性
   嵌入式容器，推荐jar打包运行；
   条件注解驱动自动配置：@ConditionalOnClass、@ConditionalOnMissingBean；有类才装配、没有开发者自定义Bean才装配；
   可插拔：不需要某个功能排除对应starter即可；
   统一异常处理、全局跨域、日志默认配置完成。
### 八、极简背诵版
   SpringBoot = Spring简化工具，约定优于配置；
   starter整合依赖解决版本冲突，自动配置免去手动注册Bean；
   一个启动类@SpringBootApplication，内置Tomcat，jar包一键启动；
   配置放yml，支持多环境；
   SpringCloud基于Boot；Boot简化单体，Cloud治理微服务。
------------------------------------------------------------------------------
## JavaConfig详解
### 一、定义
JavaConfig是Spring的纯Java代码配置方案，用来替代传统XML文件配置。
不用写XML，全部通过Java类 + 注解完成Bean注册、依赖组装、组件配置，是SpringBoot主流配置方式。

核心注解：@Configuration。
早年Spring大量使用xml配置Bean；现在SSM、SpringBoot统一改用JavaConfig。
### 二、核心注解
1. @Configuration
   标记当前类为配置类，等价于xml文件。Spring启动会解析这个类，扫描里面所有@Bean方法。
```
   @Configuration
       public class MyConfig {
   }
```
   底层：配置类会被CGLIB动态代理，保证@Bean方法多次调用返回同一个单例Bean。
2. @Bean
   写在配置类的方法上，手动将方法返回对象注册为Spring Bean。
   适合注册第三方类（RedisTemplate、线程池、MyBatis、Feign），这类组件没法加@Component注解。
```
   @Configuration
   public class MyConfig {
       // 将RestTemplate交给IOC
       @Bean
       public RestTemplate restTemplate(){
           return new RestTemplate();
       }
   }
```
   Bean名称：默认方法名；可以手动指定@Bean("customName")。 方法入参直接走DI自动注入依赖。
3. @ComponentScan
   配置包扫描，替代xml的<context:component-scan>，扫描@Service/@Controller/@Component。
   SpringBoot启动类自带该注解，普通JavaConfig需要手动添加：
```
   @Configuration
   @ComponentScan("com.xxx.service,com.xxx.controller")
   public class MyConfig {
   }
```
4. @Import
   导入其他配置类，拆分多个Config，避免配置类臃肿。
```
   @Configuration
   @Import(RedisConfig.class, MyBatisConfig.class)
   public class MainConfig {
   }
```
5. @PropertySource
   加载外部properties配置文件，配合@Value读取配置。
### 三、JavaConfig两种Bean注册方式区分
   1. 注解扫描方式
   业务自定义类加@Service/@Component，配合@ComponentScan自动扫描。适合自己写的业务代码。
   2. @Bean方式
   配置类中手动new对象注册。适合第三方开源组件、工具类、复杂创建逻辑。
### 四、@Configuration全注解模式vs@Component
   很多人混淆：配置类不能用普通@Component替代@Configuration。
   @Configuration：CGLIB代理。同一个配置类内A方法调用B方法的@Bean，依然拿到容器单例。
```
   @Configuration
   public class Config {
       @Bean
       public User user(){
           return new User();
       }
       @Bean
       public Order order(){
           // 多次调用user()，拿到的是同一个单例
           return new Order(user());
       }
   }
```
如果把@Configuration改成普通@Component，配置类不会被代理，每次user()都是new新对象，无法保证单例。
SpringBoot提供轻量化注解：@Configuration(proxyBeanMethods = false)，关闭代理，提升启动速度，适合配置内无方法互相调用的场景。
### 五、JavaConfig 和 SpringBoot 的关系
   SpringBoot启动注解@SpringBootApplication内置@SpringBootConfiguration，本质就是@Configuration；启动类本身就是主配置类。
   Boot的自动配置底层全是大量JavaConfig + @Conditional条件注解。
   日常自定义线程池、Redis、MyBatis、OSS 客户端，全部新建@Configuration配置类写@Bean。
### 六、JavaConfig 优势对比 XML
   类型安全：Java 编译期报错，XML 写错字段只有运行报错；
   支持代码逻辑：创建Bean可以写if判断、循环，XML很难做到；
   不用记繁杂xml标签，语法统一Java；
   更容易重构，IDE跳转、提示完善。
### 七、极简背诵版
   JavaConfig = Java代码代替XML做Spring配置，核心注解@Configuration；
   @Bean手动注册第三方Bean；@ComponentScan扫描业务注解；
   @Configuration带CGLIB代理，保证配置内方法调用拿到单例；普通@Component 无此特性；
   SpringBoot所有自定义配置均使用JavaConfig；启动类本身就是配置类；
   自研业务用包扫描，第三方组件用@Bean。
------------------------------------------------------------------------------
## Spring Boot Starter自动配置原理？
### 一、整体一句话概括
Starter = 依赖包 + 配套自动配置类。
引入starter不用手动写大量 XML/JavaConfig 创建组件；SpringBoot 依靠 SPI 机制 + 条件注解，根据当前环境自动判断是否创建 Bean，实现开箱即用。
核心链路：引入 starter 依赖 → 读取 META-INF 配置加载自动配置类 → 条件注解按需装配 Bean。
### 二、两大核心组成：starter 包 + 自动配置模块
标准拆分（官方规范）
xxx-spring-boot-starter（启动器）
只做依赖管理，整合所有需要的 jar，统一锁定版本，解决版本冲突。无任何 Java 代码。
例：mybatis-spring-boot-starter 整合 mybatis、mybatis-spring、JDBC 等全套依赖。
xxx-spring-boot-autoconfigure（自动配置模块）
存放所有 Java 配置类（大量@Configuration）、条件注解、配置属性绑定代码，是自动配置的逻辑本体。
日常开发只需要引入 starter，间接依赖到 autoconfigure。
### 三、底层加载入口：@EnableAutoConfiguration
@SpringBootApplication 包含 @EnableAutoConfiguration，这是开启自动配置总开关。
@EnableAutoConfiguration 借助 AutoConfigurationImportSelector 完成加载：
项目启动时，借助 SPI 读取所有 jar 包内路径：
META-INF/spring.factories
文件中 key 为org.springframework.boot.autoconfigure.EnableAutoConfiguration，value 是一批全路径自动配置类；
Spring 把所有配置类加载到内存，再用条件注解过滤，满足条件才生效。
spring.factories 示例：
```
properties
org.springframework.boot.autoconfigure.EnableAutoConfiguration=\
org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration,\
org.springframework.boot.autoconfigure.redis.RedisAutoConfiguration
```
### 四、核心：条件注解（决定配不配）
所有自动配置类都带大量@Conditional系列注解，满足环境条件才创建 Bean，不会无脑全部装配。高频注解：
@ConditionalOnClass：classpath 下存在指定类，配置才生效
示例：存在RedisTemplate.class才开启 Redis 自动配置
@ConditionalOnMissingBean：IOC 容器没有开发者自己定义的 Bean，框架才自动创建。
个性化定制原理：你手动 @Bean 自定义 RedisTemplate，框架自动放弃装配，以你的为准。
@ConditionalOnProperty：配置文件存在指定配置项，开关控制是否启用
@ConditionalOnWebApplication：仅 Web 环境生效
### 五、配置绑定：application.yml 绑定属性
自动配置类搭配 @ConfigurationProperties，把 yml/properties 中的配置批量注入属性类。
举例 Redis：
```
@ConfigurationProperties(prefix = "spring.redis")
public class RedisProperties {
    private String host;
    private int port;
}
用户写配置：
yaml
spring:
    redis:
        host: 127.0.0.1
        port: 6379
```
框架自动读取赋值，不需要手动读取配置文件。
### 六、完整执行全链路（以 Redis 举例）
pom 引入spring-boot-starter-data-redis；starter 引入 autoconfigure 依赖；
启动类@SpringBootApplication开启自动配置；
SPI 扫描 spring.factories，拿到 RedisAutoConfiguration；
校验@ConditionalOnClass(RedisTemplate.class)，Jar 包存在该类，放行；
读取 spring.redis 配置绑定到 RedisProperties；
判断@ConditionalOnMissingBean(RedisTemplate.class)：
你没自定义 RedisTemplate → Spring 自动创建放入容器；
你写了 @Bean 自定义 → 框架不创建，使用你的 Bean；
RedisTemplate 就绪，业务直接 @Autowired 注入使用。
### 七、如何关闭指定自动配置
启动类注解排除不需要的自动配置类：
```@SpringBootApplication(exclude = {RedisAutoConfiguration.class})```
### 八、自定义 Starter 极简步骤（拓展）
分两个模块：starter 依赖包 + autoconfigure 配置模块；
autoconfigure 写 @Configuration 配置类 + 条件注解 +@ConfigurationProperties；
autoconfigure 资源目录新建 META-INF/spring.factories，写入自动配置类全路径；
starter 引入 autoconfigure 和所有业务依赖；
其他项目引入 starter 即可自动装配。
### 九、高频总结坑点
自动配置不是一定生效，条件注解是闸门；
开发者自定义 Bean 优先级高于框架自动配置（ConditionalOnMissingBean）；
配置写错前缀会绑定失败，yml 属性和 Properties 字段严格对应；
spring.factories 是旧 SPI 方案，Boot2.7 之后推荐使用 META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports 新文件，逻辑一致。
### 十、极简背诵版
Starter 分为 starter（管理依赖）+autoconfigure（配置代码）；
@EnableAutoConfiguration 通过 SPI 读取 spring.factories 加载全部自动配置类；
依靠 @Conditional 系列条件注解，按需创建 Bean；用户自定义 Bean 优先；
@ConfigurationProperties 绑定 yml 配置；
整体逻辑：有依赖则加载、有条件才创建、用户配置优先级最高。
------------------------------------------------------------------------------
## Spring-boot-starter-parent 有什么⽤？
### 一、核心定位
它是SpringBoot的统一版本管理父工程，是所有Boot项目推荐继承的Maven父 pom，不提供任何业务代码，只做依赖管控、默认插件、全局配置统一。
```
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>2.7.15</version>
    <relativePath/>
</parent>
```
### 二、四大核心功能
1. 锁定全套依赖版本，告别版本冲突（最重要）
   parent内部维护一张极其完整的依赖版本清单，把Spring全家桶、所有starter、第三方常用库（MySQL、Redis、MyBatis、Jackson、连接池等）的版本全部定义好。
   子项目引入各类 starter时不需要写version 版本号；
```
    <!-- 不用写version，版本由父工程统一管控 -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <dependency>
        <groupId>mysql</groupId>
        <artifactId>mysql-connector-java</artifactId>
    </dependency>
```
所有组件版本互相适配，不会出现 Spring 版本和 MyBatis 版本不兼容、jar包冲突、缺包报错；
想统一升级整个项目SpringBoot版本，只需要改parent的version，全项目所有依赖批量升级。
2. 统一管理 Maven 构建插件版本
   内置绑定编译、打包、测试、资源过滤等全套 Maven 插件的版本：
   maven-compiler-plugin：JDK 编译版本默认适配
   maven-resources-plugin：配置文件过滤
   maven-test-plugin：单元测试
   spring-boot-maven-plugin：SpringBoot 打包插件（打成可执行 jar）
   子工程无需配置插件版本，避免插件版本错乱、打包异常。
3.  提供默认全局 Maven 配置
    - 编码统一：默认 UTF-8，不用子项目重复配置编码；
    - JDK 默认版本对齐；
    -  资源文件过滤：默认开启application.yml/application.properties配置文件的占位符@xxx@替换；
    - 统一仓库配置、打包规则。
4. 内置属性，方便自定义版本覆盖
   parent 定义了大量 properties 属性，子项目可直接重写属性就能改依赖版本，不用重写整个dependency：
```
<!-- 子pom直接覆盖mysql版本，不用改父工程 -->
<properties>
    <mysql.version>8.0.33</mysql.version>
</properties>
```
适合局部微调某个中间件版本，整体依然由 Boot 统一管控。
### 三、不继承 parent 的替代方案（import 依赖管理）
很多多模块项目不方便继承父 pom，使用 Maven 的dependencyManagement导入，只接管版本，不继承打包插件：
```
<dependencyManagement>
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-dependencies</artifactId>
        <version>2.7.15</version>
        <type>pom</type>
        <scope>import</scope>
    </dependency>
</dependencies>
```
区别：
1. 继承 starter-parent：版本 + 插件 + 全部配置全拿；
2. import spring-boot-dependencies：只接管依赖版本，打包插件、编码等需要自己手动配置。
### 四、极简背诵版
   统一管控全部依赖版本，引入 starter 不用写 version，解决 jar 版本冲突；
   统一 Maven 插件、编码、打包规则，省去大量重复 pom 配置；
   修改父版本即可全项目升级 SpringBoot；支持属性单独修改某个组件版本；
   不想继承用 import 导入 spring-boot-dependencies，仅做版本管理，无插件配置。
------------------------------------------------------------------------------
## Spring Boot常用注解有哪些？
1. @SpringBootApplication ：是一个组合注解（组合注解可以自定义，包含所有引入注解功能）。定义在main方法入口类处，用于启动spring boot项目
2. @EnableAutoConfiguration ：让spring boot容器根据类路径中的jar包依赖当前项目进行自动配置，文件在src/main/resources的META-INF/spring.factories
3. @Value ：application.properties定义属性，直接使用@Value注入即可
4. @ConfigurationProperties(prefix="person") ：可以新建一个properties文件，ConfigurationProperties的属性prefix指定properties的配置的前缀，通过location指定properties文件的位置
5. @RestController ： 组合@Controller和@ResponseBody，当你开发一个和页面交互数据的控制时，比如bbs-web的api接口需要此注解
------------------------------------------------------------------------------
## Spring Boot配置加载顺序？
1. properties⽂件；
2. YAML⽂件；
3. 系统环境变量；
4. 命令⾏参数；
------------------------------------------------------------------------------
## Spring Boot 的运⾏⽅式？
1. 直接执⾏ main ⽅法运⾏
2. ⽤ Maven / Gradle 插件运⾏
3. 打包⽤命令或者放到容器中运⾏
------------------------------------------------------------------------------
## SpringBoot 有哪几种读取配置的方式？
SpringBoot可以通过 @PropertySource、@Value、@Environment、@ConfigurationProperties 来绑定变量。
------------------------------------------------------------------------------
## Spring Boot 打成的 jar 和普通的 jar 有什么区别 ?
Spring Boot的项目终止以jar包的形式进行打包，这种jar包可以通过可以通过命令（java -jar xxx.jar）来运行的，这种jar包不能被其他项目所依赖，即使被依赖了也不能直接使用其中的类。
普通的jar包，解压后直接就是包名，包里就是我们的代码，而 Spring Boot 打包成的可执行 jar 解压后，在 \BOOT-INF\classes 目录下才是我们的代码，因此无法被直接引用。
如果非要引用，可以在 pom.xml 文件中增加配置，将 Spring Boot 项目打包成两个 jar ，一个可执行，一个可引用。
```
<build>
    <plugins>
        <plugin>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-maven-plugin</artifactId>
            <executions>
                <execution>
                    <goals>
                        <!--可以把依赖的包都打包到生成的Jar包中 -->
                        <goal>repackage</goal>
                    </goals>
                    <!--可以生成不含依赖包的不可执行Jar包 -->
                    <configuration>
                        <classifier>exec</classifier>
                    </configuration>
                </execution>
            </executions>
        </plugin>
        <plugin>
            <artifactId>maven-compiler-plugin</artifactId>
            <configuration>
                <source>1.8</source>
                <target>1.8</target>
            </configuration>
        </plugin>
    </plugins>
</build>

```
一次打包生成两个jar包，其中XXX.jar 可作为其它工程的依赖，XXX-exec.jar可被执行
------------------------------------------------------------------------------
## 开启 Spring Boot特性有哪⼏种⽅式？
1. 继承 spring-boot-starter-parent 项⽬
2. 添加 spring-boot-dependencies jar包依赖
------------------------------------------------------------------------------
## Spring Boot 统一参数校验？
使用@Validated注解配合参数校验注解， 比如：@NotEmpty、 @NotNull对参数进行校验。
然后对抛出的异常ControllerAdvice进行捕获然后调整输出数据。
------------------------------------------------------------------------------
## Spring Boot 如何实现定时任务 ?
定时任务很常见，Spring Boot 对于定时任务的⽀持有两种⽅式：
1. 使用 Spring 中的 @Scheduled 注解
2. 使⽤第三⽅框架 Quartz
使⽤ Spring 中的 @Scheduled 的⽅式主要通过 @Scheduled 注解来实现。
使⽤ Quartz ，则按照 Quartz 的⽅式，定义 Job 和 Trigger 即可。
------------------------------------------------------------------------------
## Spring Boot如何实现异步调用？
SpringBoot中使用异步调用很简单，只需要在方法上添加@Async注解即可实现方法的异步调用。
注意：需要在启动类加上@EnableAsync使异步调用@Async注解生效。
------------------------------------------------------------------------------
## Spring Security 和 Shiro 各⾃的优缺点 ?
由于 Spring Boot 官⽅提供了⼤量的⾮常⽅便的开箱即⽤的 Starter ，包括 Spring Security 的 Starter，使得在 Spring Boot 中使⽤ Spring Security 变得更加容易，甚⾄只需要添加⼀个依赖就可以保护所有的接⼝。
如果是 Spring Boot 项⽬，⼀般选择 Spring Security 。当然这只是⼀个建议的组合，单纯从技术上来说，⽆论怎么组合，都是没有问题的。Shiro 和 Spring Security 相⽐，主要有如下⼀些特点：
1. Spring Security 是⼀个重量级的安全管理框架；Shiro 则是⼀个轻量级的安全管理框架
2. Spring Security 概念复杂，配置繁琐；Shiro 概念简单、配置简单
3. Spring Security 功能强⼤；Shiro 功能简单
------------------------------------------------------------------------------
## Spring Boot 支持哪些日志框架？
Spring Boot支持Java Util Logging、Log4j2、Logback ，如果你使用Starters启动器，Spring Boot将使用Logback作为默认日志框架。