/*******************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.general.system.ldm
import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.general.overall.ldm.GlobalUniqueIdentifier
import net.hedtech.banner.general.system.AcademicHonorView
import net.hedtech.banner.general.system.ldm.v4.AcademicHonor
import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.junit.After
import org.junit.Before
import org.junit.Test
/**
 * <p>Integration Test cases for AcademicHonorCompositeService</p>
 * */
class AcademicHonorCompositeServiceIntegrationTests extends BaseIntegrationTestCase {

    def academicHonorCompositeService

    @Before
    public void setUp() {
        formContext = ['GUAGMNU']
        super.setUp()
    }

    @After
    public void tearDown() {
        super.tearDown()
    }

    /**
     * <p> Test to lists the academicHonors Records from academicHonorsCompositeService</p>
     * */
    @Test
    public void testListAcademicHonors(){
       List academicHonorList = academicHonorCompositeService.list([max:'5'])
       assertNotNull academicHonorList
       assertFalse academicHonorList.isEmpty()
       assertEquals 5, academicHonorList.size()
    }

    /**
     * <p> Test to list the award type academic honors from academicHonorsCompositeService</p>
     * */
    @Test
    public void testAwardHonors(){
        params.type='award'
        List list = academicHonorCompositeService.list(params)
        assertTrue list.size()>0
        AcademicHonor academicHonor = list.get(0)
        assertEquals academicHonor.type,'award'
    }

    /**
     * <p> Test to list the distinction type academic honors from academicHonorsCompositeService</p>
     * */
    @Test
    public void testDistinctionHonors(){
        params.type='distinction'
        List list = academicHonorCompositeService.list(params)
        assertTrue list.size()>0
        AcademicHonor academicHonor = list.get(0)
        assertEquals academicHonor.type,'distinction'
    }

    /**
     * <p>Test to check the count on academicHonorCompositeService</p>
     * */
        @Test
    public void testCount(){
        assertNotNull academicHonorCompositeService
        assertEquals AcademicHonorView.count(),academicHonorCompositeService.count(params)
    }

    /**
     * <p> Test to check the sort order and sorting field on academicHonorsCompositeService</p>
     * */
    @Test
    public void testSortOrder(){
        params.order='DESC'
        params.sort='title'
        List list = academicHonorCompositeService.list(params)
        assertTrue list.size()>0
    }

    /**
     * <p> Tests to check the offset on academicHonorsCompositeService</p>
     * */
    @Test
    public void testOffsetCriteria(){
        List academicHonorList = academicHonorCompositeService.list(params)
        assertTrue academicHonorList.size()>0

        params.offset='2'
        List academicHonorListNew = academicHonorCompositeService.list(params)
        assertTrue academicHonorListNew.size()>0

        assertTrue academicHonorListNew.size()<academicHonorList.size()
    }

    /**
     * <p> Test to get a single record from academicHonorsCompositeService</p>
     * */
    @Test
    public void testShow(){
        List list = academicHonorCompositeService.list(params)
        assertNotNull list
        assertTrue list.size()>0
        AcademicHonor academicHonor = list.get(0)
        assertNotNull academicHonor

        AcademicHonor academicHonor1 = academicHonorCompositeService.get(academicHonor.guid)
        assertNotNull academicHonor1
        assertEquals academicHonor.descriptions,academicHonor1.descriptions
        assertEquals academicHonor.guid,academicHonor1.guid
        assertEquals academicHonor.type,academicHonor1.type
    }

    /**
     * <p> Test to pass an invalid guid to academicHonorsCompositeService which will return an exception saying record not found</p>
     * */
    @Test
    public void testInvalidShow(){
        List list = academicHonorCompositeService.list(params)
        assertNotNull list
        assertTrue list.size()>0
        AcademicHonor academicHonor = list.get(0)
        assertNotNull academicHonor

        try {
            academicHonorCompositeService.get(academicHonor.guid.substring(0,academicHonor.guid.length()-2))
        } catch (ApplicationException ae) {
            assertApplicationException ae, "NotFoundException"
        }
    }

    /**
     * <p> Test to pass another service guid to academicHonorsCompositeService which will throw exception saying Invalid Guid for academicHonorsService</p>
     * */
    @Test
    public void testDifferentLDMGuid(){
        String siteGuid = GlobalUniqueIdentifier.findByLdmNameAndDomainKey('campuses', 'M')?.guid
        assertNotNull siteGuid
        try {
            academicHonorCompositeService.get(siteGuid)
        } catch (ApplicationException ae) {
            assertApplicationException ae, "invalid.guid"
        }
    }

    /**
     * <p> Test show for academicHonorCompositeService with null guid </p>
     * */
    @Test
    void testGetNullGuid() {
        try {
            academicHonorCompositeService.get( null )
        } catch (ApplicationException ae) {
            assertApplicationException ae, "NotFoundException"
        }
    }

}
