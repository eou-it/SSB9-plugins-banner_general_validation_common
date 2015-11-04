/*******************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.general.system.ldm

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.general.system.AcademicDisciplineView
import net.hedtech.banner.general.system.MajorMinorConcentration
import net.hedtech.banner.general.system.ldm.v4.AcademicDisciplineType
import net.hedtech.banner.restfulapi.RestfulApiValidationException
import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.junit.After
import org.junit.Before
import org.junit.Test

/**
 * This is the integration test cases for academic discipline composite service
 */
class AcademicDisciplineCompositeServiceIntegrationTests extends BaseIntegrationTestCase {

    def academicDisciplineCompositeService
    def i_fail_academicDiscipline


    @Before
    public void setUp() {
        formContext = ['GUAGMNU']
        super.setUp()
        initializeDataReferences()
    }

    @After
    public void tearDown() {
        super.tearDown()
    }

    private void initializeDataReferences() {
        i_fail_academicDiscipline = MajorMinorConcentration.findByValidMinorIndicatorIsNullAndValidMajorIndicatorIsNullAndValidConcentratnIndicatorIsNull()
    }
    
    /**
     * This test case is checking for AcademicDisciplineCompositeService get method
     */
    @Test
    void testGet() {
        def paginationParams = [max: '1', offset: '0']
        List academicDisciplines=AcademicDisciplineView.list(paginationParams)
        assertNotNull academicDisciplines
        assertFalse academicDisciplines.isEmpty()
        assertNotNull academicDisciplines[0].guid
        def academicDiscipline = academicDisciplineCompositeService.get(academicDisciplines[0].guid)
        assertNotNull academicDiscipline
        assertEquals academicDisciplines[0].guid, academicDiscipline.guid
        assertEquals academicDisciplines[0].code, academicDiscipline.code
        assertFalse academicDiscipline.code.contains(i_fail_academicDiscipline?.code)
    }
    
    /**
     * This test case is checking for AcademicDisciplineCompositeService get method passing with invalid guid as an argument
     */
    @Test
    void testGetWithInvalidGuid() {
        def paginationParams = [max: '1', offset: '0']
        List academicDisciplines=AcademicDisciplineView.list(paginationParams)
        assertNotNull academicDisciplines
        assertFalse academicDisciplines.isEmpty()
        assertNotNull academicDisciplines[0].guid
        try {
            academicDisciplineCompositeService.get(academicDisciplines[0].guid + '2')
        } catch (ApplicationException ae) {
            assertApplicationException ae, "NotFoundException"
        }
    }

    /**
     * This test case is checking for AcademicDisciplineCompositeService get method passing with null guid as an argument
     */
    @Test
    void testGetWithNullGuid() {
        try {
            academicDisciplineCompositeService.get(null)
        } catch (ApplicationException ae) {
            assertApplicationException ae, "NotFoundException"
        }
    }

    /**
     * This test case is checking for AcademicDisciplineCompositeService get method passing with empty guid as an argument
     */
    @Test
    void testGetWithEmptyGuid() {
        try {
          academicDisciplineCompositeService.get("")
        } catch (ApplicationException ae) {
            assertApplicationException ae, "NotFoundException"
        }
    }


    /**
     * This test case is checking for AcademicDisciplineCompositeService list method passing with invalid sort filed
     */
    @Test
    void testListWithInvalidSortFiled(){
        shouldFail(RestfulApiValidationException) {
            def map = [sort:'code']
            academicDisciplineCompositeService.list(map)
        }
    }

    /**
     * This test case is checking for AcademicDisciplineCompositeService list method with passing invalid sort order
     */
    @Test
    void testListWithInvalidSortOrder(){
        shouldFail(RestfulApiValidationException) {
            def map = [order:'test']
            academicDisciplineCompositeService.list(map)
        }
    }

    /**
     * This test case is checking for AcademicDisciplineCompositeService list method with out pagination 
     */
    @Test
    void testListWithoutPaginationParams() {
        List academicDisciplines = academicDisciplineCompositeService.list([:])
        assertNotNull academicDisciplines
        assertFalse academicDisciplines.isEmpty()
        assertEquals academicDisciplines.size() , AcademicDisciplineView.list([max:'500']).size()
        assertFalse academicDisciplines.code.contains(i_fail_academicDiscipline.code)
    }
    
    /**
     * This test case is checking for AcademicDisciplineCompositeService list method with pagination (max 4 and offset 0) 
     */
    @Test
    void testListWithPagination() {
        def paginationParams = [max: '4', offset: '0']
        List academicDisciplines = academicDisciplineCompositeService.list(paginationParams)
        assertNotNull academicDisciplines
        assertFalse academicDisciplines.isEmpty()
        assertEquals academicDisciplines.size() , 4
        assertFalse academicDisciplines.code.contains(i_fail_academicDiscipline.code)
    }

