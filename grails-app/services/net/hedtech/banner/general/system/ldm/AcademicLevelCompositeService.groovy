/*******************************************************************************
 Copyright 2014 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.general.system.ldm

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.exceptions.NotFoundException
import net.hedtech.banner.general.overall.ldm.GlobalUniqueIdentifier
import net.hedtech.banner.general.overall.ldm.GlobalUniqueIdentifierService
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
    def globalUniqueIdentifierService

    private static final String LDM_NAME = 'academic-levels'


    List<AcademicLevel> list(Map map) {
        List academicLevels = []
        RestfulApiValidationUtility.correctMaxAndOffset(map, RestfulApiValidationUtility.MAX_DEFAULT, RestfulApiValidationUtility.MAX_UPPER_LIMIT)
        List<Level> levels = levelService.list(map) as List
        levels.each { level ->
            academicLevels << new AcademicLevel(level, GlobalUniqueIdentifier.findByLdmNameAndDomainId(LDM_NAME, level.id)?.guid)
        }
        return academicLevels
    }


    Long count() {
        return Level.count()
    }


    @Transactional(readOnly = true)
    AcademicLevel get(String guid) {
        GlobalUniqueIdentifier globalUniqueIdentifier = GlobalUniqueIdentifier.fetchByLdmNameAndGuid(LDM_NAME, guid)
        if (!globalUniqueIdentifier) {
            throw new ApplicationException(GlobalUniqueIdentifierService.API, new NotFoundException(id: Level.class.simpleName))
        }

        Level level = Level.get(globalUniqueIdentifier.domainId)
        if (!level) {
            throw new ApplicationException(GlobalUniqueIdentifierService.API, new NotFoundException(id: Level.class.simpleName))
        }

        return new AcademicLevel(level, globalUniqueIdentifier.guid);
    }


    AcademicLevel fetchByLevelId(Long domainId) {
        if (null == domainId) {
            return null
        }
        Level level = levelService.get(domainId)
        if (!level) {
            return null
        }
        return new AcademicLevel(levelService.get(domainId) as Level, GlobalUniqueIdentifier.findByLdmNameAndDomainId(LDM_NAME, domainId)?.guid)
    }


    AcademicLevel fetchByLevelCode(String levelCode) {
        if (!levelCode) {
            return null
        }
        Level level = Level.findByCode(levelCode)
        if (!level) {
            return null
        }
        return new AcademicLevel(level, GlobalUniqueIdentifier.findByLdmNameAndDomainId(LDM_NAME, level.id)?.guid)
    }

}
