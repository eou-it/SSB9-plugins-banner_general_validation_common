-- *****************************************************************************
-- *                                                                           *
-- * Copyright 2013 Ellucian Company L.P. and its affiliates.                  *
-- *                                                                           *
-- *****************************************************************************
REM
REM gv_gorrace.sql
REM
REM AUDIT TRAIL: 9.0
REM 1. Horizon
REM Generated view for Horizon API support
REM AUDIT TRAIL END
REM
CREATE OR REPLACE FORCE VIEW gv_gorrace AS SELECT
      gorrace_race_cde,
      gorrace_desc,
      gorrace_rrac_code,
      gorrace_edi_equiv,
      gorrace_lms_equiv,
      gorrace_surrogate_id,
      gorrace_version,
      gorrace_user_id,
      gorrace_data_origin,
      gorrace_activity_date,
      ROWID gorrace_v_rowid
  FROM gorrace;
CREATE OR REPLACE PUBLIC SYNONYM gv_gorrace FOR gv_gorrace;
