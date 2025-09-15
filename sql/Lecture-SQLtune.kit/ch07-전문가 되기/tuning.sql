-- 7.1 불필요한 조인을 수행하는 나쁜 SQL
explain
select count(distinct e.emp_id) as count
from emp e,
    (select emp_id from entry_record where gate = 'A') record
where e.emp_id = record.emp_id
;

explain
select count(e.emp_id) as count
from emp e
where exists(select * from ENTRY_RECORD record where GATE = 'A' and e.EMP_ID = record.EMP_ID)
;

-- 7.2 HAVING 절로 추가적 필터를 수행하는 나쁜 SQL
explain
select e.EMP_ID, e.FIRST_NAME, e.LAST_NAME
from EMP e
    inner join SALARY s on e.EMP_ID = s.EMP_ID
where e.EMP_ID > 450000
group by s.EMP_ID
having max(s.ANNUAL_SALARY) > 100000
;

-- having 은 결과가 도출 된 다음에 가장 마지막에 추가적으로 mysql엔진에서 filter 하기 때문에,
-- 스토리지 엔진 부터 안가져오게끔 할 수 있으면 그렇게 작성하기.

explain
select e.EMP_ID, e.FIRST_NAME, e.LAST_NAME
from emp e
where e.EMP_ID > 450000
and exists(select * from SALARY s where s.EMP_ID = e.EMP_ID and s.ANNUAL_SALARY > 100000)
# and (select max(s.ANNUAL_SALARY) from SALARY s where s.EMP_ID = e.EMP_ID) > 100000
;

-- 7.3 유사한 SELECT 문을 여러개 나열한 나쁜 SQL
explain
select 'BOSS' as grade_name, count(1) cnt
from GRADE
where GRADE_name = 'Manager' and END_DATE = '9999-01-01'
union all
select 'TL' as grade_name, count(1) cnt
from GRADE
where GRADE_name = 'Technique Leader' and END_DATE = '9999-01-01'
union all
select 'AE' as grade_name, count(1) cnt
from GRADE
where GRADE_name = 'Assistant Engineer' and END_DATE = '9999-01-01'
;

explain
select
    case
        when GRADE_NAME = 'Manager' then 'BOSS'
        when GRADE_NAME = 'Technique Leader' then 'TL'
        when GRADE_NAME = 'Assistant Engineer' then 'AE'
        else 'NONE'
    end as grade_name
    , count(1) cnt
from GRADE
where GRADE_name in ('Manager', 'Technique Leader', 'Assistant Engineer')
  and END_DATE = '9999-01-01'
group by grade_name
;


-- 7.4 소계/통계를 위한 쿼리를 반복 하는 나쁜 SQL
explain
select REGION, null gate, count(1) cnt
from ENTRY_RECORD
where REGION <> ''
group by REGION
union all
select REGION, gate, count(1) cnt
from ENTRY_RECORD
where REGION <> ''
group by REGION, GATE
union all
select null REGION, null gate, count(1) cnt
from ENTRY_RECORD
where REGION <> ''
;

-- 이외에도 SQL 에서 제공하는 여러 기능이 있으니 사용하자.
select REGION, gate, count(1) cnt
from ENTRY_RECORD
where REGION <> ''
group by REGION, gate with rollup
;

-- 7.5 처음부터 모든 데이터를 가져오는 나쁜 SQL
analyze
select e.EMP_ID, avg_salary, max_salary, min_salary
from EMP e
    inner join (
        select EMP_ID
             , round(avg(ANNUAL_SALARY)) avg_salary
             , round(max(ANNUAL_SALARY)) max_salary
             , round(min(ANNUAL_SALARY)) min_salary
        from SALARY s
        group by EMP_ID
    ) s on s.EMP_ID = e.EMP_ID
where e.EMP_ID between 10001 and 10100
;

