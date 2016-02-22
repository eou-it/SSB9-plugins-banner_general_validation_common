/** *******************************************************************************
 Copyright 2014-2015 Ellucian Company L.P. and its affiliates.
 ********************************************************************************* */
package net.hedtech.banner.general.system.ldm

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.general.common.GeneralValidationCommonConstants
import net.hedtech.banner.general.overall.IntegrationConfiguration
import net.hedtech.banner.general.overall.ldm.GlobalUniqueIdentifier
import net.hedtech.banner.general.overall.ldm.LdmService
import net.hedtech.banner.general.system.MaritalStatus
import net.hedtech.banner.general.system.Race
import net.hedtech.banner.general.system.RegulatoryRace
import net.hedtech.banner.general.system.ldm.v1.RaceDetail
import net.hedtech.banner.general.system.ldm.v4.MaritalStatusMaritalCategory
import net.hedtech.banner.general.system.ldm.v4.RaceRacialCategory
import net.hedtech.banner.restfulapi.RestfulApiValidationException
import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.codehaus.groovy.grails.plugins.testing.GrailsMockHttpServletRequest
import org.junit.Before
import org.junit.Test


class RaceCompositeServiceIntegrationTests extends BaseIntegrationTestCase {

    Race i_success_race
    def raceCompositeService
    private String invalid_sort_orderErrorMessage = 'RestfulApiValidationUtility.invalidSortField'
    Map i_success_input_content
    def i_creation_guid = 'ttt_guid'

    @Before
    public void setUp() {
        formContext = ['GUAGMNU']
        super.setUp()
        initiializeDataReferences()
    }


    private void initiializeDataReferences() {
        newRace()
        i_success_race = Race.findByRace('TTT')
        i_success_input_content = [race: 'TWO', metadata: [dataOrigin: 'Banner'], description: 'The Y code']
    }

    @Test
    void testListWithoutPaginationParams() {
        //we will forcefully set the accept header so that the tests go through all possible code flows
        GrailsMockHttpServletRequest request = LdmService.getHttpServletRequest()
        request.addHeader("Accept", "application/vnd.hedtech.integration.v1+json")
        List races = raceCompositeService.list([:])
        assertNotNull races
        assertFalse races.isEmpty()
        assertTrue races.size() > 0
    }

    @Test
    void testListWithPagination() {
        //we will forcefully set the accept header so that the tests go through all possible code flows
        GrailsMockHttpServletRequest request = LdmService.getHttpServletRequest()
        request.addHeader("Accept", "application/vnd.hedtech.integration.v1+json")
        def paginationParams = [max: '2', offset: '0']
        List races = raceCompositeService.list(paginationParams)
        assertNotNull races
        assertFalse races.isEmpty()
        assertTrue races.size() == 2
    }

