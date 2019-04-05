/** *****************************************************************************
 Copyright 2009-2013 Ellucian Company L.P. and its affiliates.
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
class PhoneRateIntegrationTests extends BaseIntegrationTestCase {

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
    void testCreateValidPhoneRate() {
        def phoneRate = newValidForCreatePhoneRate()
        phoneRate.save(failOnError: true, flush: true)
        //Test if the generated entity now has an id assigned
        assertNotNull phoneRate.id
    }


    @Test
    void testCreateInvalidPhoneRate() {
        def phoneRate = newInvalidForCreatePhoneRate()
        shouldFail {
            phoneRate.save(failOnError: true, flush: true)
        }
    }


    @Test
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

        //Asset for successful update
        phoneRate = PhoneRate.get(phoneRate.id)
        assertEquals 1L, phoneRate?.version
        assertEquals u_success_monthlyIndicator, phoneRate.monthlyIndicator
        assertEquals u_success_dailyIndicator, phoneRate.dailyIndicator
        assertEquals u_success_termlyIndicator, phoneRate.termlyIndicator
        assertEquals u_success_description, phoneRate.description
    }


    @Test
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


    @Test
    void testOptimisticLock() {
        def phoneRate = newValidForCreatePhoneRate()
        phoneRate.save(failOnError: true, flush: true)

        def sql
        try {
            sql = new Sql(sessionFactory.getCurrentSession().connection())
            sql.executeUpdate("update STVPRCD set STVPRCD_VERSION = 999 where STVPRCD_SURROGATE_ID = ?", [phoneRate.id])
        } finally {
//            sql?.close() // note that the test will close the connection, since it's our current session's connection
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


    @Test
    void testDeletePhoneRate() {
        def phoneRate = newValidForCreatePhoneRate()
        phoneRate.save(failOnError: true, flush: true)
        def id = phoneRate.id
        assertNotNull id
        phoneRate.delete()
        assertNull PhoneRate.get(id)
    }


    @Test
    void testValidation() {
        def phoneRate = newValidForCreatePhoneRate()
        assertTrue "PhoneRate could not be validated as expected due to ${phoneRate.errors}", phoneRate.validate()
    }


    @Test
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


}
