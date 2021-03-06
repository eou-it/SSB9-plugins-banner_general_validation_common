/*******************************************************************************
 Copyright 2013 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.system
import org.junit.Before
import org.junit.Test
import org.junit.After

import groovy.sql.Sql
import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.springframework.orm.hibernate5.HibernateOptimisticLockingFailureException

import java.text.SimpleDateFormat
import static groovy.test.GroovyAssert.*
import grails.testing.mixin.integration.Integration
import grails.gorm.transactions.Rollback

@Integration
@Rollback
class PortOfEntryIntegrationTests extends BaseIntegrationTestCase {

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
    void testCreateValidPortOfEntry() {
        def portOfEntry = newValidForCreatePortOfEntry()
        portOfEntry.save(failOnError: true, flush: true)
        //Test if the generated entity now has an id assigned
        assertNotNull portOfEntry.id
    }


    @Test
    void testUpdateValidPortOfEntry() {
        def portOfEntry = newValidForCreatePortOfEntry()
        portOfEntry.save(failOnError: true, flush: true)
        assertNotNull portOfEntry.id
        assertEquals 0L, portOfEntry.version
        assertEquals "TTT", portOfEntry.code
        assertEquals "TTTTTTTTTTTTTTTTTTTTTTTTTTTTTT", portOfEntry.description
        assertEquals "TTT", portOfEntry.studentExchangeVisitorInformationSystemEquivalent

        //Update the entity
        portOfEntry.description = "TTTTTTTTTTTTTTTTTTTTTTTTUPDATE"
        portOfEntry.studentExchangeVisitorInformationSystemEquivalent = "UPD"
        portOfEntry.save(failOnError: true, flush: true)
        //Assert for sucessful update
        portOfEntry = PortOfEntry.get(portOfEntry.id)
        assertEquals 1L, portOfEntry?.version
        assertEquals "TTTTTTTTTTTTTTTTTTTTTTTTUPDATE", portOfEntry.description
        assertEquals "UPD", portOfEntry.studentExchangeVisitorInformationSystemEquivalent
    }


    @Test
    void testDates() {
        def hour = new SimpleDateFormat('HH')
        def date = new SimpleDateFormat('yyyy-M-d')
        def today = new Date()

        def portOfEntry = newValidForCreatePortOfEntry()

        portOfEntry.save(flush: true, failOnError: true)
        portOfEntry.refresh()
        assertNotNull "PortOfEntry should have been saved", portOfEntry.id

        // test date values -
        assertEquals date.format(today), date.format(portOfEntry.lastModified)
        assertEquals hour.format(today), hour.format(portOfEntry.lastModified)
    }


    @Test
    void testOptimisticLock() {
        def portOfEntry = newValidForCreatePortOfEntry()
        portOfEntry.save(failOnError: true, flush: true)

        def sql
        try {
            sql = new Sql(sessionFactory.getCurrentSession().connection())
            sql.executeUpdate("update STVPENT set STVPENT_VERSION = 999 where STVPENT_SURROGATE_ID = ?", [portOfEntry.id])
        } finally {
//            sql?.close() // note that the test will close the connection, since it's our current session's connection
        }
        //Try to update the entity
        //Update the entity
        portOfEntry.description = "TTTTTTTTTTTTTTTTTTTTTTTTUPDATE"
        portOfEntry.studentExchangeVisitorInformationSystemEquivalent = "UPD"
        shouldFail(HibernateOptimisticLockingFailureException) {
            portOfEntry.save(failOnError: true, flush: true)
        }
    }


    @Test
    void testDeletePortOfEntry() {
        def portOfEntry = newValidForCreatePortOfEntry()
        portOfEntry.save(failOnError: true, flush: true)
        def id = portOfEntry.id
        assertNotNull id
        portOfEntry.delete()
        assertNull PortOfEntry.get(id)
    }


    @Test
    void testValidation() {
        def portOfEntry = new PortOfEntry()
        assertFalse "PortOfEntry could not be validated as expected due to ${portOfEntry.errors}", portOfEntry.validate()
    }


    @Test
    void testNullValidationFailure() {
        def portOfEntry = new PortOfEntry()
        assertFalse "PortOfEntry should have failed validation", portOfEntry.validate()
        assertErrorsFor portOfEntry, 'nullable',
                [
                        'code'
                ]
        assertNoErrorsFor portOfEntry,
                [
                        'description',
                        'studentExchangeVisitorInformationSystemEquivalent'
                ]
    }


    @Test
    void testMaxSizeValidationFailures() {
        def portOfEntry = new PortOfEntry(
                description: 'XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX',
                studentExchangeVisitorInformationSystemEquivalent: 'XXXXX')
        assertFalse "PortOfEntry should have failed validation", portOfEntry.validate()
        assertErrorsFor portOfEntry, 'maxSize', ['description', 'studentExchangeVisitorInformationSystemEquivalent']
    }


    private def newValidForCreatePortOfEntry() {
        def portOfEntry = new PortOfEntry(
                code: "TTT",
                description: "TTTTTTTTTTTTTTTTTTTTTTTTTTTTTT",
                studentExchangeVisitorInformationSystemEquivalent: "TTT",
        )
        return portOfEntry
    }
}
