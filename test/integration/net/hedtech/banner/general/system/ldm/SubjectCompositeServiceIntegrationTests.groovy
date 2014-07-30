/** *******************************************************************************
 Copyright 2014 Ellucian Company L.P. and its affiliates.
 ********************************************************************************* */
package net.hedtech.banner.general.system.ldm

import net.hedtech.banner.general.system.Subject
import net.hedtech.banner.general.system.ldm.v1.SubjectDetail
import net.hedtech.banner.testing.BaseIntegrationTestCase

class SubjectCompositeServiceIntegrationTests extends BaseIntegrationTestCase {

    Subject subjectResource
    def subjectCompositeService


    void setUp() {
        formContext = ['GUAGMNU']
        super.setUp()
        initiializeDataReferences()
    }


    private void initiializeDataReferences() {
        subjectResource = Subject.findByCode('OPEN')
    }

    /**
     * Test case for List method
     */
    void testList() {
        def paginationParams = [max: '20', offset: '0']
        List subjectList = subjectCompositeService.list(paginationParams)
        assertNotNull subjectList
        assertFalse subjectList.isEmpty()
        assertTrue subjectList.code.contains(subjectResource.code)
    }

    /**
     * Testcase for count method
     */
    void testCount() {
        assertTrue subjectCompositeService.count() > 0
    }

    /**
     * Testcase for show method
     */
    void testGet() {
        def paginationParams = [max: '1', offset: '0']
        List subjectList = subjectCompositeService.list(paginationParams)
        assertNotNull subjectList
        assertTrue subjectList.size()> 0
        assertNotNull subjectList[0].guid
        def subjectDetail = subjectCompositeService.get(subjectList[0].guid)
        assertNotNull subjectDetail
        assertEquals subjectList[0], subjectDetail
    }

    /**
     * Testcase for fetchBySubjectId method
     */
    void testFetchBySubjectId() {
        def subjectDetail = subjectCompositeService.fetchBySubjectId(subjectResource.id)
        assertNotNull subjectDetail
        assertEquals subjectResource.id, subjectDetail.id
        assertEquals subjectResource.code , subjectDetail.code
        assertEquals subjectResource.description, subjectDetail.description

    }

    /**
     * Testcase for fetchBySubjectCode
     */
    void testFetchFetchBySubjectCode() {
        SubjectDetail subjectDetail = subjectCompositeService.fetchBySubjectCode(subjectResource.code)
        assertNotNull subjectDetail
        assertEquals subjectResource.id, subjectDetail.id
        assertEquals subjectResource.code, subjectDetail.code
        assertEquals subjectResource.description, subjectDetail.description

    }
}
