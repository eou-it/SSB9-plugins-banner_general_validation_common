/*******************************************************************************
 Copyright 2016 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/

package net.hedtech.banner.general.system

import groovy.sql.Sql
import net.hedtech.banner.general.common.GeneralValidationCommonConstants
import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.junit.After
import org.junit.Before
import org.junit.Test

/**
 * <p>Integration Test cases for AcademicHonorService</p>
 * */
class AcademicHonorServiceIntegrationTests extends BaseIntegrationTestCase {

    def academicHonorService

    @Before
    public void setUp() {
        formContext = ['GUAGMNU'] // Since we are not testing a controller, we need to explicitly set this
        super.setUp()
    }


    @After
    public void tearDown() {
        super.tearDown()
    }

    /* *
    * Test to check count
    * */

    @Test
    public void testCount() {
        Sql sql
        try {
            Integer count = academicHonorService.countRecord()
            assertNotNull count
            sql = new Sql(sessionFactory.getCurrentSession().connection())
            def result = sql.firstRow("SELECT COUNT(*) as cnt FROM gvq_acad_honors")
            Long expectCount = result.cnt
            assertNotNull expectCount
            assertEquals expectCount, count
        }
        finally {
            sql?.close() // note that the test will close the connection, since it's our current session's connection
        }

    }

    /* *
    * Test to check count
    * */

    @Test
    public void testCountWithFilter() {
        Sql sql
        try {
            Integer count = academicHonorService.countRecordWithFilter(GeneralValidationCommonConstants.LDM_NAME_INSTITUTIONAL)
            assertNotNull count
            sql = new Sql(sessionFactory.getCurrentSession().connection())
            def result = sql.firstRow("SELECT COUNT(*) as cnt FROM gvq_acad_honors  WHERE honor_type= '" + GeneralValidationCommonConstants.LDM_NAME_INSTITUTIONAL + "'")
            Long expectCount = result.cnt
            assertNotNull expectCount
            assertEquals expectCount, count
        }
        finally {
            sql?.close() // note that the test will close the connection, since it's our current session's connection
        }
    }

    /*
    * Tests validate for list
    * */

    @Test
    void testFetchList() {
        def params = [max: '10', offset: '0']
        def academicHonorList = academicHonorService.fetchAll(params)
        assertNotNull academicHonorList
    }

    /*
     * Tests validate for filter
     * */

    @Test
    void fetchCrossListByType() {
        def params = academicHonorFilterMap()
        def academicHonorList = academicHonorService.fetchByType(params.get("type"), params)
        assertNotNull academicHonorList
    }

    /*
    * create a param map
    * */

    private Map academicHonorFilterMap() {
        def param = [max: '10', offset: '0', type: 'award']
        return param
    }

    /** Tests validate for crosslist sections*/
    @Test
    void fetchAcademinHonorByGuid() {
        def params = [max: '10', offset: '0']
        def academicHonorList = academicHonorService.fetchAll(params)
        def academicHonorGuid = academicHonorList.get(0).id
        def crossList = academicHonorService.fetchByGuid(academicHonorGuid)
        assertNotNull crossList
    }

    @Test
    void testFetchAllByCodeInList() {
        assertEquals(AcademicHonorView.findAll(), academicHonorService.fetchAllByCodeInList(AcademicHonorView.findAll().code))
    }

}
