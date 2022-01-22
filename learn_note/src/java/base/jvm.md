[面试官：为什么需要Java内存模型？](https://www.cnblogs.com/Java3y/p/15405045.html)  
[转：深入理解Java G1垃圾收集器](https://www.cnblogs.com/sidesky/p/10797382.html)  
[详解 JVM Garbage First(G1) 垃圾收集器](https://www.cnblogs.com/draem0507/p/9350699.html)  
[CMS 与 G1 垃圾收集器的思考 (1)](https://zhuanlan.zhihu.com/p/71058481)  
[G1垃圾回收器详解](https://www.jianshu.com/p/aef0f4765098)  
[G1垃圾收集器详解](http://www.javaadu.online/?p=465)  
[三色标记的漏标问题及两种解决方案](https://blog.csdn.net/qq_32310175/article/details/107724213)  
[三色标记法与读写屏障](https://www.jianshu.com/p/12544c0ad5c1)  
[G1垃圾收集器详解（3）之CSet](https://cloud.tencent.com/developer/article/1847053)  
[G1垃圾收集器详解（4）之RSet](https://cloud.tencent.com/developer/article/1824880)  
[G1垃圾收集器详解（5）之Card Table](https://cloud.tencent.com/developer/article/1824884)  
[G1垃圾收集器（6）之写屏障](https://cloud.tencent.com/developer/article/1824885)  
[G1垃圾收集器（6）之Young GC](https://cloud.tencent.com/developer/article/1824886)  
[G1垃圾收集器（7）之Mixed GC](https://cloud.tencent.com/developer/article/1838041)  
[G1垃圾收集器之年轻代GC日志（完全年轻代）](https://cloud.tencent.com/developer/article/1838043)  
[G1垃圾收集器（10）之mixed gc日志](https://cloud.tencent.com/developer/article/1838044)

https://blog.csdn.net/wangchaox123/article/details/100658938
Linux进程突然被杀掉（OOM killer），查看系统日志

[一次完整的JVM堆外内存泄漏故障排查记录](https://www.cnblogs.com/adolfmc/p/13580748.html)  
[Netty堆外内存泄露排查与总结](https://blog.csdn.net/MeituanTech/article/details/83177051)  
[java堆外内存泄漏](https://blog.csdn.net/f529352479/article/details/51908655?locationNum=10&fps=1)

[堆外内存泄漏排查过程记录](http://jnews.jd.com/circle-info-detail?postId=81697)  
1 背景

2021Q1指挥调度中心系统作为青龙3战略项目之一，由快递主导，分拣、运输、揽派、中台-配运、中台-路由、职能-人资、大数据等部门共同参与，以信息共享为基础，以协同决策、指挥调度为核心的运行机制。通过对快递业务保障节点进行实时、异常的有效管控，优化人、车、货、场资源配置，建立调度与挽救机制，从理论、技术、解决方案等角度探索研究和应用实践，实现快递业务运行效率的全面提升、科学决策和资源优化。
作为从0-1的系统，团队面临的挑战是系统如何快速落地和上线、数百指标如何在毫秒级返回。京东当前技术生态下，采用传统阻塞式编程无法在性能和编码上完美的解决系统面临的问题，我们通过对业务和系统分析、以及技术架构设计，引入了WebFlux非阻塞式编程框架，进行探索和技术创新实践。
2 发现问题

在上线过程中，火眼监控发现进程内存超过85%，进程频繁被kill，我们的堆内存设置是2G（容器内存的50%），很明显是发生了内存泄漏。

3 解决过程

3.1 怀疑堆内存泄漏

使用压测工具（MAC上使用Wrk, Windows上使用ab压力测试工具），压测自己的服务，在服务内存占用比较高的时候，JONE上dump堆内存，使用MAT工具查看，有大量Unreachable的对象，此时怀疑是jvm参数设置不合理。

换了以前线上跑的很稳定的jvm参数并且添加上gc log打印日志，重启服务进行压测，还是同样存在内存泄漏，证明了不是jvm参数的问题。
同时gc log打印出的日志显示堆内存回收后一直没有增长，这说明并没有堆内存泄漏。但是，这和我们dump下来的内存文件有明显差异，到底那块出了问题呢？

gc log是不会骗人的，问题应该是出在dump上面了。仔细看JONE上的dump工具是没有任何其他参数的，那么在dump时候就不会触发fullgc，当然也就存在很多Unreacheable的对象干扰我们的判断了。解决方式有两种，第一登录到容器上自己使用jmap -dump:live,file=dump_001.bin PID命令自己dump堆文件，第二种添加ump对jvm做监控。由于第二种方式需要改动代码，时间有限，我们采取第一种更加简单的方式，结果发现Unreachable对象都消失了，并且堆的各种对象占比比较正常，没有增长的迹象，这和gc log指征的是相同的。那一直在增长的内存是什么？只能是堆外的直接内存了。

3.2 直接内存泄漏排查过程（山重水复疑无路）

因为我们的服务依赖WebFlux技术栈，并且对WebFlux做了一些定制化的增强，且我们使用了Reactive Feign, r2dbc等组件，他们底层都是依赖于Netty，而Netty默认使用的是直接内存，如果发生直接内存泄漏，也不是奇怪的事情。并且从最开始的现象看，内存直接涨到了85%，远远超过了我们的堆内存，也肯定是发生了直接内存泄漏，但排查起来却是大海捞针。
首先，我们分析Java8的一些改变，比如Metaspace使用的是直接内存，在查询资料时候，有些案例（https://www.cnblogs.com/adolfmc/p/13580748.html）就出现了反射使用类加载，一直不会卸载类，最终Metaspace占用的直接内存越来越大，最后OOM。我们继续重启服务压测，在服务内存占用特别高的时候，登录到机器上，使用jstat –gcmetacapacity PID 命令查看Metaspace 使用内存大小，一直为100M左右并未增长，说明并非是Metaspace泄漏问题。此时，我们也注意到我们的另外一个app，使用了同样的技术，只是业务代码不同，却没有发生OOM。通过以上对比，我们继续将目光缩小到WebFlux或者Reactive Feign、 r2dbc这些组价上，多数情况下是他们底层依赖的Netty产生了直接内存泄漏。
带着我们的怀疑，我们继续查询资料。在网上的案例中（https://blog.csdn.net/MeituanTech/article/details/83177051），有Netty底层Bug造成的直接内存泄漏，我们仿照案例的方法，打印出Netty的直接内存占用量，发现这个数值并没有增长。这就非常奇怪了，从上面的数据看，我们堆肯定是没有泄漏的，所以还是直接内存泄漏的问题，只是这个案例和我们遇到的情况不同。
到这，我们继续查阅资料，看看有没有工具能够直接定位到直接内存泄漏的地方。整个业界使用比较多的还是gperftool来排查直接内存泄漏(https://blog.csdn.net/f529352479/article/details/51908655?locationNum=10&fps=1)。这个案例中，还是能直接看出端倪的，如图：

Java_java_util_zip_Inflater_init占据了大量内存，但是在我们的服务下，截图如下：

3.3 直接内存排查（柳暗花明又一村）

在我们使用了市场上通用的一些手段，依然一筹莫展的情况下，我们突然想起了我们另外一个服务B使用了完全相同的技术，只是业务代码不同，没有使用Reactive Feign，却没有发生内存泄漏，既然常规的工具不能解决问题，那我们何不利用排除法，不断压测来定位问题的根源呢。
第一步，我们本着成熟组件不会有问题，一般是我们自己的代码造成的人祸的思想，来排除自己对于框架的增强代码，然后压测。发现依然是OOM。
第二步，在排除自己对于框架的增强代码的前提下，排除Reactive Feign，因为服务B和其他服务最大的不同就在于没有Reactive Feign，继续压测，此时就没有了OOM问题，内存曲线比较平稳了，至此问题缩小到了reactive feign组件上面。
第三步，在排除自己对于框架的增强代码的前提下，升级Reactive Feign到最新版本，继续压测，结果OOM再次出现。新版本的Reactive Feign是由playtika做的，他们内部也在使用，为啥会有问题呢？经过查看它的源代码，发现底层使用的是WebClient，那我们继续排查是不是WebClient的问题。
第四步，新建一个接口，内部直接使用WebClient调用，继续压测，发现还是有内存泄漏。但是WebClient是Spring Boot内部组件，居然存在内存泄漏问题。查看资料，WebClient在使用不适当的情况下确实存在内存泄漏问题，但是我们使用方式是官网的例子也是正确的。问题进一步缩小到WebClient上。
第五步，查询WebClient官网，官网推荐加上-Dio.netty.leakDetection.level=paranoid并且将Netty日志调整成logging.level.reactor.netty=DEBUG，按照上述调整后再次压测，终于捕捉到了异常日志：

发现确实是WebClient底层在调用encode编码时候发生了直接内存泄漏，结合我们之前使用过WebClient却没有发生内存泄漏问题，我们考虑可能是Spring Boot版本问题，事实上，我们也只能通过升级版本来试试了，而官网上针对此类问题，也是建议升级版本。
第六步，升级Spring Boot从2.3.1到2.3.11 （就差一个数字）release后，再次压测，OOM问题消失了。

至此，直接内存泄漏问题完全解决了。就是Spring Boot自己的锅！！
4 总结

JVM内存泄漏要考虑全面，堆内存，Metaspace直接内存，其他直接内存要一步步排除，缩小、锁定范围；
充分利用工具，但不能一直依赖工具，如果工具不能明确指征问题所在就要另辟蹊径；
排除大法，虽然耗时但是在工具都不好用的时候它是最管用、最高效的方式；
充分利用历史经验对比差异、分析和思考根因，不要放过任何有差异的地方；
第一自己写代码的锅，然后开源组件的锅，最后是Spring的锅，排查顺序不要变，但Spring不一定是完美的；
使用开源组件时候，最好选用大版本的中间小版本。

本次排查使用到的工具如下：压测工具： wrk 、 ab压测工具；内存监控工具： 火眼监控、top、free等；JVM监控与分析工具：jmap、 jstat 、gc.log；堆内存分析工具： MAT、 VisualVM；直接内存分析工具：Google Performance Tools 、 gdb；Netty内存泄漏分析工具：-Dio.netty.leakDetection.level；