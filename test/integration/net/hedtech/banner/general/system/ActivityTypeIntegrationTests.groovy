/** *******************************************************************************
 Copyright 2009-2013 Ellucian Company L.P. and its affiliates.
 ********************************************************************************* */
package net.hedtech.banner.general.system

import grails.validation.ValidationException
import groovy.sql.Sql
import java.text.SimpleDateFormat
import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.springframework.orm.hibernate3.HibernateOptimisticLockingFailureException

class ActivityTypeIntegrationTests extends BaseIntegrationTestCase {

    def i_success_code = "TTTTT"
    def i_success_description = "TTTTT"
    def i_success_systemRequiredIndicator = "Y"

    def i_failure_code = "TTTTTT"
    def i_failure_description = "TTTTT"
    def i_failure_systemRequiredIndicator = "Y"

    def u_success_description = "Updated"
    def u_success_systemRequiredIndicator = null

    def u_failure_description = "This is invalid description and it should fail the update."
    def u_failure_systemRequiredIndicator = "N"


    protected void setUp() {
        formContext = ['GUAGMNU'] // Since we are not testing a controller, we need to explicitly set this
        super.setUp()
    }


    protected void tearDown() {
        super.tearDown()
    }


    void testCreateValidActivityType() {
        def activityType = newValidForCreateActivityType()
        activityType.save(failOnError: true, flush: true)
        //Test if the generated entity now has an id assigned
        assertNotNull activityType.id
    }


    void testCreateInvalidActivityType() {
        def activityType = newInvalidForCreateActivityType()
        shouldFail(ValidationException) {
            activityType.save(failOnError: true, flush: true)
        }
    }


    void testUpdateValidActivityType() {
        def activityType = newValidForCreateActivityType()
        activityType.save(failOnError: true, flush: true)
        assertNotNull activityType.id
        assertEquals 0L, activityType.version
        assertEquals i_success_code, activityType.code
        assertEquals i_success_description, activityType.description
        assertEquals i_success_systemRequiredIndicator, activityType.systemRequiredIndicator

        //Update the entity
        activityType.description = u_success_description
        activityType.systemRequiredIndicator = u_success_systemRequiredIndicator
        activityType.save(failOnError: true, flush: true)
        //Assert for sucessful update
        activityType = ActivityType.get(activityType.id)
        assertEquals 1L, activityType?.version
        assertEquals u_success_description, activityType.description
        assertEquals u_success_systemRequiredIndicator, activityType.systemRequiredIndicator
    }


    void testUpdateInvalidActivityType() {
        def activityType = newValidForCreateActivityType()
        activityType.save(failOnError: true, flush: true)
        assertNotNull activityType.id
        assertEquals 0L, activityType.version
        assertEquals i_success_code, activityType.code
        assertEquals i_success_description, activityType.description
        assertEquals i_success_systemRequiredIndicator, activityType.systemRequiredIndicator

        //Update the entity with invalid values
        activityType.description = u_failure_description
        activityType.systemRequiredIndicator = u_failure_systemRequiredIndicator
        shouldFail(ValidationException) {
            activityType.save(failOnError: true, flush: true)
        }
    }


    void testDates() {
        def hour = new SimpleDateFormat('HH')
        def date = new SimpleDateFormat('yyyy-M-d')
        def today = new Date()

        def activityType = newValidForCreateActivityType()

        activityType.save(flush: true, failOnError: true)
        activityType.refresh()
        assertNotNull "ActivityType should have been saved", activityType.id

        // test date values -
        assertEquals date.format(today), date.format(activityType.lastModified)
        assertEquals hour.format(today), hour.format(activityType.lastModified)
    }


    void testOptimisticLock() {
        def activityType = newValidForCreateActivityType()
        activityType.save(failOnError: true, flush: true)

        def sql
        try {
            sql = new Sql(sessionFactory.getCurrentSession().connection())
            sql.executeUpdate("update STVACTP set STVACTP_VERSION = 999 where STVACTP_SURROGATE_ID = ?", [activityType.id])
        } finally {
            sql?.close() // note that the test will close the connection, since it's our current session's connection
        }
        //Try to update the entity
        //Update the entity
        activityType.description = u_success_description
        activityType.systemRequiredIndicator = u_success_systemRequiredIndicator
        shouldFail(HibernateOptimisticLockingFailureException) {
            activityType.save(failOnError: true, flush: true)
        }
    }


    void testDeleteActivityType() {
        def activityType = newValidForCreateActivityType()
        activityType.save(failOnError: true, flush: true)
        def id = activityType.id
        assertNotNull id
        activityType.delete()
        assertNull ActivityType.get(id)
    }


    void testValidation() {
        def activityType = newInvalidForCreateActivityType()
        assertFalse "ActivityType could not be validated as expected due to ${activityType.errors}", activityType.validate()
    }


    void testNullValidationFailure() {
        def activityType = new ActivityType()
        assertFalse "ActivityType should have failed validation", activityType.validate()
        assertErrorsFor activityType, 'nullable',
                [
                        'code',
                        'description'
                ]
        assertNoErrorsFor activityType,
                [
                        'systemRequiredIndicator'
                ]
    }


    void testMaxSizeValidationFailures() {
        def activityType = new ActivityType(
                systemRequiredIndicator: 'XXX')
        assertFalse "ActivityType should have failed validation", activityType.validate()
        assertErrorsFor activityType, 'maxSize', ['systemRequiredIndicator']
    }


    private def newValidForCreateActivityType() {
        def activityType = new ActivityType(
                code: i_success_code,
                description: i_success_description,
                systemRequiredIndicator: i_success_systemRequiredIndicator,
        )
        return activityType
    }


    private def newInvalidForCreateActivityType() {
        def activityType = new ActivityType(
                code: i_failure_code,
                description: i_failure_description,
                systemRequiredIndicator: i_failure_systemRequiredIndicator,
        )
        return activityType
    }

}
