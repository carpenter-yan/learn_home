
# ES 写入、查询、搜索底层原理 面试精简笔记

## 一、三种请求集群路由流程
1. 单文档写入（新增 / 更新 / 删除）
   请求发到任意节点，该节点为协调节点coordinating node；
   根据文档ID哈希路由到对应主分片节点；
   主分片执行写入，同步数据到所有副本分片；
   主副本全部写入成功后，协调节点返回客户端成功。
2. 根据DocId精准单查
   请求到协调节点，按ID哈希定位分片；
   轮询主/副本分片做读负载均衡；
   目标节点返回文档，协调节点统一返回客户端。
3. 全文检索（两阶段 Query+Fetch）
   Query 阶段：协调节点把查询下发所有分片，各分片仅返回匹配docId集合；协调节点合并、排序、分页，截取需要的docId列表。
   Fetch 阶段：协调节点根据上一步 docId，去对应分片拉取完整文档数据，组装返回结果。
   读写区分：写只能走主分片；读可轮询主/副本分担压力。

## 二、写入底层持久化完整流程（NRT准实时原理）
   核心组件：内存buffer、translog、segment、os cache、flush/commit
   数据同时写入内存buffer + translog（预写日志）；
   buffer内数据不可检索；translog 防止宕机丢数据。
   refresh（默认 1s 一次，准实时来源）
   buffer数据写入os cache生成新segment文件，清空buffer；
   os cache内数据可被检索，因此ES写入后约 1 秒可见。
   translog 持久策略
   默认5秒刷盘；配置request则每条写入强制刷盘，无数据丢失但性能下降；默认最多丢失5秒数据。
   flush（commit，默认30min/translog过大触发）
   ① 执行一次refresh；
   ② os cache中所有segment强制fsync落地磁盘；
   ③ 写入commit point记录所有有效segment；
   ④ 清空旧translog，生成新日志。
   更新 / 删除机制（无原地修改）
   删除：生成.del标记文件，搜索时过滤标记删除文档；
   更新：旧文档标记删除，新增一条完整新文档；
   segment merge 合并：后台自动合并多个小segment，物理清理被标记删除的数据，生成大 segment 并删除旧文件。

## 三、底层基础：Lucene 与倒排索引
   Lucene：单机搜索引擎库，ES基于Lucene封装分布式能力，最小存储单元为 segment。
   倒排索引（正向文档→反向词条映射）
   正向索引：DocId → 内容；
   倒排索引：词条 Word → 所有包含该词的 DocId 列表；
   检索时直接通过词条快速匹配文档，是全文检索核心。

## 四、面试标准口述答题话术
   我分集群路由、底层写入持久、检索两阶段、底层 Lucene倒排四块说明：
   集群路由：所有请求先到协调节点；写入路由到主分片并同步副本；
       单查轮询主副本；全文检索分Query拿docId、Fetch拉完整文档两阶段执行。
   写入持久与准实时：数据同时进buffer和translog；默认1s执行refresh刷入os cache，数据才可搜索，所以ES是准实时；
       定时flush把segment强制落盘；更新删除仅做标记，后台merge才物理清理旧数据。translog默认5秒刷盘，宕机最多丢5秒数据。
   检索两阶段：Query阶段各分片只返回匹配文档ID，协调节点合并排序；Fetch阶段再根据ID拉取完整文档，减少网络传输开销。
   底层基础：ES封装Lucene，依靠倒排索引实现高速全文检索；倒排索引是词条映射文档ID，区别于正向文档存储。