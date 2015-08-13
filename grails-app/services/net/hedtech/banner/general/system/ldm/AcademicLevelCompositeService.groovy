/*******************************************************************************
 Copyright 2014-2015 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.general.system.ldm

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.exceptions.BusinessLogicValidationException
import net.hedtech.banner.exceptions.NotFoundException
import net.hedtech.banner.general.overall.ldm.GlobalUniqueIdentifier
import net.hedtech.banner.general.overall.ldm.LdmService
import net.hedtech.banner.general.system.Level
import net.hedtech.banner.general.system.ldm.v1.AcademicLevel
import net.hedtech.banner.general.system.ldm.v1.Metadata
import net.hedtech.banner.general.system.ldm.v4.MetadataV4
import net.hedtech.banner.restfulapi.RestfulApiValidationUtility
import org.springframework.transaction.annotation.Transactional

/**
 * LevelCompositeService.
 * This class supports Level (Course) service for LDM.
 */

@Transactional
class AcademicLevelCompositeService extends  LdmService {

    def levelService

    private static final String LDM_NAME = 'academic-levels'
    private static final List<String> VERSIONS = ["v1","v4"]


    @Transactional(readOnly = true)
    List<AcademicLevel> list(Map params) {
        List academicLevels = []
        RestfulApiValidationUtility.correctMaxAndOffset(params, RestfulApiValidationUtility.MAX_DEFAULT, RestfulApiValidationUtility.MAX_UPPER_LIMIT)
        List allowedSortFields = ("v4".equals(getAcceptVersion(VERSIONS))? ['code', 'title']:['abbreviation', 'title'])
        RestfulApiValidationUtility.validateSortField(params.sort, allowedSortFields)
        RestfulApiValidationUtility.validateSortOrder(params.order)
        params.sort = fetchBannerDomainPropertyForLdmField(params.sort)
        List<Level> levels = levelService.list(params) as List
        levels.each { level ->
            academicLevels <<  getDecorator(level)
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

        return getDecorator(level,globalUniqueIdentifier?.guid)
    }

    /**
     * POST /api/academic-levels
     *
     * @param content Request body
     */
    AcademicLevel create(Map content) {
        Level level = Level.findByCode(content?.code)
        if (level) {
            def parameterValue
            if ("v4".equals(LdmService.getAcceptVersion(VERSIONS))) {
                parameterValue = 'code'
            } else if ("v1".equals(LdmService.getAcceptVersion(VERSIONS))) {
                parameterValue = 'abbreviation'
            }
            throw new ApplicationException('academicLevel', new BusinessLogicValidationException('code.exists.message', [parameterValue]))
        }
         level = bindLevel(new Level(), content)
        String levelGuid = content?.id?.trim()?.toLowerCase()
        if (levelGuid) {
            // Overwrite the GUID created by DB insert trigger, with the one provided in the request body
            levelGuid= updateGuidValue(level.id, levelGuid, LDM_NAME)?.guid
        } else {
            levelGuid = GlobalUniqueIdentifier.findByLdmNameAndDomainId(LDM_NAME, level?.id)?.guid
        }
        return getDecorator(level,levelGuid)
    }

    /**
     * PUT /api/academic-levels/<guid>
     *
     * @param content Request body
     */
    AcademicLevel update(content) {
        String levelGuid = content?.id?.trim()?.toLowerCase()
        GlobalUniqueIdentifier globalUniqueIdentifier = GlobalUniqueIdentifier.fetchByLdmNameAndGuid(LDM_NAME, levelGuid)
        if (levelGuid) {
            if (!globalUniqueIdentifier) {
                if (!content?.guid) {
                    content.put('guid', levelGuid)
                }
                //Per strategy when a GUID was provided, the create should happen.
                return create(content)
            }
        } else {
            throw new ApplicationException("academicLevel", new NotFoundException())
        }

        Level level = Level.findById(globalUniqueIdentifier?.domainId)
        if (!level) {
            throw new ApplicationException("academicLevel", new NotFoundException())
        }

        // Should not allow to update LEVEL.code as it is read-only
        if (level?.code != content?.code?.trim()) {
            content.put("code", level?.code)
        }
        level = bindLevel(level, content)
        return getDecorator(level, levelGuid)
    }

    private  def bindLevel(Level level, Map content) {
        bindData(level, content)
        levelService.createOrUpdate(level)
    }

    private void bindData(domainModel,content){
      super.setDataOrigin(domainModel,content)
      super.bindData(domainModel,content,[:])
      domainModel.ceuInd=Boolean.FALSE
    }

    private def  getDecorator(Level level,String levelGuid=null) {
        if (level) {
            def metaData
            if(!levelGuid){
                levelGuid = GlobalUniqueIdentifier.findByLdmNameAndDomainId(LDM_NAME, level.id)?.guid
            }
            if ("v4".equals(LdmService.getAcceptVersion(VERSIONS))) {
                metaData = new MetadataV4(level?.lastModifiedBy)
            } else if ("v1".equals(LdmService.getAcceptVersion(VERSIONS))) {
                metaData = new Metadata(level?.dataOrigin)
            }
                new AcademicLevel(level,levelGuid, metaData)
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
