/** *******************************************************************************
 Copyright 2014 Ellucian Company L.P. and its affiliates.
 ********************************************************************************* */
package net.hedtech.banner.general.system.ldm

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.general.system.College
import net.hedtech.banner.general.system.Department
import net.hedtech.banner.general.system.ldm.v1.Organization
import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.junit.Before
import org.junit.Test

class OrganizationCompositeServiceIntegrationTests extends BaseIntegrationTestCase {

    College college
    def organizationCompositeService

    @Before
    public void setUp() {
        formContext = ['GUAGMNU']
        super.setUp()
        initiializeDataReferences()
    }


    private void initiializeDataReferences() {
        college = College.findByCode('AH')
    }

    /**
     * Test case for List method
     */
    @Test
    void testListWithPagination() {
        def paginationParams = [max: '20', offset: '0']
        List organizationList = organizationCompositeService.list(paginationParams)
        assertNotNull organizationList
        assertFalse organizationList.isEmpty()
        assertTrue organizationList.abbreviation.contains(college.code)
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
        assertNotNull organization
        assertEquals organizationList[0].abbreviation, organization.abbreviation
        assertEquals organizationList[0].title, organization.title
        assertEquals organizationList[0].metadata.dataOrigin, organization.metadata.dataOrigin
        assertEquals organizationList[0].guid, organization.guid
        assertEquals organizationList[0].organizationType, organization.organizationType
    }

    /**
     * Testcase for show method with ApplicationException
     */
    @Test
    void testGetWithInvalidGuid() {
        shouldFail( ApplicationException  ) {
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
     * Testcase for fetchByCollegeCode
     */
    @Test
    void testFetchFetchByCollegeCode() {
        Organization organization = organizationCompositeService.fetchByCollegeCode(college.code)
        assertNotNull organization
        assertEquals college.code, organization.abbreviation
        assertEquals college.description, organization.title
        assertEquals college.dataOrigin, organization.metadata.dataOrigin
    }

}
