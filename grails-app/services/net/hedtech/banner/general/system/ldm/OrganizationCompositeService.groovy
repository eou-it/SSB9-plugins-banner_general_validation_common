/** *******************************************************************************
 Copyright 2014 Ellucian Company L.P. and its affiliates.
 ********************************************************************************* */
package net.hedtech.banner.general.system.ldm

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.exceptions.NotFoundException
import net.hedtech.banner.general.overall.ldm.GlobalUniqueIdentifier
import net.hedtech.banner.general.system.College
import net.hedtech.banner.general.system.Department
import net.hedtech.banner.general.system.ldm.v1.Metadata
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
    private static final String DEPARTMENT_LDM_NAME = "departments"

    def collegeService
    def departmentService

    // For Hibernate current session
    def sessionFactory

    /**
     * GET /api/organizations/<guid>
     *
     * @param guid
     * @return
     */
    @Transactional(readOnly = true)
    Organization get(String guid) {
        def organization
        GlobalUniqueIdentifier globalUniqueIdentifier = GlobalUniqueIdentifier.fetchByLdmNamesAndGuid(guid)

        String organizationLdmName = globalUniqueIdentifier?.ldmName?.toLowerCase()
        String organizationType = null

        switch (organizationLdmName) {
            case COLLEGE_LDM_NAME:
                //Organization of type college
                organization = College.get(globalUniqueIdentifier.domainId)
                organizationType = OrganizationType.COLLEGE.value
                break

            case DEPARTMENT_LDM_NAME:
                //Organization of type department
                organization = Department.get(globalUniqueIdentifier.domainId)
                organizationType = OrganizationType.DEPARTMENT.value
                break

            default:
                throw new ApplicationException("organization", new NotFoundException())
        }

        return new Organization(guid, organization.code, organization.description, organizationType, new Metadata(organization.dataOrigin))
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

        def records = getOrganizationsFromDB(map.max?.toInteger(), map.offset?.toInteger() ?: 0, map.sort, map.order, false)

        records.each {
            organizations << new Organization(it.guid, it.abbreviation, it.title, it.organizationType, new Metadata(it.dataOrigin))
        }

        return organizations
    }

    /**
     * GET /api/organizations
     *
     * @return
     */
    Long count() {
        return getOrganizationsFromDB(0, 0, null, null, true)
    }


    Organization fetchByCollegeId(Long domainId) {
        if (null == domainId) {
            return null
        }
        College college = collegeService.get(domainId)
        if (!college) {
            return null
        }
        return new Organization(GlobalUniqueIdentifier.findByLdmNameAndDomainId(COLLEGE_LDM_NAME, domainId).guid, college.code, college.description, OrganizationType.COLLEGE.value, new Metadata(college.dataOrigin))
    }


    Organization fetchByCollegeCode(String collegeCode) {
        if (!collegeCode) {
            return null
        }
        College college = collegeService.fetchByCode(collegeCode)
        if (!college) {
            return null
        }
        log.debug( "Start of GlobalUniqueIdentifier.findByLdmNameAndDomainId + Organization-colleges" )
        return new Organization(GlobalUniqueIdentifier.findByLdmNameAndDomainId(COLLEGE_LDM_NAME, college.id).guid, college.code, college.description, OrganizationType.COLLEGE.value, new Metadata(college.dataOrigin))
        log.debug( "End of GlobalUniqueIdentifier.findByLdmNameAndDomainId + Organization-colleges" )
    }


    Organization fetchByDepartmentId(Long domainId) {
        if (null == domainId) {
            return null
        }
        Department department = departmentService.get(domainId)
        if (!department) {
            return null
        }
        return new Organization(GlobalUniqueIdentifier.findByLdmNameAndDomainId(DEPARTMENT_LDM_NAME, domainId)?.guid, department.code, department.description, OrganizationType.DEPARTMENT.value, new Metadata(department.dataOrigin))
    }


    Organization fetchByDepartmentCode(String departmentCode) {
        if (!departmentCode) {
            return null
        }
        Department department = Department.findByCode(departmentCode)
        if (!department) {
            return null
        }
        log.debug( "Start of GlobalUniqueIdentifier.findByLdmNameAndDomainId + Organization-departments" )
        return new Organization(GlobalUniqueIdentifier.findByLdmNameAndDomainId(DEPARTMENT_LDM_NAME, department.id)?.guid, department.code, department.description, OrganizationType.DEPARTMENT.value, new Metadata(department.dataOrigin))
        log.debug( "End of GlobalUniqueIdentifier.findByLdmNameAndDomainId + Organization-departments" )
    }


    private def getOrganizationsFromDB(int max, int offset, String sortColumn, String sortOrder, boolean count = false) {
        String selectFragment = count ? "SELECT count(*)" : "SELECT *"

        String fromFragment = """ FROM (SELECT a.gorguid_guid, b.stvcoll_code AS abbreviation, b.stvcoll_desc AS title, '${OrganizationType.COLLEGE.value}' AS organizationType, b.stvcoll_surrogate_id, b.stvcoll_data_origin FROM gorguid a, stvcoll b
                                        WHERE a.gorguid_ldm_name = '${COLLEGE_LDM_NAME}' AND a.gorguid_domain_surrogate_id = b.stvcoll_surrogate_id
                                        UNION ALL
                                        SELECT a.gorguid_guid, b.stvdept_code AS abbreviation, b.stvdept_desc AS title, '${OrganizationType.DEPARTMENT.value}' AS organizationType, b.stvdept_surrogate_id, b.stvdept_data_origin FROM gorguid a, stvdept b
                                        WHERE a.gorguid_ldm_name = '${DEPARTMENT_LDM_NAME}' AND a.gorguid_domain_surrogate_id = b.stvdept_surrogate_id)"""

        String whereFragment = ""
        String orderFragment = ""

        if (!count && sortColumn) {
            orderFragment = " ORDER BY ${sortColumn}"
            if (sortOrder) {
                orderFragment += " ${sortOrder}"
            }
        }

        String sql = "$selectFragment$fromFragment$whereFragment$orderFragment"

        // Get the current Hiberante session
        def session = sessionFactory.getCurrentSession()
        def sqlQuery = session.createSQLQuery(sql);

        if(!count) {
            // Pagination
            sqlQuery.setFirstResult(offset)
            sqlQuery.setMaxResults(max)
        }

        final queryResults = sqlQuery.list()
        def results
        if(!count) {
            results = queryResults?.collect {
                [guid: it[0], abbreviation: it[1], title: it[2], organizationType: it[3], surrogateId: it[4], dataOrigin: it[5]]
            }
        } else {
            results = queryResults[0]
        }

        return results
    }


}
