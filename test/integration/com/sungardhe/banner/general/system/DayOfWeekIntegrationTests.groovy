/** *****************************************************************************
 © 2011 SunGard Higher Education.  All Rights Reserved.

 CONFIDENTIAL BUSINESS INFORMATION

 THIS PROGRAM IS PROPRIETARY INFORMATION OF SUNGARD HIGHER EDUCATION
 AND IS NOT TO BE COPIED, REPRODUCED, LENT, OR DISPOSED OF,
 NOR USED FOR ANY PURPOSE OTHER THAN THAT WHICH IT IS SPECIFICALLY PROVIDED
 WITHOUT THE WRITTEN PERMISSION OF THE SAID COMPANY
 ****************************************************************************** */
/**
 Banner Automator Version: 0.1.1
 Generated: Thu Mar 31 13:48:48 EDT 2011
 */
package com.sungardhe.banner.general.system

import com.sungardhe.banner.testing.BaseIntegrationTestCase
import groovy.sql.Sql
import org.springframework.orm.hibernate3.HibernateOptimisticLockingFailureException

class DayOfWeekIntegrationTests extends BaseIntegrationTestCase {

    /*PROTECTED REGION ID(dayofweek_domain_integration_test_data) ENABLED START*/
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
    /*PROTECTED REGION END*/


    protected void setUp() {
        formContext = ['SSAEXCL', 'SSASECT', 'STVDAYS'] // Since we are not testing a controller, we need to explicitly set this
        super.setUp()
        initializeTestDataForReferences()
    }

    //This method is used to initialize test data for references.
    //A method is required to execute database calls as it requires a active transaction


    void initializeTestDataForReferences() {
        /*PROTECTED REGION ID(dayofweek_domain_integration_test_data_initialization) ENABLED START*/
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


    void testCreateValidDayOfWeek() {
        def dayOfWeek = newValidForCreateDayOfWeek()
        dayOfWeek.save(failOnError: true, flush: true)
        //Test if the generated entity now has an id assigned
        assertNotNull dayOfWeek.id
    }


    void testCreateInvalidDayOfWeek() {
        def dayOfWeek = newInvalidForCreateDayOfWeek()
        shouldFail() {
            dayOfWeek.save(failOnError: true, flush: true)
        }
    }


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


    void testOptimisticLock() {
        def dayOfWeek = newValidForCreateDayOfWeek()
        dayOfWeek.save(failOnError: true, flush: true)

        def sql
        try {
            sql = new Sql(sessionFactory.getCurrentSession().connection())
            sql.executeUpdate("update STVDAYS set STVDAYS_VERSION = 999 where STVDAYS_SURROGATE_ID = ?", [dayOfWeek.id])
        } finally {
            sql?.close() // note that the test will close the connection, since it's our current session's connection
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


    void testDeleteDayOfWeek() {
        def dayOfWeek = newValidForCreateDayOfWeek()
        dayOfWeek.save(failOnError: true, flush: true)
        def id = dayOfWeek.id
        assertNotNull id
        dayOfWeek.delete()
        assertNull DayOfWeek.get(id)
    }


    void testValidation() {
        def dayOfWeek = newValidForCreateDayOfWeek()
        assertTrue "DayOfWeek could not be validated as expected due to ${dayOfWeek.errors}", dayOfWeek.validate()
    }


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


    void testMaxSizeValidationFailures() {
        def dayOfWeek = new DayOfWeek(
                description: 'XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX',
                systemRequiredIndicator: 'XXX')
        assertFalse "DayOfWeek should have failed validation", dayOfWeek.validate()
        assertErrorsFor dayOfWeek, 'maxSize', ['description', 'systemRequiredIndicator']
    }


    void testValidationMessages() {
        def dayOfWeek = newInvalidForCreateDayOfWeek()
        dayOfWeek.code = null
        assertFalse dayOfWeek.validate()
        assertLocalizedError dayOfWeek, 'nullable', /.*Field.*code.*of class.*DayOfWeek.*cannot be null.*/, 'code'
        dayOfWeek.number = null
        assertFalse dayOfWeek.validate()
        assertLocalizedError dayOfWeek, 'nullable', /.*Field.*number.*of class.*DayOfWeek.*cannot be null.*/, 'number'
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

    /**
     * Please put all the custom tests in this protected section to protect the code
     * from being overwritten on re-generation
     */
    /*PROTECTED REGION ID(dayofweek_custom_integration_test_methods) ENABLED START*/
    /*PROTECTED REGION END*/
}

