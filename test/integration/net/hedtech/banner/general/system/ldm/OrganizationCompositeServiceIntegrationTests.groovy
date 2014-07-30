/** *******************************************************************************
 Copyright 2014 Ellucian Company L.P. and its affiliates.
 ********************************************************************************* */
package net.hedtech.banner.general.system.ldm

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
        college = College.findByCode('OPEN')
    }

    /**
     * Test case for List method
     */
    void testList() {
        def paginationParams = [max: '20', offset: '0']
        List collegeList = organizationCompositeService.list(paginationParams)
        assertNotNull collegeList
        assertFalse collegeList.isEmpty()
        assertTrue collegeList.code.contains(college.code)
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
        def paginationParams = [max: '1', offset: '0']
        List collegeList = organizationCompositeService.list(paginationParams)
        assertNotNull collegeList
        assertTrue collegeList.size() > 0
        assertNotNull collegeList[0].guid
        def collegeDetail = organizationCompositeService.get(collegeList[0].guid)
        assertNotNull collegeDetail
        assertEquals collegeList[0], collegeDetail
    }

    /**
     * Testcase for fetchByCollegeId method
     */
    void testFetchByCollegeId() {
        def collegeDetail = organizationCompositeService.fetchByCollegeId(college.id)
        assertNotNull collegeDetail
        assertEquals college.id, collegeDetail.id
        assertEquals college.code, collegeDetail.code
        assertEquals college.description, collegeDetail.description

    }

    /**
     * Testcase for fetchByCollegeCode
     */
    void testFetchFetchByCollegeCode() {
        Organization collegeDetail = organizationCompositeService.fetchByCollegeCode(college.code)
        assertNotNull collegeDetail
        assertEquals college.id, collegeDetail.id
        assertEquals college.code, collegeDetail.code
        assertEquals college.description, collegeDetail.description
    }

}
