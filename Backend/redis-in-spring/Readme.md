# Redis

key value 로 구성 된 NoSQL

메모리에 데이터를 들고 있어 응답 속도가 빠르다.

-> 디스크보다 메모리의 공간이 적기 때문에 많은 데이터를 가지고 있을 수 없음

-> 데이터의 유효 시간 관리 (TTL)

레디스를 사용하는 방식에 여러가지가 있지만, 아래 두 방식을 많이 사용함.

1. Cache Aside

```
API 요청(Read)
    key 로 Redis 조회
        if 해당하는 값 존재
            값 반환
        else
            API 로직 수행
            값 반환
            key 에 값 저장
```

2. Write Around

```
CUD 발생 시, DB 에만 반영(Redis 에는 미반영) 
```

데이터 정합성이 맞지 않을 수 있는데, 데이터 정합성과 빠른 응답 모두를 충족할 수는 없음.

Redis 는 데이터의 정합성보다 빠른 응답이 필요한 상황에서 사용(캐싱)

## 성능 개선 팁

성능 개선 방식에 여러가지가 있지만, SQL 튜닝을 가장 먼저하고 그래도 해결이 되지 않으면 다른 방식을 고려.

쿼리를 개선 함으로 근본적인 문제 해결하고 추가 환경을 구성할 필요 없으니 더 적은 비용으로 해결 할 수 있음.

ex, 캐싱 서버 활용, [레플리케이션](https://www.coovil.net/db-replication/), [샤딩](https://aws.amazon.com/ko/what-is/database-sharding/), [DB 스케일업](https://azure.microsoft.com/ko-kr/resources/cloud-computing-dictionary/scaling-out-vs-scaling-up)...

성능 개선은 무조건 지표를 기반으로 말할 수 있어야함.

ex. k6, jmeter...

## Redis 명령어

```bash
redis-cli

set key value ex 60

get key

keys *

ttl

del key
```