/*******************************************************************************
 Copyright 2014 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.general.overall.ldm

import grails.validation.ValidationException
import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.exceptions.BusinessLogicValidationException
import net.hedtech.banner.exceptions.NotFoundException
import net.hedtech.banner.general.overall.IntegrationConfiguration
import net.hedtech.banner.utility.MessageResolver
import org.grails.databinding.SimpleMapDataBindingSource
import org.springframework.web.context.request.RequestAttributes
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes

import javax.servlet.http.HttpServletRequest
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat

class LdmService {

    def sessionFactory
    def grailsWebDataBinder
    def globalUniqueIdentifierService

    String timeFormat = ''

    private static HashMap ldmFieldToBannerDomainPropertyMap = [
            abbreviation: 'code',
            title       : 'description',
            number      : 'roomNumber'
    ]

    private static List globalBindExcludes = ['id', 'version', 'dataOrigin']
    private static final String SETTING_INTEGRATION_PARTNER = "INTEGRATION.PARTNER"


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
        List<IntegrationConfiguration> integrationConfigs = IntegrationConfiguration.fetchAllByProcessCodeAndSettingNameAndTranslationValue(processCode, settingName, translationValue)
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
        List<IntegrationConfiguration> integrationConfigs = IntegrationConfiguration.fetchAllByProcessCodeAndSettingNameAndValue(processCode, settingName, value)
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
            throw new ApplicationException(ae.getEntityClassName(), new BusinessLogicValidationException("not.found.message", [ae.getUserFriendlyName(), ae.wrappedException.id]))
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
        } else {
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

    /**
     * Return "Accept" header
     *
     * @return
     */
    public static String getResponseRepresentation() {
        return responseBodyMediaType()
    }

    /**
     * RESTful APIs use custom media types (previously known as 'MIME types') for versioning.
     * HEDM APIs will have Accept headers like application/vnd.hedtech.integration.v1+json, application/vnd.hedtech.integration.v2+json so on.
     * Non-HEDM APIs will have Accept headers like application/vnd.hedtech.v1+json, application/vnd.hedtech.v2+json so on.
     *
     * Accept header can contain generic media types like application/vnd.hedtech.integration+json or application/json that
     * represent latest (current) version of the API.  In such cases, this method does not return anything.
     *
     * @return version (v1,v2 so on) extracted from Accept header
     */
    public static String getResponseRepresentationVersion() {
        String version
        String acceptHeader = responseBodyMediaType()
        if (acceptHeader) {
            int indexOfDotBeforeVersion = acceptHeader.lastIndexOf(".")
            int indexOfPlus = acceptHeader.lastIndexOf("+")
            if (indexOfDotBeforeVersion != -1 && indexOfPlus != -1 && indexOfDotBeforeVersion + 1 < indexOfPlus) {
                version = acceptHeader.substring(indexOfDotBeforeVersion + 1, indexOfPlus)
                if (!version?.toLowerCase()?.startsWith("v")) {
                    // May be generic Accept header like "application/vnd.hedtech.integration+json"
                    version = null
                }
            }
        }
        return version?.toLowerCase()
    }

    /**
     * Is RESTful API "Accept" header is for extended version (deep marshalling) like application/vnd.hedtech.integration.maximum.v1+json.
     * In this case, service has to return response with some of the associations expanded as specified by maximum schema.
     *
     * @return
     */
    public static boolean isMaximumResponseRepresentation() {
        boolean maxRep = false
        String acceptHeader = responseBodyMediaType()
        if (acceptHeader) {
            maxRep = acceptHeader.contains("maximum")
        }
        return maxRep
    }

    /**
     * Utility method used to decide which version flow should be executed for given Accept header.
     *
     * @param apiVersions List of HEDM versions supported by API
     * @return
     */
    public static String getAcceptVersion(List<String> apiVersions) {
        List<String> sortedApiVersions = apiVersions?.sort(false)
        String representationVersion = LdmService.getResponseRepresentationVersion()
        if (sortedApiVersions) {
            if (representationVersion == null || representationVersion > sortedApiVersions.last()) {
                // Assume latest (current) version
                representationVersion = sortedApiVersions.last()
            } else {
                int index = sortedApiVersions.findLastIndexOf { it <= representationVersion }
                if (index != -1) {
                    representationVersion = sortedApiVersions.get(index)
                }
            }
        }
        return representationVersion
    }

    /**
     * Returns "Content-Type" header
     *
     * @return
     */
    public static String getRequestRepresentation() {
        return requestBodyMediaType()
    }

    /**
     * HeDM APIs will have Content-Type headers like application/vnd.hedtech.integration.v1+json, application/vnd.hedtech.integration.room-availability.v1+json so on.
     *
     * Content-Type header can contain generic media types like application/vnd.hedtech.integration+json or application/vnd.hedtech.integration.room-availability+json or
     * application/json that represent latest (current) request version of the API.  In such cases, this method does not return anything.
     *
     * @return version (v1,v2 so on) extracted from Content-Type header
     */
    public static String getRequestRepresentationVersion() {
        String version
        String contentTypeHeader = requestBodyMediaType()
        if (contentTypeHeader) {
            int indexOfDotBeforeVersion = contentTypeHeader.lastIndexOf(".")
            int indexOfPlus = contentTypeHeader.lastIndexOf("+")
            if (indexOfDotBeforeVersion != -1 && indexOfPlus != -1 && indexOfDotBeforeVersion + 1 < indexOfPlus) {
                version = contentTypeHeader.substring(indexOfDotBeforeVersion + 1, indexOfPlus)
                if (!version?.toLowerCase()?.startsWith("v")) {
                    // May be generic Content-Type header like "application/vnd.hedtech.integration.room-availability+json"
                    version = null
                }
            }
        }
        return version?.toLowerCase()
    }

    /**
     * Utility method used to decide which version flow should be executed for given Content-Type header.
     *
     * @param apiVersions List of HEDM versions supported by API
     * @return
     */
    public static String getContentTypeVersion(List<String> apiVersions) {
        List<String> sortedApiVersions = apiVersions?.sort(false)
        String representationVersion = LdmService.getRequestRepresentationVersion()
        if (sortedApiVersions) {
            if (representationVersion == null || representationVersion > sortedApiVersions.last()) {
                // Assume latest (current) version
                representationVersion = sortedApiVersions.last()
            } else {
                int index = sortedApiVersions.findLastIndexOf { it <= representationVersion }
                if (index != -1) {
                    representationVersion = sortedApiVersions.get(index)
                }
            }
        }
        return representationVersion
    }


    private static String requestBodyMediaType() {
        HttpServletRequest request = getHttpServletRequest()
        return request?.getHeader("Content-Type")
    }


    private static String responseBodyMediaType() {
        HttpServletRequest request = getHttpServletRequest()
        return request?.getHeader("Accept")
    }


    public static HttpServletRequest getHttpServletRequest() {
        HttpServletRequest request
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes()
        if (requestAttributes && requestAttributes instanceof ServletRequestAttributes) {
            request = ((ServletRequestAttributes) requestAttributes).getRequest()
        }
        return request
    }

    /**
     * Used to bind map properties onto grails domains.
     * Can provide an exclusion and inclusion list in the third param.
     */
    public void bindData(def domain, Map properties, Map includeExcludeMap) {
        if (includeExcludeMap?.exclude instanceof List) {
            includeExcludeMap.exclude.addAll(globalBindExcludes)
        } else {
            includeExcludeMap.put('exclude', globalBindExcludes)
        }
        grailsWebDataBinder.bind(domain,
                properties as SimpleMapDataBindingSource,
                null,
                includeExcludeMap?.include,
                includeExcludeMap?.exclude,
                null)
        if (domain.hasErrors()) {
            throw new ApplicationException("${domain.class.simpleName}", new ValidationException("${domain.class.simpleName}", domain.errors))
        }
    }

    /**
     * Used to set the GUID to a specific value when update method
     * calls create.
     */
    private GlobalUniqueIdentifier updateGuidValue(def id, def guid, def ldmName) {
        // Update the GUID to the one we received.
        GlobalUniqueIdentifier newEntity = GlobalUniqueIdentifier.findByLdmNameAndDomainId(ldmName, id)
        if (!newEntity) {
            throw new ApplicationException(GlobalUniqueIdentifierService.API, new NotFoundException(id: ldmName))
        }
        newEntity.guid = guid
        globalUniqueIdentifierService.update(newEntity)
    }

    /**
     * This method will not perform well because of lack of keys for domain keys.
     *  Use the method above to use the faster indexed method.
     *  This is only used when there is no single master table for the GUID or no single record for the GUID.
     */
    private GlobalUniqueIdentifier updateGuidValueByDomainKey(String domainKey, String guid, String ldmName) {
        // Update the GUID to the one we received.
        GlobalUniqueIdentifier newEntity = GlobalUniqueIdentifier.findByLdmNameAndDomainKey(ldmName, domainKey)
        if (!newEntity) {
            throw new ApplicationException(GlobalUniqueIdentifierService.API, new NotFoundException(id: ldmName))
        }
        newEntity.guid = guid
        globalUniqueIdentifierService.update(newEntity)
    }

}
