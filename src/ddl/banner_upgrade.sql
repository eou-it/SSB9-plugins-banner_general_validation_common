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
spool off;