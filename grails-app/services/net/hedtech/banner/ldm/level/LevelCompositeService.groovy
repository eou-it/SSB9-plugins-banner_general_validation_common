/*******************************************************************************
 Copyright 2014 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.ldm.level

import net.hedtech.banner.exceptions.ApplicationException
import org.springframework.transaction.annotation.Transactional
import net.hedtech.banner.ldm.overall.Guid
import net.hedtech.banner.general.system.Level

/**
 * LevelCompositeService.
 * This class supports Level (Course) service for LDM.
 */

@Transactional
class LevelCompositeService {

    net.hedtech.banner.general.system.ldm.v1.Level ldmLevel

    @Transactional(readOnly = true)
    def get(id) {
        log.trace "get:Begin"
        if( null != id ){
            def entity = Guid.findByGuid(id)
            if( !entity ) {
                throw new ApplicationException("Level","@@r1:not.found.message:Level::BusinessLogicValidationException@@")
            }
            Level studentLevel = Level.findByCode(entity.domainKey)
            if( null != studentLevel ){
                // Create decorated object to return.
                ldmLevel = new net.hedtech.banner.general.system.ldm.v1.Level(studentLevel)
            }
        }
        log.trace "get:End"
        return ldmLevel
    }
}
