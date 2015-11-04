/*******************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.general.system

import net.hedtech.banner.general.overall.ldm.GlobalUniqueIdentifier
import net.hedtech.banner.general.system.ldm.AcademicDisciplineCompositeService
import net.hedtech.banner.general.system.ldm.v4.AcademicDisciplineType
import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.hibernate.sql.ordering.antlr.GeneratedOrderByLexer
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.springframework.dao.InvalidDataAccessResourceUsageException

/**
 * Integration Test cases for AcademicDisciplineView which is Read Only view
 */
class AcademicDisciplineViewIntegrationTests extends BaseIntegrationTestCase {
    def i_fail_academicDiscipline
    def i_success_academicDiscipline_minor
    def i_success_academicDiscipline_major
    def i_success_academicDiscipline_concentration
    def i_success_academicDiscipline_code

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
    
    private void initializeDataReferences() {
        i_success_academicDiscipline_code = '101'
        i_fail_academicDiscipline = MajorMinorConcentration.findByValidMinorIndicatorIsNullAndValidMajorIndicatorIsNullAndValidConcentratnIndicatorIsNull()
        i_success_academicDiscipline_minor = MajorMinorConcentration.findAllByValidMinorIndicator(Boolean.TRUE)
        i_success_academicDiscipline_major = MajorMinorConcentration.findAllByValidMajorIndicator(Boolean.TRUE)
        i_success_academicDiscipline_concentration = MajorMinorConcentration.findAllByValidConcentratnIndicator(Boolean.TRUE)
    }

    /**
     * This test case is for checking Academic Discipline View return count is matching with sum of MajorMinorConcentration type count of Major , minor and concentration
     */
    @Test
    void testCount() {
        def count =AcademicDisciplineView.count()
        assertNotNull count
        assertEquals count ,i_success_academicDiscipline_minor.size()+i_success_academicDiscipline_major.size()+i_success_academicDiscipline_concentration.size()
    }

    /**
     * This test case is checking for Academic Discipline View type return count is match with respective MajorMinorConcentration type count of Major , minor or concentration
     */
    @Test
    void testCountByType() {
        def majorCount=AcademicDisciplineView.countByType(AcademicDisciplineType.MAJOR.value)
        def minorCount=AcademicDisciplineView.countByType(AcademicDisciplineType.MINOR.value)
        def concentrationCount= AcademicDisciplineView.countByType(AcademicDisciplineType.CONCENTRATION.value)
        assertNotNull majorCount
        assertNotNull minorCount
        assertNotNull concentrationCount
        assertEquals minorCount,i_success_academicDiscipline_minor.size()
        assertEquals majorCount,i_success_academicDiscipline_major.size()
        assertEquals concentrationCount,i_success_academicDiscipline_concentration.size()
    }

    /**
     * This test case is checking for Academic Discipline View return data have only MajorMinorConcentration type of Major , minor and concentration data.
     * And checking return data does contains MajorMinorConcentration records which do not have major, minor or concentration.
     */
    @Test
    void testList(){
        def academicDisciplineList=AcademicDisciplineView.list()
        assertNotNull academicDisciplineList
        assertFalse academicDisciplineList.code.contains(i_fail_academicDiscipline.code)
        assertTrue academicDisciplineList.code.contains(i_success_academicDiscipline_minor[0].code)
        assertTrue academicDisciplineList.code.contains(i_success_academicDiscipline_major[0].code)
        assertTrue academicDisciplineList.code.contains(i_success_academicDiscipline_concentration[0].code)
        
    }
    /**
     * This test case is checking  for Academic Discipline View return type of Major data does contains MajorMinorConcentration records which do not have major, minor or concentration.
     */
    @Test
    void testFetchByMajorType() {
       def majorList= AcademicDisciplineView.findAllByType(AcademicDisciplineType.MAJOR.value)
        assertNotNull majorList
        assertFalse majorList.code.contains(i_fail_academicDiscipline.code)
    }

    /**
     * This test case is checking for Academic Discipline View return type of Minor data does contains MajorMinorConcentration records which do not have major, minor or concentration.
     */
    @Test
    void testFetchByMinorType() {
        def minorList= AcademicDisciplineView.findAllByType(AcademicDisciplineType.MINOR.value)
        assertNotNull minorList
        assertFalse minorList.code.contains(i_fail_academicDiscipline.code)
    }

    /**
     * This test case is checking for Academic Discipline View return type of concentration data does contains MajorMinorConcentration records which do not have major, minor or concentration.
     */
    @Test
    void testFetchByConcentrationType() {
        def concentrationList=AcademicDisciplineView.findAllByType(AcademicDisciplineType.CONCENTRATION.value)
        assertNotNull concentrationList
        assertFalse concentrationList.code.contains(i_fail_academicDiscipline.code)
    }

    /**
     * This test case is checking for Academic Discipline View return data does have guid value is empty or null
     * And checking return data does contains MajorMinorConcentration records which do not have major, minor or concentration.
     */
    @Test
    void testFetchByguid() {
        assertEquals AcademicDisciplineView.findAllByGuid("").size , 0
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
     * This test case is checking for Academic Discipline View return data does have different guid value with type of major, minor or concentration.
     */
    @Test
    void testAcademicDisciplineGuid(){
       def majorGuid = GlobalUniqueIdentifier.findByLdmNameAndDomainKey('academic-disciplines', i_success_academicDiscipline_code+"^"+AcademicDisciplineType.MAJOR.value)?.guid
       assertNotNull majorGuid
       AcademicDisciplineView disciplineView = AcademicDisciplineView.get(majorGuid)
       assertNotNull disciplineView
       assertEquals disciplineView.code , i_success_academicDiscipline_code
       assertEquals disciplineView.type , AcademicDisciplineType.MAJOR.value

        def minorGuid = GlobalUniqueIdentifier.findByLdmNameAndDomainKey('academic-disciplines', i_success_academicDiscipline_code+"^"+AcademicDisciplineType.MINOR.value)?.guid
        assertNotNull majorGuid
        disciplineView = AcademicDisciplineView.get(minorGuid)
        assertNotNull disciplineView
        assertEquals disciplineView.code , i_success_academicDiscipline_code
        assertEquals disciplineView.type , AcademicDisciplineType.MINOR.value

        def concentrationGuid = GlobalUniqueIdentifier.findByLdmNameAndDomainKey('academic-disciplines', i_success_academicDiscipline_code+"^"+AcademicDisciplineType.CONCENTRATION.value)?.guid
        assertNotNull majorGuid
        disciplineView = AcademicDisciplineView.get(concentrationGuid)
        assertNotNull disciplineView
        assertEquals disciplineView.code , i_success_academicDiscipline_code
        assertEquals disciplineView.type , AcademicDisciplineType.CONCENTRATION.value
    }

    /**
     * This test case is checking for number occurrences Academic Discipline type of major, minor or concentration guid should match with gorguid count.
     */
    @Test
    void testAcademicDisciplineGuidCount(){
       def expectedCount = GlobalUniqueIdentifier.countByLdmNameAndDomainKeyLike('academic-disciplines',i_success_academicDiscipline_code+"%")
       assertNotNull expectedCount
       def actualCount = AcademicDisciplineView.countByCode(i_success_academicDiscipline_code)
       assertNotNull actualCount
       assertEquals expectedCount , actualCount
    }


    private def newAcademicDiscipline(){
        new AcademicDisciplineView(
             code:'test',
             description:'test data',
             dataOrigin:'test',
             guid:'test_guid',
             type :'test'
        )
    }

}
