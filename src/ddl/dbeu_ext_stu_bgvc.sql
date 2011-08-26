--
-- dbeu_table_extends.sql
--
-- V8.1
--
-- *****************************************************************************
-- *                                                                           *
-- * Copyright 2011 SunGard. All rights reserved.                              *
-- *                                                                           *
-- * SunGard or its subsidiaries in the U.S. and other countries is the owner  *
-- * of numerous marks, including 'SunGard,' the SunGard logo, 'Banner,'       *
-- * 'PowerCAMPUS,' 'Advance,' 'Luminis,' 'UDC,' and 'Unified Digital Campus.' *
-- * Other names and marks used in this material are owned by third parties.   *
-- *                                                                           *
-- * This [site/software] contains confidential and proprietary information of *
-- * SunGard and its subsidiaries. Use of this [site/software] is limited to   *
-- * SunGard Higher Education licensees, and is subject to the terms and       *
-- * conditions of one or more written license agreements between SunGard      *
-- * Higher Education and the licensee in question.                            *
-- *                                                                           *
-- *****************************************************************************
--
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

