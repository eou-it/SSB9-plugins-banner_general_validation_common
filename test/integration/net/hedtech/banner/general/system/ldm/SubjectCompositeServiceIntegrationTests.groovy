/** *******************************************************************************
 Copyright 2014 Ellucian Company L.P. and its affiliates.
 ********************************************************************************* */
package net.hedtech.banner.general.system.ldm

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.general.system.Subject
import net.hedtech.banner.general.system.ldm.v1.SubjectDetail
import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.junit.Before
import org.junit.Test

class SubjectCompositeServiceIntegrationTests extends BaseIntegrationTestCase {

    Subject subjectResource
    def subjectCompositeService

    @Before
    public void setUp() {
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
    @Test
    void testListWithPagination() {
        def paginationParams = [max: '20', offset: '0']
        List subjectList = subjectCompositeService.list(paginationParams)
        assertNotNull subjectList
        assertFalse subjectList.isEmpty()
        assertTrue subjectList.code.contains(subjectResource.code)
    }

    /**
     * Testcase for count method
     */
    @Test
    void testCount() {
        assertEquals Subject.count(), subjectCompositeService.count()
    }

    /**
     * Testcase for show method
     */
    @Test
    void testGet() {
        def paginationParams = [max: '1', offset: '0']
        List subjectList = subjectCompositeService.list(paginationParams)
        assertNotNull subjectList
        assertTrue subjectList.size()> 0
        assertNotNull subjectList[0].guid
        def subjectDetail = subjectCompositeService.get(subjectList[0].guid)
        assertNotNull subjectDetail
        assertEquals subjectList[0], subjectDetail
        assertEquals subjectList[0].guid, subjectDetail.guid
        assertEquals subjectList[0].code, subjectDetail.code
        assertEquals subjectList[0].description, subjectDetail.description
        assertEquals subjectList[0].metadata.dataOrigin, subjectDetail.metadata.dataOrigin
    }
    /**
     * Testcase for show method with ApplicationException
     */
    @Test
    void testGetWithInvalidGuid() {
        shouldFail( ApplicationException  ) {
            subjectCompositeService.get(null)
        }

    }

    /**
     * Testcase for fetchBySubjectId method
     */
    @Test
    void testFetchBySubjectId() {
        def subjectDetail = subjectCompositeService.fetchBySubjectId(subjectResource.id)
        assertNotNull subjectDetail
        assertEquals subjectResource.id, subjectDetail.id
        assertEquals subjectResource.code , subjectDetail.code
        assertEquals subjectResource.description, subjectDetail.description
        assertEquals subjectResource.dataOrigin, subjectDetail.metadata.dataOrigin
    }

    /**
     * Testcase for fetchBySubjectCode
     */
    @Test
    void testFetchFetchBySubjectCode() {
        SubjectDetail subjectDetail = subjectCompositeService.fetchBySubjectCode(subjectResource.code)
        assertNotNull subjectDetail
        assertEquals subjectResource.id, subjectDetail.id
        assertEquals subjectResource.code, subjectDetail.code
        assertEquals subjectResource.description, subjectDetail.description
        assertEquals subjectResource.dataOrigin, subjectDetail.metadata.dataOrigin
    }
}
