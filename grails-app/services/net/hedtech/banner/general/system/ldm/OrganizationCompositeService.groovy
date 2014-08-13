/** *******************************************************************************
 Copyright 2014 Ellucian Company L.P. and its affiliates.
 ********************************************************************************* */
package net.hedtech.banner.general.system.ldm

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.exceptions.NotFoundException
import net.hedtech.banner.general.overall.ldm.GlobalUniqueIdentifier
import net.hedtech.banner.general.overall.ldm.GlobalUniqueIdentifierService
import net.hedtech.banner.general.overall.ldm.LdmService
import net.hedtech.banner.general.system.College
import net.hedtech.banner.general.system.ldm.v1.Organization
import net.hedtech.banner.general.system.ldm.v1.OrganizationType
import net.hedtech.banner.query.DynamicFinder
import net.hedtech.banner.query.QueryBuilder
import net.hedtech.banner.query.operators.Operators
import net.hedtech.banner.restfulapi.RestfulApiValidationUtility
import org.springframework.transaction.annotation.Transactional

/**
 * RESTful APIs for Organization LDM (/base/domain/organization/v1/organization.json-schema)
 */
@Transactional
class OrganizationCompositeService {

    private static final String LDM_NAME = "colleges"
    private static final String ABBREVIATION ='abbreviation'
    private static final String TITLE ='title'
    private static final String CODE ="code"
    private static final String DESCRIPTION ="description"
    private static final String QUERY = """from College a"""
    private static final String ENTITY ="a"

    def collegeService
    def globalUniqueIdentifierService

    /**
     * GET /api/organizations/<guid>
     *
     * @param guid
     * @return
     */
    @Transactional(readOnly = true)
    Organization get(String guid) {
        GlobalUniqueIdentifier globalUniqueIdentifier = GlobalUniqueIdentifier.fetchByLdmNameAndGuid(LDM_NAME, guid)
        if (!globalUniqueIdentifier) {
            throw new ApplicationException(GlobalUniqueIdentifierService.API, new NotFoundException(id: College.class.simpleName))
        }

        College college = College.get(globalUniqueIdentifier.domainId)
        if (!college) {
            throw new ApplicationException(GlobalUniqueIdentifierService.API, new NotFoundException(id: College.class.simpleName))
        }

        return new Organization(guid, college, OrganizationType.COLLEGE.value);
    }

    /**
     * GET /api/organizations
     *
     * @param map
     * @return
     */
    def list(Map map) {
        def organizations = []

        RestfulApiValidationUtility.correctMaxAndOffset(map, RestfulApiValidationUtility.MAX_DEFAULT, RestfulApiValidationUtility.MAX_UPPER_LIMIT)

        List allowedSortFields = ['abbreviation', 'title']
        RestfulApiValidationUtility.validateSortField(map.sort, allowedSortFields)
        RestfulApiValidationUtility.validateSortOrder(map.order)
        map.sort = LdmService.fetchBannerDomainPropertyForLdmField(map.sort)
        def filters = QueryBuilder.createFilters(map)
        def allowedSearchFields = [CODE, DESCRIPTION]
        def allowedOperators = [Operators.EQUALS, Operators.EQUALS_IGNORE_CASE, Operators.CONTAINS, Operators.STARTS_WITH]
        RestfulApiValidationUtility.validateCriteria(filters, allowedSearchFields, allowedOperators)
        RestfulApiValidationUtility.validateSortField(map.sort,allowedSearchFields)
        def filterMap = QueryBuilder.getFilterData(map)
        DynamicFinder dynamicFinder = new DynamicFinder(College.class, QUERY, ENTITY)
        List<College> colleges = dynamicFinder.find([params: filterMap.params, criteria: filterMap.criteria], filterMap.pagingAndSortParams)

        colleges.each { college ->
            organizations << new Organization(GlobalUniqueIdentifier.findByLdmNameAndDomainId(LDM_NAME, college.id).guid, college, OrganizationType.COLLEGE.value)
        }

        return organizations
    }

    /**
     * GET /api/organizations
     *
     * @return
     */
    Long count() {
        return collegeService.count()
    }


    Organization fetchByCollegeId(Long domainId) {
        if (null == domainId) {
            return null
        }
        College college = collegeService.get(domainId)
        if (!college) {
            return null
        }
        return new Organization(GlobalUniqueIdentifier.findByLdmNameAndDomainId(LDM_NAME, domainId).guid, college, OrganizationType.COLLEGE.value)
    }


    Organization fetchByCollegeCode(String collegeCode) {
        if (!collegeCode) {
            return null
        }
        College college = collegeService.fetchByCode(collegeCode)
        if (!college) {
            return null
        }
        return new Organization(GlobalUniqueIdentifier.findByLdmNameAndDomainId(LDM_NAME, college.id).guid, college, OrganizationType.COLLEGE.value)
    }

}
