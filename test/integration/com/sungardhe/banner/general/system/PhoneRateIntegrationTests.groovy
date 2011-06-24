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
 Generated: Fri Feb 11 16:39:35 EST 2011 
 */
package com.sungardhe.banner.general.system

import com.sungardhe.banner.testing.BaseIntegrationTestCase
import groovy.sql.Sql
import org.springframework.orm.hibernate3.HibernateOptimisticLockingFailureException

class PhoneRateIntegrationTests extends BaseIntegrationTestCase {

    /*PROTECTED REGION ID(phonerate_domain_integration_test_data) ENABLED START*/
    //Test data for creating new domain instance
    //Valid test data (For success tests)

    def i_success_code = "TEST"
    def i_success_monthlyIndicator = "Y"
    def i_success_dailyIndicator = "Y"
    def i_success_termlyIndicator = "Y"
    def i_success_description = "Test Phone Rate"
    //Invalid test data (For failure tests)

    def i_failure_code = "FAILURE"
    def i_failure_monthlyIndicator = "X"
    def i_failure_dailyIndicator = "X"
    def i_failure_termlyIndicator = "X"
    def i_failure_description = "Failure Phone Rate Description Too Long"

    //Test data for creating updating domain instance
    //Valid test data (For success tests)

    def u_success_code = "TEST"
    def u_success_monthlyIndicator = "N"
    def u_success_dailyIndicator = "N"
    def u_success_termlyIndicator = "N"
    def u_success_description = "Test Rate"
    //Valid test data (For failure tests)

    def u_failure_code = "TEST"
    def u_failure_monthlyIndicator = "X"
    def u_failure_dailyIndicator = "X"
    def u_failure_termlyIndicator = "X"
    def u_failure_description = "Test Failure Description for invalid update"
    /*PROTECTED REGION END*/


    protected void setUp() {
        formContext = ['STVPRCD'] // Since we are not testing a controller, we need to explicitly set this
        super.setUp()
        initializeTestDataForReferences()
    }

    //This method is used to initialize test data for references.
    //A method is required to execute database calls as it requires a active transaction


    void initializeTestDataForReferences() {
        /*PROTECTED REGION ID(phonerate_domain_integration_test_data_initialization) ENABLED START*/
        /*PROTECTED REGION END*/
    }


    protected void tearDown() {
        super.tearDown()
    }


    void testCreateValidPhoneRate() {
        def phoneRate = newValidForCreatePhoneRate()
        phoneRate.save(failOnError: true, flush: true)
        //Test if the generated entity now has an id assigned
        assertNotNull phoneRate.id
    }


    void testCreateInvalidPhoneRate() {
        def phoneRate = newInvalidForCreatePhoneRate()
        shouldFail {
            phoneRate.save(failOnError: true, flush: true)
        }
    }


    void testUpdateValidPhoneRate() {
        def phoneRate = newValidForCreatePhoneRate()
        phoneRate.save(failOnError: true, flush: true)
        assertNotNull phoneRate.id
        assertEquals 0L, phoneRate.version
        assertEquals i_success_code, phoneRate.code
        assertEquals i_success_monthlyIndicator, phoneRate.monthlyIndicator
        assertEquals i_success_dailyIndicator, phoneRate.dailyIndicator
        assertEquals i_success_termlyIndicator, phoneRate.termlyIndicator
        assertEquals i_success_description, phoneRate.description

        //Update the entity
        phoneRate.monthlyIndicator = u_success_monthlyIndicator
        phoneRate.dailyIndicator = u_success_dailyIndicator
        phoneRate.termlyIndicator = u_success_termlyIndicator
        phoneRate.description = u_success_description
        phoneRate.save(failOnError: true, flush: true)
        //Asset for sucessful update
        phoneRate = PhoneRate.get(phoneRate.id)
        assertEquals 1L, phoneRate?.version
        assertEquals u_success_monthlyIndicator, phoneRate.monthlyIndicator
        assertEquals u_success_dailyIndicator, phoneRate.dailyIndicator
        assertEquals u_success_termlyIndicator, phoneRate.termlyIndicator
        assertEquals u_success_description, phoneRate.description
    }


    void testUpdateInvalidPhoneRate() {
        def phoneRate = newValidForCreatePhoneRate()
        phoneRate.save(failOnError: true, flush: true)
        assertNotNull phoneRate.id
        assertEquals 0L, phoneRate.version
        assertEquals i_success_code, phoneRate.code
        assertEquals i_success_monthlyIndicator, phoneRate.monthlyIndicator
        assertEquals i_success_dailyIndicator, phoneRate.dailyIndicator
        assertEquals i_success_termlyIndicator, phoneRate.termlyIndicator
        assertEquals i_success_description, phoneRate.description

        //Update the entity with invalid values
        phoneRate.monthlyIndicator = u_failure_monthlyIndicator
        phoneRate.dailyIndicator = u_failure_dailyIndicator
        phoneRate.termlyIndicator = u_failure_termlyIndicator
        phoneRate.description = u_failure_description
        shouldFail {
            phoneRate.save(failOnError: true, flush: true)
        }
    }


