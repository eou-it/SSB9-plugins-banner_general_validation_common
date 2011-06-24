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
execute dbeu_util.extend_table('SATURN','STVACYR','T',FALSE);
execute dbeu_util.extend_table('SATURN','STVADMR','T',FALSE);
execute dbeu_util.extend_table('SATURN','STVACAT','T',FALSE);
execute dbeu_util.extend_table('SATURN','STVBLDG','T',FALSE);
execute dbeu_util.extend_table('SATURN','STVRDEF','T',FALSE);
execute dbeu_util.extend_table('SATURN','STVCAMP','T',FALSE);
execute dbeu_util.extend_table('SATURN','STVCIPC','T',FALSE);
execute dbeu_util.extend_table('SATURN','STVCOLL','T',FALSE);
execute dbeu_util.extend_table('SATURN','STVCOMT','T',FALSE);
execute dbeu_util.extend_table('SATURN','STVCNTY','T',FALSE);
execute dbeu_util.extend_table('SATURN','STVDAYS','T',FALSE);
execute dbeu_util.extend_table('SATURN','STVDEGC','T',FALSE);
execute dbeu_util.extend_table('SATURN','STVDLEV','T',FALSE);
execute dbeu_util.extend_table('SATURN','STVDEPT','T',FALSE);
execute dbeu_util.extend_table('SATURN','STVDISA','T',FALSE);
execute dbeu_util.extend_table('SATURN','STVSPSR','T',FALSE);
execute dbeu_util.extend_table('SATURN','STVHLDD','T',FALSE);
execute dbeu_util.extend_table('SATURN','STVLEVL','T',FALSE);
execute dbeu_util.extend_table('SATURN','STVMAJR','T',FALSE);
execute dbeu_util.extend_table('SATURN','STVMEDI','T',FALSE);
execute dbeu_util.extend_table('SATURN','STVMDEQ','T',FALSE);
execute dbeu_util.extend_table('SATURN','STVNATN','T',FALSE);
execute dbeu_util.extend_table('SATURN','STVORIG','T',FALSE);
execute dbeu_util.extend_table('SATURN','STVPRCD','T',FALSE);
execute dbeu_util.extend_table('SATURN','STVRRCD','T',FALSE);
execute dbeu_util.extend_table('SATURN','STVRMST','T',FALSE);
execute dbeu_util.extend_table('SATURN','STVSITE','T',FALSE);
execute dbeu_util.extend_table('SATURN','STVSBGI','T',FALSE);
execute dbeu_util.extend_table('SATURN','STVSTAT','T',FALSE);
execute dbeu_util.extend_table('SATURN','STVSUBJ','T',FALSE);
execute dbeu_util.extend_table('SATURN','STVTERM','T',FALSE);
execute dbeu_util.extend_table('SATURN','STVTRMT','T',FALSE);
execute dbeu_util.extend_table('SATURN','STVTESC','T',FALSE);
execute dbeu_util.extend_table('SATURN','STVVTYP','T',FALSE);
