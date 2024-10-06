create database tuningStudy;

use tuningStudy;

DROP TABLE IF EXISTS users; # 기존 테이블 삭제

CREATE TABLE users (
                       id INT AUTO_INCREMENT PRIMARY KEY,
                       name VARCHAR(100),
                       age INT
);

-- 높은 재귀(반복) 횟수를 허용하도록 설정
-- (아래에서 생성할 더미 데이터의 개수와 맞춰서 작성하면 된다.)
SET SESSION cte_max_recursion_depth = 1000000;

-- 더미 데이터 삽입 쿼리
INSERT INTO users (name, age)
WITH RECURSIVE cte (n) AS
                   (
                       SELECT 1
                       UNION ALL
                       SELECT n + 1 FROM cte WHERE n < 1000000 -- 생성하고 싶은 더미 데이터의 개수
                   )
SELECT
    CONCAT('User', LPAD(n, 7, '0')),   -- 'User' 다음에 7자리 숫자로 구성된 이름 생성
    FLOOR(1 + RAND() * 1000) AS age    -- 1부터 1000 사이의 랜덤 값으로 나이 생성
FROM cte;

-- 잘 생성됐는 지 확인
SELECT COUNT(*) FROM users;

SELECT * FROM users
WHERE age = 24;

-- 인덱스 조회
show index from users;

-- 인덱스 생성
create index idx_age on users(age);

-- 인덱스 제거
alter table users drop index idx_age;


DROP TABLE IF EXISTS users; # 기존 테이블 삭제

CREATE TABLE users (
                       id INT PRIMARY KEY,
                       name VARCHAR(100)
);

INSERT INTO users (id, name) VALUES
                                 (1, 'a'),
                                 (3, 'b'),
                                 (5, 'c'),
                                 (7, 'd');

SELECT * FROM users;

update users
set id = 2
where id = 7
;

-- 이렇게 바뀌면 1357 순서로 내려옴.
-- PK 를 기준으로 데이터를 자체적으로 정렬함

DROP TABLE IF EXISTS users; # 기존 테이블 삭제

CREATE TABLE users (
                       id INT AUTO_INCREMENT PRIMARY KEY,
                       name VARCHAR(100) UNIQUE
);

show index from users;

-- 그럼 인덱스를 무조건 많이 거는게 좋은거냐?

-- 테이블 A: 인덱스가 없는 테이블
CREATE TABLE test_table_no_index (
                                     id INT AUTO_INCREMENT PRIMARY KEY,
                                     column1 INT,
                                     column2 INT,
                                     column3 INT,
                                     column4 INT,
                                     column5 INT,
                                     column6 INT,
                                     column7 INT,
                                     column8 INT,
                                     column9 INT,
                                     column10 INT
);

-- 테이블 B: 인덱스가 많은 테이블
CREATE TABLE test_table_many_indexes (
                                         id INT AUTO_INCREMENT PRIMARY KEY,
                                         column1 INT,
                                         column2 INT,
                                         column3 INT,
                                         column4 INT,
                                         column5 INT,
                                         column6 INT,
                                         column7 INT,
                                         column8 INT,
                                         column9 INT,
                                         column10 INT
);

-- 각 컬럼에 인덱스를 추가
CREATE INDEX idx_column1 ON test_table_many_indexes (column1);
CREATE INDEX idx_column2 ON test_table_many_indexes (column2);
CREATE INDEX idx_column3 ON test_table_many_indexes (column3);
CREATE INDEX idx_column4 ON test_table_many_indexes (column4);
CREATE INDEX idx_column5 ON test_table_many_indexes (column5);
CREATE INDEX idx_column6 ON test_table_many_indexes (column6);
CREATE INDEX idx_column7 ON test_table_many_indexes (column7);
CREATE INDEX idx_column8 ON test_table_many_indexes (column8);
CREATE INDEX idx_column9 ON test_table_many_indexes (column9);
CREATE INDEX idx_column10 ON test_table_many_indexes (column10);

show index from test_table_many_indexes;

-- 높은 재귀(반복) 횟수를 허용하도록 설정
-- (아래에서 생성할 더미 데이터의 개수와 맞춰서 작성하면 된다.)
SET SESSION cte_max_recursion_depth = 800000;

