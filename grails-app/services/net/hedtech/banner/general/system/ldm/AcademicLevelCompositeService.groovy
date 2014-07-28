/*******************************************************************************
 Copyright 2014 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.general.system.ldm

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.exceptions.NotFoundException
import net.hedtech.banner.general.overall.ldm.GlobalUniqueIdentifier
import net.hedtech.banner.general.system.Level
import net.hedtech.banner.general.system.ldm.v1.AcademicLevel
import net.hedtech.banner.restfulapi.RestfulApiValidationUtility
import org.springframework.transaction.annotation.Transactional

/**
 * LevelCompositeService.
 * This class supports Level (Course) service for LDM.
 */

@Transactional
class AcademicLevelCompositeService {

    def levelService

    private static final String LDM_NAME = 'academic-levels'


    List<AcademicLevel> list(Map map) {
        List academicLevels = []
        RestfulApiValidationUtility.correctMaxAndOffset(map, 10, 30)
        List<Level> levels = levelService.list(map) as List
        levels.each { level ->
            academicLevels << new AcademicLevel(GlobalUniqueIdentifier.fetchByLdmNameAndDomainId(LDM_NAME, level.id)?.guid, level)
        }
        return academicLevels
    }


    Long count() {
        return Level.count()
    }


    @Transactional(readOnly = true)
    AcademicLevel get(String guid) {
        log.trace "get:Begin"
        def ldmLevel
        if (null != guid) {
            def entity = GlobalUniqueIdentifier.findByGuid(guid)
            if (!entity && (entity?.ldmName != LDM_NAME)) {
                throw new ApplicationException("api", new NotFoundException(id: Level.class.simpleName))
            }
            //Level studentLevel = Level.findByCode(entity.domainKey)
            Level studentLevel = levelService.get(entity.domainId)
            if (!studentLevel) {
                throw new ApplicationException("api", new NotFoundException(id: Level.class.simpleName))
            }
            // Create decorated object to return.
            ldmLevel = new AcademicLevel(entity.guid, studentLevel)
        }
        log.trace "get:End"
        return ldmLevel
    }


    AcademicLevel fetchByAcademicLevelId(Long academicLevelId) {
        if (null == academicLevelId) {
            return null
        }
        return new AcademicLevel(GlobalUniqueIdentifier.fetchByLdmNameAndDomainId(LDM_NAME, academicLevelId)?.guid, Level.get(academicLevelId) as Level)
    }


    AcademicLevel fetchByAcademicLevel(String academicLevelCode) {
        if (!academicLevelCode) {
            return null
        }
        Level levelCode = Level.findByCode(academicLevelCode)
        if (!levelCode) {
            return null
        }
        return new AcademicLevel(GlobalUniqueIdentifier.fetchByLdmNameAndDomainId(LDM_NAME, levelCode.id)?.guid, levelCode)
    }

}
