/** *******************************************************************************
 Copyright 2014-2016 Ellucian Company L.P. and its affiliates.
 ********************************************************************************* */
package net.hedtech.banner.general.system


import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.junit.After
import org.junit.Before
import org.junit.Test


class RaceServiceIntegrationTests extends BaseIntegrationTestCase{

    def raceService

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
    void testCreateRace() {
        def race = newRace()
        race = raceService.create([domainModel:race])
        assertNotNull race
        assertNotNull race.id
    }

    @Test
    void testUpdateRace() {
        def race = newRace()
        race = raceService.create([domainModel:race])

        Race raceUpdate = Race.findWhere(race: "TTT")
        assertNotNull raceUpdate

        raceUpdate.description = "ZZ"
        raceUpdate = raceService.update([domainModel:raceUpdate])
        assertEquals "ZZ", raceUpdate.description
    }

    @Test
    void testDeleteRace() {
        def race = newRace()
        race = raceService.create([domainModel:race])
        assertNotNull race

        def id = race.id
        def deleteMe = Race.get(id)
        raceService.delete([domainModel:race])
        assertNull Race.get(id)

    }

    @Test
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
