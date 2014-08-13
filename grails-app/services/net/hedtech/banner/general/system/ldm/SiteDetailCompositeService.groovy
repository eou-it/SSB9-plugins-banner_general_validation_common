/** *******************************************************************************
 Copyright 2014 Ellucian Company L.P. and its affiliates.
 ********************************************************************************* */
package net.hedtech.banner.general.system.ldm

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.exceptions.NotFoundException
import net.hedtech.banner.general.overall.ldm.GlobalUniqueIdentifier
import net.hedtech.banner.general.overall.ldm.GlobalUniqueIdentifierService
import net.hedtech.banner.general.system.Campus
import net.hedtech.banner.general.system.College
import net.hedtech.banner.general.system.Site
import net.hedtech.banner.general.system.ldm.v1.Organization
import net.hedtech.banner.general.system.ldm.v1.OrganizationType
import net.hedtech.banner.general.system.ldm.v1.SiteDetail
import net.hedtech.banner.restfulapi.RestfulApiValidationUtility
import org.springframework.transaction.annotation.Transactional

/**
 * RESTful APIs for Site LDM (/base/domain/site/v1/site.json-schema)
 */
@Transactional
class SiteDetailCompositeService {

    private static final String CAMPUS_LDM_NAME = "campuses"
    private static final String COLLEGE_LDM_NAME = "colleges"

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
        Organization organization
        def buildings = []
        GlobalUniqueIdentifier globalUniqueIdentifier = GlobalUniqueIdentifier.fetchByLdmNameAndGuid(CAMPUS_LDM_NAME, guid)
        if (!globalUniqueIdentifier) {
            throw new ApplicationException(GlobalUniqueIdentifierService.API, new NotFoundException(id: Site.class.simpleName))
        }

        Campus campus = Campus.get(globalUniqueIdentifier.domainId)
        if (!campus) {
            throw new ApplicationException(GlobalUniqueIdentifierService.API, new NotFoundException(id: Site.class.simpleName))
        }

        List<College> colleges = collegeService.list(paginationParams) as List
        organization = new Organization(GlobalUniqueIdentifier.findByLdmNameAndDomainId(COLLEGE_LDM_NAME, colleges[0].id).guid, colleges[0], OrganizationType.COLLEGE.value)

        return new SiteDetail(guid, campus, organization, buildings);
    }

    /**
     * GET /api/sites
     *
     * @param map
     * @return
     */
    def list(Map map) {
        def sites = []
        Organization organization
        def buildings = []

        RestfulApiValidationUtility.correctMaxAndOffset(map, RestfulApiValidationUtility.MAX_DEFAULT, RestfulApiValidationUtility.MAX_UPPER_LIMIT)
        List<College> colleges = collegeService.list(paginationParams) as List
        List<Campus> campuses = campusService.list(map) as List
        campuses.each { campus ->
            organization = new Organization(GlobalUniqueIdentifier.findByLdmNameAndDomainId(COLLEGE_LDM_NAME, colleges[0].id).guid, colleges[0], OrganizationType.COLLEGE.value)
            sites << new SiteDetail(GlobalUniqueIdentifier.findByLdmNameAndDomainId(CAMPUS_LDM_NAME, campus.id).guid, campus, organization, buildings)
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
        Organization organization
        def buildings = []

        if (null == domainId) {
            return null
        }
        Campus campus = campusService.get(domainId)
        if (!campus) {
            return null
        }
        List<College> colleges = collegeService.list(paginationParams) as List
        organization = new Organization(GlobalUniqueIdentifier.findByLdmNameAndDomainId(COLLEGE_LDM_NAME, colleges[0].id).guid, colleges[0], OrganizationType.COLLEGE.value)
        return new SiteDetail(GlobalUniqueIdentifier.findByLdmNameAndDomainId(CAMPUS_LDM_NAME, domainId).guid, campus, organization, buildings)
    }


    SiteDetail fetchByCampusCode(String campusCode) {
        Organization organization
        def buildings = []

        if (!campusCode) {
            return null
        }
        Campus campus = campusService.fetchByCode(campusCode)
        if (!campus) {
            return null
        }
        List<College> colleges = collegeService.list(paginationParams) as List
        organization = new Organization(GlobalUniqueIdentifier.findByLdmNameAndDomainId(COLLEGE_LDM_NAME, colleges[0].id).guid, colleges[0], OrganizationType.COLLEGE.value)
        return new SiteDetail(GlobalUniqueIdentifier.findByLdmNameAndDomainId(CAMPUS_LDM_NAME, campus.id)?.guid, campus, organization, buildings)
    }

}
