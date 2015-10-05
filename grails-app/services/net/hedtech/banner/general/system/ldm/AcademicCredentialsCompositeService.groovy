/*******************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.general.system.ldm

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.exceptions.NotFoundException
import net.hedtech.banner.general.overall.ldm.LdmService
import net.hedtech.banner.general.system.AcademicCredentialsView
import net.hedtech.banner.general.system.ldm.v4.AcademicCredentials
import net.hedtech.banner.query.DynamicFinder
import net.hedtech.banner.query.QueryBuilder
import net.hedtech.banner.query.operators.Operators
import net.hedtech.banner.restfulapi.RestfulApiValidationUtility
import org.springframework.transaction.annotation.Transactional

/**
* <p> REST End point for Academic Credential Service. If we'll pass type is degree then , Academic Credential degree type of data will return.</p>
* <p> If we'll pass type is honorary then, Academic Credential honorary type of data will return.</p>
 * <p>If we'll pass type is diploma then, Academic Credential diploma type of data will return.</p>
 * <p>If we'll pass type is certificate then, Academic Credential certificate type of data will return.</p>
 * <p> else, It will return all  type of Academic Credential data.</p>
*/
@Transactional
class AcademicCredentialsCompositeService {

    //Injection of transactional service
    def degreeService

    private static final String DEFAULT_SORT_FIELD = 'abbreviation'
    private static final String DEFAULT_ORDER_TYPE = 'ASC'
    private static final List allowedSortFields = [DEFAULT_SORT_FIELD,'type']

    /**
     * GET /api/academic-credentials
     * @param params
     * @return
     */
    @Transactional(readOnly = true)
    List<AcademicCredentials> list(Map params) {
        List<AcademicCredentials> academicCredentialsList=[]
        RestfulApiValidationUtility.correctMaxAndOffset(params, RestfulApiValidationUtility.MAX_DEFAULT, RestfulApiValidationUtility.MAX_UPPER_LIMIT)
        params?.sort = params?.sort ?: DEFAULT_SORT_FIELD
        params?.order = params?.order ?: DEFAULT_ORDER_TYPE
        RestfulApiValidationUtility.validateSortField(params.sort, allowedSortFields)
        RestfulApiValidationUtility.validateSortOrder(params.order)
        params.sort = LdmService.fetchBannerDomainPropertyForLdmField(params.sort)?:params.sort
        List<AcademicCredentialsView> academicCredentialList=fetchAcademicCredentialByCriteria(params)
        academicCredentialList.each { academicCredential ->
            academicCredentialsList << new AcademicCredentials(academicCredential)
        }
        return academicCredentialsList
    }

    /**
     * @return Count
     */
    @Transactional(readOnly = true)
    Long count(Map params) {
        fetchAcademicCredentialByCriteria(params,true)
    }

    /**
     * GET /api/academic-credentials/{guid}
     * @param guid
     * @return
     */
    @Transactional(readOnly = true)
    AcademicCredentials get(String guid) {
        AcademicCredentialsView academicCredential = AcademicCredentialsView.findByGuid(guid?.trim())
        if(!academicCredential){
            throw new ApplicationException("academicCredential", new NotFoundException())
        }
       return new AcademicCredentials(academicCredential)
    }


    private def fetchAcademicCredentialByCriteria(Map content, boolean count = false) {

        def result
        def filterMap = QueryBuilder.getFilterData(content)
        def params = filterMap.params
        def criteria = filterMap.criteria
        def pagingAndSortParams = filterMap.pagingAndSortParams

        if (content.containsKey("type")) {
            params.put("type", content.get("type").trim().toLowerCase())
            criteria.add([key: "type", binding: "type", operator: Operators.EQUALS])
        }
        def query = "from AcademicCredentialsView a where 1 = 1"

        DynamicFinder dynamicFinder = new DynamicFinder(AcademicCredentialsView.class, query, "a")
        if (count) {
            result = dynamicFinder.count([params: params, criteria: criteria])
        } else {
            result = dynamicFinder.find([params: params, criteria: criteria], pagingAndSortParams)
        }

        return result
    }
}