-- 튜닝 과정 1
analyze
select e.EMP_ID, avg_salary, max_salary, min_salary
from EMP e
    inner join (
        select EMP_ID
             , round(avg(ANNUAL_SALARY)) avg_salary
             , round(max(ANNUAL_SALARY)) max_salary
             , round(min(ANNUAL_SALARY)) min_salary
        from SALARY s
        where s.EMP_ID between 10001 and 10100
        group by EMP_ID
    ) s on s.EMP_ID = e.EMP_ID
;

-- inline view 에서 대상을 줄여서 가져오려 했는데
-- salary 에 emp_id 가 index 가 아니라 all 로 전체 스캔해서 다른 방식 고민.
show index from SALARY;
select count(1) from SALARY;

-- 내가 튜닝한 최종 쿼리
analyze
select e.EMP_ID
    , round(avg(ANNUAL_SALARY)) avg_salary
    , round(max(ANNUAL_SALARY)) max_salary
    , round(min(ANNUAL_SALARY)) min_salary
from EMP e
    inner join SALARY s on s.EMP_ID = e.EMP_ID
where e.EMP_ID between 10001 and 10100
group by e.EMP_ID
;

-- 강사님 튜닝 쿼리
analyze
select e.EMP_ID
    , (select round(avg(ANNUAL_SALARY)) from SALARY s1 where e.EMP_ID = s1.EMP_ID) as avg_salary
    , (select round(max(ANNUAL_SALARY)) from SALARY s1 where e.EMP_ID = s1.EMP_ID) as max_salary
    , (select round(min(ANNUAL_SALARY)) from SALARY s1 where e.EMP_ID = s1.EMP_ID) as min_salary
from EMP e
where e.EMP_ID between 10001 and 10100
;
-- 조인은 병합 작업을 한 번에 처리하고 인덱스를 활용하는 반면에,
-- 서브쿼리는 내부적으로 반복 실행되기 때문에
-- 대용량 데이터일수록 서브쿼리 방식이 비효율적일 가능성이 높다.

-- 7.6 비효율적인 페이징을 수행하는 나쁜 SQL
explain
select e.EMP_ID, e.FIRST_NAME, e.LAST_NAME, e.HIRE_DATE
from emp e
    inner join SALARY s on e.EMP_ID = s.EMP_ID
where e.EMP_ID between 10001 and 50000
group by e.EMP_ID
order by sum(s.ANNUAL_SALARY) desc
limit 150, 10
;

-- 30만 -> 4만 (전체 -> emp_id between 걸 경우)
select count(1) from EMP e
where e.EMP_ID between 10001 and 50000
;

-- 280만 -> 38만 (전체 -> emp_id between 걸 경우)
select count(1) from SALARY s
where s.EMP_ID between 10001 and 50000
;

explain analyze
select e.EMP_ID, e.FIRST_NAME, e.LAST_NAME, e.HIRE_DATE
from emp e --
    inner join (
        select si.EMP_ID, sum(si.ANNUAL_SALARY) as annual_salary_sum
        from SALARY si
        where si.EMP_ID between 10001 and 50000
        group by si.EMP_ID
        order by sum(si.ANNUAL_SALARY) desc
        limit 150, 10
    ) s on e.EMP_ID = s.EMP_ID
order by annual_salary_sum desc
;

-- 1
explain analyze
select *
from emp e
    inner join SALARY s on e.EMP_ID = s.EMP_ID
where e.EMP_ID between 10001 and 50000
;

# -> Nested loop inner join  (cost=136395 rows=753646) (actual time=0.0376..235 rows=379595 loops=1)
#     -> Filter: (e.EMP_ID between 10001 and 50000)  (cost=15951 rows=79652) (actual time=0.024..24.1 rows=40000 loops=1)
#         -> Index range scan on e using PRIMARY over (10001 <= EMP_ID <= 50000)  (cost=15951 rows=79652) (actual time=0.0226..21.1 rows=40000 loops=1)
#     -> Index lookup on s using PRIMARY (EMP_ID=e.EMP_ID)  (cost=0.566 rows=9.46) (actual time=0.0033..0.00467 rows=9.49 loops=40000)

