
# Redis 过期策略&内存淘汰&LRU

## 一、过期删除策略（清理已设置 TTL 的过期 key）
Redis采用惰性删除 + 定期删除组合，不使用定时删除。
1. 惰性删除
   访问 key 时才校验是否过期，过期直接删除并返回 nil。
   优点：CPU 开销极低，无主动扫描；
   缺点：长期无人访问的过期 key 会常驻内存，造成内存泄漏。
2. 定期删除
   每 100ms 随机抽取一批带过期的 key 检查删除，不会全量遍历。
   优点：主动释放内存，弥补惰性删除缺陷；
   缺点：只能清理部分过期 key，仍有残留。
   补充兜底
   两种策略仍会堆积大量过期 key，内存占满后触发内存淘汰机制。

## 二、六大内存淘汰策略（maxmemory 达到上限触发）
   noeviction（默认不推荐）：拒绝写入，直接报错；
   allkeys-lru（生产最常用）：全部 key 中淘汰最近最少使用；
   allkeys-random：所有 key 随机删除，无优先级；
   volatile-lru：仅在带过期 key 中淘汰 LRU；
   volatile-random：仅过期 key 随机删；
   volatile-ttl：优先删除剩余过期时间最短的 key。

## 三、LRU 算法（最近最少使用）
   核心思想
   缓存容量满时，移除长时间未访问的数据，保留热点数据。
   Java 简易实现（LinkedHashMap）
``` java
   public class LRUCache<K,V> extends LinkedHashMap<K,V> {
       private int capacity;
       public LRUCache(int capacity) {
           super(capacity, 0.75f, true);
           this.capacity = capacity;
       }
       @Override
       protected boolean removeEldestEntry(Map.Entry<K,V> eldest) {
           return size() > capacity;
       }
   }
```
   原理：开启 accessOrder，访问后自动移至链表头部，重写方法超容量删除队尾最久未使用元素。

## 四、面试标准口述话术
   Redis分两层机制清理无效数据：
   过期删除：惰性删除 + 定期删除配合。惰性删除访问时清理过期 key，节省CPU；定期每100ms随机扫描部分过期key主动释放内存，但仍会残留大量过期数据。
   内存淘汰：内存打满触发6种淘汰策略，线上首选allkeys-lru，全局淘汰最少访问 key，最大化缓存命中率；其余策略仅针对带过期 key 或随机删除，业务场景很少用。
   LRU 核心是淘汰久未访问数据，Java 可通过 LinkedHashMap 快速实现，利用有序链表把热点数据维持在头部，超容量移除尾部冷数据。