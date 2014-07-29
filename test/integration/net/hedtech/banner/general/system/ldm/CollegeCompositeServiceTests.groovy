/** *******************************************************************************
 Copyright 2014 Ellucian Company L.P. and its affiliates.
 ********************************************************************************* */
package net.hedtech.banner.general.system.ldm

import net.hedtech.banner.general.system.College
import net.hedtech.banner.general.system.ldm.v1.CollegeDetail
import net.hedtech.banner.testing.BaseIntegrationTestCase

class CollegeCompositeServiceTests extends BaseIntegrationTestCase {

    College collegeResource
    def collegeCompositeService


    void setUp() {
        formContext = ['GUAGMNU']
        super.setUp()
        initiializeDataReferences()
    }


    private void initiializeDataReferences() {
        collegeResource = College.findByCode('OPEN')
    }

    /**
     * Test case for List method
     */
    void testList() {
        def paginationParams = [max: '20', offset: '0']
        List collegeList = collegeCompositeService.list(paginationParams)
        assertNotNull collegeList
        assertFalse collegeList.isEmpty()
        assertTrue collegeList.code.contains(collegeResource.code)
    }

    /**
     * Testcase for count method
     */
    void testCount() {
        assertTrue collegeCompositeService.count() > 0
    }

    /**
     * Testcase for show method
     */
    void testGet() {
        def paginationParams = [max: '1', offset: '0']
        List collegeList = collegeCompositeService.list(paginationParams)
        assertNotNull collegeList
        assertTrue collegeList.size()> 0
        assertNotNull collegeList[0].guid
        def collegeDetail = collegeCompositeService.get(collegeList[0].guid)
        assertNotNull collegeDetail
        assertEquals collegeList[0], collegeDetail
    }

    /**
     * Testcase for fetchByCollegeId method
     */
    void testFetchByCollegeId() {
        def collegeDetail = collegeCompositeService.fetchByCollegeId(collegeResource.id)
        assertNotNull collegeDetail
        assertEquals collegeResource.id, collegeDetail.id
        assertEquals collegeResource.code , collegeDetail.code
        assertEquals collegeResource.description, collegeDetail.description

    }

    /**
     * Testcase for fetchByCollegeCode
     */
    void testFetchFetchByCollegeCode() {
        CollegeDetail collegeDetail = collegeCompositeService.fetchByCollegeCode(collegeResource.code)
        assertNotNull collegeDetail
        assertEquals collegeResource.id, collegeDetail.id
        assertEquals collegeResource.code, collegeDetail.code
        assertEquals collegeResource.description, collegeDetail.description

    }
}
