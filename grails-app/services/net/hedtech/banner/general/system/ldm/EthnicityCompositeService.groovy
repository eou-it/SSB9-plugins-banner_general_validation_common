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
import net.hedtech.banner.general.system.Ethnicity
import net.hedtech.banner.general.system.ldm.v1.EthnicityDetail
import net.hedtech.banner.general.system.ldm.v1.EthnicityParentCategory
import net.hedtech.banner.restfulapi.RestfulApiValidationUtility
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional


/**
 * Service used to support "ethnicities" resource for CDM
 */
@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
class EthnicityCompositeService {

    def ethnicityService
    private static final String ETHNICITY_LDM_NAME = 'ethnicities'

    List<EthnicityDetail> list(Map params) {
        List ethnicityDetailList = []
        List allowedSortFields = ['abbreviation', 'title']

        RestfulApiValidationUtility.correctMaxAndOffset(params, RestfulApiValidationUtility.MAX_DEFAULT, RestfulApiValidationUtility.MAX_UPPER_LIMIT)
        RestfulApiValidationUtility.validateSortField(params.sort, allowedSortFields)
        RestfulApiValidationUtility.validateSortOrder(params.order)

        params.sort = LdmService.fetchBannerDomainPropertyForLdmField(params.sort)
        List<Ethnicity> ethnicityList = ethnicityService.list(params) as List
        ethnicityList.each { ethnicity ->
            ethnicityDetailList << new EthnicityDetail(ethnicity, GlobalUniqueIdentifier.findByLdmNameAndDomainId(ETHNICITY_LDM_NAME, ethnicity.id)?.guid, getLdmEthnicity(ethnicity.code))
        }
        return ethnicityDetailList
    }


    Long count() {
        return Ethnicity.count()
    }


    EthnicityDetail get(String guid) {
        GlobalUniqueIdentifier globalUniqueIdentifier = GlobalUniqueIdentifier.fetchByLdmNameAndGuid(ETHNICITY_LDM_NAME, guid)
        if (!globalUniqueIdentifier) {
            throw new ApplicationException(GlobalUniqueIdentifierService.API, new NotFoundException(id: GrailsNameUtils.getNaturalName(Ethnicity.class.simpleName)))
        }

        Ethnicity ethnicity = Ethnicity.get(globalUniqueIdentifier.domainId)
        if (!ethnicity) {
            throw new ApplicationException(GlobalUniqueIdentifierService.API, new NotFoundException(id: GrailsNameUtils.getNaturalName(Ethnicity.class.simpleName)))
        }

        return new EthnicityDetail(ethnicity, globalUniqueIdentifier.guid, getLdmEthnicity(ethnicity.code));
    }


    EthnicityDetail fetchByEthnicityId(Long ethnicityId) {
        Ethnicity ethnicity = ethnicityService.get(ethnicityId) as Ethnicity
        return new EthnicityDetail(ethnicity, GlobalUniqueIdentifier.findByLdmNameAndDomainId(ETHNICITY_LDM_NAME, ethnicityId)?.guid, getLdmEthnicity(ethnicity.code))
    }


    EthnicityDetail fetchByEthnicityCode(String ethnicityCode) {
        EthnicityDetail ethnicityDetail = null
        if (ethnicityCode) {
            Ethnicity ethnicity = Ethnicity.findByCode(ethnicityCode)
            if (!ethnicity) {
                return ethnicityDetail
            }
            ethnicityDetail = new EthnicityDetail(ethnicity, GlobalUniqueIdentifier.findByLdmNameAndDomainId(ETHNICITY_LDM_NAME, ethnicity.id)?.guid, getLdmEthnicity(ethnicity.code))
        }
        return ethnicityDetail
    }

    //TODO: Move this function to common place
    // Return LDM enumeration value for the corresponding ethnicity code.
    def getLdmEthnicity(def ethnicity) {
        if (ethnicity != null) {
            return EthnicityParentCategory.HISPANIC.value
        }
        return null
    }

}
