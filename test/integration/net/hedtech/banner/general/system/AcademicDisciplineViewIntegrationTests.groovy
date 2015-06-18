/*******************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.general.system

import net.hedtech.banner.general.overall.ldm.GlobalUniqueIdentifier
import net.hedtech.banner.general.system.ldm.v4.AcademicDisciplineType
import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.springframework.dao.InvalidDataAccessResourceUsageException

/**
 * Integration Test cases for AcademicDisciplineView which is Read Only view
 */
class AcademicDisciplineViewIntegrationTests extends BaseIntegrationTestCase {
    def i_fail_academicDiscipline
    def i_sucess_academicDiscipline_minor
    def i_sucess_academicDiscipline_major
    def i_sucess_academicDiscipline_concentration
    def i_fail_academicDiscipline_gorguid

    @Before
    public void setUp() {
        formContext = ['GUAGMNU'] // Since we are not testing a controller, we need to explicitly set this
        super.setUp()
        initializeDataReferences()
    }

    @After
    public void tearDown() {
        super.tearDown()
    }
    
    /**
     * Initialize data for test cases
     */
    private void initializeDataReferences() {
        i_fail_academicDiscipline = MajorMinorConcentration.findByValidMinorIndicatorIsNullAndValidMajorIndicatorIsNullAndValidConcentratnIndicatorIsNull()
        i_sucess_academicDiscipline_minor = MajorMinorConcentration.findAllByValidMinorIndicator(Boolean.TRUE)
        i_sucess_academicDiscipline_major = MajorMinorConcentration.findAllByValidMajorIndicator(Boolean.TRUE)
        i_sucess_academicDiscipline_concentration = MajorMinorConcentration.findAllByValidConcentratnIndicator(Boolean.TRUE)
        i_fail_academicDiscipline_gorguid=  GlobalUniqueIdentifier.findByLdmNameAndDomainKey('academic-disciplines',i_fail_academicDiscipline.code)
    }

    /**
     * This test case is for checking Academic Discipline View return count is matching with sum of MajorMinorConcentration type count of Major , minor and concentration
     */
    @Test
    void testCount() {
        def count =AcademicDisciplineView.count()
        assertNotNull count
        assertEquals count ,i_sucess_academicDiscipline_minor.size()+i_sucess_academicDiscipline_major.size()+i_sucess_academicDiscipline_concentration.size()
    }

    /**
     * This test case is checking for Academic Discipline View type return count is match with respective MajorMinorConcentration type count of Major , minor and concentration
     */
    @Test
    void testCountByType() {
        def majorCount=AcademicDisciplineView.countByType(AcademicDisciplineType.MAJOR.value)
        def minorCount=AcademicDisciplineView.countByType(AcademicDisciplineType.MINOR.value)
        def concentrationCount= AcademicDisciplineView.countByType(AcademicDisciplineType.CONCENTRATION.value)
        assertNotNull majorCount
        assertNotNull minorCount
        assertNotNull concentrationCount
        assertEquals minorCount,i_sucess_academicDiscipline_minor.size()
        assertEquals majorCount,i_sucess_academicDiscipline_major.size()
        assertEquals concentrationCount,i_sucess_academicDiscipline_concentration.size()
    }

    /**
     * This test case is checking for Academic Discipline View return data have MajorMinorConcentration type of Major , minor and concentration data.
     * And Checking for Academic Discipline View return data doesn't have MajorMinorConcentration null type of Major and minor and concentration data.
     */
    @Test
    void testList(){
        def academicDisciplineList=AcademicDisciplineView.list()
        assertNotNull academicDisciplineList
        assertFalse academicDisciplineList.code.contains(i_fail_academicDiscipline.code)
        assertTrue academicDisciplineList.code.contains(i_sucess_academicDiscipline_minor[0].code)
        assertTrue academicDisciplineList.code.contains(i_sucess_academicDiscipline_major[0].code)
        assertTrue academicDisciplineList.code.contains(i_sucess_academicDiscipline_concentration[0].code)
        
    }
    /**
     * This test case is checking  for Academic Discipline View return type of Major data.
     * And Checking for Academic Discipline View return type of Major data doesn't have MajorMinorConcentration null type of Major and minor and concentration data.
     */
    @Test
    void testFetchByMajorType() {
       def majorList= AcademicDisciplineView.findAllByType(AcademicDisciplineType.MAJOR.value)
        assertNotNull majorList
        assertFalse majorList.code.contains(i_fail_academicDiscipline.code)
    }

