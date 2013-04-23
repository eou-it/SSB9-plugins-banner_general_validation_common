-- *****************************************************************************
-- *                                                                           *
-- * Copyright 2013 Ellucian Company L.P. and its affiliates.                  *
-- *                                                                           *
-- *****************************************************************************
REM
REM gendbpr_bgvc.sql
REM
REM AUDIT TRAIL: 9.0
REM 1. Horizon
REM Create views.
REM AUDIT TRAIL END
REM
whenever oserror exit rollback;
whenever sqlerror exit rollback;
REM connect baninst1/&&baninst1_password
REM
start gv_gorrace_del_trg
start gv_gorrace_ins_trg
start gv_gorrace_upd_trg
