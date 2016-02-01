/*********************************************************************************
 Copyright 2016 Ellucian Company L.P. and its affiliates.
 **********************************************************************************/
package net.hedtech.banner.general.system.ldm

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.exceptions.BusinessLogicValidationException
import net.hedtech.banner.exceptions.NotFoundException
import net.hedtech.banner.general.common.GeneralValidationCommonConstants
import net.hedtech.banner.general.overall.ldm.GlobalUniqueIdentifier
import net.hedtech.banner.general.system.VisaType
import net.hedtech.banner.general.system.VisaTypeService
import net.hedtech.banner.general.overall.ldm.LdmService
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
    private static final String VISATYPE_LDM_NAME = 'visa-types'

    /**
     * GET /api/visa-types
     *
     * @param params Request parameters
     * @return
     */
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    List<VisaTypeDetail> list(Map params) {

        log.debug "list:Begin:$params"

        List visaTypeDetailList = []

        RestfulApiValidationUtility.correctMaxAndOffset(params, RestfulApiValidationUtility.MAX_DEFAULT, RestfulApiValidationUtility.MAX_UPPER_LIMIT)
        RestfulApiValidationUtility.validateSortField(params.sort, ['code', 'title'])
        RestfulApiValidationUtility.validateSortOrder(params.order)
        params.sort = fetchBannerDomainPropertyForLdmField(params.sort)

        List<VisaType> visaTypeList = visaTypeService.list(params) as List
        visaTypeList.each { visaTypeDetail ->
            visaTypeDetailList << getDecorator(visaTypeDetail)
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
        log.debug "count:Begin"
        int total

        total = VisaType.count()
        log.debug "count:End:$total"
        return total
    }

    /**
     * GET /api/visa-types/<id>
     *
     * @param id
     * @return
     */
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    def get(String guid) {
        log.debug "get:Begin:$guid"
        def result

        GlobalUniqueIdentifier globalUniqueIdentifier = GlobalUniqueIdentifier.fetchByLdmNameAndGuid(VISATYPE_LDM_NAME, guid)
        if (!globalUniqueIdentifier) {
            throw new ApplicationException("visaType", new NotFoundException())
        }

        VisaType visaType = VisaType.get(globalUniqueIdentifier.domainId)
        if (!visaType) {
            throw new ApplicationException("visaType", new NotFoundException())
        }

        result = getDecorator(visaType, globalUniqueIdentifier.guid)
        log.debug "get:End:$result"
        return result;
    }


    private def getDecorator(VisaType visaType, String visaTypeGuid = null) {
        VisaTypeDetail decorator
        if (visaType) {
            if (!visaTypeGuid) {
                visaTypeGuid = GlobalUniqueIdentifier.fetchByLdmNameAndDomainId(VISATYPE_LDM_NAME, visaType.id)?.guid
            }

            def category=visaType.nonResIndicator=="Y"?GeneralValidationCommonConstants.NON_IMMIGRANT:GeneralValidationCommonConstants.IMMIGRANT

            decorator = new VisaTypeDetail(visaType,category,visaTypeGuid)
        }
        return decorator
    }

}
