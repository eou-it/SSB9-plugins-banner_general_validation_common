--/** *****************************************************************************
-- Copyright 2013 Ellucian Company L.P. and its affiliates.
-- ****************************************************************************** */

REM
REM ggtvgurinfoi.sql
REM
-- AUDIT TRAIL: 9.0
-- 1. Seed data entries for GURINFO for BXE pages.
-- AUDIT TRAIL END
REM

--
-- Seed data for Race And Ethnicity Page
--

INSERT INTO GURINFO (
            GURINFO_PAGE_NAME,
            GURINFO_LABEL,
            GURINFO_TEXT_TYPE,
            GURINFO_SEQUENCE_NUMBER,
            GURINFO_ROLE_CODE,
            GURINFO_TEXT,
            GURINFO_LOCALE,
            GURINFO_COMMENT,
            GURINFO_SOURCE_INDICATOR,
            GURINFO_ACTIVITY_DATE,
            GURINFO_START_DATE,
            GURINFO_END_DATE,
            GURINFO_SURROGATE_ID,
            GURINFO_VERSION,
            GURINFO_USER_ID,
            GURINFO_DATA_ORIGIN,
            GURINFO_VPDI_CODE)
SELECT 'RESURVEY',
       'ethnicity.header',
       'N',
       1,
       'DEFAULT',
       'What is your ethnicity?',
       'en_US',
       null,
       'B',
       SYSDATE,
       null,
       null,
       null,
       null,
       'BASELINE',
       'GENERAL',
       null
FROM DUAL WHERE NOT EXISTS ( SELECT 'X'
                             FROM GURINFO
                             WHERE GURINFO_PAGE_NAME = 'RESURVEY'
                               AND GURINFO_LABEL = 'ethnicity.header'
                               AND GURINFO_SEQUENCE_NUMBER = 1
                               AND GURINFO_ROLE_CODE = 'DEFAULT'
                               AND GURINFO_LOCALE = 'en_US'
                               AND GURINFO_SOURCE_INDICATOR = 'B');

INSERT INTO GURINFO (
            GURINFO_PAGE_NAME,
            GURINFO_LABEL,
            GURINFO_TEXT_TYPE,
            GURINFO_SEQUENCE_NUMBER,
            GURINFO_ROLE_CODE,
            GURINFO_TEXT,
            GURINFO_LOCALE,
            GURINFO_COMMENT,
            GURINFO_SOURCE_INDICATOR,
            GURINFO_ACTIVITY_DATE,
            GURINFO_START_DATE,
            GURINFO_END_DATE,
            GURINFO_SURROGATE_ID,
            GURINFO_VERSION,
            GURINFO_USER_ID,
            GURINFO_DATA_ORIGIN,
            GURINFO_VPDI_CODE)
SELECT 'RESURVEY',
       'race.header',
       'N',
       1,
       'DEFAULT',
       'Select one or more races to indicate what you consider yourself to be.',
       'en_US',
       null,
       'B',
       SYSDATE,
       null,
       null,
       null,
       null,
       'BASELINE',
       'GENERAL',
       null
FROM DUAL WHERE NOT EXISTS ( SELECT 'X'
                             FROM GURINFO
                             WHERE GURINFO_PAGE_NAME = 'RESURVEY'
                               AND GURINFO_LABEL = 'race.header'
                               AND GURINFO_SEQUENCE_NUMBER = 1
                               AND GURINFO_ROLE_CODE = 'DEFAULT'
                               AND GURINFO_LOCALE = 'en_US'
                               AND GURINFO_SOURCE_INDICATOR = 'B');

--
-- Seed data for Terms of Usage page
--

INSERT INTO GURINFO (
            GURINFO_PAGE_NAME,
            GURINFO_LABEL,
            GURINFO_TEXT_TYPE,
            GURINFO_SEQUENCE_NUMBER,
            GURINFO_ROLE_CODE,
            GURINFO_TEXT,
            GURINFO_LOCALE,
            GURINFO_COMMENT,
            GURINFO_SOURCE_INDICATOR,
            GURINFO_ACTIVITY_DATE,
            GURINFO_START_DATE,
            GURINFO_END_DATE,
            GURINFO_SURROGATE_ID,
            GURINFO_VERSION,
            GURINFO_USER_ID,
            GURINFO_DATA_ORIGIN,
            GURINFO_VPDI_CODE)
