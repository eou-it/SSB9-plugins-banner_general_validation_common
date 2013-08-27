/** *****************************************************************************
 Copyright 2009-2013 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.system

import grails.validation.ValidationException
import groovy.sql.Sql
import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.springframework.orm.hibernate3.HibernateOptimisticLockingFailureException

import java.text.SimpleDateFormat

class EntriesForSqlIntegrationTests extends BaseIntegrationTestCase {

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
    def i_failure_endDate = new Date() - 1

    //Test data for creating updating domain instance
    //Valid test data (For success tests)

    def u_success_code = "TTTTT"
    def u_success_description = "TTTTTTTTTT"
    def u_success_systemRequiredIndicator = true
    def u_success_startDate = new Date()
    def u_success_endDate = new Date() - 1
    //Valid test data (For failure tests)

    def u_failure_code = "TTTTT"
    def u_failure_description = "TTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTT"
    def u_failure_systemRequiredIndicator = true
    def u_failure_startDate = new Date()
    def u_failure_endDate = new Date() - 1


    protected void setUp() {
        formContext = ['GUAGMNU'] // Since we are not testing a controller, we need to explicitly set this
        super.setUp()
    }


    protected void tearDown() {
        super.tearDown()
    }


    void testCreateValidEntriesForSql() {
        def entriesForSql = newValidForCreateEntriesForSql()
        entriesForSql.save(failOnError: true, flush: true)
        //Test if the generated entity now has an id assigned
        assertNotNull entriesForSql.id
    }


    void testCreateInvalidEntriesForSql() {
        def entriesForSql = newInvalidForCreateEntriesForSql()
        shouldFail(ValidationException) {
            entriesForSql.save(failOnError: true, flush: true)
        }
    }


    void testUpdateValidEntriesForSql() {
        def entriesForSql = newValidForCreateEntriesForSql()
        entriesForSql.save(failOnError: true, flush: true)
        assertNotNull entriesForSql.id
        assertEquals 0L, entriesForSql.version
        assertEquals i_success_code, entriesForSql.code
        assertEquals i_success_description, entriesForSql.description
        assertEquals i_success_systemRequiredIndicator, entriesForSql.systemRequiredIndicator
        assertEquals i_success_startDate, entriesForSql.startDate
        assertEquals i_success_endDate, entriesForSql.endDate

        //Update the entity
        entriesForSql.description = u_success_description
        entriesForSql.systemRequiredIndicator = u_success_systemRequiredIndicator
        entriesForSql.startDate = u_success_startDate
        entriesForSql.endDate = u_success_endDate
        entriesForSql.save(failOnError: true, flush: true)
        //Assert for sucessful update
        entriesForSql = EntriesForSql.get(entriesForSql.id)
        assertEquals 1L, entriesForSql?.version
        assertEquals u_success_description, entriesForSql.description
        assertEquals u_success_systemRequiredIndicator, entriesForSql.systemRequiredIndicator
        assertEquals u_success_startDate, entriesForSql.startDate
        assertEquals u_success_endDate, entriesForSql.endDate
    }


    void testUpdateInvalidEntriesForSql() {
        def entriesForSql = newValidForCreateEntriesForSql()
        entriesForSql.save(failOnError: true, flush: true)
        assertNotNull entriesForSql.id
        assertEquals 0L, entriesForSql.version
        assertEquals i_success_code, entriesForSql.code
        assertEquals i_success_description, entriesForSql.description
        assertEquals i_success_systemRequiredIndicator, entriesForSql.systemRequiredIndicator
        assertEquals i_success_startDate, entriesForSql.startDate
        assertEquals i_success_endDate, entriesForSql.endDate

        //Update the entity with invalid values
        entriesForSql.description = u_failure_description
        entriesForSql.systemRequiredIndicator = u_failure_systemRequiredIndicator
        entriesForSql.startDate = u_failure_startDate
        entriesForSql.endDate = u_failure_endDate
        shouldFail(ValidationException) {
            entriesForSql.save(failOnError: true, flush: true)
        }
    }


    void testDates() {
        def time = new SimpleDateFormat('HHmmss')
        def hour = new SimpleDateFormat('HH')
        def date = new SimpleDateFormat('yyyy-M-d')
        def today = new Date()

        def entriesForSql = newValidForCreateEntriesForSql()

        // TODO review the arbitrary use of "Date()" as a date value in the test below and choose better values

        entriesForSql.startDate = new Date()
        entriesForSql.endDate = new Date()

        entriesForSql.save(flush: true, failOnError: true)
        entriesForSql.refresh()
        assertNotNull "EntriesForSql should have been saved", entriesForSql.id

        // test date values -
        assertEquals date.format(today), date.format(entriesForSql.lastModified)
        assertEquals hour.format(today), hour.format(entriesForSql.lastModified)

        assertEquals time.format(entriesForSql.startDate), "000000"
        assertEquals time.format(entriesForSql.endDate), "000000"

    }


    void testOptimisticLock() {
        def entriesForSql = newValidForCreateEntriesForSql()
        entriesForSql.save(failOnError: true, flush: true)

        def sql
        try {
            sql = new Sql(sessionFactory.getCurrentSession().connection())
            sql.executeUpdate("update GTVSQRU set GTVSQRU_VERSION = 999 where GTVSQRU_SURROGATE_ID = ?", [entriesForSql.id])
        } finally {
            sql?.close() // note that the test will close the connection, since it's our current session's connection
        }
        //Try to update the entity
        //Update the entity
        entriesForSql.description = u_success_description
        entriesForSql.systemRequiredIndicator = u_success_systemRequiredIndicator
        entriesForSql.startDate = u_success_startDate
        entriesForSql.endDate = u_success_endDate
        shouldFail(HibernateOptimisticLockingFailureException) {
            entriesForSql.save(failOnError: true, flush: true)
        }
    }


    void testDeleteEntriesForSql() {
        def entriesForSql = newValidForCreateEntriesForSql()
        entriesForSql.save(failOnError: true, flush: true)
        def id = entriesForSql.id
        assertNotNull id
        entriesForSql.delete()
        assertNull EntriesForSql.get(id)
    }


    void testValidation() {
        def entriesForSql = newInvalidForCreateEntriesForSql()
        assertFalse "EntriesForSql could not be validated as expected due to ${entriesForSql.errors}", entriesForSql.validate()
    }


    void testNullValidationFailure() {
        def entriesForSql = new EntriesForSql()
        assertFalse "EntriesForSql should have failed validation", entriesForSql.validate()
        assertErrorsFor entriesForSql, 'nullable',
                [
                        'code',
                        'description',
                        'systemRequiredIndicator',
                        'startDate'
                ]
        assertNoErrorsFor entriesForSql,
                [
                        'endDate'
                ]
    }


    private def newValidForCreateEntriesForSql() {
        def entriesForSql = new EntriesForSql(
                code: i_success_code,
                description: i_success_description,
                systemRequiredIndicator: i_success_systemRequiredIndicator,
                startDate: i_success_startDate,
                endDate: i_success_endDate,
        )
        return entriesForSql
    }


    private def newInvalidForCreateEntriesForSql() {
        def entriesForSql = new EntriesForSql(
                code: i_failure_code,
                description: i_failure_description,
                systemRequiredIndicator: i_failure_systemRequiredIndicator,
                startDate: i_failure_startDate,
                endDate: i_failure_endDate,
        )
        return entriesForSql
    }


}
