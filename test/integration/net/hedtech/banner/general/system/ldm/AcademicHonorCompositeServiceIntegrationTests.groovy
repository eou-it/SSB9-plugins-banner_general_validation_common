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
     * <p> Test to lists the Academic Honors Records from AcademicHonorsCompositeService</p>
     * */
    @Test
    public void testListAcademicHonors(){
       List academicHonorList = academicHonorCompositeService.list(params)
       assertNotNull academicHonorList
       assertFalse academicHonorList.isEmpty()
       def totalCount = AcademicHonorView.count()
       assertNotNull totalCount
       assertEquals totalCount, academicHonorList.size()
    }

    /**
     * <p> Test to list the Academic Honors of type 'award' from AcademicHonorsCompositeService</p>
     * */
    @Test
    public void testAwardHonors(){
        params.type='award'
        List list = academicHonorCompositeService.list(params)
        assertTrue list.size()>0
        AcademicHonor academicHonor = list.get(0)
        assertEquals academicHonor.honorType,'award'
    }

    /**
     * <p>Test to list the Academic Honors of type 'distinction' from AcademicHonorsCompositeService</p>
     * */
    @Test
    public void testDistinctionHonors(){
        params.type='distinction'
        List list = academicHonorCompositeService.list(params)
        assertTrue list.size()>0
        AcademicHonor academicHonor = list.get(0)
        assertEquals academicHonor.honorType,'distinction'
    }

    /**
     * <p>Test to check the count on AcademicHonorCompositeService</p>
     * */
        @Test
    public void testCount(){
        assertNotNull academicHonorCompositeService
        assertEquals AcademicHonorView.count(),academicHonorCompositeService.count(params)
    }

    /**
     * <p> Test to check the sort order and sorting field on AcademicHonorsCompositeService</p>
     * */
    @Test
    public void testSortOrder(){
        params.order='DESC'
        params.sort='title'
        List list = academicHonorCompositeService.list(params)
        String tempParam
        list.each{
            academicHonor->
                String title=academicHonor.titles.get(0).get('en')
                if(!tempParam){
                    tempParam=title
                }
                assertTrue tempParam.compareTo(title)>0 || tempParam.compareTo(title)==0
                tempParam=title
        }

        params.clear()
        params.order='ASC'
        params.sort='title'
        list = academicHonorCompositeService.list(params)
        tempParam=null
        list.each{
            academicHonor->
                String title=academicHonor.titles.get(0).get('en')
                if(!tempParam){
                    tempParam=title
                }
                assertTrue tempParam.compareTo(title)<0 || tempParam.compareTo(title)==0
                tempParam=title
        }
    }

    /**
     * <p> Tests to check the offset criteria on AcademicHonorsCompositeService</p>
     * */
    @Test
    public void testOffsetCriteria(){
        List academicHonorList = academicHonorCompositeService.list(params)
        assertNotNull academicHonorList
        params.offset='2'
        List academicHonorListNew = academicHonorCompositeService.list(params)
        assertNotNull academicHonorListNew

        AcademicHonor academicHonor = academicHonorList.get(2)
        assertNotNull academicHonor
        AcademicHonor newAcademicHonor = academicHonorListNew.get(0)
        assertNotNull newAcademicHonor

        assertEquals academicHonor?.guid,newAcademicHonor?.guid
        assertEquals academicHonor?.description,newAcademicHonor?.description
        assertEquals academicHonor?.code,newAcademicHonor?.code
        assertEquals academicHonor?.dataOrigin,newAcademicHonor?.dataOrigin
        assertEquals academicHonor?.titles,newAcademicHonor?.titles
        assertEquals academicHonor?.organization,newAcademicHonor?.organization

    }

    /**
     * <p> Test to get a single record from AcademicHonorsCompositeService</p>
     * */
    @Test
    public void testShow(){
        List list = academicHonorCompositeService.list(params)
        assertNotNull list
        assertTrue list.size()>0
        AcademicHonor academicHonor = list.get(0)
        assertNotNull academicHonor

        AcademicHonor newAcademicHonor = academicHonorCompositeService.get(academicHonor.guid)
        assertNotNull newAcademicHonor
        assertEquals academicHonor.descriptions,newAcademicHonor.descriptions
        assertEquals academicHonor.guid,newAcademicHonor.guid
        assertEquals academicHonor.honorType,newAcademicHonor.honorType
    }

    /**
     * <p> Test to pass an invalid guid to AcademicHonorsCompositeService which will return an exception saying record not found</p>
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
     * <p> Test to pass another LDM guid to AcademicHonorsCompositeService which will throw exception saying Invalid Guid for AcademicHonorsService</p>
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
     * <p> Test show for AcademicHonorsCompositeService with null guid </p>
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