-- 인덱스가 없는 테이블에 데이터 80만개 삽입: 385ms
INSERT INTO test_table_no_index (column1, column2, column3, column4, column5, column6, column7, column8, column9, column10)
WITH RECURSIVE cte AS (
    SELECT 1 AS n
    UNION ALL
    SELECT n + 1 FROM cte WHERE n < 800000
)
SELECT
    FLOOR(RAND() * 1000),
    FLOOR(RAND() * 1000),
    FLOOR(RAND() * 1000),
    FLOOR(RAND() * 1000),
    FLOOR(RAND() * 1000),
    FLOOR(RAND() * 1000),
    FLOOR(RAND() * 1000),
    FLOOR(RAND() * 1000),
    FLOOR(RAND() * 1000),
    FLOOR(RAND() * 1000)
FROM cte;

-- 인덱스가 없는 테이블에 데이터 조회: 277ms
select *
from test_table_no_index
where column1 = 488
;

-- 인덱스가 많은 테이블에 데이터 80만개 삽입: 1s 785ms
INSERT INTO test_table_many_indexes (column1, column2, column3, column4, column5, column6, column7, column8, column9, column10)
WITH RECURSIVE cte AS (
    SELECT 1 AS n
    UNION ALL
    SELECT n + 1 FROM cte WHERE n < 800000
)
SELECT
    FLOOR(RAND() * 1000),
    FLOOR(RAND() * 1000),
    FLOOR(RAND() * 1000),
    FLOOR(RAND() * 1000),
    FLOOR(RAND() * 1000),
    FLOOR(RAND() * 1000),
    FLOOR(RAND() * 1000),
    FLOOR(RAND() * 1000),
    FLOOR(RAND() * 1000),
    FLOOR(RAND() * 1000)
FROM cte;

-- 인덱스가 많은 테이블에 데이터 조회: 65ms
select *
from test_table_many_indexes
where column1 = 488
;

-- 멀티 컬럼 인덱스
DROP TABLE IF EXISTS users;

CREATE TABLE users (
                       id INT AUTO_INCREMENT PRIMARY KEY,
                       이름 VARCHAR(100),
                       부서 VARCHAR(100),
                       나이 INT
);

INSERT INTO users (이름, 부서, 나이) VALUES
                                   ('박미나', '회계', 26),
                                   ('김미현', '회계', 23),
                                   ('김민재', '회계', 21),
                                   ('이재현', '운영', 24),
                                   ('조민규', '운영', 23),
                                   ('하재원', '인사', 22),
                                   ('최지우', '인사', 22);

CREATE INDEX idx_부서_이름 ON users (부서, 이름);

SHOW INDEX FROM users;

-- 인덱스 O
# -> Sort: users.`부서`, users.`이름`  (cost=0.95 rows=7) (actual time=0.0546..0.0552 rows=7 loops=1)
#     -> Table scan on users  (cost=0.95 rows=7) (actual time=0.0302..0.0351 rows=7 loops=1)
explain analyze
select *
from users
order by 부서, 이름;

-- 인덱스 X
# -> Sort: users.`이름`, users.`부서`  (cost=0.95 rows=7) (actual time=0.075..0.0756 rows=7 loops=1)
#     -> Table scan on users  (cost=0.95 rows=7) (actual time=0.0314..0.0366 rows=7 loops=1)
explain analyze
select *
from users
order by 이름, 부서;

# 3. 실행 계획
DROP TABLE IF EXISTS users; # 기존 테이블 삭제

CREATE TABLE users (
                       id INT AUTO_INCREMENT PRIMARY KEY,
                       name VARCHAR(100),
                       age INT
);

INSERT INTO users (name, age) VALUES
                                  ('박미나', 26),
                                  ('김미현', 23),
                                  ('김민재', 21),
                                  ('이재현', 24),
                                  ('조민규', 23),
                                  ('하재원', 22),
                                  ('최지우', 22);

## 3.1 실행 계획 타입
### 3.1.1 all: full table scan
# 인덱스를 활용하지 않고 테이블을 처음부터 끝가지 전부 다 뒤져 데이터를 찾는 방식
DROP TABLE IF EXISTS users; # 기존 테이블 삭제

