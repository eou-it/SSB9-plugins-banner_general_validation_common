/*********************************************************************************
 Copyright 2014 Ellucian Company L.P. and its affiliates.
 **********************************************************************************/
package net.hedtech.banner.general.system.ldm

import grails.util.GrailsNameUtils
import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.exceptions.NotFoundException
import net.hedtech.banner.general.overall.ldm.GlobalUniqueIdentifier
import net.hedtech.banner.general.overall.ldm.GlobalUniqueIdentifierService
import net.hedtech.banner.general.overall.ldm.LdmService
import net.hedtech.banner.general.system.MaritalStatus
import net.hedtech.banner.general.system.ldm.v1.MaritalStatusDetail
import net.hedtech.banner.general.system.ldm.v1.MaritalStatusParentCategory
import net.hedtech.banner.restfulapi.RestfulApiValidationUtility
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional


/**
 * Service used to support "marital-status" resource for LDM media types
 */
@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
class MaritalStatusCompositeService {

    def maritalStatusService
    private static final String MARITAL_STATUS_LDM_NAME = 'marital-status'

    List<MaritalStatusDetail> list(Map params) {
        List maritalStatusDetailList = []
        List allowedSortFields = ['abbreviation', 'title']

        RestfulApiValidationUtility.correctMaxAndOffset(params, RestfulApiValidationUtility.MAX_DEFAULT, RestfulApiValidationUtility.MAX_UPPER_LIMIT)
        RestfulApiValidationUtility.validateSortField(params.sort, allowedSortFields)
        RestfulApiValidationUtility.validateSortOrder(params.order)

        params.sort = LdmService.fetchBannerDomainPropertyForLdmField(params.sort)
        List<MaritalStatus> maritalStatusList = maritalStatusService.list(params) as List
        maritalStatusList.each { maritalStatus ->
            maritalStatusDetailList << new MaritalStatusDetail(maritalStatus, GlobalUniqueIdentifier.findByLdmNameAndDomainId(MARITAL_STATUS_LDM_NAME, maritalStatus.id)?.guid, getLdmMaritalStatus(maritalStatus.code))
        }
        return maritalStatusDetailList
    }


    Long count() {
        return MaritalStatus.count()
    }


    @Transactional(readOnly = true)
    MaritalStatusDetail get(String guid) {
        GlobalUniqueIdentifier globalUniqueIdentifier = GlobalUniqueIdentifier.fetchByLdmNameAndGuid(MARITAL_STATUS_LDM_NAME, guid)
        if (!globalUniqueIdentifier) {
            throw new ApplicationException(GlobalUniqueIdentifierService.API, new NotFoundException(id: GrailsNameUtils.getNaturalName(MaritalStatus.class.simpleName)))
        }

        MaritalStatus maritalStatus = MaritalStatus.get(globalUniqueIdentifier.domainId)
        if (!maritalStatus) {
            throw new ApplicationException(GlobalUniqueIdentifierService.API, new NotFoundException(id: GrailsNameUtils.getNaturalName(MaritalStatus.class.simpleName)))
        }

        return new MaritalStatusDetail(maritalStatus, globalUniqueIdentifier.guid, getLdmMaritalStatus(maritalStatus.code));
    }


    MaritalStatusDetail fetchByMaritalStatusId(Long maritalStatusId) {
        MaritalStatus maritalStatus = maritalStatusService.get(maritalStatusId) as MaritalStatus
        return new MaritalStatusDetail(maritalStatus, GlobalUniqueIdentifier.findByLdmNameAndDomainId(MARITAL_STATUS_LDM_NAME, maritalStatusId)?.guid, getLdmMaritalStatus(maritalStatus.code))
    }


    MaritalStatusDetail fetchByMaritalStatusCode(String maritalStatusCode) {
        MaritalStatusDetail maritalStatusDetail = null
        if (maritalStatusCode) {
            MaritalStatus maritalStatus = MaritalStatus.findByCode(maritalStatusCode)
            if (!maritalStatus) {
                return maritalStatusDetail
            }
            maritalStatusDetail = new MaritalStatusDetail(maritalStatus, GlobalUniqueIdentifier.findByLdmNameAndDomainId(MARITAL_STATUS_LDM_NAME, maritalStatus.id)?.guid, getLdmMaritalStatus(maritalStatus.code))
        }
        return maritalStatusDetail
    }

    //TODO: Move this function to common place
    // Return LDM enumeration value for this marital status code.
    def getLdmMaritalStatus(def maritalStatus) {
        if (maritalStatus != null) {
            switch (maritalStatus) {
                case "S":
                    return MaritalStatusParentCategory.SINGLE.value
                case "M":
                    return MaritalStatusParentCategory.MARRIED.value
                case "D":
                    return MaritalStatusParentCategory.DIVORCED.value
                case "W":
                    return MaritalStatusParentCategory.WIDOWED.value
                case "P":
                    return MaritalStatusParentCategory.SEPARATED.value
                case "R":
                    return MaritalStatusParentCategory.MARRIED.value
                default:
                    MaritalStatusCompositeService.log.warn "Banner marital status code ${maritalStatus} not found."
            }
        }
        return null
    }
}
