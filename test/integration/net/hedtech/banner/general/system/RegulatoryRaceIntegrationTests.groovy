/*******************************************************************************
 Copyright 2013 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.system
import org.junit.Before
import org.junit.Test
import org.junit.After

import groovy.sql.Sql
import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.springframework.orm.hibernate3.HibernateOptimisticLockingFailureException

import java.text.SimpleDateFormat

class RegulatoryRaceIntegrationTests extends BaseIntegrationTestCase {

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
    void testCreateRegulatoryRace() {
        def regulatoryRace = newRegulatoryRace()
        save regulatoryRace
        //Test if the generated entity now has an id assigned
        assertNotNull regulatoryRace.id
    }


    @Test
    void testUpdateRegulatoryRace() {
        def regulatoryRace = newRegulatoryRace()
        save regulatoryRace

        assertNotNull regulatoryRace.id
        groovy.util.GroovyTestCase.assertEquals(0L, regulatoryRace.version)
        assertEquals("T", regulatoryRace.code)
        assertEquals("TTTTT", regulatoryRace.description)
        assertTrue(regulatoryRace.systemRequiredIndicator)

        //Update the entity
        regulatoryRace.code = "U"
        regulatoryRace.description = "UUUUU"
        regulatoryRace.systemRequiredIndicator = false
        regulatoryRace.lastModified = new Date()
        regulatoryRace.lastModifiedBy = "test"
        regulatoryRace.dataOrigin = "Banner"

        save regulatoryRace

        regulatoryRace = RegulatoryRace.get(regulatoryRace.id)
        groovy.util.GroovyTestCase.assertEquals new Long(1), regulatoryRace?.version
        assertEquals("U", regulatoryRace.code)
        assertEquals("UUUUU", regulatoryRace.description)
        assertFalse(regulatoryRace.systemRequiredIndicator)

    }


    @Test
    void testDates() {
        def hour = new SimpleDateFormat('HH')
        def date = new SimpleDateFormat('yyyy-M-d')
        def today = new Date()

        def regulatoryRace = newRegulatoryRace()

        regulatoryRace.save(flush: true, failOnError: true)
        regulatoryRace.refresh()
        assertNotNull "RegulatoryRace should have been saved", regulatoryRace.id

        // test date values -
        assertEquals date.format(today), date.format(regulatoryRace.lastModified)
        assertEquals hour.format(today), hour.format(regulatoryRace.lastModified)
    }


    @Test
    void testOptimisticLock() {
        def regulatoryRace = newRegulatoryRace()
        save regulatoryRace

        def sql
        try {
            sql = new Sql(sessionFactory.getCurrentSession().connection())
            sql.executeUpdate("update GTVRRAC set GTVRRAC_VERSION = 999 where GTVRRAC_SURROGATE_ID = ?", [regulatoryRace.id])
        } finally {
            sql?.close() // note that the test will close the connection, since it's our current session's connection
        }
        //Try to update the entity
        regulatoryRace.code = "U"
        regulatoryRace.description = "UUUUU"
        regulatoryRace.systemRequiredIndicator = true
        regulatoryRace.lastModified = new Date()
        regulatoryRace.lastModifiedBy = "test"
        regulatoryRace.dataOrigin = "Banner"
        shouldFail(HibernateOptimisticLockingFailureException) {
            regulatoryRace.save(flush: true, failOnError: true)
        }
    }


    @Test
    void testDeleteRegulatoryRace() {
        def regulatoryRace = newRegulatoryRace()
        save regulatoryRace
        def id = regulatoryRace.id
        assertNotNull id
        regulatoryRace.delete()
        assertNull RegulatoryRace.get(id)
    }


    @Test
    void testValidation() {
        def regulatoryRace = new RegulatoryRace()
        assertFalse "RegulatoryRace could not be validated as expected due to ${regulatoryRace.errors}", regulatoryRace.validate()
    }


    @Test
    void testNullValidationFailure() {
        def regulatoryRace = new RegulatoryRace()
        assertFalse "RegulatoryRace should have failed validation", regulatoryRace.validate()
        assertErrorsFor(regulatoryRace, 'nullable', ['code', 'description', 'systemRequiredIndicator'])
    }


    @Test
    void testMaxSizeValidationFailures() {
        def regulatoryRace = new RegulatoryRace(
                code: 'XXXX',
                description: 'XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX')
        assertFalse "RegulatoryRace should have failed validation", regulatoryRace.validate()
        assertErrorsFor(regulatoryRace, 'maxSize', ['code', 'description'])
    }

    @Test
    void testFetchAllByRegulatoryRaceSuccess() {
            RegulatoryRace regulatoryRace = newRegulatoryRace()
            save regulatoryRace
            def id=regulatoryRace.id
            assertNotNull id

            List<RegulatoryRace> quiredRegulatoryRace = RegulatoryRace.fetchRequiredRegulatoryRaces()
            assertNotNull quiredRegulatoryRace
            assertFalse quiredRegulatoryRace.isEmpty()

            regulatoryRace.delete()
            assertNull RegulatoryRace.get(id)


        }


    private def newRegulatoryRace() {
        new RegulatoryRace(code: "T", description: "TTTTT", systemRequiredIndicator: true, lastModified: new Date(),
                lastModifiedBy: "test", dataOrigin: "Banner")
    }

}
