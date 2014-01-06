REM *****************************************************************************
REM * Copyright 2013 Ellucian Company L.P. and its affiliates.                  *
REM *****************************************************************************
REM
REM gv_gorrace_ins_trg.sql
REM
REM AUDIT TRAIL: 9.0
REM 1. Banner XE
REM Generated trigger for Banner XE API support
REM AUDIT TRAIL END
REM

CREATE OR REPLACE TRIGGER gorrace_view_create_trg
  INSTEAD OF INSERT ON gv_gorrace
DECLARE
  p_rowid_v VARCHAR2(100);
BEGIN
  gfksjpa.setId(:NEW.gorrace_surrogate_id);
  gfksjpa.setVersion(:NEW.gorrace_version);
  gb_race_ethnicity.p_create
    (p_race_cde => :NEW.gorrace_race_cde,
     p_desc => :NEW.gorrace_desc,
     p_user_id => :NEW.gorrace_user_id,
     p_rrac_code => :NEW.gorrace_rrac_code,
     p_edi_equiv => :NEW.gorrace_edi_equiv,
     p_lms_equiv => :NEW.gorrace_lms_equiv,
     p_data_origin => :NEW.gorrace_data_origin,
     p_rowid_out => p_rowid_v);
END;
/
