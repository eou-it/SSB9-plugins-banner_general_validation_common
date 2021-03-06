/** *****************************************************************************
 Copyright 2013 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.system
import org.junit.Before
import org.junit.Test
import org.junit.After

import grails.validation.ValidationException
import groovy.sql.Sql
import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.springframework.orm.hibernate5.HibernateOptimisticLockingFailureException

import java.text.SimpleDateFormat
import static groovy.test.GroovyAssert.*
import grails.testing.mixin.integration.Integration
import grails.gorm.transactions.Rollback

@Integration
@Rollback
class CreditCardTypeIntegrationTests extends BaseIntegrationTestCase {

    //Test data for creating new domain instance

    //Valid test data (For success tests)
    def i_success_code = "TTTTT"
    def i_success_description = "TTTTT"
    def i_success_externalMerchantId = "TTTTT"

    //Invalid test data (For failure tests)
    def i_failure_code = "TTTTTTTTTTTTTTT"
    def i_failure_description = null
    def i_failure_externalMerchantId = "TTTTT"

    //Test data for creating updating domain instance

    //Valid test data (For success tests)
    def u_success_code = "TTTTT"
    def u_success_description = "#####"
    def u_success_externalMerchantId = "TTTTT"

    //Valid test data (For failure tests)
    def u_failure_code = "TTTTT"
    def u_failure_description = null
    def u_failure_externalMerchantId = "TTTTT"


    @Before
    public void setUp() {
        formContext = ['GUAGMNU']
        super.setUp()
    }


    @After
    public void tearDown() {
        super.tearDown()
    }


    @Test
    void testCreateValidCreditCardType() {
        def creditCardType = newValidForCreateCreditCardType()
        creditCardType.save(failOnError: true, flush: true)
        //Test if the generated entity now has an id assigned
        assertNotNull creditCardType.id
    }


    @Test
    void testCreateInvalidCreditCardType() {
        def creditCardType = newInvalidForCreateCreditCardType()
        shouldFail(ValidationException) {
            creditCardType.save(failOnError: true, flush: true)
        }
    }


    @Test
    void testUpdateValidCreditCardType() {
        def creditCardType = newValidForCreateCreditCardType()
        creditCardType.save(failOnError: true, flush: true)
        assertNotNull creditCardType.id
        assertEquals 0L, creditCardType.version
        assertEquals i_success_code, creditCardType.code
        assertEquals i_success_description, creditCardType.description
        assertEquals i_success_externalMerchantId, creditCardType.externalMerchantId

        //Update the entity
        creditCardType.description = u_success_description
        creditCardType.externalMerchantId = u_success_externalMerchantId
        creditCardType.save(failOnError: true, flush: true)

        //Assert for successful update
        creditCardType = CreditCardType.get(creditCardType.id)
        assertEquals 1L, creditCardType?.version
        assertEquals u_success_description, creditCardType.description
        assertEquals u_success_externalMerchantId, creditCardType.externalMerchantId
    }


    @Test
    void testUpdateInvalidCreditCardType() {
        def creditCardType = newValidForCreateCreditCardType()
        creditCardType.save(failOnError: true, flush: true)
        assertNotNull creditCardType.id
        assertEquals 0L, creditCardType.version
        assertEquals i_success_code, creditCardType.code
        assertEquals i_success_description, creditCardType.description
        assertEquals i_success_externalMerchantId, creditCardType.externalMerchantId

        //Update the entity with invalid values
        creditCardType.description = u_failure_description
        creditCardType.externalMerchantId = u_failure_externalMerchantId
        shouldFail(ValidationException) {
            creditCardType.save(failOnError: true, flush: true)
        }
    }


    @Test
    void testDates() {
        def hour = new SimpleDateFormat('HH')
        def date = new SimpleDateFormat('yyyy-M-d')
        def today = new Date()

        def creditCardType = newValidForCreateCreditCardType()

        creditCardType.save(flush: true, failOnError: true)
        creditCardType.refresh()
        assertNotNull "CreditCardType should have been saved", creditCardType.id

        // test date values -
        assertEquals date.format(today), date.format(creditCardType.lastModified)
        assertEquals hour.format(today), hour.format(creditCardType.lastModified)
    }


    @Test
    void testOptimisticLock() {
        def creditCardType = newValidForCreateCreditCardType()
        creditCardType.save(failOnError: true, flush: true)

        def sql
        try {
            sql = new Sql(sessionFactory.getCurrentSession().connection())
            sql.executeUpdate("update GTVCCRD set GTVCCRD_VERSION = 999 where GTVCCRD_SURROGATE_ID = ?", [creditCardType.id])
        } finally {
//            sql?.close() // note that the test will close the connection, since it's our current session's connection
        }
        //Try to update the entity
        //Update the entity
        creditCardType.description = u_success_description
        creditCardType.externalMerchantId = u_success_externalMerchantId
        shouldFail(HibernateOptimisticLockingFailureException) {
            creditCardType.save(failOnError: true, flush: true)
        }
    }


    @Test
    void testDeleteCreditCardType() {
        def creditCardType = newValidForCreateCreditCardType()
        creditCardType.save(failOnError: true, flush: true)
        def id = creditCardType.id
        assertNotNull id
        creditCardType.delete()
        assertNull CreditCardType.get(id)
    }


    @Test
    void testValidation() {
        def creditCardType = newInvalidForCreateCreditCardType()
        assertFalse "CreditCardType could not be validated as expected due to ${creditCardType.errors}", creditCardType.validate()
    }


    @Test
    void testNullValidationFailure() {
        def creditCardType = new CreditCardType()
        assertFalse "CreditCardType should have failed validation", creditCardType.validate()
        assertErrorsFor creditCardType, 'nullable',
                [
                        'code',
                        'description'
                ]
        assertNoErrorsFor creditCardType,
                [
                        'externalMerchantId'
                ]
    }


    @Test
    void testMaxSizeValidationFailures() {
        def creditCardType = new CreditCardType(
                description: 'XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX',
                externalMerchantId: 'XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX')
        assertFalse "CreditCardType should have failed validation", creditCardType.validate()
        assertErrorsFor creditCardType, 'maxSize', ['description', 'externalMerchantId']
    }


    private def newValidForCreateCreditCardType() {
        def creditCardType = new CreditCardType(
                code: i_success_code,
                description: i_success_description,
                externalMerchantId: i_success_externalMerchantId,
        )
        return creditCardType
    }


    private def newInvalidForCreateCreditCardType() {
        def creditCardType = new CreditCardType(
                code: i_failure_code,
                description: i_failure_description,
                externalMerchantId: i_failure_externalMerchantId,
        )
        return creditCardType
    }

}
