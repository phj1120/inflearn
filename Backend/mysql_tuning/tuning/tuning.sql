DROP TABLE IF EXISTS users; # 기존 테이블 삭제

CREATE TABLE users
(
    id   INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100),
    age  INT
);

-- 높은 재귀(반복) 횟수를 허용하도록 설정
-- (아래에서 생성할 더미 데이터의 개수와 맞춰서 작성하면 된다.)
SET SESSION cte_max_recursion_depth = 1000000;

-- 더미 데이터 삽입 쿼리
INSERT INTO users (name, age)
WITH RECURSIVE cte (n) AS
                   (SELECT 1
                    UNION ALL
                    SELECT n + 1
                    FROM cte
                    WHERE n < 1000000 -- 생성하고 싶은 더미 데이터의 개수
                   )
SELECT CONCAT('User', LPAD(n, 7, '0')), -- 'User' 다음에 7자리 숫자로 구성된 이름 생성
       FLOOR(1 + RAND() * 1000) AS age  -- 1부터 1000 사이의 난수로 나이 생성
FROM cte;

SELECT *
FROM users
LIMIT 100;

DROP TABLE IF EXISTS users;

CREATE TABLE users
(
    id         INT AUTO_INCREMENT PRIMARY KEY,
    name       VARCHAR(100),
    department VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 높은 재귀(반복) 횟수를 허용하도록 설정
-- (아래에서 생성할 더미 데이터의 개수와 맞춰서 작성하면 된다.)
SET SESSION cte_max_recursion_depth = 1000000;

-- 더미 데이터 삽입 쿼리
INSERT INTO users (name, department, created_at)
WITH RECURSIVE cte (n) AS
                   (SELECT 1
                    UNION ALL
                    SELECT n + 1
                    FROM cte
                    WHERE n < 1000000 -- 생성하고 싶은 더미 데이터의 개수
                   )
SELECT CONCAT('User', LPAD(n, 7, '0'))                                                                       AS name,       -- 'User' 다음에 7자리 숫자로 구성된 이름 생성
       CASE
           WHEN n % 10 = 1 THEN 'Engineering'
           WHEN n % 10 = 2 THEN 'Marketing'
           WHEN n % 10 = 3 THEN 'Sales'
           WHEN n % 10 = 4 THEN 'Finance'
           WHEN n % 10 = 5 THEN 'HR'
           WHEN n % 10 = 6 THEN 'Operations'
           WHEN n % 10 = 7 THEN 'IT'
           WHEN n % 10 = 8 THEN 'Customer Service'
           WHEN n % 10 = 9 THEN 'Research and Development'
           ELSE 'Product Management'
           END                                                                                               AS department, -- 의미 있는 단어 조합으로 부서 이름 생성
       TIMESTAMP(DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 3650) DAY) +
                 INTERVAL FLOOR(RAND() * 86400) SECOND)                                                      AS created_at  -- 최근 10년 내의 임의의 날짜와 시간 생성
FROM cte;

-- 잘 생성됐는 지 확인
SELECT COUNT(*)
FROM users;
SELECT *
FROM users
LIMIT 10;

explain
SELECT *
FROM users
WHERE created_at >= DATE_SUB(NOW(), INTERVAL 3 DAY);

create index idx_create_at on users (created_at);

alter table users
    drop index idx_create_at
;

## 4.2
DROP TABLE IF EXISTS users;

