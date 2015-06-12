/*******************************************************************************
 Copyright 2009-2015 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.general.system
import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.junit.After
import org.junit.Before
import org.junit.Test

/**
 * <p>Integration Test case for AcademicHonorView which is a Read only view</p>
 * @author Sitakant
 */
class AcademicHonorViewIntegrationTests extends BaseIntegrationTestCase {

    @Before
    public void setUp() {
        formContext = ['GUAGMNU'] // Since we are not testing a controller, we need to explicitly set this
        super.setUp()
    }

    @After
    public void tearDown() {
        super.tearDown()
    }

    @Test
    public void testCount(){
        assertTrue AcademicHonorView.count()>0
    }

    @Test
    public void testFetchDataBasedOnAwardType(){
        def type = 'departmental-honors'
        List list = AcademicHonorView.fetchByType(type)
        assertTrue list.size()>0
        AcademicHonorView academicHonorView = list.get(0)
        assertEquals academicHonorView.type,type
    }

    @Test
    public void testFetchDataBasedOnDistinctionType(){
        def type = 'institutional-honors'
        List list = AcademicHonorView.fetchByType(type)
        assertTrue list.size()>0
        AcademicHonorView academicHonorView = list.get(0)
        assertEquals academicHonorView.type,type
    }

    @Test
    public void testFetchDataByCode(){
        def code='D'
        def type = 'departmental-honors'
        AcademicHonorView academicHonorView = AcademicHonorView.fetchByCode(code,type)
        assertFalse academicHonorView==null
        assertEquals academicHonorView.code,code
    }
}
