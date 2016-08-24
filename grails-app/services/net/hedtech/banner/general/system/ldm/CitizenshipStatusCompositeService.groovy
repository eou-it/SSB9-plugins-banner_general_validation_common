/** *******************************************************************************
 Copyright 2016 Ellucian Company L.P. and its affiliates.
 ********************************************************************************* */
package net.hedtech.banner.general.system.ldm

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.exceptions.NotFoundException
import net.hedtech.banner.general.common.GeneralValidationCommonConstants
import net.hedtech.banner.general.overall.ldm.GlobalUniqueIdentifier
import net.hedtech.banner.general.overall.ldm.LdmService
import net.hedtech.banner.general.system.CitizenType
import net.hedtech.banner.general.system.ldm.v4.CitizenshipStatus
import net.hedtech.banner.restfulapi.RestfulApiValidationUtility
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional

/**
 * RESTful APIs for HeDM citizenship-statuses
 */
@Transactional
class CitizenshipStatusCompositeService extends LdmService {

    private static final List<String> VERSIONS = [GeneralValidationCommonConstants.VERSION_V4]

    def citizenTypeService

    /**
     * GET /api/citizenship-statuses
     *
     * @param params Request parameters
     * @return
     */
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    List<CitizenshipStatus> list(Map params) {
        log.debug "list:Begin:$params"
        String acceptVersion = getAcceptVersion(VERSIONS)
        List<CitizenshipStatus> citizenshipStatuses = []
        RestfulApiValidationUtility.correctMaxAndOffset(params, RestfulApiValidationUtility.MAX_DEFAULT, RestfulApiValidationUtility.MAX_UPPER_LIMIT)

        List allowedSortFields = [GeneralValidationCommonConstants.CODE, GeneralValidationCommonConstants.TITLE]
        RestfulApiValidationUtility.validateSortField(params.sort, allowedSortFields)
        RestfulApiValidationUtility.validateSortOrder(params.order)
        params.sort = fetchBannerDomainPropertyForLdmField(params.sort)
        List<CitizenType> citizenTypes = citizenTypeService.list(params) as List

        citizenTypes?.each { citizenType ->
            citizenshipStatuses << getDecorator(citizenType)
        }

        log.debug "list:End:${citizenshipStatuses?.size()}"
        return citizenshipStatuses
    }

    /**
     * GET /api/citizenship-statuses
     *
     * The count method must return the total number of instances of the resource.
     * It is used in conjunction with the list method when returning a list of resources.
     * RestfulApiController will make call to "count" only if the "list" execution happens without any exception.
     *
     * @return
     */
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    Long count() {
        return citizenTypeService.count()
    }

    /**
     * GET /api/citizenship-statuses/<guid>
     *
     * @param guid
     * @return
     */
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    CitizenshipStatus get(String guid) {
        log.debug "get:Begin:$guid"
        String acceptVersion = getAcceptVersion(VERSIONS)
        GlobalUniqueIdentifier globalUniqueIdentifier = globalUniqueIdentifierService.fetchByLdmNameAndGuid(GeneralValidationCommonConstants.CITIZENSHIP_STATUSES_LDM_NAME, guid)
        if (!globalUniqueIdentifier) {
            throw new ApplicationException(GeneralValidationCommonConstants.CITIZENSHIP_STATUS, new NotFoundException())
        }

        CitizenType citizenType = citizenTypeService.get(globalUniqueIdentifier.domainId)
        if (!citizenType) {
            throw new ApplicationException(GeneralValidationCommonConstants.CITIZENSHIP_STATUS, new NotFoundException())
        }
        log.debug("guid: ${globalUniqueIdentifier.guid}")
        return getDecorator(citizenType, globalUniqueIdentifier.guid)
    }


    private CitizenshipStatus getDecorator(CitizenType citizenType, String guid = null) {
        if (!guid) {
            guid = globalUniqueIdentifierService.fetchByLdmNameAndDomainId(GeneralValidationCommonConstants.CITIZENSHIP_STATUSES_LDM_NAME, citizenType.id)?.guid
        }

        return new CitizenshipStatus(citizenType, guid, getCitizenshipStatusCategory(citizenType.citizenIndicator ?: null))
    }

    public Map fetchGUIDs(List<String> citizenCodes){
        Map content=[:]
        def result
        String hql='''select a.code, b.guid from CitizenType a, GlobalUniqueIdentifier b WHERE a.code in :citizenCodes and b.ldmName = :ldmName and a.code = b.domainKey'''
        CitizenType.withSession { session ->
            def query = session.createQuery(hql).setString('ldmName', 'citizenship-statuses')
            query.setParameterList('citizenCodes', citizenCodes)
            result = query.list()
        }
        result.each { citizenType ->
            content.put(citizenType[0], citizenType[1])
        }
        return content
    }


    public String getCitizenshipStatusCategory(Boolean citizenIndicator) {
        String category
        if (citizenIndicator) {
            category = GeneralValidationCommonConstants.CITIZENSHIP_STATUSES_CATEGORY_CITIZEN
        } else {
            category = GeneralValidationCommonConstants.CITIZENSHIP_STATUSES_CATEGORY_NON_CITIZEN
        }
        return category
    }

}
