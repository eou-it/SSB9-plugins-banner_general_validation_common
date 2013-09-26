-- /** *****************************************************************************
--  Copyright 2009-2013 Ellucian Company L.P. and its affiliates.
--  ****************************************************************************** */
REM
REM banner_upgrade.sql
REM
REM AUDIT TRAIL: 9.0
REM 1. Horizon
REM Main common project schema maintenance script.
REM AUDIT TRAIL END
REM
set scan on echo on termout on;
REM
REM spool horizon_upgrade.lis
REM
connect dbeu_owner/&&dbeu_password
REM
start dbeu_ext_gen_bgvc
start dbeu_ext_stu_bgvc
REM
connect baninst1/&&baninst1_password
start genview_bgvc
REM
connect baninst1/&&baninst1_password
start gendbpr_bgvc
REM
REM Create baseline seed data for GURINFO
connect baninst1/&&baninst1_password
start ggtvgurinfoi.sql
REM
REM Recompile invalid objects before doing seed data
REM
conn sys/u_pick_it as sysdba
execute utl_recomp.recomp_parallel();
start showinv
spool off;
