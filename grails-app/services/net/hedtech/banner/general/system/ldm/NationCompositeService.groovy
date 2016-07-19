/** *******************************************************************************
 Copyright 2016 Ellucian Company L.P. and its affiliates.
 ********************************************************************************* */
package net.hedtech.banner.general.system.ldm

import net.hedtech.banner.general.system.Nation
import net.hedtech.banner.general.system.NationService

/**
 * Service used to support "nation" resource for LDM
 */
class NationCompositeService {

    NationService nationService

    def fetchAllByCodesInList(Collection<String> codes) {
        Map<String, String> codeToNationMap = [:]
        if (codes) {
            List<Nation> entities = nationService.fetchAllByCodeInList(codes)
            entities.each { country ->
                codeToNationMap.put(country.code, country)
            }
        }
        return codeToNationMap
    }

}
