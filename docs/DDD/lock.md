## 낙관적 락 (Optimistic Lock)

* 트랜잭션 대부분 충돌이 발생하지 않는다고 가정하는 방법으로써 어플리케이션이 제공하는 락 방식입니다. 
* 그러므로 읽는 시점에 Lock을 사용하지 않기 때문에 데이터를 수정하는 시점에 다른 사용자에 의해 데이터가 변경되었는지 변경여부를 확인해야 합니다.
* 이렇게 낙관적 락은 트랜잭션을 커밋하기 전까지는 트랜잭션의 충돌 여부를 알 수 없습니다.



## 비관적 락 (Pessimistic Lock)

> **비관적락은 내가 접근하고 하는 Database 리소스에 다른사람이 접근조차 하지못하도록 락을 걸고 작업을 진행하는 것**을 말합니다.

* 자원에 대한 동시 요청이 발생하여 일관성에 문제가 생길 것이라고 비관적으로 생각하고 이를 방지하기 위해 우선 락을 거는 방식
  * 트랜잭션끼리의 충돌이 발생한다고 가정하고 우선 락을 거는 방법
* 트랜잭션이 데이터에 대한 LOCK을 선점하기 때문에 다른 트랜잭션들의 지연(WAIT)을 유발할 수 있음
  * 선점 잠금이라고 불리기도 함
* DB에서 제공하는 락기능을 사용

Pessimistic Lock은 배타적 락(Exclusive Lock)과 공유 락(Shared Lock) 두가지 타입이 있다.



 물론 여기서 접근이라는 것은 **READ** 작업과 **WRITE** 작업이 분할되어 있습니다. 경우에 맞춰 둘다 불가능할지 아니면 하나만 가능할지를 정하는것이 가능합니다. 

비관적락을 사용할 때 2가지 옵션을 선택할 수 있습니다. **배타락(exclusive lock)과 공유락(shared lock)**입니다. 

공유락을 걸면 다른 트랜잭션에서는 읽기는 가능하지만 쓰기는 불가능힙니다. 

베타락에서는 다른 트랜잭션에서 읽기와 쓰기가 모두 부가능합니다. 배타락은 쿼리로 보면 `SELECT … FOR UPDATE`로 나타낼 수 있습니다.

JPA에서 비관적 락의 옵션을 3가지입니다. 아래 옵션들은 낙관적락과 마찬가지로 `LockModeType` enum으로 설정할 수 있습니다. 아래의 락 설정들은 해당 트랜잭션이 commit 되거나 rollback 될때까지 **유지됩니다.**





### PESSIMISTIC_READ 공유락(shared lock)

해당 리소스에 공유락을 겁니다. 타 트랜잭션에서 읽기는 가능하지만 쓰기는 불가능**해집니다.

`LockModeType.PESSIMISTIC_READ` 옵션으로 진행했을 때 발생하는 쿼리들을 확인해보도록 하겠습니다. 먼저 아래는 성공적으로 update 될때 출력되는 쿼리입니다. **select를 하며 `in share mode`가 출력되며 update 쿼리도 정상출력되어 결과적으로 업데이트가 완료됨을 알 수 있었습니다.**

```
select
    user0_.id as id1_12_,
    user0_.created_datetime as created_2_12_,
    user0_.updated_datetime as updated_3_12_,
    user0_.email as email4_12_,
    user0_.is_third_party as is_third5_12_,
    user0_.nickname as nickname6_12_,
    user0_.password as password7_12_,
    user0_.third_party_type as third_pa8_12_,
    user0_.version as version9_12_ 
from
    user user0_ 
where
    user0_.id=? lock in share mode

update
    user 
set
    is_third_party=? 
where
    id=?
```

2곳에서 동시에 발생시켜 LockException이 발생했을때는 아래와 같은 stacktrace가 잡혔습니다. **에러 로그 메시지로는 Dealock 을 찾았다는 말과 함께 예외가 아래처럼 발생하는 것을 알 수 있었습니다.**