    @Test
    void testCount() {
        //we will forcefully set the accept header so that the tests go through all possible code flows
        GrailsMockHttpServletRequest request = LdmService.getHttpServletRequest()
        request.addHeader("Accept", "application/vnd.hedtech.integration.v1+json")
        assertNotNull i_success_race
        assertEquals Race.count(), raceCompositeService.count([max:500,offset:0])
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
        //we will forcefully set the accept header so that the tests go through all possible code flows
        GrailsMockHttpServletRequest request = LdmService.getHttpServletRequest()
        request.addHeader("Accept", "application/vnd.hedtech.integration.v1+json")
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

    @Test
    void testCreateV1Header() {
        //we will forcefully set the accept header so that the tests go through all possible code flows
        GrailsMockHttpServletRequest request = LdmService.getHttpServletRequest()
        request.addHeader("Accept", "application/vnd.hedtech.integration.v1+json")
        request.addHeader("Content-Type", "application/vnd.hedtech.integration.v1+json")
        RaceDetail raceDetail = raceCompositeService.create(i_success_input_content)
        assertNotNull raceDetail
        assertEquals i_success_input_content.race, raceDetail.race
        assertEquals i_success_input_content.description, raceDetail.description
        assertEquals i_success_input_content.metadata.dataOrigin, raceDetail.dataOrigin
    }

    @Test
    void testCreateV4Header() {
        i_success_input_content.put('racialCategory','asian')
        //we will forcefully set the accept header so that the tests go through all possible code flows
        GrailsMockHttpServletRequest request = LdmService.getHttpServletRequest()
        request.addHeader("Accept", "application/vnd.hedtech.integration.v4+json")
        request.addHeader("Content-Type", "application/vnd.hedtech.integration.v4+json")
        RaceDetail raceDetail = raceCompositeService.create(i_success_input_content)
        assertNotNull raceDetail
        assertEquals i_success_input_content.race, raceDetail.race
        assertEquals i_success_input_content.description, raceDetail.description
        assertEquals i_success_input_content.metadata.dataOrigin, raceDetail.dataOrigin
    }

    @Test
    void testCreateWithNoCode() {
        i_success_input_content.remove('race')
        try {
           raceCompositeService.create(i_success_input_content)
        } catch (Exception ae) {
            assertApplicationException ae, "code.required"
        }
    }

    @Test
    void testCreateWithNoDesc() {
        i_success_input_content.remove('description')
        try {
            raceCompositeService.create(i_success_input_content)
        } catch (Exception ae) {
            assertApplicationException ae, "description.required"
        }
    }

    @Test
    void testCreateWithAlreadyExistCodeV4Header() {
        i_success_input_content.put('racialCategory','asian')
        i_success_input_content?.race = i_success_race?.race
        //we will forcefully set the accept header so that the tests go through all possible code flows
        GrailsMockHttpServletRequest request = LdmService.getHttpServletRequest()
        request.addHeader("Accept", "application/vnd.hedtech.integration.v4+json")
        request.addHeader("Content-Type", "application/vnd.hedtech.integration.v4+json")
        try{
            raceCompositeService.create(i_success_input_content)
        }catch (Exception ae){
            assertApplicationException ae, "code.exists"
        }
    }

    @Test
    void testUpdateNoId() {
        try {
            raceCompositeService.update(i_success_input_content)
        } catch (ApplicationException ae) {
            assertApplicationException ae, "NotFoundException"
        }
    }

    @Test
    void testUpdateCreateV1Header() {
        //we will forcefully set the accept header so that the tests go through all possible code flows
        GrailsMockHttpServletRequest request = LdmService.getHttpServletRequest()
        request.addHeader("Accept", "application/vnd.hedtech.integration.v1+json")
        request.addHeader("Content-Type", "application/vnd.hedtech.integration.v1+json")
        i_success_input_content.put('id', i_creation_guid)
        RaceDetail raceDetail = raceCompositeService.update(i_success_input_content)
        assertNotNull raceDetail
        assertEquals i_creation_guid, raceDetail.guid
        assertEquals i_success_input_content.race, raceDetail.race
        assertEquals i_success_input_content.description, raceDetail.description
        assertEquals i_success_input_content.metadata.dataOrigin, raceDetail.dataOrigin
    }

    @Test
    void testUpdateCreateV4Header() {
        i_success_input_content.put('racialCategory','asian')
        //we will forcefully set the accept header so that the tests go through all possible code flows
        GrailsMockHttpServletRequest request = LdmService.getHttpServletRequest()
        request.addHeader("Accept", "application/vnd.hedtech.integration.v4+json")
        request.addHeader("Content-Type", "application/vnd.hedtech.integration.v4+json")
        i_success_input_content.put('id', i_creation_guid)
        RaceDetail raceDetail = raceCompositeService.update(i_success_input_content)
        assertNotNull raceDetail
        assertEquals i_creation_guid, raceDetail.guid
        assertEquals i_success_input_content.race, raceDetail.race
        assertEquals i_success_input_content.description, raceDetail.description
        assertEquals i_success_input_content.metadata.dataOrigin, raceDetail.dataOrigin
    }

    @Test
    void testUpdateRaceV1Header() {
        //we will forcefully set the accept header so that the tests go through all possible code flows
        GrailsMockHttpServletRequest request = LdmService.getHttpServletRequest()
        request.addHeader("Accept", "application/vnd.hedtech.integration.v1+json")
        request.addHeader("Content-Type", "application/vnd.hedtech.integration.v1+json")
        RaceDetail raceDetail = raceCompositeService.create(i_success_input_content)
        assertNotNull raceDetail
        i_success_input_content.put('id', raceDetail.guid)
        i_success_input_content.put('description', 'The N code')
        raceDetail = raceCompositeService.update(i_success_input_content)
        assertEquals i_success_input_content.race, raceDetail.race
        assertEquals 'The N code', raceDetail.description
        assertEquals i_success_input_content.metadata.dataOrigin, raceDetail.dataOrigin
    }

    @Test
    void testUpdateRaceV4Header() {
        //we will forcefully set the accept header so that the tests go through all possible code flows
        GrailsMockHttpServletRequest request = LdmService.getHttpServletRequest()
        request.addHeader("Accept", "application/vnd.hedtech.integration.v4+json")
        request.addHeader("Content-Type", "application/vnd.hedtech.integration.v4+json")
        i_success_input_content.put('id',GlobalUniqueIdentifier.findByDomainKeyAndLdmName(i_success_race.race,GeneralValidationCommonConstants.RACE_LDM_NAME)?.guid)
        RaceDetail raceDetail = raceCompositeService.update(i_success_input_content)
        assertEquals i_success_input_content?.race, raceDetail.race
        assertEquals i_success_input_content?.description, raceDetail.description
        assertEquals i_success_input_content.metadata.dataOrigin, raceDetail.dataOrigin
    }

    @Test
    void testListWithPaginationV4Header() {
        //we will forcefully set the accept header so that the tests go through all possible code flows
        GrailsMockHttpServletRequest request = LdmService.getHttpServletRequest()
        request.addHeader("Accept", "application/vnd.hedtech.integration.v4+json")
        def paginationParams = [max: '2', offset: '0']
        List races = raceCompositeService.list(paginationParams)
        assertNotNull races
        assertFalse races.isEmpty()
        assertEquals races.size() , 2
    }

    @Test
    void testCountV4Header() {
        //we will forcefully set the accept header so that the tests go through all possible code flows
        GrailsMockHttpServletRequest request = LdmService.getHttpServletRequest()
        request.addHeader("Accept", "application/vnd.hedtech.integration.v4+json")
        assertNotNull i_success_race
        int racialCount = IntegrationConfiguration.countByTranslationValueInListAndSettingNameAndValueInList(RaceRacialCategory.RACE_RACIAL_CATEGORY,
                GeneralValidationCommonConstants.RACE_RACIAL_CATEGORY,
                Race.findAll().race)
        assertEquals racialCount, raceCompositeService.count([max:500,offset:0])
    }

    @Test
    void testGetV4Header() {
        //we will forcefully set the accept header so that the tests go through all possible code flows
        GrailsMockHttpServletRequest request = LdmService.getHttpServletRequest()
        request.addHeader("Accept", "application/vnd.hedtech.integration.v4+json")
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
    }

    @Test
    void testGetInvalidGuidV4Header() {
        try {
            raceCompositeService.get('Invalid-guid')
        } catch (ApplicationException ae) {
            assertApplicationException ae, "NotFoundException"
        }
    }

    @Test
    void testGetNonExistGuidV4Header() {
        try {
            //we will forcefully set the accept header so that the tests go through all possible code flows
            GrailsMockHttpServletRequest request = LdmService.getHttpServletRequest()
            request.addHeader("Accept", "application/vnd.hedtech.integration.v4+json")
            raceCompositeService.get(GlobalUniqueIdentifier.findByDomainKeyAndLdmName(i_success_race.race,GeneralValidationCommonConstants.RACE_LDM_NAME)?.guid)
        } catch (ApplicationException ae) {
            assertApplicationException ae, "NotFoundException"
        }
    }
}
