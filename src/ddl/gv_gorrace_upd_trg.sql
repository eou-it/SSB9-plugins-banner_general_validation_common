-- *****************************************************************************
-- *                                                                           *
-- *  Copyright 2013 Ellucian Company L.P. and its affiliates.                 *
-- *                                                                           *
-- *****************************************************************************
REM
REM gv_gorrace_upd_trg.sql
REM
REM AUDIT TRAIL: 9.0
REM 1. Banner XE
REM Generated view for Banner XE API support
REM AUDIT TRAIL END
REM
CREATE OR REPLACE TRIGGER gorrace_view_update_trg
  INSTEAD OF UPDATE ON gv_gorrace
BEGIN
  gfksjpa.setId(:OLD.gorrace_surrogate_id);
  gfksjpa.setVersion(:NEW.gorrace_version);
  gb_race_ethnicity.p_update
    (p_race_cde => :NEW.gorrace_race_cde,
     p_desc => :NEW.gorrace_desc,
     p_user_id => :NEW.gorrace_user_id,
     p_rrac_code => :NEW.gorrace_rrac_code,
     p_edi_equiv => :NEW.gorrace_edi_equiv,
     p_lms_equiv => :NEW.gorrace_lms_equiv,
     p_data_origin => :NEW.gorrace_data_origin,
     p_rowid => :NEW.gorrace_v_rowid);
END;
/