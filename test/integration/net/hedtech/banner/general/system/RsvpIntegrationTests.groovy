/** *****************************************************************************
 Copyright 2009-2013 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.system

import net.hedtech.banner.testing.BaseIntegrationTestCase
import grails.validation.ValidationException
import groovy.sql.Sql
import org.springframework.orm.hibernate3.HibernateOptimisticLockingFailureException

class RsvpIntegrationTests extends BaseIntegrationTestCase {

    //Test data for creating new domain instance
    //Valid test data (For success tests)

    def i_success_code = "TTTTT"
    def i_success_description = "i_success_description"
    def i_success_planToAttendenceIndicator = true
    //Invalid test data (For failure tests)

    def i_failure_code = "TTTTT"
    def i_failure_description = null
    def i_failure_planToAttendenceIndicator = true

    //Test data for creating updating domain instance
    //Valid test data (For success tests)

    def u_success_code = "TTTTT"
    def u_success_description = "u_success_description"
    def u_success_planToAttendenceIndicator = true
    //Valid test data (For failure tests)

    def u_failure_code = "TTTTT"
    def u_failure_description = null
    def u_failure_planToAttendenceIndicator = true

    protected void setUp() {
        formContext = ['GUAGMNU'] // Since we are not testing a controller, we need to explicitly set this
        super.setUp()
        //initializeTestDataForReferences()
    }

    //This method is used to initialize test data for references.
    //A method is required to execute database calls as it requires a active transaction
    void initializeTestDataForReferences() {
        //Valid test data (For success tests)

        //Invalid test data (For failure tests)

        //Valid test data (For success tests)

        //Valid test data (For failure tests)

        //Test data for references for custom tests
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testCreateValidRsvp() {
        def rsvp = newValidForCreateRsvp()
        rsvp.save(failOnError: true, flush: true)
        //Test if the generated entity now has an id assigned
        assertNotNull rsvp.id
    }

    void testCreateInvalidRsvp() {
        def rsvp = newInvalidForCreateRsvp()
        shouldFail(ValidationException) {
            rsvp.save(failOnError: true, flush: true)
        }
    }

    void testUpdateValidRsvp() {
        def rsvp = newValidForCreateRsvp()
        rsvp.save(failOnError: true, flush: true)
        assertNotNull rsvp.id
        assertEquals 0L, rsvp.version
        assertEquals i_success_code, rsvp.code
        assertEquals i_success_description, rsvp.description
        assertEquals i_success_planToAttendenceIndicator, rsvp.planToAttendenceIndicator

        //Update the entity
        rsvp.description = u_success_description
        rsvp.planToAttendenceIndicator = u_success_planToAttendenceIndicator
        rsvp.save(failOnError: true, flush: true)

        //Assert for successful update
        rsvp = Rsvp.get(rsvp.id)
        assertEquals 1L, rsvp?.version
        assertEquals u_success_description, rsvp.description
        assertEquals u_success_planToAttendenceIndicator, rsvp.planToAttendenceIndicator
    }

    void testUpdateInvalidRsvp() {
        def rsvp = newValidForCreateRsvp()
        rsvp.save(failOnError: true, flush: true)
        assertNotNull rsvp.id
        assertEquals 0L, rsvp.version
        assertEquals i_success_code, rsvp.code
        assertEquals i_success_description, rsvp.description
        assertEquals i_success_planToAttendenceIndicator, rsvp.planToAttendenceIndicator

        //Update the entity with invalid values
        rsvp.description = u_failure_description
        rsvp.planToAttendenceIndicator = u_failure_planToAttendenceIndicator
        shouldFail(ValidationException) {
            rsvp.save(failOnError: true, flush: true)
        }
    }

    void testOptimisticLock() {
        def rsvp = newValidForCreateRsvp()
        rsvp.save(failOnError: true, flush: true)

        def sql
        try {
            sql = new Sql(sessionFactory.getCurrentSession().connection())
            sql.executeUpdate("update GTVRSVP set GTVRSVP_VERSION = 999 where GTVRSVP_SURROGATE_ID = ?", [rsvp.id])
        } finally {
            sql?.close() // note that the test will close the connection, since it's our current session's connection
        }
        //Try to update the entity
        //Update the entity
        rsvp.description = u_success_description
        rsvp.planToAttendenceIndicator = u_success_planToAttendenceIndicator
        shouldFail(HibernateOptimisticLockingFailureException) {
            rsvp.save(failOnError: true, flush: true)
        }
    }

    void testDeleteRsvp() {
        def rsvp = newValidForCreateRsvp()
        rsvp.save(failOnError: true, flush: true)
        def id = rsvp.id
        assertNotNull id
        rsvp.delete()
        assertNull Rsvp.get(id)
    }

    void testValidation() {
        def rsvp = newInvalidForCreateRsvp()
        assertFalse "Rsvp could not be validated as expected due to ${rsvp.errors}", rsvp.validate()
    }

    void testNullValidationFailure() {
        def rsvp = new Rsvp()
        assertFalse "Rsvp should have failed validation", rsvp.validate()
        assertErrorsFor rsvp, 'nullable',
                [
                        'code',
                        'description',
                        'planToAttendenceIndicator'
                ]
    }


    private def newValidForCreateRsvp() {
        def rsvp = new Rsvp(
                code: i_success_code,
                description: i_success_description,
                planToAttendenceIndicator: i_success_planToAttendenceIndicator,
        )
        return rsvp
    }

    private def newInvalidForCreateRsvp() {
        def rsvp = new Rsvp(
                code: i_failure_code,
                description: i_failure_description,
                planToAttendenceIndicator: i_failure_planToAttendenceIndicator,
        )
        return rsvp
    }


}
