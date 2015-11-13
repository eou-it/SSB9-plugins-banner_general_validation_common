/*******************************************************************************
 Copyright 2013 Ellucian Company L.P. and its affiliates.
 ***************************************************************************** */
package net.hedtech.banner.general.utility

import grails.util.Holders
import org.apache.commons.lang.StringUtils
import org.apache.log4j.Logger
import grails.util.Holders
import org.springframework.context.MessageSource
import org.springframework.context.i18n.LocaleContextHolder

import java.text.ParseException
import java.text.SimpleDateFormat

class InformationTextPersonaListService {
    def sessionFactory
    def static webTailorRoleList = []
    static final String PERSONA_DEFAULT = "DEFAULT"

    static Logger staticLog = Logger.getLogger( "net.hedtech.banner.general.utility.InformationTextPersonaListService" )

    static {
        if (Holders?.config?.webTailorRoleList?.size() > 0) {
            webTailorRoleList = Holders.config.webTailorRoleList
        }
    }

    private static boolean isDefaultPersonaAdded() {
        return webTailorRoleList.any { it.code == PERSONA_DEFAULT }
    }

    private static void addDefaultPersonaIfNotExists() {

        webTailorRoleList.removeAll{
            it.code == PERSONA_DEFAULT
        }
        MessageSource messageSource = Holders.getGrailsApplication().mainContext.getBean('messageSource')
        webTailorRoleList <<  [
                code: PERSONA_DEFAULT,
                description: messageSource.getMessage("net.hedtech.banner.general.utility.InformationTextPersona.default.persona.description", null, LocaleContextHolder.getLocale()),
                lastModified: "",
                lastModifiedBy: ""
        ]
    }

    public static def getWebtailorRoleConfiguration() {
        addDefaultPersonaIfNotExists();
        return webTailorRoleList;
    }

    public static List fetchInformationTextPersonas(String filter) {
        if (StringUtils.isBlank(filter)) {
            filter = "%"
        } else if (!(filter =~ /%/)) {
            filter += "%"
        }

        List filteredWebTailorRoleList = filterAndOrderWebTailorRoleList(getWebtailorRoleConfiguration(), filter)

        List informationTextPersonaList = []
        filteredWebTailorRoleList?.each { webTailorRole ->
            informationTextPersonaList << new InformationTextPersona(
                    [
                            code: webTailorRole.code,
                            description: webTailorRole.description,
                            lastModifiedBy: webTailorRole.lastModifiedBy,
                            lastModified: getDate(webTailorRole.lastModified, "dd-MMM-yy")
                    ])

        }

        return informationTextPersonaList
    }

    private static List filterAndOrderWebTailorRoleList(List webTailorRoleList, String filter) {
        String filterColumn = "code"

        String regEx = this.getRegEx(filter)

        List filteredResults
        filteredResults = webTailorRoleList.findAll {
            it."$filterColumn".toString().toLowerCase().matches(regEx)
        }

        //apply order by
        def orderByCodeDescLastMod = new OrderBy([{ it.code }, { it.description }, { it.lastModified }])
        filteredResults.sort(orderByCodeDescLastMod)

        return filteredResults
    }

    private static String getRegEx(String val) {
        val = val.toLowerCase()

        if (!val.startsWith("%")) {
            val = "%" + val
        }
        if (!val.endsWith("%")) {
            val = val + "%"
        }

        val = val.replaceAll("%", "\\\\E" + ".*" + "\\\\Q")
        val = val.replaceAll("_", "\\\\E" + "." + "\\\\Q")
        val = "\\Q" + val + "\\E"

        String regEx = val

        return regEx
    }

    private static Date getDate(String dateString, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format)
        sdf.setLenient(false)
        try {
            return sdf.parse(dateString)
        } catch (ParseException e) {
            // Ignore because its not the right format.
            staticLog.warn(" Date was not in the $format format")
        }
        return null
    }


    public static def fetchPersonas() {
        def filter = "%"
        def returnList = fetchInformationTextPersonas(filter)
        def returnObj = [list: returnList]
        return returnObj
    }


    public static def fetchPersonas(filter) {
        def returnList = fetchInformationTextPersonas(filter)
        def returnObj = [list: returnList]
        return returnObj
    }


    public static InformationTextPersona fetchValidInformationTextPersona(String filter) {
        def returnObj = fetchInformationTextPersonas(filter)[0]
        return returnObj?.code ? returnObj : null
    }
}
