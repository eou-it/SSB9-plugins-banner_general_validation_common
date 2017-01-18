/*********************************************************************************
 Copyright 2017 Ellucian Company L.P. and its affiliates.
 **********************************************************************************/
package net.hedtech.banner.general.system.ldm

import net.hedtech.banner.general.common.GeneralValidationCommonConstants
import net.hedtech.banner.general.overall.ldm.GlobalUniqueIdentifier
import net.hedtech.banner.general.overall.ldm.GlobalUniqueIdentifierService

/**
 * Ethnic code defined by the U.S. government. The valid values are 1 - Not Hispanic or Latino, 2 - Hispanic or Latino.
 * Possible values for database column SPBPERS_ETHN_CDE.
 *
 */
class UsEthnicCodeService {

    boolean transactional = true

    GlobalUniqueIdentifierService globalUniqueIdentifierService

    /**
     * SELECT * FROM GORGUID WHERE GORGUID_LDM_NAME = 'ethnicities-us'
     *
     * @param sortField
     * @param sortOrder
     * @param max
     * @param offset
     * @return
     */
    def fetchAll(String sortField = null, String sortOrder = null, int max = 0, int offset = -1) {
        Collection<GlobalUniqueIdentifier> entities = globalUniqueIdentifierService.fetchAllByLdmName(GeneralValidationCommonConstants.ETHNICITIES_US, sortField, sortOrder, max, offset)
        entities.removeAll { it.domainId == 0 }
        return entities
    }


    def count() {
        return GlobalUniqueIdentifier.countByLdmNameAndDomainIdGreaterThan(GeneralValidationCommonConstants.ETHNICITIES_US, 0L)
    }


    GlobalUniqueIdentifier fetchByGuid(String usEthnicCodeGuid) {
        return globalUniqueIdentifierService.fetchByLdmNameAndGuid(GeneralValidationCommonConstants.ETHNICITIES_US, usEthnicCodeGuid?.toLowerCase()?.trim())
    }


    Map<String, String> getUsEthnicCodeToGuidMap() {
        Map<String, String> usEthnicCodeToGuidMap = [:]
        fetchAll().each {
            usEthnicCodeToGuidMap.put(String.valueOf(it.domainId), it.guid)
        }
        return usEthnicCodeToGuidMap
    }


    String fetchUsEthnicCodeByGuid(String usEthnicCodeGuid) {
        String usEthnicCode
        GlobalUniqueIdentifier globalUniqueIdentifier = fetchByGuid(usEthnicCodeGuid)
        if (globalUniqueIdentifier) {
            usEthnicCode = String.valueOf(globalUniqueIdentifier.domainId)
        }
        return usEthnicCode
    }

}
