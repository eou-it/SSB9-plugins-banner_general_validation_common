/** *****************************************************************************
 Copyright 2009-2013 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.system
import org.junit.Before
import org.junit.Test
import org.junit.After

import grails.validation.ValidationException
import groovy.sql.Sql
import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.springframework.orm.hibernate5.HibernateOptimisticLockingFailureException

import java.text.SimpleDateFormat
import static groovy.test.GroovyAssert.*
import grails.testing.mixin.integration.Integration
import grails.gorm.transactions.Rollback

@Integration
@Rollback
class EntriesForSqlProcesssIntegrationTests extends BaseIntegrationTestCase {

    //Test data for creating new domain instance
    //Valid test data (For success tests)

    def i_success_code = "TTTTT"
    def i_success_description = "TTTTT"
    def i_success_systemRequiredIndicator = true
    def i_success_startDate = new Date()
    def i_success_endDate = new Date()
    //Invalid test data (For failure tests)

    def i_failure_code = "TTTTTTTTTTTTTTTTTTTTTTTTTTTTTTT"
    def i_failure_description = "TTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTT"
    def i_failure_systemRequiredIndicator = true
    def i_failure_startDate = new Date()
    def i_failure_endDate = (new Date()) - 1

    //Test data for creating updating domain instance
    //Valid test data (For success tests)

    def u_success_code = "TTTTT"
    def u_success_description = "TTTTTTTTTT"
    def u_success_systemRequiredIndicator = true
    def u_success_startDate = new Date()
    def u_success_endDate = new Date()
    //Valid test data (For failure tests)

    def u_failure_code = "TTTTTTTTTTTTTTTTTTTTTTTTTTTTTTT"
    def u_failure_description = "TTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTT"
    def u_failure_systemRequiredIndicator = true
    def u_failure_startDate = new Date()
    def u_failure_endDate = (new Date()) - 1



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
    void testCreateValidEntriesForSqlProcesss() {
        def entriesForSqlProcesss = newValidForCreateEntriesForSqlProcesss()
        entriesForSqlProcesss.save(failOnError: true, flush: true)
        //Test if the generated entity now has an id assigned
        assertNotNull entriesForSqlProcesss.id
    }


    @Test
    void testCreateInvalidEntriesForSqlProcesss() {
        def entriesForSqlProcesss = newInvalidForCreateEntriesForSqlProcesss()
        shouldFail(ValidationException) {
            entriesForSqlProcesss.save(failOnError: true, flush: true)
        }
    }


    @Test
    void testUpdateValidEntriesForSqlProcesss() {
        def entriesForSqlProcesss = newValidForCreateEntriesForSqlProcesss()
        entriesForSqlProcesss.save(failOnError: true, flush: true)
        assertNotNull entriesForSqlProcesss.id
        assertEquals 0L, entriesForSqlProcesss.version
        assertEquals i_success_code, entriesForSqlProcesss.code
        assertEquals i_success_description, entriesForSqlProcesss.description
        assertEquals i_success_systemRequiredIndicator, entriesForSqlProcesss.systemRequiredIndicator
        assertEquals i_success_startDate, entriesForSqlProcesss.startDate
        assertEquals i_success_endDate, entriesForSqlProcesss.endDate

        //Update the entity
        entriesForSqlProcesss.description = u_success_description
        entriesForSqlProcesss.systemRequiredIndicator = u_success_systemRequiredIndicator
        entriesForSqlProcesss.startDate = u_success_startDate
        entriesForSqlProcesss.endDate = u_success_endDate
        entriesForSqlProcesss.save(failOnError: true, flush: true)
        //Assert for sucessful update
        entriesForSqlProcesss = EntriesForSqlProcesss.get(entriesForSqlProcesss.id)
        assertEquals 1L, entriesForSqlProcesss?.version
        assertEquals u_success_description, entriesForSqlProcesss.description
        assertEquals u_success_systemRequiredIndicator, entriesForSqlProcesss.systemRequiredIndicator
        assertEquals u_success_startDate, entriesForSqlProcesss.startDate
        assertEquals u_success_endDate, entriesForSqlProcesss.endDate
    }


    @Test
    void testUpdateInvalidEntriesForSqlProcesss() {
        def entriesForSqlProcesss = newValidForCreateEntriesForSqlProcesss()
        entriesForSqlProcesss.save(failOnError: true, flush: true)
        assertNotNull entriesForSqlProcesss.id
        assertEquals 0L, entriesForSqlProcesss.version
        assertEquals i_success_code, entriesForSqlProcesss.code
        assertEquals i_success_description, entriesForSqlProcesss.description
        assertEquals i_success_systemRequiredIndicator, entriesForSqlProcesss.systemRequiredIndicator
        assertEquals i_success_startDate, entriesForSqlProcesss.startDate
        assertEquals i_success_endDate, entriesForSqlProcesss.endDate

        //Update the entity with invalid values
        entriesForSqlProcesss.description = u_failure_description
        entriesForSqlProcesss.systemRequiredIndicator = u_failure_systemRequiredIndicator
        entriesForSqlProcesss.startDate = u_failure_startDate
        entriesForSqlProcesss.endDate = u_failure_endDate
        shouldFail(ValidationException) {
            entriesForSqlProcesss.save(failOnError: true, flush: true)
        }
    }


    @Test
    void testDates() {
        def time = new SimpleDateFormat('HHmmss')
        def hour = new SimpleDateFormat('HH')
        def date = new SimpleDateFormat('yyyy-M-d')
        def today = new Date()

        def entriesForSqlProcesss = newValidForCreateEntriesForSqlProcesss()

        // TODO review the arbitrary use of "Date()" as a date value in the test below and choose better values

        entriesForSqlProcesss.startDate = new Date()
        entriesForSqlProcesss.endDate = new Date()

        entriesForSqlProcesss.save(flush: true, failOnError: true)
        entriesForSqlProcesss.refresh()
        assertNotNull "EntriesForSqlProcesss should have been saved", entriesForSqlProcesss.id

        // test date values -
        assertEquals date.format(today), date.format(entriesForSqlProcesss.lastModified)
        assertEquals hour.format(today), hour.format(entriesForSqlProcesss.lastModified)

        assertEquals time.format(entriesForSqlProcesss.startDate), "000000"
        assertEquals time.format(entriesForSqlProcesss.endDate), "000000"

    }


    @Test
    void testOptimisticLock() {
        def entriesForSqlProcesss = newValidForCreateEntriesForSqlProcesss()
        entriesForSqlProcesss.save(failOnError: true, flush: true)

        def sql
        try {
            sql = new Sql(sessionFactory.getCurrentSession().connection())
            sql.executeUpdate("update GTVSQPR set GTVSQPR_VERSION = 999 where GTVSQPR_SURROGATE_ID = ?", [entriesForSqlProcesss.id])
        } finally {
//            sql?.close() // note that the test will close the connection, since it's our current session's connection
        }
        //Try to update the entity
        //Update the entity
        entriesForSqlProcesss.description = u_success_description
        entriesForSqlProcesss.systemRequiredIndicator = u_success_systemRequiredIndicator
        entriesForSqlProcesss.startDate = u_success_startDate
        entriesForSqlProcesss.endDate = u_success_endDate
        shouldFail(HibernateOptimisticLockingFailureException) {
            entriesForSqlProcesss.save(failOnError: true, flush: true)
        }
    }


    @Test
    void testDeleteEntriesForSqlProcesss() {
        def entriesForSqlProcesss = newValidForCreateEntriesForSqlProcesss()
        entriesForSqlProcesss.save(failOnError: true, flush: true)
        def id = entriesForSqlProcesss.id
        assertNotNull id
        entriesForSqlProcesss.delete()
        assertNull EntriesForSqlProcesss.get(id)
    }


    @Test
    void testValidation() {
        def entriesForSqlProcesss = newInvalidForCreateEntriesForSqlProcesss()
        assertFalse "EntriesForSqlProcesss could not be validated as expected due to ${entriesForSqlProcesss.errors}", entriesForSqlProcesss.validate()
    }


    @Test
    void testNullValidationFailure() {
        def entriesForSqlProcesss = new EntriesForSqlProcesss()
        assertFalse "EntriesForSqlProcesss should have failed validation", entriesForSqlProcesss.validate()
        assertErrorsFor entriesForSqlProcesss, 'nullable',
                [
                        'code',
                        'description',
                        'systemRequiredIndicator',
                        'startDate'
                ]
        assertNoErrorsFor entriesForSqlProcesss,
                [
                        'endDate'
                ]
    }


    private def newValidForCreateEntriesForSqlProcesss() {
        def entriesForSqlProcesss = new EntriesForSqlProcesss(
                code: i_success_code,
                description: i_success_description,
                systemRequiredIndicator: i_success_systemRequiredIndicator,
                startDate: i_success_startDate,
                endDate: i_success_endDate,
        )
        return entriesForSqlProcesss
    }


    private def newInvalidForCreateEntriesForSqlProcesss() {
        def entriesForSqlProcesss = new EntriesForSqlProcesss(
                code: i_failure_code,
                description: i_failure_description,
                systemRequiredIndicator: i_failure_systemRequiredIndicator,
                startDate: i_failure_startDate,
                endDate: i_failure_endDate,
        )
        return entriesForSqlProcesss
    }


}
