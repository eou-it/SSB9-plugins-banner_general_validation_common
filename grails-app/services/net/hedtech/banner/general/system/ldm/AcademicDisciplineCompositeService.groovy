/*******************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.general.system.ldm

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.exceptions.NotFoundException
import net.hedtech.banner.general.overall.ldm.LdmService
import net.hedtech.banner.general.system.AcademicDisciplineView
import net.hedtech.banner.general.system.ldm.v4.AcademicDiscipline
import net.hedtech.banner.query.QueryBuilder
import net.hedtech.banner.query.operators.Operators
import net.hedtech.banner.restfulapi.RestfulApiValidationUtility
import org.springframework.transaction.annotation.Transactional

/**
 * <p> REST End point for Academic Discipline Service. If we'll pass type is major then , Academic Discipline major type of data will return.</p>
 * <p> If we'll pass type is minor then, Academic Discipline minor type of data will return else, It will return all  type of Academic Discipline data.</p>
 */
@Transactional
class AcademicDisciplineCompositeService {
    
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
            throw new ApplicationException("academicDiscipline", new NotFoundException())
        }
        AcademicDisciplineView academicDiscipline = AcademicDisciplineView.get(guid?.trim())
        if (!academicDiscipline) {
            throw new ApplicationException("academicDiscipline", new NotFoundException())
        }
        return new AcademicDiscipline(academicDiscipline.code, academicDiscipline.description, academicDiscipline.type, academicDiscipline.guid)
    }


    private def fetchAcademicDisciplineByCriteria(Boolean count, Map content) {
        def result
        def filterMap = QueryBuilder.getFilterData(content)
        def params = filterMap.params
        def criteria = filterMap.criteria
        def pagingAndSortParams = filterMap.pagingAndSortParams

        if (content.containsKey("type")) {
            params.put("type", content.get("type").trim())
            criteria.add([key: "type", binding: "type", operator: Operators.EQUALS])
        }
        if (count) {
            result = AcademicDisciplineView.countAll([params: params, criteria: criteria])
        } else {
            result = AcademicDisciplineView.fetchSearch([params: params, criteria: criteria], pagingAndSortParams)
        }

        return result
    }

}
