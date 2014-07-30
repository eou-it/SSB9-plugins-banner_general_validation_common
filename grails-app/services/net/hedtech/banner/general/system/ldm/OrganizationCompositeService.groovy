/** *******************************************************************************
 Copyright 2014 Ellucian Company L.P. and its affiliates.
 ********************************************************************************* */
package net.hedtech.banner.general.system.ldm

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.exceptions.NotFoundException
import net.hedtech.banner.general.overall.ldm.GlobalUniqueIdentifier
import net.hedtech.banner.general.overall.ldm.GlobalUniqueIdentifierService
import net.hedtech.banner.general.system.College
import net.hedtech.banner.general.system.ldm.v1.Organization
import net.hedtech.banner.general.system.ldm.v1.OrganizationType
import net.hedtech.banner.restfulapi.RestfulApiValidationUtility
import org.springframework.transaction.annotation.Transactional

/**
 * RESTful APIs for Organization LDM (/base/domain/organization/v1/organization.json-schema)
 */
@Transactional
class OrganizationCompositeService {

    private static final String COLLEGE_LDM_NAME = "colleges"

    def collegeService
    def globalUniqueIdentifierService

    /**
     * GET /api/organizations/<guid>
     *
     * @param guid
     * @return
     */
    Organization get(String guid) {
        GlobalUniqueIdentifier globalUniqueIdentifier = globalUniqueIdentifierService.fetchByLdmNameAndGuid(COLLEGE_LDM_NAME, guid)
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

        RestfulApiValidationUtility.correctMaxAndOffset(map, 10, 30)

        List<College> colleges = collegeService.list(map) as List
        colleges.each { college ->
            organizations << new Organization(globalUniqueIdentifierService.fetchByLdmNameAndDomainId(COLLEGE_LDM_NAME, college.id), college, OrganizationType.COLLEGE.value)
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
        return new Organization(globalUniqueIdentifierService.fetchByLdmNameAndDomainId(COLLEGE_LDM_NAME, domainId), college, OrganizationType.COLLEGE.value)
    }


    Organization fetchByCollegeCode(String collegeCode) {
        if (!collegeCode) {
            return null
        }
        College college = collegeService.fetchByCode(collegeCode)
        if (!college) {
            return null
        }
        return new Organization(globalUniqueIdentifierService.fetchByLdmNameAndDomainId(COLLEGE_LDM_NAME, college.id), college, OrganizationType.COLLEGE.value)
    }

}
