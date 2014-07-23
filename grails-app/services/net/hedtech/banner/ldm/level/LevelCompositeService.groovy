/*******************************************************************************
 Copyright 2014 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.ldm.level

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.exceptions.NotFoundException
import net.hedtech.banner.general.overall.ldm.GlobalUniqueIdentifier
import net.hedtech.banner.general.system.Level
import org.springframework.transaction.annotation.Transactional

/**
 * LevelCompositeService.
 * This class supports Level (Course) service for LDM.
 */

@Transactional
class LevelCompositeService {

    def levelService


    @Transactional(readOnly = true)
    def get(id) {
        log.trace "get:Begin"
        def ldmLevel
        if (null != id) {
            def entity = GlobalUniqueIdentifier.findByGuid(id)
            if (!entity) {
                throw new ApplicationException("api", new NotFoundException(id: Level.class.simpleName))
            }
            //Level studentLevel = Level.findByCode(entity.domainKey)
            Level studentLevel = levelService.get(entity.domainId)
            if (!studentLevel) {
                throw new ApplicationException("api", new NotFoundException(id: Level.class.simpleName))
            }
            // Create decorated object to return.
            ldmLevel = new net.hedtech.banner.general.system.ldm.v1.Level(entity.guid, studentLevel)
        }
        log.trace "get:End"
        return ldmLevel
    }

}