CREATE TABLE users
(
    id         INT AUTO_INCREMENT PRIMARY KEY,
    name       VARCHAR(100),
    department VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 높은 재귀(반복) 횟수를 허용하도록 설정
-- (아래에서 생성할 더미 데이터의 개수와 맞춰서 작성하면 된다.)
SET SESSION cte_max_recursion_depth = 1000000;

-- 더미 데이터 삽입 쿼리
INSERT INTO users (name, department, created_at)
WITH RECURSIVE cte (n) AS
                   (SELECT 1
                    UNION ALL
                    SELECT n + 1
                    FROM cte
                    WHERE n < 1000000 -- 생성하고 싶은 더미 데이터의 개수
                   )
SELECT CONCAT('User', LPAD(n, 7, '0'))                                                                       AS name,       -- 'User' 다음에 7자리 숫자로 구성된 이름 생성
       CASE
           WHEN n % 10 = 1 THEN 'Engineering'
           WHEN n % 10 = 2 THEN 'Marketing'
           WHEN n % 10 = 3 THEN 'Sales'
           WHEN n % 10 = 4 THEN 'Finance'
           WHEN n % 10 = 5 THEN 'HR'
           WHEN n % 10 = 6 THEN 'Operations'
           WHEN n % 10 = 7 THEN 'IT'
           WHEN n % 10 = 8 THEN 'Customer Service'
           WHEN n % 10 = 9 THEN 'Research and Development'
           ELSE 'Product Management'
           END                                                                                               AS department, -- 의미 있는 단어 조합으로 부서 이름 생성
       TIMESTAMP(DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 3650) DAY) +
                 INTERVAL FLOOR(RAND() * 86400) SECOND)                                                      AS created_at  -- 최근 10년 내의 임의의 날짜와 시간 생성
FROM cte;

-- 잘 생성됐는 지 확인
SELECT COUNT(*)
FROM users;
SELECT *
FROM users
LIMIT 10;

explain
SELECT *
FROM users
WHERE department = 'Sales'
  AND created_at >= DATE_SUB(NOW(), INTERVAL 3 DAY)
;

show index from users;

-- range 40ms
create index idx_create_at on users (created_at);
alter table users
    drop index idx_create_at;

-- ref 160ms: department 의 데이터 중복이 많아 대상 rows 가 많음.
create index idx_department on users (department);
alter table users
    drop index idx_department;

-- range 40ms
create index idx_create_at_department on users (created_at, department);
alter table users
    drop index idx_create_at_department;

-- range 40ms
create index idx_department_create_at on users (department, created_at);
alter table users
    drop index idx_department_create_at;

-- 조회하는 데이터 row 수를 줄이는게 중요함 -> 중복 정도가 낮은 컬럼을 기준으로 인덱스 생성

-- 인덱스를 여러개 걸어도, 옵티마이저가 더 효율적이라 판단하는 인덱스가 탐.

-- create_at, department 멀티 컬럼 인덱스를 걸어도 create_at 에 인덱스를 건 것 보다 큰 성능 향상은 없음.
-- 인덱스를 최소화 하는 것이 좋으니, 멀티 컬럼인덱스를 걸었을때와 단일 컬럼 인덱스를 걸었을때의 성능이 비슷하다면 단일 컬럼 인덱스를 걸자.

# 4.3
DROP TABLE IF EXISTS users; # 기존 테이블 삭제

CREATE TABLE users
(
    id   INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100),
    age  INT
);

-- 높은 재귀(반복) 횟수를 허용하도록 설정
-- (아래에서 생성할 더미 데이터의 개수와 맞춰서 작성하면 된다.)
SET SESSION cte_max_recursion_depth = 1000000;

-- 더미 데이터 삽입 쿼리
INSERT INTO users (name, age)
WITH RECURSIVE cte (n) AS
                   (SELECT 1
                    UNION ALL
                    SELECT n + 1
                    FROM cte
                    WHERE n < 1000000 -- 생성하고 싶은 더미 데이터의 개수
                   )
SELECT CONCAT('User', LPAD(n, 7, '0')), -- 'User' 다음에 7자리 숫자로 구성된 이름 생성
       FLOOR(1 + RAND() * 1000) AS age  -- 1부터 1000 사이의 난수로 나이 생성
FROM cte;

CREATE INDEX idx_name ON users (name);

EXPLAIN
SELECT *
FROM users
ORDER BY name DESC
limit 2660
;

