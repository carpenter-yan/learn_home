互联网时代，面对复杂业务，讲究分而治之。将一个大的单体系统拆分为若干个微服务，保证每个系统的职责单一，可以垂直深度扩展。
但是一个个独立的微服务像一座座孤岛，如何将他们串联起来，才能发挥最大价值。

这时，我们就要提微服务的生态圈。
那么微服务生态圈都有哪些模块？他们的作用分别是什么？

服务的注册、发现。生产者启动时，会将自己的信息注册上报，这样调用方只需连接注册中心，根据一定的负载算法，就可以与服务提供方建立连接，从而实现应用间的解耦。
服务调用。通过多种协议（如：HTTP等）实现目标服务的真正调用。
负载均衡。主要是提供多种负载算法，满足不同业务场景下的集群多实例的选择机制
服务的稳定性。提供了服务熔断、限流、降级
分布式配置中心。应用的配置项统一管理，修改后能动态生效
消息队列。非核心逻辑从同步流程抽离，解耦，异步化处理，缩短RT时间
网关。将一些通用的处理逻辑，如：限流、鉴权、黑白名单、灰度等抽取到一个单独的、前置化系统统一处理。
监控。监控系统的健康状况
分布式链路追踪。查看接口的调用链路，为性能优化、排查问题提供输入
自动化部署。持续集成，快速部署应用。
围绕这些功能模块，Spring Cloud Alibaba 为我们提供了微服务化开发的一站式解决方案，我们只需要少量的Spring注解和yaml配置，便可以快速构建出一套微服务系统。真的是创业者的福音。

那么这套生态规范都提供了哪些技术框架呢？
## 分模块整理Spring微服务主流开源组件
分为国内主流（Spring Cloud Alibaba，企业最常用）、国际原生Spring Cloud，同时标注老旧停更组件（不推荐新项目使用）。
### 一、服务注册与发现
生产者上报实例信息，消费者拉取服务列表。
- 国内首选（主流）
Nacos（阿里开源）：同时兼顾注册中心+配置中心，一套组件搞定两件事；健康检查完善、支持权重、灰度、多环境，国内90%中小厂首选。
- 原生Spring Cloud备选
Consul：HashiCorp开源，自带健康检查、KV 配置、多数据中心，适合跨国多机房；
Zookeeper：强一致性，适合金融对一致性要求极高场景；偏重协调，服务治理功能弱。
- 老旧淘汰（新项目禁止）
Eureka（Netflix）：早已停止维护，仅老旧系统遗留使用。
### 二、服务调用（远程通信）
- 实现跨服务HTTP/RPC调用
OpenFeign（全生态通用标配）：声明式HTTP调用，基于注解写接口即可远程调用，适配LoadBalancer负载均衡，Spring全家桶通用。
Dubbo（Spring Cloud Alibaba）：高性能RPC协议，比HTTP更快，高并发服务推荐替换OpenFeign做内网调用。
- 老旧淘汰
原生 Feign、Ribbon：Ribbon停止维护，Spring官方替换为Spring Cloud LoadBalancer做客户端负载均衡。
### 三、负载均衡
- 客户端侧根据算法挑选服务节点
Spring Cloud LoadBalancer：Spring官方新一代，替代 Ribbon，内置轮询、随机，支持自定义权重；所有新项目标配。
Nacos内置负载：自带权重配置，可配合LoadBalancer使用。
Dubbo自带完整负载均衡：随机、轮询、一致性哈希、最小活跃数。
- 淘汰
Ribbon（Netflix 停更）。
### 四、熔断、限流、降级（服务稳定性）
防雪崩，异常限流、失败降级、超时熔断
- 国内主流
Sentinel（阿里）：功能最全，熔断+限流+热点限流+系统保护+自适应限流；带可视化控制台，规则动态修改，适配双十一高并发，国内业务首选。
- 国际开源首选
Resilience4j：Spring官方主推替代Hystrix，轻量无额外依赖，熔断、限流、重试、超时齐全，海外项目常用。
- 老旧淘汰
Hystrix（Netflix 停止维护，无新版本迭代）。
### 五、分布式配置中心
配置统一托管、动态刷新不用重启服务
1. Nacos Config：国内首选，和注册中心合体，配置实时推送、灰度发布、多环境、配置加密，运维极简。
2. Apollo（携程开源）：权限管控极强，多租户、精细化灰度、版本回滚，大型集团、多团队协作首选。
3. Spring Cloud Config（原生）：基于Git存放配置，搭配Spring Cloud Bus实现批量刷新；缺点依赖Git，动态配置运维复杂。
### 六、消息队列（异步解耦、削峰、缩短 RT）
通用开源 MQ，全部适配 Spring Cloud Stream：
1. RocketMQ（阿里）：国内主流，高可靠、高吞吐，支持事务消息、死信队列、延迟消息，适配Alibaba生态。
2. RabbitMQ：社区成熟，交换机灵活，适合业务复杂路由、可靠性优先场景。
3. Kafka：超高吞吐，日志、大数据、海量埋点首选。
### 七、API网关（统一入口：鉴权、限流、路由、灰度、跨域、黑白名单）
- 全行业新标准
Spring Cloud Gateway：Spring 官方新一代，基于 WebFlux 非阻塞，性能远高于 Zuul；支持动态路由、过滤器、限流、灰度，所有新项目统一用它。
- 淘汰
Zuul 1.x：阻塞 Servlet 架构，性能差、停止维护。
### 八、监控体系（服务健康、指标监控）
1. Spring Boot Actuator：Spring原生，暴露健康、内存、线程、接口指标，所有微服务必引入；
2. Prometheus + Grafana：云原生标配，时序数据库存指标，Grafana可视化大盘，监控QPS、CPU、内存、错误率；
3. Spring Boot Admin：轻量可视化，一站式查看所有服务健康、日志、启停。
### 九、分布式链路追踪（调用链路、性能排查）
定位慢接口、跨服务报错链路
1. SkyWalking（国产主流）：无侵入探针，Java 自动埋点，UI 友好，支持 JVM、MQ、DB 全链路，国内企业最常用。
2. Spring Cloud Sleuth + Zipkin：原生经典组合，Sleuth生成Trace/Span ID，Zipkin存储展示链路；简单轻量，适合小项目。
3. Jaeger：Uber 开源，云原生适配好，海外项目常用。
### 十、自动化部署、CI/CD（持续集成、快速发布）
不属于 Spring 组件，但是微服务标配流水线工具：
1. Jenkins：最通用 CI/CD 调度；
2. Docker + Kubernetes：容器化部署、滚动发布、灰度扩容；
3. Nacos/Gateway 灰度配合K8s实现分批上线。
### 两套成熟落地技术栈总结
1. 国内企业主流（Spring Cloud Alibaba）
Nacos（注册+配置）+ OpenFeign/Dubbo + LoadBalancer + Sentinel + Spring Cloud Gateway + SkyWalking + RocketMQ + Prometheus/Grafana
2. 国际原生 Spring Cloud
Consul + OpenFeign + LoadBalancer + Resilience4j + Gateway + Sleuth+Zipkin + RabbitMQ/Kafka + Prometheus