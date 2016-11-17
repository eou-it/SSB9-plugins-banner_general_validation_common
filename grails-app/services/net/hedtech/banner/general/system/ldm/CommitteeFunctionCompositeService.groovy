/** *******************************************************************************
 Copyright 2016 Ellucian Company L.P. and its affiliates.
 ********************************************************************************* */
package net.hedtech.banner.general.system.ldm

import net.hedtech.banner.general.overall.ldm.GlobalUniqueIdentifier
import net.hedtech.banner.general.system.CommitteeFunction
import net.hedtech.banner.general.system.CommitteeFunctionService
import net.hedtech.banner.general.overall.ldm.LdmService
import org.springframework.transaction.annotation.Transactional

@Transactional
class CommitteeFunctionCompositeService extends LdmService {

    CommitteeFunctionService committeeFunctionService

    def getCommitteeFunctionCodeToGuidMap(Collection<String> codes) {
        Map<String, String> codeToGuidMap = [:]
        if (codes) {
            List entities = committeeFunctionService.fetchAllWithGuidByCodeInList(codes)
            entities.each {
                CommitteeFunction committeeFunction = it.committeeFunction
                GlobalUniqueIdentifier globalUniqueIdentifier = it.globalUniqueIdentifier
                codeToGuidMap.put(committeeFunction.code, globalUniqueIdentifier.guid)
            }
        }
        return codeToGuidMap
    }

}
