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
    public void testSortOrderOnCode(){
        params.order='DESC'
        params.sort='code'
        List list = academicHonorCompositeService.list(params)
        String tempParam
        list.each{
            academicHonor->
                String code=academicHonor.code
                if(!tempParam){
                    tempParam=code
                }
                assertTrue tempParam.compareTo(code)>0 || tempParam.compareTo(code)==0
                tempParam=code
        }

        params.clear()
        params.order='ASC'
        params.sort='code'
        list = academicHonorCompositeService.list(params)
        tempParam=null
        list.each{
            academicHonor->
                String code=academicHonor.code
                if(!tempParam){
                    tempParam=code
                }
                assertTrue tempParam.compareTo(code)<0 || tempParam.compareTo(code)==0
                tempParam=code
        }
    }

    @Test
    public void testSortByType(){
        params.order='ASC'
        params.sort='type'
        List list = academicHonorCompositeService.list(params)
        assertNotNull list
        def tempParam=null
        list.each{
            academicHonor->
                String type=academicHonor.honorType
                if(!tempParam){
                    tempParam=type
                }
                assertTrue tempParam.compareTo(type)<0 || tempParam.compareTo(type)==0
                tempParam=type
        }

        params.clear()
        params.order='DESC'
        params.sort='type'
        list = academicHonorCompositeService.list(params)
        assertNotNull list
        tempParam=null
        list.each{
            academicHonor->
                String type=academicHonor.honorType
                if(!tempParam){
                    tempParam=type
                }
                assertTrue tempParam.compareTo(type)>0 || tempParam.compareTo(type)==0
                tempParam=type
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
        assertEquals academicHonor?.title,newAcademicHonor?.title
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
        assertEquals academicHonor.title,newAcademicHonor.title
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