    void testOptimisticLock() {
        def phoneRate = newValidForCreatePhoneRate()
        phoneRate.save(failOnError: true, flush: true)

        def sql
        try {
            sql = new Sql(sessionFactory.getCurrentSession().connection())
            sql.executeUpdate("update STVPRCD set STVPRCD_VERSION = 999 where STVPRCD_SURROGATE_ID = ?", [phoneRate.id])
        } finally {
            sql?.close() // note that the test will close the connection, since it's our current session's connection
        }
        //Try to update the entity
        //Update the entity
        phoneRate.monthlyIndicator = u_success_monthlyIndicator
        phoneRate.dailyIndicator = u_success_dailyIndicator
        phoneRate.termlyIndicator = u_success_termlyIndicator
        phoneRate.description = u_success_description
        shouldFail(HibernateOptimisticLockingFailureException) {
            phoneRate.save(failOnError: true, flush: true)
        }
    }


    void testDeletePhoneRate() {
        def phoneRate = newValidForCreatePhoneRate()
        phoneRate.save(failOnError: true, flush: true)
        def id = phoneRate.id
        assertNotNull id
        phoneRate.delete()
        assertNull PhoneRate.get(id)
    }


    void testValidation() {
        def phoneRate = newValidForCreatePhoneRate()
        assertTrue "PhoneRate could not be validated as expected due to ${phoneRate.errors}", phoneRate.validate()
    }


    void testNullValidationFailure() {
        def phoneRate = new PhoneRate()
        assertFalse "PhoneRate should have failed validation", phoneRate.validate()
        assertErrorsFor phoneRate, 'nullable',
                [
                        'code',
                        'monthlyIndicator',
                        'dailyIndicator',
                        'termlyIndicator',
                        'description'
                ]
    }


    void testValidationMessages() {
        def phoneRate = newInvalidForCreatePhoneRate()
        phoneRate.code = null
        assertFalse phoneRate.validate()
        assertLocalizedError phoneRate, 'nullable', /.*Field.*code.*of class.*PhoneRate.*cannot be null.*/, 'code'
        phoneRate.monthlyIndicator = null
        assertFalse phoneRate.validate()
        assertLocalizedError phoneRate, 'nullable', /.*Field.*monthlyIndicator.*of class.*PhoneRate.*cannot be null.*/, 'monthlyIndicator'
        phoneRate.dailyIndicator = null
        assertFalse phoneRate.validate()
        assertLocalizedError phoneRate, 'nullable', /.*Field.*dailyIndicator.*of class.*PhoneRate.*cannot be null.*/, 'dailyIndicator'
        phoneRate.termlyIndicator = null
        assertFalse phoneRate.validate()
        assertLocalizedError phoneRate, 'nullable', /.*Field.*termlyIndicator.*of class.*PhoneRate.*cannot be null.*/, 'termlyIndicator'
        phoneRate.description = null
        assertFalse phoneRate.validate()
        assertLocalizedError phoneRate, 'nullable', /.*Field.*description.*of class.*PhoneRate.*cannot be null.*/, 'description'
    }


    private def newValidForCreatePhoneRate() {
        def phoneRate = new PhoneRate(
                code: i_success_code,
                monthlyIndicator: i_success_monthlyIndicator,
                dailyIndicator: i_success_dailyIndicator,
                termlyIndicator: i_success_termlyIndicator,
                description: i_success_description,
                lastModified: new Date(),
                lastModifiedBy: "test",
                dataOrigin: "Banner"
        )
        return phoneRate
    }


    private def newInvalidForCreatePhoneRate() {
        def phoneRate = new PhoneRate(
                code: i_failure_code,
                monthlyIndicator: i_failure_monthlyIndicator,
                dailyIndicator: i_failure_dailyIndicator,
                termlyIndicator: i_failure_termlyIndicator,
                description: i_failure_description,
                lastModified: new Date(),
                lastModifiedBy: "test",
                dataOrigin: "Banner"
        )
        return phoneRate
    }

    /**
     * Please put all the custom tests in this protected section to protect the code
     * from being overwritten on re-generation
     */
    /*PROTECTED REGION ID(phonerate_custom_integration_test_methods) ENABLED START*/
    /*PROTECTED REGION END*/
}
