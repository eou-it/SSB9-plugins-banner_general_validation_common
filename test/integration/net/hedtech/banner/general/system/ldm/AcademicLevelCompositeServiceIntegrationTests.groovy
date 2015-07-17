/** *******************************************************************************
 Copyright 2014-2015 Ellucian Company L.P. and its affiliates.
 ********************************************************************************* */
package net.hedtech.banner.general.system.ldm

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.general.system.Level
import net.hedtech.banner.general.system.ldm.v1.AcademicLevel
import net.hedtech.banner.restfulapi.RestfulApiValidationException
import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.junit.Before
import org.junit.Test

/**
 * AcademicLevelCompositeServiceIntegrationTests.
 *
 * Date: 7/24/14
 * Time: 4:33 PM
 */
class AcademicLevelCompositeServiceIntegrationTests extends BaseIntegrationTestCase {

    def academicLevelCompositeService

    Level i_success_level

    def i_success_code = "TZ"
    def i_success_description = "Test Description"
    def i_success_academicIndicator = true
    def i_success_continuingEducationIndicator = true

    def i_success_systemRequiredIndicator = true
    def i_success_voiceResponseMessageNumber = 1

    def i_success_electronicDataInterchangeEquivalent = "TT"

    @Before
    public void setUp() {
        formContext = ['GUAGMNU']
        super.setUp()
        initiializeDataReferences()
    }


    private void initiializeDataReferences() {
        i_success_level = Level.findByCode('LW')
    }

    @Test
    void testListWithoutPaginationParams() {
        List academicLevels = academicLevelCompositeService.list([:])
        assertNotNull academicLevels
        assertFalse academicLevels.isEmpty()
        assertTrue academicLevels.code.contains(i_success_level.code)
    }

    @Test
    void testListWithPagination() {
        def paginationParams = [max: '20', offset: '0']
        List academicLevels = academicLevelCompositeService.list(paginationParams)
        assertNotNull academicLevels
        assertFalse academicLevels.isEmpty()
        assertTrue academicLevels.code.contains(i_success_level.code)
    }

    @Test
    void testCount() {
        assertNotNull i_success_level
        assertEquals Level.count(), academicLevelCompositeService.count()
    }

    @Test
    void testGetInvalidGuid() {
        try {
            academicLevelCompositeService.get('Invalid-guid')
        } catch (ApplicationException ae) {
            assertApplicationException ae, "NotFoundException"
        }
    }


    @Test
    void testGetInvalidNonExistentAcademicLevel() {
        Level level = newValidForCreateLevel()
        save level
        assertNotNull level.id
        AcademicLevel academicLevel = academicLevelCompositeService.fetchByLevelId(level.id)
        assertNotNull academicLevel.toString()
        assertNotNull academicLevel.guid
        assertEquals academicLevel.id, level.id

        level.delete(flush: true)
        assertNull level.get(academicLevel.id)

        try {
            academicLevelCompositeService.get(academicLevel.guid)
        } catch (ApplicationException ae) {
            assertApplicationException ae, "NotFoundException"
        }
    }

    @Test
    void testGet() {
        def paginationParams = [max: '1', offset: '0']
        List academicLevels = academicLevelCompositeService.list(paginationParams)
        assertNotNull academicLevels
        assertFalse academicLevels.isEmpty()

        assertNotNull academicLevels[0].guid
        def academicLevel = academicLevelCompositeService.get(academicLevels[0].guid)
        assertNotNull academicLevel
        assertEquals academicLevels[0].guid, academicLevel.guid
        assertEquals academicLevels[0].code, academicLevel.code
        assertEquals academicLevels[0].metadata, academicLevel.metadata
        assertEquals academicLevels[0], academicLevel
    }

    @Test
    void testFetchByAcademicLevelId() {
        AcademicLevel academicLevel = academicLevelCompositeService.fetchByLevelId(i_success_level.id)
        assertNotNull academicLevel
        assertEquals i_success_level.id, academicLevel.id
        assertEquals i_success_level.code, academicLevel.code
        assertEquals i_success_level.description, academicLevel.description
        assertEquals i_success_level.dataOrigin, academicLevel.metadata.dataOrigin
    }

    @Test
    void testFetchByAcademicLevelIdInvalid() {
        assertNull academicLevelCompositeService.fetchByLevelId(null)
    }

    @Test
    void testFetchByAcademicLevel() {
        AcademicLevel academicLevel = academicLevelCompositeService.fetchByLevelCode(i_success_level.code)
        assertNotNull academicLevel
        assertEquals i_success_level.id, academicLevel.id
        assertEquals i_success_level.code, academicLevel.code
        assertEquals i_success_level.description, academicLevel.description
        assertEquals i_success_level.dataOrigin, academicLevel.metadata.dataOrigin
    }

    @Test
    void testFetchByAcademicLevelInvalid() {
        assertNull academicLevelCompositeService.fetchByLevelCode(null)

        def invalidLevelCode = 'A1'
        if (!Level.findByCode(invalidLevelCode)){
            assertNull academicLevelCompositeService.fetchByLevelCode(invalidLevelCode)
        }
    }


    private def newValidForCreateLevel() {
        def level = new Level(
                code: i_success_code,
                description: i_success_description,
                acadInd: i_success_academicIndicator,
                ceuInd: i_success_continuingEducationIndicator,
                systemReqInd: i_success_systemRequiredIndicator,
                vrMsgNo: i_success_voiceResponseMessageNumber,
                ediEquiv: i_success_electronicDataInterchangeEquivalent
        )
        return level
    }

    /**
     * Test to check the AcademicLevelCompositeService list method with valid sort and order field and supported version
     * If No "Accept" header is provided, by default it takes the latest supported version
     */
    @Test
    void testListWithValidSortAndOrderFieldWithSupportedVersion() {
        def params = [order: 'ASC', sort: 'code']
        def academicLevelList = academicLevelCompositeService.list(params)
        assertNotNull academicLevelList
        assertFalse academicLevelList.isEmpty()
        assertTrue academicLevelList.code.contains(i_success_level.code)
    }


    /**
     * Test to check the AcademicLevelCompositeService list method with invalid order field
     */
    @Test
    void testListWithInvalidSortOrder() {
        shouldFail(RestfulApiValidationException) {
            def map = [order: 'test']
            academicLevelCompositeService.list(map)
        }
    }

    /**
     * Test to check the AcademicLevelCompositeService list method with invalid sort field
     */
    @Test
    void testListWithInvalidSortField() {
        shouldFail(RestfulApiValidationException) {
            def map = [sort: 'test']
            academicLevelCompositeService.list(map)
        }
    }


    /**
     * Test to check the sort by code on AcademicLevelCompositeService
     * */
    @Test
    public void testSortByCode(){
        params.order='ASC'
        params.sort='code'
        List list = academicLevelCompositeService.list(params)
        assertNotNull list
        def tempParam=null
        list.each{
            academicLevel->
                String code=academicLevel.code
                if(!tempParam){
                    tempParam=code
                }
                assertTrue tempParam.compareTo(code)<0 || tempParam.compareTo(code)==0
                tempParam=code
        }

        params.clear()
        params.order='DESC'
        params.sort='code'
        list = academicLevelCompositeService.list(params)
        assertNotNull list
        tempParam=null
        list.each{
            academicLevel->
                String code=academicLevel.code
                if(!tempParam){
                    tempParam=code
                }
                assertTrue tempParam.compareTo(code)>0 || tempParam.compareTo(code)==0
                tempParam=code
        }
    }
}
