/*********************************************************************************
 Copyright 2016 Ellucian Company L.P. and its affiliates.
 **********************************************************************************/
package net.hedtech.banner.general.system.ldm

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.exceptions.NotFoundException
import net.hedtech.banner.general.common.GeneralValidationCommonConstants
import net.hedtech.banner.general.overall.ldm.GlobalUniqueIdentifier
import net.hedtech.banner.general.overall.ldm.LdmService
import net.hedtech.banner.general.system.VisaType
import net.hedtech.banner.general.system.ldm.v4.VisaTypeDetail
import net.hedtech.banner.restfulapi.RestfulApiValidationUtility
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional

/**
 * Service used to support "visa-types" resource for HEDM
 */
@Transactional
class VisaTypeCompositeService extends LdmService {

    def visaTypeService
    private static final List<String> VERSIONS = [GeneralValidationCommonConstants.VERSION_V4]

    /**
     * GET /api/visa-types
     *
     * @param params Request parameters
     * @return
     */
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    List<VisaTypeDetail> list(Map params) {
        String acceptVersion = getAcceptVersion(VERSIONS)
        log.debug "list:Begin:$params"
        List<VisaTypeDetail> visaTypeDetailList = []
        RestfulApiValidationUtility.correctMaxAndOffset(params, RestfulApiValidationUtility.MAX_DEFAULT, RestfulApiValidationUtility.MAX_UPPER_LIMIT)

        List allowedSortFields = [GeneralValidationCommonConstants.CODE, GeneralValidationCommonConstants.TITLE]
        RestfulApiValidationUtility.validateSortField(params.sort, allowedSortFields)
        RestfulApiValidationUtility.validateSortOrder(params.order)
        params.sort = fetchBannerDomainPropertyForLdmField(params.sort)
        params.offset = params.offset ?: 0

        visaTypeService.fetchAllWithGuid(params.max as int, params.offset as int).each {
            VisaType visaType = it.visaType
            GlobalUniqueIdentifier globalUniqueIdentifier = it.globalUniqueIdentifier
            visaTypeDetailList << new VisaTypeDetail(visaType, getVisaTypeCategory(visaType.nonResIndicator), globalUniqueIdentifier.guid)
        }

        log.debug "list:End:${visaTypeDetailList?.size()}"
        return visaTypeDetailList
    }

    /**
     * GET /api/visa-types
     *
     * The count method must return the total number of instances of the resource.
     * It is used in conjunction with the list method when returning a list of resources.
     * RestfulApiController will make call to "count" only if the "list" execution happens without any exception.
     *
     * @param params Request parameters
     * @return
     */
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    Long count() {
        return visaTypeService.count()
    }

    /**
     * GET /api/visa-types/<id>
     *
     * @param id
     * @return
     */
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    VisaTypeDetail get(String guid) {
        String acceptVersion = getAcceptVersion(VERSIONS)
        log.debug "get:Begin:$guid"

        GlobalUniqueIdentifier globalUniqueIdentifier = GlobalUniqueIdentifier.fetchByLdmNameAndGuid(GeneralValidationCommonConstants.VISA_TYPES_LDM_NAME, guid)
        if (!globalUniqueIdentifier) {
            throw new ApplicationException("visaType", new NotFoundException())
        }

        VisaType visaType = visaTypeService.get(globalUniqueIdentifier.domainId)
        if (!visaType) {
            throw new ApplicationException("visaType", new NotFoundException())
        }

        log.debug "get:End"
        return new VisaTypeDetail(visaType, getVisaTypeCategory(visaType.nonResIndicator), globalUniqueIdentifier.guid)
    }


    def getVisaTypeCodeToGuidMap(Collection<String> codes) {
        Map<String, String> codeToGuidMap = [:]
        if (codes) {
            List entities = visaTypeService.fetchAllWithGuidByCodeInList(codes)
            entities.each {
                VisaType visaType = it.visaType
                GlobalUniqueIdentifier globalUniqueIdentifier = it.globalUniqueIdentifier
                codeToGuidMap.put(visaType.code, globalUniqueIdentifier.guid)
            }
        }
        return codeToGuidMap
    }

    public String getVisaTypeCategory(String nonResIndicator) {
        return nonResIndicator == "Y" ? GeneralValidationCommonConstants.NON_IMMIGRANT : GeneralValidationCommonConstants.IMMIGRANT
    }

}
