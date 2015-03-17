/*******************************************************************************
 Copyright 2014 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.general.system.ldm

import grails.util.GrailsNameUtils
import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.exceptions.NotFoundException
import net.hedtech.banner.general.overall.ldm.GlobalUniqueIdentifier
import net.hedtech.banner.general.overall.ldm.GlobalUniqueIdentifierService
import net.hedtech.banner.general.overall.ldm.LdmService
import net.hedtech.banner.general.system.Level
import net.hedtech.banner.general.system.ldm.v1.AcademicLevel
import net.hedtech.banner.general.system.ldm.v1.Metadata
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


    @Transactional(readOnly = true)
    List<AcademicLevel> list(Map params) {
        List academicLevels = []
        RestfulApiValidationUtility.correctMaxAndOffset(params, RestfulApiValidationUtility.MAX_DEFAULT, RestfulApiValidationUtility.MAX_UPPER_LIMIT)
        List allowedSortFields = ['abbreviation', 'title']
        RestfulApiValidationUtility.validateSortField(params.sort, allowedSortFields)
        RestfulApiValidationUtility.validateSortOrder(params.order)
        params.sort = LdmService.fetchBannerDomainPropertyForLdmField(params.sort)
        List<Level> levels = levelService.list(params) as List
        levels.each { level ->
            academicLevels << new AcademicLevel(level, GlobalUniqueIdentifier.findByLdmNameAndDomainId(LDM_NAME, level.id)?.guid, new Metadata(level.dataOrigin))
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
            throw new ApplicationException("academicLevel", new NotFoundException())
        }

        Level level = Level.get(globalUniqueIdentifier.domainId)
        if (!level) {
            throw new ApplicationException("academicLevel", new NotFoundException())
        }

        return new AcademicLevel(level, globalUniqueIdentifier.guid, new Metadata(level.dataOrigin));
    }


    AcademicLevel fetchByLevelId(Long domainId) {
        if (null == domainId) {
            return null
        }
        Level level = levelService.get(domainId)
        if (!level) {
            return null
        }
        return new AcademicLevel(level, GlobalUniqueIdentifier.findByLdmNameAndDomainId(LDM_NAME, domainId)?.guid, new Metadata(level.dataOrigin))
    }


    AcademicLevel fetchByLevelCode(String levelCode) {
        if (!levelCode) {
            return null
        }
        Level level = Level.findByCode(levelCode)
        if (!level) {
            return null
        }
        return new AcademicLevel(level, GlobalUniqueIdentifier.findByLdmNameAndDomainId(LDM_NAME, level.id)?.guid, new Metadata(level.dataOrigin))
    }

}
