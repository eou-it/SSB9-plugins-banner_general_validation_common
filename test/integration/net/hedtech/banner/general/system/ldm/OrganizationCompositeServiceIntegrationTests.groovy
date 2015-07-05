/** *******************************************************************************
 Copyright 2014-2015 Ellucian Company L.P. and its affiliates.
 ********************************************************************************* */
package net.hedtech.banner.general.system.ldm

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.general.overall.ldm.GlobalUniqueIdentifier
import net.hedtech.banner.general.system.College
import net.hedtech.banner.general.system.Department
import net.hedtech.banner.general.system.ldm.v1.Organization
import net.hedtech.banner.general.system.ldm.v1.OrganizationType
import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.junit.Before
import org.junit.Test

class OrganizationCompositeServiceIntegrationTests extends BaseIntegrationTestCase {

    College college
    Department department
    def organizationCompositeService

    @Before
    public void setUp() {
        formContext = ['GUAGMNU']
        super.setUp()
        initiializeDataReferences()
    }


    private void initiializeDataReferences() {
        college = College.findByCode('AH')
        department = Department.findByCode('ECON')
    }

    /**
     * Test case for List method
     */
    @Test
    void testListWithSortAndPagination() {
        def paginationParams = [max: '20', offset: '0', sort: 'abbreviation', order: 'asc']
        List organizationList = organizationCompositeService.list(paginationParams)
        assertNotNull organizationList
        assertFalse organizationList.isEmpty()
    }

    /**
     * Testcase for count method
     */
    @Test
    void testCount() {
        Long count = College.count()
        count += Department.count()

        assertEquals count, organizationCompositeService.count()
    }

    /**
     * Testcase for show method
     */
    @Test
    void testGet() {
        def paginationParams = [max: '20', offset: '0']
        List organizationList = organizationCompositeService.list(paginationParams)
        assertNotNull organizationList
        assertTrue organizationList.size() > 0
        assertNotNull organizationList[0].guid
        def organization = organizationCompositeService.get(organizationList[0].guid)
        assertNotNull organization.toString()
        assertEquals organizationList[0].abbreviation, organization.abbreviation
        assertEquals organizationList[0].title, organization.title
        assertEquals organizationList[0].metadata.dataOrigin, organization.metadata.dataOrigin
        assertEquals organizationList[0].guid, organization.guid
        assertEquals organizationList[0].organizationType, organization.organizationType
        assertEquals organizationList[0], organization
    }

    /**
     * Testcase for show method
     */
    @Test
    void testGetByDepartmentGuid() {
        String departmentGuid = GlobalUniqueIdentifier.findByLdmNameAndDomainKey('departments', department.code)?.guid
        def organization = organizationCompositeService.get(departmentGuid)
        assertNotNull organization
        assertEquals department.code, organization.abbreviation
        assertEquals department.description, organization.title
        assertEquals department.dataOrigin, organization.metadata.dataOrigin
        assertEquals departmentGuid, organization.guid
        assertEquals OrganizationType.DEPARTMENT.value, organization.organizationType
    }

    /**
     * Testcase for show method with ApplicationException
     */
    @Test
    void testGetWithInvalidGuid() {
        shouldFail(ApplicationException) {
            organizationCompositeService.get(null)
        }
    }

    /**
     * Testcase for fetchByCollegeId method
     */
    @Test
    void testFetchByCollegeId() {
        def organization = organizationCompositeService.fetchByCollegeId(college.id)
        assertNotNull organization
        assertEquals college.code, organization.abbreviation
        assertEquals college.description, organization.title
        assertEquals college.dataOrigin, organization.metadata.dataOrigin
    }

    /**
     * Testcase for fetchByCollegeCode method
     */
    @Test
    void testFetchByCollegeCode() {
        Organization organization = organizationCompositeService.fetchByCollegeCode(college.code)
        assertNotNull organization
        assertEquals college.code, organization.abbreviation
        assertEquals college.description, organization.title
        assertEquals college.dataOrigin, organization.metadata.dataOrigin
    }

    /**
     * Testcase for fetchByDepartmentId method
     */
    @Test
    void testFetchByDepartmentId() {
        def organization = organizationCompositeService.fetchByDepartmentId(department.id)
        assertNotNull organization
        assertEquals department.code, organization.abbreviation
        assertEquals department.description, organization.title
        assertEquals department.dataOrigin, organization.metadata.dataOrigin
    }

    /**
     * Testcase for fetchByDepartmentCode method
     */
    @Test
    void testFetchByDepartmentCode() {
        Organization organization = organizationCompositeService.fetchByDepartmentCode(department.code)
        assertNotNull organization
        assertEquals department.code, organization.abbreviation
        assertEquals department.description, organization.title
        assertEquals department.dataOrigin, organization.metadata.dataOrigin
    }

}