-- 넓은 범위의 데이터를 조회할때
-- 인덱스를 거치지 않고 원래 테이블을 가져와서 정렬하는 것이 더 효율적이라고 옵티마이저가 판단했기 때문에 인덱스르 타지 않음.(잘 판단한거임)
-- limit 걸어서 범위를 줄이면 인덱스 탐

# 4.4
DROP TABLE IF EXISTS users;

CREATE TABLE users
(
    id         INT AUTO_INCREMENT PRIMARY KEY,
    name       VARCHAR(100),
    salary     INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 높은 재귀(반복) 횟수를 허용하도록 설정
-- (아래에서 생성할 더미 데이터의 개수와 맞춰서 작성하면 된다.)
SET SESSION cte_max_recursion_depth = 1000000;

-- users 테이블에 더미 데이터 삽입
INSERT INTO users (name, salary, created_at)
WITH RECURSIVE cte (n) AS
                   (SELECT 1
                    UNION ALL
                    SELECT n + 1
                    FROM cte
                    WHERE n < 1000000 -- 생성하고 싶은 더미 데이터의 개수
                   )
SELECT CONCAT('User', LPAD(n, 7, '0'))                                                                       AS name,      -- 'User' 다음에 7자리 숫자로 구성된 이름 생성
       FLOOR(1 + RAND() * 1000000)                                                                           AS salary,    -- 1부터 1000000 사이의 난수로 급여 생성
       TIMESTAMP(DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 3650) DAY) +
                 INTERVAL FLOOR(RAND() * 86400) SECOND)                                                      AS created_at -- 최근 10년 내의 임의의 날짜와 시간 생성
FROM cte;

CREATE INDEX idx_name ON users (name);
CREATE INDEX idx_salary ON users (salary);

# User000000으로 시작하는 이름을 가진 유저 조회
EXPLAIN
SELECT *
FROM users
WHERE SUBSTRING(name, 1, 10) = 'User000000';

EXPLAIN
SELECT *
FROM users
WHERE name like 'User000000%';

# 2달치 급여(salary)가 1000 이하인 유저 조회
explain
select *
from users
where salary * 2 < 1000
order by salary;

explain
select *
from users
where salary < 1000 / 2
order by salary;

-- 인덱스 컬럼을 가공하면 인덱스을 활용하지 못할 가능성이 큼.

# 4.5
DROP TABLE IF EXISTS users;

CREATE TABLE users
(
    id         INT AUTO_INCREMENT PRIMARY KEY,
    name       VARCHAR(100),
    department VARCHAR(100),
    salary     INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 높은 재귀(반복) 횟수를 허용하도록 설정
-- (아래에서 생성할 더미 데이터의 개수와 맞춰서 작성하면 된다.)
SET SESSION cte_max_recursion_depth = 1000000;

-- 더미 데이터 삽입 쿼리
INSERT INTO users (name, department, salary, created_at)
WITH RECURSIVE cte (n) AS
                   (SELECT 1
                    UNION ALL
                    SELECT n + 1
                    FROM cte
                    WHERE n < 1000000 -- 생성하고 싶은 더미 데이터의 개수
                   )
SELECT CONCAT('User', LPAD(n, 7, '0'))                                                                       AS name,       -- 'User' 다음에 7자리 숫자로 구성된 이름 생성
       CASE
           WHEN n % 10 = 1 THEN 'Engineering'
           WHEN n % 10 = 2 THEN 'Marketing'
           WHEN n % 10 = 3 THEN 'Sales'
           WHEN n % 10 = 4 THEN 'Finance'
           WHEN n % 10 = 5 THEN 'HR'
           WHEN n % 10 = 6 THEN 'Operations'
           WHEN n % 10 = 7 THEN 'IT'
           WHEN n % 10 = 8 THEN 'Customer Service'
           WHEN n % 10 = 9 THEN 'Research and Development'
           ELSE 'Product Management'
           END                                                                                               AS department, -- 의미 있는 단어 조합으로 부서 이름 생성
       FLOOR(1 + RAND() * 1000000)                                                                           AS salary,     -- 1부터 1000000 사이의 난수로 나이 생성
       TIMESTAMP(DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 3650) DAY) +
                 INTERVAL FLOOR(RAND() * 86400) SECOND)                                                      AS created_at  -- 최근 10년 내의 임의의 날짜와 시간 생성
