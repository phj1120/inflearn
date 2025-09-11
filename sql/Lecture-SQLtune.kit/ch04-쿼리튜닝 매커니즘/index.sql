-- full scan
explain
select * from emp
where gender is not null
;

-- index scan
explain
select * from emp
where EMP_ID between 10000 and 200000
;

-- index full scan
explain
select gender, LAST_NAME from emp
where gender <> 'F'
;

-- index unique scan
explain
select * from emp
where EMP_ID = 10001
;

-- index loose scan
explain
select GENDER, count(distinct LAST_NAME) cnt
from emp
where GENDER = 'F'
group by GENDER
;

-- index skip scan
explain
select max(EMP_ID) max_emp_id
from emp
where LAST_NAME = 'Peha'
;

-- index merge scan
explain
select * from emp
where
    (hire_date between '2000-01-01' and '2000-12-31')
    or EMP_ID > 600000
;

-- access condition vs filter condition
explain
select *
from emp
where EMP_ID between 10000 and 200000
and gender = 'F'
and LAST_NAME in ('Peha', 'Kim')
and HIRE_DATE >= '1990-01-01'
;
-- 왜 index merge scan 이 아닌 index range scan 일까?
-- -> 인덱스를 합치는 비용이 범위로 가져오는 것 보다 더 비싸다고 판단 되면 범위로 가져옴.
