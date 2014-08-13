/** *******************************************************************************
 Copyright 2014 Ellucian Company L.P. and its affiliates.
 ********************************************************************************* */
package net.hedtech.banner.general.system.ldm

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.exceptions.NotFoundException
import net.hedtech.banner.general.overall.ldm.GlobalUniqueIdentifier
import net.hedtech.banner.general.overall.ldm.GlobalUniqueIdentifierService
import net.hedtech.banner.general.overall.ldm.LdmService
import net.hedtech.banner.general.system.Campus
import net.hedtech.banner.general.system.Site
import net.hedtech.banner.general.system.ldm.v1.SiteDetail
import net.hedtech.banner.query.DynamicFinder
import net.hedtech.banner.query.QueryBuilder
import net.hedtech.banner.query.operators.Operators
import net.hedtech.banner.restfulapi.RestfulApiValidationUtility
import org.springframework.transaction.annotation.Transactional

/**
 * RESTful APIs for Site LDM (/base/domain/site/v1/site.json-schema)
 */
@Transactional
class SiteDetailCompositeService {

    private static final String LDM_NAME = "campuses"
    private static final String ABBREVIATION ='abbreviation'
    private static final String TITLE ='title'
    private static final String CODE ="code"
    private static final String DESCRIPTION ="description"
    private static final String QUERY = """from Campus a"""
    private static final String ENTITY ="a"


    def campusService
    def collegeService
    def globalUniqueIdentifierService
    def paginationParams = [max: '20', offset: '0']

    /**
     * GET /api/sites/<guid>
     *
     * @param guid
     * @return
     */
    @Transactional(readOnly = true)
    SiteDetail get(String guid) {
        GlobalUniqueIdentifier globalUniqueIdentifier = GlobalUniqueIdentifier.fetchByLdmNameAndGuid(LDM_NAME, guid)
        if (!globalUniqueIdentifier) {
            throw new ApplicationException(GlobalUniqueIdentifierService.API, new NotFoundException(id: Site.class.simpleName))
        }

        Campus campus = Campus.get(globalUniqueIdentifier.domainId)
        if (!campus) {
            throw new ApplicationException(GlobalUniqueIdentifierService.API, new NotFoundException(id: Site.class.simpleName))
        }
        return new SiteDetail(guid, campus);
    }

    /**
     * GET /api/sites
     *
     * @param map
     * @return
     */
    def list(Map map) {
        def sites = []

        RestfulApiValidationUtility.correctMaxAndOffset(map, RestfulApiValidationUtility.MAX_DEFAULT, RestfulApiValidationUtility.MAX_UPPER_LIMIT)

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
        DynamicFinder dynamicFinder = new DynamicFinder(Campus.class, QUERY, ENTITY)
        List<Campus> campuses = dynamicFinder.find([params: filterMap.params, criteria: filterMap.criteria], filterMap.pagingAndSortParams)

        campuses.each { campus ->
            sites << new SiteDetail(GlobalUniqueIdentifier.findByLdmNameAndDomainId(LDM_NAME, campus.id).guid, campus)
         }

        return sites
    }

    /**
     * GET /api/organizations
     *
     * @return
     */
    Long count() {
        return campusService.count()
    }


    SiteDetail fetchByCampusId(Long domainId) {
        if (null == domainId) {
            return null
        }
        Campus campus = campusService.get(domainId)
        if (!campus) {
            return null
        }
        return new SiteDetail(GlobalUniqueIdentifier.findByLdmNameAndDomainId(LDM_NAME, domainId).guid, campus)
    }


    SiteDetail fetchByCampusCode(String campusCode) {
        if (!campusCode) {
            return null
        }
        Campus campus = campusService.fetchByCode(campusCode)
        if (!campus) {
            return null
        }
        return new SiteDetail(GlobalUniqueIdentifier.findByLdmNameAndDomainId(LDM_NAME, campus.id)?.guid, campus)
    }

}
