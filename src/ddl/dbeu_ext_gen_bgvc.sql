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


