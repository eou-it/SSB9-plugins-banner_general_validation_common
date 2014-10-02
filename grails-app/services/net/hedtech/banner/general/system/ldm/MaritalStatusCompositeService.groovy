/*********************************************************************************
 Copyright 2014 Ellucian Company L.P. and its affiliates.
 **********************************************************************************/
package net.hedtech.banner.general.system.ldm

import grails.util.GrailsNameUtils
import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.exceptions.NotFoundException
import net.hedtech.banner.general.overall.IntegrationConfiguration
import net.hedtech.banner.general.overall.ldm.GlobalUniqueIdentifier
import net.hedtech.banner.general.overall.ldm.GlobalUniqueIdentifierService
import net.hedtech.banner.general.overall.ldm.LdmService
import net.hedtech.banner.general.system.MaritalStatus
import net.hedtech.banner.general.system.ldm.v1.MaritalStatusDetail
import net.hedtech.banner.general.system.ldm.v1.MaritalStatusParentCategory
import net.hedtech.banner.general.system.ldm.v1.Metadata
import net.hedtech.banner.restfulapi.RestfulApiValidationUtility
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional


/**
 * Service used to support "marital-status" resource for LDM media types
 */
@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
class MaritalStatusCompositeService extends LdmService {

    def maritalStatusService
    private static final String MARITAL_STATUS_LDM_NAME = 'marital-status'
    static final String PROCESS_CODE = "LDM"
    static final String MARITAL_STATUS_PARENT_CATEGORY = "MARITALSTATUS.PARENTCATEGORY"

    List<MaritalStatusDetail> list(Map params) {
        List maritalStatusDetailList = []
        List allowedSortFields = ['abbreviation', 'title']

        RestfulApiValidationUtility.correctMaxAndOffset(params, RestfulApiValidationUtility.MAX_DEFAULT, RestfulApiValidationUtility.MAX_UPPER_LIMIT)
        RestfulApiValidationUtility.validateSortField(params.sort, allowedSortFields)
        RestfulApiValidationUtility.validateSortOrder(params.order)

        params.sort = LdmService.fetchBannerDomainPropertyForLdmField(params.sort)
        List<MaritalStatus> maritalStatusList = maritalStatusService.list(params) as List
        maritalStatusList.each { maritalStatus ->
            maritalStatusDetailList << new MaritalStatusDetail(maritalStatus, GlobalUniqueIdentifier.findByLdmNameAndDomainId(MARITAL_STATUS_LDM_NAME, maritalStatus.id)?.guid, getLdmMaritalStatus(maritalStatus.code), new Metadata(maritalStatus.dataOrigin))
        }
        return maritalStatusDetailList
    }


    Long count() {
        return MaritalStatus.count()
    }


    MaritalStatusDetail get(String guid) {
        GlobalUniqueIdentifier globalUniqueIdentifier = GlobalUniqueIdentifier.fetchByLdmNameAndGuid(MARITAL_STATUS_LDM_NAME, guid)
        if (!globalUniqueIdentifier) {
            throw new ApplicationException(GlobalUniqueIdentifierService.API, new NotFoundException(id: GrailsNameUtils.getNaturalName(MaritalStatus.class.simpleName)))
        }

        MaritalStatus maritalStatus = MaritalStatus.get(globalUniqueIdentifier.domainId)
        if (!maritalStatus) {
            throw new ApplicationException(GlobalUniqueIdentifierService.API, new NotFoundException(id: GrailsNameUtils.getNaturalName(MaritalStatus.class.simpleName)))
        }

        return new MaritalStatusDetail(maritalStatus, globalUniqueIdentifier.guid, getLdmMaritalStatus(maritalStatus.code), new Metadata(maritalStatus.dataOrigin));
    }


    MaritalStatusDetail fetchByMaritalStatusId(Long maritalStatusId) {
        if (null == maritalStatusId) {
            return null
        }
        MaritalStatus maritalStatus = maritalStatusService.get(maritalStatusId) as MaritalStatus
        if (!maritalStatus) {
            return null
        }
        return new MaritalStatusDetail(maritalStatus, GlobalUniqueIdentifier.findByLdmNameAndDomainId(MARITAL_STATUS_LDM_NAME, maritalStatusId)?.guid, getLdmMaritalStatus(maritalStatus.code), new Metadata(maritalStatus.dataOrigin))
    }


    MaritalStatusDetail fetchByMaritalStatusCode(String maritalStatusCode) {
        MaritalStatusDetail maritalStatusDetail = null
        if (maritalStatusCode) {
            MaritalStatus maritalStatus = MaritalStatus.findByCode(maritalStatusCode)
            if (!maritalStatus) {
                return maritalStatusDetail
            }
            maritalStatusDetail = new MaritalStatusDetail(maritalStatus, GlobalUniqueIdentifier.findByLdmNameAndDomainId(MARITAL_STATUS_LDM_NAME, maritalStatus.id)?.guid, getLdmMaritalStatus(maritalStatus.code), new Metadata(maritalStatus.dataOrigin))
        }
        return maritalStatusDetail
    }

    def getLdmMaritalStatus(def maritalStatus) {
        if (maritalStatus != null) {
            IntegrationConfiguration rule = findAllByProcessCodeAndSettingNameAndValue(PROCESS_CODE, MARITAL_STATUS_PARENT_CATEGORY, maritalStatus)
            return MaritalStatusParentCategory.MARITAL_STATUS_PARENT_CATEGORY.contains(rule?.translationValue) ? rule?.translationValue : null
        }
        return null
    }
}
