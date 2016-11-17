/*******************************************************************************
 Copyright 2016 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.general.system

import net.hedtech.banner.general.overall.ldm.GlobalUniqueIdentifier
import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.springframework.dao.InvalidDataAccessResourceUsageException

/**
 * <p>Integration Test case for AcademicHonorView which is a Read only view</p>
 */
class AcademicHonorViewIntegrationTests extends BaseIntegrationTestCase {

    public static final String INSTITUTIONAL_HONORS = 'institutional-honors'

    @Before
    public void setUp() {
        formContext = ['GUAGMNU'] // Since we are not testing a controller, we need to explicitly set this
        super.setUp()
    }

    @After
    public void tearDown() {
        super.tearDown()
    }

    /**
     * <p> Test to get the count from AcademicHonorView and cumulative count of DepartmentalHonor and InstitutionalHonor</p>
     * */
    @Test
    public void testCount() {
        def departmentalHonorCount = DepartmentalHonor.count()
        assertNotNull departmentalHonorCount
        def institutionalHonorCount = InstitutionalHonor.count()
        assertNotNull institutionalHonorCount
        assertTrue AcademicHonorView.count() == (institutionalHonorCount + departmentalHonorCount)
    }

    /**
     * <p> Test to get the departmental-honors records from AcademicHonorView </p>
     * */
    @Test
    public void testFetchDeptHonorsCount() {
        def type = 'departmental-honors'
        def params = setParams()
        List academicHonorViewRecords = AcademicHonorView.fetchByType(type, params)
        assertNotNull academicHonorViewRecords
        def departmentalHonorsCount = DepartmentalHonor.count()
        assertNotNull departmentalHonorsCount
        assertEquals academicHonorViewRecords.size(), departmentalHonorsCount
        AcademicHonorView academicHonorView = academicHonorViewRecords.get(0)
        assertEquals academicHonorView.type, type
    }


    public def setParams() {
        def params = [max: '10', offset: '0']
        return params
    }
    /**
     * <p> Test to get the institutional-honors records from AcademicHonorView </p>
     * */
    @Test
    public void testFetchInstitutionalHonorsCount() {
        def params = setParams()
        List list = AcademicHonorView.fetchByType(INSTITUTIONAL_HONORS, params)
        assertNotNull list
        def institutionalHonorCount = InstitutionalHonor.count();
        assertNotNull institutionalHonorCount
        assertEquals institutionalHonorCount, list.size()
        AcademicHonorView academicHonorView = list.get(0)
        assertEquals academicHonorView.type, INSTITUTIONAL_HONORS
    }

    /**
     * <p> Test to get the record from AcademicHonorView based on Guid</p>
     * */
    @Test
    void testFetchByguid() {
        String instHonorsGuid = GlobalUniqueIdentifier.findByLdmNameAndDomainKey(INSTITUTIONAL_HONORS, 'M')?.guid
        assertNotNull instHonorsGuid
        AcademicHonorView academicHonorView = AcademicHonorView.fetchByGuid(instHonorsGuid);
        assertNotNull academicHonorView
        assertEquals INSTITUTIONAL_HONORS, academicHonorView.type
        assertEquals 'M', academicHonorView.code
    }

    /**
     * <p> Test to get the no record from AcademicHonorView by passing an invalid guid</p>
     * */
    @Test
    void testFetchByInvalidGuid() {
        String instHonorsGuid = GlobalUniqueIdentifier.findByLdmNameAndDomainKey(INSTITUTIONAL_HONORS, 'M')?.guid
        assertNotNull instHonorsGuid
        AcademicHonorView academicHonorView = AcademicHonorView.fetchByGuid(instHonorsGuid.substring(0, 10));
        assertNull academicHonorView
    }

    /**
     * <p> Test to create a record on AcademicHonorView which will return exception as this is a read-only view</p>
     */
    @Test
    void testReadOnlyForCreateAcademicHonor() {
        def academicHonorView = new AcademicHonorView()
        academicHonorView.id = 'aaaaaaaa'
        assertNotNull academicHonorView
        shouldFail(InvalidDataAccessResourceUsageException) {
            academicHonorView.save(flush: true, onError: true)
        }
    }

    /**
     * <p>Test to update on AcademicHonorView which will return exception as this is a read-only view</p>
     */
    @Test
    void testReadOnlyForUpdateAcademicHonor() {
        String instHonorsGuid = GlobalUniqueIdentifier.findByLdmNameAndDomainKey(INSTITUTIONAL_HONORS, 'M')?.guid
        assertNotNull instHonorsGuid
        def academicHonorView = AcademicHonorView.fetchByGuid(instHonorsGuid)
        assertNotNull academicHonorView
        academicHonorView.title = 'Dummy Value'
        shouldFail(InvalidDataAccessResourceUsageException) {
            academicHonorView.save(flush: true, onError: true)
        }
    }

    /**
     * <p>Test to delete on AcademicHonorView which will return exception as this is a read-only view</p>
     */
    @Test
    void testReadOnlyForDeleteAcademicHonor() {
        String instHonorsGuid = GlobalUniqueIdentifier.findByLdmNameAndDomainKey(INSTITUTIONAL_HONORS, 'M')?.guid
        assertNotNull instHonorsGuid
        def academicDiscipline = AcademicHonorView.fetchByGuid(instHonorsGuid)
        assertNotNull academicDiscipline
        shouldFail(InvalidDataAccessResourceUsageException) {
            academicDiscipline.delete(flush: true, onError: true)
        }
    }

    @Test
    void testFetchAllByCodeInListNullList() {
        assertEquals([], AcademicHonorView.fetchAllByCodeInList(null))
    }

    @Test
    void testFetchAllByCodeInListEmptyList() {
        assertEquals([], AcademicHonorView.fetchAllByCodeInList([]))
    }

    @Test
    void testFetchAllByCodeInList() {
        assertEquals(AcademicHonorView.findAll(), AcademicHonorView.fetchAllByCodeInList(AcademicHonorView.findAll().code))
    }

    private def newAcademicHonors() {
        new AcademicHonorView(
                code: 'SS', title: 'Dummy Description', type: INSTITUTIONAL_HONORS, guid: 'aaaaaaaaaaaaa'
        )
    }


}
