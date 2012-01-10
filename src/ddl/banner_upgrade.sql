-- *****************************************************************************************
-- * Copyright 2009-2011 SunGard Higher Education. All Rights Reserved.                    *
-- * This copyrighted software contains confidential and proprietary information of        *
-- * SunGard Higher Education and its subsidiaries. Any use of this software is limited    *
-- * solely to SunGard Higher Education licensees, and is further subject to the terms     *
-- * and conditions of one or more written license agreements between SunGard Higher       *
-- * Education and the licensee in question. SunGard is either a registered trademark or   *
-- * trademark of SunGard Data Systems in the U.S.A. and/or other regions and/or countries.*
-- * Banner and Luminis are either registered trademarks or trademarks of SunGard Higher   *
-- * Education in the U.S.A. and/or other regions and/or countries.                        *
-- *****************************************************************************************
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