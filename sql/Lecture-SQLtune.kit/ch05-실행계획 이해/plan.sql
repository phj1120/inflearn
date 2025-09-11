explain
select *
from DEPT_EMP_MAPPING de
where 1=1
#     and de.END_DATE < '9999-12-31'
    and de.emp_id = 10001
    and de.DEPT_ID = 'd005'
;

explain analyze
select *
from EMP
where 1=1
    and EMP_ID between 10001 and 100000
    and HIRE_DATE = '1985-11-21'
    and birth = '1985-11-21'
;

explain
select *
from GRADE
where GRADE_NAME = 'Manager'
;