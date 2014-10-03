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

class MaritalStatusIntegrationTests extends BaseIntegrationTestCase {

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
    void testCreateMaritalStatus() {
        def maritalStatus = newMaritalStatus()
        maritalStatus.save(failOnError: true, flush: true)
        //Test if the generated entity now has an id assigned
        assertNotNull maritalStatus.id
        assertEquals 0L, maritalStatus.version
        assertEquals "T", maritalStatus.code
        assertEquals "TTTTTTTTTT", maritalStatus.description
        assertEquals "T", maritalStatus.financeConversion
        assertEquals "T", maritalStatus.electronicDataInterchangeEquivalent
    }


	@Test
    void testUpdateMaritalStatus() {
        def maritalStatus = newMaritalStatus()
        maritalStatus.save(failOnError: true, flush: true)
        assertNotNull maritalStatus.id
        assertEquals "T", maritalStatus.code
        assertEquals "TTTTTTTTTT", maritalStatus.description
        assertEquals "T", maritalStatus.financeConversion
        assertEquals "T", maritalStatus.electronicDataInterchangeEquivalent

        //Update the entity
        maritalStatus.description = "UUUUUUUUUU"
        maritalStatus.financeConversion = "U"
        maritalStatus.electronicDataInterchangeEquivalent = "U"
        maritalStatus.save(failOnError: true, flush: true)
        //Assert for successful update
        maritalStatus = MaritalStatus.get(maritalStatus.id)
        assertEquals 1L, maritalStatus?.version
        assertEquals "UUUUUUUUUU", maritalStatus.description
        assertEquals "U", maritalStatus.financeConversion
        assertEquals "U", maritalStatus.electronicDataInterchangeEquivalent
    }


	@Test
    void testDates() {
        def hour = new SimpleDateFormat('HH')
        def date = new SimpleDateFormat('yyyy-M-d')
        def today = new Date()

        def maritalStatus = newMaritalStatus()

        maritalStatus.save(flush: true, failOnError: true)
        maritalStatus.refresh()
        assertNotNull "MaritalStatus should have been saved", maritalStatus.id

        // test date values -
        assertEquals date.format(today), date.format(maritalStatus.lastModified)
        assertEquals hour.format(today), hour.format(maritalStatus.lastModified)
    }


	@Test
    void testOptimisticLock() {
        def maritalStatus = newMaritalStatus()
        maritalStatus.save(failOnError: true, flush: true)

        def sql
        try {
            sql = new Sql(sessionFactory.getCurrentSession().connection())
            sql.executeUpdate("update STVMRTL set STVMRTL_VERSION = 999 where STVMRTL_SURROGATE_ID = ?", [maritalStatus.id])
        } finally {
            sql?.close() // note that the test will close the connection, since it's our current session's connection
        }
        //Try to update the entity
        //Update the entity
        maritalStatus.description = "UUUUUUUUUU"
        shouldFail(HibernateOptimisticLockingFailureException) {
            maritalStatus.save(failOnError: true, flush: true)
        }
    }


	@Test
    void testDeleteMaritalStatus() {
        def maritalStatus = newMaritalStatus()
        maritalStatus.save(failOnError: true, flush: true)
        def id = maritalStatus.id
        assertNotNull id
        maritalStatus.delete()
        assertNull MaritalStatus.get(id)
    }


	@Test
    void testValidation() {
        def maritalStatus = new MaritalStatus()
        assertFalse "MaritalStatus could not be validated as expected due to ${maritalStatus.errors}", maritalStatus.validate()
    }


	@Test
    void testNullValidationFailure() {
        def maritalStatus = new MaritalStatus()
        assertFalse "MaritalStatus should have failed validation", maritalStatus.validate()
        assertErrorsFor maritalStatus, 'nullable', ['code', 'financeConversion']
        assertNoErrorsFor maritalStatus, ['description', 'electronicDataInterchangeEquivalent']
    }


	@Test
    void testMaxSizeValidationFailures() {
        def maritalStatus = new MaritalStatus(
                code: "TTTTT",
                description: 'TTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTT',
                financeConversion: "TTT",
                electronicDataInterchangeEquivalent: 'TTTTTT')

        assertFalse "MaritalStatus should have failed validation", maritalStatus.validate()
        assertErrorsFor maritalStatus, 'maxSize', ['code', 'description', 'financeConversion', 'electronicDataInterchangeEquivalent']
    }


    private def newMaritalStatus() {
        def maritalStatus = new MaritalStatus(
                code: "T",
                description: "TTTTTTTTTT",
                financeConversion: "T",
                electronicDataInterchangeEquivalent: "T",
        )
        return maritalStatus
    }

}