-- 2
explain analyze
select *
from emp e
     inner join (
         select *
         from SALARY si
        where si.EMP_ID between 10001 and 50000
        ) s on e.EMP_ID = s.EMP_ID
where e.EMP_ID between 10001 and 50000
;
# -> Nested loop inner join  (cost=136395 rows=753646) (actual time=0.0374..222 rows=379595 loops=1)
#     -> Filter: ((e.EMP_ID between 10001 and 50000) and (e.EMP_ID between 10001 and 50000))  (cost=15951 rows=79652) (actual time=0.0243..26.6 rows=40000 loops=1)
#         -> Index range scan on e using PRIMARY over (10001 <= EMP_ID <= 50000)  (cost=15951 rows=79652) (actual time=0.0226..22.5 rows=40000 loops=1)
#     -> Index lookup on si using PRIMARY (EMP_ID=e.EMP_ID)  (cost=0.566 rows=9.46) (actual time=0.00291..0.00426 rows=9.49 loops=40000)

-- inline view 를 이용해도 인덱스를 이용할 수 있고, 옵티마이저가 알아서 처리를 해주기 떄문에 위의 쿼리는 차이가 나지 않음
-- limit 으로 모수를 줄 일 수 있을때 의미가 있다해서 아래 테스트

-- 3
explain analyze
select *
from emp e
    inner join SALARY s on e.EMP_ID = s.EMP_ID
where e.EMP_ID between 10001 and 50000
limit 100
;

# -> Limit: 100 row(s)  (cost=136395 rows=100) (actual time=0.037..0.135 rows=100 loops=1)
#     -> Nested loop inner join  (cost=136395 rows=753646) (actual time=0.0365..0.128 rows=100 loops=1)
#         -> Filter: (e.EMP_ID between 10001 and 50000)  (cost=15951 rows=79652) (actual time=0.0235..0.0342 rows=9 loops=1)
#             -> Index range scan on e using PRIMARY over (10001 <= EMP_ID <= 50000)  (cost=15951 rows=79652) (actual time=0.0221..0.032 rows=9 loops=1)
#         -> Index lookup on s using PRIMARY (EMP_ID=e.EMP_ID)  (cost=0.566 rows=9.46) (actual time=0.00593..0.00941 rows=11.1 loops=9)

-- 4
explain analyze
select *
from emp e
    inner join (
        select *
        from SALARY si
        where si.EMP_ID between 10001 and 50000
        limit 100
    ) s on e.EMP_ID = s.EMP_ID
where e.EMP_ID between 10001 and 50000
;

# -> Nested loop inner join  (cost=49.9 rows=100) (actual time=0.62..0.764 rows=100 loops=1)
#     -> Filter: (s.EMP_ID between 10001 and 50000)  (cost=154737..13.8 rows=100) (actual time=0.577..0.598 rows=100 loops=1)
#         -> Table scan on s  (cost=156300..156304 rows=100) (actual time=0.576..0.588 rows=100 loops=1)
#             -> Materialize  (cost=156300..156300 rows=100) (actual time=0.575..0.575 rows=100 loops=1)
#                 -> Limit: 100 row(s)  (cost=156290 rows=100) (actual time=0.0234..0.452 rows=100 loops=1)
#                     -> Filter: (si.EMP_ID between 10001 and 50000)  (cost=156290 rows=779148) (actual time=0.0232..0.445 rows=100 loops=1)
#                         -> Index range scan on si using PRIMARY over (10001 <= EMP_ID <= 50000)  (cost=156290 rows=779148) (actual time=0.0216..0.432 rows=100 loops=1)
#     -> Single-row index lookup on e using PRIMARY (EMP_ID=s.EMP_ID)  (cost=0.263 rows=1) (actual time=0.00144..0.00148 rows=1 loops=100)

