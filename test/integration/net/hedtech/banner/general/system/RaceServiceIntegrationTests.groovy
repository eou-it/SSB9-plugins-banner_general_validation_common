/** *******************************************************************************
 Copyright 2014 Ellucian Company L.P. and its affiliates.
 ********************************************************************************* */
package net.hedtech.banner.general.system


import net.hedtech.banner.testing.BaseIntegrationTestCase


class RaceServiceIntegrationTests extends BaseIntegrationTestCase{

    def raceService

    protected void setUp() {
        formContext = ['GUAGMNU'] // Since we are not testing a controller, we need to explicitly set this
        super.setUp()
    }


    protected void tearDown() {
        super.tearDown()
    }


    void testCreateRace() {
        def race = newRace()
        race = raceService.create([domainModel:race])
        assertNotNull race
        assertNotNull race.id
    }


    void testUpdateRace() {
        def race = newRace()
        race = raceService.create([domainModel:race])

        Race raceUpdate = Race.findWhere(race: "TTT")
        assertNotNull raceUpdate

        raceUpdate.description = "ZZ"
        raceUpdate = raceService.update([domainModel:raceUpdate])
        assertEquals "ZZ", raceUpdate.description
    }


    void testDeleteRace() {
        def race = newRace()
        race = raceService.create([domainModel:race])
        assertNotNull race

        def id = race.id
        def deleteMe = Race.get(id)
        raceService.delete([domainModel:race])
        assertNull Race.get(id)

    }

    void testList(){
        def race = raceService.list()
        assertTrue race.size() > 0
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
        return race
    }
}
