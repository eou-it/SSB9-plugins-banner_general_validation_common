/*******************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.general.system.ldm

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.exceptions.BusinessLogicValidationException
import net.hedtech.banner.exceptions.NotFoundException
import net.hedtech.banner.general.overall.ldm.GlobalUniqueIdentifier
import net.hedtech.banner.general.system.AcademicHonorView
import net.hedtech.banner.general.system.ldm.v4.AcademicHonor
import net.hedtech.banner.query.DynamicFinder
import net.hedtech.banner.query.QueryBuilder
import net.hedtech.banner.query.operators.Operators
import net.hedtech.banner.restfulapi.RestfulApiValidationUtility
import org.springframework.transaction.annotation.Transactional

/**
 * <p> REST End point for Academic Honor Service. If we'll pass type is award then , Departmental-honor records will return .</p>
 * <p> If we'll pass type is distinction then, Institutional  Honor records will return else, Both of 2 Honor will return</p>
 */
class AcademicHonorCompositeService {

    public static final def allowedSortFields=['title']
    public static final def DEFAULT_SORTED_FIELD='code'
    public static final def ALLOWED_SEARCH_FIELD='type'
    public static final String LDM_NAME_INSTITUTIONAL = 'institutional-honors'
    public static final String LDM_NAME_DEPARTMENTAL = 'departmental-honors'
    public static final String DEFAULT_ORDER_TYPE='ASC'
    public static final Integer MAX_UPPER_LIMIT=500
    public final def SEARCH_PARAMETERS=['award', 'distinction']
    public final Map sortFieldMap=['title':'code']
    public final Map ldmToDomainFieldMap=[
            'award':LDM_NAME_DEPARTMENTAL,
            'distinction':LDM_NAME_INSTITUTIONAL
    ]

    /**
     * GET /api/academic-honors
     * @return List
     */
    @Transactional(readOnly = true)
    List<AcademicHonor> list(Map params) {
        List academicHonors = []
        List<AcademicHonorView> results = fetchAcademicHonorViewCriteria(params)

        results.each {
            result->
                academicHonors << new AcademicHonor(result)
        }
        academicHonors
    }

     private def fetchAcademicHonorViewCriteria(Map params,boolean count=false) {
        def results
         RestfulApiValidationUtility.validateSortField(params.sort, allowedSortFields)
         RestfulApiValidationUtility.correctMaxAndOffset(params,0,MAX_UPPER_LIMIT)
         def sortParam = params?.sort
         params.sort = params?.sort ? sortFieldMap[params.sort] : DEFAULT_SORTED_FIELD
         params.order = params?.order ? params.order : DEFAULT_ORDER_TYPE
         RestfulApiValidationUtility.validateSortOrder(params.order)
         def pagingAndSortingParams = QueryBuilder.getFilterData(params)?.pagingAndSortParams
         if(!count){
             params.sort=sortParam
         }

        def queryParam = [:]

        String type = params.type

        def criteria = []
        if (type && SEARCH_PARAMETERS.contains(type)) {
            criteria.add([key: type, binding: ALLOWED_SEARCH_FIELD, operator: Operators.EQUALS_IGNORE_CASE])
            queryParam.put(type,ldmToDomainFieldMap[type])
        }
        if (type && !SEARCH_PARAMETERS.contains(type)) {
            criteria.add([key: type, binding: ALLOWED_SEARCH_FIELD, operator: Operators.EQUALS_IGNORE_CASE])
            queryParam.put(type,type)
        }

        DynamicFinder dynamicFinder = new DynamicFinder(AcademicHonorView.class, "from AcademicHonorView honor", "honor")


        if (count) {
            results = dynamicFinder.count([params: queryParam, criteria: criteria])
        } else {
            results = dynamicFinder.find([params: queryParam, criteria: criteria], pagingAndSortingParams)
        }
        return results
    }

    /**
     *
     * @return Long value as total count
     */
    Long count(params) {
        return fetchAcademicHonorViewCriteria(params,true)
    }

    /**
     * GET /api/academic-honors/{guid}
     * @param guid
     * @return
     */
    @Transactional(readOnly = true)
    AcademicHonor get(String guid){
        AcademicHonorView academicHonorView=null
        if(guid){
            academicHonorView = AcademicHonorView.fetchByGuid(guid)
        }
        if(!academicHonorView){
            GlobalUniqueIdentifier globalUniqueIdentifier = GlobalUniqueIdentifier.findByGuid(guid)
            if(globalUniqueIdentifier){
                throw new ApplicationException("academicCompositeService", new BusinessLogicValidationException("invalid.guid",[]))
            }else{
                throw new ApplicationException("academicCompositeService", new NotFoundException())
            }

        }
        new AcademicHonor(academicHonorView)
    }



}
