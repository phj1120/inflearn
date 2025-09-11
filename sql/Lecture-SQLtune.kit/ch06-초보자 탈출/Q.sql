-- 6.1 기본키를 변형하는 나쁜 SQL
-- AS-IS
explain
select *
from emp
where 1=1
    and substring(EMP_ID, 1, 4) = '1100'
    and length(emp_id) = 5
;

-- TO-BE

;

-- 6.2 불필요한 함수를 포함하는 나쁜 SQL
-- AS-IS
explain
select IFNUll(GENDER, 'NO DATA'), count(1) count
from EMP
group by ifnull(gender, 'NO DATA')
;

-- TO-BE

;

-- 6.3 인덱스를 활용하지 못하는 나쁜 SQL
-- AS-IS
explain
select count(1) count
from SALARY
where is_yn = 1
;

-- TO-BE

;

-- 6.4 Full Table Scan 방식으로 수행하는 나쁜 SQL
-- AS-IS
explain
select FIRST_NAME, LAST_NAME
from EMP
where HIRE_DATE like '1994%'
;

-- TO-BE

;

-- 6.5 컬럼을 결합해서 사용하는 나쁜 SQL
-- AS-IS
explain
select *
from EMP
where concat(gender, ' ', LAST_NAME) = 'M Radwan'
;

-- TO-BE

;

-- 6.6 습관적으로 중복을 제거하는 나쁜 SQL
-- AS-IS
explain
select distinct e.EMP_ID, e.FIRST_NAME, e.LAST_NAME, s.ANNUAL_SALARY
from EMP e
    join SALARY s on e.EMP_ID = s.EMP_ID
where s.IS_YN = '1'
;

-- TO-BE

;

-- 6.7 UNION 문으로 쿼리를 합치는 나쁜 SQL
-- AS-IS
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

-- TO-BE

;

-- 6.8 인덱스를 생각하지 않고 작성한 나쁜 SQL
-- AS-IS
explain
select LAST_NAME, GENDER, count(1) count
from EMP
group by LAST_NAME, GENDER
;

-- TO-BE

;

-- 6.9 엉뚱한 인덱스를 사용하는 SQL
-- AS-IS
explain
select EMP_ID
from EMP
where HIRE_DATE like '1989%'
    and EMP_ID > 100000
;

-- TO-BE

;

-- 6.10 잘못된 드라이빙 테이블로 수행 되는 나쁜 SQL
-- AS-IS
explain
select de.EMP_ID, d.DEPT_ID
from DEPT_EMP_MAPPING de,
     dept d
where de.DEPT_ID = d.DEPT_ID
  and de.START_DATE >= '2002-03-01'
;

-- TO-BE

;