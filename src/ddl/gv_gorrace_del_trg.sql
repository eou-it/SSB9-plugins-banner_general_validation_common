-- *****************************************************************************
-- *                                                                           *
-- *  Copyright 2013 Ellucian Company L.P. and its affiliates.                 *
-- *                                                                           *
-- *****************************************************************************
REM
REM gv_gorrace_del_trg.sql
REM
REM AUDIT TRAIL: 9.0
REM 1. Banner XE
REM Generated trigger for Banner XE API support
REM AUDIT TRAIL END
REM
CREATE OR REPLACE TRIGGER gorrace_view_delete_trg
  INSTEAD OF DELETE ON gv_gorrace
BEGIN
  gb_race_ethnicity.p_delete
    (p_race_cde => :OLD.gorrace_race_cde,
     p_rowid => :OLD.gorrace_v_rowid);
END;
/
