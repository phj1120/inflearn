-- 특정 부서에서 최대 연봉을 가진 사용자들 조회하는 SQL문 튜닝하기
DROP TABLE IF EXISTS posts;
DROP TABLE IF EXISTS users;

CREATE TABLE users (
                       id INT AUTO_INCREMENT PRIMARY KEY,
                       name VARCHAR(100),
                       department VARCHAR(100),
                       salary INT,
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 높은 재귀(반복) 횟수를 허용하도록 설정
-- (아래에서 생성할 더미 데이터의 개수와 맞춰서 작성하면 된다.)
SET SESSION cte_max_recursion_depth = 1000000;

-- 더미 데이터 삽입 쿼리
INSERT INTO users (name, department, salary, created_at)
WITH RECURSIVE cte (n) AS
                   (
                       SELECT 1
                       UNION ALL
                       SELECT n + 1 FROM cte WHERE n < 1000000 -- 생성하고 싶은 더미 데이터의 개수
                   )
SELECT
    CONCAT('User', LPAD(n, 7, '0')) AS name,  -- 'User' 다음에 7자리 숫자로 구성된 이름 생성
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
        END AS department,  -- 의미 있는 단어 조합으로 부서 이름 생성
    FLOOR(1 + RAND() * 100000) AS salary,    -- 1부터 100000 사이의 난수로 나이 생성
    TIMESTAMP(DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 3650) DAY) + INTERVAL FLOOR(RAND() * 86400) SECOND) AS created_at -- 최근 10년 내의 임의의 날짜와 시간 생성
FROM cte;

explain analyze
SELECT *
FROM users
WHERE salary = (SELECT MAX(salary) FROM users)
  AND department IN ('Sales', 'Marketing', 'IT');

# -> Filter: ((users.salary = (select #2)) and (users.department in ('Sales','Marketing','IT')))  (cost=100569 rows=29899) (actual time=207..404 rows=4 loops=1)
#     -> Table scan on users  (cost=100569 rows=996636) (actual time=0.155..205 rows=1e+6 loops=1)
#     -> Select #2 (subquery in condition; run only once)
#         -> Aggregate: max(users.salary)  (cost=200232 rows=1) (actual time=161..161 rows=1 loops=1)
#             -> Table scan on users  (cost=100569 rows=996636) (actual time=0.0706..131 rows=1e+6 loops=1)

create index idx_salary on users(salary);
alter table users
drop index idx_salary;

# users 테이블이 풀스캔 중인데, 중복이 적은 salary 에 인덱스를 걸면 데이터를 줄일 수 있겠다.
# -> Filter: (users.department in ('Sales','Marketing','IT'))  (cost=2.8 rows=3) (actual time=0.13..0.136 rows=4 loops=1)
#     -> Index lookup on users using idx_salary (salary=(select #2))  (cost=2.8 rows=10) (actual time=0.126..0.131 rows=10 loops=1)

create index idx_department on users(department);
alter table users
drop index idx_department;

# -> Filter: ((users.salary = (select #2)) and (users.department in ('Sales','Marketing','IT')))  (cost=100569 rows=59424) (actual time=219..414 rows=4 loops=1)
#     -> Table scan on users  (cost=100569 rows=996636) (actual time=0.146..204 rows=1e+6 loops=1)
#     -> Select #2 (subquery in condition; run only once)
#         -> Aggregate: max(users.salary)  (cost=200232 rows=1) (actual time=173..173 rows=1 loops=1)
#             -> Table scan on users  (cost=100569 rows=996636) (actual time=0.0473..141 rows=1e+6 loops=1)
