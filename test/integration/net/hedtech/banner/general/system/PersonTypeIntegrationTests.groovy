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

class PersonTypeIntegrationTests extends BaseIntegrationTestCase {

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
    void testCreatePersonType() {
        def personType = newPersonType()
        save personType
        //Test if the generated entity now has an id assigned
        assertNotNull personType.id
    }


	@Test
    void testUpdatePersonType() {
        def personType = newPersonType()
        save personType

        assertNotNull personType.id
        groovy.util.GroovyTestCase.assertEquals(0L, personType.version)
        assertEquals("TT", personType.code)
        assertEquals("TTTTT", personType.description)

        //Update the entity
        personType.code = "UU"
        personType.description = "UUUUU"
        personType.lastModified = new Date()
        personType.lastModifiedBy = "test"
        personType.dataOrigin = "Banner"

        save personType

        personType = PersonType.get(personType.id)
        groovy.util.GroovyTestCase.assertEquals new Long(1), personType?.version
        assertEquals("UU", personType.code)
        assertEquals("UUUUU", personType.description)

    }


	@Test
    void testDates() {
        def hour = new SimpleDateFormat('HH')
        def date = new SimpleDateFormat('yyyy-M-d')
        def today = new Date()

        def personType = newPersonType()

        personType.save(flush: true, failOnError: true)
        personType.refresh()
        assertNotNull "PersonType should have been saved", personType.id

        // test date values -
        assertEquals date.format(today), date.format(personType.lastModified)
        assertEquals hour.format(today), hour.format(personType.lastModified)
    }


	@Test
    void testOptimisticLock() {
        def personType = newPersonType()
        save personType

        def sql
        try {
            sql = new Sql(sessionFactory.getCurrentSession().connection())
            sql.executeUpdate("update STVPTYP set STVPTYP_VERSION = 999 where STVPTYP_SURROGATE_ID = ?", [personType.id])
        } finally {
            sql?.close() // note that the test will close the connection, since it's our current session's connection
        }
        //Try to update the entity
        personType.code = "UU"
        personType.description = "UUUUU"
        personType.lastModified = new Date()
        personType.lastModifiedBy = "test"
        personType.dataOrigin = "Banner"
        shouldFail(HibernateOptimisticLockingFailureException) {
            personType.save(flush: true, failOnError: true)
        }
    }


	@Test
    void testDeletePersonType() {
        def personType = newPersonType()
        save personType
        def id = personType.id
        assertNotNull id
        personType.delete()
        assertNull PersonType.get(id)
    }


	@Test
    void testValidation() {
        def personType = new PersonType()
        assertFalse "PersonType could not be validated as expected due to ${personType.errors}", personType.validate()
    }


	@Test
    void testNullValidationFailure() {
        def personType = new PersonType()
        assertFalse "PersonType should have failed validation", personType.validate()
        assertErrorsFor(personType, 'nullable', ['code'])
    }


	@Test
    void testMaxSizeValidationFailures() {
        def personType = new PersonType(
                code: 'XXXXX',
                description: 'XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX')
        assertFalse "PersonType should have failed validation", personType.validate()
        assertErrorsFor(personType, 'maxSize', ['code', 'description'])
    }


    private def newPersonType() {
        new PersonType(code: "TT", description: "TTTTT", lastModified: new Date(),
                lastModifiedBy: "test", dataOrigin: "Banner")
    }

}
