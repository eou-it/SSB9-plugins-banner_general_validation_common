/** *******************************************************************************
 Copyright 2014-2015 Ellucian Company L.P. and its affiliates.
 ********************************************************************************* */
package net.hedtech.banner.general.system.ldm

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.general.system.Race
import net.hedtech.banner.general.system.RegulatoryRace
import net.hedtech.banner.general.system.ldm.v1.RaceDetail
import net.hedtech.banner.restfulapi.RestfulApiValidationException
import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.junit.Before
import org.junit.Test


class RaceCompositeServiceIntegrationTests extends BaseIntegrationTestCase {

    Race i_success_race
    def raceCompositeService
    private String invalid_sort_orderErrorMessage = 'RestfulApiValidationUtility.invalidSortField'

    @Before
    public void setUp() {
        formContext = ['GUAGMNU']
        super.setUp()
        initiializeDataReferences()
    }


    private void initiializeDataReferences() {
        newRace()
        i_success_race = Race.findByRace('TTT')
    }

    @Test
    void testListWithoutPaginationParams() {
        List races = raceCompositeService.list([:])
        assertNotNull races
        assertFalse races.isEmpty()
        assertTrue races.size() > 0
    }

    @Test
    void testListWithPagination() {
        def paginationParams = [max: '2', offset: '0']
        List races = raceCompositeService.list(paginationParams)
        assertNotNull races
        assertFalse races.isEmpty()
        assertTrue races.size() == 2
    }

    @Test
    void testCount() {
        assertNotNull i_success_race
        assertEquals Race.count(), raceCompositeService.count()
    }

    @Test
    void testGetInvalidGuid() {
        try {
            raceCompositeService.get('Invalid-guid')
        } catch (ApplicationException ae) {
            assertApplicationException ae, "NotFoundException"
        }
    }

    @Test
    void testGetNullGuid() {
        try {
            raceCompositeService.get(null)
        } catch (ApplicationException ae) {
            assertApplicationException ae, "NotFoundException"
        }
    }

    @Test
    void testGet() {
        def paginationParams = [max: '1', offset: '0']
        def raceDetails = raceCompositeService.list(paginationParams)
        assertNotNull raceDetails
        assertFalse raceDetails.isEmpty()

        assertNotNull raceDetails[0].guid
        def raceDetail = raceCompositeService.get(raceDetails[0].guid)
        assertNotNull raceDetail.toString()
        assertNotNull raceDetail.race
        assertEquals raceDetail.race, raceDetails[0].race
        assertNotNull raceDetail.guid
        assertEquals raceDetail.guid, raceDetails[0].guid
        assertNotNull raceDetail.parentCategory
        assertEquals raceDetail.parentCategory, raceDetails[0].parentCategory
        assertNotNull raceDetail.metadata
        assertEquals raceDetail.metadata, raceDetails[0].metadata
        assertEquals raceDetails[0], raceDetail
    }

    @Test
    void testFetchByRaceIdInvalid() {
        try {
            raceCompositeService.fetchByRaceId(null)
        } catch (ApplicationException ae) {
            assertApplicationException ae, "NotFoundException"
        }
    }

    @Test
    void testFetchByRaceId() {
        RaceDetail raceDetail = raceCompositeService.fetchByRaceId(i_success_race.id)
        assertNotNull raceDetail
        assertEquals i_success_race.id, raceDetail.id
        assertEquals i_success_race.race, raceDetail.race
        assertEquals i_success_race.description, raceDetail.description
        assertEquals i_success_race.dataOrigin, raceDetail.metadata.dataOrigin
        assertEquals raceCompositeService.getLdmRace(i_success_race.race), raceDetail.parentCategory
    }

    @Test
    void testFetchByRaceInvalid() {
        assertNull raceCompositeService.fetchByRaceCode(null)
        assertNull raceCompositeService.fetchByRaceCode('Q')
    }

    @Test
    void testFetchByRace() {
        RaceDetail raceDetail = raceCompositeService.fetchByRaceCode(i_success_race.race)
        assertNotNull raceDetail
        assertEquals i_success_race.id, raceDetail.id
        assertEquals i_success_race.race, raceDetail.race
        assertEquals i_success_race.description, raceDetail.description
        assertEquals i_success_race.dataOrigin, raceDetail.metadata.dataOrigin
        assertEquals raceCompositeService.getLdmRace(i_success_race.race), raceDetail.parentCategory
    }
/*
    //TODO: Move this function to common place
    // Return LDM enumeration value for the corresponding race code.
    def getLdmRace(def race) {
        if (race != null) {
            return RaceParentCategory.WHITE.value
        }
        return null
    }
*/

    /**
     * Test to check the RaceCompositeService list method with valid sort and order field and supported version
     * If No "Accept" header is provided, by default it takes the latest supported version
     */
    @Test
    void testListWithValidSortAndOrderFieldWithSupportedVersion() {
        def params = [order: 'ASC', sort: 'code']
        def raceList = raceCompositeService.list(params)
        assertNotNull raceList
        assertFalse raceList.isEmpty()
        assertNotNull raceList.race
        assertEquals Race.count(), raceList.size()
        assertNotNull i_success_race
        assertTrue raceList.id.contains(i_success_race.id)
        assertTrue raceList.race.contains( i_success_race.race)
        assertTrue raceList.description.contains(i_success_race.description)
        assertTrue raceList.dataOrigin.contains(i_success_race.dataOrigin)
    }

    /**
     * Test to check the sort by code on RaceCompositeService
     * */
    @Test
    public void testSortByCode(){
        params.order='ASC'
        params.sort='code'
        List list = raceCompositeService.list(params)
        assertNotNull list
        def tempParam=null
        list.each{
            race->
                String code=race.race
                if(!tempParam){
                    tempParam=code
                }
                assertTrue tempParam.compareTo(code)<0 || tempParam.compareTo(code)==0
                tempParam=code
        }

        params.clear()
        params.order='DESC'
        params.sort='code'
        list = raceCompositeService.list(params)
        assertNotNull list
        tempParam=null
        list.each{
            race->
                String code=race.race
                if(!tempParam){
                    tempParam=code
                }
                assertTrue tempParam.compareTo(code)>0 || tempParam.compareTo(code)==0
                tempParam=code
        }
    }

    /**
     * Test to check the RaceCompositeService list method with invalid sort field
     */
    @Test
    void testListWithInvalidSortField() {
        try {
            def map = [sort: 'test']
            raceCompositeService.list(map)
            fail()
        } catch (RestfulApiValidationException e) {
            assertEquals 400, e.getHttpStatusCode()
            assertEquals invalid_sort_orderErrorMessage , e.messageCode.toString()
        }
    }

    /**
     * Test to check the RaceCompositeService list method with invalid order field
     */
    @Test
    void testListWithInvalidOrderField() {
        shouldFail(RestfulApiValidationException) {
            def map = [order: 'test']
            raceCompositeService.list(map)
        }
    }

    private def newRace() {
        def regulatoryRace = RegulatoryRace.findByCode("1")
        def race = new Race(
                race: "TTT",
                description: "TTTTTTTTTT",
                electronicDataInterchangeEquivalent: "T",
                lmsEquivalent: "T",
                regulatoryRace: regulatoryRace
        )
        race.save(failOnError: true, flush: true)
    }
}
