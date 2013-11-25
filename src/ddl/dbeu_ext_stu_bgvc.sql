REM *****************************************************************************************
REM * Copyright 2009-2013 Ellucian Company L.P. and its affiliates.                         *
REM *****************************************************************************************
REM dbeu_ext_stu_bgvc.sql
REM
REM AUDIT TRAIL: 9.0
REM 1. Invocation of dbeu extenstion utility for student module. 
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
execute dbeu_util.extend_table('SATURN','STVBCHR','S',FALSE);
execute dbeu_util.extend_table('SATURN','STVBLDG','S',FALSE);
execute dbeu_util.extend_table('SATURN','STVCAMP','S',FALSE);
execute dbeu_util.extend_table('SATURN','STVCIPC','S',FALSE);
execute dbeu_util.extend_table('SATURN','STVCITZ','S',FALSE);
execute dbeu_util.extend_table('SATURN','STVCNTY','S',FALSE);
execute dbeu_util.extend_table('SATURN','STVCOLL','S',FALSE);
execute dbeu_util.extend_table('SATURN','STVCOMT','S',FALSE);
execute dbeu_util.extend_table('SATURN','STVDAYS','S',FALSE);
execute dbeu_util.extend_table('SATURN','STVDEGC','S',FALSE);
execute dbeu_util.extend_table('SATURN','STVDEPT','S',FALSE);
execute dbeu_util.extend_table('SATURN','STVDISA','S',FALSE);
execute dbeu_util.extend_table('SATURN','STVDLEV','S',FALSE);
execute dbeu_util.extend_table('SATURN','STVDPLM','S',FALSE);
execute dbeu_util.extend_table('SATURN','STVETCT','S',FALSE);
execute dbeu_util.extend_table('SATURN','STVETHN','S',FALSE);
execute dbeu_util.extend_table('SATURN','STVETYP','S',FALSE);
execute dbeu_util.extend_table('SATURN','STVHLDD','S',FALSE);
execute dbeu_util.extend_table('SATURN','STVHLDD','S',FALSE);
execute dbeu_util.extend_table('SATURN','STVLEVL','S',FALSE);
execute dbeu_util.extend_table('SATURN','STVLGCY','S',FALSE);
execute dbeu_util.extend_table('SATURN','STVMAJR','S',FALSE);
execute dbeu_util.extend_table('SATURN','STVMDEQ','S',FALSE);
execute dbeu_util.extend_table('SATURN','STVMEDI','S',FALSE);
execute dbeu_util.extend_table('SATURN','STVMRTL','S',FALSE);
execute dbeu_util.extend_table('SATURN','STVNATN','S',FALSE);
execute dbeu_util.extend_table('SATURN','STVORIG','S',FALSE);
execute dbeu_util.extend_table('SATURN','STVPRCD','S',FALSE);
execute dbeu_util.extend_table('SATURN','STVPTYP','S',FALSE);
execute dbeu_util.extend_table('SATURN','STVRDEF','S',FALSE);
execute dbeu_util.extend_table('SATURN','STVRELG','S',FALSE);
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
execute dbeu_util.extend_table('SATURN','STVHONR','S',FALSE);