    /**
     * This test case is checking for AcademicDisciplineCompositeService list method with type of minor
     */
    @Test
    void testListWithoutPaginationParamsByMinorType() {
        List academicDisciplines = academicDisciplineCompositeService.list([type: AcademicDisciplineType.MINOR.value])
        assertNotNull academicDisciplines
        assertFalse academicDisciplines.isEmpty()
        assertEquals academicDisciplines.size() , AcademicDisciplineView.countByType(AcademicDisciplineType.MINOR.value)
        assertFalse academicDisciplines.code.contains(i_fail_academicDiscipline.code)
    }

    /**
     * This test case is checking for AcademicDisciplineCompositeService list method with type of major
     */
    @Test
    void testListWithoutPaginationParamsByMajorType() {
        List academicDisciplines = academicDisciplineCompositeService.list([type: AcademicDisciplineType.MAJOR.value])
        assertNotNull academicDisciplines
        assertFalse academicDisciplines.isEmpty()
        assertEquals academicDisciplines.size() ,AcademicDisciplineView.countByType(AcademicDisciplineType.MAJOR.value)
        assertFalse academicDisciplines.code.contains(i_fail_academicDiscipline.code)
    }

    /**
     * This test case is checking for AcademicDisciplineCompositeService list method with type of concentration
     */
    @Test
    void testListWithoutPaginationParamsByConcentrationType() {
        List academicDisciplines = academicDisciplineCompositeService.list([type: AcademicDisciplineType.CONCENTRATION.value])
        assertNotNull academicDisciplines
        assertFalse academicDisciplines.isEmpty()
        assertEquals academicDisciplines.size() ,AcademicDisciplineView.countByType(AcademicDisciplineType.CONCENTRATION.value)
        assertFalse academicDisciplines.code.contains(i_fail_academicDiscipline.code)
    }

    /**
     * This test case is checking for AcademicDisciplineCompositeService list method with Invalid type
     */
    @Test
    void testListWithInvalidType() {
        List academicDisciplines = academicDisciplineCompositeService.list([type: "INVALID_TYPE"])
        assertNotNull academicDisciplines
        assertTrue academicDisciplines.isEmpty()
    }

    /**
     *This test case is checking for AcademicDisciplineCompositeService list method with pagination by type of minor   
     */
    @Test
    void testListWithPaginationByMinorType() {
        def params = [max: '2', offset: '0', type: AcademicDisciplineType.MINOR.value]
        List academicDisciplines = academicDisciplineCompositeService.list(params)
        assertNotNull academicDisciplines
        assertFalse academicDisciplines.isEmpty()
        assertEquals academicDisciplines.size() , 2
        assertFalse academicDisciplines.code.contains(i_fail_academicDiscipline.code)
    }
    
    /**
     * This test case is checking for AcademicDisciplineCompositeService list method with pagination by type of concentration 
     */
    @Test
    void testListWithPaginationByConcentrationType() {
        def params = [max: '3', offset: '0', type: AcademicDisciplineType.CONCENTRATION.value]
        List academicDisciplines = academicDisciplineCompositeService.list(params)
        assertNotNull academicDisciplines
        assertFalse academicDisciplines.isEmpty()
        assertEquals academicDisciplines.size() , 3
        assertFalse academicDisciplines.code.contains(i_fail_academicDiscipline.code)
    }

    /**
     * This test case is checking for AcademicDisciplineCompositeService list method with pagination by type of major  
     */
    @Test
    void testListWithPaginationByMajorType() {
        def params = [max: '4', offset: '0', type: AcademicDisciplineType.MAJOR.value]
        List academicDisciplines = academicDisciplineCompositeService.list(params)
        assertNotNull academicDisciplines
        assertFalse academicDisciplines.isEmpty()
        assertEquals academicDisciplines.size() , 4
        assertFalse academicDisciplines.code.contains(i_fail_academicDiscipline.code)
    }
    
    /**
     * This test case is checking for AcademicDisciplineCompositeService count method
     */
    @Test
    void testCount() {
        assertEquals AcademicDisciplineView.count(), academicDisciplineCompositeService.count([:])
    }

    /**
     * This test case is checking for AcademicDisciplineCompositeService count method with type value as an argument
     */
    @Test
    void testCountByType() {
        assertEquals AcademicDisciplineView.countByType(AcademicDisciplineType.MINOR.value), academicDisciplineCompositeService.count([type: AcademicDisciplineType.MINOR.value])
        assertEquals AcademicDisciplineView.countByType(AcademicDisciplineType.MAJOR.value), academicDisciplineCompositeService.count([type: AcademicDisciplineType.MAJOR.value])
        assertEquals AcademicDisciplineView.countByType(AcademicDisciplineType.CONCENTRATION.value), academicDisciplineCompositeService.count([type: AcademicDisciplineType.CONCENTRATION.value])
        assertEquals 0, academicDisciplineCompositeService.count([type: AcademicDisciplineType.CONCENTRATION.value+'test'])
    }


}
