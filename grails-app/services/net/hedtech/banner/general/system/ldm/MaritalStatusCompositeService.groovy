/*********************************************************************************
 Copyright 2014-2015 Ellucian Company L.P. and its affiliates.
 **********************************************************************************/
package net.hedtech.banner.general.system.ldm

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.exceptions.BusinessLogicValidationException
import net.hedtech.banner.exceptions.NotFoundException
import net.hedtech.banner.general.common.GeneralValidationCommonConstants
import net.hedtech.banner.general.overall.IntegrationConfiguration
import net.hedtech.banner.general.overall.ldm.GlobalUniqueIdentifier
import net.hedtech.banner.general.overall.ldm.LdmService
import net.hedtech.banner.general.system.MaritalStatus
import net.hedtech.banner.general.system.ldm.v1.MaritalStatusDetail
import net.hedtech.banner.general.system.ldm.v1.MaritalStatusParentCategory
import net.hedtech.banner.general.system.ldm.v1.Metadata
import net.hedtech.banner.general.system.ldm.v4.MaritalStatusMaritalCategory
import net.hedtech.banner.restfulapi.RestfulApiValidationUtility
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional


/**
 * RESTful APIs for HeDM marital-statuses
 */
@Transactional
class MaritalStatusCompositeService extends LdmService {

    def maritalStatusService
    private static final List<String> VERSIONS = [GeneralValidationCommonConstants.VERSION_V1,
                                                  GeneralValidationCommonConstants.VERSION_V4]

    /**
     * GET /api/marital-statuses
     *
     * @param params Request parameters
     * @return
     */
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    List<MaritalStatusDetail> list(Map params) {
        List maritalStatusDetailList = []
        def version = LdmService.getAcceptVersion(VERSIONS)
        RestfulApiValidationUtility.correctMaxAndOffset(params, RestfulApiValidationUtility.MAX_DEFAULT, RestfulApiValidationUtility.MAX_UPPER_LIMIT)

        List allowedSortFields = (GeneralValidationCommonConstants.VERSION_V4.equals(version)? [GeneralValidationCommonConstants.CODE, GeneralValidationCommonConstants.TITLE]:[GeneralValidationCommonConstants.ABBREVIATION, GeneralValidationCommonConstants.TITLE])
        RestfulApiValidationUtility.validateSortField(params.sort, allowedSortFields)
        RestfulApiValidationUtility.validateSortOrder(params.order)
        params.sort = fetchBannerDomainPropertyForLdmField(params.sort)
        if(GeneralValidationCommonConstants.VERSION_V4.equals(version)){
            maritalStatusService.fetchMartialStatusDetails(params).each {maritalStatus ->
                maritalStatusDetailList << getDecorator(maritalStatus[0],maritalStatus[1]?.translationValue)
            }
        } else if(GeneralValidationCommonConstants.VERSION_V1.equals(version)){
            List<MaritalStatus> maritalStatusList = maritalStatusService.list(params) as List
            maritalStatusList.each { maritalStatus ->
                maritalStatusDetailList << getDecorator(maritalStatus,null)
            }
        }

        return maritalStatusDetailList
    }

    /**
     * GET /api/marital-statuses
     *
     * The count method must return the total number of instances of the resource.
     * It is used in conjunction with the list method when returning a list of resources.
     * RestfulApiController will make call to "count" only if the "list" execution happens without any exception.
     *
     * @return
     */
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    Long count(Map params) {
        def version = LdmService.getAcceptVersion(VERSIONS)
        if(GeneralValidationCommonConstants.VERSION_V4.equals(version)){
            return  maritalStatusService.fetchMartialStatusDetails(params,true)
        }else  if(GeneralValidationCommonConstants.VERSION_V1.equals(version)) {
            return maritalStatusService.count()
        }
    }

