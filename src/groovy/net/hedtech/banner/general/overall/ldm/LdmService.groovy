/*******************************************************************************
 Copyright 2014 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.general.overall.ldm
import net.hedtech.banner.exceptions.BusinessLogicValidationException

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.exceptions.NotFoundException
import net.hedtech.banner.general.overall.IntegrationConfiguration
import net.hedtech.banner.utility.MessageResolver

import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat

class LdmService {

    def sessionFactory
    String timeFormat = ''

    private static HashMap ldmFieldToBannerDomainPropertyMap = [
            abbreviation: 'code',
            title       : 'description',
            number      : 'roomNumber'
    ]


    static String fetchBannerDomainPropertyForLdmField(String ldmField) {
        return ldmFieldToBannerDomainPropertyMap[ldmField]
    }

    /**
     * Added for supporting secondary level cache on GV_GORICCR View, the cached is specific to the query and
     * data will be fetched based  the query, this method is used for  fetch the Banner Value by passing
     * the LDM Value i.e. can be used to fetch rule probably  during the CREATE and UPDATE API’s,
     * wherein the request payload containing the LDM Value can be passes to this method to get the corresponding
     * Banner Type before creating/updating the record in Banner
     * @param processCode
     * @param settingName
     * @param translationValue
     * @return
     */
    IntegrationConfiguration fetchAllByProcessCodeAndSettingNameAndTranslationValue(String processCode, String settingName, String translationValue) {
        List<IntegrationConfiguration> integrationConfigs = IntegrationConfiguration.fetchAllByProcessCodeAndSettingNameAndTranslationValue('LDM', settingName, translationValue)
        IntegrationConfiguration integrationConfig = integrationConfigs.size() > 0 ? integrationConfigs.get(0) : null
        LdmService.log.debug("ldmEnumeration MissCount--" + sessionFactory.getStatistics().getSecondLevelCacheStatistics(IntegrationConfiguration.LDM_CACHE_REGION_NAME).getMissCount())
        LdmService.log.debug("ldmEnumeration HitCount --" + sessionFactory.getStatistics().getSecondLevelCacheStatistics(IntegrationConfiguration.LDM_CACHE_REGION_NAME).getHitCount())
        LdmService.log.debug("ldmEnumeration PutCount --" + sessionFactory.getStatistics().getSecondLevelCacheStatistics(IntegrationConfiguration.LDM_CACHE_REGION_NAME).getPutCount())
        return integrationConfig
    }

    /**
     * Added for supporting secondary level cache on GV_GORICCR View, the cached is specific to the query and
     * data will be fetched based on the query, this method is used for  fetch the LDM Value by passing
     * the Banner Value i.e. can be used to fetch rule probably during the SHOW  and LIST API’ s,
     * wherein before sending the response  to LDM the  Banner Value can be passes to this method to
     * get the LDM Type for including in the response.
     * @param processCode
     * @param settingName
     * @param value
     * @return
     */
    IntegrationConfiguration findAllByProcessCodeAndSettingNameAndValue(String processCode, String settingName, String value) {
        List<IntegrationConfiguration> integrationConfigs = IntegrationConfiguration.fetchAllByProcessCodeAndSettingNameAndValue('LDM', settingName, value)
        IntegrationConfiguration integrationConfig = integrationConfigs.size() > 0 ? integrationConfigs.get(0) : null
        LdmService.log.debug("ldmEnumeration MissCount--" + sessionFactory.getStatistics().getSecondLevelCacheStatistics(IntegrationConfiguration.LDM_CACHE_REGION_NAME).getMissCount())
        LdmService.log.debug("ldmEnumeration HitCount --" + sessionFactory.getStatistics().getSecondLevelCacheStatistics(IntegrationConfiguration.LDM_CACHE_REGION_NAME).getHitCount())
        LdmService.log.debug("ldmEnumeration PutCount --" + sessionFactory.getStatistics().getSecondLevelCacheStatistics(IntegrationConfiguration.LDM_CACHE_REGION_NAME).getPutCount())
        return integrationConfig
    }

    /**
     * This method ensures that application exception includes BusinessLogicValidationException message which returns with http status code 400.
     * @param e ApplicationException
     */
    static void throwBusinessLogicValidationException(ApplicationException ae) {
        if (ae.wrappedException instanceof NotFoundException) {
                throw new ApplicationException(ae.getEntityClassName(), new BusinessLogicValidationException("not.found.message", [ae.getUserFriendlyName(),ae.wrappedException.id]))
        } else if (ae.getType() == "RuntimeException" && ae.wrappedException.message.startsWith("@@r1")) {
            String message = ae.wrappedException.message
            Map rcps = getResourceCodeAndParams(message)
            throw new ApplicationException(ae.getEntityClassName(), new BusinessLogicValidationException(rcps.resourceCode, rcps.bindingParams))
        } else {
            throw ae
        }
    }

    /**
     * This method sets the data origin if passed along with metadata in the payload.
     *
     * @param domainModel Domain object
     * @param map json payload
     */
    static void setDataOrigin(domainModel, map) {
        if (!domainModel) return
        if (map?.metadata && map?.metadata?.dataOrigin) {
            domainModel.dataOrigin = map?.metadata?.dataOrigin
        }
    }


    static Date convertString2Date(String strDate) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd")
        dateFormat.lenient = false

        Date date
        try {
            date = dateFormat.parse(strDate)
        } catch (ParseException pe) {
            throw new ApplicationException(GlobalUniqueIdentifierService.API, new BusinessLogicValidationException("date.invalid.format.message", ["BusinessLogicValidationException"]))
        }
        return date
    }


    public String getTimeInHHmmFormat(String time) {
        if (time) {
            if (time?.length() != getTimeFormat()?.length())
                throw new ApplicationException(GlobalUniqueIdentifierService.API, new BusinessLogicValidationException("time.invalid.format.message", ["BusinessLogicValidationException"]))

            String[] timesArray = time?.split(':')
            List patternList = getTimeFormat()?.toLowerCase().split(':') as List
            return timesArray[patternList.indexOf('hh')] + timesArray[patternList.indexOf('mm')]
        }
    }


    public String getTimeFormat() {
        return timeFormat ?: (timeFormat = MessageResolver.message("default.time.withSeconds.format"))
    }

    //Stolen from private methods in ApplicationException...
    private static Map getResourceCodeAndParams(String msg) {
        List<String> bindingParams = parse(extractAPIErrorText(msg))
        if (bindingParams.size() < 2) {
            return [resourceCode: "unknown.banner.api.exception"]
        }
        if (!bindingParams.get(0).equals("r1")) {
            return [resourceCode: "unknown.banner.api.exception"]
        }
        else {
            String resourceCode = bindingParams.get(1)
            bindingParams.remove(0)
            bindingParams.remove(0)

            [resourceCode: resourceCode, bindingParams: bindingParams]
        }
    }

    private static List<String> parse(String source) {
        List<String> values = new ArrayList<String>()
        String currentChar = null
        StringBuffer sb = new StringBuffer()
        boolean lastCharWasEscape = false
        for (int i = 0; i < source.length(); i++) {
            currentChar = source.substring(i, i + 1)
            if (lastCharWasEscape) {
                if (currentChar.equals("\\")) {
                    lastCharWasEscape = false
                    sb.append("\\")
                    currentChar = null
                } else if (currentChar.equals(":")) {
                    lastCharWasEscape = false
                    sb.append(":")
                    currentChar = null
                } else {
                    // didn't escape anything, discard
                    sb.append(currentChar)
                    lastCharWasEscape = false
                    currentChar = null
                }
            } else if (currentChar.equals("\\")) {
                lastCharWasEscape = true
            } else if (currentChar.equals(":")) {
                // field separater
                values.add(sb.toString())
                sb = new StringBuffer()
                currentChar = null
            } else {
                sb.append(currentChar)
                currentChar = null
            }
        }
        if (currentChar != null) {
            sb.append(currentChar)
        }
        values.add(sb.toString())
        return values
    }

    private static String extractAPIErrorText(String message) {
        if (message == null) {
            return message
        }
        String tmp = message.substring(message.indexOf("@@") + 2, message.length())
        int end = tmp.indexOf("@@")
        return tmp.substring(0, end)
    }
}