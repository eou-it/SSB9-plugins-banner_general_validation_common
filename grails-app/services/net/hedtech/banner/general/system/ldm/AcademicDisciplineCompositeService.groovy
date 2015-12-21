/*******************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.general.system.ldm

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.exceptions.BusinessLogicValidationException
import net.hedtech.banner.exceptions.NotFoundException
import net.hedtech.banner.general.common.GeneralValidationCommonConstants
import net.hedtech.banner.general.overall.ldm.GlobalUniqueIdentifier
import net.hedtech.banner.general.overall.ldm.LdmService
import net.hedtech.banner.general.system.AcademicDisciplineView
import net.hedtech.banner.general.system.MajorMinorConcentration
import net.hedtech.banner.general.system.ldm.v4.AcademicDiscipline
import net.hedtech.banner.general.system.ldm.v4.AcademicDisciplineType
import net.hedtech.banner.query.QueryBuilder
import net.hedtech.banner.query.operators.Operators
import net.hedtech.banner.restfulapi.RestfulApiValidationUtility
import org.springframework.transaction.annotation.Transactional

/**
 * <p> REST End point for Academic Discipline Service. If we'll pass type is major then , Academic Discipline major type of data will return.</p>
 * <p> If we'll pass type is minor then, Academic Discipline minor type of data will return else, It will return all  type of Academic Discipline data.</p>
 */
@Transactional
class AcademicDisciplineCompositeService extends LdmService{
    
    //This filed is used for only to create and update of Academic Discipline data
    def majorMinorConcentrationService
    
    private static final String ACADEMIC_DISCIPLINE_HEDM = 'academic-disciplines'
    private static final String DEFAULT_SORT_FIELD = 'abbreviation'
    private static final String DEFAULT_ORDER_TYPE = 'ASC'
    private static final List allowedSortFields = [DEFAULT_SORT_FIELD,'type']

    /**
     * GET /api/academic-disciplines
     * @param params
     * @return
     */
    @Transactional(readOnly = true)
    List<AcademicDiscipline> list(Map params) {
        List<AcademicDiscipline> academicDisciplineDetailList = []
        RestfulApiValidationUtility.correctMaxAndOffset(params, RestfulApiValidationUtility.MAX_DEFAULT, RestfulApiValidationUtility.MAX_UPPER_LIMIT)
        params?.sort = params?.sort ?: DEFAULT_SORT_FIELD
        params?.order = params?.order ?: DEFAULT_ORDER_TYPE
        RestfulApiValidationUtility.validateSortField(params.sort, allowedSortFields)
        RestfulApiValidationUtility.validateSortOrder(params.order)
        params.sort = LdmService.fetchBannerDomainPropertyForLdmField(params.sort)?:params.sort
       fetchAcademicDisciplineByCriteria(false, params).each { academicDiscipline ->
            academicDisciplineDetailList <<  new AcademicDiscipline(academicDiscipline?.code, academicDiscipline?.description, academicDiscipline?.type, academicDiscipline?.guid)
        }
        return academicDisciplineDetailList;
    }

    /**
     *
     * @return Count
     */
    @Transactional(readOnly = true)
    Long count(Map params) {
        fetchAcademicDisciplineByCriteria(true, params)
    }

    /**
     * GET /api/academic-disciplines/{guid}
     * @param guid
     * @return 
     */
    @Transactional(readOnly = true)
    AcademicDiscipline get(String guid) {
        if (!guid) {
            throw new ApplicationException(GeneralValidationCommonConstants.ACADEMIC_DISCIPLINE, new NotFoundException())
        }
        AcademicDisciplineView academicDiscipline = AcademicDisciplineView.get(guid?.trim())
        if (!academicDiscipline) {
            throw new ApplicationException(GeneralValidationCommonConstants.ACADEMIC_DISCIPLINE, new NotFoundException())
        }
        return new AcademicDiscipline(academicDiscipline.code, academicDiscipline.description, academicDiscipline.type, academicDiscipline.guid)
    }

    /**
     * fetch academic discipline data based on criteria
     * @param count
     * @param content
     * @return
     */
    private def fetchAcademicDisciplineByCriteria(Boolean count, Map content) {
        def result
        def filterMap = QueryBuilder.getFilterData(content)
        def params = filterMap.params
        def criteria = filterMap.criteria
        def pagingAndSortParams = filterMap.pagingAndSortParams

        if (content.containsKey(GeneralValidationCommonConstants.TYPE)) {
            params.put(GeneralValidationCommonConstants.TYPE, content.get(GeneralValidationCommonConstants.TYPE).trim())
            criteria.add([key: GeneralValidationCommonConstants.TYPE, binding: GeneralValidationCommonConstants.TYPE, operator: Operators.EQUALS])
        }
        if (count) {
            result = AcademicDisciplineView.countAll([params: params, criteria: criteria])
        } else {
            result = AcademicDisciplineView.fetchSearch([params: params, criteria: criteria], pagingAndSortParams)
        }

        return result
    }

