/** *****************************************************************************
 Copyright 2016-2019 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */

package net.hedtech.banner.general.system

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.hibernate.Session
import org.junit.After
import org.junit.Before
import org.junit.Test

class CountyServiceIntegrationTests extends BaseIntegrationTestCase {

    def countyService

    final String COUNTY_CODE_HARRI = 'HARRI'
    final String COUNTY_CODE_TARRA = 'TARRA'
    final String COUNTY_CODE_001 = '001'

    final String ISO_COUNTY_HARRI_GB_CHE = 'GB-CHE'
    final String ISO_COUNTY_TARRA_GB_DAL = 'GB-DAL'
    final String ISO_COUNTY_001_GB_ESS = 'GB-ESS'

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
        assertEquals 'Abbeville, SC', countyList[0].description
        assertEquals 'Allamakee, IA', countyList[49].description
    }

    @Test
    void testFetchCountyListMidList() {
        def countyList = countyService.fetchCountyList(12, 20)

        assertEquals 12, countyList.size()
        assertEquals 'Addison, VT', countyList[0].description
    }

    @Test
    void testFetchEaCountysList() {
        def countyList = countyService.fetchCountyList(10, 0, 'Ea')

        assertEquals 10, countyList.size()
        assertEquals 'Aleutians East, AK', countyList[0].description
    }

    @Test
    void testFetchEaCountysMidList() {
        def countyList = countyService.fetchCountyList(5, 2, 'Ea')

        assertEquals 5, countyList.size()
        assertEquals 'Bear Lake, ID', countyList[0].description
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

    @Test
    void test_fetchAllByCodeInList(){
        def countyCodes = [COUNTY_CODE_HARRI, COUNTY_CODE_TARRA, COUNTY_CODE_001]

        updateCountyWithIsoCodeInList(countyCodes)

        def counties = countyService.fetchAllByCodeInList(countyCodes)
        assertNotNull counties
        assertTrue counties.size() > 0

        def county_harri = counties.find { county -> county.code == COUNTY_CODE_HARRI}
        assertNotNull county_harri

        def county_tarra = counties.find { county -> county.code == COUNTY_CODE_TARRA}
        assertNotNull county_tarra

        def county_001 = counties.find { county -> county.code == COUNTY_CODE_001}
        assertNotNull county_001
    }

    @Test
    void test_fetchIsoCodeToCountyCodeMap() {
        def countyCodes = [COUNTY_CODE_HARRI, COUNTY_CODE_TARRA, COUNTY_CODE_001]

        updateCountyWithIsoCodeInList(countyCodes)

        def counties = countyService.fetchAllByCodeInList(countyCodes)
        assertNotNull counties
        assertTrue counties.size() > 0

        Map countiesMap = countyService.fetchIsoCodeToCountyCodeMap([COUNTY_CODE_HARRI, COUNTY_CODE_TARRA])
        assertNotNull countiesMap
        assertTrue countiesMap.size() == 2

        County countyNewHarri = countiesMap.get(COUNTY_CODE_HARRI)
        assertNotNull countyNewHarri
        assertNotNull countyNewHarri.id
        assertNotNull countyNewHarri.isoCode
        assertTrue ISO_COUNTY_HARRI_GB_CHE.equalsIgnoreCase(countyNewHarri.isoCode)

        County countyNewTarra = countiesMap.get(COUNTY_CODE_TARRA)
        assertNotNull countyNewTarra
        assertNotNull countyNewTarra.id
        assertNotNull countyNewTarra.isoCode
        assertTrue ISO_COUNTY_TARRA_GB_DAL.equalsIgnoreCase(countyNewTarra.isoCode)

    }

    private updateCountyWithIsoCodeInList(Collection<String> countyCodes) {
        Session session = sessionFactory.getCurrentSession()

        if(countyCodes && countyCodes.contains(COUNTY_CODE_HARRI)) {
            session.createSQLQuery( "UPDATE STVCNTY set STVCNTY_SCOD_CODE_ISO = '" + ISO_COUNTY_HARRI_GB_CHE + "' WHERE STVCNTY_CODE ='" + COUNTY_CODE_HARRI + "' ").executeUpdate()
        }

        if(countyCodes && countyCodes.contains(COUNTY_CODE_TARRA)) {
            session.createSQLQuery( "UPDATE STVCNTY set STVCNTY_SCOD_CODE_ISO = '" + ISO_COUNTY_TARRA_GB_DAL + "' WHERE STVCNTY_CODE ='" + COUNTY_CODE_TARRA + "' ").executeUpdate()
        }

        if(countyCodes && countyCodes.contains(COUNTY_CODE_001)) {
            session.createSQLQuery( "UPDATE STVCNTY set STVCNTY_SCOD_CODE_ISO = '" + ISO_COUNTY_001_GB_ESS + "' WHERE STVCNTY_CODE ='" + COUNTY_CODE_001 + "' ").executeUpdate()
        }
    }
}
