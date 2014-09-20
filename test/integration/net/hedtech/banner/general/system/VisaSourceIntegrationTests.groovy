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

class VisaSourceIntegrationTests extends BaseIntegrationTestCase {

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
    void testCreateValidVisaSource() {
        def visaSource = newValidForCreateVisaSource()
        visaSource.save(failOnError: true, flush: true)
        //Test if the generated entity now has an id assigned
        assertNotNull visaSource.id
    }


	@Test
    void testUpdateValidVisaSource() {
        def visaSource = newValidForCreateVisaSource()
        visaSource.save(failOnError: true, flush: true)
        assertNotNull visaSource.id
        assertEquals 0L, visaSource.version
        assertEquals "TTTTTT", visaSource.code
        assertEquals "TTTTTTTTTTTTTTTTTTTTTTTTTTTTTT", visaSource.description

        //Update the entity
        visaSource.description = "TTTTTTTTTTTTTTTTTTTTTTTTUPDATE"
        visaSource.save(failOnError: true, flush: true)
        //Assert for sucessful update
        visaSource = VisaSource.get(visaSource.id)
        assertEquals 1L, visaSource?.version
        assertEquals "TTTTTTTTTTTTTTTTTTTTTTTTUPDATE", visaSource.description
    }


	@Test
    void testDates() {
        def hour = new SimpleDateFormat('HH')
        def date = new SimpleDateFormat('yyyy-M-d')
        def today = new Date()

        def visaSource = newValidForCreateVisaSource()

        visaSource.save(flush: true, failOnError: true)
        visaSource.refresh()
        assertNotNull "VisaSource should have been saved", visaSource.id

        // test date values -
        assertEquals date.format(today), date.format(visaSource.lastModified)
        assertEquals hour.format(today), hour.format(visaSource.lastModified)
    }


	@Test
    void testOptimisticLock() {
        def visaSource = newValidForCreateVisaSource()
        visaSource.save(failOnError: true, flush: true)

        def sql
        try {
            sql = new Sql(sessionFactory.getCurrentSession().connection())
            sql.executeUpdate("update GTVSRCE set GTVSRCE_VERSION = 999 where GTVSRCE_SURROGATE_ID = ?", [visaSource.id])
        } finally {
            sql?.close() // note that the test will close the connection, since it's our current session's connection
        }
        //Try to update the entity
        //Update the entity
        visaSource.description = "TTTTTTTTTTTTTTTTTTTTTTTTUPDATE"
        shouldFail(HibernateOptimisticLockingFailureException) {
            visaSource.save(failOnError: true, flush: true)
        }
    }


	@Test
    void testDeleteVisaSource() {
        def visaSource = newValidForCreateVisaSource()
        visaSource.save(failOnError: true, flush: true)
        def id = visaSource.id
        assertNotNull id
        visaSource.delete()
        assertNull VisaSource.get(id)
    }


	@Test
    void testValidation() {
        def visaSource = new VisaSource()
        assertFalse "VisaSource could not be validated as expected due to ${visaSource.errors}", visaSource.validate()
    }


	@Test
    void testNullValidationFailure() {
        def visaSource = new VisaSource()
        assertFalse "VisaSource should have failed validation", visaSource.validate()
        assertErrorsFor visaSource, 'nullable',
                [
                        'code',
                        'description'
                ]
    }


    private def newValidForCreateVisaSource() {
        def visaSource = new VisaSource(
                code: "TTTTTT",
                description: "TTTTTTTTTTTTTTTTTTTTTTTTTTTTTT",
        )
        return visaSource
    }
}