SELECT 'TERMSOFUSAGE',
       'terms.of.usage',
       'N',
       1,
       'DEFAULT',
       '>This area contains secure information. Unless otherwise noted, any information you enter or change will become effective immediately. You are responsible for any changes made using your ID. Please do not share your ID or PIN with others.
>
>',
       'en_US',
       null,
       'B',
       SYSDATE,
       null,
       null,
       null,
       null,
       'BASELINE',
       'GENERAL',
       null
FROM DUAL WHERE NOT EXISTS ( SELECT 'X'
                             FROM GURINFO
                             WHERE GURINFO_PAGE_NAME = 'TERMSOFUSAGE'
                               AND GURINFO_LABEL = 'terms.of.usage'
                               AND GURINFO_SEQUENCE_NUMBER = 1
                               AND GURINFO_ROLE_CODE = 'DEFAULT'
                               AND GURINFO_LOCALE = 'en_US'
                               AND GURINFO_SOURCE_INDICATOR = 'B');



INSERT INTO GURINFO (
            GURINFO_PAGE_NAME,
            GURINFO_LABEL,
            GURINFO_TEXT_TYPE,
            GURINFO_SEQUENCE_NUMBER,
            GURINFO_ROLE_CODE,
            GURINFO_TEXT,
            GURINFO_LOCALE,
            GURINFO_COMMENT,
            GURINFO_SOURCE_INDICATOR,
            GURINFO_ACTIVITY_DATE,
            GURINFO_START_DATE,
            GURINFO_END_DATE,
            GURINFO_SURROGATE_ID,
            GURINFO_VERSION,
            GURINFO_USER_ID,
            GURINFO_DATA_ORIGIN,
            GURINFO_VPDI_CODE)
SELECT 'TERMSOFUSAGE',
       'terms.of.usage',
       'N',
       2,
       'DEFAULT',
       '>If you agree to these terms of usage, Continue. If you do not, please Exit.',
       'en_US',
       null,
       'B',
       SYSDATE,
       null,
       null,
       null,
       null,
       'BASELINE',
       'GENERAL',
       null
FROM DUAL WHERE NOT EXISTS ( SELECT 'X'
                             FROM GURINFO
                             WHERE GURINFO_PAGE_NAME = 'TERMSOFUSAGE'
                               AND GURINFO_LABEL = 'terms.of.usage'
                               AND GURINFO_SEQUENCE_NUMBER = 2
                               AND GURINFO_ROLE_CODE = 'DEFAULT'
                               AND GURINFO_LOCALE = 'en_US'
                               AND GURINFO_SOURCE_INDICATOR = 'B');

INSERT INTO GURINFO (
            GURINFO_PAGE_NAME,
            GURINFO_LABEL,
            GURINFO_TEXT_TYPE,
            GURINFO_SEQUENCE_NUMBER,
            GURINFO_ROLE_CODE,
            GURINFO_TEXT,
            GURINFO_LOCALE,
            GURINFO_COMMENT,
            GURINFO_SOURCE_INDICATOR,
            GURINFO_ACTIVITY_DATE,
            GURINFO_START_DATE,
            GURINFO_END_DATE,
            GURINFO_SURROGATE_ID,
            GURINFO_VERSION,
            GURINFO_USER_ID,
            GURINFO_DATA_ORIGIN,
            GURINFO_VPDI_CODE)
SELECT 'SECURITYQA',
       'security.qa.information',
       'N',
       1,
       'DEFAULT',
       'Please enter your new Security Question and Answer, then Submit Changes.',
       'en_US',
       null,
       'B',
       SYSDATE,
       null,
       null,
       null,
       null,
       'BASELINE',
       'GENERAL',
       null
FROM DUAL WHERE NOT EXISTS ( SELECT 'X'
                             FROM GURINFO
                             WHERE GURINFO_PAGE_NAME = 'SECURITYQA'
                               AND GURINFO_LABEL = 'security.qa.information'
                               AND GURINFO_SEQUENCE_NUMBER = 1
                               AND GURINFO_ROLE_CODE = 'DEFAULT'
                               AND GURINFO_LOCALE = 'en_US'
                               AND GURINFO_SOURCE_INDICATOR = 'B');