FROM cte;

explain analyze
SELECT *
FROM users
ORDER BY salary
LIMIT 100;

# 인덱스 X, limit O
# -> Limit: 100 row(s)  (cost=100569 rows=100) (actual time=316..316 rows=100 loops=1)
#     -> Sort: users.salary, limit input to 100 row(s) per chunk  (cost=100569 rows=996636) (actual time=316..316 rows=100 loops=1)
#         -> Table scan on users  (cost=100569 rows=996636) (actual time=0.118..235 rows=1e+6 loops=1)

# 인덱스 O, limit O
# -> Limit: 100 row(s)  (cost=0.0918 rows=100) (actual time=0.618..0.651 rows=100 loops=1)
#     -> Index scan on users using idx_salary  (cost=0.0918 rows=100) (actual time=0.618..0.643 rows=100 loops=1)

explain analyze
SELECT *
FROM users
ORDER BY salary
;

# 인덱스 X, limit X
# -> Sort: users.salary  (cost=100569 rows=996636) (actual time=665..723 rows=1e+6 loops=1)
#     -> Table scan on users  (cost=100569 rows=996636) (actual time=0.0895..230 rows=1e+6 loops=1)

# 인덱스 O, limit X
# 넓은 범위의 데이터를 대상으로 하기 때문에 인덱스를 타지 않음
# -> Sort: users.salary  (cost=100569 rows=996636) (actual time=632..694 rows=1e+6 loops=1)
#     -> Table scan on users  (cost=100569 rows=996636) (actual time=0.0567..218 rows=1e+6 loops=1)

create index idx_salary on users (salary);

alter table users
    drop index idx_salary;

# 4.6 where index vs order by index
DROP TABLE IF EXISTS users;

CREATE TABLE users
(
    id         INT AUTO_INCREMENT PRIMARY KEY,
    name       VARCHAR(100),
    department VARCHAR(100),
    salary     INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 높은 재귀(반복) 횟수를 허용하도록 설정
-- (아래에서 생성할 더미 데이터의 개수와 맞춰서 작성하면 된다.)
SET SESSION cte_max_recursion_depth = 1000000;

-- 더미 데이터 삽입 쿼리
INSERT INTO users (name, department, salary, created_at)
WITH RECURSIVE cte (n) AS
                   (SELECT 1
                    UNION ALL
                    SELECT n + 1
                    FROM cte
                    WHERE n < 1000000 -- 생성하고 싶은 더미 데이터의 개수
                   )
SELECT CONCAT('User', LPAD(n, 7, '0'))                                                                       AS name,       -- 'User' 다음에 7자리 숫자로 구성된 이름 생성
       CASE
           WHEN n % 10 = 1 THEN 'Engineering'
           WHEN n % 10 = 2 THEN 'Marketing'
           WHEN n % 10 = 3 THEN 'Sales'
           WHEN n % 10 = 4 THEN 'Finance'
           WHEN n % 10 = 5 THEN 'HR'
           WHEN n % 10 = 6 THEN 'Operations'
           WHEN n % 10 = 7 THEN 'IT'
           WHEN n % 10 = 8 THEN 'Customer Service'
           WHEN n % 10 = 9 THEN 'Research and Development'
           ELSE 'Product Management'
           END                                                                                               AS department, -- 의미 있는 단어 조합으로 부서 이름 생성
       FLOOR(1 + RAND() * 1000000)                                                                           AS salary,     -- 1부터 1000000 사이의 난수로 나이 생성
       TIMESTAMP(DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 3650) DAY) +
                 INTERVAL FLOOR(RAND() * 86400) SECOND)                                                      AS created_at  -- 최근 10년 내의 임의의 날짜와 시간 생성
