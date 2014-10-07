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

class ActivityCategoryIntegrationTests extends BaseIntegrationTestCase {

    def i_success_code = "TTTTT"
    def i_success_description = "TTTTT"

    def i_failure_code = "TTTTTTT"
    def i_failure_description = "This is the invalid description and it should fail the test"

    def u_success_description = "Updated Descripiton"

    def u_failure_description = "This is the invalid description and it should fail the test"

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
    void testCreateValidActivityCategory() {
        def activityCategory = newValidForCreateActivityCategory()
        activityCategory.save(failOnError: true, flush: true)
        //Test if the generated entity now has an id assigned
        assertNotNull activityCategory.id
    }


    @Test
    void testCreateInvalidActivityCategory() {
        def activityCategory = newInvalidForCreateActivityCategory()
        shouldFail(ValidationException) {
            activityCategory.save(failOnError: true, flush: true)
        }
    }


    @Test
    void testUpdateValidActivityCategory() {
        def activityCategory = newValidForCreateActivityCategory()
        activityCategory.save(failOnError: true, flush: true)
        assertNotNull activityCategory.id
        assertEquals 0L, activityCategory.version
        assertEquals i_success_code, activityCategory.code
        assertEquals i_success_description, activityCategory.description

        //Update the entity
        activityCategory.description = u_success_description
        activityCategory.save(failOnError: true, flush: true)
        //Assert for sucessful update
        activityCategory = ActivityCategory.get(activityCategory.id)
        assertEquals 1L, activityCategory?.version
        assertEquals u_success_description, activityCategory.description
    }


    @Test
    void testUpdateInvalidActivityCategory() {
        def activityCategory = newValidForCreateActivityCategory()
        activityCategory.save(failOnError: true, flush: true)
        assertNotNull activityCategory.id
        assertEquals 0L, activityCategory.version
        assertEquals i_success_code, activityCategory.code
        assertEquals i_success_description, activityCategory.description

        //Update the entity with invalid values
        activityCategory.description = u_failure_description
        shouldFail(ValidationException) {
            activityCategory.save(failOnError: true, flush: true)
        }
    }


    @Test
    void testDates() {
        def hour = new SimpleDateFormat('HH')
        def date = new SimpleDateFormat('yyyy-M-d')
        def today = new Date()

        def activityCategory = newValidForCreateActivityCategory()

        activityCategory.save(flush: true, failOnError: true)
        activityCategory.refresh()
        assertNotNull "ActivityCategory should have been saved", activityCategory.id

        // test date values -
        assertEquals date.format(today), date.format(activityCategory.lastModified)
        assertEquals hour.format(today), hour.format(activityCategory.lastModified)
    }


    @Test
    void testOptimisticLock() {
        def activityCategory = newValidForCreateActivityCategory()
        activityCategory.save(failOnError: true, flush: true)

        def sql
        try {
            sql = new Sql(sessionFactory.getCurrentSession().connection())
            sql.executeUpdate("update STVACCG set STVACCG_VERSION = 999 where STVACCG_SURROGATE_ID = ?", [activityCategory.id])
        } finally {
            sql?.close() // note that the test will close the connection, since it's our current session's connection
        }
        //Try to update the entity
        //Update the entity
        activityCategory.description = u_success_description
        shouldFail(HibernateOptimisticLockingFailureException) {
            activityCategory.save(failOnError: true, flush: true)
        }
    }


    @Test
    void testDeleteActivityCategory() {
        def activityCategory = newValidForCreateActivityCategory()
        activityCategory.save(failOnError: true, flush: true)
        def id = activityCategory.id
        assertNotNull id
        activityCategory.delete()
        assertNull ActivityCategory.get(id)
    }


    @Test
    void testValidation() {
        def activityCategory = newInvalidForCreateActivityCategory()
        assertFalse "ActivityCategory could not be validated as expected due to ${activityCategory.errors}", activityCategory.validate()
    }


    @Test
    void testNullValidationFailure() {
        def activityCategory = new ActivityCategory()
        assertFalse "ActivityCategory should have failed validation", activityCategory.validate()
        assertErrorsFor activityCategory, 'nullable',
                [
                        'code',
                        'description'
                ]
    }


    private def newValidForCreateActivityCategory() {
        def activityCategory = new ActivityCategory(
                code: i_success_code,
                description: i_success_description,
        )
        return activityCategory
    }


    private def newInvalidForCreateActivityCategory() {
        def activityCategory = new ActivityCategory(
                code: i_failure_code,
                description: i_failure_description,
        )
        return activityCategory
    }

}
