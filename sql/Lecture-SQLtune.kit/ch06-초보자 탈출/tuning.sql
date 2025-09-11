show index from emp;

-- 6.1 기본키를 변형하는 나쁜 SQL
explain
select *
from emp
where 1=1
and substring(EMP_ID, 1, 4) = '1100'
and length(emp_id) = 5
;

-- 6.1 TO-BE
explain
select *
from EMP
where 1=1
and emp_id between '11000' and '11009'
;

/****
  기본키를 가공해 index 를 사용할 수 없음.
  기본키를 가공하지 않고 동일한 데이터를 가져오는 방법으로 우회.
 */

-- 6.2 불필요한 함수를 포함하는 나쁜 SQL
explain
select IFNUll(GENDER, 'NO DATA'), count(1) count
from EMP
group by ifnull(gender, 'NO DATA')
;

explain
select GENDER, count(1) count
from EMP
group by gender
;

/***
  not null 컬럼에서 null 체크 필요하지 않음.
  */

-- 6.3 인덱스를 활용하지 못하는 나쁜 SQL
explain
select count(1) count
from SALARY
where is_yn = 1
;
-- filtered 를 보면 10 으로 되어 있는데, 그러면 스토리지 엔진에서 값을 다 가져왔다는 말.
-- ddl 보고 해당 조건을 확인해보면 char(1) 인데 int 로 비교 중.

explain
select count(1) count
from SALARY
where is_yn = '1'
;

/**
  형이 다르면 스토리지 엔진에서 거르지 못하니 형을 맞춰주자
  ***/

-- 6.4 Full Table Scan 방식으로 수행하는 나쁜 SQL
explain
select FIRST_NAME, LAST_NAME
from EMP
where HIRE_DATE like '1994%'
;

show index from EMP;

explain
select FIRST_NAME, LAST_NAME
from EMP
where HIRE_DATE between '1994-01-01' and '1994-12-31'
;

/**
  인덱스가 걸려 있음에도 불필요하게 type all 로 타는 중
  */

-- 6.5 컬럼을 결합해서 사용하는 나쁜 SQL
explain
select *
from EMP
where concat(gender, ' ', LAST_NAME) = 'M Radwan'
;

explain
select *
from EMP
where gender = 'M'
  and LAST_NAME = 'Radwan'
;

-- 6.6 습관적으로 중복을 제거하는 나쁜 SQL
explain
select distinct e.EMP_ID, e.FIRST_NAME, e.LAST_NAME, s.ANNUAL_SALARY
from EMP e
join SALARY s on e.EMP_ID = s.EMP_ID
where s.IS_YN = '1'
;

-- extra 에 using temporary? 왜 임시테이블을 만들지? 아 distinct?
-- 근데 데이터 관점에서 보면,
-- 사원 한 명 당 현재 연봉의 정보는 한 개 밖에 없음.
-- 연봉이 바뀌면 기존거는 0 으로 꺽을거임.
-- 그러므로 굳이 메모리에 임시테이블을 올려서 중복 제거 distinct 를 할 필요가 없음.
-- 습관적으로 붙이 필요 없는거지 꼭 필요하다면 붙여!

explain
select e.EMP_ID, e.FIRST_NAME, e.LAST_NAME, s.ANNUAL_SALARY
from EMP e
         join SALARY s on e.EMP_ID = s.EMP_ID
where s.IS_YN = '1'
;

-- 6.7 UNION 문으로 쿼리를 합치는 나쁜 SQL
explain
select 'M' as gender, EMP_ID
from EMP
where gender  = 'M'
and LAST_NAME = 'Baba'

union

select 'W' as gender, EMP_ID
from EMP
where gender  = 'W'
  and LAST_NAME = 'Baba'
;

explain
select 'M' as gender, EMP_ID
from EMP
where gender  = 'M'
  and LAST_NAME = 'Baba'

union all

select 'W' as gender, EMP_ID
from EMP
where gender  = 'W'
  and LAST_NAME = 'Baba'
;

/**
  union 은 정렬 후 중복 제거까지 들어가기 때문에 임시 테이블에 올려서 처리.
  데이터 상 봤을때 중복이 없을 데이터니 union all 으로 변경
  -- 심화에 다른 튜닝 방식 있음.
  */

-- 6.8 인덱스를 생각하지 않고 작성한 나쁜 SQL
explain
select LAST_NAME, GENDER, count(1) count
from EMP
group by LAST_NAME, GENDER
;

-- 임시 테이블을 만드는데 굳이 왜 만들지?
-- 인덱스 순서로 group by 한게 아니라 그렇구나

explain
select LAST_NAME, GENDER, count(1) count
from EMP
group by GENDER, LAST_NAME
;

/*
group by 나 order by 할 때 복합 인덱스 컬럼이라면 순서를 잘 지정해주자.
***/

-- 6.9 엉뚱한 인덱스를 사용하는 SQL
explain
select EMP_ID
from EMP
where HIRE_DATE like '1989%'
and EMP_ID > 100000
;

select count(1) from emp; -- 300,024
select count(1) from emp where HIRE_DATE like '1989%'; -- 28,394 -> 10%
select count(1) from emp where EMP_ID > 100000; -- 210,024 -> 70%

-- 범위를 줄일 때 많이 줄이는게 좋음. 그래서 HIRE_DATE index 타는게 좋은데,
-- mysql 에서 like 로 하면 range 를 못 타기 때문에, 아래 처럼 바꾸면 range 를 탈 수 있어서,
-- hire_date index 를 타게 됨.

explain
select EMP_ID
from EMP
where HIRE_DATE between '1989-01-01' and '1989-12-31'
  and EMP_ID > 100000
;

/*
데이터의 비율을 판단 한 다음에 어떤 인덱스를 타는게 더 성능상 좋은지 판단.
*/


-- 6.10 잘못된 드라이빙 테이블로 수행 되는 나쁜 SQL
explain
select de.EMP_ID, d.DEPT_ID
from DEPT_EMP_MAPPING de,
     dept d
where de.DEPT_ID = d.DEPT_ID
  and de.START_DATE >= '2002-03-01'
;

-- 실행 계획
-- 위에 나오는게 드라이빙 테이블
-- 아래 나오는게 드리븐 테이블

select count(1) from DEPT_EMP_MAPPING; -- 331,603 건
select count(1) from DEPT_EMP_MAPPING  where START_DATE >= '2002-03-01'; -- 1,341건
select count(1) from DEPT; -- 9건

-- 데이터 적은걸 드라이빙 테이블에 넣는게 좋다고 했는데,
-- Nested Loop Join 을 생각해보면
-- 9번 테이블에 점근해서 331,603 건 데이터 뒤지기 vs 1,341 건 테이블 접근해서 9건 데이터 뒤지기
-- 후자가 훨씬 나아서 드라이빙 테이블을 DEPT_EMP_MAPPING 으로, 드리븐 테이블을 DEPT 으로 처리하는게 더 성능상 빠름.
-- 그래서, 조건절에 의해 액세스 대상 행 수를 가장 크게 줄일 수 있는 테이블이 있다면, 해당 테이블을 드라이빙 테이블로 선택하자.

explain
select straight_join de.EMP_ID, d.DEPT_ID
from DEPT_EMP_MAPPING de,
     dept d
where de.DEPT_ID = d.DEPT_ID
  and de.START_DATE >= '2002-03-01'
;
