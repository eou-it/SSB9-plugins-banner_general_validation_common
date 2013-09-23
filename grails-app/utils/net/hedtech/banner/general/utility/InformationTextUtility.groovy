package net.hedtech.banner.general.utility

import org.springframework.context.i18n.LocaleContextHolder
import org.codehaus.groovy.grails.web.context.ServletContextHolder
import org.codehaus.groovy.grails.web.servlet.GrailsApplicationAttributes
import groovy.sql.Sql

import net.hedtech.banner.security.BannerGrantedAuthorityService
import net.hedtech.banner.general.utility.SourceIndicators
/** *****************************************************************************
 © 2013 SunGard Higher Education.  All Rights Reserved.

 CONFIDENTIAL BUSINESS INFORMATION

 THIS PROGRAM IS PROPRIETARY INFORMATION OF SUNGARD HIGHER EDUCATION
 AND IS NOT TO BE COPIED, REPRODUCED, LENT, OR DISPOSED OF,
 NOR USED FOR ANY PURPOSE OTHER THAN THAT WHICH IT IS SPECIFICALLY PROVIDED
 WITHOUT THE WRITTEN PERMISSION OF THE SAID COMPANY
 ****************************************************************************** */
class InformationTextUtility {

    public static Map getMessages(String pageName, Locale locale = LocaleContextHolder.getLocale()) {
        Map informationTexts = new HashMap()
        String localeParam = locale.toString();
        List<String> roles = BannerGrantedAuthorityService.getUserRoles()
        if (roles) {
            List<String> params = [pageName]
            String roleClauseParams = getParams(roles, params)
            params << localeParam
            def sql = getSQLObject()
            def queryString = " ${buildBasicQueryString(roleClauseParams)} ORDER BY GURINFO_LABEL, GURINFO_SEQUENCE_NUMBER "
            String sqlQueryString = queryString.toString()
            def resultSet = sql.rows(sqlQueryString, params)
            resultSet.each { t ->
                String infoText = informationTexts.get(t.GURINFO_LABEL)
                infoText = getInfoText(infoText, t)
                informationTexts.put(t.GURINFO_LABEL, infoText)
            }
        }
        return informationTexts
    }



    public static String getMessage(String pageName, String label, Locale locale = LocaleContextHolder.getLocale()) {
        String infoText = null
        String localeParam = locale.toString();
        List<String> roles = BannerGrantedAuthorityService.getUserRoles()
        if (roles) {
            List<String> params = [pageName]
            String roleClauseParams = getParams(roles, params)
            params << localeParam
            params << label
            def queryString = " ${buildBasicQueryString(roleClauseParams)} AND GURINFO_LABEL = ?   ORDER BY GURINFO_LABEL, GURINFO_SEQUENCE_NUMBER "
            String sqlQueryString = queryString.toString()
            Sql sql = getSQLObject()
            def resultSet = sql.rows(sqlQueryString, params)
            resultSet.each {t ->
                infoText = getInfoText(infoText, t)
                if (infoText == null) {
                    infoText = label
                }
            }
            return infoText
        }
    }

    private static String getInfoText(infoText, t) {
        if (infoText == null || infoText == "") { infoText = getTextBasedOnDateRange(t) }

        else {
            if (getTextBasedOnDateRange(t) != "") { infoText += "\n" + getTextBasedOnDateRange(t) }

        }
    }

    private static String buildBasicQueryString(roleClauseParams) {
        return """ SELECT * FROM gurinfo a
                           WHERE gurinfo_page_name = ?
                           AND GURINFO_ROLE_CODE IN (${roleClauseParams})
                           AND GURINFO_LOCALE = ?
                           AND GURINFO_SOURCE_INDICATOR =
                           (
                               SELECT nvl( MAX(GURINFO_SOURCE_INDICATOR ),'${SourceIndicators.getSourceIndicator("B").getCode()}')
                               FROM GURINFO
                               WHERE gurinfo_page_name = a.gurinfo_page_name
                               AND GURINFO_LABEL = a.GURINFO_LABEL
                               AND GURINFO_SEQUENCE_NUMBER = a.GURINFO_SEQUENCE_NUMBER
                               AND GURINFO_ROLE_CODE = a.GURINFO_ROLE_CODE
                               AND GURINFO_LOCALE = a.GURINFO_LOCALE
                               AND GURINFO_SOURCE_INDICATOR ='${SourceIndicators.getSourceIndicator("L").getCode()}'
                               AND TRUNC(SYSDATE) BETWEEN TRUNC(NVL( GURINFO_START_DATE, (SYSDATE - 1) ) ) AND TRUNC( NVL( GURINFO_END_DATE, (SYSDATE + 1) ))
                           )"""


    }

    private static String getTextBasedOnDateRange(row) {
        if (row.GURINFO_SOURCE_INDICATOR == "${SourceIndicators.getSourceIndicator("L").getCode()}" && row.GURINFO_START_DATE == null) {
            return ""
        }
        else {
            row.GURINFO_TEXT
        }
    }

    private static Sql getSQLObject() {
        def ctx = ServletContextHolder.servletContext.getAttribute(GrailsApplicationAttributes.APPLICATION_CONTEXT)
        def sessionFactory = ctx.sessionFactory
        def session = sessionFactory.currentSession
        def sql = new Sql(session.connection())
        sql
    }

    private static String getParams(ArrayList<String> roles, params) {
        String roleClauseParams = null
        if (roles.size() >= 1) {
            roleClauseParams = "?"
            String role = roles.get(0)
            params << role
        }
        for (int i = 1; i < roles.size(); i++) {
            roleClauseParams += ", ? "
            String role = roles.get(i)
            params << role
        }
        roleClauseParams
    }
    }
