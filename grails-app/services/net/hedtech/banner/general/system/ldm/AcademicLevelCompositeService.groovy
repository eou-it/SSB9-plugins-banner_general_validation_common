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
import net.hedtech.banner.general.system.ldm.v4.AcademicLevelDetail
import net.hedtech.banner.restfulapi.RestfulApiValidationUtility
import net.hedtech.restfulapi.UnsupportedMethodException
import org.springframework.transaction.annotation.Transactional

/**
 * LevelCompositeService.
 * This class supports Level (Course) service for LDM.
 */

@Transactional
class AcademicLevelCompositeService {

    def levelService

    private static final String LDM_NAME = 'academic-levels'
    private static final List<String> VERSIONS = ["v1", "v2","v3","v4"]
    private static final int LEVEL_TITLE_LENGTH = 30
    private static final String LATEST_VERSION = "v4"


    @Transactional(readOnly = true)
    def list(Map params) {
        List academicLevels = []
        RestfulApiValidationUtility.correctMaxAndOffset(params, RestfulApiValidationUtility.MAX_DEFAULT, RestfulApiValidationUtility.MAX_UPPER_LIMIT)
        List allowedSortFields = (LATEST_VERSION.equals(LdmService.getAcceptVersion(VERSIONS))? ['code', 'title']:['abbreviation', 'title'])
        RestfulApiValidationUtility.validateSortField(params.sort, allowedSortFields)
        RestfulApiValidationUtility.validateSortOrder(params.order)
        params.sort = LdmService.fetchBannerDomainPropertyForLdmField(params.sort)
        def flag=LATEST_VERSION.equals(LdmService.getAcceptVersion(VERSIONS))
        List<Level> levels = levelService.list(params) as List
        levels.each { level ->
            academicLevels << getDecorator(level,null,flag)
        }
        return academicLevels
    }


    Long count() {
        return Level.count()
    }


    @Transactional(readOnly = true)
    def get(String guid) {
        GlobalUniqueIdentifier globalUniqueIdentifier = GlobalUniqueIdentifier.fetchByLdmNameAndGuid(LDM_NAME, guid)

        if (!globalUniqueIdentifier) {
            throw new ApplicationException("academicLevel", new NotFoundException())
        }

        Level level = Level.get(globalUniqueIdentifier.domainId)
        if (!level) {
            throw new ApplicationException("academicLevel", new NotFoundException())
        }

        return getDecorator(level,globalUniqueIdentifier.guid,LATEST_VERSION.equals(LdmService.getAcceptVersion(VERSIONS)))
    }

    /**
     * POST /api/academic-levels
     *
     * @param content Request body
     */
    def create(Map content) {
        if(LATEST_VERSION.equals(LdmService.getContentTypeVersion(VERSIONS)) && LATEST_VERSION.equals(LdmService.getAcceptVersion(VERSIONS))) {
            validateCode(content)
            validateTitle(content)
            Level level = bindLevel(new Level(), content)
            return getDecorator(level,null,Boolean.TRUE)
        }else{
            throw new UnsupportedMethodException(supportedMethods:['GET'],pluralizedResourceName:LDM_NAME)
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

    private void validateCode(content) {
        if(!content.containsKey("code")){
            throw new ApplicationException('level', new BusinessLogicValidationException('code.required.message', null))
        }else if (!content?.code) {
            throw new ApplicationException('level', new BusinessLogicValidationException('code.empty/null.message', null))
        }else if(content?.code.size()>2) {
            throw new ApplicationException('level', new BusinessLogicValidationException('code.exceed.size.message', null))
        }else if(Level.findByCode(content?.code)!=null){
            throw new ApplicationException('level', new BusinessLogicValidationException('code.exists.message', null))
        }
    }
    
    private void validateTitle(content){
        if(!content?.title){
            throw new ApplicationException('level', new BusinessLogicValidationException('title.required.message', null))
        }else if (!content?.title[0]?.containsKey("en")) {
            throw new ApplicationException('level', new BusinessLogicValidationException('title.invalid.Multi-lingual.message', null))
        }else if(!content?.title[0].en){
            throw new ApplicationException('level', new BusinessLogicValidationException('title.empty/null.message', null))
        }
        
    }

  private  def bindLevel(Level level, Map content) {
        LdmService.setDataOrigin(level, content)
        bindData(level, content)
        levelService.createOrUpdate(level)
    }

    private void bindData(domainModel,content){
        domainModel.code=content?.code
        def description= content?.title[0].en
        domainModel.description=description?.size()>30 ? description?.substring(0,LEVEL_TITLE_LENGTH) : description
        domainModel.ceuInd=Boolean.FALSE
    }

    private def  getDecorator(Level level,String levelGuid,Boolean versionFlag) {
        if (level) {
            if(!levelGuid)
                levelGuid = GlobalUniqueIdentifier.findByLdmNameAndDomainId(LDM_NAME, level.id)?.guid
            if(versionFlag)
                new AcademicLevelDetail(level, levelGuid, new Metadata(level.dataOrigin))
            else
                new AcademicLevel(level,levelGuid, new Metadata(level.dataOrigin))

        }
    }

}
