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
        GlobalUniqueIdentifier globalUniqueIdentifier = GlobalUniqueIdentifier.fetchByLdmNameAndGuid(CAMPUS_LDM_NAME, guid)
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
        List<Campus> campuses = campusService.list(map) as List
        campuses.each { campus ->
            sites << new SiteDetail(GlobalUniqueIdentifier.findByLdmNameAndDomainId(CAMPUS_LDM_NAME, campus.id).guid, campus)
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
        return new SiteDetail(GlobalUniqueIdentifier.findByLdmNameAndDomainId(CAMPUS_LDM_NAME, domainId).guid, campus)
    }


    SiteDetail fetchByCampusCode(String campusCode) {
        if (!campusCode) {
            return null
        }
        Campus campus = campusService.fetchByCode(campusCode)
        if (!campus) {
            return null
        }
        return new SiteDetail(GlobalUniqueIdentifier.findByLdmNameAndDomainId(CAMPUS_LDM_NAME, campus.id)?.guid, campus)
    }

}
