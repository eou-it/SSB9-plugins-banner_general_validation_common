/*******************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.general.system.ldm

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.general.overall.ldm.GlobalUniqueIdentifier
import net.hedtech.banner.general.system.AcademicDisciplineView
import net.hedtech.banner.general.system.MajorMinorConcentration
import net.hedtech.banner.general.system.ldm.v4.AcademicDiscipline
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
    def i_success_input_content
    def i_sucess_academicDiscipline_major
    private String acedemic_discipline_hedm = 'academic-disciplines'


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
        i_success_input_content = [code: 'MMC', description: 'Test Description', type:'major']
        i_sucess_academicDiscipline_major = MajorMinorConcentration.findByValidMajorIndicatorIsNotNullAndValidMinorIndicatorIsNull()
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
        assertNotNull academicDisciplines[0].id
        def academicDiscipline = academicDisciplineCompositeService.get(academicDisciplines[0].id)
        assertNotNull academicDiscipline
        assertEquals academicDisciplines[0].id, academicDiscipline.guid
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
        assertNotNull academicDisciplines[0].id
        try {
            academicDisciplineCompositeService.get(academicDisciplines[0].id + '2')
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

    /**
     * Test to check the create method of AcademicDisciplineCompositeService of Type Major with valid request payload
     */
    @Test
    void testCreateAcademicDisciplineWithTypeMajor() {
        AcademicDiscipline academicDiscipline= createAndTestAcademicDiscipline(i_success_input_content)
        MajorMinorConcentration majorMinorConcentration=  MajorMinorConcentration.findByCode(academicDiscipline.code)
        assertNotNull majorMinorConcentration
        assertTrue majorMinorConcentration.validMajorIndicator
    }

    /**
     * Test to check the create method of AcademicDisciplineCompositeService of Type Minor with valid request payload
     */
    @Test
    void testCreateAcademicDisciplineWithTypeMinor() {
        i_success_input_content.type = 'minor'
        AcademicDiscipline academicDiscipline= createAndTestAcademicDiscipline(i_success_input_content)
        MajorMinorConcentration majorMinorConcentration=  MajorMinorConcentration.findByCode(academicDiscipline.code)
        assertNotNull majorMinorConcentration
        assertTrue majorMinorConcentration.validMinorIndicator
    }

    /**
     * Test to check the create method of AcademicDisciplineCompositeService of Type Concentration with valid request payload
     */
    @Test
    void testCreateAcademicDisciplineWithTypeConcentration() {
        i_success_input_content.type = 'concentration'
        AcademicDiscipline academicDiscipline= createAndTestAcademicDiscipline(i_success_input_content)
        MajorMinorConcentration majorMinorConcentration=  MajorMinorConcentration.findByCode(academicDiscipline.code)
        assertNotNull majorMinorConcentration
        assertTrue majorMinorConcentration.validConcentratnIndicator
    }

    /**
     * Test to check the AcademicDisciplineCompositeService create method without mandatory code in the request payload
     */
    @Test
    void testCreateAcademicDisciplineWithoutMandatoryCode() {
        i_success_input_content.remove('code')
        try {
            academicDisciplineCompositeService.create(i_success_input_content)
        } catch (Exception ae) {
            assertApplicationException ae, "code.required.message"
        }
    }

    /**
     * Test to check the AcademicDisciplineCompositeService create method with existing guid in the request payload
     */
    @Test
    void testCreateAcademicDisciplineWIthExistingGuid() {
        String existingGuid = GlobalUniqueIdentifier.findByLdmNameAndDomainKey(acedemic_discipline_hedm, i_sucess_academicDiscipline_major.code+"^"+i_success_input_content.type)?.guid
        i_success_input_content.id = existingGuid
        try {
            academicDisciplineCompositeService.create(i_success_input_content)
        } catch (Exception ae) {
            assertApplicationException ae, "exists.message"
        }
    }

    /**
     * Test to check the AcademicDisciplineCompositeService create method with new guid in the request payload
     */
    @Test
    void testCreateAcademicDisciplineWIthNewGuid() {
        i_success_input_content.id = 'test-guid'
        AcademicDiscipline academicDiscipline= createAndTestAcademicDiscipline(i_success_input_content)
    }

    /**
     * Test to check the AcademicDisciplineCompositeService create method with new guid but with a existing code and type in the request payload
     */
    @Test
    void testCreateAcademicDisciplineWIthNewGuidExistingCodeAndType() {
        i_success_input_content.id = 'test-guid'
        i_success_input_content.code = i_sucess_academicDiscipline_major.code
        try {
            academicDisciplineCompositeService.create(i_success_input_content)
        } catch (Exception ae) {
            assertApplicationException ae, "exists.message"
        }
    }

    /**
     * Test to check the AcademicDisciplineCompositeService create method with new guid but with a existing code and new type in the request payload
     */
    @Test
    void testCreateAcademicDisciplineWIthNewGuidExistingCodeWithNewType() {
        i_success_input_content.id = 'test-guid'
        i_success_input_content.code = i_sucess_academicDiscipline_major.code
        i_success_input_content.type = 'minor'
        AcademicDiscipline academicDiscipline= createAndTestAcademicDiscipline(i_success_input_content)
    }

    /**
     * Test to check the AcademicDisciplineCompositeService update method with a existing Guid,code, but new Title in the request payload
     * */
    @Test
    void testUpdateAcademicDisciplineExistingGuidAndCodeWithNewTitle() {
        AcademicDiscipline academicDiscipline= createAndTestAcademicDiscipline(i_success_input_content)
        MajorMinorConcentration majorMinorConcentration=  MajorMinorConcentration.findByCode(academicDiscipline.code)
        assertNotNull majorMinorConcentration
        assertTrue majorMinorConcentration.validMajorIndicator
        Map update_content = updateAcademicDisciplineMap(academicDiscipline.guid)
        def o_success_AcademicDiscipline_update = academicDisciplineCompositeService.update(update_content)
        assertNotNull o_success_AcademicDiscipline_update
        assertEquals o_success_AcademicDiscipline_update.guid, update_content.id
        assertEquals o_success_AcademicDiscipline_update.code, update_content.code
        assertEquals o_success_AcademicDiscipline_update.description, update_content.description
    }

    /**
     * Test to check the AcademicDisciplineCompositeService update method with a existing Guid,code, but new Type in the request payload
     * */
    @Test
    void testUpdateAcademicDisciplineExistingGuidAndCodeWithNewType() {
        AcademicDiscipline academicDiscipline= createAndTestAcademicDiscipline(i_success_input_content)
        MajorMinorConcentration majorMinorConcentration=  MajorMinorConcentration.findByCode(academicDiscipline.code)
        assertNotNull majorMinorConcentration
        assertTrue majorMinorConcentration.validMajorIndicator
        Map update_content = updateAcademicDisciplineMap(academicDiscipline.guid)
        update_content.type = 'minor'
        def o_success_AcademicDiscipline_update = academicDisciplineCompositeService.update(update_content)
        assertNotNull o_success_AcademicDiscipline_update
        assertEquals o_success_AcademicDiscipline_update.guid, update_content.id
        assertEquals o_success_AcademicDiscipline_update.code, update_content.code
        assertEquals o_success_AcademicDiscipline_update.type, academicDiscipline.type
        assert o_success_AcademicDiscipline_update.type  !=  'minor'
    }

    /**
     * Test to check the AcademicDisciplineCompositeService update method with Invalid Guid
     * */
    @Test
    void testUpdateAcademicDisciplineWithInvalidGuid() {
        AcademicDiscipline academicDiscipline= createAndTestAcademicDiscipline(i_success_input_content)
        Map update_content = updateAcademicDisciplineMap(null)
        shouldFail(ApplicationException) {
            academicDisciplineCompositeService.update(update_content)
        }
    }

    /**
     * Test to check the AcademicDiscipline update method with new Guid and Code -create method invocation
     * */
    @Test
    void testUpdateAcademicDisciplineWithCreateForNewCodeAndGuid() {
        i_success_input_content.put("id","test-guid")
        AcademicDiscipline academicDiscipline= createAndTestAcademicDiscipline(i_success_input_content)
    }

    /**
     * Common Method to test academicDiscipline create method  based on the request payload and invoked form all the create and update test methods
     * */
    AcademicDiscipline createAndTestAcademicDiscipline(Map i_success_input_content){
        AcademicDiscipline academicDiscipline = academicDisciplineCompositeService.create(i_success_input_content)
        assertNotNull academicDiscipline
        assertNotNull academicDiscipline.guid
        assertEquals i_success_input_content.code, academicDiscipline.code
        assertEquals i_success_input_content.description, academicDiscipline.description
        assertEquals i_success_input_content.type, academicDiscipline.type
        return academicDiscipline
    }

    private Map updateAcademicDisciplineMap(id) {
        Map params = [id: id,code: 'KKR', description: 'Updated Description', type:'major']
        return params
    }

}