CREATE TABLE users (
                       id INT AUTO_INCREMENT PRIMARY KEY,
                       name VARCHAR(100),
                       age INT
);

INSERT INTO users (name, age) VALUES
                                  ('Alice', 30),
                                  ('Bob', 23),
                                  ('Charlie', 35);

EXPLAIN SELECT * FROM users WHERE age = 23; # type : ALL


### 3.1.2 index: full index scan
# 인덱스 테이블을 처음부터 끝까지 다 뒤져 데이터를 찾는 방식
DROP TABLE IF EXISTS users; # 기존 테이블 삭제

CREATE TABLE users (
                       id INT AUTO_INCREMENT PRIMARY KEY,
                       name VARCHAR(100),
                       age INT
);

-- 높은 재귀(반복) 횟수를 허용하도록 설정
-- (아래에서 생성할 더미 데이터의 개수와 맞춰서 작성하면 된다.)
SET SESSION cte_max_recursion_depth = 1000000;

-- 더미 데이터 삽입 쿼리
INSERT INTO users (name, age)
WITH RECURSIVE cte (n) AS
                   (
                       SELECT 1
                       UNION ALL
                       SELECT n + 1 FROM cte WHERE n < 1000000 -- 생성하고 싶은 더미 데이터의 개수
                   )
SELECT
    CONCAT('User', LPAD(n, 7, '0')),   -- 'User' 다음에 7자리 숫자로 구성된 이름 생성
    FLOOR(1 + RAND() * 1000) AS age    -- 1부터 1000 사이의 난수로 나이 생성
FROM cte;

CREATE INDEX idx_name ON users (name);

EXPLAIN SELECT * FROM users
        ORDER BY name
            LIMIT 10;

## 3.3.3 const
# unique index, pk 를 이용해, 1건의 데이터 바로 찾을 수 있는 경우
DROP TABLE IF EXISTS users; # 기존 테이블 삭제

CREATE TABLE users (
                       id INT AUTO_INCREMENT PRIMARY KEY,
                       account VARCHAR(100) UNIQUE
);

INSERT INTO users (account) VALUES
                                ('user1@example.com'),
                                ('user2@example.com'),
                                ('user3@example.com');

EXPLAIN SELECT * FROM users WHERE id = 3;
EXPLAIN SELECT * FROM users WHERE account = 'user3@example.com';

### 3.3.4 range: index range scan
# 인덱스를 활용해 범위 형테의 데이터를 조회(between, 부등호, in, like)
DROP TABLE IF EXISTS users;

CREATE TABLE users (
                       id INT AUTO_INCREMENT PRIMARY KEY,
                       age INT
);

-- 높은 재귀(반복) 횟수를 허용하도록 설정
-- (아래에서 생성할 더미 데이터의 개수와 맞춰서 작성하면 된다.)
SET SESSION cte_max_recursion_depth = 1000000;

-- 더미 데이터 삽입 쿼리
INSERT INTO users (age)
WITH RECURSIVE cte (n) AS
                   (
                       SELECT 1
                       UNION ALL
                       SELECT n + 1 FROM cte WHERE n < 1000000 -- 생성하고 싶은 더미 데이터의 개수
                   )
SELECT
    FLOOR(1 + RAND() * 1000) AS age    -- 1부터 1000 사이의 난수로 나이 생성
FROM cte;

CREATE INDEX idx_age ON users(age);

EXPLAIN SELECT * FROM users
        WHERE age BETWEEN 10 and 20;

EXPLAIN SELECT * FROM users
        WHERE age IN (10, 20, 30);

EXPLAIN SELECT * FROM users
        WHERE age < 20;

### 3.3.5 ref: not unique index
# 비고유 인덱스를 활용하는 경우
DROP TABLE IF EXISTS users; # 기존 테이블 삭제

CREATE TABLE users (
                       id INT AUTO_INCREMENT PRIMARY KEY,
                       name VARCHAR(100)
);

INSERT INTO users (name) VALUES
                             ('박재성'),
                             ('김지현'),
                             ('이지훈');

CREATE INDEX idx_name ON users(name);

EXPLAIN SELECT * FROM users WHERE name = '박재성';