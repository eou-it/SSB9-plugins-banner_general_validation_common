/** *******************************************************************************
 Copyright 2015-2016 Ellucian Company L.P. and its affiliates.
 ********************************************************************************* */
package net.hedtech.banner.general.system.ldm

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.exceptions.NotFoundException
import net.hedtech.banner.general.common.GeneralValidationCommonConstants
import net.hedtech.banner.general.overall.ldm.GlobalUniqueIdentifier
import net.hedtech.banner.general.overall.ldm.LdmService
import net.hedtech.banner.general.system.Religion
import net.hedtech.banner.general.system.ldm.v6.ReligionDecorator
import net.hedtech.banner.restfulapi.RestfulApiValidationUtility
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional

/**
 * Service used to support "religions" resource
 */
@Transactional
class ReligionCompositeService extends LdmService {

    def religionService
    private static final List<String> VERSIONS = [GeneralValidationCommonConstants.VERSION_V6]

    /**
     * GET /api/religions
     *
     * @return lists of religions
     */
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    List<ReligionDecorator> list(Map params) {
        log.debug "list:Begin:$params"
        String acceptVersion = getAcceptVersion(VERSIONS)

        List religionsList = []
        RestfulApiValidationUtility.correctMaxAndOffset(params, RestfulApiValidationUtility.MAX_DEFAULT, RestfulApiValidationUtility.MAX_UPPER_LIMIT)
        params.offset = params?.offset ?: 0
        List<ReligionDecorator> religionList = religionService.list(params)
        religionList.each { religion ->
            religionsList << getDecorator(religion)
        }
        log.debug "list:End:${religionsList?.size()}"
        return religionsList
    }

/**
 * @return total count for religions
 * */
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    Long count() {
        return religionService.count()
    }


    private ReligionDecorator getDecorator(Religion religion, String religionGuid = null) {
        if (!religionGuid) {
            religionGuid = globalUniqueIdentifierService.fetchByLdmNameAndDomainId(GeneralValidationCommonConstants.RELIGION_LDM_NAME, religion.id)?.guid
        }
        return new ReligionDecorator(religionGuid, religion.code, religion.description)
    }

    /**
     * GET /api/religions/<GUID>
     *
     * @return religion for the provided GUID
     */
    @Transactional(readOnly = true)
    ReligionDecorator get(String guid) {
        String acceptVersion = getAcceptVersion(VERSIONS)

        GlobalUniqueIdentifier globalUniqueIdentifier = globalUniqueIdentifierService.fetchByLdmNameAndGuid(GeneralValidationCommonConstants.RELIGION_LDM_NAME, guid?.toLowerCase()?.trim())
        if (!globalUniqueIdentifier) {
            throw new ApplicationException(ReligionCompositeService.class, new NotFoundException())
        }
        Religion religionObj = religionService.get(globalUniqueIdentifier.domainId)
        if (!religionObj) {
            throw new ApplicationException(ReligionCompositeService.class, new NotFoundException())
        }
        return getDecorator(religionObj, globalUniqueIdentifier.guid)
    }

}
