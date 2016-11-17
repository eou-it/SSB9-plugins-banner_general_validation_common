/*******************************************************************************
 Copyright 2016 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.general.system.ldm

import net.hedtech.banner.general.overall.ldm.GlobalUniqueIdentifier
import net.hedtech.banner.general.overall.ldm.LdmService
import net.hedtech.banner.general.system.ActivityType
import net.hedtech.banner.general.system.ActivityTypeService
import org.springframework.transaction.annotation.Transactional

@Transactional
class ActivityTypeCompositeService extends LdmService {

    ActivityTypeService activityTypeService


    def getActivityTypeCodeToGuidMap(Collection<String> codes) {
        Map<String, String> codeToGuidMap = [:]
        if (codes) {
            List entities = activityTypeService.fetchAllWithGuidByCodeInList(codes)
            entities.each {
                ActivityType activityType = it.activityType
                GlobalUniqueIdentifier globalUniqueIdentifier = it.globalUniqueIdentifier
                codeToGuidMap.put(activityType.code, globalUniqueIdentifier.guid)
            }
        }
        return codeToGuidMap
    }
}
