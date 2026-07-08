
# MQ 如何保证消息可靠传输（消息不丢失）

## 一、核心总览
消息丢失分三大环节：生产者发送、MQ 服务端存储、消费者消费，RabbitMQ/Kafka/RocketMQ分别提供对应机制兜底，核心目标：消息一条不丢。

## 二、RabbitMQ可靠传输方案
1. 生产者端：防止发送丢失
   两种机制:事务tx（同步阻塞，低吞吐，不推荐）;confirm 确认模式生产首选。
   channel.txSelect() 开启事务，发送异常回滚 txRollback，正常提交 txCommit；
   缺点：同步阻塞，并发性能差。
   Publisher Confirm 发布确认（异步高性能）
   发送消息分配唯一 seq 序号，MQ 接收成功回调handleAck，失败回调handleNack，自行重发；分 3 种使用方式：
   普通 confirm：发一条阻塞等待确认；
   批量 confirm：批量发送后统一确认；
   异步回调 confirm：非阻塞，主流生产方案。
2. MQ 服务端：防止宕机丢消息
   同时开启两层持久化，缺一不可：
   队列持久化：创建队列标记 durable=true；
   消息持久化：发送消息deliveryMode=2；
   补充：搭配仲裁队列 Quorum Queue（Raft 多副本），节点宕机数据不丢失；仅持久化内存未刷盘时宕机极小概率丢少量消息，配合confirm可重试补偿。
3. 消费者端：防止消费丢失
   关闭自动ack（noAck=false），业务完全处理成功后手动 basicAck；
   若消费失败/进程宕机，未ack消息会重新投递给其他消费者，不会丢失。

## 三、Kafka 可靠传输方案
1. 生产者端 配置三大参数杜绝发送丢失：
   acks=all：消息同步至所有ISR副本才算写入成功；
   retries=最大值：写入失败无限重试；
   多副本replication.factor>1。
2. Broker 服务端（防止主从切换丢数据）
   4个核心配置：
   replication.factor >=2 分区至少 2 副本；
   min.insync.replicas>1 至少 1 个同步 follower 在线；
   acks=all 生产者强同步；
   unclean.leader.election.enable=false 禁止落后副本选主，避免数据丢失。
3. 消费者端
   关闭自动提交offset，业务处理完成后手动提交offset；
   风险：处理完未提交 offset 宕机，会重复消费，靠业务幂等解决。

## 四、RocketMQ 可靠传输方案
1. 生产者防丢失
   自带发送重试机制；
   核心业务使用事务消息：先发半消息（Half Message），执行本地事务；成功 commit消息对外可见，失败rollback丢弃；状态未知定时回查本地事务，防止消息丢失。
2. Broker服务端防丢失
   同步刷盘：flushDiskType=SYNC_FLUSH，消息落地磁盘再返回 ack；
   DLedger多副本集群：主从同步，过半节点同步完成才提交，主节点宕机自动选从节点接管；
   多主多从集群，磁盘故障靠副本备份。
3. 消费者防丢失
   业务处理完成返回CONSUME_SUCCESS才确认消费；失败返回RECONSUME_LATER自动重试；
   消息默认重试16次，超限转入死信队列，避免永久丢失。
## 五、标准面试口述答题话术
   保证消息可靠传输，要覆盖生产者、MQ 服务端、消费者三个环节，分别以 RabbitMQ、Kafka、RocketMQ 说明：
   生产者防丢失
   RabbitMQ优先用异步confirm确认机制，事务同步阻塞性能差不推荐；
   Kafka配置acks=all、无限重试；
   RocketMQ 支持发送重试，核心业务用事务消息保证本地事务与发消息原子性。
