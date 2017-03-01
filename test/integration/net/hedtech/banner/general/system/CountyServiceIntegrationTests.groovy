package net.hedtech.banner.general.system

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.service.ServiceBase
import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.junit.After
import org.junit.Before
import org.junit.Test

class CountyServiceIntegrationTests extends BaseIntegrationTestCase {

    def countyService

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
    void testListCountys() {
        def countyList = County.list()

        assertTrue 200 < countyList.size()
    }

    @Test
    void testFetchCountyList() {
        def countyList = countyService.fetchCountyList()

        assertEquals 10, countyList.size()
    }

    @Test
    void testFetchCountyListFifty() {
        def countyList = countyService.fetchCountyList(50)

        assertEquals 50, countyList.size()
        assertEquals 'Ada County', countyList[0].description
        assertEquals 'Clay County', countyList[49].description
    }

    @Test
    void testFetchCountyListMidList() {
        def countyList = countyService.fetchCountyList(12, 20)

        assertEquals 12, countyList.size()
        assertEquals 'Boulder County', countyList[0].description
    }

    @Test
    void testFetchEaCountysList() {
        def countyList = countyService.fetchCountyList(10, 0, 'Ea')

        assertEquals 10, countyList.size()
        assertEquals 'Cape Girardeau County', countyList[0].description
    }

    @Test
    void testFetchEaCountysMidList() {
        def countyList = countyService.fetchCountyList(5, 2, 'Ea')

        assertEquals 5, countyList.size()
        assertEquals 'Eau Claire County', countyList[0].description
    }


    @Test
    void testInvalidCounty(){
        try{
            countyService.fetchCounty('DJD')
            fail("I should have received an error but it passed; @@r1:invalidCounty@@ ")
        }
        catch (ApplicationException ae) {
            assertApplicationException ae, "invalidCounty"
        }
    }

    @Test
    void testValidCounty(){
        def county = countyService.fetchCounty('118')
        assertEquals 'Layfayette County', county.description
    }
}