-- 실행 계획은 둘이 달라졌지만, 시간은 비슷함 오히려 밖에서 limit 한게 더 빠름.
-- 임시 테이블을 만드는 작업이 있어서 그런건가?

# 3번은 단순 범위 조건 + 조인 키 인덱스가 잘 잡혀 있어서,
# EMP 인덱스 범위 스캔 후 SALARY 단건 인덱스 룩업을 반복하는 Nested Loop로
# 바로 100건을 채우며 조기 종료가 가능하니 매우 빠르다.
# 반면 4번은 서브쿼리 결과를 Materialize하고 다시 스캔하는 오버헤드가 있어,
# 대상이 줄더라도 이득이 상쇄되어 체감 차이가 거의 없거나 오히려 느릴 수 있다.

-- 7.7 불필요한 정보를 가져오는 나쁜 SQL
explain
select count(EMP_ID) as count
from (select e.EMP_ID, m.DEPT_ID
      from (
          select EMP_ID
          from EMP
          where gender = 'M'
          and EMP_ID > 300000
           ) e
      left join MANAGER m on e.EMP_ID = m.EMP_ID
      ) sub
;

explain
select count(EMP_ID)
from EMP
where gender = 'M'
    and EMP_ID > 300000
;

-- 7.8 비효율적인 조인을 수행하는 나쁜 SQL
explain
select distinct de.dept_id
from MANAGER m -- 24
    inner join DEPT_EMP_MAPPING de on m.DEPT_ID = de.dept_id -- 33만건
order by de.DEPT_ID
;

-- 튜닝1
explain
select distinct DEPT_ID
from DEPT_EMP_MAPPING de
where exists(select 1 from MANAGER m where m.DEPT_ID = de.DEPT_ID)
# order by de.DEPT_ID -- DEPT_ID 인덱스를 사용했고, 이는 이미 정렬 되어 있으니, 정렬 안해도 된다네...
;

-- 튜닝2
explain
select de.dept_id
from (
        select distinct DEPT_ID
        from dept_emp_mapping de
    ) de
where exists(select 1 from MANAGER m where m.DEPT_ID = de.DEPT_ID)
;
-- 조인 대상의 타겟을 줄여주는...


-- 7.9 인덱스 없이 데이터를 조회하는 나쁜 SQL
explain
select *
from EMP
where FIRST_NAME = 'Georgi'
and LAST_NAME = 'Wielonsky'
;

show index from EMP;

-- first_name, last_name 에 대한 index 가 없기 때문에, table full scan 되기 때문에,
-- 인덱스 순서를 바꾸거나, 인덱스를 추가!
-- 인덱스 변경은 다른 쿼리 성능에 영향을 줄 수 있어서 조심스럽게 작업 필요함.

select count(distinct FIRST_NAME) -- 1275
     , count(distinct LAST_NAME) -- 1637
from EMP
;

-- last_name 의 카디널리티가 더 높기 때문에 last_name 을 앞에 생성
-- (값이 다양할수록 구분이 쉬우니...)

-- 인덱스 생성
create index idx_emp_name on EMP(LAST_NAME, FIRST_NAME);

-- 인덱스 제거
alter table EMP drop index idx_emp_name;

-- select 는 비약적으로 좋아지지만
-- update, delete, insert 가 자주 일어난다면, index 추가시 성능 이슈 발생할 수 있음.

-- 7.10 인덱스를 사용하지 않는 나쁜 SQL
explain
select EMP_ID, FIRST_NAME, LAST_NAME, GENDER
from EMP
where FIRST_NAME = 'Matt' -- 233
    or HIRE_DATE = '1987-03-31' -- 111
;

show index from EMP;
-- hire_date
-- gender, last_name

-- or 절이라 두 개 다 인덱스가 있어야 의미가 있음.
-- 단순 이 쿼리문의 효과를 늘리려면 first_name 인덱스를 추가하면 되나,
-- 현업에서는 다른 쿼리에 영향을 줄 수 있으니, 충분히 분석 후 추가.