FROM cte;

explain analyze
SELECT *
FROM users
WHERE created_at >= DATE_SUB(NOW(), INTERVAL 3 DAY)
  AND department = 'Sales'
ORDER BY salary
LIMIT 100;
# 320ms
# -> Limit: 100 row(s)  (cost=93924 rows=100) (actual time=320..320 rows=100 loops=1)
#     -> Sort: users.salary, limit input to 100 row(s) per chunk  (cost=93924 rows=996636) (actual time=320..320 rows=100 loops=1)
#         -> Filter: ((users.department = 'Sales') and (users.created_at >= <cache>((now() - interval 3 day))))  (cost=93924 rows=996636) (actual time=0.821..320 rows=109 loops=1)
#             -> Table scan on users  (cost=93924 rows=996636) (actual time=0.0975..231 rows=1e+6 loops=1)

-- where 절 create_at 에 index 를 건다면 create_at 에 인덱스를 걸면 데이터 범위를 더 줄일 수 있어 효율적이 었음.
-- order by 절 salary 에 index 를 건다면 sort 를 하지 않아도 되어 효율적이 었음.

show index from users;

create index idx_create_at on users (created_at);
alter table users
    drop index idx_create_at;

# 11ms
# -> Limit: 100 row(s)  (cost=497 rows=100) (actual time=11.9..11.9 rows=100 loops=1)
#     -> Sort: users.salary, limit input to 100 row(s) per chunk  (cost=497 rows=1103) (actual time=11.9..11.9 rows=100 loops=1)
#         -> Filter: (users.department = 'Sales')  (cost=497 rows=1103) (actual time=0.132..11.7 rows=109 loops=1)
#             -> Index range scan on users using idx_create_at over ('2024-10-03 11:30:38' <= created_at), with index condition: (users.created_at >= <cache>((now() - interval 3 day)))  (cost=497 rows=1103) (actual time=0.0532..11.6 rows=1103 loops=1)


create index idx_salary on users (salary);
alter table users
    drop index idx_salary;

# 1s 398ms
# -> Limit: 100 row(s)  (cost=9.09 rows=3.33) (actual time=34.7..1398 rows=100 loops=1)
#     -> Filter: ((users.department = 'Sales') and (users.created_at >= <cache>((now() - interval 3 day))))  (cost=9.09 rows=3.33) (actual time=34.7..1398 rows=100 loops=1)
#         -> Index scan on users using idx_salary  (cost=9.09 rows=100) (actual time=0.802..1349 rows=944183 loops=1)


# -> Limit: 100 row(s)  (cost=497 rows=100) (actual time=12.1..12.1 rows=100 loops=1)
#     -> Sort: users.salary, limit input to 100 row(s) per chunk  (cost=497 rows=1103) (actual time=12.1..12.1 rows=100 loops=1)
#         -> Filter: (users.department = 'Sales')  (cost=497 rows=1103) (actual time=0.226..12 rows=109 loops=1)
#             -> Index range scan on users using idx_create_at over ('2024-10-03 11:33:32' <= created_at), with index condition: (users.created_at >= <cache>((now() - interval 3 day)))  (cost=497 rows=1103) (actual time=0.0575..11.4 rows=1103 loops=1)

# 4.7 Having
DROP TABLE IF EXISTS users;

