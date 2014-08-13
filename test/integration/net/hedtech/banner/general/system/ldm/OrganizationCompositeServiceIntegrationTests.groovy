/** *******************************************************************************
 Copyright 2014 Ellucian Company L.P. and its affiliates.
 ********************************************************************************* */
package net.hedtech.banner.general.system.ldm

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.general.system.College
import net.hedtech.banner.general.system.ldm.v1.Organization
import net.hedtech.banner.testing.BaseIntegrationTestCase

class OrganizationCompositeServiceIntegrationTests extends BaseIntegrationTestCase {

    College college
    def organizationCompositeService


    void setUp() {
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
    void testList() {
        def paginationParams = [max: '20', offset: '0']
        List organizationList = organizationCompositeService.list(paginationParams)
        assertNotNull organizationList
        assertFalse organizationList.isEmpty()
        assertTrue organizationList.code.contains(college.code)
    }

    /**
     * Testcase for count method
     */
    void testCount() {
        assertTrue organizationCompositeService.count() > 0
    }

    /**
     * Testcase for show method
     */
    void testGet() {
        def paginationParams = [max: '20', offset: '0']
        List organizationList = organizationCompositeService.list(paginationParams)
        assertNotNull organizationList
        assertTrue organizationList.size() > 0
        assertNotNull organizationList[0].guid
        def organization = organizationCompositeService.get(organizationList[0].guid)
        assertNotNull organization
        assertEquals organizationList[0].code, organization.code
    }

    /**
     * Testcase for show method with ApplicationException
     */
    void testGetWithInvalidGuid() {
        shouldFail( ApplicationException  ) {
            organizationCompositeService.get(null)
        }

    }

    /**
     * Testcase for fetchByCollegeId method
     */
    void testFetchByCollegeId() {
        def organization = organizationCompositeService.fetchByCollegeId(college.id)
        assertNotNull organization
        assertEquals college.id, organization.id
        assertEquals college.code, organization.code
        assertEquals college.description, organization.description

    }

    /**
     * Testcase for fetchByCollegeCode
     */
    void testFetchFetchByCollegeCode() {
        Organization organization = organizationCompositeService.fetchByCollegeCode(college.code)
        assertNotNull organization
        assertEquals college.id, organization.id
        assertEquals college.code, organization.code
        assertEquals college.description, organization.description
    }

}
