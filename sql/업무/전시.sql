-- 1. 전시 매장 정보 조회
select * from venus_main.cc_site_base;
select * from venus_main.pr_dpml_base;
select * from venus_main.pr_disp_shop_base;
select * from venus_main.pr_tmpl_base;
select * from venus_main.pr_tmpl_mapp_info;
select * from venus_main.pr_tmpl_conr_mapp_info;
select * from venus_main.pr_conr_base;
select * from venus_main.pr_conr_set_info;
select * from venus_main.pr_conr_cont_info;
select * from venus_main.pr_conr_cont_info_ml;

-- union all
explain
-- 일반, 스와이퍼 코너는 세트와 컨텐츠가 있어야 노출
select pdsb.shop_no
     , ptmi.shop_tmpl_no
     , ptcmi.tmpl_conr_no
     , ptcmi.disp_seq as tmlp_conr_disp_seq
     , pcsi.disp_set_seq as tmpl_conr_set_seq
     , pcsi.disp_seq as tmpl_conr_set_disp_seq
     , pcci.conr_tgt_cd
     , pcci.disp_seq as corn_disp_seq
     , pcci.conr_cont_no
from venus_main.pr_disp_shop_base pdsb
         inner join lateral (
    select *
    from venus_main.pr_tmpl_mapp_info ptmii
    where ptmii.shop_no = pdsb.shop_no
      and ptmii.disp_yn = 'Y'
      and now() between ptmii.disp_str_dtm and ptmii.disp_end_dtm
    order by ptmii.sys_mod_dtm desc
        limit 1
    ) ptmi on pdsb.shop_no = ptmi.shop_no
    inner join venus_main.pr_tmpl_base ptb on ptb.tmpl_no = ptmi.tmpl_no
    inner join venus_main.pr_tmpl_conr_mapp_info ptcmi on ptmi.shop_tmpl_no = ptcmi.shop_tmpl_no
    inner join venus_main.pr_conr_base pcb on pcb.conr_no = ptcmi.conr_no and pcb.conr_typ_cd in ('10', '30')
    inner join venus_main.pr_conr_set_info pcsi on ptcmi.tmpl_conr_no = pcsi.tmpl_conr_no
    inner join venus_main.pr_conr_cont_info pcci on pcci.disp_set_seq = pcsi.disp_set_seq
where 1 = 1
-- 매장
  and pdsb.dpml_no = '10001' -- 사이트: Plateer, 몰: 통합몰 고정.
  and pdsb.shop_no = '4060' -- :shopNo
  and pdsb.disp_yn = 'Y'
-- 템플릿
  and ptb.tmpl_typ_cd = '10' -- DP004(템플릿유형코드): 10(일반)
  and ptb.use_yn = 'Y'
-- 템플릿코너매핑
  and ptcmi.disp_yn = 'Y'
  and now() between ptcmi.disp_str_dtm and ptcmi.disp_end_dtm
-- 코너
  and pcb.use_yn = 'Y'
-- 세트
  and pcsi.disp_yn = 'Y'
  and now() between pcsi.disp_str_dtm and pcsi.disp_end_dtm
-- 컨텐츠
  and pcci.disp_yn = 'Y'
  and now() between pcci.disp_str_dtm and pcci.disp_end_dtm
union all
-- 더미코너는 세트와 컨텐츠를 꽂을 수 없음.
select pdsb.shop_no
     , ptmi.shop_tmpl_no
     , ptcmi.tmpl_conr_no
     , ptcmi.disp_seq as tmlp_conr_disp_seq
     , null as tmpl_conr_set_seq
     , null as tmpl_conr_set_disp_seq
     , null as conr_tgt_cd
     , null as corn_disp_seq
     , null as conr_cont_no
from venus_main.pr_disp_shop_base pdsb
         inner join lateral (
    select *
    from venus_main.pr_tmpl_mapp_info ptmii
    where ptmii.shop_no = pdsb.shop_no
      and ptmii.disp_yn = 'Y'
      and now() between ptmii.disp_str_dtm and ptmii.disp_end_dtm
    order by ptmii.sys_mod_dtm desc
        limit 1
    ) ptmi on pdsb.shop_no = ptmi.shop_no
    inner join venus_main.pr_tmpl_base ptb on ptb.tmpl_no = ptmi.tmpl_no
    inner join venus_main.pr_tmpl_conr_mapp_info ptcmi on ptmi.shop_tmpl_no = ptcmi.shop_tmpl_no
    inner join venus_main.pr_conr_base pcb on pcb.conr_no = ptcmi.conr_no and pcb.conr_typ_cd = '20'
where 1 = 1
-- 매장
  and pdsb.dpml_no = '10001' -- 사이트: Plateer, 몰: 통합몰 고정.
  and pdsb.shop_no = '4060' -- :shopNo
  and pdsb.disp_yn = 'Y'
