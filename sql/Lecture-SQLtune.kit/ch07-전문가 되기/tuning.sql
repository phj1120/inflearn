-- 7.1 불필요한 OO 을 수행하는 나쁜 SQL
explain
select count(distinct e.emp_id) as count
from emp e,
    (select emp_id from entry_record where gate = 'A') record
where e.emp_id = record.emp_id
;