    /**
     * This test case is checking for Academic Discipline View return type of Minor data.
     * And Checking for Academic Discipline View return type of Minor data doesn't have MajorMinorConcentration null type of Major and minor and concentration data.
     */
    @Test
    void testFetchByMinorType() {
        def minorList= AcademicDisciplineView.findAllByType(AcademicDisciplineType.MINOR.value)
        assertNotNull minorList
        assertFalse minorList.code.contains(i_fail_academicDiscipline.code)
    }

    /**
     * This test case is checking for Academic Discipline View return type of concentration data.
     * And Checking for Academic Discipline View return type of concentration data doesn't have MajorMinorConcentration null type of Major and minor and concentration data.
     */
    @Test
    void testFetchByConcentrationType() {
        def concentrationList=AcademicDisciplineView.findAllByType(AcademicDisciplineType.CONCENTRATION.value)
        assertNotNull concentrationList
        assertFalse concentrationList.code.contains(i_fail_academicDiscipline.code)
    }

    /**
     * This test case is checking for Academic Discipline View return data does have guid value is empty or null
     * And Checking for Academic Discipline View return type of concentration data doesn't have MajorMinorConcentration null type of Major and minor and concentration data.
     */
    @Test
    void testFetchByguid() {
        assertEquals AcademicDisciplineView.findAllByGuid("").size , 0
        assertEquals AcademicDisciplineView.findAllByGuid(i_fail_academicDiscipline_gorguid.guid).size, 0
        assertEquals AcademicDisciplineView.findAllByGuid(null).size , 0
    }

    /**
     * This test case is checking for creating one of record on read only view
     */
    @Test
    void testReadOnlyForCreateAcademicDiscipline(){
            def academicDiscipline = newAcademicDiscipline()
            assertNotNull academicDiscipline
        shouldFail(InvalidDataAccessResourceUsageException) {
            academicDiscipline.save(flush: true, onError: true)
        }
    }
    
    /**
     * This test case is checking for updating one of record on read only view
     */
    @Test
    void testReadOnlyForUpdateAcademicDiscipline(){
            def academicDiscipline = AcademicDisciplineView.findByType(AcademicDisciplineType.MINOR.value)
            assertNotNull academicDiscipline
            academicDiscipline.description='Test for Update'
            shouldFail(InvalidDataAccessResourceUsageException) {
                academicDiscipline.save(flush: true, onError: true)
            }
    }
    
    /**
     * This test case is checking for deletion one of record on read only view
     */
    @Test
    void testReadOnlyForDeleteAcademicDiscipline(){
            def academicDiscipline = AcademicDisciplineView.findByType(AcademicDisciplineType.MINOR.value)
            assertNotNull academicDiscipline
            shouldFail(InvalidDataAccessResourceUsageException) {
                academicDiscipline.delete(flush: true, onError: true)
            }
    }
    
    /**
     * This method is used to create new AcademicDisciplineView
     * @return AcademicDisciplineView
     */
    private def newAcademicDiscipline(){
        new AcademicDisciplineView(
             id:   new AcademicDisciplinePK(
                     surrogateId:9999,
                     validMajorIndicator:true,
                     validMinorIndicator:false,
                     validConcentratnIndicator:false
             ),
             code:'test',
             description:'test data',
             dataOrigin:'test',
             guid:'test_guid',
             type :'test'
        )
    }

}
