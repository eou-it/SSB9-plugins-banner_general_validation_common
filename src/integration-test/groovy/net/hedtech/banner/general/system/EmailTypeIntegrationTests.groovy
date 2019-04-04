/** *****************************************************************************
 Copyright 2009-2016 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.system
import org.junit.Before
import org.junit.Test
import org.junit.After

import grails.validation.ValidationException
import net.hedtech.banner.testing.BaseIntegrationTestCase
import static groovy.test.GroovyAssert.*
import grails.testing.mixin.integration.Integration
import grails.gorm.transactions.Rollback

@Integration
@Rollback
class EmailTypeIntegrationTests extends BaseIntegrationTestCase {

    //Test data for creating new domain instance
    //Valid test data (For success tests)

    def i_success_code = "TTTT"
    def i_success_description = "TTTTT"
    def i_success_displayWebIndicator = true
    def i_success_urlIndicator = true
    //Invalid test data (For failure tests)

    def i_failure_code = null
    def i_failure_description = null
    def i_failure_displayWebIndicator = null
    def i_failure_urlIndicator = null

    //Test data for creating updating domain instance
    //Valid test data (For success tests)

    def u_success_code = "TTTT"
    def u_success_description = "TTTTT"
    def u_success_displayWebIndicator = true
    def u_success_urlIndicator = true
    //Valid test data (For failure tests)

    def u_failure_code = null
    def u_failure_description = null
    def u_failure_displayWebIndicator = null
    def u_failure_urlIndicator = null


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
    void testCreateValidEmailType() {
        def emailType = newValidForCreateEmailType()
        emailType.save(failOnError: true, flush: true)
        //Test if the generated entity now has an id assigned
        assertNotNull emailType.id
    }


    @Test
    void testCreateInvalidEmailType() {
        def emailType = newInvalidForCreateEmailType()
        shouldFail(ValidationException) {
            emailType.save(failOnError: true, flush: true)
        }
    }


    @Test
    void testUpdateValidEmailType() {
        def emailType = newValidForCreateEmailType()
        emailType.save(failOnError: true, flush: true)
        assertNotNull emailType.id
        assertEquals 0L, emailType.version
        assertEquals i_success_code, emailType.code
        assertEquals i_success_description, emailType.description
        assertEquals i_success_displayWebIndicator, emailType.displayWebIndicator
        assertEquals i_success_urlIndicator, emailType.urlIndicator

        //Update the entity
        emailType.description = u_success_description
        emailType.displayWebIndicator = u_success_displayWebIndicator
        emailType.urlIndicator = u_success_urlIndicator
        emailType.save(failOnError: true, flush: true)
        //Assert for sucessful update
        emailType = EmailType.get(emailType.id)
        // assertEquals 1L, emailType?.version
        assertEquals u_success_description, emailType.description
        assertEquals u_success_displayWebIndicator, emailType.displayWebIndicator
        assertEquals u_success_urlIndicator, emailType.urlIndicator
    }


    @Test
    void testUpdateInvalidEmailType() {
        def emailType = newValidForCreateEmailType()
        emailType.save(failOnError: true, flush: true)
        assertNotNull emailType.id
        assertEquals 0L, emailType.version
        assertEquals i_success_code, emailType.code
        assertEquals i_success_description, emailType.description
        assertEquals i_success_displayWebIndicator, emailType.displayWebIndicator
        assertEquals i_success_urlIndicator, emailType.urlIndicator

        //Update the entity with invalid values
        emailType.description = u_failure_description
        emailType.displayWebIndicator = u_failure_displayWebIndicator
        emailType.urlIndicator = u_failure_urlIndicator
        shouldFail(ValidationException) {
            emailType.save(failOnError: true, flush: true)
        }
    }


    @Test
    void testDeleteEmailType() {
        def emailType = newValidForCreateEmailType()
        emailType.save(failOnError: true, flush: true)
        def id = emailType.id
        assertNotNull id
        emailType.delete()
        assertNull EmailType.get(id)
    }


    @Test
    void testValidation() {
        def emailType = newInvalidForCreateEmailType()
        assertFalse "EmailType could not be validated as expected due to ${emailType.errors}", emailType.validate()
    }


    @Test
    void testNullValidationFailure() {
        def emailType = new EmailType()
        assertFalse "EmailType should have failed validation", emailType.validate()
        assertErrorsFor emailType, 'nullable',
                [
                        'code',
                        'displayWebIndicator',
                        'urlIndicator'
                ]
        assertNoErrorsFor emailType,
                [
                        'description'
                ]
    }


    @Test
    void testMaxSizeValidationFailures() {
        def emailType = new EmailType(
                description: 'XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX')
        assertFalse "EmailType should have failed validation", emailType.validate()
        assertErrorsFor emailType, 'maxSize', ['description']
    }

    @Test
    void testFetchByCodeAndWebDisplayable() {
        def emailType  = EmailType.fetchByCodeAndWebDisplayable('BUSI')[0];

        assertEquals 'Business E-Mail', emailType.description
    }




    private def newValidForCreateEmailType() {
        def emailType = new EmailType(
                code: i_success_code,
                description: i_success_description,
                displayWebIndicator: i_success_displayWebIndicator,
                urlIndicator: i_success_urlIndicator,
        )
        return emailType
    }


    private def newInvalidForCreateEmailType() {
        def emailType = new EmailType(
                code: i_failure_code,
                description: i_failure_description,
                displayWebIndicator: i_failure_displayWebIndicator,
                urlIndicator: i_failure_urlIndicator,
        )
        return emailType
    }


}