    /**
     * GET /api/marital-statuses/<guid>
     *
     * @param guid
     * @return
     */
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    MaritalStatusDetail get(String guid) {
        GlobalUniqueIdentifier globalUniqueIdentifier = globalUniqueIdentifierService.fetchByLdmNameAndGuid(GeneralValidationCommonConstants.MARITAL_STATUS_LDM_NAME, guid)
        if (!globalUniqueIdentifier) {
            throw new ApplicationException(GeneralValidationCommonConstants.MARITAL_STATUS, new NotFoundException())
        }

        MaritalStatus maritalStatus = maritalStatusService.get(globalUniqueIdentifier.domainId)
        if (!maritalStatus) {
            throw new ApplicationException(GeneralValidationCommonConstants.MARITAL_STATUS, new NotFoundException())
        }
       def maritalCategory = getHeDMEnumeration(maritalStatus?.code)
        if(GeneralValidationCommonConstants.VERSION_V4.equals(LdmService.getAcceptVersion(VERSIONS)) && !maritalCategory ){
            throw new ApplicationException(GeneralValidationCommonConstants.MARITAL_STATUS, new NotFoundException())
        }
        return getDecorator(maritalStatus, globalUniqueIdentifier.guid,maritalCategory)
    }

    /**
     * POST /api/marital-statuses
     *
     * @param content Request body
     */
    def create(content) {
        def version = LdmService.getAcceptVersion(VERSIONS)
        validateRequest(content,version)
        MaritalStatus maritalStatus = maritalStatusService.fetchByCode(content.code?.trim())
        if (maritalStatus) {
            def messageCode = GeneralValidationCommonConstants.VERSION_V4.equals(version) ? GeneralValidationCommonConstants.ERROR_MSG_CODE_EXISTS : GeneralValidationCommonConstants.ERROR_MSG_EXISTS_MESSAGE
            throw new ApplicationException(GeneralValidationCommonConstants.MARITAL_STATUS, new BusinessLogicValidationException(messageCode, null))
        }

        maritalStatus = bindMaritalStatus(new MaritalStatus(), content)

        String msGuid = content.guid?.trim()?.toLowerCase()
        if (msGuid) {
            // Overwrite the GUID created by DB insert trigger, with the one provided in the request body
            updateGuidValue(maritalStatus.id, msGuid, GeneralValidationCommonConstants.MARITAL_STATUS_LDM_NAME)
        } else {
            msGuid = globalUniqueIdentifierService.fetchByLdmNameAndDomainId(GeneralValidationCommonConstants.MARITAL_STATUS_LDM_NAME, maritalStatus.id)?.guid
        }
        log.debug("GUID: ${msGuid}")
        def maritalCategory = GeneralValidationCommonConstants.VERSION_V4.equals(version) ? content.maritalCategory : content.parentCategory
        return getDecorator(maritalStatus, msGuid,maritalCategory)
    }

    /**
     * PUT /api/marital-statuses/<guid>
     *
     * @param content Request body
     */
    def update(content) {
        String msGuid = content.id?.trim()?.toLowerCase()
        if(!msGuid){
            throw new ApplicationException(GeneralValidationCommonConstants.MARITAL_STATUS, new NotFoundException())
        }

        GlobalUniqueIdentifier globalUniqueIdentifier = globalUniqueIdentifierService.fetchByLdmNameAndGuid(GeneralValidationCommonConstants.MARITAL_STATUS_LDM_NAME, msGuid)
            if (!globalUniqueIdentifier) {
                if (!content.guid) {
                    content.guid = msGuid
                }
                //Per strategy when a GUID was provided, the create should happen.
                return create(content)
            }

        MaritalStatus maritalStatus = maritalStatusService.get(globalUniqueIdentifier.domainId)
        if (!maritalStatus) {
            throw new ApplicationException(GeneralValidationCommonConstants.MARITAL_STATUS, new NotFoundException())
        }

        // Should not allow to update maritalStatus.code as it is read-only
        if (maritalStatus.code != content.code?.trim()) {
            content.code = maritalStatus.code
        }
        validateRequest(content,LdmService.getAcceptVersion(VERSIONS))
        maritalStatus = bindMaritalStatus(maritalStatus, content)
        return getDecorator(maritalStatus, msGuid,getHeDMEnumeration(maritalStatus.code) )
    }


    MaritalStatusDetail fetchByMaritalStatusId(Long maritalStatusId) {
        if (null == maritalStatusId) {
            return null
        }
        MaritalStatus maritalStatus = maritalStatusService.get(maritalStatusId) as MaritalStatus
        return getDecorator(maritalStatus,null)
    }


