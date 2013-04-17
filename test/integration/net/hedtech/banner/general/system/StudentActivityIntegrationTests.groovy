/** *******************************************************************************
 Copyright 2009-2013 Ellucian Company L.P. and its affiliates.
 ********************************************************************************* */
package net.hedtech.banner.general.system

import grails.validation.ValidationException
import groovy.sql.Sql
import java.text.SimpleDateFormat
import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.springframework.orm.hibernate3.HibernateOptimisticLockingFailureException

class StudentActivityIntegrationTests extends BaseIntegrationTestCase {

    def i_success_code = "TTTTT"
    def i_success_description = "TTTTT"

    def i_failure_code = "TTTTTTT"
    def i_failure_description = "This is invalid description and it should fail the test"

    def u_success_description = "Updated Description"

    def u_failure_description = "This is invalid description and it should fail the test"


    protected void setUp() {
        formContext = ['GUAGMNU'] // Since we are not testing a controller, we need to explicitly set this
        super.setUp()
    }


    protected void tearDown() {
        super.tearDown()
    }


    void testCreateValidStudentActivity() {
        def studentActivity = newValidForCreateStudentActivity()
        studentActivity.save(failOnError: true, flush: true)
        //Test if the generated entity now has an id assigned
        assertNotNull studentActivity.id
    }


    void testCreateInvalidStudentActivity() {
        def studentActivity = newInvalidForCreateStudentActivity()
        shouldFail(ValidationException) {
            studentActivity.save(failOnError: true, flush: true)
        }
    }


    void testUpdateValidStudentActivity() {
        def studentActivity = newValidForCreateStudentActivity()
        studentActivity.save(failOnError: true, flush: true)
        def initialVersion = studentActivity.version
        assertNotNull studentActivity.id
        assertEquals 0L, studentActivity.version
        assertEquals i_success_code, studentActivity.code
        assertEquals i_success_description, studentActivity.description

        //Update the entity
        studentActivity.description = u_success_description

        def leadership = new Leadership(
                code: "L1",
                description: "Leadership to update",
        )
        leadership.save(failOnError: true, flush: true)
        assertNotNull leadership.id

        def activityCategory = new ActivityCategory(
                code: "AC1",
                description: "Activity Category to update",
        )
        activityCategory.save(failOnError: true, flush: true)
        assertNotNull activityCategory.id

        def activityType = new ActivityType(
                code: "A1",
                description: "Activity to update"
        )
        activityType.save(failOnError: true, flush: true)
        assertNotNull activityType.id

        studentActivity.activityType = activityType
        studentActivity.activityCategory = activityCategory
        studentActivity.leadership = leadership

        studentActivity.save(failOnError: true, flush: true)
        //Assert for sucessful update
        studentActivity = StudentActivity.get(studentActivity.id)
        assertTrue studentActivity?.version > initialVersion
        assertEquals u_success_description, studentActivity.description


        studentActivity.activityType = activityType
        studentActivity.activityCategory = activityCategory
        studentActivity.leadership = leadership
    }


    void testUpdateInvalidStudentActivity() {
        def studentActivity = newValidForCreateStudentActivity()
        studentActivity.save(failOnError: true, flush: true)
        assertNotNull studentActivity.id
        assertEquals 0L, studentActivity.version
        assertEquals i_success_code, studentActivity.code
        assertEquals i_success_description, studentActivity.description

        //Update the entity with invalid values
        studentActivity.description = u_failure_description

        shouldFail(ValidationException) {
            studentActivity.save(failOnError: true, flush: true)
        }
    }


    void testDates() {
        def time = new SimpleDateFormat('HHmmss')
        def hour = new SimpleDateFormat('HH')
        def date = new SimpleDateFormat('yyyy-M-d')
        def today = new Date()

        def studentActivity = newValidForCreateStudentActivity()

        studentActivity.save(flush: true, failOnError: true)
        studentActivity.refresh()
        assertNotNull "StudentActivity should have been saved", studentActivity.id

        // test date values -
        assertEquals date.format(today), date.format(studentActivity.lastModified)
        assertEquals hour.format(today), hour.format(studentActivity.lastModified)
    }


    void testOptimisticLock() {
        def studentActivity = newValidForCreateStudentActivity()
        studentActivity.save(failOnError: true, flush: true)

        def sql
        try {
            sql = new Sql(sessionFactory.getCurrentSession().connection())
            sql.executeUpdate("update STVACTC set STVACTC_VERSION = 999 where STVACTC_SURROGATE_ID = ?", [studentActivity.id])
        } finally {
            sql?.close() // note that the test will close the connection, since it's our current session's connection
        }
        //Try to update the entity
        //Update the entity
        studentActivity.description = u_success_description
        shouldFail(HibernateOptimisticLockingFailureException) {
            studentActivity.save(failOnError: true, flush: true)
        }
    }


    void testDeleteStudentActivity() {
        def studentActivity = newValidForCreateStudentActivity()
        studentActivity.save(failOnError: true, flush: true)
        def id = studentActivity.id
        assertNotNull id
        studentActivity.delete()
        assertNull StudentActivity.get(id)
    }


    void testValidation() {
        def studentActivity = newInvalidForCreateStudentActivity()
        assertFalse "StudentActivity could not be validated as expected due to ${studentActivity.errors}", studentActivity.validate()
    }


    void testNullValidationFailure() {
        def studentActivity = new StudentActivity()
        assertFalse "StudentActivity should have failed validation", studentActivity.validate()
        assertErrorsFor studentActivity, 'nullable',
                [
                        'code'
                ]
        assertNoErrorsFor studentActivity,
                [
                        'description',
                        'activityType',
                        'activityCategory',
                        'leadership'
                ]
    }


    void testMaxSizeValidationFailures() {
        def studentActivity = new StudentActivity(
                description: 'XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX')
        assertFalse "StudentActivity should have failed validation", studentActivity.validate()
        assertErrorsFor studentActivity, 'maxSize', ['description']
    }


    private def newValidForCreateStudentActivity() {
        def leadership = new Leadership(
                code: "LEAD",
                description: "Leadership Test",
        )
        leadership.save(failOnError: true, flush: true)
        assertNotNull leadership.id

        def activityType = new ActivityType(
                code: "ACTT",
                description: "Activity Type Test"
        )
        activityType.save(failOnError: true, flush: true)
        assertNotNull activityType.id

        def activityCategory = new ActivityCategory(
                code: "ACTC",
                description: "Activity Category Test",
        )
        activityCategory.save(failOnError: true, flush: true)
        assertNotNull activityCategory.id

        def studentActivity = new StudentActivity(
                code: i_success_code,
                description: i_success_description,
                activityType: activityType,
                activityCategory: activityCategory,
                leadership: leadership,
        )
        return studentActivity
    }


    private def newInvalidForCreateStudentActivity() {
        def studentActivity = new StudentActivity(
                code: i_failure_code,
                description: i_failure_description
        )
        return studentActivity
    }
}