```
2021-08-15 18:04:45.802 ERROR 65445 --- [       Thread-9] o.h.engine.jdbc.spi.SqlExceptionHelper   : (conn=277) Deadlock found when trying to get lock; try restarting transaction
Exception in thread "Thread-9" org.springframework.dao.CannotAcquireLockException: could not execute batch; SQL
Caused by: org.hibernate.exception.LockAcquisitionException: could not execute batch
[...중략...]
Caused by: java.sql.BatchUpdateException: (conn=277) Deadlock found when trying to get lock; try restarting transaction
[...중략...]
Caused by: java.sql.SQLTransactionRollbackException: (conn=277) Deadlock found when trying to get lock; try restarting transaction
[...중략...]
Caused by: org.mariadb.jdbc.internal.util.exceptions.MariaDbSqlException: Deadlock found when trying to get lock; try restarting transaction
[...중략...]
Caused by: java.sql.SQLException: Deadlock found when trying to get lock; try restarting transaction
```

### PESSIMISTIC_WRITE 배타락(exclusive lock)

`LockModeType.PESSIMISTIC_WRITE` 옵션을 사용하여 쿼리를 실행했을 때에는 **아래와 같이 select 쿼리의 마지막에 `for update`가 붙는것을 알 수 있었습니다.**

해당 리소스에 베타락을 겁니다. 타 트랜잭션에서는 읽기와 쓰기 모두 불가능**해집니다. (DBMS 종류에 따라 상황이 달라질 수 있습니다)

```
select
    user0_.id as id1_12_,
    user0_.created_datetime as created_2_12_,
    user0_.updated_datetime as updated_3_12_,
    user0_.email as email4_12_,
    user0_.is_third_party as is_third5_12_,
    user0_.nickname as nickname6_12_,
    user0_.password as password7_12_,
    user0_.third_party_type as third_pa8_12_ 
from
    user user0_ 
where
    user0_.id=? for update

update
    user 
set
    is_third_party=? 
where
    id=?
```

그렇다면 2개의 트랜잭션을 동시에 하나의 리소스에 접근시키면 어떻게 될까요 ? 처음에 저는 예외를 에상했지만 예외는 나지 않았습니다. 결과적으로 **for update가 걸려 있을때 MariaDB에서는 하나의 리소스에 대해서 여러 READ의 경우 Error가 나진 않고 `MVCC`를 통해서 `select ... for update`는 동시에 가능한것을 확인**하였습니다. 그리고 `for update`가 붙어있을 때 업데이트가 진행되면 Lock 이걸려 있는 순서대로 update가 진행되는 점을 확인할 수 있었습니다. `MVCC`를 지원하지 않는 DB를 사용한다면 멀티 READ시 에러가 날 것입니다.





- PESSIMISTIC_FORCE_INCREMENT – 해당 리소스에 베타락을 겁니다. 타 트랜잭션에서는 읽기와 쓰기 모두 불가능해집니다. 추가적으로 낙관적락처럼 버저닝을 합니다. 따라서 버전에 대한 컬럼이 필요합니다.



## Exception

비관적 락에서 발생할 수 있는 예외상황은 아래와 같습니다.

### PessimisticLockException 

>  트랜잭션에서 락 취득을 하지 못해서 생기는 예외.한번에 하나의 Lock만 얻을 수 있으며, 락을 가져오는데 실패하면 발생하는 예외

### PersistanceException 

값을 변경할 때 생길 수 있는 예외상황 (NoResultException, NoUniqueResultException, LockTimeoutException, QueryTimeoutException) 등등 이럴경우 결과적으로 롤백됩니다.영속성 문제가 발생했을때의 예외

### LockTimeoutException 

> 트랜잭션에서 락 취득을 타임아웃 시간내에 해지 못해서 생기는 예외



## Lock Scope

### PessimisticLockScope.NORMAL
>  기본값으로써 해당 entity만 lock이 걸립니다.
> @Inheritance(strategy = InheritanceType.JOINED)와 같이 조인 상속을 사용하면 부모도 함께 잠급니다.

### PessimisticLockScope.EXTENDED
>  @ElementCollection, @OneToOne , @OneToMany 등 연관된 entity들도 lock이 됩니다.