CREATE TABLE users
(
    id         INT AUTO_INCREMENT PRIMARY KEY,
    name       VARCHAR(100),
    age        INT,
    department VARCHAR(100),
    salary     INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 높은 재귀(반복) 횟수를 허용하도록 설정
-- (아래에서 생성할 더미 데이터의 개수와 맞춰서 작성하면 된다.)
SET SESSION cte_max_recursion_depth = 1000000;

-- 더미 데이터 삽입 쿼리
INSERT INTO users (name, age, department, salary, created_at)
WITH RECURSIVE cte (n) AS
                   (SELECT 1
                    UNION ALL
                    SELECT n + 1
                    FROM cte
                    WHERE n < 1000000 -- 생성하고 싶은 더미 데이터의 개수
                   )
SELECT CONCAT('User', LPAD(n, 7, '0'))                                                                       AS name,       -- 'User' 다음에 7자리 숫자로 구성된 이름 생성
       FLOOR(1 + RAND() * 100)                                                                               AS age,        -- 1부터 100 사이의 난수로 생성
       CASE
           WHEN n % 10 = 1 THEN 'Engineering'
           WHEN n % 10 = 2 THEN 'Marketing'
           WHEN n % 10 = 3 THEN 'Sales'
           WHEN n % 10 = 4 THEN 'Finance'
           WHEN n % 10 = 5 THEN 'HR'
           WHEN n % 10 = 6 THEN 'Operations'
           WHEN n % 10 = 7 THEN 'IT'
           WHEN n % 10 = 8 THEN 'Customer Service'
           WHEN n % 10 = 9 THEN 'Research and Development'
           ELSE 'Product Management'
           END                                                                                               AS department, -- 의미 있는 단어 조합으로 부서 이름 생성
       FLOOR(1 + RAND() * 1000000)                                                                           AS salary,     -- 1부터 1000000 사이의 난수로 생성
       TIMESTAMP(DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 3650) DAY) +
                 INTERVAL FLOOR(RAND() * 86400) SECOND)                                                      AS created_at  -- 최근 10년 내의 임의의 날짜와 시간 생성
FROM cte;

CREATE INDEX idx_age ON users (age);
alter table users
    drop index idx_age;

explain
SELECT age, MAX(salary)
FROM users
GROUP BY age
HAVING age >= 20
   AND age < 30;

# 1204ms,
# age index 를 타지만, salary 의 정보도 필요하기때문에 테이블과 데이터를 매핑해서 가져오는데,
# 데이터 row 수가 많이 오래 걸림.
# -> Filter: ((users.age >= 20) and (users.age < 30))  (cost=200263 rows=101) (actual time=276..1204 rows=10 loops=1)
#     -> Group aggregate: max(users.salary)  (cost=200263 rows=101) (actual time=50.5..1204 rows=100 loops=1)
#         -> Index scan on users using idx_age  (cost=100624 rows=996389) (actual time=0.414..1165 rows=1e+6 loops=1)

# query hint
explain analyze
SELECT /*+ NO_INDEX(users idx_age) */ age, MAX(salary)
FROM users
GROUP BY age
HAVING age >= 20
   AND age < 30;
# 291ms 인덱스를 타서 오래걸려 인덱스 안타도록 쿼리 힌트 줌.
# -> Filter: ((users.age >= 20) and (users.age < 30))  (actual time=291..291 rows=10 loops=1)
#     -> Table scan on <temporary>  (actual time=291..291 rows=100 loops=1)
#         -> Aggregate using temporary table  (actual time=291..291 rows=100 loops=1)
#             -> Table scan on users  (cost=100624 rows=996389) (actual time=0.0855..162 rows=1e+6 loops=1)

explain analyze
SELECT age, MAX(salary)
FROM users
where age >= 20
  AND age < 30
GROUP BY age
# 167ms where 절로 가져오는 데이터 수를 줄여서, group by 연산에 대상 row 수를 줄임.
# -> Group aggregate: max(users.salary)  (cost=104825 rows=95) (actual time=45.2..167 rows=10 loops=1)
#     -> Index range scan on users using idx_age over (20 <= age < 30), with index condition: ((users.age >= 20) and (users.age < 30))  (cost=85766 rows=190590) (actual time=0.0352..163 rows=99311 loops=1)


