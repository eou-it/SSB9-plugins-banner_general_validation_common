/** *****************************************************************************
 Copyright 2009-2016 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.system
import org.junit.Before
import org.junit.Test
import org.junit.After

import groovy.sql.Sql
import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.springframework.orm.hibernate5.HibernateOptimisticLockingFailureException
import static groovy.test.GroovyAssert.*
import grails.testing.mixin.integration.Integration
import grails.gorm.transactions.Rollback

@Integration
@Rollback
class DayOfWeekIntegrationTests extends BaseIntegrationTestCase {

    //Test data for creating new domain instance
    //Valid test data (For success tests)

    def i_success_code = "Z"
    def i_success_description = "Newday"
    def i_success_number = "8"
    def i_success_systemRequiredIndicator = "Y"
    //Invalid test data (For failure tests)

    def i_failure_code = "M"
    def i_failure_description = "Already Exist"
    def i_failure_number = "10"
    def i_failure_systemRequiredIndicator = "Y"

    //Test data for creating updating domain instance
    //Valid test data (For success tests)

    def u_success_code = "Z"
    def u_success_description = "NewerDay"
    def u_success_number = "9"
    def u_success_systemRequiredIndicator = ""
    //Valid test data (For failure tests)

    def u_failure_code = "#"
    def u_failure_description = "This is too long of a day description"
    def u_failure_number = "99"
    def u_failure_systemRequiredIndicator = "X"



    @Before
    public void setUp() {
        formContext = ['GUAGMNU', 'SSAEXCL', 'SSASECT', 'STVDAYS'] // Since we are not testing a controller, we need to explicitly set this
        super.setUp()
    }


    @After
    public void tearDown() {
        super.tearDown()
    }


    @Test
    void testCreateValidDayOfWeek() {
        def dayOfWeek = newValidForCreateDayOfWeek()
        dayOfWeek.save(failOnError: true, flush: true)
        //Test if the generated entity now has an id assigned
        assertNotNull dayOfWeek.id
    }


    @Test
    void testCreateInvalidDayOfWeek() {
        def dayOfWeek = newInvalidForCreateDayOfWeek()
        shouldFail() {
            dayOfWeek.save(failOnError: true, flush: true)
        }
    }


    @Test
    void testUpdateValidDayOfWeek() {
        def dayOfWeek = newValidForCreateDayOfWeek()
        dayOfWeek.save(failOnError: true, flush: true)
        assertNotNull dayOfWeek.id
        assertEquals 0L, dayOfWeek.version
        assertEquals i_success_code, dayOfWeek.code
        assertEquals i_success_description, dayOfWeek.description
        assertEquals i_success_number, dayOfWeek.number
        assertEquals i_success_systemRequiredIndicator, dayOfWeek.systemRequiredIndicator

        //Update the entity
        dayOfWeek.description = u_success_description
        dayOfWeek.number = u_success_number
        dayOfWeek.systemRequiredIndicator = u_success_systemRequiredIndicator
        dayOfWeek.save(failOnError: true, flush: true)
        //Asset for successful update
        dayOfWeek = DayOfWeek.get(dayOfWeek.id)
        assertEquals 1L, dayOfWeek?.version
        assertEquals u_success_description, dayOfWeek.description
        assertEquals u_success_number, dayOfWeek.number
        assertEquals u_success_systemRequiredIndicator, dayOfWeek.systemRequiredIndicator
    }


    @Test
    void testUpdateInvalidDayOfWeek() {
        def dayOfWeek = newValidForCreateDayOfWeek()
        dayOfWeek.save(failOnError: true, flush: true)
        assertNotNull dayOfWeek.id
        assertEquals 0L, dayOfWeek.version
        assertEquals i_success_code, dayOfWeek.code
        assertEquals i_success_description, dayOfWeek.description
        assertEquals i_success_number, dayOfWeek.number
        assertEquals i_success_systemRequiredIndicator, dayOfWeek.systemRequiredIndicator

        //Update the entity with invalid values
        dayOfWeek.description = u_failure_description
        dayOfWeek.number = u_failure_number
        dayOfWeek.systemRequiredIndicator = u_failure_systemRequiredIndicator
        shouldFail() {
            dayOfWeek.save(failOnError: true, flush: true)
        }
    }


    @Test
    void testOptimisticLock() {
        def dayOfWeek = newValidForCreateDayOfWeek()
        dayOfWeek.save(failOnError: true, flush: true)

        def sql
        try {
            sql = new Sql(sessionFactory.getCurrentSession().connection())
            sql.executeUpdate("update STVDAYS set STVDAYS_VERSION = 999 where STVDAYS_SURROGATE_ID = ?", [dayOfWeek.id])
        } finally {
//            sql?.close() // note that the test will close the connection, since it's our current session's connection
        }
        //Try to update the entity
        //Update the entity
        dayOfWeek.description = u_success_description
        dayOfWeek.number = u_success_number
        dayOfWeek.systemRequiredIndicator = u_success_systemRequiredIndicator
        shouldFail(HibernateOptimisticLockingFailureException) {
            dayOfWeek.save(failOnError: true, flush: true)
        }
    }


    @Test
    void testDeleteDayOfWeek() {
        def dayOfWeek = newValidForCreateDayOfWeek()
        dayOfWeek.save(failOnError: true, flush: true)
        def id = dayOfWeek.id
        assertNotNull id
        dayOfWeek.delete()
        assertNull DayOfWeek.get(id)
    }


    @Test
    void testValidation() {
        def dayOfWeek = newValidForCreateDayOfWeek()
        assertTrue "DayOfWeek could not be validated as expected due to ${dayOfWeek.errors}", dayOfWeek.validate()
    }


    @Test
    void testNullValidationFailure() {
        def dayOfWeek = new DayOfWeek()
        assertFalse "DayOfWeek should have failed validation", dayOfWeek.validate()
        assertErrorsFor dayOfWeek, 'nullable',
                [
                        'code',
                        'number'
                ]
        assertNoErrorsFor dayOfWeek,
                [
                        'description',
                        'systemRequiredIndicator'
                ]
    }


    @Test
    void testMaxSizeValidationFailures() {
        def dayOfWeek = new DayOfWeek(
                description: 'XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX',
                systemRequiredIndicator: 'XXX')
        assertFalse "DayOfWeek should have failed validation", dayOfWeek.validate()
        assertErrorsFor dayOfWeek, 'maxSize', ['description', 'systemRequiredIndicator']
    }



    private def newValidForCreateDayOfWeek() {
        def dayOfWeek = new DayOfWeek(
                code: i_success_code,
                description: i_success_description,
                number: i_success_number,
                systemRequiredIndicator: i_success_systemRequiredIndicator,
                lastModified: new Date(),
                lastModifiedBy: "test",
                dataOrigin: "Banner"
        )
        return dayOfWeek
    }


    private def newInvalidForCreateDayOfWeek() {
        def dayOfWeek = new DayOfWeek(
                code: i_failure_code,
                description: i_failure_description,
                number: i_failure_number,
                systemRequiredIndicator: i_failure_systemRequiredIndicator,
                lastModified: new Date(),
                lastModifiedBy: "test",
                dataOrigin: "Banner"
        )
        return dayOfWeek
    }


}

