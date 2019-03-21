/** *****************************************************************************
 Copyright 2009-2013 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.system
import org.junit.Before
import org.junit.Test
import org.junit.After

import net.hedtech.banner.testing.BaseIntegrationTestCase
import groovy.sql.Sql
import org.springframework.orm.hibernate3.HibernateOptimisticLockingFailureException

class DegreeIntegrationTests extends BaseIntegrationTestCase {

    def degreeService


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
    void testCreateDegree() {
        def degree = newDegree()
        save degree
        //Test if the generated entity now has an id assigned
        assertNotNull degree.id
    }


    @Test
    void testUpdateDegree() {
        def degree = newDegree()
        save degree

        assertNotNull degree.id
        assertEquals(0L, degree.version)
        assertEquals("TTTTT", degree.code)
        assertEquals("TTTTT", degree.description)
        assertEquals("TT", degree.levelOne)
        assertEquals("TT", degree.levelTwo)
        assertEquals("TT", degree.levelThree)
        assertEquals("T", degree.financeCountIndicator)
        assertEquals("T", degree.systemRequiredIndicator)
        assertTrue degree.displayWebIndicator

        //Update the entity
        degree.code = "UUUUU"
        degree.description = "UUUUU"
        degree.levelOne = "UU"
        degree.levelTwo = "UU"
        degree.levelThree = "UU"
        degree.financeCountIndicator = "U"
        degree.systemRequiredIndicator = "U"
        degree.voiceResponseMsgNumber = new Integer(2)
        degree.displayWebIndicator = false
        degree.lastModified = new Date()
        degree.lastModifiedBy = "test"
        degree.dataOrigin = "Banner"

        save degree

        degree = Degree.get(degree.id)
        assertEquals new Long(1), degree?.version
        assertEquals("UUUUU", degree.code)
        assertEquals("UUUUU", degree.description)
        assertEquals("UU", degree.levelOne)
        assertEquals("UU", degree.levelTwo)
        assertEquals("UU", degree.levelThree)
        assertEquals("U", degree.financeCountIndicator)
        assertEquals("U", degree.systemRequiredIndicator)
        assertEquals(new Integer(2), degree.voiceResponseMsgNumber)
        assertEquals(false, degree.displayWebIndicator)

    }


    @Test
    void testOptimisticLock() {
        def degree = newDegree()
        save degree

        def sql
        try {
            sql = new Sql(sessionFactory.getCurrentSession().connection())
            sql.executeUpdate("update STVDEGC set STVDEGC_VERSION = 999 where STVDEGC_SURROGATE_ID = ?", [degree.id])
        } finally {
            sql?.close() // note that the test will close the connection, since it's our current session's connection
        }
        //Try to update the entity
        degree.code = "UUUUU"
        degree.description = "UUUUU"
        degree.levelOne = "UU"
        degree.levelTwo = "UU"
        degree.levelThree = "UU"
        degree.financeCountIndicator = "U"
        degree.systemRequiredIndicator = "U"
        degree.voiceResponseMsgNumber = new Integer(1)
        degree.displayWebIndicator = false
        degree.lastModified = new Date()
        degree.lastModifiedBy = "test"
        degree.dataOrigin = "Banner"
        shouldFail(HibernateOptimisticLockingFailureException) {
            degree.save(flush: true, failOnError: true)
        }
    }


    @Test
    void testDeleteDegree() {
        def degree = newDegree()
        save degree
        def id = degree.id
        assertNotNull id
        degree.delete()
        assertNull Degree.get(id)
    }


    @Test
    void testValidation() {
        def degree = newDegree()
        assertTrue "Degree could not be validated as expected due to ${degree.errors}", degree.validate()
    }


    @Test
    void testNullValidationFailure() {
        def degree = new Degree()
        assertFalse "Degree should have failed validation", degree.validate()
        assertNoErrorsFor(degree, ['levelOne', 'levelTwo', 'levelThree', 'financeCountIndicator', 'systemRequiredIndicator', 'voiceResponseMsgNumber'])
        assertErrorsFor(degree, 'nullable', ['code', 'description'])
    }


    @Test
    void testMaxSizeValidationFailures() {
        def degree = new Degree(
                code: 'XXXXXXXX',
                description: 'xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx')
        assertFalse "Degree should have failed validation", degree.validate()
        assertErrorsFor(degree, 'maxSize', ['code', 'description'])
    }


    private def newDegree() {

        def degreeLevel = new DegreeLevel(code: "TT", description: "TTTTT", numericValue: 2, lastModified: new Date(),
                lastModifiedBy: "test", dataOrigin: "Banner")
        def acat = new AwardCategory(code: "TT", description: "TTTTT", systemRequiredIndicator: "T", lastModified: new Date(),
                lastModifiedBy: "test", dataOrigin: "Banner")
        acat.save(flush: true, failOnError: true)
        degreeLevel.save(flush: true, failOnError: true)
        new Degree(code: "TTTTT", description: "TTTTT", levelOne: "TT", levelTwo: "TT",
                levelThree: "TT", financeCountIndicator: "T", systemRequiredIndicator: "T",
                voiceResponseMsgNumber: new Integer(1), displayWebIndicator: true,
                lastModified: new Date(), degreeLevelCode: degreeLevel, awardCatCode: acat,
                lastModifiedBy: "test", dataOrigin: "Banner")
    }


}