    MaritalStatusDetail fetchByMaritalStatusCode(String maritalStatusCode) {
        MaritalStatusDetail maritalStatusDetail = null
        if (maritalStatusCode) {
            MaritalStatus maritalStatus = MaritalStatus.findByCode(maritalStatusCode)
            if (!maritalStatus) {
                return maritalStatusDetail
            }
            maritalStatusDetail = getDecorator(maritalStatus,null)
        }
        return maritalStatusDetail
    }


    private void validateRequest(content,version) {
        if (!content.code) {
            def parameterValue = GeneralValidationCommonConstants.VERSION_V4.equals(version) ? GeneralValidationCommonConstants.CODE.capitalize() : GeneralValidationCommonConstants.ABBREVIATION.capitalize()
            throw new ApplicationException(GeneralValidationCommonConstants.MARITAL_STATUS, new BusinessLogicValidationException(GeneralValidationCommonConstants.ERROR_MSG_CODE_REQUIRED,[parameterValue]))
        }
        if (!content.description) {
            throw new ApplicationException(GeneralValidationCommonConstants.MARITAL_STATUS, new BusinessLogicValidationException(GeneralValidationCommonConstants.ERROR_MSG_DESCRIPTION_REQUIRED, null))
        }
    }


    def bindMaritalStatus(MaritalStatus maritalStatus, Map content) {
        setDataOrigin(maritalStatus, content)
        bindData(maritalStatus, content, [:])
        if (content.parentCategory) {
            // No domain field to store.  So not useful for create and update operations
        }
        maritalStatus.financeConversion = maritalStatus.financeConversion ?: "1"
        maritalStatusService.createOrUpdate(maritalStatus)
    }


    private MaritalStatusDetail getDecorator(MaritalStatus maritalStatus, String msGuid = null,String martialStatusCategory) {
        MaritalStatusDetail decorator
        if (maritalStatus) {
            if (!msGuid) {
                msGuid = globalUniqueIdentifierService.fetchByLdmNameAndDomainId(GeneralValidationCommonConstants.MARITAL_STATUS_LDM_NAME, maritalStatus.id)?.guid
            }
            if(!martialStatusCategory){
                martialStatusCategory =  getHeDMEnumeration(maritalStatus.code)
            }
            decorator = new MaritalStatusDetail(maritalStatus, msGuid, martialStatusCategory, new Metadata(maritalStatus.dataOrigin))
        }
        return decorator
    }

    /**
     * STVMRTL_CODE -> HeDM enumeration (Mapping)(in LIST/GET operations)
     *
     * @param maritalStatusCode STVMRTL_CODE
     * @return
     */
    String getHeDMEnumeration(String maritalStatusCode) {
        String hedmEnum
        if (maritalStatusCode) {
            def version = LdmService.getAcceptVersion(VERSIONS)
            def maritalStatusCategory = GeneralValidationCommonConstants.VERSION_V4.equals(version) ? GeneralValidationCommonConstants.MARITAL_STATUS_MARTIAL_CATEGORY : GeneralValidationCommonConstants.MARITAL_STATUS_PARENT_CATEGORY
            IntegrationConfiguration intgConf = IntegrationConfiguration.fetchByProcessCodeAndSettingNameAndValue(GeneralValidationCommonConstants.PROCESS_CODE, maritalStatusCategory, maritalStatusCode)
            log.debug "Value ${intgConf?.value} - TranslationValue ${intgConf?.translationValue}"
            if(GeneralValidationCommonConstants.VERSION_V4.equals(version)){
                hedmEnum = MaritalStatusMaritalCategory.MARITAL_STATUS_MARTIAL_CATEGORY.contains(intgConf?.translationValue) ? intgConf?.translationValue : null
            }else  if(GeneralValidationCommonConstants.VERSION_V1.equals(version)){
                hedmEnum = MaritalStatusParentCategory.MARITAL_STATUS_PARENT_CATEGORY.contains(intgConf?.translationValue) ? intgConf?.translationValue : null
            }
        }
        return hedmEnum
    }


}
