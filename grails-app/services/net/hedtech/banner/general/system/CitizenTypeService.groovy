package net.hedtech.banner.general.system

import net.hedtech.banner.general.common.GeneralValidationCommonConstants
import net.hedtech.banner.general.overall.ldm.GlobalUniqueIdentifier
import net.hedtech.banner.general.overall.ldm.GlobalUniqueIdentifierService
import net.hedtech.banner.service.ServiceBase

class CitizenTypeService extends ServiceBase {

    GlobalUniqueIdentifierService globalUniqueIdentifierService

    boolean transactional = true

    public def fetchByGuid(String guid){
        CitizenType citizenType
        GlobalUniqueIdentifier globalUniqueIdentifier = globalUniqueIdentifierService.fetchByLdmNameAndGuid(GeneralValidationCommonConstants.CITIZENSHIP_STATUSES_LDM_NAME, guid?.toLowerCase()?.trim())
        if(globalUniqueIdentifier){
            citizenType = CitizenType.findByCode(globalUniqueIdentifier.domainKey)
        }
        return citizenType
    }

    public def fetchByCitizenIndicator(Boolean CitizenIndicator) {
        return CitizenType.findByCitizenIndicator(CitizenIndicator)
    }


}
