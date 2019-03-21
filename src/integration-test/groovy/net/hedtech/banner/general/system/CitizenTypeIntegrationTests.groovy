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

class CitizenTypeIntegrationTests extends BaseIntegrationTestCase {

    @Before
    public void setUp() {
        formContext = ['GUAGMNU']
        super.setUp()
    }


    @After
    public void tearDown() {
        super.tearDown()
    }


    @Test
    void testCreateCitizenType() {
        def citizenType = newCitizenType()
        citizenType.save(failOnError: true, flush: true)
        //Test if the generated entity now has an id assigned
        assertNotNull citizenType.id
        assertNotNull citizenType.id
        assertEquals 0L, citizenType.version
        assertEquals "TT", citizenType.code
        assertEquals "TTTTTTTTTT", citizenType.description
        assertTrue citizenType.citizenIndicator
        assertEquals "T", citizenType.electronicDataInterchangeEquivalent
    }


    @Test
    void testUpdateCitizenType() {
        def citizenType = newCitizenType()
        citizenType.save(failOnError: true, flush: true)
        assertNotNull citizenType.id
        assertEquals 0L, citizenType.version
        assertEquals "TT", citizenType.code
        assertEquals "TTTTTTTTTT", citizenType.description
        assertTrue citizenType.citizenIndicator
        assertEquals "T", citizenType.electronicDataInterchangeEquivalent

        //Update the entity
        citizenType.description = "UUUUUUUUUU"
        citizenType.citizenIndicator = false
        citizenType.electronicDataInterchangeEquivalent = "U"
        citizenType.save(failOnError: true, flush: true)
        //Assert for successful update
        citizenType = CitizenType.get(citizenType.id)
        assertEquals 1L, citizenType?.version
        assertEquals "UUUUUUUUUU", citizenType.description
        assertFalse citizenType.citizenIndicator
        assertEquals "U", citizenType.electronicDataInterchangeEquivalent
    }


    @Test
    void testDates() {
        def hour = new SimpleDateFormat('HH')
        def date = new SimpleDateFormat('yyyy-M-d')
        def today = new Date()

        def citizenType = newCitizenType()

        citizenType.save(flush: true, failOnError: true)
        citizenType.refresh()
        assertNotNull "CitizenType should have been saved", citizenType.id

        // test date values -
        assertEquals date.format(today), date.format(citizenType.lastModified)
        assertEquals hour.format(today), hour.format(citizenType.lastModified)
    }


    @Test
    void testOptimisticLock() {
        def citizenType = newCitizenType()
        citizenType.save(failOnError: true, flush: true)

        def sql
        try {
            sql = new Sql(sessionFactory.getCurrentSession().connection())
            sql.executeUpdate("update STVCITZ set STVCITZ_VERSION = 999 where STVCITZ_SURROGATE_ID = ?", [citizenType.id])
        } finally {
            sql?.close() // note that the test will close the connection, since it's our current session's connection
        }
        //Try to update the entity
        //Update the entity
        citizenType.description = "UUUUUUUUUU"
        shouldFail(HibernateOptimisticLockingFailureException) {
            citizenType.save(failOnError: true, flush: true)
        }
    }


    @Test
    void testDeleteCitizenType() {
        def citizenType = newCitizenType()
        citizenType.save(failOnError: true, flush: true)
        def id = citizenType.id
        assertNotNull id
        citizenType.delete()
        assertNull CitizenType.get(id)
    }


    @Test
    void testValidation() {
        def citizenType = new CitizenType()
        assertFalse "CitizenType could not be validated as expected due to ${citizenType.errors}", citizenType.validate()
    }


    @Test
    void testNullValidationFailure() {
        def citizenType = new CitizenType()
        assertFalse "CitizenType should have failed validation", citizenType.validate()
        assertErrorsFor citizenType, 'nullable', ['code', 'description', 'citizenIndicator']
        assertNoErrorsFor citizenType, ['electronicDataInterchangeEquivalent']
    }


    @Test
    void testMaxSizeValidationFailures() {
        def citizenType = new CitizenType(
                code: "TTTTT",
                description: 'TTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTT',
                citizenIndicator: true,
                electronicDataInterchangeEquivalent: 'TTTTTT')

        assertFalse "CitizenType should have failed validation", citizenType.validate()
        assertErrorsFor citizenType, 'maxSize', ['code', 'description', 'electronicDataInterchangeEquivalent']
    }


    private def newCitizenType() {
        def citizenType = new CitizenType(
                code: "TT",
                description: "TTTTTTTTTT",
                citizenIndicator: true,
                electronicDataInterchangeEquivalent: "T",
        )
        return citizenType
    }

}
