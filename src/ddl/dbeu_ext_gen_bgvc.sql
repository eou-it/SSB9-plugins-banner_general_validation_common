--
-- dbeu_table_extends.sql
--
-- V8.1
--
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
REM dbeu_ext_gen_bgvc.sql
REM
REM AUDIT TRAIL: 9.0
REM 1. Horizon
REM AUDIT TRAIL END
REM
whenever oserror exit rollback;
whenever sqlerror exit rollback;
REM connect dbeu_owner/&&dbeu_password
execute dbeu_util.extend_table('GENERAL','GTVDICD','G',FALSE);
execute dbeu_util.extend_table('GENERAL','GTVDUNT','G',FALSE);
execute dbeu_util.extend_table('GENERAL','GTVEMPH','G',FALSE);
execute dbeu_util.extend_table('GENERAL','GTVFDMN','G',FALSE);
execute dbeu_util.extend_table('GENERAL','GTVFEES','G',FALSE);
execute dbeu_util.extend_table('GENERAL','GTVFSTA','G',FALSE);
execute dbeu_util.extend_table('GENERAL','GTVFUNC','G',FALSE);
execute dbeu_util.extend_table('GENERAL','GTVINSM','G',FALSE);
execute dbeu_util.extend_table('GENERAL','GTVINTP','G',FALSE);
execute dbeu_util.extend_table('GENERAL','GTVLETR','G',FALSE);
execute dbeu_util.extend_table('GENERAL','GTVLFST','G',FALSE);
execute dbeu_util.extend_table('GENERAL','GTVMENU','G',FALSE);
execute dbeu_util.extend_table('GENERAL','GTVMTYP','G',FALSE);
execute dbeu_util.extend_table('GENERAL','GTVNTYP','G',FALSE);
execute dbeu_util.extend_table('GENERAL','GTVPARS','G',FALSE);
execute dbeu_util.extend_table('GENERAL','GTVPTYP','G',FALSE);
execute dbeu_util.extend_table('GENERAL','GTVPURP','G',FALSE);
execute dbeu_util.extend_table('GENERAL','GTVRSVP','G',FALSE);
execute dbeu_util.extend_table('GENERAL','GTVRTNG','G',FALSE);
execute dbeu_util.extend_table('GENERAL','GTVSCHS','G',FALSE);
execute dbeu_util.extend_table('GENERAL','GTVSDAX','G',FALSE);
execute dbeu_util.extend_table('GENERAL','GTVSUBJ','G',FALSE);
execute dbeu_util.extend_table('GENERAL','GTVSYSI','G',FALSE);
execute dbeu_util.extend_table('GENERAL','GUBINST','G',FALSE);
execute dbeu_util.extend_table('GENERAL','GTVEMAL','G',FALSE);
execute dbeu_util.extend_table('GENERAL','GTVEXPN','G',FALSE);
execute dbeu_util.extend_table('GENERAL','GTVREVN','G',FALSE);
execute dbeu_util.extend_table('GENERAL','GTVFTYP','G',FALSE);
execute dbeu_util.extend_table('GENERAL','GTVMAIL','G',FALSE);
execute dbeu_util.extend_table('GENERAL','GTVRATE','G',FALSE);
execute dbeu_util.extend_table('GENERAL','GTVTARG','G',FALSE);
execute dbeu_util.extend_table('GENERAL','GTVCMSC','G',FALSE);

execute dbeu_util.extend_table('GENERAL','GTVSQRU','G',FALSE);
execute dbeu_util.extend_table('GENERAL','GTVSQPR','G',FALSE);