-- 템플릿
  and ptb.tmpl_typ_cd = '10' -- DP004(템플릿유형코드): 10(일반)
  and ptb.use_yn = 'Y'
-- 템플릿코너매핑
  and ptcmi.disp_yn = 'Y'
  and now() between ptcmi.disp_str_dtm and ptcmi.disp_end_dtm
-- 코너
  and pcb.use_yn = 'Y'
order by tmlp_conr_disp_seq, tmpl_conr_set_disp_seq, conr_tgt_cd, corn_disp_seq
;

-- 2. with
explain
with disp as (
    select pdsb.shop_no
         , ptmi.shop_tmpl_no
         , ptcmi.tmpl_conr_no
         , ptcmi.disp_seq as tmlp_conr_disp_seq
         , pcb.conr_typ_cd as conr_typ_cd
    from venus_main.pr_disp_shop_base pdsb
             inner join lateral (
        select *
        from venus_main.pr_tmpl_mapp_info ptmii
        where ptmii.shop_no = pdsb.shop_no
          and ptmii.disp_yn = 'Y'
          and now() between ptmii.disp_str_dtm and ptmii.disp_end_dtm
        order by ptmii.sys_mod_dtm desc
        limit 1
        ) ptmi on pdsb.shop_no = ptmi.shop_no
             inner join venus_main.pr_tmpl_base ptb on ptb.tmpl_no = ptmi.tmpl_no
             inner join venus_main.pr_tmpl_conr_mapp_info ptcmi on ptmi.shop_tmpl_no = ptcmi.shop_tmpl_no
             inner join venus_main.pr_conr_base pcb on pcb.conr_no = ptcmi.conr_no
    where 1 = 1
      -- 매장
      and pdsb.dpml_no = '10001' -- 사이트: Plateer, 몰: 통합몰 고정.
      and pdsb.shop_no = '4060' -- :shopNo
      and pdsb.disp_yn = 'Y'
      -- 템플릿
      and ptb.tmpl_typ_cd = '10' -- DP004(템플릿유형코드): 10(일반)
      and ptb.use_yn = 'Y'
      -- 템플릿코너매핑
      and ptcmi.disp_yn = 'Y'
      and now() between ptcmi.disp_str_dtm and ptcmi.disp_end_dtm
      -- 코너
      and pcb.use_yn = 'Y'
    )
select disp.shop_no
     , disp.shop_tmpl_no
     , disp.tmpl_conr_no
     , tmlp_conr_disp_seq
     , null as tmpl_conr_set_seq
     , null as tmpl_conr_set_disp_seq
     , null as conr_tgt_cd
     , null as corn_disp_seq
     , null as conr_cont_no
from disp
where disp.conr_typ_cd = '20'
union all
select disp.shop_no
     , disp.shop_tmpl_no
     , disp.tmpl_conr_no
     , tmlp_conr_disp_seq
     , pcsi.disp_set_seq as tmpl_conr_set_seq
     , pcsi.disp_seq as tmpl_conr_set_disp_seq
     , pcci.conr_tgt_cd
     , pcci.disp_seq as corn_disp_seq
     , pcci.conr_cont_no
from disp
         inner join venus_main.pr_conr_set_info pcsi on disp.tmpl_conr_no = pcsi.tmpl_conr_no
         inner join venus_main.pr_conr_cont_info pcci on pcci.disp_set_seq = pcsi.disp_set_seq
where disp.conr_typ_cd in ('10', '30')
  -- 세트
  and pcsi.disp_yn = 'Y'
  and now() between pcsi.disp_str_dtm and pcsi.disp_end_dtm
  -- 컨텐츠
  and pcci.disp_yn = 'Y'
  and now() between pcci.disp_str_dtm and pcci.disp_end_dtm
order by tmlp_conr_disp_seq, tmpl_conr_set_disp_seq, conr_tgt_cd, corn_disp_seq
;

-- 3. case when
explain
select pdsb.shop_no
     , ptmi.shop_tmpl_no
     , ptcmi.tmpl_conr_no
     , ptcmi.disp_seq as tmlp_conr_disp_seq
     , pcsi.disp_set_seq as tmpl_conr_set_seq
     , pcsi.disp_seq as tmpl_conr_set_disp_seq
     , pcci.conr_tgt_cd
     , pcci.disp_seq as corn_disp_seq
     , pcci.conr_cont_no
