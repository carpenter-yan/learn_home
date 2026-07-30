
# ES写入、查询、搜索底层原理 面试精简笔记

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
------------------------------------------------------------------------------
## 精通es查询和统计？
Elasticsearch查询与统计完整体系（后端实战 + 面试全覆盖）
适配Java + Elasticsearch，覆盖基础查询、复合查询、聚合统计、分页、高亮、深度分页、优化，分为DSL语法、Java代码、高频场景、避坑优化四部分。
### 一、ES 核心基础前提
ES底层基于Lucene，存储结构：索引 (index)= 数据库，type 废弃，文档 (doc)= 行，field = 字段；
查询分两类：
query 查询：算相关性打分_score，适合全文检索（商品标题、详情）；
filter 过滤：不打分、走缓存，精准匹配（状态、时间、分类），性能更高；
开发主流工具：Elasticsearch RestHighLevelClient；Mybatis-ES、Jest淘汰。
### 二、常用基础查询（DSL+Java 思路）
1. 精准过滤（Filter），无打分
   适合等值、范围、包含，推荐放bool.filter
   常用过滤语法 
   term：精准匹配，不分词（id、状态、枚举、手机号）
```
json
// 订单状态=已支付，精准匹配
{
    "term": {
        "orderStatus": "PAID"
    }
}
```
terms：多值in查询，terms: {field: [A,B,C]}
range：区间（时间、金额），gte 大于等于，lte 小于等于
```
"range": {
    "payTime": {
        "gte": "2026-01-01",
        "lte": "2026-07-30",
        "format": "yyyy-MM-dd"
    }
}
```
exists：判断字段不为 null；bool 组合多条件。
2. 全文检索（Query，打分），分词检索
   match：最常用，字段分词检索，支持中文拆分
```
json
// 标题包含“华为手机”，分词匹配华为/手机任一都命中
{
    "match": {
        "title": "华为手机"
    }
}
```
match_phrase：短语匹配，要求分词连续，精准度更高；
multi_match：多字段同时检索（标题、详情一起搜关键词）；
wildcard：通配符*，慎用，性能差。
3. Bool 复合查询（日常 90% 场景都用 bool）
   四大子句，组合所有查询条件：
   must：必须满足，打分，等价 AND；
   must_not：必须不满足，过滤，等价 NOT；
   should：满足任意即可，OR，控制权重；
   filter：必须满足，不打分、可缓存，优先放精准条件。
   标准结构：全文关键词放must，精准状态/时间放filter
```
json
{
    "query": {
        "bool": {
            "must": [{"match": {"title": "耳机"}}],
            "filter": [
                {"term": {"status": 1}},
                {"range": {"price": {"gte": 100, "lte": 500}}}
            ]
        }
    }
}
```
4. 排序、分页、高亮
   sort：支持普通字段排序、按得分_score排序，多字段优先级
```
"sort": [
    {"_score": "desc"},
    {"payTime": "desc"}
]
```
普通分页：from+size；from 越大越慢，深度分页禁用；
高亮 highlight：关键词加标签，前端标红
```
"highlight": {
    "fields": {"title": {}},
    "pre_tags": "<em>",
    "post_tags": "</em>"
}
```
### 三、ES 聚合统计（核心，分组、求和、计数、均值）
聚合统一在aggs节点，分为桶聚合（分组）、指标聚合（计算）。
1. 指标聚合（单值计算，MySQL 聚合函数）
   表格
   ES 聚合|MySQL 等价|作用
   count|count()|文档总数
   sum|sum()|金额总和
   avg|avg()|平均值
   max/min|max/min|最大最小
   cardinality|count(distinct)|去重计数（UV 统计）
   示例：统计所有订单总金额、平均实付
   json
   {
   "size": 0, // 不需要返回明细，只看统计
   "aggs": {
   "total_amount": {"sum": {"field": "payAmount"}},
   "avg_money": {"avg": {"field": "payAmount"}}
   }
   }

2. 桶聚合（分组 Group By），最常用 3 种
   （1）Terms 分组（group by 字段）
   按支付状态分组，统计每组订单数、总金额
   json
   {
   "size": 0,
   "aggs": {
   "group_status": {
   "terms": {"field": "orderStatus"},
   // 分组内嵌套指标聚合
   "aggs": {
   "sum_pay": {"sum": {"field": "payAmount"}}
   }
   }
   }
   }

