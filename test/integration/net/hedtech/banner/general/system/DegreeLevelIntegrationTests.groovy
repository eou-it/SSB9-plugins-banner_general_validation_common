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

class DegreeLevelIntegrationTests extends BaseIntegrationTestCase {

    def degreeLevelService


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
    void testCreateDegreeLevel() {
        def degreeLevel = newDegreeLevel()
        save degreeLevel
        //Test if the generated entity now has an id assigned
        assertNotNull degreeLevel.id
    }


	@Test
    void testUpdateDegreeLevel() {
        def degreeLevel = newDegreeLevel()
        save degreeLevel

        assertNotNull degreeLevel.id
        groovy.util.GroovyTestCase.assertEquals(0L, degreeLevel.version)
        assertEquals("TT", degreeLevel.code)
        assertEquals("TTTTT", degreeLevel.description)
        assertEquals(2, degreeLevel.numericValue)

        //Update the entity
        degreeLevel.code = "UU"
        degreeLevel.description = "UUUUU"
        degreeLevel.numericValue = 1
        degreeLevel.lastModified = new Date()
        degreeLevel.lastModifiedBy = "test"
        degreeLevel.dataOrigin = "Banner"

        save degreeLevel

        degreeLevel = DegreeLevel.get(degreeLevel.id)
        groovy.util.GroovyTestCase.assertEquals new Long(1), degreeLevel?.version
        assertEquals("UU", degreeLevel.code)
        assertEquals("UUUUU", degreeLevel.description)
        assertEquals(1, degreeLevel.numericValue)

    }


	@Test
    void testOptimisticLock() {
        def degreeLevel = newDegreeLevel()
        save degreeLevel

        def sql
        try {
            sql = new Sql(sessionFactory.getCurrentSession().connection())
            sql.executeUpdate("update STVDLEV set STVDLEV_VERSION = 999 where STVDLEV_SURROGATE_ID = ?", [degreeLevel.id])
        } finally {
            sql?.close() // note that the test will close the connection, since it's our current session's connection
        }
        //Try to update the entity
        degreeLevel.code = "UU"
        degreeLevel.description = "UUUUU"
        degreeLevel.numericValue = 2
        degreeLevel.lastModified = new Date()
        degreeLevel.lastModifiedBy = "test"
        degreeLevel.dataOrigin = "Banner"
        shouldFail(HibernateOptimisticLockingFailureException) {
            degreeLevel.save(flush: true, failOnError: true)
        }
    }


	@Test
    void testDeleteDegreeLevel() {
        def degreeLevel = newDegreeLevel()
        save degreeLevel
        def id = degreeLevel.id
        assertNotNull id
        degreeLevel.delete()
        assertNull DegreeLevel.get(id)
    }


	@Test
    void testValidation() {
        def degreeLevel = newDegreeLevel()
        assertTrue "DegreeLevel could not be validated as expected due to ${degreeLevel.errors}", degreeLevel.validate()
    }


	@Test
    void testNullValidationFailure() {
        def degreeLevel = new DegreeLevel()
        assertFalse "DegreeLevel should have failed validation", degreeLevel.validate()
        assertNoErrorsFor(degreeLevel, ['numericValue'])
        assertErrorsFor(degreeLevel, 'nullable', ['code', 'description'])
    }


	@Test
    void testMaxSizeValidationFailures() {
        def degreeLevel = new DegreeLevel(
                code: 'XXXX',
                description: 'XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX')
        assertFalse "DegreeLevel should have failed validation", degreeLevel.validate()
        assertErrorsFor(degreeLevel, 'maxSize', ['code', 'description'])
    }



    private def newDegreeLevel() {
        new DegreeLevel(code: "TT", description: "TTTTT", numericValue: 2, lastModified: new Date(),
                lastModifiedBy: "test", dataOrigin: "Banner")
    }


}