from venus_main.pr_disp_shop_base pdsb
         inner join lateral (
    select *
    from venus_main.pr_tmpl_mapp_info ptmii
    where ptmii.shop_no = pdsb.shop_no
      and ptmii.disp_yn = 'Y'
      and now() between ptmii.disp_str_dtm and ptmii.disp_end_dtm
    order by ptmii.sys_mod_dtm desc
        limit 1
      ) ptmi on pdsb.shop_no = ptmi.shop_no
    inner join venus_main.pr_tmpl_base ptb on ptb.tmpl_no = ptmi.tmpl_no
    inner join venus_main.pr_tmpl_conr_mapp_info ptcmi on ptmi.shop_tmpl_no = ptcmi.shop_tmpl_no
    inner join venus_main.pr_conr_base pcb on pcb.conr_no = ptcmi.conr_no
    left join venus_main.pr_conr_set_info pcsi on ptcmi.tmpl_conr_no = pcsi.tmpl_conr_no
    left join venus_main.pr_conr_cont_info pcci on pcci.disp_set_seq = pcsi.disp_set_seq
where 1 = 1
-- 매장
  and pdsb.dpml_no = '10001' -- 사이트: Plateer, 몰: 통합몰 고정.
  and pdsb.shop_no = '4060' -- :shopNo
  and pdsb.disp_yn = 'Y'
-- 템플릿
  and ptb.tmpl_typ_cd = '10' -- DP004(템플릿유형코드): 10(일반)
  and ptb.use_yn = 'Y'
-- 템플릿코너매핑
  and ptcmi.disp_yn = 'Y'
  and now() between ptcmi.disp_str_dtm and ptcmi.disp_end_dtm
-- 코너
  and pcb.use_yn = 'Y'
  and case
    when pcb.conr_typ_cd in ('10', '30')
    then (
-- 세트
    pcsi.disp_yn = 'Y'
  and now() between pcsi.disp_str_dtm and pcsi.disp_end_dtm
-- 컨텐츠
  and pcci.disp_yn = 'Y'
  and now() between pcci.disp_str_dtm and pcci.disp_end_dtm
    )
    when pcb.conr_typ_cd = '20'
    then true
    else false
end
order by tmlp_conr_disp_seq, tmpl_conr_set_disp_seq, conr_tgt_cd, corn_disp_seq
;

-- 기획전 상품 조회
-- 띵샵에서 기획전 상품 조회가 느려서 쿼리 확인이 필요하시다고 하신거면,
-- 띵샵에서는 기획전 하나에 많은 상품을 꽂아서 사용 중인데, 기획전 상품 조회 시 페이징을 하지 않기 때문으로 보입니다.
-- 우선 기획전 유형에 따라 구분자의 영역으로 바로 이동하는 로직과 무한 스크롤이 공존 할 수 있는지 여부를 확인해보고,
-- 가능하다면 최초 기획전 데이터 로딩 시 구분자별 상품 20개를 조회 한 후 해당 구분자의 상품이 더 있을 경우 조회하는 방식으로 개선하면 좋을 것 같습니다.
explain
select pmb.mkdp_no, pmdi.divobj_no, pdgsi.*
from venus_main.pr_mkdp_base pmb
         inner join venus_main.pr_mkdp_divobj_info pmdi on pmdi.mkdp_no = pmb.mkdp_no
         inner join venus_main.pr_mkdp_goods_info pmgi on pmgi.mkdp_no = pmb.mkdp_no and pmgi.divobj_no = pmdi.divobj_no
         inner join venus_main.pr_disp_goods_sumr_info pdgsi on pdgsi.goods_no = pmgi.goods_no
where 1=1
  and pmb.mkdp_no = :mkdpNo
  and now() between pmb.disp_str_dtm and pmb.disp_end_dtm
  and pmb.disp_yn = 'Y'
  and pmdi.disp_yn = 'Y'
  and pdgsi.disp_yn = 'Y'
  and pdgsi.sale_stat_cd = '10' -- PR005: 10(판매중)
  and now() between pdgsi.sale_str_dtm and pdgsi.sale_end_dtm
;

-- 인덱스 조회
SELECT
    t.relname as table_name,
    i.relname AS index_name,
    a.attname AS column_name,
    pos.n as index_column_order
FROM
    pg_class t
        JOIN pg_index ix ON t.oid = ix.indrelid
        JOIN pg_class i ON i.oid = ix.indexrelid
        JOIN LATERAL unnest(ix.indkey) WITH ORDINALITY AS pos(attnum, n) ON TRUE
    JOIN pg_attribute a ON a.attrelid = t.oid AND a.attnum = pos.attnum
WHERE
    t.relname in ('pr_mkdp_base', 'pr_mkdp_divobj_info', 'pr_mkdp_goods_info', 'pr_disp_goods_sumr_info')
ORDER BY
    t.relname, index_name, index_column_order
;

--- DTO
/**
매장
템플릿
코너 목록
    세트 목록
        세트정보
        컨텐츠목록
            컨텐츠정보
            컨텐츠대상목록
*/