    /**
     * POST /api/academic-disciplines
     *
     * @param content Request body
     */
    def create(Map content) {
        if (!content?.code) {
            throw new ApplicationException(GeneralValidationCommonConstants.ACADEMIC_DISCIPLINE, new BusinessLogicValidationException(GeneralValidationCommonConstants.ERROR_MSG_CODE_REQUIRED, null))
        }
        String majorMinorConcentrationGuid = content?.id?.trim()?.toLowerCase()
        if (globalUniqueIdentifierService.fetchByLdmNameAndGuid(ACADEMIC_DISCIPLINE_HEDM, majorMinorConcentrationGuid)) {
            throw new ApplicationException(GeneralValidationCommonConstants.ACADEMIC_DISCIPLINE, new BusinessLogicValidationException(GeneralValidationCommonConstants.ERROR_MSG_EXISTS_MESSAGE, null))
        }
        MajorMinorConcentration majorMinorConcentration = majorMinorConcentrationService.fetchByCode(content?.code?.trim())
        if (!majorMinorConcentration) {
            // if code in the request does not exists - create the record
            majorMinorConcentration = bindmajorMinorConcentration(new MajorMinorConcentration(), content)
            majorMinorConcentrationGuid = updateGuidIfExist(majorMinorConcentration, content, majorMinorConcentrationGuid)
        }
            // if code in the request exists - then check with the type
            else if(AcademicDisciplineView.findByCodeAndType(content?.code,content.type)){
                throw new ApplicationException(GeneralValidationCommonConstants.ACADEMIC_DISCIPLINE, new BusinessLogicValidationException(GeneralValidationCommonConstants.ERROR_MSG_EXISTS_MESSAGE, null))
            }
            else{
                majorMinorConcentration = bindmajorMinorConcentration(majorMinorConcentration, content)
                majorMinorConcentrationGuid = updateGuidIfExist(majorMinorConcentration, content, majorMinorConcentrationGuid)
            }

        return new AcademicDiscipline(majorMinorConcentration.code, majorMinorConcentration.description, content.type, majorMinorConcentrationGuid)
    }

    /**
     * PUT /api/academic-disciplines/<id>
     *
     * @param content Request body
     * @return
     */
    def update(Map content) {
        String majorMinorConcentrationGuid = content?.id?.trim()?.toLowerCase()
        if (!majorMinorConcentrationGuid) {
            throw new ApplicationException(GeneralValidationCommonConstants.ACADEMIC_DISCIPLINE, new NotFoundException())
        }
        GlobalUniqueIdentifier globalUniqueIdentifier = globalUniqueIdentifierService.fetchByLdmNameAndGuid(ACADEMIC_DISCIPLINE_HEDM, majorMinorConcentrationGuid)
        if (!globalUniqueIdentifier) {
            //Per strategy when a ID was provided, the create should happen.
            return create(content)
        }
        String code = globalUniqueIdentifier?.domainKey.split('\\^')[0]
        String type = globalUniqueIdentifier?.domainKey.split('\\^')[1]
        MajorMinorConcentration majorMinorConcentration = majorMinorConcentrationService.fetchByCode(code?.trim())
        if (!majorMinorConcentration) {
            throw new ApplicationException(GeneralValidationCommonConstants.ACADEMIC_DISCIPLINE, new NotFoundException())
        }
        if (code != content?.code?.trim()) {
            content.put(GeneralValidationCommonConstants.CODE, code)
        }
        if (type != content?.type?.trim()) {
            content.put(GeneralValidationCommonConstants.TYPE, type)
        }
        majorMinorConcentration = bindmajorMinorConcentration(majorMinorConcentration, content)
        return new AcademicDiscipline(majorMinorConcentration.code, majorMinorConcentration.description, content.type, majorMinorConcentrationGuid)
    }

    /**
     * Checking the id - if id is being sent in the request payload - update the id else return the generated id
     */
    private String updateGuidIfExist(MajorMinorConcentration majorMinorConcentration, Map content, String majorMinorConcentrationGuid = null) {
        // if id being sent in the request - update the guid else return the generated guid
        if (majorMinorConcentrationGuid) {
            majorMinorConcentrationGuid = updateGuidValueByDomainKey(majorMinorConcentration.code+"^"+content.type, majorMinorConcentrationGuid, ACADEMIC_DISCIPLINE_HEDM)?.guid
        } else {
            majorMinorConcentrationGuid = GlobalUniqueIdentifier.findByLdmNameAndDomainKey(ACADEMIC_DISCIPLINE_HEDM, majorMinorConcentration.code+"^"+content.type)?.guid
        }
        majorMinorConcentrationGuid
    }

    /**
     * Invoking the LDM service to bind map properties onto grails domains.
     * Invoking the ServiceBase to creates or updates a model instance provided within the supplied domainModelOrMap.
     */
    def bindmajorMinorConcentration(MajorMinorConcentration majorMinorConcentration, Map content) {
        bindData(majorMinorConcentration, content, [:])
        switch (content?.type){
            case AcademicDisciplineType.MAJOR.value :
                majorMinorConcentration.validMajorIndicator = true
                break
            case AcademicDisciplineType.MINOR.value :
                majorMinorConcentration.validMinorIndicator = true
                break
            case AcademicDisciplineType.CONCENTRATION.value :
                majorMinorConcentration.validConcentratnIndicator = true
                break
        }
        majorMinorConcentrationService.createOrUpdate(majorMinorConcentration)
    }
}
