/** *****************************************************************************
 Copyright 2009-2011 SunGard Higher Education. All Rights Reserved.

 This copyrighted software contains confidential and proprietary information of
 SunGard Higher Education and its subsidiaries. Any use of this software is limited
 solely to SunGard Higher Education licensees, and is further subject to the terms
 and conditions of one or more written license agreements between SunGard Higher
 Education and the licensee in question. SunGard is either a registered trademark or
 trademark of SunGard Data Systems in the U.S.A. and/or other regions and/or countries.
 Banner and Luminis are either registered trademarks or trademarks of SunGard Higher
 Education in the U.S.A. and/or other regions and/or countries.
 ****************************************************************************** */
/**
 Banner Automator Version: 1.29
 Generated: Sun May 20 17:49:44 IST 2012
 */
package net.hedtech.banner.general.system

import net.hedtech.banner.testing.BaseIntegrationTestCase
import grails.validation.ValidationException
import groovy.sql.Sql
import java.text.SimpleDateFormat
import org.springframework.orm.hibernate3.HibernateOptimisticLockingFailureException

class EntriesForSqlProcesssIntegrationTests extends BaseIntegrationTestCase {

    /*PROTECTED REGION ID(entriesforsqlprocesss_domain_integration_test_data) ENABLED START*/
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
    /*PROTECTED REGION END*/


    protected void setUp() {
        formContext = ['GUAGMNU'] // Since we are not testing a controller, we need to explicitly set this
        super.setUp()
        initializeTestDataForReferences()
    }

    //This method is used to initialize test data for references.
    //A method is required to execute database calls as it requires a active transaction
    void initializeTestDataForReferences() {
        /*PROTECTED REGION ID(entriesforsqlprocesss_domain_integration_test_data_initialization) ENABLED START*/
        //Valid test data (For success tests)

        //Invalid test data (For failure tests)

        //Valid test data (For success tests)

        //Valid test data (For failure tests)

        //Test data for references for custom tests
        /*PROTECTED REGION END*/
    }


    protected void tearDown() {
        super.tearDown()
    }


    void testCreateValidEntriesForSqlProcesss() {
        def entriesForSqlProcesss = newValidForCreateEntriesForSqlProcesss()
        entriesForSqlProcesss.save(failOnError: true, flush: true)
        //Test if the generated entity now has an id assigned
        assertNotNull entriesForSqlProcesss.id
    }


    void testCreateInvalidEntriesForSqlProcesss() {
        def entriesForSqlProcesss = newInvalidForCreateEntriesForSqlProcesss()
        shouldFail(ValidationException) {
            entriesForSqlProcesss.save(failOnError: true, flush: true)
        }
    }


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


    void testOptimisticLock() {
        def entriesForSqlProcesss = newValidForCreateEntriesForSqlProcesss()
        entriesForSqlProcesss.save(failOnError: true, flush: true)

        def sql
        try {
            sql = new Sql(sessionFactory.getCurrentSession().connection())
            sql.executeUpdate("update GTVSQPR set GTVSQPR_VERSION = 999 where GTVSQPR_SURROGATE_ID = ?", [entriesForSqlProcesss.id])
        } finally {
            sql?.close() // note that the test will close the connection, since it's our current session's connection
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


    void testDeleteEntriesForSqlProcesss() {
        def entriesForSqlProcesss = newValidForCreateEntriesForSqlProcesss()
        entriesForSqlProcesss.save(failOnError: true, flush: true)
        def id = entriesForSqlProcesss.id
        assertNotNull id
        entriesForSqlProcesss.delete()
        assertNull EntriesForSqlProcesss.get(id)
    }


    void testValidation() {
        def entriesForSqlProcesss = newInvalidForCreateEntriesForSqlProcesss()
        assertFalse "EntriesForSqlProcesss could not be validated as expected due to ${entriesForSqlProcesss.errors}", entriesForSqlProcesss.validate()
    }


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

    /**
     * Please put all the custom tests in this protected section to protect the code
     * from being overwritten on re-generation
     */
    /*PROTECTED REGION ID(entriesforsqlprocesss_custom_integration_test_methods) ENABLED START*/
    /*PROTECTED REGION END*/
}
