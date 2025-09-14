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

