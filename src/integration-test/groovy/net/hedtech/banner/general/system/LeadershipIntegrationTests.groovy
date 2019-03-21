/** *******************************************************************************
 Copyright 2009-2013 Ellucian Company L.P. and its affiliates.
 ********************************************************************************* */
package net.hedtech.banner.general.system
import org.junit.Before
import org.junit.Test
import org.junit.After

import grails.validation.ValidationException
import groovy.sql.Sql
import java.text.SimpleDateFormat
import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.springframework.orm.hibernate3.HibernateOptimisticLockingFailureException

class LeadershipIntegrationTests extends BaseIntegrationTestCase {

    def i_success_code = "TTTTT"
    def i_success_description = "TTTTT"

    def i_failure_code = "TTTTTT"
    def i_failure_description = "This is invalid description and it should fail the test"

    def u_success_description = "Updated Description"

    def u_failure_description = "This is invalid description and it should fail the test"


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
    void testCreateValidLeadership() {
        def leadership = newValidForCreateLeadership()
        leadership.save(failOnError: true, flush: true)
        //Test if the generated entity now has an id assigned
        assertNotNull leadership.id
    }


    @Test
    void testCreateInvalidLeadership() {
        def leadership = newInvalidForCreateLeadership()
        shouldFail(ValidationException) {
            leadership.save(failOnError: true, flush: true)
        }
    }


    @Test
    void testUpdateValidLeadership() {
        def leadership = newValidForCreateLeadership()
        leadership.save(failOnError: true, flush: true)
        assertNotNull leadership.id
        assertEquals 0L, leadership.version
        assertEquals i_success_code, leadership.code
        assertEquals i_success_description, leadership.description

        //Update the entity
        leadership.description = u_success_description
        leadership.save(failOnError: true, flush: true)
        //Assert for sucessful update
        leadership = Leadership.get(leadership.id)
        assertEquals 1L, leadership?.version
        assertEquals u_success_description, leadership.description
    }


    @Test
    void testUpdateInvalidLeadership() {
        def leadership = newValidForCreateLeadership()
        leadership.save(failOnError: true, flush: true)
        assertNotNull leadership.id
        assertEquals 0L, leadership.version
        assertEquals i_success_code, leadership.code
        assertEquals i_success_description, leadership.description

        //Update the entity with invalid values
        leadership.description = u_failure_description
        shouldFail(ValidationException) {
            leadership.save(failOnError: true, flush: true)
        }
    }


    @Test
    void testDates() {
        def hour = new SimpleDateFormat('HH')
        def date = new SimpleDateFormat('yyyy-M-d')
        def today = new Date()

        def leadership = newValidForCreateLeadership()

        leadership.save(flush: true, failOnError: true)
        leadership.refresh()
        assertNotNull "Leadership should have been saved", leadership.id

        // test date values -
        assertEquals date.format(today), date.format(leadership.lastModified)
        assertEquals hour.format(today), hour.format(leadership.lastModified)
    }


    @Test
    void testOptimisticLock() {
        def leadership = newValidForCreateLeadership()
        leadership.save(failOnError: true, flush: true)

        def sql
        try {
            sql = new Sql(sessionFactory.getCurrentSession().connection())
            sql.executeUpdate("update STVLEAD set STVLEAD_VERSION = 999 where STVLEAD_SURROGATE_ID = ?", [leadership.id])
        } finally {
            sql?.close() // note that the test will close the connection, since it's our current session's connection
        }
        //Try to update the entity
        //Update the entity
        leadership.description = u_success_description
        shouldFail(HibernateOptimisticLockingFailureException) {
            leadership.save(failOnError: true, flush: true)
        }
    }


    @Test
    void testDeleteLeadership() {
        def leadership = newValidForCreateLeadership()
        leadership.save(failOnError: true, flush: true)
        def id = leadership.id
        assertNotNull id
        leadership.delete()
        assertNull Leadership.get(id)
    }


    @Test
    void testValidation() {
        def leadership = newInvalidForCreateLeadership()
        assertFalse "Leadership could not be validated as expected due to ${leadership.errors}", leadership.validate()
    }


    @Test
    void testNullValidationFailure() {
        def leadership = new Leadership()
        assertFalse "Leadership should have failed validation", leadership.validate()
        assertErrorsFor leadership, 'nullable',
                [
                        'code',
                        'description'
                ]
    }


    private def newValidForCreateLeadership() {
        def leadership = new Leadership(
                code: i_success_code,
                description: i_success_description,
        )
        return leadership
    }


    private def newInvalidForCreateLeadership() {
        def leadership = new Leadership(
                code: i_failure_code,
                description: i_failure_description,
        )
        return leadership
    }

}