（2）Date Histogram 时间柱状聚合（按天 / 小时统计）
按天统计订单量，做报表、折线图必备
json
"date_histogram": {
"field": "payTime",
"calendar_interval": "day", // day/hour/month
"format": "yyyy-MM-dd"
}

（3）Range 区间分组（价格区间统计）
比如：0-100，100-500，500 以上分区间统计数量。
3. 多层嵌套聚合
   先按天分组，每天内部再按支付状态分组；实现 MySQL 多层 group by。
   四、Java RestHighLevelClient 标准代码模板
1. 通用 Bool 查询模板
   java
   运行
   // 1.构建请求
   SearchRequest request = new SearchRequest("order_index");
   SearchSourceBuilder source = new SearchSourceBuilder();

// bool组装
BoolQueryBuilder bool = QueryBuilders.boolQuery();
// 全文检索
bool.must(QueryBuilders.matchQuery("title", "蓝牙耳机"));
// 精准过滤
bool.filter(QueryBuilders.termQuery("status", 1));
bool.filter(QueryBuilders.rangeQuery("payAmount").gte(100));

source.query(bool);
source.size(10);
source.sort("_score", SortOrder.DESC);

// 高亮
HighlightBuilder highlight = new HighlightBuilder();
highlight.field("title").preTags("<red>").postTags("</red>");
source.highlighter(highlight);

request.source(source);
// 执行请求
SearchResponse resp = client.search(request, RequestOptions.DEFAULT);

2. 聚合统计 Java 示例：按状态分组求和
   java
   运行
   SearchRequest request = new SearchRequest("order_index");
   SearchSourceBuilder source = new SearchSourceBuilder();
   source.size(0); // 不要明细只统计

// 1.第一层分组：按orderStatus
TermsAggregationBuilder statusAgg = AggregationBuilders
.terms("group_by_status").field("orderStatus.keyword");
// 2.分组内聚合总金额
statusAgg.subAggregation(AggregationBuilders.sum("total_money").field("payAmount"));

source.aggregation(statusAgg);
request.source(source);

SearchResponse response = client.search(request, RequestOptions.DEFAULT);
// 解析聚合结果
Terms terms = response.getAggregations().get("group_by_status");
for (Terms.Bucket bucket : terms.getBuckets()) {
String status = bucket.getKeyAsString();
long count = bucket.getDocCount();
Sum sum = bucket.getAggregations().get("total_money");
double total = sum.getValue();
}

### 五、高频实战场景解决方案
分页量大（上万条）
from+size 深度分页会内存爆炸；解决方案：
浅分页：from+size；
深分页：search_after（基于最后一条排序值滚动分页）；
大批量导出：scroll 滚动查询。
精准匹配踩坑：text 字段用 term 查不到
text 类型分词存储，term 匹配原始分词，必须：
用 match 查 text；
精准字段定义为 keyword 不分词，term 只查 keyword。
中文乱分词：使用 ik 分词器（ik_max_word 细粒度、ik_smart 粗粒度）。
多条件复杂报表：全部用聚合，禁止查出全量内存汇总。
### 六、聚合统计常见业务落地
运营报表：按日 / 按月订单量、销售额（date_histogram + sum）；
渠道统计：按渠道分组订单数（terms）；
商品价格分布：range 区间聚合；
访客 UV：cardinality 去重统计用户 ID；
多维度交叉统计：嵌套多层 aggs。
### 七、性能优化（精通必备）
过滤条件放 filter 不走 must，利用 ES 缓存；
不查大返回：size 尽量小，统计务必 size=0；
keyword 用于精准查询、分组聚合；text 只用于全文搜索；
聚合字段建议开启 doc_values，加快分组；
禁止聚合超大索引，可按日期拆分多索引（按天分索引）；
避免 wildcard、regex 正则，扫描全索引。
### 八、面试高频区分 & 背诵总结
query打分用于检索，filter无打分可缓存，日常组合bool 使用；
聚合分桶聚合（分组 terms/date_histogram）、指标聚合（sum/count/avg/cardinality）；
text分词适合match搜索，keyword不分词适合term、分组、排序；
浅分页from/size，深分页search_after，大批量导出scroll；
Java统一用RestHighLevelClient拼装DSL，不用手写JSON字符串；
绝大多数后台报表、多维度筛选统计，全部依赖 ES 聚合，不用查明细内存计算。