/*******************************************************************************
 Copyright 2013 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.system

import groovy.sql.Sql
import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.springframework.orm.hibernate3.HibernateOptimisticLockingFailureException

import java.text.SimpleDateFormat

class RaceIntegrationTests extends BaseIntegrationTestCase {

    protected void setUp() {
        formContext = ['GUAGMNU']
        super.setUp()
    }


    protected void tearDown() {
        super.tearDown()
    }


    void testCreateRace() {
        def race = newRace()
        race.save(failOnError: true, flush: true)
        //Test if the generated entity now has an id assigned
        assertNotNull race.id
    }


    void testUpdateRace() {
        def race = newRace()
        race.save(failOnError: true, flush: true)
        assertNotNull race.id
        assertEquals 0L, race.version
        assertEquals "TTT", race.race
        assertEquals "TTTTTTTTTT", race.description
        assertEquals "T", race.electronicDataInterchangeEquivalent
        assertEquals "T", race.lmsEquivalent

        //Update the entity
        race.description = "UUUUUUUUUU"
        race.electronicDataInterchangeEquivalent = "U"
        race.lmsEquivalent = "U"
        race.regulatoryRace = RegulatoryRace.findByCode("2")
        race.save(failOnError: true, flush: true)
        //Assert for sucessful update
        race = Race.get(race.id)
        assertEquals 1L, race?.version
        assertEquals "UUUUUUUUUU", race.description
        assertEquals "U", race.electronicDataInterchangeEquivalent
        assertEquals "U", race.lmsEquivalent
        assertEquals "2", race?.regulatoryRace?.code
    }

    void testDates() {
        def hour = new SimpleDateFormat('HH')
        def date = new SimpleDateFormat('yyyy-M-d')
        def today = new Date()

        def race = newRace()

        race.save(flush: true, failOnError: true)
        race.refresh()
        assertNotNull "Race should have been saved", race.id

        // test date values -
        assertEquals date.format(today), date.format(race.lastModified)
        assertEquals hour.format(today), hour.format(race.lastModified)
    }


    void testOptimisticLock() {
        def race = newRace()
        race.save(failOnError: true, flush: true)

        def sql
        try {
            sql = new Sql(sessionFactory.getCurrentSession().connection())
            sql.executeUpdate("update GV_GORRACE set GORRACE_VERSION = 999 where GORRACE_SURROGATE_ID = ?", [race.id])
        } finally {
            sql?.close() // note that the test will close the connection, since it's our current session's connection
        }
        //Try to update the entity
        //Update the entity
        race.description = "UUUUUUUUUU"
        race.electronicDataInterchangeEquivalent = "U"
        race.lmsEquivalent = "U"
        shouldFail(HibernateOptimisticLockingFailureException) {
            race.save(failOnError: true, flush: true)
        }
    }


    void testDeleteRace() {
        def race = newRace()
        race.save(failOnError: true, flush: true)
        def id = race.id
        assertNotNull id
        race.delete()
        assertNull Race.get(id)
    }


    void testValidation() {
        def race = new Race()
        assertFalse "Race could not be validated as expected due to ${race.errors}", race.validate()
    }


    void testNullValidationFailure() {
        def race = new Race()
        assertFalse "Race should have failed validation", race.validate()
        assertErrorsFor race, 'nullable', [ 'race', 'description' ]
        assertNoErrorsFor race, [ 'electronicDataInterchangeEquivalent', 'lmsEquivalent', 'regulatoryRace'
                ]
    }


    void testFetchAllLikeRaceOrDescription() {
        def races = Race.fetchAllLikeRaceOrDescription("AA")
        assertNotNull  races
        assertTrue races.size() > 1
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
