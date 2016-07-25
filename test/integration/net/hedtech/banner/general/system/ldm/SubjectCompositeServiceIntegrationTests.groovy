/** *******************************************************************************
 Copyright 2014-2015 Ellucian Company L.P. and its affiliates.
 ********************************************************************************* */
package net.hedtech.banner.general.system.ldm

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.general.system.Subject
import net.hedtech.banner.general.system.ldm.v1.SubjectDetail
import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.junit.Before
import org.junit.Test

class SubjectCompositeServiceIntegrationTests extends BaseIntegrationTestCase {

    def subjectCompositeService

    Subject subjectResource
    Map i_success_content
    String u_success_description = 'Subject 1 updated description'


    @Before
    public void setUp() {
        formContext = ['GUAGMNU']
        super.setUp()
        initiializeDataReferences()
    }


    private void initiializeDataReferences() {
        subjectResource = Subject.findByCode('AC')
        i_success_content = [code: 'S1', description: 'Subject 1', metadata: [dataOrigin: 'Banner']]
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
        assertTrue subjectList.size() > 0
        assertNotNull subjectList[0].guid
        def subjectDetail = subjectCompositeService.get(subjectList[0].guid)
        assertNotNull subjectDetail
        assertEquals subjectList[0], subjectDetail
        assertEquals subjectList[0].guid, subjectDetail.guid
        assertEquals subjectList[0].code, subjectDetail.code
        assertEquals subjectList[0].description, subjectDetail.description
        assertEquals subjectList[0].metadata.dataOrigin, subjectDetail.metadata.dataOrigin
        assertEquals subjectList[0], subjectDetail
    }
    /**
     * Testcase for show method with ApplicationException
     */
    @Test
    void testGetWithInvalidGuid() {
        shouldFail(ApplicationException) {
            subjectCompositeService.get(null)
        }

    }

    /**
     * Testcase for fetchBySubjectId method
     */
    @Test
    void testFetchBySubjectId() {
        def subjectDetail = subjectCompositeService.fetchBySubjectId(subjectResource.id)
        assertNotNull subjectDetail.toString()
        assertEquals subjectResource.id, subjectDetail.id
        assertEquals subjectResource.code, subjectDetail.code
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


    @Test
    void testCreateNoCode() {
        i_success_content.remove('code')
        try {
            SubjectDetail subjectDetail = subjectCompositeService.create(i_success_content)
        } catch (Exception ae) {
            assertApplicationException ae, "code.required.message"
        }
    }


    @Test
    void testCreateNoDesc() {
        i_success_content.remove('description')
        try {
            SubjectDetail subjectDetail = subjectCompositeService.create(i_success_content)
        } catch (Exception ae) {
            assertApplicationException ae, "description.required.message"
        }
    }


    @Test
    void testCreate() {
        SubjectDetail subjectDetail = subjectCompositeService.create(i_success_content)
        assertNotNull subjectDetail
        assertEquals i_success_content.code, subjectDetail.code
        assertEquals i_success_content.description, subjectDetail.description
        assertEquals i_success_content.metadata.dataOrigin, subjectDetail.dataOrigin
    }


    @Test
    void testUpdate() {
        SubjectDetail subjectDetail = subjectCompositeService.create(i_success_content)
        assertNotNull subjectDetail
        i_success_content.put('id', subjectDetail.guid)
        i_success_content.put('description', u_success_description)
        subjectDetail = subjectCompositeService.update(i_success_content)
        assertEquals i_success_content.code, subjectDetail.code
        assertEquals u_success_description, subjectDetail.description
        assertEquals i_success_content.metadata.dataOrigin, subjectDetail.dataOrigin
    }

}
