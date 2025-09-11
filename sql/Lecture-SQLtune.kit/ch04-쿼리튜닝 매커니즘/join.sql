use tuning;

-- Nested Loop Join
explain
select emp.EMP_ID, emp.FIRST_NAME, emp.LAST_NAME, GRADE.GRADE_NAME
from emp, grade
where 1=1
and emp.LAST_NAME = 'Suri'
and grade.EMP_ID = emp.EMP_ID
;

-- Hash Join
explain
select emp.EMP_ID, emp.FIRST_NAME, emp.LAST_NAME, GRADE.GRADE_NAME
from emp, grade
where 1=1
#     and grade.EMP_ID = emp.EMP_ID
;

select * from mysql.innodb_table_stats
where database_name = 'tuning'
;

analyze table emp;