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
REM dbeu_ext_stu_bgvc.sql
REM 
REM AUDIT TRAIL: 9.0 
REM 1. Horizon 
REM AUDIT TRAIL END 
REM
whenever oserror exit rollback;
whenever sqlerror exit rollback;
REM connect dbeu_owner/&&dbeu_password
execute dbeu_util.extend_table('SATURN','STVACAT','S',FALSE);
execute dbeu_util.extend_table('SATURN','STVACYR','S',FALSE);
execute dbeu_util.extend_table('SATURN','STVADMR','S',FALSE);
execute dbeu_util.extend_table('SATURN','STVASRC','S',FALSE);
execute dbeu_util.extend_table('SATURN','STVATYP','S',FALSE);
execute dbeu_util.extend_table('SATURN','STVBLDG','S',FALSE);
execute dbeu_util.extend_table('SATURN','STVCAMP','S',FALSE);
execute dbeu_util.extend_table('SATURN','STVCIPC','S',FALSE);
execute dbeu_util.extend_table('SATURN','STVCNTY','S',FALSE);
execute dbeu_util.extend_table('SATURN','STVCOLL','S',FALSE);
execute dbeu_util.extend_table('SATURN','STVCOMT','S',FALSE);
execute dbeu_util.extend_table('SATURN','STVDAYS','S',FALSE);
execute dbeu_util.extend_table('SATURN','STVDEGC','S',FALSE);
execute dbeu_util.extend_table('SATURN','STVDEPT','S',FALSE);
execute dbeu_util.extend_table('SATURN','STVDISA','S',FALSE);
execute dbeu_util.extend_table('SATURN','STVDLEV','S',FALSE);
execute dbeu_util.extend_table('SATURN','STVETYP','S',FALSE);
execute dbeu_util.extend_table('SATURN','STVHLDD','S',FALSE);
execute dbeu_util.extend_table('SATURN','STVHLDD','S',FALSE);
execute dbeu_util.extend_table('SATURN','STVLEVL','S',FALSE);
execute dbeu_util.extend_table('SATURN','STVMAJR','S',FALSE);
execute dbeu_util.extend_table('SATURN','STVMDEQ','S',FALSE);
execute dbeu_util.extend_table('SATURN','STVMEDI','S',FALSE);
execute dbeu_util.extend_table('SATURN','STVNATN','S',FALSE);
execute dbeu_util.extend_table('SATURN','STVORIG','S',FALSE);
execute dbeu_util.extend_table('SATURN','STVPRCD','S',FALSE);
execute dbeu_util.extend_table('SATURN','STVRDEF','S',FALSE);
execute dbeu_util.extend_table('SATURN','STVRELT','S',FALSE);
execute dbeu_util.extend_table('SATURN','STVRMST','S',FALSE);
execute dbeu_util.extend_table('SATURN','STVRRCD','S',FALSE);
execute dbeu_util.extend_table('SATURN','STVSBGI','S',FALSE);
execute dbeu_util.extend_table('SATURN','STVSITE','S',FALSE);
execute dbeu_util.extend_table('SATURN','STVSPSR','S',FALSE);
execute dbeu_util.extend_table('SATURN','STVSTAT','S',FALSE);
execute dbeu_util.extend_table('SATURN','STVSUBJ','S',FALSE);
execute dbeu_util.extend_table('SATURN','STVTELE','S',FALSE);
execute dbeu_util.extend_table('SATURN','STVTERM','S',FALSE);
execute dbeu_util.extend_table('SATURN','STVTESC','S',FALSE);
execute dbeu_util.extend_table('SATURN','STVTRMT','S',FALSE);
execute dbeu_util.extend_table('SATURN','STVVTYP','S',FALSE);
execute dbeu_util.extend_table('SATURN','STVINIT','S',FALSE);
execute dbeu_util.extend_table('SATURN','STVACTC','S',FALSE);
execute dbeu_util.extend_table('SATURN','STVACTP','S',FALSE);
execute dbeu_util.extend_table('SATURN','STVACCG','S',FALSE);
execute dbeu_util.extend_table('SATURN','STVLEAD','S',FALSE);