-- 유저 이름으로 특정 기간에 작성된 글 검색하는 SQL문 튜닝하기.
DROP TABLE IF EXISTS posts;
DROP TABLE IF EXISTS users;

CREATE TABLE users (
                       id INT AUTO_INCREMENT PRIMARY KEY,
                       name VARCHAR(50) NOT NULL,
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE posts (
                       id INT AUTO_INCREMENT PRIMARY KEY,
                       title VARCHAR(255) NOT NULL,
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       user_id INT,
                       FOREIGN KEY (user_id) REFERENCES users(id)
);

-- 높은 재귀(반복) 횟수를 허용하도록 설정
-- (아래에서 생성할 더미 데이터의 개수와 맞춰서 작성하면 된다.)
SET SESSION cte_max_recursion_depth = 1000000;

-- users 테이블에 더미 데이터 삽입
INSERT INTO users (name, created_at)
WITH RECURSIVE cte (n) AS
                   (
                       SELECT 1
                       UNION ALL
                       SELECT n + 1 FROM cte WHERE n < 1000000 -- 생성하고 싶은 더미 데이터의 개수
                   )
SELECT
    CONCAT('User', LPAD(n, 7, '0')) AS name,  -- 'User' 다음에 7자리 숫자로 구성된 이름 생성
    TIMESTAMP(DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 3650) DAY) + INTERVAL FLOOR(RAND() * 86400) SECOND) AS created_at -- 최근 10년 내의 임의의 날짜와 시간 생성
FROM cte;

-- posts 테이블에 더미 데이터 삽입
INSERT INTO posts (title, created_at, user_id)
WITH RECURSIVE cte (n) AS
                   (
                       SELECT 1
                       UNION ALL
                       SELECT n + 1 FROM cte WHERE n < 1000000 -- 생성하고 싶은 더미 데이터의 개수
                   )
SELECT
    CONCAT('Post', LPAD(n, 7, '0')) AS name,  -- 'User' 다음에 7자리 숫자로 구성된 이름 생성
    TIMESTAMP(DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 3650) DAY) + INTERVAL FLOOR(RAND() * 86400) SECOND) AS created_at, -- 최근 10년 내의 임의의 날짜와 시간 생성
    FLOOR(1 + RAND() * 50000) AS user_id -- 1부터 50000 사이의 난수로 급여 생성
FROM cte;

#
explain analyze
select *
from users u join posts p on u.id = p.user_id
where 1 = 1
  and u.name = 'User0000046'
  and p.created_at between '2024-03-09 19:01:22' and '2024-06-25 18:02:49'
;

# explain
# table p 에 type 이 all 으로 테이블 전체 데이터 조회 중. -> where 절에서 name 사용 하고 있으므로, 인덱스 추가 하자.
# 1,SIMPLE,p,,ALL,user_id,,,,997442,11.11,Using where
# 1,SIMPLE,u,,eq_ref,PRIMARY,PRIMARY,4,tuningstudy.p.user_id,1,10,Using where

# explain analyze
# -> Nested loop inner join  (cost=148184 rows=11082) (actual time=615..615 rows=0 loops=1)
#     -> Filter: ((p.created_at between '2024-03-09 19:01:22' and '2024-06-25 18:02:49') and (p.user_id is not null))  (cost=100473 rows=110816) (actual time=0.101..569 rows=29478 loops=1)
#         -> Table scan on p  (cost=100473 rows=997442) (actual time=0.0822..190 rows=1e+6 loops=1)
#     -> Filter: (u.`name` = 'User0000046')  (cost=0.331 rows=0.1) (actual time=0.00151..0.00151 rows=0 loops=29478)
#         -> Single-row index lookup on u using PRIMARY (id=p.user_id)  (cost=0.331 rows=1) (actual time=0.00134..0.00136 rows=1 loops=29478)

show index from posts;
show index from users;

create index idx_name on users(name);
alter table users
    drop index idx_name;

# where 절에 있는 create_at 는 인덱스를 걸어도 안타므로 제거.
create index idx_created_at on posts(created_at);
alter table posts
drop index idx_created_at;