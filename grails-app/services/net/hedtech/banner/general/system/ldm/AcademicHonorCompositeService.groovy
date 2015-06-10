package net.hedtech.banner.general.system.ldm

import net.hedtech.banner.general.system.AcademicHonorView
import net.hedtech.banner.general.system.ldm.v4.AcademicHonor
import net.hedtech.banner.query.DynamicFinder
import net.hedtech.banner.query.QueryBuilder
import net.hedtech.banner.query.operators.Operators
import net.hedtech.banner.restfulapi.RestfulApiValidationUtility
import org.springframework.transaction.annotation.Transactional
/**
 * <p> REST End point for Academic Honor Service. If we'll pass type is award then , Departmental-honors will display .</p>
 * <p> If we'll pass type is distinction then, Institutional  Honors will display else, Both of 2 Honor will display</p>
 * @author Sitakant
 */
class AcademicHonorCompositeService {
    public static final String AWARD = "award"
    public static final String DISTINCTION = "distinction"
    public static final def allowedSortFields=['title']
    public static final def DEFAULT_SORTED_FIELD='code'
    public static final def ALLOWED_SEARCH_FIELD='type'
    public static final String LDM_NAME_INSTITUTIONAL = 'institutional-honors'
    public static final String LDM_NAME_DEPARTMENTAL = 'departmental-honors'
    public static final String DEFAULT_ORDER_TYPE='ASC'
    public static final Integer MAX_DEFAULT=10
    public static final Integer MAX_UPPER_LIMIT=15

    public final def SEARCH_PARAMETERS=['award', 'distinction']
    public final Map sortFieldMap=['title':'code']
    public final Map ldmToDomainFieldMap=[
            "award":LDM_NAME_DEPARTMENTAL,
            "distinction":LDM_NAME_INSTITUTIONAL
    ]


    @Transactional(readOnly = true)
    List<AcademicHonor> list(Map params) {
        List academicHonors = []
                List<AcademicHonorView> results = fetchAcademicHonorViewCriteria(params)

                results.each {
                    result->
                        def type=''
                        if (result.type==LDM_NAME_DEPARTMENTAL){
                            type= 'award'
                        }
                        if(result.type==LDM_NAME_INSTITUTIONAL){
                            type='distinction'
                        }
                        academicHonors << new AcademicHonor(result,type)
        }
        academicHonors
    }

     private def fetchAcademicHonorViewCriteria(Map params,boolean count=false) {
        def results
         RestfulApiValidationUtility.validateSortField(params.sort, allowedSortFields)
         def sortParam = params.sort
         params.sort = params?.sort ? sortFieldMap[params?.sort] : DEFAULT_SORTED_FIELD
         params?.order = params?.order ? params?.order : DEFAULT_ORDER_TYPE
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

    Long count(params) {
        return fetchAcademicHonorViewCriteria(params,true)
    }

}
