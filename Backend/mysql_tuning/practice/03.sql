# 부서별 최대 연봉을 가진 사용자들 조회하는 SQL문 튜닝하기

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
       FLOOR(1 + RAND() * 100000)                                                                            AS salary,     -- 1부터 100000 사이의 난수로 나이 생성
       TIMESTAMP(DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 3650) DAY) +
                 INTERVAL FLOOR(RAND() * 86400) SECOND)                                                      AS created_at  -- 최근 10년 내의 임의의 날짜와 시간 생성
FROM cte;

explain analyze
SELECT u.id, u.name, u.department, u.salary, u.created_at
FROM users u
         JOIN (SELECT department, MAX(salary) AS max_salary
               FROM users
               GROUP BY department) d
             ON u.department = d.department AND u.salary = d.max_salary;

# -> Nested loop inner join  (cost=2.59e+6 rows=0) (actual time=428..1014 rows=19 loops=1)
#     -> Filter: ((u.department is not null) and (u.salary is not null))  (cost=100569 rows=996636) (actual time=0.366..250 rows=1e+6 loops=1)
#         -> Table scan on u  (cost=100569 rows=996636) (actual time=0.365..206 rows=1e+6 loops=1)
#     -> Covering index lookup on d using <auto_key0> (department=u.department, max_salary=u.salary)  (cost=0.25..2.5 rows=10) (actual time=708e-6..708e-6 rows=19e-6 loops=1e+6)
#         -> Materialize  (cost=0..0 rows=0) (actual time=416..416 rows=10 loops=1)
#             -> Table scan on <temporary>  (actual time=416..416 rows=10 loops=1)
#                 -> Aggregate using temporary table  (actual time=416..416 rows=10 loops=1)
#                     -> Table scan on users  (cost=100569 rows=996636) (actual time=0.0586..174 rows=1e+6 loops=1)

create index idx_department_salary on users (department, salary);
# -> Nested loop inner join  (cost=9.53 rows=16.9) (actual time=0.252..0.463 rows=19 loops=1)
#     -> Filter: ((d.department is not null) and (d.max_salary is not null))  (cost=14.3..3.62 rows=10) (actual time=0.217..0.22 rows=10 loops=1)
#         -> Table scan on d  (cost=15.8..18.1 rows=10) (actual time=0.216..0.218 rows=10 loops=1)
#             -> Materialize  (cost=15.5..15.5 rows=10) (actual time=0.215..0.215 rows=10 loops=1)
#                 -> Covering index skip scan for grouping on users using idx_department_salary  (cost=14.5 rows=10) (actual time=0.06..0.19 rows=10 loops=1)
#     -> Index lookup on u using idx_department_salary (department=d.department, salary=d.max_salary)  (cost=0.439 rows=1.69) (actual time=0.0227..0.0239 rows=1.9 loops=10)
