/*******************************************************************************
 Copyright 2016 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.general.system.ldm

import groovy.sql.Sql
import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.general.system.AcademicHonorView
import net.hedtech.banner.general.system.ldm.v6.AcademicHonor
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
        assertFalse list.isEmpty()
        assertEquals list.honorType as Set,['award'] as Set
    }

    /**
     * <p>Test to list the Academic Honors of type 'distinction' from AcademicHonorsCompositeService</p>
     * */
    @Test
    public void testDistinctionHonors(){
        params.type='distinction'
        List list = academicHonorCompositeService.list(params)
        assertFalse list.isEmpty()
        assertTrue(['distinction'].containsAll(list.honorType))
    }

    /* *
     * Test to check count
     * */
    @Test
    public void testCount(){
        def count = academicHonorCompositeService.count(params)
        assertNotNull count
        Sql sql = new Sql(sessionFactory.getCurrentSession().connection())
        def result = sql.firstRow("SELECT COUNT(*) as cnt FROM gvq_acad_honors")
        Long expectCount = result.cnt
        assertNotNull expectCount
        assertEquals expectCount,count
    }


    /* *
    * Test to check count
    * */
    @Test
    public void testCountWithFilter(){
        params.type = "award"
        def count = academicHonorCompositeService.count(params)
        assertNotNull count
        Sql sql = new Sql(sessionFactory.getCurrentSession().connection())
        def result = sql.firstRow("SELECT COUNT(*) as cnt FROM gvq_acad_honors  WHERE honor_type='departmental-honors'")
        Long expectCount = result.cnt
        assertNotNull expectCount
        assertEquals expectCount,count
    }

    /* *
   * Test to check count
   * */
    @Test
    public void testCountWithFilterDistinction(){
        params.type = "distinction"
        def count = academicHonorCompositeService.count(params)
        assertNotNull count
        Sql sql = new Sql(sessionFactory.getCurrentSession().connection())
        def result = sql.firstRow("SELECT COUNT(*) as cnt FROM gvq_acad_honors  WHERE honor_type='institutional-honors'")
        Long expectCount = result.cnt
        assertNotNull expectCount
        assertEquals expectCount,count
    }


    /**
     * <p> Test to check the sort order and sorting field on AcademicHonorsCompositeService</p>
     * */
    @Test
    public void testSortOrderOnCode(){
        List list = academicHonorCompositeService.list(params)
        String tempParam
        list.each{
            academicHonor->
                String code=academicHonor.code
                if(!tempParam){
                    tempParam=code
                }
                assertTrue tempParam.compareTo(code)<0 || tempParam.compareTo(code)== 0
                tempParam=code
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
        assertEquals academicHonor.id,newAcademicHonor.id
        assertEquals academicHonor.title,newAcademicHonor.title
        assertEquals academicHonor.code,newAcademicHonor.code
        assertEquals academicHonor.title,newAcademicHonor.title
    }

    /**
     * <p> Test to get a single record from AcademicHonorsCompositeService</p>
     * */
    @Test
    public void testShow() {
        List list = academicHonorCompositeService.list(params)
        assertNotNull list
        assertFalse list.isEmpty()
        AcademicHonor academicHonor = list?.get(0)
        assertNotNull academicHonor
        AcademicHonor newAcademicHonor = academicHonorCompositeService.get(academicHonor.id)
        assertNotNull newAcademicHonor
        assertEquals academicHonor.title, newAcademicHonor.title
        assertEquals academicHonor.id, newAcademicHonor.id
        assertEquals academicHonor.getHonorType(), newAcademicHonor.getHonorType()
        assertEquals academicHonor.type, newAcademicHonor.type


    }

    /**
     * <p> Test to pass an invalid guid to AcademicHonorsCompositeService which will return an exception saying record not found</p>
     * */
    @Test
    public void testInvalidShow(){
        List list = academicHonorCompositeService.list(params)
        assertNotNull list
        assertFalse list.isEmpty()
        AcademicHonor academicHonor = list.get(0)
        assertNotNull academicHonor
        try {
            academicHonorCompositeService.get(academicHonor.id.substring(0,academicHonor.id.length()-2))
        } catch (ApplicationException ae) {
            assertApplicationException ae, "NotFoundException"
        }
    }


    /**
     * <p> Test show for AcademicHonorsCompositeService with null guid </p>
     * */
    @Test
    void testGetNullGuid() {
        try {
            academicHonorCompositeService.get(null)
        } catch (ApplicationException ae) {
            assertApplicationException ae, "NotFoundException"
        }
    }

}