create index idx_emp_first_name on EMP(FIRST_NAME);

alter table EMP drop index idx_emp_first_name;

-- 7.11 인덱스에 나쁜 영향을 주는 DML
explain
update ENTRY_RECORD
set GATE = 'X'
where gate = 'B'
;

select count(1) from ENTRY_RECORD; -- 60만
select count(1) from ENTRY_RECORD where gate = 'B'; -- 30만

show index from ENTRY_RECORD;

-- 인덱스가 있는 테이블의 경우 많은 데이터를 cud 하면 성능상 이슈 발생 가능
-- 이럴때 사용자가 적은 시간대에 인덱스를 지우고, cud 실행 후 인덱스 재생성.

alter table ENTRY_RECORD drop index I_REGION;
alter table ENTRY_RECORD drop index I_ENTRY_TIME;
alter table ENTRY_RECORD drop index I_GATE;

alter table ENTRY_RECORD add index I_REGION(REGION);
alter table ENTRY_RECORD add index I_ENTRY_TIME(ENTRY_TIME);
alter table ENTRY_RECORD add index I_GATE(GATE);

-- 현재 auto commit 상태: 0(off), 1(on)
select @@autocommit;
-- 현재 세션내에서의 설정
set @@autocommit = 0;

rollback;

-- 인덱스 O: 3s 118ms
-- 인덱스 X: 1 s 550 ms

-- 7.12 비효율적인 인덱스를 사용하는 나쁜 SQL
explain
select EMP_ID, FIRST_NAME, LAST_NAME
from EMP
where GENDER = 'M'
and LAST_NAME = 'Baba'
;

select count(1) from EMP where GENDER = 'M'; -- 18 만건
select count(1) from EMP where LAST_NAME = 'Baba'; -- 226 건

show index from EMP;
-- gender, last_name <- last name 이 더 다양하니 last name, gender 로 걸리는게 더 빠릅
-- 카디널리티(컬럼이 유니크한 정도) 차이가 명확하다면, 미리 바꾸는게 추후에도 더 좋음.(부정정인 영향 사전에 방지)

alter table EMP drop index I_GENDER_LAST_NAME;

alter table EMP add index I_GENDER_LAST_NAME(GENDER, LAST_NAME);

alter table EMP drop index I_LAST_NAME_GENDER;

alter table EMP add index I_LAST_NAME_GENDER(LAST_NAME, GENDER);

-- gender, last_name: 473 ms

-- last_name, gender: 468 ms

-- 7.13 대소문자가 섞인 데이터와 비교하는 나쁜 SQL
show index from EMP;

explain
select FIRST_NAME, LAST_NAME, GENDER, BIRTH
from EMP -- 30 만건
where LOWER(FIRST_NAME) = 'mary' -- 224 <- 이걸 인덱스로 태우면 좋을듯!
and HIRE_DATE >= str_to_date('1990-01-01', '%Y-%m-%d') -- 13 만건
;

-- first_name 의 Collation(조합)을 대소문자를 구분하지 않는 값으로 바꾸면, 인덱스 필드를 함수로 조작하지 않고 처리 가능!
-- 현업에서 정책 상 바꾸기 힘들다면 이렇게 특수한 경우를 위한 컬럼을 추가 + 인덱스 생성 ex. lower_first_name

-- 7.14 파티션 없이 대량 데이터를 사용하는 나쁜 SQL
explain
select count(1)
from SALARY -- 284만건
where START_DATE between str_to_date('2000-01-01', '%Y-%m-%d') and str_to_date('2000-12-31', '%Y-%m-%d') -- 25만건
;

-- salary 테이블의 레코드가 계속 축적되고 있다
-- 연도별로 salary 의 레코드를 조회하는 횟수가 잦다
-- -> 연도별로 파티션을 만들어서 조회!



