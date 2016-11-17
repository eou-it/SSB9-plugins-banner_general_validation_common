/*******************************************************************************
 Copyright 2014-2016 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.general.system.ldm

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.exceptions.BusinessLogicValidationException
import net.hedtech.banner.exceptions.NotFoundException
import net.hedtech.banner.general.common.GeneralValidationCommonConstants
import net.hedtech.banner.general.overall.ldm.GlobalUniqueIdentifier
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
class AcademicLevelCompositeService extends LdmService {

    def levelService

    private static
    final List<String> VERSIONS = [GeneralValidationCommonConstants.VERSION_V1, GeneralValidationCommonConstants.VERSION_V4]

    /**
     * GET /api/academic-levels
     *
     * @param params Request parameters
     * @return
     */
    @Transactional(readOnly = true)
    List<AcademicLevel> list(Map params) {
        List academicLevels = []
        RestfulApiValidationUtility.correctMaxAndOffset(params, RestfulApiValidationUtility.MAX_DEFAULT, RestfulApiValidationUtility.MAX_UPPER_LIMIT)
        List allowedSortFields = (GeneralValidationCommonConstants.VERSION_V4.equals(getAcceptVersion(VERSIONS)) ? [GeneralValidationCommonConstants.CODE, GeneralValidationCommonConstants.TITLE] : [GeneralValidationCommonConstants.ABBREVIATION, GeneralValidationCommonConstants.TITLE])
        if (params.containsKey("sort")) {
            RestfulApiValidationUtility.validateSortField(params.sort, allowedSortFields)
            params.sort = fetchBannerDomainPropertyForLdmField(params.sort)
        }

        if (params.containsKey("order")) {
            RestfulApiValidationUtility.validateSortOrder(params.order)
        } else {
            params.put('order', "asc")
        }

        String sortField = params.sort?.trim()
        String sortOrder = params.order?.trim()
        int max = params.max?.trim()?.toInteger() ?: 0
        int offset = params.offset?.trim()?.toInteger() ?: 0

        Map mapForSearch = [:]
        List<Level> levels = levelService.fetchAllByCriteria(mapForSearch, sortField, sortOrder, max, offset) as List

        levels.each { level ->
            academicLevels << getDecorator(level)
        }
        return academicLevels
    }

    /**
     * GET /api/academic-levels
     *
     * The count method must return the total number of instances of the resource.
     * It is used in conjunction with the list method when returning a list of resources.
     * RestfulApiController will make call to "count" only if the "list" execution happens without any exception.
     *
     * @return
     */
    Long count() {
        return levelService.count()
    }

    /**
     * GET /api/academic-levels/<guid>
     *
     * @param guid
     * @return
     */
    @Transactional(readOnly = true)
    AcademicLevel get(String guid) {
        getAcceptVersion(VERSIONS)

        GlobalUniqueIdentifier globalUniqueIdentifier = GlobalUniqueIdentifier.fetchByLdmNameAndGuid(GeneralValidationCommonConstants.ACADEMIC_LEVEL_LDM_NAME, guid)

        Level level = fetchLevelByGuid(guid)
        if (!level) {
            throw new ApplicationException(GeneralValidationCommonConstants.ACADEMIC_LEVEL, new NotFoundException())
        }
        return getDecorator(level, globalUniqueIdentifier.guid)
    }

    /**
     * POST /api/academic-levels
     *
     * @param content Request body
     */
    AcademicLevel create(Map content) {
        Level level = levelService.fetchByCode(content.code)
        if (level) {
            def parameterValue = GeneralValidationCommonConstants.VERSION_V4.equals(LdmService.getAcceptVersion(VERSIONS)) ? GeneralValidationCommonConstants.CODE : GeneralValidationCommonConstants.ABBREVIATION
            throw new ApplicationException(GeneralValidationCommonConstants.ACADEMIC_LEVEL, new BusinessLogicValidationException(GeneralValidationCommonConstants.ERROR_MSG_CODE_EXISTS, [parameterValue]))
        }
        level = bindLevel(new Level(), content)
        String levelGuid = content.guid?.trim()?.toLowerCase()
        if (levelGuid) {
            // Overwrite the GUID created by DB insert trigger, with the one provided in the request body
            levelGuid = updateGuidValue(level.id, levelGuid, GeneralValidationCommonConstants.ACADEMIC_LEVEL_LDM_NAME)?.guid
        } else {
            levelGuid = globalUniqueIdentifierService.fetchByLdmNameAndDomainId(GeneralValidationCommonConstants.ACADEMIC_LEVEL_LDM_NAME, level.id)?.guid
        }
        return getDecorator(level, levelGuid)
    }

    /**
     * PUT /api/academic-levels/<guid>
     *
     * @param content Request body
     */
    AcademicLevel update(content) {
        String levelGuid = content.id?.trim()?.toLowerCase()
        if (!levelGuid) {
            throw new ApplicationException(GeneralValidationCommonConstants.ACADEMIC_LEVEL, new NotFoundException())
        }
        GlobalUniqueIdentifier globalUniqueIdentifier = globalUniqueIdentifierService.fetchByLdmNameAndGuid(GeneralValidationCommonConstants.ACADEMIC_LEVEL_LDM_NAME, levelGuid)
        if (!globalUniqueIdentifier) {
            if (!content.guid) {
                content.guid = levelGuid
            }
            //Per strategy when a GUID was provided, the create should happen.
            return create(content)
        }

        Level level = levelService.get(globalUniqueIdentifier.domainId)
        if (!level) {
            throw new ApplicationException(GeneralValidationCommonConstants.ACADEMIC_LEVEL, new NotFoundException())
        }

        // Should not allow to update LEVEL.code as it is read-only
        if (level.code != content.code?.trim()) {
            content.code = level.code
        }
        level = bindLevel(level, content)
        return getDecorator(level, levelGuid)
    }

    private def bindLevel(Level level, Map content) {
        bindData(level, content)
        levelService.createOrUpdate(level)
    }

    private void bindData(domainModel, content) {
        super.setDataOrigin(domainModel, content)
        super.bindData(domainModel, content, [:])
        domainModel.ceuInd = Boolean.FALSE
    }

    private def getDecorator(Level level, String levelGuid = null) {
        if (level) {
            if (!levelGuid) {
                levelGuid = GlobalUniqueIdentifier.findByLdmNameAndDomainId(GeneralValidationCommonConstants.ACADEMIC_LEVEL_LDM_NAME, level.id)?.guid
            }
            return new AcademicLevel(level, levelGuid, new Metadata(level?.dataOrigin))
        }
    }

    AcademicLevel fetchByLevelId(Long domainId) {
        if (null == domainId) {
            return null
        }
        Level level = levelService.get(domainId)
        if (!level) {
            return null
        }
        return new AcademicLevel(level, GlobalUniqueIdentifier.findByLdmNameAndDomainId(GeneralValidationCommonConstants.ACADEMIC_LEVEL_LDM_NAME, domainId)?.guid, new Metadata(level.dataOrigin))
    }


    AcademicLevel fetchByLevelCode(String levelCode) {
        if (!levelCode) {
            return null
        }
        Level level = Level.findByCode(levelCode)
        if (!level) {
            return null
        }
        return new AcademicLevel(level, GlobalUniqueIdentifier.findByLdmNameAndDomainId(GeneralValidationCommonConstants.ACADEMIC_LEVEL_LDM_NAME, level.id)?.guid, new Metadata(level.dataOrigin))
    }


    Level fetchLevelByGuid(String guid) {
        Level level
        GlobalUniqueIdentifier globalUniqueIdentifier = GlobalUniqueIdentifier.fetchByLdmNameAndGuid(GeneralValidationCommonConstants.ACADEMIC_LEVEL_LDM_NAME, guid)
        if (globalUniqueIdentifier) {
            level = levelService.get(globalUniqueIdentifier.domainId)
        }
        return level
    }


    List<AcademicLevel> fetchAllByLevelIdInList(List<String> ids) {
        List<AcademicLevel> academicLevelList = []
        if (ids) {
            List<GlobalUniqueIdentifier> levelGuidList = GlobalUniqueIdentifier.fetchAllByGuidInList(ids.unique())
            if (levelGuidList) {
                List<Level> levelList = levelService.fetchAllByCodeInList(levelGuidList.domainKey)
                Map levelCodeMap = [:]
                levelList.each {
                    levelCodeMap.put(it.code, it)
                }

                levelGuidList.each {
                    academicLevelList << new AcademicLevel(levelCodeMap.get(it.domainKey), it.guid, new Metadata(levelCodeMap.get(it.domainKey).dataOrigin))
                }
            }
        }
        return academicLevelList
    }

    def getLevelCodeToGuidMap(Collection<String> codes) {
        Map<String, String> codeToGuidMap = [:]
        if (codes) {
            List entities = levelService.fetchAllWithGuidByCodeInList(codes)
            entities.each {
                Level level = it.level
                GlobalUniqueIdentifier globalUniqueIdentifier = it.globalUniqueIdentifier
                codeToGuidMap.put(level.code, globalUniqueIdentifier.guid)
            }
        }
        return codeToGuidMap
    }

}
