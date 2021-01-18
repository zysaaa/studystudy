```sql
CREATE TABLE `person`  (
  `id` int(11) NOT NULL,
  `age` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Compact;

INSERT INTO `person` VALUES (1, 18);
INSERT INTO `person` VALUES (2, 22);

```


**testcase1:**

```sql
事务1：

set session transaction isolation level read committed;
begin
select * from person;  --1  看到id1，2的
select * from person;  --3  看到id1，2的
select * from person;  --5  看到id1，2, 3的

事务2：

set session transaction isolation level read committed;
begin
insert into person values(3, 22) --2
commit --4

```


**testcase2:**

```sql
事务1：

set session transaction isolation level REPEATABLE READ;
begin
select * from person;  --1  看到id1，2的
select * from person;  --3  看到id1，2的
select * from person;  --5  看到id1，2的   --这是所谓的快照读，RR下 MVCC机制，避免了写幻读。 
-- 如果把这一行改成：select * from person for update;就能看到id1，2，3的数据了
-- select * from person where id = xxx 同理。

-- 所谓的：
--     快照读：就是select
       select * from table ….;
       当前读：特殊的读操作，插入/更新/删除操作，属于当前读，处理的都是当前的数据，需要加锁。
       select * from table where ? lock in share mode;
       select * from table where ? for update;
       insert;
       update ;
       delete;


事务2：

set session transaction isolation level REPEATABLE READ;
begin
insert into person values(3, 22) --2
commit --4

```

**testcase3:**
```sql
set session transaction isolation level REPEATABLE READ
begin
select * from person where age = 888  --1
update person set age = 222 where age = 888 --2
commit


事务2：
set session transaction isolation level REPEATABLE READ
begin

insert into person values(125,22) --3 卡死，由于age不是主键，事务1直接使用了next-key锁，所以此处直接无法插入。
--行锁和GAP锁结合形成的的Next-Key锁共同解决了RR级别在写数据时的幻读问题。
commit

```

**总结**：

在快照读读情况下，mysql通过mvcc来避免幻读。
在当前读读情况下，mysql通过next-key来避免幻读。
select * from t where a=1;属于快照读
select * from t where a=1 lock in share mode;属于当前读

不能把快照读和当前读得到的结果不一样这种情况认为是幻读，这是两种不同的使用。所以我认为mysql的rr级别是解决了幻读的。

https://github.com/Yhzhtk/note/issues/42
