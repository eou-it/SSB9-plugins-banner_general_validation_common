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
import net.hedtech.banner.general.system.HoldType
import net.hedtech.banner.general.system.ldm.v1.Metadata
import net.hedtech.banner.general.system.ldm.v1.RestrictionType
import net.hedtech.banner.restfulapi.RestfulApiValidationUtility
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional


/**
 * Service used to support "restriction-types" resource for CDM
 */
@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
class RestrictionTypeCompositeService {

    def holdTypeService
    private static final String RESTRICTION_TYPE_LDM_NAME = 'restriction-types'

    List<RestrictionType> list(Map params) {
        List restrictionTypes = []
        List allowedSortFields = ['abbreviation', 'title']

        RestfulApiValidationUtility.correctMaxAndOffset(params, RestfulApiValidationUtility.MAX_DEFAULT, RestfulApiValidationUtility.MAX_UPPER_LIMIT)
        RestfulApiValidationUtility.validateSortField(params.sort, allowedSortFields)
        RestfulApiValidationUtility.validateSortOrder(params.order)

        params.sort = LdmService.fetchBannerDomainPropertyForLdmField(params.sort)
        List<HoldType> holdTypes = holdTypeService.list(params) as List
        holdTypes.each { holdType ->
            restrictionTypes << new RestrictionType(holdType, GlobalUniqueIdentifier.findByLdmNameAndDomainId(RESTRICTION_TYPE_LDM_NAME, holdType.id)?.guid, new Metadata(holdType.dataOrigin))
        }
        return restrictionTypes
    }


    Long count() {
        return HoldType.count()
    }


    RestrictionType get(String guid) {
        GlobalUniqueIdentifier globalUniqueIdentifier = GlobalUniqueIdentifier.fetchByLdmNameAndGuid(RESTRICTION_TYPE_LDM_NAME, guid)
        if (!globalUniqueIdentifier) {
            throw new ApplicationException(GlobalUniqueIdentifierService.API, new NotFoundException(id: GrailsNameUtils.getNaturalName(HoldType.class.simpleName)))
        }

        HoldType holdType = HoldType.get(globalUniqueIdentifier.domainId)
        if (!holdType) {
            throw new ApplicationException(GlobalUniqueIdentifierService.API, new NotFoundException(id: GrailsNameUtils.getNaturalName(HoldType.class.simpleName)))
        }

        return new RestrictionType(holdType, globalUniqueIdentifier.guid, new Metadata(holdType.dataOrigin));
    }


    RestrictionType fetchByRestrictionTypeId(Long holdTypeId) {
        if (null == holdTypeId) {
            return null
        }
        HoldType holdType = holdTypeService.get(holdTypeId) as HoldType
        if (!holdType) {
            return null
        }
        return new RestrictionType(holdType, GlobalUniqueIdentifier.findByLdmNameAndDomainId(RESTRICTION_TYPE_LDM_NAME, holdTypeId)?.guid, new Metadata(holdType.dataOrigin))
    }


    RestrictionType fetchByRestrictionTypeCode(String holdTypeCode) {
        RestrictionType restrictionType = null
        if (holdTypeCode) {
            HoldType holdType = HoldType.findByCode(holdTypeCode)
            if (!holdType) {
                return restrictionType
            }
            restrictionType = new RestrictionType(holdType, GlobalUniqueIdentifier.findByLdmNameAndDomainId(RESTRICTION_TYPE_LDM_NAME, holdType.id)?.guid, new Metadata(holdType.dataOrigin))
        }
        return restrictionType
    }

}
